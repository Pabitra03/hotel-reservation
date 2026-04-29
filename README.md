# Hotel Reservation Application

A Java-based hotel reservation system built using Object-Oriented Programming principles. The app runs on the command line and lets customers search for rooms, make reservations, and manage bookings.

---

## Tech Stack

- Java 14+
- Java Collections (in-memory storage)
- CLI (Command Line Interface)

---

## Project Structure

```
hotelReservation/
└── src/
    ├── api/        
    ├── model/            
    └── service/          
```

---

## Model Classes

| Class | Description |
|---|---|
| `RoomType` | Enum — SINGLE or DOUBLE occupancy |
| `IRoom` | Interface defining room contract |
| `Room` | Implements IRoom — paid room with price per night |
| `FreeRoom` | Extends Room — price is $0, displays "Free" |
| `Customer` | Customer account with email validation |
| `Reservation` | Links a customer, room, check-in and check-out dates |

---

## Features

### User
- Create a customer account
- Search available rooms by check-in and check-out date
- Book a room and create a reservation
- View all personal reservations

### Admin
- View all customers
- View all rooms
- View all reservations
- Add a new room

---

## Business Rules

- A room cannot be double-booked for overlapping dates
- If no rooms are available, the system suggests rooms with dates shifted +7 days
- Check-out date must be strictly after check-in date
- Past dates are not allowed
- Reservations must be at least 1 day long
- Free rooms show "Free" instead of a price
- Polymorphism is used across IRoom → Room → FreeRoom

---

## Validation

- Email must match format `name@domain.com` — validated with RegEx, throws `IllegalArgumentException` if invalid
- Room numbers must be unique and non-empty
- Room price must be numeric and non-negative
- Room type must be SINGLE or DOUBLE only
- All user input is wrapped in try/catch — no crashes

---

## Architecture

The app follows a layered architecture with no cross-layer communication:

```
UI Components → Resources (API) → Services → Models
```

- UI never talks directly to Services
- Services handle all business logic
- Models represent the domain data

---

## How to Run

1. Make sure Java 14 or higher is installed
2. Open the project in IntelliJ IDEA (or Antigravity)
3. Run `HotelApplication.java` inside the `ui` package
4. Follow the CLI menus to interact with the app

---

## Notes

- All data is stored in-memory using Java Collections (no database)
- At least one model class overrides `hashCode()` and `equals()` for Collections support
- `toString()` is overridden in Room, FreeRoom, Customer, and Reservation for readable output
