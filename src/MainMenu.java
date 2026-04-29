import api.HotelResource;
import model.IRoom;
import model.Reservation;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Date;
import java.util.Scanner;
import java.util.Calendar;

public class MainMenu {
    private static final HotelResource hotelResource = HotelResource.getInstance();

    public static void main(String[] args) {
        start();
    }

    public static void start() {
        Scanner scanner = new Scanner(System.in);
        boolean keepRunning = true;

        while (keepRunning) {
            System.out.println("\nMain Menu");
            System.out.println("----------------------------------------");
            System.out.println("1. Find and reserve a room");
            System.out.println("2. See my reservations");
            System.out.println("3. Create an account");
            System.out.println("4. Admin");
            System.out.println("5. Exit");
            System.out.println("----------------------------------------");
            System.out.println("Please select a number for the menu option:");

            int selection;
            try {
                selection = Integer.parseInt(scanner.nextLine());
            } catch (NumberFormatException e) {
                System.out.println("Invalid input. Please enter a number.");
                continue;
            }

            switch (selection) {
                case 1:
                    findAndReserveRoom(scanner);
                    break;
                case 2:
                    seeMyReservations(scanner);
                    break;
                case 3:
                    createAccount(scanner);
                    break;
                case 4:
                    AdminMenu.start();
                    break;
                case 5:
                    System.out.println("Exiting the application.");
                    keepRunning = false;
                    break;
                default:
                    System.out.println("Invalid selection. Please choose a number from 1 to 5.");
            }
        }
    }

    private static void findAndReserveRoom(Scanner scanner) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");
        dateFormat.setLenient(false); // Reject invalid dates

        try {
            System.out.println("Enter Check-In Date mm/dd/yyyy example 02/01/2025");
            Date checkIn = dateFormat.parse(scanner.nextLine());

            Date today = new Date();
            Calendar cal = Calendar.getInstance();
            cal.setTime(today);
            cal.set(Calendar.HOUR_OF_DAY, 0);
            cal.set(Calendar.MINUTE, 0);
            cal.set(Calendar.SECOND, 0);
            cal.set(Calendar.MILLISECOND, 0);
            if (checkIn.before(cal.getTime())) {
                System.out.println("Reservations cannot be made in the past.");
                return;
            }

            System.out.println("Enter Check-Out Date mm/dd/yyyy example 02/21/2025");
            Date checkOut = dateFormat.parse(scanner.nextLine());

            Collection<IRoom> availableRooms = hotelResource.findARoom(checkIn, checkOut);

            if (availableRooms.isEmpty()) {
                System.out.println("No rooms available for those dates. Searching for recommendations 7 days later...");
                
                Calendar calendar = Calendar.getInstance();
                calendar.setTime(checkIn);
                calendar.add(Calendar.DAY_OF_MONTH, 7);
                Date recommendedCheckIn = calendar.getTime();

                calendar.setTime(checkOut);
                calendar.add(Calendar.DAY_OF_MONTH, 7);
                Date recommendedCheckOut = calendar.getTime();

                availableRooms = hotelResource.findARoom(recommendedCheckIn, recommendedCheckOut);

                if (availableRooms.isEmpty()) {
                    System.out.println("No rooms available for the recommended dates either. Please try another search.");
                    return;
                } else {
                    System.out.println("Recommended rooms for dates: " + dateFormat.format(recommendedCheckIn) + " to " + dateFormat.format(recommendedCheckOut));
                    checkIn = recommendedCheckIn;
                    checkOut = recommendedCheckOut;
                }
            }

            System.out.println("Available rooms:");
            for (IRoom room : availableRooms) {
                System.out.println(room);
            }

            System.out.println("Would you like to book a room? y/n");
            String bookRoom = scanner.nextLine();

            if (bookRoom.equalsIgnoreCase("y")) {
                System.out.println("Do you have an account with us? y/n");
                String hasAccount = scanner.nextLine();

                if (hasAccount.equalsIgnoreCase("y")) {
                    System.out.println("Enter Email format: name@domain.com");
                    String email = scanner.nextLine();

                    if (hotelResource.getCustomer(email) == null) {
                        System.out.println("Customer not found. Please create an account first.");
                        return;
                    }

                    System.out.println("What room number would you like to reserve?");
                    String roomNumber = scanner.nextLine();

                    IRoom roomToBook = hotelResource.getRoom(roomNumber);
                    if (roomToBook == null || !availableRooms.contains(roomToBook)) {
                        System.out.println("Invalid room selection. It might be booked.");
                        return;
                    }

                    Reservation reservation = hotelResource.bookARoom(email, roomToBook, checkIn, checkOut);
                    System.out.println("Reservation successful!");
                    System.out.println(reservation);
                } else {
                    System.out.println("Please create an account from the Main Menu first.");
                }
            }
        } catch (ParseException e) {
            System.out.println("Invalid date format. Please use mm/dd/yyyy. Ensure the date is real.");
        } catch (IllegalArgumentException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    private static void seeMyReservations(Scanner scanner) {
        System.out.println("Enter your Email format: name@domain.com");
        String email = scanner.nextLine();

        Collection<Reservation> reservations = hotelResource.getCustomersReservations(email);
        if (reservations == null || reservations.isEmpty()) {
            System.out.println("No reservations found for this email.");
        } else {
            for (Reservation reservation : reservations) {
                System.out.println(reservation);
            }
        }
    }

    private static void createAccount(Scanner scanner) {
        try {
            System.out.println("Enter Email format: name@domain.com");
            String email = scanner.nextLine();

            System.out.println("First Name:");
            String firstName = scanner.nextLine();

            System.out.println("Last Name:");
            String lastName = scanner.nextLine();

            hotelResource.createACustomer(email, firstName, lastName);
            System.out.println("Account created successfully!");
        } catch (IllegalArgumentException e) {
            System.out.println("Error creating account: " + e.getMessage());
        }
    }
}
