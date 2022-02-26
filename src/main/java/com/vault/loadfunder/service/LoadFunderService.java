package com.vault.loadfunder.service;

import com.google.gson.*;
import com.vault.loadfunder.models.ClientControl;
import com.vault.loadfunder.models.Input;
import com.vault.loadfunder.models.Output;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.ZonedDateTime;
import java.time.temporal.TemporalField;
import java.time.temporal.WeekFields;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class LoadFunderService {

    private final TemporalField weekOfTheYearFormat = WeekFields.of(Locale.getDefault()).weekOfWeekBasedYear();

    public List<Input> getInputsFromFile() throws IOException {
        List<Input> inputList = new ArrayList<>();
        try (Stream<String> stream = Files.lines(Paths.get("src", "main", "resources", "input.txt"))) {
            Gson g = getGsonBuilder();
            List<String> linesList = stream.collect(Collectors.toList());
            for (String s : linesList) {
                Input input = g.fromJson(s, Input.class);
                if (inputList.stream().noneMatch(x -> x.getId().equals(input.getId()) && x.getCustomerId().equals(input.getCustomerId()))) { //Removing duplicate transactions id
                    inputList.add(input);
                } /*else {
                    Output output = new Output(input.getId(), input.getCustomerId(), Boolean.FALSE);
                    outputList.add(output);  //Adding duplicate transactions to false output.
                }*/
            }
        }
        return inputList;
    }

    public void loadFunder(List<Input> inputList) {
        List<Output> outputList = new ArrayList<>();

        Map<String, ClientControl> clientControl = new HashMap<>();

        //Agarro el dia de la primer transaccion de la lista.
        int currentDay = inputList.get(0).getTime().getDayOfMonth();

        //Agarro la semana de la primer transaccion de la lista.
        int currentWeek = inputList.get(0).getTime().get(weekOfTheYearFormat);

        for (Input i : inputList) {
            //Si el id del cliente esta en el mapa y el dia de la trx es el mismo que el de la variable de corte
            //Agarro el monto diario y le sumo
            if (currentDay == i.getTime().getDayOfMonth()) {
                if (clientControl.containsKey(i.getId())) {
                    ClientControl actualControl = clientControl.get(i.getId());
                    if (actualControl.getDayAmount() + Double.parseDouble(removeSymbol(i.getLoadAmount())) > 5000 ||
                            actualControl.getDayLoads() > 3 ||
                            actualControl.getWeekAmount() + Double.parseDouble(removeSymbol(i.getLoadAmount())) > 20000) {
                        Output output = new Output(i.getId(), i.getCustomerId(), Boolean.FALSE);
                        outputList.add(output);
                    } else {
                        //Armo ClientControl con los valores actualizados
                        ClientControl newClientControl = new ClientControl(
                                actualControl.getDayAmount() + Double.parseDouble(removeSymbol(i.getLoadAmount())),
                                actualControl.getDayLoads() + 1,
                                actualControl.getWeekAmount() + Double.parseDouble(removeSymbol(i.getLoadAmount())));
                        //Reemplazo en el mapa
                        clientControl.replace(i.getId(), newClientControl);

                        //Añado a la lista un procesado exitoso
                        Output output = new Output(i.getId(), i.getCustomerId(), Boolean.TRUE);
                        outputList.add(output);
                    }
                } else {
                    //Armo ClientControl con los valores
                    ClientControl newClientControl = new ClientControl(
                            Double.parseDouble(removeSymbol(i.getLoadAmount())),
                            1,
                            Double.parseDouble(removeSymbol(i.getLoadAmount())));

                    //Inserto en el mapa
                    clientControl.put(i.getId(), newClientControl);

                    //Añado a la lista un procesado exitoso
                    Output output = new Output(i.getId(), i.getCustomerId(), Boolean.TRUE);
                    outputList.add(output);
                }
            } else {
                //Cambio el dia de corte
                currentDay = i.getTime().getDayOfMonth();

                    /*//Reseteo valores para el nuevo dia
                    if (clientControl.containsKey(i.getId())) {
                        ClientControl actualControl = clientControl.get(i.getId());
                        ClientControl restartedDayControl = new ClientControl(actualControl.getWeekAmount());
                        clientControl.replace(i.getId(), restartedDayControl);

                    }*/

                if (currentWeek == i.getTime().get(weekOfTheYearFormat)) {
                    if (clientControl.containsKey(i.getId())) {
                        ClientControl actualControl = clientControl.get(i.getId());
                        if (actualControl.getWeekAmount() + Double.parseDouble(removeSymbol(i.getLoadAmount())) > 20000) {
                            Output output = new Output(i.getId(), i.getCustomerId(), Boolean.FALSE);
                            outputList.add(output);
                        } else {
                            ClientControl newClientControl = new ClientControl(actualControl.getWeekAmount());
                            clientControl.replace(i.getId(), newClientControl);
                            Output output = new Output(i.getId(), i.getCustomerId(), Boolean.TRUE);
                            outputList.add(output);
                        }
                    } else {
                        ClientControl newClientControl = new ClientControl(
                                Double.parseDouble(removeSymbol(i.getLoadAmount())),
                                1,
                                Double.parseDouble(removeSymbol(i.getLoadAmount())));
                        clientControl.put(i.getId(), newClientControl);
                        Output output = new Output(i.getId(), i.getCustomerId(), Boolean.TRUE);
                        outputList.add(output);
                    }
                } else {
                    currentWeek = i.getTime().get(weekOfTheYearFormat);
                }
            }
        }

        for (Output o : outputList) {
            System.out.println(o);
        }
    }


    private String removeSymbol(String amount) {
        return amount.substring(1);
    }

    private Gson getGsonBuilder() {
        return new GsonBuilder().registerTypeAdapter(LocalDateTime.class,
                        (JsonDeserializer<LocalDateTime>)
                                (json, type, jsonDeserializationContext)
                                        -> ZonedDateTime.parse(json.getAsJsonPrimitive().getAsString()).toLocalDateTime())
                .create();
    }
}
