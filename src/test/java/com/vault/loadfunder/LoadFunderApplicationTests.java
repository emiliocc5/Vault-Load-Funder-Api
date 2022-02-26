package com.vault.loadfunder;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.ParseException;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
class LoadFunderApplicationTests {

	@Test
	void contextLoads() {
	}

	@Test
	public void test() throws ParseException {
		DecimalFormat format = new DecimalFormat("\u00A4####.00");
		format.setParseBigDecimal(true);

		BigDecimal decimal = (BigDecimal) format.parse("$1000.57");
		System.out.println(decimal);

	}

}
