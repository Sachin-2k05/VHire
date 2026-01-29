# 🚀 VHire – Role-Based Hiring Platform (Backend)

VHire is a production-oriented backend system designed to facilitate secure, role-based interactions between **Companies** and **Workers**. The platform focuses on real-world backend concerns such as stateless authentication, fine-grained authorization, strict business rule enforcement, and controlled state transitions.

Unlike basic CRUD applications, VHire implements a state-driven booking workflow, enforces method-level security, and models real hiring constraints such as availability windows, role ownership, and transition validation.

---

## 🧠 Problem Statement

In many real-world hiring scenarios—especially for short-term or task-based work—companies struggle to find workers with clear availability, while workers face unstructured job requests and poor scheduling control.

**Common issues include:**
* Companies sending booking requests without knowing if a worker is available.
* Workers receiving overlapping or last-minute requests.
* Lack of accountability over who can accept, cancel, or modify a booking.
* No clear ownership of actions once a booking is created.

**These problems lead to:**
* Missed work opportunities and scheduling conflicts.
* Poor trust between companies and workers.
* Manual coordination outside the platform.

---

## 💡 Solution Overview

VHire introduces a backend-driven hiring workflow where:
1.  **Workers** explicitly define availability slots.
2.  **Companies** can only request bookings within valid availability windows.
3.  Each action (request, accept, reject, cancel) is **strictly role-controlled**.
4.  **Booking state transitions** are validated to prevent invalid workflows.

The result is a predictable, secure, and scalable hiring system that mirrors how real-world platforms manage temporary or skill-based work engagements.

---

## 🛠️ Tech Stack

| Category | Technology |
| :--- | :--- |
| **Language** | Java 17 |
| **Framework** | Spring Boot 3.x |
| **Security** | Spring Security, JWT (Stateless) |
| **Persistence** | Spring Data JPA (Hibernate) |
| **Database** | MySQL |
| **Build Tool** | Maven |
| **Documentation** | Swagger / OpenAPI |
| **Email** | JavaMailSender (SMTP) |

---

## 🔐 Security Architecture

### Authentication
VHire uses stateless **JWT-based authentication**. Users authenticate via email and password and receive a JWT token, which must be included in the header:
`Authorization: Bearer <token>`

### Authorization
Role-based access is enforced at the method level using `@PreAuthorize`.
* **👷 Worker:** Can manage profile, availability, and respond to bookings.
* **🏢 Company:** Can browse workers, create booking requests, and manage hiring history.

This ensures **zero trust** by default and prevents role escalation.

---

## 📧 Email Notification System

VHire includes an event-driven email notification system to simulate real-world platform behavior. Emails are triggered automatically for key workflow events:

* 📩 **Booking request sent** (Company → Worker)
* ✅ **Booking accepted** (Worker → Company)
* ❌ **Booking rejected**
* 🚫 **Booking cancelled**

**Implementation Details:**
* Built using `JavaMailSender`.
* Configurable via environment variables.
* Fully decoupled from business logic using a dedicated `EmailService`.

---

## 📦 Core Features

### 1. User & Role Management
* Unified User entity with role-based behavior.
* JWT-backed stateless sessions.
* Method-level authorization.

### 2. Worker Profile Management
Workers manage profiles containing **Skills, Experience, Hourly Rate, and Bio**. Profiles include an activation flag to control public visibility. Companies can view profiles but cannot modify them.

### 3. Availability Management
Workers define when they are available. Business rules enforced:
* No past dates.
* No overlapping availability slots.
* No availability during already accepted bookings.

### 4. Booking Management (State Machine)


Bookings follow a strict lifecycle:
`REQUESTED` ➔ `ACCEPTED / REJECTED` ➔ `COMPLETED / CANCELLED`

* **Company:** Browse workers, create requests, cancel bookings.
* **Worker:** View incoming requests, accept/reject bookings.
* **Validation:** All transitions are validated server-side. No invalid states.

### 5. Pagination & Data Access
All large result sets use database-level pagination via `Pageable` and `Page<T>` to prevent over-fetching and improve API performance.

---

## 🧾 API Overview

### Authentication
* `POST /api/v1/auth/signup`
* `POST /api/v1/auth/login`

### Worker Profiles
* `GET /api/v1/worker-profiles/me`
* `GET /api/v1/worker-profiles/{id}`
* `POST /api/v1/worker-profiles`

### Availability & Bookings
* `POST /api/v1/availability`
* `POST /api/v1/bookings`
* `POST /api/v1/bookings/{id}/accept`

> **Note:** Full documentation is available via Swagger at `http://localhost:8080/swagger-ui.html` once the application is running.
> if you want the full project the must connect it from frontend repository on my profile 

---

## 🔁 DTO-Driven Design

VHire never exposes JPA entities directly. All external communication uses **Data Transfer Objects (DTOs)**.

* **Security:** No accidental sensitive field exposure.
* **Loose Coupling:** API remains stable even if the database schema changes.
* **Validation:** Strong request validation at the entry point.

---

## 🗄️ Database Design

Core relational entities include:
* **Users:** Credentials and roles.
* **Worker_Profiles:** Worker metadata.
* **Availability_Slots:** Time-bound availability.
* **Bookings:** Company ↔ Worker transactions.

---

## 🚧 Future Enhancements
* OAuth2 (Google/GitHub login).
* Refresh Token implementation.
* Advanced filtering (skills, rates, location).
* Message queue (RabbitMQ/Kafka) for async emails.
* Dockerized deployment.

---

## 👤 Author
**Sachin Sharma**
*Backend-Focused Engineering Student*
*Specializing in Java, Spring Boot, and Scalable Systems.*
