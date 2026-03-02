package com.oceanview.service;

import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;

@DisplayName("BillingService Tests")
class BillingServiceTest {

    private BillingService billingService;

    @BeforeEach
    void setUp() {
        billingService = new BillingService();
    }

    @Test
    @DisplayName("Standard room: 1 night should be 5750 (5000 + 10% tax + 5% service)")
    void testStandardRoom1Night() {
        double total = billingService.calculateTotal(5000.00, 1);
        assertEquals(5750.00, total, 0.01, "1 night standard room should cost 5750");
    }

    @Test
    @DisplayName("Standard room: 3 nights total calculation")
    void testStandardRoom3Nights() {
        double total = billingService.calculateTotal(5000.00, 3);
        assertEquals(17250.00, total, 0.01, "3 nights standard room should cost 17250");
    }

    @Test
    @DisplayName("Ocean View room: 2 nights")
    void testOceanViewRoom2Nights() {
        double total = billingService.calculateTotal(20000.00, 2);
        assertEquals(46000.00, total, 0.01, "2 nights ocean view should cost 46000");
    }

    @Test
    @DisplayName("Sub-total should equal rate x nights")
    void testSubTotal() {
        double subTotal = billingService.getSubTotal(8500.00, 2);
        assertEquals(17000.00, subTotal, 0.01, "SubTotal should be 8500 x 2 = 17000");
    }

    @Test
    @DisplayName("Tax should be 10% of subtotal")
    void testTax() {
        double tax = billingService.getTax(10000.00, 1);
        assertEquals(1000.00, tax, 0.01, "Tax should be 10% of 10000 = 1000");
    }

    @Test
    @DisplayName("Service charge should be 5% of subtotal")
    void testServiceCharge() {
        double sc = billingService.getServiceCharge(10000.00, 1);
        assertEquals(500.00, sc, 0.01, "Service charge should be 5% of 10000 = 500");
    }

    @Test
    @DisplayName("Zero nights should return zero total")
    void testZeroNights() {
        double total = billingService.calculateTotal(5000.00, 0);
        assertEquals(0.0, total, 0.01, "Zero nights should be 0 total");
    }

    @Test
    @DisplayName("Suite room: 5 nights calculation")
    void testSuiteRoom5Nights() {
        double total = billingService.calculateTotal(15000.00, 5);
        assertEquals(86250.00, total, 0.01, "5 nights suite should be 86250");
    }
}
