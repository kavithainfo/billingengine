package com.kavi.billingengine.discounts.rule;

import com.kavi.billingengine.domain.Discount;
import com.kavi.billingengine.domain.Service;

import java.util.List;

import static com.kavi.billingengine.domain.ServiceType.BLOOD_TEST;
import static java.util.Collections.emptyList;
import static java.util.Collections.singletonList;

public class InsuranceRuleProcessor implements RuleProcessor {

    @Override
    public List<Discount> process(Service service, Integer age, Boolean hasHealthInsurance, Boolean hasBeenDiagnosed) {
        return (BLOOD_TEST.equals(service.getServiceType()) && hasBeenDiagnosed && hasHealthInsurance) ?
                singletonList(new Discount("15% off", 15)) :
                emptyList();
    }
}