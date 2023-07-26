package com.nphase.service;


import com.nphase.entity.Product;
import com.nphase.entity.ShoppingCart;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.assertThrows;

public class ShoppingCartServiceTest {
    private final ShoppingCartService service = new ShoppingCartService();

    @Test
    public void calculatesPrice() {
        ShoppingCart cart = new ShoppingCart(Arrays.asList(
                new Product("Tea", BigDecimal.valueOf(5.0), 2),
                new Product("Coffee", BigDecimal.valueOf(6.5), 1)
        ));

        BigDecimal result = service.calculateTotalPrice(cart);

        Assertions.assertEquals(result, BigDecimal.valueOf(16.5));
    }

    @Test
    public void calculatesPriceWithDiscount() {
        ShoppingCart cart = new ShoppingCart(Arrays.asList(
                new Product("Tea", BigDecimal.valueOf(5.0), 5),
                new Product("Coffee", BigDecimal.valueOf(3.5), 3)
        ));

        BigDecimal result = service.calculateTotalPriceWithDiscount(cart, 3, 10.0);

        Assertions.assertEquals(BigDecimal.valueOf(33.00).setScale(2, RoundingMode.CEILING),
                result.setScale(2, RoundingMode.CEILING));
    }

    @Test
    public void calculatesPriceWithCategoryDiscount() {
        ShoppingCart cart = new ShoppingCart(Arrays.asList(
                new Product("Tea", BigDecimal.valueOf(5.3), 2, "drinks"),
                new Product("Coffee", BigDecimal.valueOf(3.5), 2, "drinks"),
                new Product("Cheese", BigDecimal.valueOf(8.0), 2, "food")
        ));

        BigDecimal result = service.calculateTotalPriceWithCategoryDiscount(cart, 3, 10.0);

        Assertions.assertEquals(BigDecimal.valueOf(31.84).setScale(2, RoundingMode.CEILING),
                result.setScale(2, RoundingMode.CEILING));
    }

    @Test
    public void throwsExceptionWhenShoppingCartIsNull() {
        Exception exception = assertThrows(IllegalArgumentException.class, () -> service.calculateTotalPrice(null));
    }
}