package com.kavi.billingengine.billing;

import com.google.common.collect.ImmutableMap;
import com.kavi.billingengine.discounts.ApplicableDiscountsFinder;
import com.kavi.billingengine.discounts.DiscountApplicator;
import com.kavi.billingengine.domain.BillingRequest;
import com.kavi.billingengine.domain.Discount;
import com.kavi.billingengine.domain.PricedService;
import com.kavi.billingengine.domain.Service;
import org.junit.Test;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import static com.kavi.billingengine.domain.ServiceType.*;
import static java.lang.Boolean.FALSE;
import static java.lang.Boolean.TRUE;
import static java.math.BigDecimal.ZERO;
import static java.util.Arrays.asList;
import static java.util.Collections.emptyList;
import static java.util.Collections.singletonList;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.*;

public class BillingServiceUnitTest {

    private final ApplicableDiscountsFinder applicableDiscountsFinder = mock(ApplicableDiscountsFinder.class);
    private final DiscountApplicator discountApplicator = mock(DiscountApplicator.class);

    private final BillingService billingService = new BillingService(applicableDiscountsFinder, discountApplicator);

    @Test
    public void shouldChargeRequestedServices() {
        final BigDecimal diagnosisDefaultPrice = new BigDecimal("60");
        final BigDecimal ecgDefaultPrice = new BigDecimal("200.40");
        final BigDecimal vaccineDefaultPrice = new BigDecimal("27.50");
        final BigDecimal vaccineServiceCharge = new BigDecimal("15");

        final Service diagnosis = new Service(DIAGNOSIS, 1, diagnosisDefaultPrice, ZERO);
        final Service ecg = new Service(ECG, 1, ecgDefaultPrice, ZERO);
        final Service vaccine = new Service(VACCINE, 4, vaccineDefaultPrice, vaccineServiceCharge);

        final List<Service> requestedServices = asList(diagnosis, ecg, vaccine);
        final BillingRequest billingRequest = new BillingRequest(requestedServices, 17, FALSE, FALSE);

        // mock
        final Map<Service, List<Discount>> applicableDiscounts = new ImmutableMap.Builder<Service, List<Discount>>()
                .put(diagnosis, emptyList())
                .put(ecg, emptyList())
                .put(vaccine, emptyList())
                .build();

        when(applicableDiscountsFinder.find(requestedServices, 17, FALSE, FALSE)).thenReturn(applicableDiscounts);

        final PricedService pricedDiagnosis = new PricedService(
                DIAGNOSIS,
                1,
                diagnosisDefaultPrice,
                ZERO,
                60,
                emptyList()
        );
        final PricedService pricedEcg = new PricedService(
                ECG,
                1,
                ecgDefaultPrice,
                ZERO,
                200,
                emptyList()
        );
        final PricedService pricedVaccine = new PricedService(
                VACCINE,
                4,
                vaccineDefaultPrice,
                vaccineServiceCharge,
                27,
                emptyList()
        );

        final List<PricedService> expectedPricedServices = asList(pricedDiagnosis, pricedEcg, pricedVaccine);

        when(discountApplicator.apply(applicableDiscounts)).thenReturn(expectedPricedServices);

        // function call
        final List<PricedService> pricedServices = billingService.chargeMedicalServices(billingRequest);

        // assertions
        assertThat(pricedServices, is(expectedPricedServices));
    }

    @Test
    public void shouldChargeRequestedServicesWithDiscounts() {
        final BigDecimal bloodTestDefaultPrice = new BigDecimal("78.00");
        final BigDecimal vaccineDefaultPrice = new BigDecimal("27.50");
        final BigDecimal vaccineServiceCharge = new BigDecimal("15.00");

        final Service bloodTest = new Service(BLOOD_TEST, 2, bloodTestDefaultPrice, ZERO);
        final Service vaccine = new Service(VACCINE, 4, vaccineDefaultPrice, vaccineServiceCharge);

        final List<Service> requestedServices = asList(bloodTest, vaccine);
        final BillingRequest billingRequest = new BillingRequest(requestedServices, 89, TRUE, TRUE);

        // mock
        final List<Discount> discounts = singletonList(new Discount("90% off", 90));
        final Map<Service, List<Discount>> applicableDiscounts = new ImmutableMap.Builder<Service, List<Discount>>()
                .put(bloodTest, discounts)
                .put(vaccine, discounts)
                .build();

        final PricedService pricedBloodTest = new PricedService(
                BLOOD_TEST,
                2,
                bloodTestDefaultPrice,
                ZERO,
                16,
                discounts
        );
        final PricedService pricedVaccine = new PricedService(
                VACCINE,
                4,
                vaccineDefaultPrice,
                vaccineServiceCharge,
                9,
                discounts
        );

        final List<PricedService> expectedPricedServices = asList(pricedBloodTest, pricedVaccine);

        when(applicableDiscountsFinder.find(requestedServices, 89, TRUE, TRUE))
                .thenReturn(applicableDiscounts);

        when(discountApplicator.apply(applicableDiscounts)).thenReturn(expectedPricedServices);

        // function call
        final List<PricedService> pricedServices = billingService.chargeMedicalServices(billingRequest);

        // assertions
        verify(applicableDiscountsFinder, times(1)).find(requestedServices, 89, TRUE, TRUE);
        verify(discountApplicator, times(1)).apply(applicableDiscounts);

        assertThat(pricedServices, is(expectedPricedServices));
    }
}