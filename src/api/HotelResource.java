package api;

import model.Customer;
import model.IRoom;
import model.Reservation;
import service.CustomerService;
import service.ReservationService;

import java.util.Collection;
import java.util.Date;
import java.util.Collections;

public class HotelResource {

    // static reference for the singleton
    private static final HotelResource instance = new HotelResource();

    // reference to the services
    private final CustomerService customerService = CustomerService.getInstance();
    private final ReservationService reservationService = ReservationService.getInstance();

    private HotelResource() {
        // private constructor
    }

    public static HotelResource getInstance() {
        return instance;
    }

    public Customer getCustomer(String email) {
        return customerService.getCustomer(email);
    }

    public void createACustomer(String email, String firstName, String lastName) {
        customerService.addCustomer(email, firstName, lastName);
    }

    public IRoom getRoom(String roomNumber) {
        return reservationService.getARoom(roomNumber);
    }

    public Reservation bookARoom(String customerEmail, IRoom room, Date checkInDate, Date CheckOutDate) {
        Customer customer = getCustomer(customerEmail);
        return reservationService.reserveARoom(customer, room, checkInDate, CheckOutDate);
    }

    public Collection<Reservation> getCustomersReservations(String customerEmail) {
        Customer customer = getCustomer(customerEmail);
        
        if (customer == null) {
            return Collections.emptyList();
        }
        
        return reservationService.getCustomersReservation(customer);
    }

    public Collection<IRoom> findARoom(Date checkIn, Date checkOut) {
        return reservationService.findRooms(checkIn, checkOut);
    }
}
