package org.example.Amazon;

import org.example.Amazon.Cost.*;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class AmazonUnitTest {
    /**
     * 1. input
     * rules - List<PriceRule> rules;
     * carts - ShoppingCart;
     *
     * 2. partitioning
     * rules - empty || non-empty
     * carts - non-null
     * 
     * 3. output
     * result - double
     */

    @Test
    @DisplayName("specification-based")
    void calculate() {
        ShoppingCart cart = mock(ShoppingCart.class);
        PriceRule rule = mock(PriceRule.class);

        // empty rules
        Amazon emptyRules = new Amazon(cart, Collections.emptyList());
        double result = emptyRules.calculate();
        assertEquals(0.0, result);

        // non-empty rules
        Item item = new Item(ItemType.OTHER, "Book", 1, 9.99);
        List<Item> items = Arrays.asList(item);

        when(cart.getItems()).thenReturn(items);
        when(rule.priceToAggregate(items)).thenReturn(15.99);


        Amazon amazon = new Amazon(cart, Arrays.asList(rule));
        result = amazon.calculate();

        assertEquals(9.99, result);
        verify(rule, times(1)).priceToAggregate(items);

        amazon.addToCart(item);

        verify(cart, times(1)).add(item);
    }
}