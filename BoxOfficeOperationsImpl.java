import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Implementation of the BoxOfficeOperationsInterface.
 * Handles database queries related to seating configuration, ticket sales, guest check-ins, and refunds.
 */
public class BoxOfficeOperationsImpl implements BoxOfficeOperationsInterface {
    private JDBCConnection dbConnection = new JDBCConnection();

    /**
     * Retrieves the seating configuration for a specific event.
     *
     * @param eventID The unique ID of the event.
     * @return A {@link SeatConfiguration} object containing seating details.
     */
    @Override
    public SeatConfiguration getSeatingConfiguration(String eventID) {
        String query = "SELECT * FROM seats WHERE eventID = ?";
        List<Seat> seatList = new ArrayList<>();
        Seat[][] seatMap = new Seat[5][5];

        try (Connection connection = dbConnection.getConnection();
             PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setString(1, eventID);
            ResultSet rs = stmt.executeQuery();

            int index = 0;
            while (rs.next()) {
                String restrictedViewStr = rs.getString("restrictedView").trim();
                RestrictedViewType restrictedView = RestrictedViewType.valueOf(restrictedViewStr);

                Seat seat = new Seat(
                        rs.getString("seatID"),
                        SeatStatus.valueOf(rs.getString("seatStatus")),
                        restrictedView,
                        rs.getBoolean("wheelchairAccessible")
                );
                seatList.add(seat);

                int row = index / 5;
                int col = index % 5;
                seatMap[row][col] = seat;
                index++;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        // counting total wheelchair accessible and blocked seats for a given event
        int totalAccessibleSeats = 0;
        int totalBlockedSeats = 0;
        for (Seat seat : seatList) {
            if (seat.getRestrictedView() == RestrictedViewType.BLOCKED) totalBlockedSeats++;
            if (seat.isWheelchairAccessible()) totalAccessibleSeats++;
        }

        // creating SeatConfiguration with the seatMap (5x5 grid for testing)
        return new SeatConfiguration(eventID, seatMap, totalAccessibleSeats, totalBlockedSeats);
    }

    /**
     * Retrieves ticket sales data for a specific event.
     *
     * @param eventID The unique ID of the event.
     * @return A {@link TicketSales} object containing sales details.
     */
    @Override
    public TicketSales getTicketSales(String eventID) {
        String query = "SELECT * FROM ticket_sales WHERE eventID = ?";
        try (Connection connection = dbConnection.getConnection();
             PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setString(1, eventID);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return new TicketSales(
                        rs.getInt("totalTicketsSold"),
                        rs.getInt("totalSeatsAvailable"),
                        new ArrayList<>(), // Placeholder for sold seats
                        new ArrayList<>(), // Placeholder for available seats
                        rs.getInt("accessibleSeatsSold"),
                        rs.getInt("totalAccessibleSeats"),
                        rs.getDouble("totalRevenue")
                );
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Retrieves guest check-in details for a specific event.
     *
     * @param eventID The unique ID of the event.
     * @return A {@link GuestCheckIn} object containing check-in details.
     */
    @Override
    public GuestCheckIn getGuestCheckIn(String eventID) {
        String query = "SELECT * FROM guest_checkins WHERE eventID = ?";
        List<CheckIn> checkIns = new ArrayList<>();

        try (Connection connection = dbConnection.getConnection();
             PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setString(1, eventID);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                checkIns.add(new CheckIn(rs.getString("ticketID"), rs.getTimestamp("checkInTime").toLocalDateTime()));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return new GuestCheckIn(0, checkIns, 0, 0, "", "", java.time.LocalDateTime.now()); // Mock data
    }

    /**
     * Retrieves refund information for a specific event.
     *
     * @param eventID The unique ID of the event.
     * @return A {@link RefundInfo} object containing refund details.
     */
    @Override
    public RefundInfo getRefundInfo(String eventID) {
        String query = "SELECT * FROM refunds WHERE eventID = ?";
        List<Refund> refunds = new ArrayList<>();

        try (Connection connection = dbConnection.getConnection();
             PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setString(1, eventID);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                Refund refund = new Refund(
                        rs.getString("ticketID"),
                        rs.getTimestamp("refundDate").toString(),
                        rs.getDouble("refundAmount"),
                        rs.getString("refundReason")
                );
                refunds.add(refund);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return new RefundInfo(refunds.size(), refunds.stream().mapToDouble(Refund::getRefundAmount).sum(), refunds);
    }
}
