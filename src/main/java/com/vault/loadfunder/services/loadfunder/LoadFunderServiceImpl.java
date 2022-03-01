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

    private static final BigDecimal DAILY_MAX = new BigDecimal(5000);
    private static final BigDecimal WEEKLY_MAX = new BigDecimal(20000);
    private static final int DAILY_LOAD_MAX = 3;

    private final TemporalField weekOfTheYearFormat = WeekFields.of(Locale.getDefault()).weekOfWeekBasedYear();

    public List<Output> loadFunder(List<Input> inputList) {
        List<Output> outputList = new ArrayList<>();
        Map<String, ClientControl> clientControl = new HashMap<>();

        for (Input i : inputList) {
            if (getLoadAmount(i).compareTo(DAILY_MAX) > 0) {
                addOutput(i, Boolean.FALSE, outputList);
                continue;
            }
            checkPreviousMovementsForClient(clientControl, i, outputList);
        }
        return outputList;
    }

    private void checkPreviousMovementsForClient(Map<String, ClientControl> clientControl, Input actualClient, List<Output> outputList) {
        if (!clientControl.containsKey(actualClient.getCustomerId())) {
            ClientControl newClient = new ClientControl(getLoadAmount(actualClient),
                    1,
                    getLoadAmount(actualClient),
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
            checkDayAndWeekControls(clientInMap, actualClient, clientControl, outputList);
        } else {
            replaceClientInControlMap(getLoadAmount(actualClient), 1,
                    clientInMap.getWeekAmount().add(getLoadAmount(actualClient)),
                    actualClient, clientControl);
            if (actualClient.getTime().get(weekOfTheYearFormat) == clientInMap.getLastOperationWeek()) {
                checkWeekControl(clientInMap, actualClient, clientControl, outputList);
            } else {
                replaceClientInControlMap(getLoadAmount(actualClient),
                        1, getLoadAmount(actualClient),
                        actualClient, clientControl);
                addOutput(actualClient, Boolean.TRUE, outputList);
            }
        }
    }

    private void checkDayAndWeekControls(ClientControl clientInMap, Input actualClient, Map<String, ClientControl> clientControl, List<Output> outputList) {
        if (clientInMap.getDayAmount().add(getLoadAmount(actualClient)).compareTo(DAILY_MAX) > 0 ||
                clientInMap.getDayLoads() > DAILY_LOAD_MAX ||
                clientInMap.getWeekAmount().add(getLoadAmount(actualClient)).compareTo(WEEKLY_MAX) > 0) {
            addOutput(actualClient, Boolean.FALSE, outputList);
        } else {
            replaceClientInControlMap(clientInMap.getDayAmount().add(getLoadAmount(actualClient)),
                    clientInMap.getDayLoads() + 1,
                    clientInMap.getWeekAmount().add(getLoadAmount(actualClient)),
                    actualClient, clientControl);
            addOutput(actualClient, Boolean.TRUE, outputList);
        }
    }

    private void checkWeekControl(ClientControl clientInMap, Input actualClient, Map<String, ClientControl> clientControl, List<Output> outputList){
        if (clientInMap.getWeekAmount().add(getLoadAmount(actualClient)).compareTo(WEEKLY_MAX) > 0) {
            addOutput(actualClient, Boolean.FALSE, outputList);
        } else {
            replaceClientInControlMap(getLoadAmount(actualClient), 1,
                    clientInMap.getWeekAmount().add(getLoadAmount(actualClient)),
                    actualClient, clientControl);
            addOutput(actualClient, Boolean.TRUE, outputList);
        }
    }

    private BigDecimal getLoadAmount(Input actualClient) {
        return new BigDecimal(removeSymbol(actualClient.getLoadAmount()));
    }

    private void replaceClientInControlMap(BigDecimal newDayAmount, int newDayLoad, BigDecimal newWeekAmount, Input actualClient, Map<String, ClientControl> clientControl) {
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
        if (amount != null && amount.contains("$")) {
            amount = amount.replace("$", "");
        }
        return amount;
    }

}
