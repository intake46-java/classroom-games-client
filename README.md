# Class Room Games

## Project Overview

This application is a robust, enterprise-grade implementation of Tic-Tac-Toe, engineered using a scalable client–server architecture. Designed to demonstrate high-concurrency handling and real-time state synchronization, the platform allows multiple clients to interact seamlessly across a network. The system prioritizes modular design, data persistence, and a responsive user interface to ensure a premium user experience.

## Key Features

### Gameplay Modes

* **Single Player:** Offline mode featuring an extensible AI opponent.
* **Local Multiplayer:** Two-player support on a single terminal.
* **Online Multiplayer:** Real-time competitive play across the network.

### Network Capabilities

* **User Authentication:** Secure registration and login workflows for player identity management.
* **Live Presence:** Real-time visibility of online users within the lobby.
* **Challenge System:** Interactive invitation mechanism allowing players to send, accept, or reject game requests.

### Data & Analytics

* **Score Tracking:** Persistent tracking of player wins, losses, and draws.
* **Game Replay:** Full recording of game sessions, allowing users to replay matches move-by-move for analysis.

### User Experience

* **Intuitive Interface:** A clean, responsive Graphical User Interface (GUI) designed for ease of navigation.
* **Multimedia Rewards:** Integration of video playback triggers upon victory.

## Server Specifications

The server functions as the central authority for state management and communication routing.

* **Session Management:** Handles concurrent client connections using a threaded architecture.
* **Real-Time Data Exchange:** Low-latency broadcasting of moves, chat, and status updates using Java Sockets.
* **Administrative Dashboard:** A dedicated GUI for server administrators featuring:
* Start/Stop server controls.
* Live telemetry displaying total online/offline user statistics.



## System Architecture

The project adheres to strict software engineering principles to ensure maintainability and scalability.

* **Client–Server Model:** Decouples the presentation layer from the business logic and data layer.
* **Concurrency:** Utilizes multithreading to manage multiple simultaneous socket connections without blocking.
* **Persistence:** Integrates with a database backend to store user profiles, credentials, and match history.
* **Modularity:** The codebase is organized into distinct packages, facilitating future extensions and maintenance.

## Technology Stack

* **Language:** Java
* **Networking:** Java Sockets (TCP/IP)
* **Concurrency:** Java Multithreading framework
* **User Interface:** JavaFX / Swing
* **Persistence:** Relational Database Management System (RDBMS)
* **Version Control:** Git

## Screens
<img src="./records/client%20(1).png" width="400" alt="Login Screen">	<img src="./records/client%20(2).png" width="400" alt="Registration">
<img src="./records/client%20(4).png" width="400" alt="Lobby Interface">	<img src="./records/client%20(5).png" width="400" alt="Player List">
<img src="./records/client%20(6).png" width="400" alt="Game Invitation">	<img src="./records/client%20(7).png" width="400" alt="Gameplay Board">
<img src="./records/client%20(8).png" width="400" alt="In-Game Chat">	<img src="./records/client%20(9).png" width="400" alt="Victory Screen">
<img src="./records/client%20(10).png" width="400" alt="Replay Mode">	<img src="./records/client%20(11).png" width="400" alt="Scoreboard">

## Setup and Installation

### Prerequisites

* Java Development Kit (JDK) 11 or higher.
* Database server configured and running.

### Configuration

1. Clone the repository.
2. Configure database connection strings in the server properties file.
3. Ensure network ports are available for socket binding.

### Execution

1. **Launch Server:** Start the server application first to initialize the socket listener and database connection.
2. **Launch Client:** Run multiple instances of the client application to simulate concurrent users.

## Development Team
[Abdelrahman Rashed Ali](https://github.com/abdelrahman-rashed-ali) </br>
[Osama Khaled](https://github.com/OsamaEmam314)</br>
[Mina Nashaat](https://github.com/MinaNashaat)</br>
[Youmna Elzairy](https://github.com/Yomnaa-Elzairy)</br>


