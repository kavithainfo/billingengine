package com.kavi.billingengine.discounts;

import com.kavi.billingengine.discounts.rule.RuleProcessor;
import com.kavi.billingengine.domain.Discount;
import com.kavi.billingengine.domain.Service;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static java.util.Collections.emptyMap;
import static java.util.Collections.singletonMap;

public class ApplicableDiscountsFinder {

    private final List<RuleProcessor> ruleProcessors;

    public ApplicableDiscountsFinder(List<RuleProcessor> ruleProcessors) {
        this.ruleProcessors = ruleProcessors;
    }

    public Map<Service, List<Discount>> find(
            List<Service> requestedServices,
            Integer age,
            Boolean hasInsurance,
            Boolean hasBeenDiagnosed) {
        if (requestedServices.isEmpty()) {
            return emptyMap();
        }

        return requestedServices.stream()
                .map(s -> {
                    final List<Discount> discounts = ruleProcessors.stream()
                            .map(rp -> rp.process(s, age, hasInsurance, hasBeenDiagnosed))
                            .flatMap(Collection::stream)
                            .collect(Collectors.toList());
                    return singletonMap(s, discounts);
                })
                .flatMap(m -> m.entrySet().stream())
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
    }
}