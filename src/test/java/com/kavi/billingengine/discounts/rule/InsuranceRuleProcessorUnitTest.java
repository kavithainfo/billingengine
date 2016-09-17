package com.kavi.billingengine.discounts.rule;

import com.kavi.billingengine.domain.Discount;
import com.kavi.billingengine.domain.Service;

import org.junit.Test;

import java.math.BigDecimal;
import java.util.List;

import static com.kavi.billingengine.domain.ServiceType.BLOOD_TEST;
import static com.kavi.billingengine.domain.ServiceType.ECG;
import static java.lang.Boolean.FALSE;
import static java.lang.Boolean.TRUE;
import static java.math.BigDecimal.ZERO;
import static java.util.Collections.emptyList;
import static java.util.Collections.singletonList;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

public class InsuranceRuleProcessorUnitTest {

    private final InsuranceRuleProcessor insuranceRuleProcessor = new InsuranceRuleProcessor();

    @Test
    public void shouldReturn15PercentDiscountForPatientsWithMediInsuranceForBloodTestWhenDiagnosedByMediPractitioner() {
        final Service bloodTest = new Service(BLOOD_TEST, 1, new BigDecimal("78.00"), ZERO);

        final List<Discount> discounts = insuranceRuleProcessor.process(bloodTest, 50, TRUE, TRUE);

        final Discount expectedDiscount = new Discount("15% off", 15);

        assertThat(discounts, is(singletonList(expectedDiscount)));
    }

    @Test
    public void shouldReturnEmptyForPatientsWithNoMedicalInsurance() {
        final Service bloodTest = new Service(BLOOD_TEST, 1, new BigDecimal("78.00"), ZERO);

        final List<Discount> discounts = insuranceRuleProcessor.process(bloodTest, 2, FALSE, TRUE);

        assertThat(discounts, is(emptyList()));
    }

    @Test
    public void shouldReturnEmptyForPatientsNotDiagnosedByMediHealthPractitioner() {
        final Service bloodTest = new Service(BLOOD_TEST, 1, new BigDecimal("78.00"), ZERO);

        final List<Discount> discounts = insuranceRuleProcessor.process(bloodTest, 80, TRUE, FALSE);

        assertThat(discounts, is(emptyList()));
    }

    @Test
    public void shouldReturnEmptyForServicesOtherThanBloodTest() {
        final Service ecg = new Service(ECG, 1, new BigDecimal("130.00"), ZERO);

        final List<Discount> discounts = insuranceRuleProcessor.process(ecg, 34, TRUE, TRUE);

        assertThat(discounts, is(emptyList()));
    }
}