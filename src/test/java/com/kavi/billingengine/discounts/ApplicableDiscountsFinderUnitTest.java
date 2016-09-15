package com.kavi.billingengine.discounts;

import com.google.common.collect.ImmutableMap;
import com.kavi.billingengine.domain.Discount;
import com.kavi.billingengine.domain.Service;

import org.junit.Test;

import java.math.BigDecimal;
import java.util.Map;
import java.util.Optional;

import static com.kavi.billingengine.domain.ServiceType.*;
import static java.lang.Boolean.FALSE;
import static java.lang.Boolean.TRUE;
import static java.math.BigDecimal.ZERO;

import static java.util.Arrays.asList;
import static java.util.Collections.*;
import static java.util.Optional.empty;
import static java.util.Optional.of;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

public class ApplicableDiscountsFinderUnitTest {

    private final ApplicableDiscountsFinder applicableDiscountsFinder = new ApplicableDiscountsFinder();

    @Test
    public void shouldReturnEmptyIfNoServiceRequested() {
        final Map<Service, Optional<Discount>> applicableDiscounts = applicableDiscountsFinder.findApplicableDiscounts(
                emptyList(),
                30,
                FALSE,
                FALSE
        );

        assertThat(applicableDiscounts, is(emptyMap()));
    }

    @Test
    public void shouldReturn40PercentDiscountForChildrenUnder5() {
        final Service service = new Service(DIAGNOSIS, 1, new BigDecimal("55"), ZERO);

        final Map<Service, Optional<Discount>> applicableDiscounts = applicableDiscountsFinder.findApplicableDiscounts(
                singletonList(service),
                3,
                FALSE,
                FALSE
        );

        final Map<Service, Optional<Discount>> expectedApplicableDiscounts = singletonMap(
                service,
                of(new Discount("40% off", 40))
        );

        assertThat(applicableDiscounts, is(expectedApplicableDiscounts));
    }

    @Test
    public void shouldReturn60PercentDiscountForSeniorCitizens() {
        final Service diagnosis = new Service(DIAGNOSIS, 1, new BigDecimal("55"), ZERO);
        final Service ecg = new Service(ECG, 1, new BigDecimal("467"), ZERO);

        final Map<Service, Optional<Discount>> applicableDiscounts = applicableDiscountsFinder.findApplicableDiscounts(
                asList(diagnosis, ecg),
                65,
                FALSE,
                FALSE
        );

        final Optional<Discount> discount = of(new Discount("60% off", 60));
        final Map<Service, Optional<Discount>> expectedApplicableDiscounts =
                new ImmutableMap.Builder<Service, Optional<Discount>>()
                        .put(diagnosis, discount)
                        .put(ecg, discount)
                        .build();

        assertThat(applicableDiscounts, is(expectedApplicableDiscounts));
    }

    @Test
    public void shouldReturn70PercentDiscountForSeniorMostCitizens() {
        final Service xray = new Service(XRAY, 1, new BigDecimal("895"), ZERO);

        final Map<Service, Optional<Discount>> applicableDiscounts = applicableDiscountsFinder.findApplicableDiscounts(
                singletonList(xray),
                90,
                FALSE,
                FALSE
        );

        final Optional<Discount> discount = of(new Discount("90% off", 90));
        final Map<Service, Optional<Discount>> expectedApplicableDiscounts = singletonMap(xray, discount);

        assertThat(applicableDiscounts, is(expectedApplicableDiscounts));
    }

    @Test
    public void shouldReturnNoDiscountForPatientsAgedBetween5And64() {
        final Service vaccine = new Service(VACCINE, 1, new BigDecimal("695"), ZERO);

        final Map<Service, Optional<Discount>> applicableDiscounts = applicableDiscountsFinder.findApplicableDiscounts(
                singletonList(vaccine),
                35,
                FALSE,
                FALSE
        );

        final Map<Service, Optional<Discount>> expectedApplicableDiscounts = singletonMap(vaccine, empty());

        assertThat(applicableDiscounts, is(expectedApplicableDiscounts));
    }

    @Test
    public void shouldReturnAdditionalDiscountForBloodTestForPatientsWithInsuranceAndPreviouslyDiagnosed() {
        final Service bloodTest = new Service(BLODD_TEST, 1, new BigDecimal("89"), ZERO);
        final Service ecg = new Service(ECG, 1, new BigDecimal("467"), ZERO);
        final Service xray = new Service(XRAY, 1, new BigDecimal("895"), ZERO);

        final Map<Service, Optional<Discount>> applicableDiscounts = applicableDiscountsFinder.findApplicableDiscounts(
                asList(bloodTest, ecg, xray),
                89,
                TRUE,
                TRUE
        );
        // TODO
    }

    @Test
    public void shouldNotReturnAdditionalDiscountForBloodTestWithoutInsuranceOrDiagnosis() {
        final Service bloodTest = new Service(BLODD_TEST, 1, new BigDecimal("89"), ZERO);
        final Service ecg = new Service(ECG, 1, new BigDecimal("467"), ZERO);
        final Service xray = new Service(XRAY, 1, new BigDecimal("895"), ZERO);

        final Map<Service, Optional<Discount>> applicableDiscounts = applicableDiscountsFinder.findApplicableDiscounts(
                asList(bloodTest, ecg, xray),
                89,
                TRUE,
                FALSE
        );
        // TODO
    }
}