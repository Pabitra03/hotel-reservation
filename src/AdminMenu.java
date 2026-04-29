import api.AdminResource;
import model.Customer;
import model.IRoom;
import model.Room;
import model.FreeRoom;
import model.RoomType;

import java.util.Collections;
import java.util.Collection;
import java.util.Scanner;

public class AdminMenu {
    private static final AdminResource adminResource = AdminResource.getInstance();

    public static void start() {
        Scanner scanner = new Scanner(System.in);
        boolean keepRunning = true;

        while (keepRunning) {
            System.out.println("\nAdmin Menu");
            System.out.println("----------------------------------------");
            System.out.println("1. See all Customers");
            System.out.println("2. See all Rooms");
            System.out.println("3. See all Reservations");
            System.out.println("4. Add a Room");
            System.out.println("5. Back to Main Menu");
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
                    Collection<Customer> customers = adminResource.getAllCustomers();
                    if (customers.isEmpty()) {
                        System.out.println("No customers found.");
                    } else {
                        for (Customer customer : customers) {
                            System.out.println(customer);
                        }
                    }
                    break;
                case 2:
                    Collection<IRoom> rooms = adminResource.getAllRooms();
                    if (rooms.isEmpty()) {
                        System.out.println("No rooms found.");
                    } else {
                        for (IRoom room : rooms) {
                            System.out.println(room);
                        }
                    }
                    break;
                case 3:
                    adminResource.displayAllReservations();
                    break;
                case 4:
                    addARoom(scanner);
                    break;
                case 5:
                    System.out.println("Returning to Main Menu...");
                    keepRunning = false;
                    break;
                default:
                    System.out.println("Invalid selection. Please choose a number from 1 to 5.");
            }
        }
    }

    private static void addARoom(Scanner scanner) {
        boolean addAnother = true;
        while (addAnother) {
            try {
                System.out.println("Enter room number:");
                String roomNumber = scanner.nextLine();

                System.out.println("Enter price per night:");
                Double price = Double.parseDouble(scanner.nextLine());

                System.out.println("Enter room type: 1 for single bed, 2 for double bed");
                int roomTypeSelection = Integer.parseInt(scanner.nextLine());
                RoomType roomType = roomTypeSelection == 1 ? RoomType.SINGLE : RoomType.DOUBLE;

                IRoom room;
                if (price == 0.0) {
                    room = new FreeRoom(roomNumber, roomType);
                } else {
                    room = new Room(roomNumber, price, roomType);
                }

                adminResource.addRoom(Collections.singletonList(room));
                System.out.println("Room added successfully!");
                
                boolean validAnswer = false;
                while (!validAnswer) {
                    System.out.println("Would you like to add another room y/n");
                    String answer = scanner.nextLine().trim();
                    if (answer.equalsIgnoreCase("y") || answer.equalsIgnoreCase("yes")) {
                        validAnswer = true;
                    } else if (answer.equalsIgnoreCase("n") || answer.equalsIgnoreCase("no")) {
                        validAnswer = true;
                        addAnother = false;
                    } else {
                        System.out.println("Please enter Y (Yes) or N (No)");
                    }
                }
            } catch (IllegalArgumentException e) {
                System.out.println("Error adding room: " + e.getMessage());
            } catch (Exception e) {
                System.out.println("Invalid input. Please try again.");
            }
        }
    }
}
