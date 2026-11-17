package com.saptarshi.movieticket;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class MovieTicketBookingTest {

    private MovieTicketBooking show;

    @BeforeEach
    void setUp() {
        // a small screen with 50 seats for every test
        show = new MovieTicketBooking(50);
    }

    @Test
    @DisplayName("Booking seats reduces the available seats")
    void testBookSeatsReducesAvailability() {
        assertTrue(show.bookSeats(4));
        assertEquals(46, show.getAvailableSeats());
    }

    @Test
    @DisplayName("Booking more seats than available is rejected")
    void testOverbookingIsRejected() {
        show.bookSeats(48);
        assertFalse(show.bookSeats(5));
        assertEquals(2, show.getAvailableSeats());
    }

    @Test
    @DisplayName("Ticket price depends on seat type and quantity")
    void testCalculateTicketPrice() {
        assertEquals(450.0, show.calculateTicketPrice("REGULAR", 3), 0.001);
        assertEquals(500.0, show.calculateTicketPrice("premium", 2), 0.001);
        assertEquals(400.0, show.calculateTicketPrice("RECLINER", 1), 0.001);
    }

    @Test
    @DisplayName("Unknown seat type throws an exception")
    void testInvalidSeatType() {
        assertThrows(IllegalArgumentException.class,
                () -> show.calculateTicketPrice("BALCONY", 2));
    }

    @Test
    @DisplayName("Coupons apply the correct discount")
    void testApplyDiscount() {
        assertEquals(450.0, show.applyDiscount(500.0, "STUDENT10"), 0.001);
        assertEquals(400.0, show.applyDiscount(500.0, "WEEKDAY20"), 0.001);
        assertEquals(450.0, show.applyDiscount(500.0, "FLAT50"), 0.001);
        assertEquals(500.0, show.applyDiscount(500.0, "WRONGCODE"), 0.001);
    }

    @Test
    @DisplayName("Final amount includes 18% GST")
    void testCalculateFinalAmountWithGst() {
        // 2 premium seats = 500, STUDENT10 -> 450, +18% GST -> 531
        double base = show.calculateTicketPrice("PREMIUM", 2);
        double discounted = show.applyDiscount(base, "STUDENT10");
        assertEquals(531.0, show.calculateFinalAmount(discounted), 0.001);
    }

    @Test
    @DisplayName("Refund depends on how early the booking is cancelled")
    void testCancelBookingRefund() {
        show.bookSeats(6);
        assertEquals(531.0, show.cancelBooking(2, 531.0, 30), 0.001);
        assertEquals(265.5, show.cancelBooking(2, 531.0, 5), 0.001);
        assertEquals(0.0, show.cancelBooking(2, 531.0, 1), 0.001);
        assertEquals(50, show.getAvailableSeats());
    }
}
