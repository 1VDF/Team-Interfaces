package repository;

import entity.Booking;
import entity.Customer;

import java.sql.SQLException;
import java.sql.Date;
import java.util.List;

/**
 * This interface will allow the Marketing team to access booking and customer data,
 * through the methods outlined below.
 * <p>
 * This interface provides different methods the Marketing team can use to access
 * data stored in the Box Office database based on various criteria.
 * </p>
 *
 * @author Denis Volocaru
 * @version 1.0
 */
public interface MarketingAccess {

    /**
     * Retrieves all bookings.
     *
     * @return - A list of all bookings.
     * @throws SQLException - This occurs if there is any issue with the database connection.
     */
    List<Booking> getAllBookings() throws SQLException;

    /**
     * Retrieves all bookings based on the entered event ID.
     *
     * @param eventID - The ID of the event.
     * @return - A list of all bookings which match with the specified event ID.
     * @throws SQLException - This occurs if there is any issue with the database connection.
     */
    List<Booking> getAllBookingsByEvent(int eventID) throws SQLException;

    /**
     * Retrieves all bookings which match the entered date.
     *
     * @param year - The year for which the booking should be retrieved.
     * @param month - A string in the format "0-12", denoting the month for the booking that should be retrieved.
     * @param day - The day for which the booking should be retrieved.
     * @return - A list of all bookings that occur on the specified date.
     * @throws SQLException - This occurs if there is any issue with the database connection.
     */
    List<Booking> getAllBookingsByDate(int year, String month, int day) throws SQLException;

    /**
     * Retrieves all bookings where the group size is 12 or more.
     *
     * @return - A list of bookings which have a group size of 12 or more.
     * @throws SQLException - This occurs if there is any issue with the database connection.
     */
    List<Booking> getAllBookingsByQuantity() throws SQLException;

    /**
     * Retrieves all customers who are Friends of Lancaster.
     *
     * @return - A list of customers who are marked as Friends of Lancaster in the database.
     * @throws SQLException - This occurs if there is any issue with the database connection.
     */
    List<Customer> getSpecificCustomerData() throws SQLException;
}
