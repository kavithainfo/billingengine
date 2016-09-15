package com.kavi.billingengine.discounts;

import com.kavi.billingengine.domain.Discount;
import com.kavi.billingengine.domain.Service;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import static java.util.Collections.emptyMap;
import static java.util.Collections.singletonMap;
import static java.util.Optional.empty;
import static java.util.Optional.of;

public class ApplicableDiscountsFinder {

    public Map<Service, Optional<Discount>> findApplicableDiscounts(
            List<Service> servicesRequested,
            Integer age,
            Boolean hasMediHealthInsurance,
            Boolean isDiagnosedByMediHealth) {
        if (servicesRequested.isEmpty()) {
            return emptyMap();
        }

        final Optional<Discount> applicableDiscount = processRules(age);

        return servicesRequested.stream()
                .map(s -> singletonMap(s, applicableDiscount))
                .flatMap(l -> l.entrySet().stream())
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
    }

    private Optional<Discount> processRules(Integer age) {
        if(age < 5) {
            return of(new Discount("40% off", 40));
        } else if(age >= 65 && age <= 70) {
            return of(new Discount("60% off", 60));
        } else if(age > 70) {
            return of(new Discount("90% off", 90));
        } else {
            return empty();
        }
    }
}