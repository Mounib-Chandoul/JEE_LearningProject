# 🧩 JEE Student Management System

<div align="center">

[![Java](https://img.shields.io/badge/Java-17-007396?style=for-the-badge&logo=java&logoColor=white)](https://www.oracle.com/java/)
[![Jakarta EE](https://img.shields.io/badge/Jakarta%20EE-10-0082C3?style=for-the-badge)](https://jakarta.ee/)
[![JavaFX](https://img.shields.io/badge/JavaFX-17-3B7EB6?style=for-the-badge)](https://openjfx.io/)
[![WildFly](https://img.shields.io/badge/WildFly-30%2B-EA283C?style=for-the-badge)](https://www.wildfly.org/)
[![License: MIT](https://img.shields.io/badge/License-MIT-9ECE6A?style=for-the-badge)](LICENSE)

</div>

A full-stack enterprise application for managing student records: a **Jakarta
EE 10 REST backend** deployed on WildFly and a **JavaFX desktop client**. Built
as an L2-S2 JEE project to practise JAX-RS, JPA, EJB, security and decoupled
client/server design.

---

## Architecture

```text
┌────────────────────────┐        HTTP/JSON        ┌────────────────────────┐
│  JavaFX desktop client │ ──────────────────────▶ │  Jakarta EE REST API   │
│  - FXML views          │                         │  - JAX-RS resources    │
│  - controllers         │  ◀───────────────────── │  - EJB business logic  │
│  - Java HttpClient     │       student data      │  - JPA / Hibernate     │
└────────────────────────┘                         │  - H2 (ExampleDS)      │
                                                    └────────────────────────┘
```

- **BackEnd (WAR)** — JAX-RS endpoints for auth and students, stateless EJBs,
  JPA persistence, PBKDF2 password hashing.
- **FrontEnd (JAR)** — FXML views with dedicated controllers and non-blocking
  calls through Java 11's `HttpClient`.

---

## Features

- Multi-role authentication: `ADMIN`, `TEACHER`, `USER`.
- Full student CRUD from the desktop client.
- Live-updating table with data binding.
- Database auto-seeded on startup via `import.sql`.
- Styled FXML layouts with responsive CSS.

---

## Stack

| Layer | Technology |
|---|---|
| Backend | Jakarta EE 10, JAX-RS, EJB 3.2, JPA 3.0, Hibernate 6.4 |
| Frontend | JavaFX 17, FXML, CSS |
| Database | H2 in-memory (WildFly `ExampleDS`) |
| Server | WildFly 30+ |
| Build | Maven 3.9+ (parent POM with two modules) |

---

## Build & run

Prerequisites: JDK 17, Maven 3.9+, WildFly 30+.

```bash
# 1. build both modules
.\build.cmd          # or: mvn clean package

# 2. start WildFly and deploy
#    BackEnd\target\jje_project_parent-BackEnd-1.0-SNAPSHOT.war

# 3. verify the API
#    http://localhost:8080/<deployed-name>/api/hello-world

# 4. launch the desktop client
.\run-frontend.cmd
```

### Default credentials

| Username | Password | Role |
|---|---|---|
| `admin` | `admin` | ADMIN |
| `mounib` | `mounib123` | TEACHER |
| `user` | `password` | USER |

> Change these before deploying anywhere beyond localhost.

---

## Layout

```text
JEE_LearningProject/
├── pom.xml                # parent POM
├── BackEnd/               # Jakarta EE WAR: resources, EJB, JPA, JAX-RS
├── FrontEnd/              # JavaFX JAR: controllers, FXML views, CSS
├── build.cmd / run-frontend.cmd
└── *_report.tex           # project documentation sources
```

## License

[MIT](LICENSE).
