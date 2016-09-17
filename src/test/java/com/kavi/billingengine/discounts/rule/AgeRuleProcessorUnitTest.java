package com.kavi.billingengine.discounts.rule;

import com.kavi.billingengine.domain.Discount;
import com.kavi.billingengine.domain.Service;
import org.junit.Test;

import java.math.BigDecimal;
import java.util.List;

import static com.kavi.billingengine.domain.ServiceType.DIAGNOSIS;
import static java.lang.Boolean.FALSE;
import static java.math.BigDecimal.ZERO;
import static java.util.Collections.emptyList;
import static java.util.Collections.singletonList;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

public class AgeRuleProcessorUnitTest {

    private final AgeRuleProcessor ageRuleProcessor = new AgeRuleProcessor();
    private final Service service = new Service(DIAGNOSIS, 1, new BigDecimal("38.00"), ZERO);

    @Test
    public void shouldReturnNoDiscountForPatientsAgedBetween5And64() {
        final List<Discount> discounts = ageRuleProcessor.process(service, 35, FALSE, FALSE);

        assertThat(discounts, is(emptyList()));
    }

    @Test
    public void shouldReturn40PercentDiscountForChildrenUnder5() {
        final List<Discount> discounts = ageRuleProcessor.process(service, 3, FALSE, FALSE);

        final Discount expectedDiscount = new Discount("40% off", 40);

        assertThat(discounts, is(singletonList(expectedDiscount)));
    }

    @Test
    public void shouldReturn60PercentDiscountForSeniorCitizensAgedBetween65And70() {
        final List<Discount> discounts = ageRuleProcessor.process(service, 65, FALSE, FALSE);

        final Discount expectedDiscount = new Discount("60% off", 60);

        assertThat(discounts, is(singletonList(expectedDiscount)));
    }

    @Test
    public void shouldReturn90PercentDiscountForSeniorCitizensAgedOver70() {
        final List<Discount> discounts = ageRuleProcessor.process(service, 89, FALSE, FALSE);

        final Discount expectedDiscount = new Discount("90% off", 90);

        assertThat(discounts, is(singletonList(expectedDiscount)));
    }

    @Test
    public void shouldReturnEmptyForInvalidAge() {
        final List<Discount> discounts = ageRuleProcessor.process(service, -10, FALSE, FALSE);

        assertThat(discounts, is(emptyList()));
    }

    @Test
    public void shouldReturnEmptyForNonRealisticAge() {
        final List<Discount> discounts = ageRuleProcessor.process(service, 170, FALSE, FALSE);

        assertThat(discounts, is(emptyList()));
    }
}