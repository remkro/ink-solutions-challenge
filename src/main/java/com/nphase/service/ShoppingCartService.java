package com.nphase.service;

import com.nphase.entity.Product;
import com.nphase.entity.ShoppingCart;

import java.math.BigDecimal;
import java.util.Map;
import java.util.stream.Collectors;

public class ShoppingCartService {

    public BigDecimal calculateTotalPrice(ShoppingCart shoppingCart) {
        if (shoppingCart == null)
            throw new IllegalArgumentException("ShoppingCart cannot be null!");

        return shoppingCart.getProducts()
                .stream()
                .map(product -> product.getPricePerUnit().multiply(BigDecimal.valueOf(product.getQuantity())))
                .reduce(BigDecimal::add)
                .orElse(BigDecimal.ZERO);
    }

    public BigDecimal calculateTotalPriceWithDiscount(ShoppingCart shoppingCart, int minItems, double discountPercentage) {
        if (shoppingCart == null)
            throw new IllegalArgumentException("ShoppingCart cannot be null!");

        BigDecimal discount = BigDecimal.ONE.subtract(BigDecimal.valueOf(discountPercentage / 100));

        return shoppingCart.getProducts().stream()
                .map(product -> {
                    if (product.getQuantity() > minItems) {
                        return product.getPricePerUnit().multiply(BigDecimal.valueOf(product.getQuantity())).multiply(discount);
                    } else {
                        return product.getPricePerUnit().multiply(BigDecimal.valueOf(product.getQuantity()));
                    }
                }).reduce(BigDecimal::add).orElse(BigDecimal.ZERO);
    }

    public BigDecimal calculateTotalPriceWithCategoryDiscount(ShoppingCart shoppingCart, int minItems, double discountPercentage) {
        if (shoppingCart == null)
            throw new IllegalArgumentException("ShoppingCart cannot be null!");

        BigDecimal discount = BigDecimal.ONE.subtract(BigDecimal.valueOf(discountPercentage / 100));
        Map<String, Integer> categoriesMap = shoppingCart.getProducts().stream()
                .collect(Collectors.groupingBy(Product::getCategory, Collectors.summingInt(Product::getQuantity)));

        return shoppingCart.getProducts().stream()
                .map(product -> {
                    if (categoriesMap.get(product.getCategory()) > minItems) {
                        return product.getPricePerUnit().multiply(BigDecimal.valueOf(product.getQuantity())).multiply(discount);
                    } else {
                        return product.getPricePerUnit().multiply(BigDecimal.valueOf(product.getQuantity()));
                    }
                }).reduce(BigDecimal::add).orElse(BigDecimal.ZERO);
    }
}
