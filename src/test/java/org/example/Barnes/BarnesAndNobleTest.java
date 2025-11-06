package org.example.Barnes;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

class BarnesAndNobleTest {
    /**
     * 1. input
     * order - Map<String, Integer>
     *
     * 2. partitioning
     * order - null || empty || non-empty
     *
     * 3. output
     * purchaseSummary - new || non-empty (if order is non-empty)
     *
     */

    @Test
    @DisplayName("specification-based")
    void getPriceForCart() {
        BookDatabase bookDB = mock(BookDatabase.class);
        when(bookDB.findByISBN(anyString())).thenAnswer(invocation -> {
            String isbn = invocation.getArgument(0);
            return new Book(isbn, 10, 10);
        });

        BuyBookProcess process = mock(BuyBookProcess.class);

        Map<String, Integer> order = new HashMap<>();
        BarnesAndNoble bn = new BarnesAndNoble(bookDB, process);

        // test null
        assertNull(bn.getPriceForCart(null));

        // test empty
        PurchaseSummary empty = bn.getPriceForCart(order);
        assertEquals(0, empty.getTotalPrice());
        assertEquals(Collections.emptyMap(), empty.getUnavailable());

        // test non-empty
        Book expectedBook = new Book("book", 10, 10);
        order.put("book", 12);
        PurchaseSummary ps = bn.getPriceForCart(order);
        assertEquals(100, ps.getTotalPrice());
        assertEquals(Map.of(expectedBook, 2), ps.getUnavailable());
        verify(process, times(1)).buyBook(expectedBook, 10);

    }

    @Test
    @DisplayName("structural-based")
    void getPriceForCartStructure() {
        BookDatabase bookDB = mock(BookDatabase.class);
        when(bookDB.findByISBN(anyString())).thenAnswer(invocation -> {
            String isbn = invocation.getArgument(0);
            return new Book(isbn, 10, 10);
        });

        BuyBookProcess process = mock(BuyBookProcess.class);

        Map<String, Integer> order = new HashMap<>();
        BarnesAndNoble bn = new BarnesAndNoble(bookDB, process);

        // buy enough with no unavailable
        Book expectedBook = new Book("book1", 10, 10);
        order.put("book1", 8);
        PurchaseSummary ps = bn.getPriceForCart(order);
        assertEquals(80, ps.getTotalPrice());
        assertEquals(Collections.emptyMap(), ps.getUnavailable());
        verify(process, times(1)).buyBook(expectedBook, 8);
    }
}