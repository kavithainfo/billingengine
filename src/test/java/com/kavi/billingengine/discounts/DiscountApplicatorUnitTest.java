package com.kavi.billingengine.discounts;

import com.google.common.collect.ImmutableMap;
import com.kavi.billingengine.domain.Discount;
import com.kavi.billingengine.domain.PricedService;
import com.kavi.billingengine.domain.Service;
import org.junit.Test;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import static com.kavi.billingengine.domain.ServiceType.*;
import static java.math.BigDecimal.ZERO;
import static java.util.Arrays.asList;
import static java.util.Collections.*;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

public class DiscountApplicatorUnitTest {

    private final DiscountApplicator discountApplicator = new DiscountApplicator();

    @Test
    public void shouldReturnDefaultPriceWhenNoDiscountToApply() {
        final BigDecimal defaultPrice = new BigDecimal("400.78");
        final Service ecg = new Service(ECG, 1, defaultPrice, ZERO);

        final Map<Service, List<Discount>> applicableDiscounts = singletonMap(ecg, emptyList());

        final List<PricedService> pricedServices = discountApplicator.apply(applicableDiscounts);

        final PricedService pricedService = new PricedService(ECG, 1, defaultPrice, ZERO, 400, emptyList());

        assertThat(pricedServices, is(singletonList(pricedService)));
    }

    @Test
    public void shouldApplyDiscountToAService() {
        final BigDecimal defaultPrice = new BigDecimal("60");
        final Service xray = new Service(XRAY, 1, defaultPrice, ZERO);
        final Discount discount = new Discount("40% off", 40);

        final Map<Service, List<Discount>> applicableDiscounts = singletonMap(xray, singletonList(discount));

        final List<PricedService> pricedServices = discountApplicator.apply(applicableDiscounts);

        final PricedService expectedDiscountedService =
                new PricedService(XRAY, 1, defaultPrice, ZERO, 36, singletonList(discount));

        assertThat(pricedServices, is(singletonList(expectedDiscountedService)));
    }

    @Test
    public void shouldApplyDiscountToMultipleServices() {
        final BigDecimal diagnosisDefaultPrice = new BigDecimal("60");
        final Service diagnosis = new Service(DIAGNOSIS, 2, diagnosisDefaultPrice, ZERO);

        final BigDecimal ecgDefaultPrice = new BigDecimal("200.40");
        final Service ecg = new Service(ECG, 1, ecgDefaultPrice, ZERO);

        final Discount discount = new Discount("60% off", 60);

        final Map<Service, List<Discount>> applicableDiscounts = new ImmutableMap.Builder<Service, List<Discount>>()
                .put(diagnosis, singletonList(discount))
                .put(ecg, singletonList(discount))
                .build();

        final List<PricedService> pricedServices = discountApplicator.apply(applicableDiscounts);

        final PricedService discountedDiagnosis = new PricedService(
                DIAGNOSIS,
                2,
                diagnosisDefaultPrice,
                ZERO,
                48,
                singletonList(discount)
        );
        final PricedService discountedEcg = new PricedService(
                ECG,
                1,
                ecgDefaultPrice,
                ZERO,
                80,
                singletonList(discount)
        );

        assertThat(pricedServices, is(asList(discountedDiagnosis, discountedEcg)));
    }

    @Test
    public void shouldApplyDiscountToMultipleVaccinesWithServiceCharge() {
        final BigDecimal defaultPrice = new BigDecimal("78");
        final BigDecimal serviceCharge = new BigDecimal("27.50");
        final Service vaccine = new Service(VACCINE, 3, defaultPrice, serviceCharge);
        final Discount discount = new Discount("90% off", 90);

        final Map<Service, List<Discount>> applicableDiscounts = singletonMap(vaccine, singletonList(discount));

        final List<PricedService> pricedServices = discountApplicator.apply(applicableDiscounts);

        final PricedService expectedDiscountedService = new PricedService(
                VACCINE,
                3,
                defaultPrice,
                serviceCharge,
                26,
                singletonList(discount)
        );

        assertThat(pricedServices, is(singletonList(expectedDiscountedService)));
    }

    @Test
    public void shouldApplyAdditionalDiscountsToBloodTest() {
        final BigDecimal bloodTestDefaultPrice = new BigDecimal("78");
        final Service bloodTest = new Service(BLOOD_TEST, 2, bloodTestDefaultPrice, ZERO);

        final BigDecimal diagnosisDefaultPrice = new BigDecimal("60");
        final Service diagnosis = new Service(DIAGNOSIS, 1, diagnosisDefaultPrice, ZERO);

        final Discount discount = new Discount("90% off", 90);
        final Discount additionalDiscount = new Discount("15% off", 15);

        final Map<Service, List<Discount>> applicableDiscounts = new ImmutableMap.Builder<Service, List<Discount>>()
                .put(bloodTest, asList(discount, additionalDiscount))
                .put(diagnosis, singletonList(discount))
                .build();

        final List<PricedService> pricedServices = discountApplicator.apply(applicableDiscounts);

        // expected
        final PricedService discountedBloodTestService = new PricedService(
                BLOOD_TEST,
                2,
                bloodTestDefaultPrice,
                ZERO,
                13,
                asList(discount, additionalDiscount)
        );
        final PricedService discountedDiagnosisService = new PricedService(
                DIAGNOSIS,
                1,
                diagnosisDefaultPrice,
                ZERO,
                6,
                singletonList(discount)
        );
        final List<PricedService> expectedPricedServices = asList(discountedBloodTestService, discountedDiagnosisService);

        assertThat(pricedServices, is(expectedPricedServices));
    }

}