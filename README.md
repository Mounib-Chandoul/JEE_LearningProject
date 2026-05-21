# ?? JEE Student Management System

[![Java](https://img.shields.io/badge/Java-17-orange.svg)](https://www.oracle.com/java/)
[![Jakarta EE](https://img.shields.io/badge/Jakarta%20EE-10-blue.svg)](https://jakarta.ee/)
[![JavaFX](https://img.shields.io/badge/JavaFX-17-green.svg)](https://openjfx.io/)
[![WildFly](https://img.shields.io/badge/WildFly-30+-red.svg)](https://www.wildfly.org/)

A full-stack enterprise application for managing student records, built with **Jakarta EE 10** (Backend) and **JavaFX** (Frontend). This project demonstrates a decoupled architecture using RESTful services, secure authentication, and JPA persistence.

## ?? Architecture Overview

The project is split into two main modules managed by a parent Maven POM:

- **BackEnd (WAR)**: A Jakarta EE 10 REST API.
  - **JAX-RS**: RESTful endpoints for student and auth management.
  - **EJB**: Stateless beans for business logic.
  - **JPA (Hibernate)**: Persistence layer with H2 in-memory database.
  - **Jakarta Security**: PBKDF2 password hashing.
- **FrontEnd (JAR)**: A modern JavaFX desktop client.
  - **MVC Pattern**: FXML-based views with dedicated controllers.
  - **Asynchronous HTTP**: Uses Java 11 HttpClient for non-blocking API calls.

## ? Key Features

- ?? **Secure Authentication**: Multi-role login system (ADMIN, TEACHER, USER).
- ?? **Student CRUD**: Full Create, Read, Update, and Delete functionality.
- ?? **Interactive Dashboard**: Real-time table updates and data binding.
- ? **Auto-Initialization**: Database auto-seeds on startup via import.sql.
- ?? **Modern UI**: Styled with CSS and responsive FXML layouts.

## ??? Tech Stack

- **Backend**: Jakarta EE 10, JAX-RS, EJB 3.2, JPA 3.0, Hibernate 6.4.
- **Frontend**: JavaFX 17, FXML, CSS.
- **Database**: H2 (In-memory, via ExampleDS).
- **Build Tool**: Apache Maven 3.9+.
- **Server**: WildFly 30+ (Runtime).

## ?? Installation & Setup

### Prerequisites

- JDK 17
- Maven 3.9+
- WildFly Application Server

### 1. Clone & Build

`ash git clone <your-repo-url> cd JJE_PROJECT .\build.cmd `

### 2. Deploy Backend

1. Start your WildFly server.
2. Deploy the generated WAR file: BackEnd\target\JJE_PROJECT-1.0-SNAPSHOT.war.
3. Verify the API is live at http://localhost:8080/JJE_PROJECT-1.0-SNAPSHOT/api/hello-world.

### 3. Run Frontend

`ash .\run-frontend.cmd `

## ?? Default Credentials


| Username | Password  | Role    |
| :------- | :-------- | :------ |
| dmin    | dmin     | ADMIN   |
| mounib   | mounib123 | TEACHER |
| user     | password  | USER    |

## ?? Project Structure

`	ext JJE_PROJECT/ +-- BackEnd/                # Jakarta EE 10 API �   +-- src/main/java/      # EJB, JPA, JAX-RS logic �   +-- src/main/resources/ # Persistence & Import SQL +-- FrontEnd/               # JavaFX Desktop Client �   +-- src/main/java/      # Controllers & HTTP Clients �   +-- src/main/resources/ # FXML Views & CSS +-- pom.xml                 # Maven Parent Configuration +-- build.cmd               # Automation Script `

## ?? License

Distributed under the MIT License. See LICENSE for more information.

---

**Developed by Mounib** - *JEE L2-S2 Project*
