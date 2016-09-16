package com.kavi.billingengine.billing;

import com.google.common.collect.ImmutableMap;
import com.kavi.billingengine.discounts.ApplicableDiscountsFinder;
import com.kavi.billingengine.discounts.DiscountApplicator;
import com.kavi.billingengine.domain.*;
import org.junit.Test;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static com.kavi.billingengine.domain.ServiceType.*;
import static java.lang.Boolean.FALSE;
import static java.lang.Boolean.TRUE;
import static java.math.BigDecimal.ZERO;
import static java.util.Arrays.asList;
import static java.util.Optional.empty;
import static java.util.Optional.of;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class BillingServiceUnitTest {

    private final ApplicableDiscountsFinder applicableDiscountsFinder = mock(ApplicableDiscountsFinder.class);
    private final DiscountApplicator discountApplicator = mock(DiscountApplicator.class);

    private final BillingService billingService = new BillingService(applicableDiscountsFinder, discountApplicator);

    @Test
    public void shouldChargeRequestedServices() {
        final BigDecimal diagnosisDefaultPrice = new BigDecimal("60.00");
        final BigDecimal ecgDefaultPrice = new BigDecimal("200.40");
        final BigDecimal vaccineDefaultPrice = new BigDecimal("27.50");
        final BigDecimal vaccineServiceCharge = new BigDecimal("15.00");

        final Service diagnosis = new Service(DIAGNOSIS, 1, diagnosisDefaultPrice, ZERO);
        final Service ecg = new Service(ECG, 1, ecgDefaultPrice, ZERO);
        final Service vaccine = new Service(VACCINE, 4, vaccineDefaultPrice, vaccineServiceCharge);

        final List<Service> requestedServices = asList(diagnosis, ecg, vaccine);
        final BillingRequest billingRequest = new BillingRequest(
                requestedServices,
                17,
                FALSE,
                FALSE
        );

        // mock
        final Map<Service, Optional<Discount>> applicableDiscounts =
                new ImmutableMap.Builder<Service, Optional<Discount>>()
                        .put(diagnosis, empty())
                        .put(ecg, empty())
                        .put(vaccine, empty())
                        .build();

        when(applicableDiscountsFinder.findApplicableDiscounts(requestedServices, 17, FALSE, FALSE))
                .thenReturn(applicableDiscounts);

        final PricedService pricedDiagnosis = new PricedService(
                DIAGNOSIS, 1, diagnosisDefaultPrice, ZERO, diagnosisDefaultPrice, empty());
        final PricedService pricedEcg = new PricedService(
                ECG, 1, ecgDefaultPrice, ZERO, ecgDefaultPrice, empty());
        final PricedService pricedVaccine = new PricedService(
                VACCINE, 4, vaccineDefaultPrice, vaccineServiceCharge, vaccineDefaultPrice, empty());

        final List<PricedService> expectedPricedServices = asList(pricedDiagnosis, pricedEcg, pricedVaccine);

        when(discountApplicator.apply(applicableDiscounts)).thenReturn(expectedPricedServices);

        // function call
        final List<PricedService> pricedServices = billingService.chargeMedicalServices(billingRequest);

        // assertions
        assertThat(pricedServices, is(expectedPricedServices));
    }

    @Test
    public void shouldChargeRequestedServicesWithDiscount() {
        final BigDecimal bloodTestDefaultPrice = new BigDecimal("78.00");
        final BigDecimal vaccineDefaultPrice = new BigDecimal("27.50");
        final BigDecimal vaccineServiceCharge = new BigDecimal("15.00");

        final Service bloodTest = new Service(BLODD_TEST, 2, bloodTestDefaultPrice, ZERO);
        final Service vaccine = new Service(VACCINE, 4, vaccineDefaultPrice, vaccineServiceCharge);

        final List<Service> requestedServices = asList(bloodTest, vaccine);
        final BillingRequest billingRequest = new BillingRequest(
                requestedServices,
                89,
                TRUE,
                TRUE
        );

        // mock
        final Optional<Discount> discount = of(new Discount("90% off", 90));
        final Map<Service, Optional<Discount>> applicableDiscounts =
                new ImmutableMap.Builder<Service, Optional<Discount>>()
                        .put(bloodTest, discount)
                        .put(vaccine, discount)
                        .build();

        when(applicableDiscountsFinder.findApplicableDiscounts(requestedServices, 89, TRUE, TRUE))
                .thenReturn(applicableDiscounts);

        final PricedService pricedBloodTest = new PricedService(
                BLODD_TEST, 2, bloodTestDefaultPrice, ZERO, new BigDecimal("15.60"), discount);
        final PricedService pricedVaccine = new PricedService(
                VACCINE, 4, vaccineDefaultPrice, vaccineServiceCharge, new BigDecimal("8.75"), discount);

        final List<PricedService> expectedPricedServices = asList(pricedBloodTest, pricedVaccine);

        when(discountApplicator.apply(applicableDiscounts)).thenReturn(expectedPricedServices);

        // function call
        final List<PricedService> pricedServices = billingService.chargeMedicalServices(billingRequest);

        // assertions
        assertThat(pricedServices, is(expectedPricedServices));
    }
}