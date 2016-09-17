package com.kavi.billingengine.discounts.rule;

import com.kavi.billingengine.domain.Discount;
import com.kavi.billingengine.domain.Service;

import java.util.List;

public interface RuleProcessor {

    List<Discount> process(Service service, Integer age, Boolean hasHealthInsurance, Boolean hasBeenDiagnosed);
}
