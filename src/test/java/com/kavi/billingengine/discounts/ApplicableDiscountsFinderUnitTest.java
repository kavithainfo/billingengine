package com.kavi.billingengine.discounts;

import com.google.common.collect.ImmutableMap;
import com.kavi.billingengine.discounts.rule.AgeRuleProcessor;
import com.kavi.billingengine.discounts.rule.RuleProcessor;
import com.kavi.billingengine.domain.Discount;
import com.kavi.billingengine.domain.Service;
import org.junit.Test;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import static com.kavi.billingengine.domain.ServiceType.*;
import static java.lang.Boolean.FALSE;
import static java.math.BigDecimal.ZERO;
import static java.util.Arrays.asList;
import static java.util.Collections.*;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class ApplicableDiscountsFinderUnitTest {

    private final RuleProcessor ageRuleProcessor = mock(AgeRuleProcessor.class);
    private final ApplicableDiscountsFinder applicableDiscountsFinder = new ApplicableDiscountsFinder(asList(ageRuleProcessor));

    @Test
    public void shouldReturnEmptyIfNoServiceIsRequested() {
        final Map<Service, List<Discount>> applicableDiscounts =
                applicableDiscountsFinder.find(emptyList(), 30, FALSE, FALSE);

        assertThat(applicableDiscounts, is(emptyMap()));
    }

    @Test
    public void shouldFindApplicableDiscountsForServices() {
        final Service diagnosis = new Service(DIAGNOSIS, 1, new BigDecimal("320.50"), ZERO);
        final Service xray = new Service(XRAY, 2, new BigDecimal("150.00"), ZERO);
        final Service bloodTest = new Service(BLOOD_TEST, 1, new BigDecimal("400.45"), ZERO);

        final List<Service> services = asList(diagnosis, xray, bloodTest);

        final Discount discount = new Discount("60% off", 60);
        final Discount additionalDiscount = new Discount("15% off", 15);

        final List<Discount> discounts = singletonList(discount);
        final List<Discount> bloodTestDiscounts = asList(discount, additionalDiscount);

        when(ageRuleProcessor.process(diagnosis, 69, FALSE, FALSE)).thenReturn(discounts);
        when(ageRuleProcessor.process(xray, 69, FALSE, FALSE)).thenReturn(discounts);
        when(ageRuleProcessor.process(bloodTest, 69, FALSE, FALSE)).thenReturn(bloodTestDiscounts);

        final Map<Service, List<Discount>> applicableDiscounts =
                applicableDiscountsFinder.find(services, 69, FALSE, FALSE);

        final Map<Service, List<Discount>> expectedApplicableDiscounts =
                new ImmutableMap.Builder<Service, List<Discount>>()
                        .put(diagnosis, discounts)
                        .put(xray, discounts)
                        .put(bloodTest, bloodTestDiscounts)
                        .build();

        assertThat(applicableDiscounts, is(expectedApplicableDiscounts));
    }

}