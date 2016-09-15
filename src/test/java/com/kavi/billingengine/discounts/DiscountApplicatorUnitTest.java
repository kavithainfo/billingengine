package com.kavi.billingengine.discounts;

import com.google.common.collect.ImmutableMap;
import com.kavi.billingengine.domain.Discount;
import com.kavi.billingengine.domain.PricedService;
import com.kavi.billingengine.domain.Service;

import org.junit.Test;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static com.kavi.billingengine.domain.ServiceType.*;
import static java.math.BigDecimal.ZERO;

import static java.util.Arrays.asList;
import static java.util.Collections.singletonList;
import static java.util.Collections.singletonMap;
import static java.util.Optional.empty;
import static java.util.Optional.of;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

public class DiscountApplicatorUnitTest {

    private DiscountApplicator discountApplicator = new DiscountApplicator();

    @Test
    public void shouldReturnDefaultPriceWhenNoDiscountToApply() {
        final BigDecimal defaultPrice = new BigDecimal("400.00");
        final Service ecg = new Service(ECG, 1, defaultPrice, ZERO);

        final Map<Service, Optional<Discount>> servicesAndDiscounts = singletonMap(ecg, empty());

        final List<PricedService> pricedServices = discountApplicator.applyDiscounts(servicesAndDiscounts);

        final PricedService pricedService = new PricedService(ECG, 1, defaultPrice, ZERO, defaultPrice, empty());

        assertThat(pricedServices, is(singletonList(pricedService)));
    }

    @Test
    public void shouldApplyDiscountToAService() {
        final BigDecimal defaultPrice = new BigDecimal("60.00");
        final Service xray = new Service(XRAY, 1, defaultPrice, ZERO);
        final Discount discount = new Discount("40% off", 40);

        final Map<Service, Optional<Discount>> servicesAndDiscounts = singletonMap(xray, of(discount));

        final List<PricedService> pricedServices = discountApplicator.applyDiscounts(servicesAndDiscounts);

        final PricedService expectedDiscountedService = new PricedService(XRAY, 1, defaultPrice, ZERO, new BigDecimal("36.000"), of(discount));

        assertThat(pricedServices, is(singletonList(expectedDiscountedService)));
    }

    @Test
    public void shouldApplyDiscountToMultipleVaccinesWithServiceCharge() {
        final BigDecimal defaultPrice = new BigDecimal("78.00");
        final BigDecimal serviceCharge = new BigDecimal("27.50");
        final Service vaccine = new Service(VACCINE, 3, defaultPrice, serviceCharge);
        final Discount discount = new Discount("90% off", 90);

        final Map<Service, Optional<Discount>> servicesAndDiscounts = singletonMap(vaccine, of(discount));

        final List<PricedService> pricedServices = discountApplicator.applyDiscounts(servicesAndDiscounts);

        final PricedService expectedDiscountedService = new PricedService(VACCINE, 3, defaultPrice, serviceCharge, new BigDecimal("26.150"), of(discount));

        assertThat(pricedServices, is(singletonList(expectedDiscountedService)));
    }

    @Test
    public void shouldApplyDiscountToMultipleServices() {
        final BigDecimal diagnosisDefaultPrice = new BigDecimal("60.00");
        final Service diagnosis = new Service(DIAGNOSIS, 2, diagnosisDefaultPrice, ZERO);

        final BigDecimal ecgDefaultPrice = new BigDecimal("200.40");
        final Service ecg = new Service(ECG, 1, ecgDefaultPrice, ZERO);

        final Discount discount = new Discount("60% off", 60);

        final Map<Service, Optional<Discount>> servicesAndDiscounts = new ImmutableMap.Builder<Service, Optional<Discount>>()
                .put(diagnosis, of(discount))
                .put(ecg, of(discount))
                .build();

        final List<PricedService> pricedServices = discountApplicator.applyDiscounts(servicesAndDiscounts);

        final PricedService discountedDiagnosis = new PricedService(DIAGNOSIS, 2, diagnosisDefaultPrice, ZERO, new BigDecimal("48.000") ,of(discount));
        final PricedService discountedEcg = new PricedService(ECG, 1, ecgDefaultPrice, ZERO, new BigDecimal("80.160"), of(discount));

        assertThat(pricedServices, is(asList(discountedDiagnosis, discountedEcg)));
    }

}