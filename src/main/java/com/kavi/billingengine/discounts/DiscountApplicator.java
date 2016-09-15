package com.kavi.billingengine.discounts;

import com.kavi.billingengine.domain.Discount;
import com.kavi.billingengine.domain.PricedService;
import com.kavi.billingengine.domain.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import static java.util.Optional.empty;
import static java.util.Optional.of;

public class DiscountApplicator {

    private final BigDecimal HUNDRED = new BigDecimal("100");

    public List<PricedService> applyDiscounts(Map<Service, Optional<Discount>> servicesAndDiscounts) {
        return servicesAndDiscounts.entrySet().stream()
                .map(e -> {
                    final Service service = e.getKey();
                    final Optional<Discount> discount = e.getValue();
                    return discount
                            .map(d -> applyDiscount(service, d))
                            .orElse(new PricedService(
                                    service.getServiceType(),
                                    service.getQuantity(),
                                    service.getDefaultPrice(),
                                    service.getServiceCharge(),
                                    service.getDefaultPrice(),
                                    empty())
                            );
                }).collect(Collectors.toList());
    }

    private PricedService applyDiscount(Service service, Discount discount) {
        final BigDecimal totalPrice = service.getDefaultPrice()
                .multiply(new BigDecimal(service.getQuantity()))
                .add(service.getServiceCharge());

        final BigDecimal discountAmount = totalPrice
                .multiply(new BigDecimal(discount.getPercentageDiscount())
                        .divide(HUNDRED));

        final BigDecimal finalDiscountedPrice = totalPrice.subtract(discountAmount);

        final PricedService discountedService = new PricedService(
                service.getServiceType(),
                service.getQuantity(),
                service.getDefaultPrice(),
                service.getServiceCharge(),
                finalDiscountedPrice,
                of(discount)
        );

        return discountedService;
    }
}
