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
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class LoadFunderServiceTest {

    private LoadFunderServiceImpl loadFunderService;

    @BeforeEach
    public void setUp() {
        loadFunderService = new LoadFunderServiceImpl();
    }

    @Test
    void givenInputWithOneTrxExceedAmount_SameCustomerId_SameDay_thenAssertTrxIsNotAccepted() {
        String notAcceptedTrxId = "1234";

        List<Output> outputList = loadFunderService.loadFunder(getMockedInputForSameCustomerIdAndSameDay(notAcceptedTrxId));

        assert(outputList.stream().anyMatch(x -> x.getId().equals(notAcceptedTrxId) && !x.isAccepted()));
        assertEquals(2, outputList.stream().filter(Output::isAccepted).count());
    }

    @Test
    void givenInputWithTwoTrxExceedAmount_SameCustomerId_DifferentDays_thenAssertTwoTrxInListAreNotAccepted() {
        String notAcceptedTrxId1 = "20220204";
        String notAcceptedTrxId2 = "20220205";
        String customerId = "123";

        List<Output> outputList = loadFunderService.loadFunder(getMockedInputForSameCustomerIdAndDifferentDay(notAcceptedTrxId1, notAcceptedTrxId2, customerId));

        List<Output> notAccepted = outputList.stream().filter(x -> !x.isAccepted()).collect(Collectors.toList());
        List<Output> accepted = outputList.stream().filter(Output::isAccepted).collect(Collectors.toList());

        assertEquals(2, notAccepted.size());
        assertEquals(notAcceptedTrxId1, notAccepted.get(0).getId());
        assertEquals(notAcceptedTrxId2, notAccepted.get(1).getId());

        assertEquals(6, accepted.size());
        assert(accepted.stream().noneMatch(x -> x.getId().equals(notAcceptedTrxId1) || x.getId().equals(notAcceptedTrxId2)));

        assert(outputList.stream().allMatch(x -> x.getCustomerId().equals(customerId)));
    }

    @Test
    void givenInputWithTwoTrxExceedAmount_DifferentCustomerId_DifferentDays_thenAssertTwoTrxInListAreNotAccepted() {
        String notAcceptedTrxId1 = "20220204";
        String notAcceptedTrxId2 = "20220210";
        String customerId1 = "123";
        String customerId2 = "1234";

        List<Output> outputList = loadFunderService.loadFunder(getMockedInputForDifferentCustomerIdAndDifferentDay(notAcceptedTrxId1, notAcceptedTrxId2, customerId1, customerId2));

        List<Output> notAccepted = outputList.stream().filter(x -> !x.isAccepted()).collect(Collectors.toList());
        List<Output> accepted = outputList.stream().filter(Output::isAccepted).collect(Collectors.toList());

        assertEquals(2, notAccepted.size());

        Output firstNotAcceptedOutput = notAccepted.get(0);
        assertEquals(notAcceptedTrxId1, firstNotAcceptedOutput.getId());
        assertEquals(customerId1, firstNotAcceptedOutput.getCustomerId());

        Output secondNotAcceptedOutput = notAccepted.get(1);
        assertEquals(customerId2, secondNotAcceptedOutput.getCustomerId());
        assertEquals(notAcceptedTrxId2, secondNotAcceptedOutput.getId());

        assertEquals(10, accepted.size());

        assertEquals(12, outputList.size());
    }

    private List<Input> getMockedInputForSameCustomerIdAndSameDay(String notAcceptedTrxId) {
        List<Input> inputList = new ArrayList<>();
        ZonedDateTime time = ZonedDateTime.from(LocalDateTime.now().atZone(ZoneId.of("Z")));

        Input input1 = new Input();
        input1.setCustomerId("123");
        input1.setId("123");
        input1.setLoadAmount("$456.76");
        input1.setTime(time);
        inputList.add(input1);

        Input input2 = new Input();
        input2.setCustomerId("123");
        input2.setId(notAcceptedTrxId);
        input2.setLoadAmount("$45655.76");
        input2.setTime(time);
        inputList.add(input2);

        Input input3 = new Input();
        input3.setCustomerId("123");
        input3.setId("12345");
        input3.setLoadAmount("$455.76");
        input3.setTime(time);
        inputList.add(input3);

        return inputList;
    }

    private List<Input> getMockedInputForSameCustomerIdAndDifferentDay(String notAcceptedTrxId1,
                                                                       String notAcceptedTrxId2,
                                                                       String customerId1) {
        List<Input> inputList = new ArrayList<>();
        Input input0 = new Input();
        input0.setCustomerId(customerId1);
        input0.setId("20220131");
        input0.setLoadAmount("$4999");
        input0.setTime(ZonedDateTime.from(LocalDateTime.of(2022, 1, 31, 20, 20).atZone(ZoneId.of("Z"))));
        inputList.add(input0);

        Input input1 = new Input();
        input1.setCustomerId(customerId1);
        input1.setId("20220201");
        input1.setLoadAmount("$4999");
        input1.setTime(ZonedDateTime.from(LocalDateTime.of(2022, 2, 1, 20, 20).atZone(ZoneId.of("Z"))));
        inputList.add(input1);

        Input input2 = new Input();
        input2.setCustomerId(customerId1);
        input2.setId("20220202");
        input2.setLoadAmount("$4999");
        input2.setTime(ZonedDateTime.from(LocalDateTime.of(2022, 2, 2, 20, 20).atZone(ZoneId.of("Z"))));
        inputList.add(input2);

        Input input3 = new Input();
        input3.setCustomerId(customerId1);
        input3.setId("20220203");
        input3.setLoadAmount("$4999");
        input3.setTime(ZonedDateTime.from(LocalDateTime.of(2022, 2, 3, 20, 20).atZone(ZoneId.of("Z"))));
        inputList.add(input3);

        Input input4 = new Input();
        input4.setCustomerId(customerId1);
        input4.setId(notAcceptedTrxId1);
        input4.setLoadAmount("$4999");
        input4.setTime(ZonedDateTime.from(LocalDateTime.of(2022, 2, 4, 20, 20).atZone(ZoneId.of("Z"))));
        inputList.add(input4);

        Input input5 = new Input();
        input5.setCustomerId(customerId1);
        input5.setId(notAcceptedTrxId2);
        input5.setLoadAmount("$4999");
        input5.setTime(ZonedDateTime.from(LocalDateTime.of(2022, 2, 5, 20, 20).atZone(ZoneId.of("Z"))));
        inputList.add(input5);

        Input input6 = new Input();
        input6.setCustomerId(customerId1);
        input6.setId("20220208");
        input6.setLoadAmount("$4999");
        input6.setTime(ZonedDateTime.from(LocalDateTime.of(2022, 2, 8, 20, 20).atZone(ZoneId.of("Z"))));
        inputList.add(input6);

        Input input7 = new Input();
        input7.setCustomerId(customerId1);
        input7.setId("20220208 - Same Hour");
        input7.setLoadAmount("$1");
        input7.setTime(ZonedDateTime.from(LocalDateTime.of(2022, 2, 8, 20, 20).atZone(ZoneId.of("Z"))));
        inputList.add(input7);

        return inputList;
    }

    private List<Input> getMockedInputForDifferentCustomerIdAndDifferentDay(String notAcceptedTrxId1, String notAcceptedTrxId2,
                                                                            String customerId1, String customerId2) {
        List<Input> inputList = new ArrayList<>();

        Input input0 = new Input();
        input0.setCustomerId(customerId1);
        input0.setId("20220131");
        input0.setLoadAmount("$5000");
        input0.setTime(ZonedDateTime.from(LocalDateTime.of(2022, 1, 31, 20, 20).atZone(ZoneId.of("Z"))));
        inputList.add(input0);

        Input input1 = new Input();
        input1.setCustomerId(customerId1);
        input1.setId("20220201");
        input1.setLoadAmount("$5000");
        input1.setTime(ZonedDateTime.from(LocalDateTime.of(2022, 2, 1, 20, 20).atZone(ZoneId.of("Z"))));
        inputList.add(input1);

        Input input2 = new Input();
        input2.setCustomerId(customerId1);
        input2.setId("20220202");
        input2.setLoadAmount("$5000");
        input2.setTime(ZonedDateTime.from(LocalDateTime.of(2022, 2, 2, 20, 20).atZone(ZoneId.of("Z"))));
        inputList.add(input2);

        Input input3 = new Input();
        input3.setCustomerId(customerId1);
        input3.setId("20220203");
        input3.setLoadAmount("$5000");
        input3.setTime(ZonedDateTime.from(LocalDateTime.of(2022, 2, 3, 20, 20).atZone(ZoneId.of("Z"))));
        inputList.add(input3);

        Input input4 = new Input();
        input4.setCustomerId(customerId2);
        input4.setId("20220204");
        input4.setLoadAmount("$5000");
        input4.setTime(ZonedDateTime.from(LocalDateTime.of(2022, 2, 4, 18, 20).atZone(ZoneId.of("Z"))));
        inputList.add(input4);

        Input input5 = new Input();
        input5.setCustomerId(customerId1);
        input5.setId(notAcceptedTrxId1);
        input5.setLoadAmount("$5000");
        input5.setTime(ZonedDateTime.from(LocalDateTime.of(2022, 2, 4, 20, 20).atZone(ZoneId.of("Z"))));
        inputList.add(input5);

        Input input6 = new Input();
        input6.setCustomerId(customerId2);
        input6.setId("20220205");
        input6.setLoadAmount("$5000");
        input6.setTime(ZonedDateTime.from(LocalDateTime.of(2022, 2, 5, 16, 20).atZone(ZoneId.of("Z"))));
        inputList.add(input6);

        Input input7 = new Input();
        input7.setCustomerId(customerId2);
        input7.setId("20220206");
        input7.setLoadAmount("$5000");
        input7.setTime(ZonedDateTime.from(LocalDateTime.of(2022, 2, 6, 17, 20).atZone(ZoneId.of("Z"))));
        inputList.add(input7);

        Input input8 = new Input();
        input8.setCustomerId(customerId2);
        input8.setId("20220207");
        input8.setLoadAmount("$5000");
        input8.setTime(ZonedDateTime.from(LocalDateTime.of(2022, 2, 7, 20, 20).atZone(ZoneId.of("Z"))));
        inputList.add(input8);

        Input input9 = new Input();
        input9.setCustomerId(customerId2);
        input9.setId("20220208");
        input9.setLoadAmount("$5000");
        input9.setTime(ZonedDateTime.from(LocalDateTime.of(2022, 2, 8, 20, 20).atZone(ZoneId.of("Z"))));
        inputList.add(input9);

        Input input10 = new Input();
        input10.setCustomerId(customerId2);
        input10.setId("20220209");
        input10.setLoadAmount("$5000");
        input10.setTime(ZonedDateTime.from(LocalDateTime.of(2022, 2, 9, 20, 20).atZone(ZoneId.of("Z"))));
        inputList.add(input10);

        Input input11 = new Input();
        input11.setCustomerId(customerId2);
        input11.setId(notAcceptedTrxId2);
        input11.setLoadAmount("$5000");
        input11.setTime(ZonedDateTime.from(LocalDateTime.of(2022, 2, 10, 20, 20).atZone(ZoneId.of("Z"))));
        inputList.add(input11);

        return inputList;
    }
}
