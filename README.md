🚀 VHire – Role-Based Hiring Platform (Backend)

VHire is a robust backend system designed to facilitate secure, role-based interactions between Companies and Workers. Built with a focus on real-world backend design, the platform emphasizes strict authentication, granular authorization, and a controlled booking lifecycle.

Unlike basic CRUD applications, VHire implements a complex state machine for availability-driven hiring and enforces security at the method level.

🧠 Problem Statement

In many real-world hiring scenarios—especially for short-term or task-based work—companies struggle to find workers with clear availability, while workers face unstructured job requests and poor scheduling control.

Common issues include:

Companies sending booking requests without knowing if a worker is actually available
Workers receiving overlapping or last-minute requests

Lack of accountability around who can accept, cancel, or modify a booking
,No clear ownership of actions once a booking is created

These issues lead to:
Missed work opportunities,
Scheduling conflicts,
Poor trust between companies and workers
,Manual coordination outside the platform

VHire aims to solve this by providing a backend-driven hiring workflow where:

Workers explicitly declare when they are available

Companies can only request bookings within valid availability windows

Each action (request, accept, reject, cancel) is strictly role-controlled

Booking transitions are validated to prevent inconsistent or invalid states

The result is a predictable, secure, and scalable hiring process that mirrors how real-world platforms manage temporary or skill-based work engagements.

Now the flow is:

Problem → Impact → Solution → Implementation

🛠️ Tech Stack

Category	Technology

Language-	Java 17

Framework- Spring Boot 3.x

Security-	Spring Security, JWT (Stateless)

Persistence-	Spring Data JPA (Hibernate)

Database-	MySQL

Build Tool-	Maven

🔐 Security Architecture
Authentication
VHire utilizes Stateless JWT-based authentication. Users authenticate via email and password to receive a token, which must be included in the Authorization: Bearer <token> header for subsequent requests.

Authorization
Role-based access is enforced at the Method Level using @PreAuthorize.

👷 Worker: Can manage profiles, define availability slots, and respond to booking requests.

🏢 Company: Can browse workers, initiate booking requests, and manage their hiring history.

📦 Core Features
1️⃣ User & Role Management

Unified User entity with role-based behavior

Role-specific access enforced via Spring Security

JWT-backed session handling (stateless)

2️⃣ Worker Profile Management (Worker Side)

Workers can create and update profiles containing:
Skills

Experience (years)

Hourly rate

Bio

Profiles include an activation flag to control visibility

Companies can view worker profiles, but cannot modify them

3️⃣ Availability Management (Worker Side)

Workers define when they are available to work.

Business rules enforced:

 No past dates
 
 No overlapping availability slots
 
 No availability during already accepted bookings

This ensures clean scheduling and prevents double-booking.

4️⃣ Booking Management (Company + Worker)

A structured booking lifecycle connects companies and workers.

Company capabilities:-

Browse worker profiles

Create booking requests for specific dates & time slots

Cancel bookings when required

View booking history (paginated)

Worker capabilities:-

View incoming booking requests

Accept or reject bookings

View assigned and past bookings (paginated)

All transitions are validated server-side to maintain data integrity.

5️⃣ Pagination & Data Access Control

 Large result sets (bookings) are paginated at the database level
 
 Uses Spring Data `Pageable` and `Page<T>`
 
 Prevents over-fetching and improves API performance
  
🧾 API Overview
Authentication
POST /api/v1/auth/signup - Register a new account.

POST /api/v1/auth/login - Authenticate and receive JWT.

Worker Profiles

GET /api/v1/worker-profiles/me - View own profile (Worker only).

GET /api/v1/worker-profiles/{id} - Public/Company view of worker details.

POST /api/v1/worker-profiles - Create/Update profile data.


Availability & Bookings

POST /api/v1/availability - Set available slots.

POST /api/v1/bookings - Company initiates a request.

POST /api/v1/bookings/{id}/accept - Worker accepts a request.

remaining APIs are documented using Swagger (OpenAPI).

After running the application, access:

http://localhost:8080/swagger-ui.html

Swagger supports JWT authentication via the Authorize button.

🔁 DTO-Driven Design (Why DTOs Instead of Entities)

VHire never exposes JPA entities directly to the API layer.

All external communication is done using DTOs.

Reasons for Using DTOs:

🔐 Security – Prevents accidental exposure of internal fields (IDs, flags, relations)

🔄 Loose Coupling – API contracts remain stable even if entities change

📦 Validation – Request DTOs enforce input constraints

🧩 Clear Boundaries – Clean separation between persistence and API models

DTO Types Used:

Request DTOs – For input validation

Response DTOs – For controlled API output

Auth DTOs – For login/signup responses

🗄️ Database Design

The schema is optimized for relational integrity with the following core entities:

Users: Core credentials and role assignment.

Worker_Profiles: Metadata linked to User IDs.

Availability_Slots: Time-bound slots linked to Workers.

Bookings: Transactions linking Companies, Workers, and specific Slots.

🚧 Future Enhancements

OAuth2 Integration: Support for Google/GitHub social logins.

Refresh Tokens: Enhancing security for long-lived sessions.

Filtering: Advanced search for worker profiles based on skills/rates.

Admin Dashboard: For platform moderation and user management.

👤 Author
Sachin

Backend-focused Engineering Student

Specializing in Java, Spring Boot, and cloud computing 
