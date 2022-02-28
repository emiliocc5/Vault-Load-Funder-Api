package com.vault.loadfunder.services.loadfunder;


import com.vault.loadfunder.models.ClientControl;
import com.vault.loadfunder.models.Input;
import com.vault.loadfunder.models.Output;

import org.springframework.stereotype.Service;

import java.time.temporal.TemporalField;
import java.time.temporal.WeekFields;
import java.util.*;

import static java.util.stream.Collectors.collectingAndThen;
import static java.util.stream.Collectors.toCollection;


@Service
public class LoadFunderServiceImpl implements LoadFunderService {

    private final TemporalField weekOfTheYearFormat = WeekFields.of(Locale.getDefault()).weekOfWeekBasedYear();

    public List<Output> loadFunder(List<Input> inputList) {
        List<Output> outputList = new ArrayList<>();
        Map<String, ClientControl> clientControl = new HashMap<>();

        //List<Input> uniqueList = inputList.stream().collect(collectingAndThen(toCollection(() -> new TreeSet<>(Comparator.comparing(Input :: getId).thenComparing(Input :: getCustomerId))), ArrayList :: new));

        for (Input i : inputList) {
            if (Double.parseDouble(removeSymbol(i.getLoadAmount())) > 5000) {
                Output output = new Output(i.getId(), i.getCustomerId(), Boolean.FALSE);
                outputList.add(output);
                continue;
            }
            checkPreviousMovementsForClient(clientControl, i, outputList);
        }
        return outputList;
    }

    private void checkPreviousMovementsForClient(Map<String, ClientControl> clientControl, Input actualClient, List<Output> outputList) {
        if (!clientControl.containsKey(actualClient.getCustomerId())) {
            ClientControl newClient = new ClientControl(Double.parseDouble(removeSymbol(actualClient.getLoadAmount())),
                    1,
                    Double.parseDouble(removeSymbol(actualClient.getLoadAmount())),
                    actualClient.getTime().getDayOfMonth(),
                    actualClient.getTime().get(weekOfTheYearFormat));
            clientControl.put(actualClient.getCustomerId(), newClient);
            Output output = new Output(actualClient.getId(), actualClient.getCustomerId(), Boolean.TRUE);
            outputList.add(output);
        } else {
            checkInExistentTransactions(clientControl, actualClient, outputList);
        }
    }

    private void checkInExistentTransactions(Map<String, ClientControl> clientControl, Input actualClient, List<Output> outputList) {
        ClientControl clientInMap = clientControl.get(actualClient.getCustomerId());
        if (actualClient.getTime().getDayOfMonth() == clientInMap.getLastOperationDay()) {
            if (clientInMap.getDayAmount() + Double.parseDouble(removeSymbol(actualClient.getLoadAmount())) > 5000 ||
                    clientInMap.getDayLoads() > 3 ||
                    clientInMap.getWeekAmount() + Double.parseDouble(removeSymbol(actualClient.getLoadAmount())) > 20000) {
                Output output = new Output(actualClient.getId(), actualClient.getCustomerId(), Boolean.FALSE);
                outputList.add(output);
            } else {
                ClientControl clientActualized = new ClientControl(clientInMap.getDayAmount() + Double.parseDouble(removeSymbol(actualClient.getLoadAmount())),
                        clientInMap.getDayLoads() + 1,
                        clientInMap.getWeekAmount() + Double.parseDouble(removeSymbol(actualClient.getLoadAmount())),
                        actualClient.getTime().getDayOfMonth(),
                        actualClient.getTime().get(weekOfTheYearFormat));
                clientControl.replace(actualClient.getCustomerId(), clientActualized);
                Output output = new Output(actualClient.getId(), actualClient.getCustomerId(), Boolean.TRUE);
                outputList.add(output);
            }
        } else {
            ClientControl clientActualized = new ClientControl(Double.parseDouble(removeSymbol(actualClient.getLoadAmount())),
                    1,
                    clientInMap.getWeekAmount() + Double.parseDouble(removeSymbol(actualClient.getLoadAmount())),
                    actualClient.getTime().getDayOfMonth(),
                    actualClient.getTime().get(weekOfTheYearFormat));
            clientControl.replace(actualClient.getCustomerId(), clientActualized);

            if (actualClient.getTime().get(weekOfTheYearFormat) == clientInMap.getLastOperationWeek()) {
                if (clientInMap.getWeekAmount() + Double.parseDouble(removeSymbol(actualClient.getLoadAmount())) > 20000) {
                    Output output = new Output(actualClient.getId(), actualClient.getCustomerId(), Boolean.FALSE);
                    outputList.add(output);
                } else {
                    ClientControl clientWeekActualized = new ClientControl(Double.parseDouble(removeSymbol(actualClient.getLoadAmount())),
                            1,
                            clientInMap.getWeekAmount() + Double.parseDouble(removeSymbol(actualClient.getLoadAmount())),
                            actualClient.getTime().getDayOfMonth(),
                            actualClient.getTime().get(weekOfTheYearFormat));
                    clientControl.replace(actualClient.getCustomerId(), clientWeekActualized);
                    Output output = new Output(actualClient.getId(), actualClient.getCustomerId(), Boolean.TRUE);
                    outputList.add(output);
                }
            } else {
                ClientControl clientWeekActualized = new ClientControl(Double.parseDouble(removeSymbol(actualClient.getLoadAmount())),
                        1,
                        Double.parseDouble(removeSymbol(actualClient.getLoadAmount())),
                        actualClient.getTime().getDayOfMonth(),
                        actualClient.getTime().get(weekOfTheYearFormat));
                clientControl.replace(actualClient.getCustomerId(), clientWeekActualized);
                Output output = new Output(actualClient.getId(), actualClient.getCustomerId(), Boolean.TRUE);
                outputList.add(output);
            }
        }
    }

    private String removeSymbol(String amount) {
        return amount.substring(1);
    }

}
