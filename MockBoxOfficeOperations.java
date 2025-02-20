import java.time.LocalDateTime;
import java.util.List;

/**
 * Mock implementation of BoxOfficeOperationsInterface.
 * This simulates expected behavior until the actual backend is implemented.
 */
class MockBoxOfficeOperations implements BoxOfficeOperationsInterface {
    @Override
    public SeatConfiguration getSeatingConfiguration(String eventID) {
        Seat[][] seats = new Seat[5][5];  // Mock seat grid
        return new SeatConfiguration(eventID, seats, 5, 5);
    }

    @Override
    public TicketSales getTicketSales(String eventID) {
        return new TicketSales(50, 200, new String[]{"A1", "A2"}, new String[]{"B1", "B2"}, 5, 10, 5000);
    }

    @Override
    public GuestCheckIn getGuestCheckIn(String eventID) {
        List<CheckIn> checkIns = List.of(
                new CheckIn("TICKET123", LocalDateTime.now()),
                new CheckIn("TICKET124", LocalDateTime.now().plusMinutes(10))
        );
        return new GuestCheckIn(checkIns.size(), checkIns, 100, 98, "Music Fest", "Hall A", LocalDateTime.now().plusHours(2));
    }

    @Override
    public RefundInfo getRefundInfo(String eventID) {
        Refund[] refunds = {
                new Refund("TICKET123", "2025-02-19", 50.0, "Customer Request"),
                new Refund("TICKET124", "2025-02-19", 75.0, "Event Canceled")
        };
        return new RefundInfo(2, 125.0, refunds);
    }
}