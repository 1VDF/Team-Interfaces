package repository;

import database.DbConnection;
import entity.Booking;
import entity.Customer;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * This class implements the MarketingAccess interface.
 * <p>
 * Through the implementation, access to the booking and customer data is granted.
 * This class connects to the database and executes SQL qureries to retrieve the relevant information,
 * matching with the requirements produced by the Marketing Team.
 * </p>
 *
 * @author Denis Volocaru
 * @version 1.0
 */
public class MarketingAccessImpl implements MarketingAccess {

    /**
     * This is a generic SQL query to select all of the bookings from the database.
     * This has been created to avoid repetition.
     */
    private final String SELECT_STATEMENT = "SELECT * FROM `bookings`";

    /**
     * This method parses a ResultSet into a list of Booking objects.
     * This method will run through the entirety of the Booking table in the database,
     * and print out all data of each row.
     *
     * @param rs - The ResultSet which contains booking data.
     * @return - Returns a list of all objects of type Booking, which are present,
     * in the database.
     * @throws SQLException - This happens if an SQL error occurs during parsing.
     */
    private static List<Booking> parseBookingRows(ResultSet rs) throws SQLException {
        List<Booking> bookings = new ArrayList<>();
        while (rs.next()) {
            bookings.add(new Booking(rs.getInt("bookingID"),
                    rs.getInt("customerID"),
                    rs.getInt("eventID"),
                    rs.getDouble("price"),
                    rs.getInt("quantity"),
                    rs.getString("bookingDate"),
                    rs.getString("discountType")));
        }
        for (Booking b : bookings) {
            System.out.println(b);
        }
        rs.close();
        return bookings;
    }

    /**
     * This method parses a ResultSet into a list of Customer objects.
     * This method will run through the entirety of the Customer table in the database,
     * and print out all data of each row.
     *
     * @param rs - The ResultSet which contains customer data.
     * @return - Returns a list of all objects of type Customer, which are present,
     * in the database.
     * @throws SQLException - This happens if an SQL error occurs during parsing.
     */
    private static List<Customer> parseCustomerRows(ResultSet rs) throws SQLException {
        List<Customer> customers = new ArrayList<>();
        while (rs.next()) {
            customers.add(new Customer(rs.getInt("customerID"),
                    rs.getString("customerName"),
                    rs.getString("email"),
                    rs.getString("phoneNumber"),
                    rs.getBoolean("friendMember")));
        }
        for (Customer c : customers) {
            System.out.println(c);
        }
        rs.close();
        return customers;
    }

    /**
     * This method will retrieve all bookings from the database.
     * <p>
     * Within this method the connection to the database is made.
     * After the block has finished execution, the connection to the database,
     * will automatically close due to AutoClosable method te Connection class has.
     * An SQL query is executed within this method.
     * </p>
     *
     * @return - A list of all bookings.
     * @throws SQLException - This occurs if there is any issue with the database connection.
     */
    @Override
    public List<Booking> getAllBookings() throws SQLException {
        try (Connection connection = DriverManager.getConnection(DbConnection.url, DbConnection.user, DbConnection.pass);
             Statement statement = connection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,
                     ResultSet.CONCUR_READ_ONLY)) {
            ResultSet rs = statement.executeQuery(SELECT_STATEMENT);
            return parseBookingRows(rs);
        }
    }

    /**
     * This method will retrieve all bookings from the database with a specific eventID.
     * <p>
     * Within this method the connection to the database is made.
     * After the block has finished execution, the connection to the database,
     * will automatically close due to AutoClosable method te Connection class has.
     * An SQL query is executed within this method.
     * </p>
     *
     * @param eventID - The ID of the event to be searched for.
     * @return - A list of all bookings with the specified eventID.
     * @throws SQLException - This occurs if there is any issue with the database connection.
     */
    @Override
    public List<Booking> getAllBookingsByEvent(int eventID) throws SQLException {
        try (Connection connection = DriverManager.getConnection(DbConnection.url, DbConnection.user, DbConnection.pass);
             Statement statement = connection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,
                ResultSet.CONCUR_READ_ONLY)) {
            String whereClause = "WHERE eventID = " + eventID;
            ResultSet rs = statement.executeQuery(SELECT_STATEMENT + whereClause);
            return parseBookingRows(rs);
        }
    }

    /**
     * This method will retrieve all bookings from the database which occured on that date.
     * <p>
     * Within this method the connection to the database is made.
     * After the block has finished execution, the connection to the database,
     * will automatically close due to AutoClosable method te Connection class has.
     * An SQL query is executed within this method.
     * </p>
     *
     * @param year - The year for which the booking should be retrieved.
     * @param month - A string in the format "0-12", denoting the month for the booking that should be retrieved.
     * @param day - The day for which the booking should be retrieved.
     * @return - A list of all bookings on the specified date.
     * @throws SQLException - This occurs if there is any issue with the database connection.
     */
    @Override
    public List<Booking> getAllBookingsByDate(int year, String month, int day) throws SQLException {
        try (Connection connection = DriverManager.getConnection(DbConnection.url, DbConnection.user, DbConnection.pass);
             Statement statement = connection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,
                     ResultSet.CONCUR_READ_ONLY)) {
            String whereClause = " WHERE bookingDate = " + year + month + day;
            ResultSet rs = statement.executeQuery(SELECT_STATEMENT + whereClause);
            return parseBookingRows(rs);
        }
    }


    /**
     * This method will retrieve all bookings from the database which were made for groups of 12 or more people.
     * <p>
     * Within this method the connection to the database is made.
     * After the block has finished execution, the connection to the database,
     * will automatically close due to AutoClosable method te Connection class has.
     * An SQL query is executed within this method.
     * </p>
     *
     * @return - A list of all bookings with a group of 12 or more people.
     * @throws SQLException - This occurs if there is any issue with the database connection.
     */
    @Override
    public List<Booking> getAllBookingsByQuantity() throws SQLException {
        try (Connection connection = DriverManager.getConnection(DbConnection.url, DbConnection.user, DbConnection.pass);
             Statement statement = connection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,
                     ResultSet.CONCUR_READ_ONLY)) {
            String whereClause = "WHERE quantity >= 12 ";
            ResultSet rs = statement.executeQuery(SELECT_STATEMENT + whereClause);
            return parseBookingRows(rs);
        }
    }

    /**
     * This method will retrieve all customers from the database which are Friends of Lancaster.
     * <p>
     * Within this method the connection to the database is made.
     * After the block has finished execution, the connection to the database,
     * will automatically close due to AutoClosable method te Connection class has.
     * An SQL query is executed within this method.
     * </p>
     *
     * @return - A list of all customers which are Friends of Lancaster.
     * @throws SQLException - This occurs if there is any issue with the database connection.
     */
    @Override
    public List<Customer> getSpecificCustomerData() throws SQLException {
        try (Connection connection = DriverManager.getConnection(DbConnection.url, DbConnection.user, DbConnection.pass);
             Statement statement = connection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,
                     ResultSet.CONCUR_READ_ONLY)) {
            ResultSet rs = statement.executeQuery("SELECT * FROM `customers` WHERE friendMember = 1");
            return parseCustomerRows(rs);
        }
    }
}
