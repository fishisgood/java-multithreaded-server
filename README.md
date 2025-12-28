# Multi-threaded Server and Registration System

This project implements a multi-threaded server in Java that manages two distinct services: a Joke Service (Knock-Knock Protocol) and a Registration Service (Ruppin Registration Protocol).

## System Architecture

The system follows a Server-Client architecture using TCP sockets:

* **Multi-threading**: The server utilizes a thread-per-connection model. Every incoming client connection is handled by a separate `ClientHandler` thread to ensure non-blocking service.
* **Protocol Selection**: The server listens on two dedicated ports. Port **4444** is assigned to the Knock-Knock Protocol (KKP), and Port **4445** is assigned to the Ruppin Registration Protocol (RUPPIN).
* **State Management**: A synchronized list of `Client` objects is maintained by the server to ensure thread-safe data access across multiple concurrent connections.

---

## File Descriptions

* **`MultiThreadedServer.java`**: The main server class. It initializes the shared client state and starts two listener threads for the respective ports.
* **`ClientHandler.java`**: Manages the communication lifecycle of a client. It handles the I/O streams and executes the backup logic.
* **`RuppinRegistrationProtocol.java`**: A Finite State Machine (FSM) that manages the registration and update logic. It handles complex dialogues including step-by-step user information updates.
* **`Client.java`**: A data entity class representing a user. It contains fields for username, password, academic status, and years of study, along with static validation logic.
* **`MainClient.java`**: The client-side application. It allows the user to choose a service and establishes a socket connection to the server.

---

## Key Features

### User Registration and Update Flow

The registration protocol manages new user creation and existing user updates:

* **New Users**: Collects username, password, status, and years of study.
* **Existing Users**: Authenticates the user and provides a granular update menu, allowing the user to change specific fields (Username, Password, or Years) individually while skipping others.
* **Validation**: Enforces password strength (minimum 9 characters, including uppercase, lowercase, and a digit) and valid academic status.

### Automated CSV Backup (Creativity Clause)

The system includes an automated backup mechanism:

* **Trigger**: A backup is generated every time the number of registered clients in the list is a multiple of 3 (3, 6, 9, etc.).
* **File Format**: Data is exported to a CSV file where each entry is comma-separated.
* **Naming Convention**: Files are named `backup_yyyyMMdd_HHmmss.csv` using the current date and time to prevent overwriting previous backups.

### Clean Connection Termination

To ensure resources are released properly, the server and client utilize a "Bye." termination string:

* When the protocol reaches the final state, it appends "Bye." to the output.
* Both the `ClientHandler` and `MainClient` detect this suffix and break their communication loops to close the socket.

---

## Execution Instructions

1. **Start the Server**: Compile and run `MultiThreadedServer.java`. The console will indicate that the server is listening on ports 4444 and 4445.
2. **Start the Client**: Compile and run `MainClient.java`.
3. **Choose Service**: Enter `1` for the Joke service or `2` for the Registration service.
4. **Follow Prompts**: Input the required information as prompted by the server.
