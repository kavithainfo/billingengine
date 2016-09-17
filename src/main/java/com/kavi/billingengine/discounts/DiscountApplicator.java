package com.kavi.billingengine.discounts;

import com.kavi.billingengine.domain.Discount;
import com.kavi.billingengine.domain.PricedService;
import com.kavi.billingengine.domain.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import static java.math.RoundingMode.HALF_DOWN;

public class DiscountApplicator implements Function<Map<Service, List<Discount>>, List<PricedService>> {

    private final BigDecimal HUNDRED = new BigDecimal("100");

    @Override
    public List<PricedService> apply(Map<Service, List<Discount>> applicableDiscountsForServices) {
        return applicableDiscountsForServices.entrySet().stream()
                .map(e -> {
                    final List<Discount> discounts = e.getValue();
                    final Service service = e.getKey();

                    final BigDecimal totalPrice = calculateTotalPrice(
                            service.getDefaultPrice(),
                            service.getServiceCharge(),
                            service.getQuantity()
                    );

                    final BigDecimal finalDiscountedPrice = discounts.stream()
                            .reduce(totalPrice, this::applyDiscount, (p1, p2) -> p2);

                    return new PricedService(
                            service.getServiceType(),
                            service.getQuantity(),
                            service.getDefaultPrice(),
                            service.getServiceCharge(),
                            roundOff(finalDiscountedPrice),
                            discounts
                    );
                })
                .collect(Collectors.toList());
    }

    private BigDecimal calculateTotalPrice(BigDecimal defaultPrice, BigDecimal serviceCharge, Integer quantity) {
        return defaultPrice
                .multiply(new BigDecimal(quantity))
                .add(serviceCharge);
    }

    private BigDecimal applyDiscount(BigDecimal totalPrice, Discount discount) {
        final BigDecimal discountAmount = totalPrice
                .multiply(new BigDecimal(discount.getPercentageDiscount())
                        .divide(HUNDRED));

        return totalPrice.subtract(discountAmount);
    }

    private Integer roundOff(BigDecimal price) {
        return price.setScale(2, HALF_DOWN).intValue();
    }
}
