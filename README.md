# Multi-threaded Ticket Management System

## Overview

This Java-based multi-threaded application simulates a ticket management system. The system involves vendors releasing tickets into a pool and customers purchasing tickets concurrently. The application supports multiple configurations and logs all activities for review.

## Features

- **Configuration System**: Configure initial ticket count, maximum capacity, vendor ticket release rate, and customer ticket purchase rate.
- **Multi-threaded Execution**: Uses a thread pool to manage vendors and customers concurrently.
- **Dynamic Logging**: Logs all activities (e.g., tickets added, tickets purchased, errors) into a timestamped log file.
- **Persistence**: Saves and loads system configurations from text files.
- **Interactive Commands**: Allows users to start a new configuration or load an existing one.

## Components

1. **Main.java**: Entry point of the application. Manages user commands, configuration, and system initialization.
2. **TicketSystem.java**: Core class managing the ticket pool, including adding and purchasing tickets.
3. **TicketSystemConfig.java**: Handles configuration input and persistence.
4. **Vendor.java**: Simulates vendors adding tickets to the pool.
5. **Customer.java**: Simulates customers purchasing tickets from the pool.
6. **Logger.java**: Provides synchronized logging to both the console and a file.

## How to Run

1. **Compile the Source Code**:
   Ensure all `.java` files are in the same directory. Use the following command to compile:
   ```bash
   javac *.java

or

2. **In intellij run the Main.java file** or you used file as a Main 


