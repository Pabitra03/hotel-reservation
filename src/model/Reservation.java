package model;

import java.util.Date;

public class Reservation {
    private final Customer customer;
    private final IRoom room;
    private final Date checkInDate;
    private final Date checkOutDate;

    public Reservation(Customer customer, IRoom room, Date checkInDate, Date checkOutDate) {
        if (checkInDate != null && checkOutDate != null && !checkOutDate.after(checkInDate)) {
            throw new IllegalArgumentException("Check-out date must be strictly after check-in date.");
        }
        this.customer = customer;
        this.room = room;
        this.checkInDate = checkInDate;
        this.checkOutDate = checkOutDate;
    }

    public Customer getCustomer() {
        return customer;
    }

    public IRoom getRoom() {
        return room;
    }

    public Date getCheckInDate() {
        return checkInDate;
    }

    public Date getCheckOutDate() {
        return checkOutDate;
    }

    @Override
    public String toString() {
        java.text.SimpleDateFormat dateFormat = new java.text.SimpleDateFormat("EEE MMM dd yyyy");
        String bedType = room.getRoomType() == RoomType.SINGLE ? "Single bed" : "Double bed";
        return "Reservation\n" +
               customer.getFirstName() + " " + customer.getLastName() + "\n" +
               "Room: " + room.getRoomNumber() + " - " + bedType + "\n" +
               "Price: $" + room.getRoomPrice() + " price per night\n" +
               "Checkin Date: " + dateFormat.format(checkInDate) + "\n" +
               "Checkout Date: " + dateFormat.format(checkOutDate);
    }
}
