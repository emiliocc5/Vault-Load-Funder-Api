package service;

import com.vault.loadfunder.models.Input;
import com.vault.loadfunder.models.Output;
import com.vault.loadfunder.services.loadfunder.LoadFunderServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;

public class LoadFunderServiceTest {

    private LoadFunderServiceImpl loadFunderService;

    @BeforeEach
    public void setUp() {
        loadFunderService = new LoadFunderServiceImpl();
    }

    @Test
    public void HappyPathTestForSameCustomerAndSameDay() {
        List<Output> outputList = loadFunderService.loadFunder(getMockedInputForSameCustomerIdAndSameDay());
        assert(outputList.stream().anyMatch(x -> x.getId().equals("1234") && !x.isAccepted()));
    }

    @Test
    public void HappyPathTestForSameCustomerAndDifferentDay() {
        loadFunderService.loadFunder(getMockedInputForSameCustomerIdAndDifferentDay());
    }

    @Test
    public void happpyPathTestForDifferentCustomerAndDifferentDay() {
        loadFunderService.loadFunder(getMockedInputForDifferentCustomerIdAndDifferentDay());
    }

    private List<Input> getMockedInputForSameCustomerIdAndDifferentDay() {
        List<Input> inputList = new ArrayList<>();

        Input input0 = new Input();
        input0.setCustomerId("123");
        input0.setId("Dia 31");
        input0.setLoadAmount("$4999");
        input0.setTime(ZonedDateTime.from(LocalDateTime.of(2022, 1, 31, 20, 20)));
        inputList.add(input0);

        Input input1 = new Input();
        input1.setCustomerId("123");
        input1.setId("Dia 1");
        input1.setLoadAmount("$4999");
        input1.setTime(ZonedDateTime.from(LocalDateTime.of(2022, 2, 1, 20, 20)));
        inputList.add(input1);

        Input input2 = new Input();
        input2.setCustomerId("123");
        input2.setId("Dia 2");
        input2.setLoadAmount("$4999");
        input2.setTime(ZonedDateTime.from(LocalDateTime.of(2022, 2, 2, 20, 20)));
        inputList.add(input2);

        Input input3 = new Input();
        input3.setCustomerId("123");
        input3.setId("Dia 3");
        input3.setLoadAmount("$4999");
        input3.setTime(ZonedDateTime.from(LocalDateTime.of(2022, 2, 3, 20, 20)));
        inputList.add(input3);

        Input input4 = new Input();
        input4.setCustomerId("123");
        input4.setId("Dia 4 - NA");
        input4.setLoadAmount("$4999");
        input4.setTime(ZonedDateTime.from(LocalDateTime.of(2022, 2, 4, 20, 20)));
        inputList.add(input4);

        Input input5 = new Input();
        input5.setCustomerId("123");
        input5.setId("Dia 5 - NA");
        input5.setLoadAmount("$4999");
        input5.setTime(ZonedDateTime.from(LocalDateTime.of(2022, 2, 5, 20, 20)));
        inputList.add(input5);

        Input input6 = new Input();
        input6.setCustomerId("123");
        input6.setId("Dia 8");
        input6.setLoadAmount("$4999");
        input6.setTime(ZonedDateTime.from(LocalDateTime.of(2022, 2, 8, 20, 20)));
        inputList.add(input6);

        Input input7 = new Input();
        input7.setCustomerId("123");
        input7.setId("Dia 8 1");
        input7.setLoadAmount("$1");
        input7.setTime(ZonedDateTime.from(LocalDateTime.of(2022, 2, 8, 20, 20)));
        inputList.add(input7);

        return inputList;
    }


    private List<Input> getMockedInputForSameCustomerIdAndSameDay() {
        List<Input> inputList = new ArrayList<>();
        Input input1 = new Input();
        input1.setCustomerId("123");
        input1.setId("123");
        input1.setLoadAmount("$456.76");
        input1.setTime(ZonedDateTime.from(LocalDateTime.now()));
        inputList.add(input1);

        Input input2 = new Input();
        input2.setCustomerId("123");
        input2.setId("1234");
        input2.setLoadAmount("$45655.76");
        input2.setTime(ZonedDateTime.from(LocalDateTime.now()));
        inputList.add(input2);

        Input input3 = new Input();
        input3.setCustomerId("123");
        input3.setId("12345");
        input3.setLoadAmount("$455.76");
        input3.setTime(ZonedDateTime.from(LocalDateTime.now()));
        inputList.add(input3);

        return inputList;
    }

    private List<Input> getMockedInputForDifferentCustomerIdAndDifferentDay() {
        List<Input> inputList = new ArrayList<>();

        Input input0 = new Input();
        input0.setCustomerId("123");
        input0.setId("Dia 31");
        input0.setLoadAmount("$4999");
        input0.setTime(ZonedDateTime.from(LocalDateTime.of(2022, 1, 31, 20, 20)));
        inputList.add(input0);

        Input input1 = new Input();
        input1.setCustomerId("123");
        input1.setId("Dia 1");
        input1.setLoadAmount("$4999");
        input1.setTime(ZonedDateTime.from(LocalDateTime.of(2022, 2, 1, 20, 20)));
        inputList.add(input1);

        Input input2 = new Input();
        input2.setCustomerId("1234");
        input2.setId("Dia 2");
        input2.setLoadAmount("$4999");
        input2.setTime(ZonedDateTime.from(LocalDateTime.of(2022, 2, 2, 20, 20)));
        inputList.add(input2);

        Input input3 = new Input();
        input3.setCustomerId("123");
        input3.setId("Dia 3");
        input3.setLoadAmount("$4999");
        input3.setTime(ZonedDateTime.from(LocalDateTime.of(2022, 2, 3, 20, 20)));
        inputList.add(input3);

        Input input4 = new Input();
        input4.setCustomerId("123");
        input4.setId("Dia 4 - NA");
        input4.setLoadAmount("$4999");
        input4.setTime(ZonedDateTime.from(LocalDateTime.of(2022, 2, 4, 20, 20)));
        inputList.add(input4);

        Input input5 = new Input();
        input5.setCustomerId("123");
        input5.setId("Dia 5 - NA");
        input5.setLoadAmount("$4999");
        input5.setTime(ZonedDateTime.from(LocalDateTime.of(2022, 2, 5, 20, 20)));
        inputList.add(input5);

        Input input6 = new Input();
        input6.setCustomerId("1234");
        input6.setId("Dia 8");
        input6.setLoadAmount("$4999");
        input6.setTime(ZonedDateTime.from(LocalDateTime.of(2022, 2, 8, 20, 20)));
        inputList.add(input6);

        Input input7 = new Input();
        input7.setCustomerId("123");
        input7.setId("Dia 8 1");
        input7.setLoadAmount("$1");
        input7.setTime(ZonedDateTime.from(LocalDateTime.of(2022, 2, 8, 20, 20)));
        inputList.add(input7);

        return inputList;
    }

    @Test
    public void testRealCaseFailed() {
        List<Output> outputList = loadFunderService.loadFunder(getMockedInputForSameCustomerIdAnd2Days());
        for (Output o : outputList) {
            System.out.println(o);
        }
    }

    private List<Input> getMockedInputForSameCustomerIdAnd2Days() {
        List<Input> inputList = new ArrayList<>();
        Input input1 = new Input();
        input1.setCustomerId("528");
        input1.setId("15887 - V");
        input1.setLoadAmount("$3318.47");
        input1.setTime(ZonedDateTime.from(LocalDateTime.of(2000, 1, 1, 0, 0, 0).atZone(ZoneId.of("Z"))));
        inputList.add(input1);

        Input input2 = new Input();
        input2.setCustomerId("528");
        input2.setId("11429 - F");
        input2.setLoadAmount("$2253.56");
        input2.setTime(ZonedDateTime.from(LocalDateTime.of(2000, 1, 1, 11, 15, 2).atZone(ZoneId.of("Z"))));
        inputList.add(input2);

        Input input4 = new Input();
        input4.setCustomerId("35");
        input4.setId("9718");
        input4.setLoadAmount("$2254.47");
        input4.setTime(ZonedDateTime.from(LocalDateTime.of(2000, 1, 2, 0, 32, 46).atZone(ZoneId.of("Z"))));
        inputList.add(input4);

        Input input3 = new Input();
        input3.setCustomerId("528");
        input3.setId("22052 - V");
        input3.setLoadAmount("$3171.75");
        input3.setTime(ZonedDateTime.from(LocalDateTime.of(2000, 1, 2, 19, 58, 46).atZone(ZoneId.of("Z"))));
        inputList.add(input3);

        return inputList;
    }
}
