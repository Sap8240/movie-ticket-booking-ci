package com.saptarshi.movieticket;

/**
 * Simple movie ticket booking system for a single show.
 * Handles seat availability, ticket pricing by seat category,
 * coupon discounts, GST and cancellation refunds.
 */
public class MovieTicketBooking {

    public static final double REGULAR_PRICE = 150.0;
    public static final double PREMIUM_PRICE = 250.0;
    public static final double RECLINER_PRICE = 400.0;
    public static final double GST_RATE = 0.18;

    private final int totalSeats;
    private int bookedSeats;

    public MovieTicketBooking(int totalSeats) {
        if (totalSeats <= 0) {
            throw new IllegalArgumentException("Total seats must be greater than zero");
        }
        this.totalSeats = totalSeats;
        this.bookedSeats = 0;
    }

    /** Returns the number of seats still free for the show. */
    public int getAvailableSeats() {
        return totalSeats - bookedSeats;
    }

    /**
     * Books the given number of seats if they are available.
     * Returns true if the booking went through, false if the show
     * does not have enough free seats.
     */
    public boolean bookSeats(int count) {
        if (count <= 0) {
            throw new IllegalArgumentException("Seat count must be at least 1");
        }
        if (count > getAvailableSeats()) {
            return false;
        }
        bookedSeats += count;
        return true;
    }

    /**
     * Calculates the base ticket price (before discount and GST)
     * for a seat category: REGULAR, PREMIUM or RECLINER.
     */
    public double calculateTicketPrice(String seatType, int quantity) {
        if (quantity <= 0) {
            throw new IllegalArgumentException("Quantity must be at least 1");
        }
        if (seatType == null) {
            throw new IllegalArgumentException("Seat type is required");
        }
        double pricePerSeat;
        switch (seatType.trim().toUpperCase()) {
            case "REGULAR":
                pricePerSeat = REGULAR_PRICE;
                break;
            case "PREMIUM":
                pricePerSeat = PREMIUM_PRICE;
                break;
            case "RECLINER":
                pricePerSeat = RECLINER_PRICE;
                break;
            default:
                throw new IllegalArgumentException("Unknown seat type: " + seatType);
        }
        return pricePerSeat * quantity;
    }

    /**
     * Applies a coupon to an amount.
     * STUDENT10 = 10% off, WEEKDAY20 = 20% off, FLAT50 = Rs.50 off.
     * An unknown or empty coupon leaves the amount unchanged.
     */
    public double applyDiscount(double amount, String couponCode) {
        if (amount < 0) {
            throw new IllegalArgumentException("Amount cannot be negative");
        }
        if (couponCode == null || couponCode.isBlank()) {
            return amount;
        }
        switch (couponCode.trim().toUpperCase()) {
            case "STUDENT10":
                return round(amount * 0.90);
            case "WEEKDAY20":
                return round(amount * 0.80);
            case "FLAT50":
                return Math.max(0, amount - 50);
            default:
                return amount;
        }
    }

    /** Adds 18% GST to the amount and returns the final payable amount. */
    public double calculateFinalAmount(double amount) {
        if (amount < 0) {
            throw new IllegalArgumentException("Amount cannot be negative");
        }
        return round(amount + amount * GST_RATE);
    }

    /**
     * Cancels seats and returns the refund.
     * Cancelling 24 hours or more before the show refunds 100%,
     * between 2 and 24 hours refunds 50%, and under 2 hours refunds nothing.
     */
    public double cancelBooking(int count, double amountPaid, int hoursBeforeShow) {
        if (count <= 0 || count > bookedSeats) {
            throw new IllegalArgumentException("Invalid number of seats to cancel");
        }
        bookedSeats -= count;
        if (hoursBeforeShow >= 24) {
            return amountPaid;
        } else if (hoursBeforeShow >= 2) {
            return round(amountPaid * 0.50);
        }
        return 0.0;
    }

    private double round(double value) {
        return Math.round(value * 100.0) / 100.0;
    }
}
