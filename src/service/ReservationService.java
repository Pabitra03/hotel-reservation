package service;

import model.Customer;
import model.IRoom;
import model.Reservation;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class ReservationService {

    // static reference for the singleton
    private static final ReservationService instance = new ReservationService();

    // collections to store and retrieve rooms and reservations
    private final Map<String, IRoom> rooms = new HashMap<>();
    private final Map<String, Collection<Reservation>> reservations = new HashMap<>();

    private ReservationService() {
        // private constructor
    }

    public static ReservationService getInstance() {
        return instance;
    }

    public void addRoom(IRoom room) {
        if (rooms.containsKey(room.getRoomNumber())) {
            throw new IllegalArgumentException("Room number already exists");
        }
        rooms.put(room.getRoomNumber(), room);
    }

    public IRoom getARoom(String roomId) {
        return rooms.get(roomId);
    }

    public Collection<IRoom> getAllRooms() {
        return rooms.values();
    }

    public Reservation reserveARoom(Customer customer, IRoom room, Date checkInDate, Date checkOutDate) {
        // Prevent double booking
        Collection<IRoom> availableRooms = findRooms(checkInDate, checkOutDate);
        if (!availableRooms.contains(room)) {
            throw new IllegalArgumentException("Room is already booked for these dates.");
        }

        Reservation newReservation = new Reservation(customer, room, checkInDate, checkOutDate);

        Collection<Reservation> customerReservations = getCustomersReservation(customer);
        if (customerReservations == null) {
            customerReservations = new ArrayList<>();
        }
        
        customerReservations.add(newReservation);
        reservations.put(customer.getEmail(), customerReservations);

        return newReservation;
    }

    public Collection<IRoom> findRooms(Date checkInDate, Date checkOutDate) {
        Collection<IRoom> availableRooms = new ArrayList<>();

        for (IRoom room : rooms.values()) {
            boolean isAvailable = true;

            for (Collection<Reservation> customerReservations : reservations.values()) {
                for (Reservation reservation : customerReservations) {
                    if (reservation.getRoom().equals(room)) {
                        // Check if the dates overlap
                        // Overlap happens if check-in is before checkout AND checkout is after check-in
                        if (reservation.getCheckInDate().before(checkOutDate) && 
                            reservation.getCheckOutDate().after(checkInDate)) {
                            isAvailable = false;
                            break;
                        }
                    }
                }
            }

            if (isAvailable) {
                availableRooms.add(room);
            }
        }

        return availableRooms;
    }

    public Collection<Reservation> getCustomersReservation(Customer customer) {
        return reservations.get(customer.getEmail());
    }

    public void printAllReservation() {
        boolean hasReservations = false;
        
        for (Collection<Reservation> customerReservations : reservations.values()) {
            for (Reservation reservation : customerReservations) {
                System.out.println(reservation);
                System.out.println("--------------------");
                hasReservations = true;
            }
        }

        if (!hasReservations) {
            System.out.println("There are no reservations right now.");
        }
    }
}
