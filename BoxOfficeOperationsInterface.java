public interface BoxOfficeOperationsInterface {

    /**
     * Method to get seating configuration for an event.
     * @param eventID - Unique ID of the event.
     * @return SeatConfiguration - Contains seating details for the event.
     */
    SeatConfiguration getSeatingConfiguration(String eventID);

    /**
     * Method to get ticket sales information for an event.
     * @param eventID - Unique ID of the event.
     * @return TicketSales - Contains sales data such as sold tickets, available seats, etc.
     */
    TicketSales getTicketSales(String eventID);

    /**
     * Method to get guest check-in information for an event.
     * @param eventID - Unique ID of the event.
     * @return GuestCheckIn - Contains guest attendance details such as check-ins and capacity.
     */
    GuestCheckIn getGuestCheckIn(String eventID);

    /**
     * Method to get refund information for an event.
     * @param eventID - Unique ID of the event.
     * @return RefundInfo - Contains refund details such as refund count and total amount.
     */
    RefundInfo getRefundInfo(String eventID);
}
