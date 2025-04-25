# Parking Management System

A comprehensive Java application for managing parking lots, bookings, and users within a university environment. This system features role-based access control, payment processing, and real-time parking space management.

## Features

- **User Management**
  - Different user types (Student, Faculty, Staff, Visitor)
  - Authentication and authorization
  - Profile management

- **Parking Management**
  - Real-time parking space availability tracking
  - Sensor data integration
  - Parking space reservation

- **Booking System**
  - Create, modify, and cancel bookings
  - Booking history
  - Iterator pattern implementation

- **Payment Processing**
  - Payment calculation based on user type and duration
  - Multiple payment methods
  - Receipt generation

- **Management Interface**
  - Administrative dashboard
  - Reports and analytics
  - User management

## Project Structure

The project is organized into the following main packages:

- `com.company` - Main package containing all application code
  - User-related classes (`User`, `Student`, `FacultyMember`, etc.)
  - Parking management (`ParkingLot`, `ParkingSpace`, `ParkingLotManager`, etc.)
  - Booking system (`Booking`, `BookingDatabaseHelper`, `BookingIterator`, etc.)
  - Authentication (`AuthenticationService`, `StrongPasswordRecognizer`, etc.)
  - Payment processing (`Payment`, `PriceCalculator`, etc.)

## Design Patterns

The application implements several design patterns:
- Builder Pattern (Booking class)
- Iterator Pattern (for traversing bookings and parking spaces)
- Observer Pattern (for notifications)
- Factory Pattern (for user creation)
- Facade Pattern (simplified interfaces for registration and booking)

## Testing

The project includes:
- Manual unit tests for all main components
- Randoop-generated tests for automated testing
- Mutation testing with PIT to evaluate test quality

## Getting Started

### Prerequisites
- Java 8 or higher
- Maven for dependency management

### Setup
1. Clone the repository
2. Run `mvn clean install` to build the project
3. Run the application using `mvn exec:java`

## License

This project is for educational purposes. 