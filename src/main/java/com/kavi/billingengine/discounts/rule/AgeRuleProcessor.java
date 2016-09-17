package com.kavi.billingengine.discounts.rule;

import com.kavi.billingengine.domain.Discount;
import com.kavi.billingengine.domain.Service;

import java.util.List;

import static java.util.Collections.emptyList;
import static java.util.Collections.singletonList;

public class AgeRuleProcessor implements RuleProcessor {

    @Override
    public List<Discount> process(Service services, Integer age, Boolean hasHealthInsurance, Boolean hasBeenDiagnosed) {
        if (age >= 1 && age < 5) {
            return singletonList(new Discount("40% off", 40));
        } else if (age >= 65 && age <= 70) {
            return singletonList(new Discount("60% off", 60));
        } else if (age >= 70 && age <= 120) {
            return singletonList(new Discount("90% off", 90));
        } else {
            return emptyList();
        }
    }
}