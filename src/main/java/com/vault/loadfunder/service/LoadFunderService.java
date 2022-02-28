package com.vault.loadfunder.service;

import com.google.gson.*;
import com.vault.loadfunder.models.ClientControl;
import com.vault.loadfunder.models.ClientControl2;
import com.vault.loadfunder.models.Input;
import com.vault.loadfunder.models.Output;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.temporal.TemporalField;
import java.time.temporal.WeekFields;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class LoadFunderService {

    private final TemporalField weekOfTheYearFormat = WeekFields.of(Locale.getDefault()).weekOfWeekBasedYear();

    //TODO llevar esto a otro service
    public List<Input> getInputsFromFile() throws IOException {
        List<Input> inputList = new ArrayList<>();
        try (Stream<String> stream = Files.lines(Paths.get("src", "main", "resources", "input.txt"))) {
            Gson g = getGsonBuilder();
            List<String> linesList = stream.collect(Collectors.toList());
            for (String s : linesList) {
                Input input = g.fromJson(s, Input.class);
                if (inputList.stream().noneMatch(x -> x.getId().equals(input.getId()) && x.getCustomerId().equals(input.getCustomerId()))) { //Removing duplicate transactions id
                    inputList.add(input);
                }
            }
        }
        return inputList;
    }

    public List<Output> loadFunderV2(List<Input> inputList) {
        List<Output> outputList = new ArrayList<>();
        Map<String, ClientControl2> clientControl = new HashMap<>();

        for (Input i : inputList) {

            if (Double.parseDouble(removeSymbol(i.getLoadAmount())) > 5000) {
                Output output = new Output(i.getId(), i.getCustomerId(), Boolean.FALSE);
                outputList.add(output);
                continue;
            }

            if (!clientControl.containsKey(i.getCustomerId())) {
                ClientControl2 newClient = new ClientControl2(Double.parseDouble(removeSymbol(i.getLoadAmount())),
                        1,
                        Double.parseDouble(removeSymbol(i.getLoadAmount())),
                        i.getTime().getDayOfMonth(),
                        i.getTime().get(weekOfTheYearFormat));
                clientControl.put(i.getCustomerId(), newClient);
                Output output = new Output(i.getId(), i.getCustomerId(), Boolean.TRUE);
                outputList.add(output);
            } else {
                ClientControl2 clientInMap = clientControl.get(i.getCustomerId());
                if (i.getTime().getDayOfMonth() == clientInMap.getLastOperationDay()) {
                    if (clientInMap.getDayAmount() + Double.parseDouble(removeSymbol(i.getLoadAmount())) > 5000 ||
                            clientInMap.getDayLoads() > 3 ||
                            clientInMap.getWeekAmount() + Double.parseDouble(removeSymbol(i.getLoadAmount())) > 20000) {
                        Output output = new Output(i.getId(), i.getCustomerId(), Boolean.FALSE);
                        outputList.add(output);
                    } else {
                        ClientControl2 clientActualized = new ClientControl2(clientInMap.getDayAmount() + Double.parseDouble(removeSymbol(i.getLoadAmount())),
                                clientInMap.getDayLoads() + 1,
                                clientInMap.getWeekAmount() + Double.parseDouble(removeSymbol(i.getLoadAmount())),
                                i.getTime().getDayOfMonth(),
                                i.getTime().get(weekOfTheYearFormat));
                        clientControl.replace(i.getCustomerId(), clientActualized);
                        Output output = new Output(i.getId(), i.getCustomerId(), Boolean.TRUE);
                        outputList.add(output);
                    }
                } else {
                    ClientControl2 clientActualized = new ClientControl2(Double.parseDouble(removeSymbol(i.getLoadAmount())),
                            1,
                            clientInMap.getWeekAmount() + Double.parseDouble(removeSymbol(i.getLoadAmount())),
                            i.getTime().getDayOfMonth(),
                            i.getTime().get(weekOfTheYearFormat));
                    clientControl.replace(i.getCustomerId(), clientActualized);

                    if (i.getTime().get(weekOfTheYearFormat) == clientInMap.getLastOperationWeek()) {
                        if (clientInMap.getWeekAmount() + Double.parseDouble(removeSymbol(i.getLoadAmount())) > 20000) {
                            Output output = new Output(i.getId(), i.getCustomerId(), Boolean.FALSE);
                            outputList.add(output);
                        } else {
                            ClientControl2 clientWeekActualized = new ClientControl2(Double.parseDouble(removeSymbol(i.getLoadAmount())),
                                    1,
                                    clientInMap.getWeekAmount() + Double.parseDouble(removeSymbol(i.getLoadAmount())),
                                    i.getTime().getDayOfMonth(),
                                    i.getTime().get(weekOfTheYearFormat));
                            clientControl.replace(i.getCustomerId(), clientWeekActualized);
                            Output output = new Output(i.getId(), i.getCustomerId(), Boolean.TRUE);
                            outputList.add(output);
                        }
                    } else {
                        ClientControl2 clientWeekActualized = new ClientControl2(Double.parseDouble(removeSymbol(i.getLoadAmount())),
                                1,
                                 Double.parseDouble(removeSymbol(i.getLoadAmount())),
                                i.getTime().getDayOfMonth(),
                                i.getTime().get(weekOfTheYearFormat));
                        clientControl.replace(i.getCustomerId(), clientWeekActualized);
                        Output output = new Output(i.getId(), i.getCustomerId(), Boolean.TRUE);
                        outputList.add(output);
                    }
                }
            }
        }
        return outputList;
    }

    public List<Output> loadFunder(List<Input> inputList) {
        List<Output> outputList = new ArrayList<>();
        Map<String, ClientControl> clientControl = new HashMap<>();

        int currentDay = inputList.get(0).getTime().getDayOfMonth();
        int currentWeek = inputList.get(0).getTime().get(weekOfTheYearFormat);

        for (Input i : inputList) {
            if (Double.parseDouble(removeSymbol(i.getLoadAmount())) < 5000) {
                if (currentDay == i.getTime().getDayOfMonth()) {
                    if (clientControl.containsKey(i.getCustomerId())) {
                        ClientControl actualControl = clientControl.get(i.getCustomerId());
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
                            clientControl.replace(i.getCustomerId(), newClientControl);

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
                        clientControl.put(i.getCustomerId(), newClientControl);

                        //Añado a la lista un procesado exitoso
                        Output output = new Output(i.getId(), i.getCustomerId(), Boolean.TRUE);
                        outputList.add(output);
                    }
                } else {
                    //Cambio el dia de corte
                    currentDay = i.getTime().getDayOfMonth();
                    cleanDays(clientControl);

                    if (currentWeek == i.getTime().get(weekOfTheYearFormat)) {
                        if (clientControl.containsKey(i.getCustomerId())) {
                            ClientControl actualControl = clientControl.get(i.getCustomerId());
                            if (actualControl.getWeekAmount() + Double.parseDouble(removeSymbol(i.getLoadAmount())) > 20000) {
                                Output output = new Output(i.getId(), i.getCustomerId(), Boolean.FALSE);
                                outputList.add(output);
                            } else {
                                ClientControl newClientControl = new ClientControl(actualControl.getWeekAmount() + Double.parseDouble(removeSymbol(i.getLoadAmount())));
                                clientControl.replace(i.getCustomerId(), newClientControl);
                                Output output = new Output(i.getId(), i.getCustomerId(), Boolean.TRUE);
                                outputList.add(output);
                            }
                        } else {
                            ClientControl newClientControl = new ClientControl(
                                    Double.parseDouble(removeSymbol(i.getLoadAmount())),
                                    1,
                                    Double.parseDouble(removeSymbol(i.getLoadAmount())));
                            clientControl.put(i.getCustomerId(), newClientControl);
                            Output output = new Output(i.getId(), i.getCustomerId(), Boolean.TRUE);
                            outputList.add(output);
                        }
                    } else {
                        currentWeek = i.getTime().get(weekOfTheYearFormat);
                        ClientControl newClientControl = new ClientControl(
                                Double.parseDouble(removeSymbol(i.getLoadAmount())),
                                1,
                                Double.parseDouble(removeSymbol(i.getLoadAmount())));
                        clientControl.put(i.getCustomerId(), newClientControl);
                        Output output = new Output(i.getId(), i.getCustomerId(), Boolean.TRUE);
                        outputList.add(output);
                    }
                }
            } else {
                Output output = new Output(i.getId(), i.getCustomerId(), Boolean.FALSE);
                outputList.add(output);
            }
        }

        return outputList;
    }

    private void cleanDays(Map<String, ClientControl> clientControl) {
        for (Map.Entry<String, ClientControl> entry : clientControl.entrySet()) {
            ClientControl actualControl = entry.getValue();
            ClientControl newClientControl = new ClientControl(actualControl.getWeekAmount());
            clientControl.replace(entry.getKey(), newClientControl);
        }
    }

    private String removeSymbol(String amount) {
        return amount.substring(1);
    }

    private Gson getGsonBuilder() {
        return new GsonBuilder().registerTypeAdapter(ZonedDateTime.class,
                        (JsonDeserializer<ZonedDateTime>)
                                (json, type, jsonDeserializationContext)
                                        -> ZonedDateTime.parse(json.getAsJsonPrimitive().getAsString()).withZoneSameInstant(ZoneId.of("Z")))
                .create();
    }
}
