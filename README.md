# Movie Ticket Booking – CI with GitHub Actions

A small Java application for booking movie tickets for a single show, built with Maven and tested with JUnit 5. Every push to `main` triggers a GitHub Actions workflow that builds the project and runs all the tests.

## Operations
- `bookSeats(count)` – books seats if enough are free
- `getAvailableSeats()` – seats left for the show
- `calculateTicketPrice(seatType, quantity)` – REGULAR ₹150, PREMIUM ₹250, RECLINER ₹400
- `applyDiscount(amount, coupon)` – STUDENT10, WEEKDAY20, FLAT50
- `calculateFinalAmount(amount)` – adds 18% GST
- `cancelBooking(count, amountPaid, hoursBeforeShow)` – refund of 100% / 50% / 0%

## Run locally
```
mvn clean test
```

Course: 24CSE538 Cloud DevOps – Unit III assignment (CI only)
Author: Saptarshi Kayal (24BTRBT023)
