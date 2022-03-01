package com.vault.loadfunder.services.loadfunder;


import com.vault.loadfunder.models.ClientControl;
import com.vault.loadfunder.models.Input;
import com.vault.loadfunder.models.Output;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.temporal.TemporalField;
import java.time.temporal.WeekFields;
import java.util.*;


@Service
public class LoadFunderServiceImpl implements LoadFunderService {

    private final TemporalField weekOfTheYearFormat = WeekFields.of(Locale.getDefault()).weekOfWeekBasedYear();

    public List<Output> loadFunder(List<Input> inputList) {
        List<Output> outputList = new ArrayList<>();
        Map<String, ClientControl> clientControl = new HashMap<>();

        for (Input i : inputList) {
            if (Double.parseDouble(removeSymbol(i.getLoadAmount())) > 5000) {
                addOutput(i, Boolean.FALSE, outputList);
                continue;
            }
            checkPreviousMovementsForClient(clientControl, i, outputList);
        }
        return outputList;
    }

    private void checkPreviousMovementsForClient(Map<String, ClientControl> clientControl, Input actualClient, List<Output> outputList) {
        if (!clientControl.containsKey(actualClient.getCustomerId())) {
            ClientControl newClient = new ClientControl(new BigDecimal(removeSymbol(actualClient.getLoadAmount())),
                    1,
                    Double.parseDouble(removeSymbol(actualClient.getLoadAmount())),
                    actualClient.getTime().getDayOfMonth(),
                    actualClient.getTime().get(weekOfTheYearFormat));
            clientControl.put(actualClient.getCustomerId(), newClient);
            addOutput(actualClient, Boolean.TRUE, outputList);
        } else {
            checkInExistentTransactions(clientControl, actualClient, outputList);
        }
    }

    private void checkInExistentTransactions(Map<String, ClientControl> clientControl, Input actualClient, List<Output> outputList) {
        ClientControl clientInMap = clientControl.get(actualClient.getCustomerId());
        if (actualClient.getTime().getDayOfMonth() == clientInMap.getLastOperationDay()) {
            if (clientInMap.getDayAmount().add(new BigDecimal(removeSymbol(actualClient.getLoadAmount()))).compareTo(new BigDecimal(5000)) > 0 ||
                    clientInMap.getDayLoads() > 3 ||
                    clientInMap.getWeekAmount() + Double.parseDouble(removeSymbol(actualClient.getLoadAmount())) > 20000) {
                addOutput(actualClient, Boolean.FALSE, outputList);
            } else {
                replaceClientInControlMap(clientInMap.getDayAmount().add(getAmount(actualClient.getLoadAmount())),
                        clientInMap.getDayLoads() + 1,
                        clientInMap.getWeekAmount() + Double.parseDouble(removeSymbol(actualClient.getLoadAmount())),
                        actualClient, clientControl);
                addOutput(actualClient, Boolean.TRUE, outputList);
            }
        } else {
            replaceClientInControlMap(getAmount(actualClient.getLoadAmount()), 1,
                    clientInMap.getWeekAmount() + Double.parseDouble(removeSymbol(actualClient.getLoadAmount())),
                    actualClient, clientControl);
            if (actualClient.getTime().get(weekOfTheYearFormat) == clientInMap.getLastOperationWeek()) {
                if (clientInMap.getWeekAmount() + Double.parseDouble(removeSymbol(actualClient.getLoadAmount())) > 20000) {
                    addOutput(actualClient, Boolean.FALSE, outputList);
                } else {
                    replaceClientInControlMap(getAmount(actualClient.getLoadAmount()), 1,
                            clientInMap.getWeekAmount() + Double.parseDouble(removeSymbol(actualClient.getLoadAmount())),
                            actualClient, clientControl);
                    addOutput(actualClient, Boolean.TRUE, outputList);
                }
            } else {
                replaceClientInControlMap(getAmount(actualClient.getLoadAmount()),
                        1, Double.parseDouble(removeSymbol(actualClient.getLoadAmount())),
                        actualClient, clientControl);
                addOutput(actualClient, Boolean.TRUE, outputList);
            }
        }
    }

    private BigDecimal getAmount(String loadAmount) {
        return new BigDecimal(removeSymbol(loadAmount));
    }

    private void replaceClientInControlMap(BigDecimal newDayAmount, int newDayLoad, Double newWeekAmount, Input actualClient, Map<String, ClientControl> clientControl) {
        ClientControl clientActualized = new ClientControl(newDayAmount, newDayLoad, newWeekAmount,
                actualClient.getTime().getDayOfMonth(),
                actualClient.getTime().get(weekOfTheYearFormat));
        clientControl.replace(actualClient.getCustomerId(), clientActualized);
    }

    private void addOutput(Input actualClient, Boolean status, List<Output> outputList) {
        Output output = new Output(actualClient.getId(), actualClient.getCustomerId(), status);
        outputList.add(output);
    }

    private String removeSymbol(String amount) {
        return amount.substring(1);
    }

}
