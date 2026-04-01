# Hướng dẫn đẩy project lên GitHub

## Bước 1: Khởi tạo Git repository
```bash
git init
```

## Bước 2: Thêm tất cả file vào staging area
```bash
git add .
```

## Bước 3: Tạo commit đầu tiên
```bash
git commit -m "Initial commit: Airline Reservation System"
```

## Bước 4: Kết nối với repository trên GitHub
```bash
git remote add origin https://github.com/Huymdd/Flight_Ticket_Reservation.git
```

## Bước 5: Đổi tên nhánh thành main (nếu cần)
```bash
git branch -M main
```

## Bước 6: Đẩy code lên GitHub
```bash
git push -u origin main
```

## Các lệnh bổ sung thường dùng

### Kiểm tra trạng thái
```bash
git status
```

### Xem lịch sử commit
```bash
git log --oneline
```

### Thêm thay đổi mới và đẩy lên
```bash
git add .
git commit -m "Mô tả thay đổi"
git push
```

### Xem remote đã kết nối
```bash
git remote -v
```

## Quy tắc đặt tên nhánh (Branch Naming Convention)

| Nhánh | File liên quan |
|-------|---------------|
| `feature/authentication-module` | login.html, register.html |
| `feature/flight-search` | index.html, search.html, detail.html (flight) |
| `feature/booking-workflow` | new.html, payment.html, detail.html (booking), history.html |
| `feature/admin-dashboard` | dashboard.html, flights.html, airplanes.html, airports.html |
| `feature/admin-user-management` | users.html, bookings.html |
| `feature/admin-service-management` | seat-classes.html, services.html |
| `feature/layout-fragments` | layout.html (navbar, footer, head, scripts) |
| `feature/static-assets` | style.css |
| `fix/<issue-description>` | e.g., fix/login-redirect-null-session |

### Tạo nhánh feature mới
```bash
git checkout -b feature/authentication-module
```

### Chuyển sang nhánh khác
```bash
git checkout feature/flight-search
```

### Đẩy nhánh feature lên remote
```bash
git push -u origin feature/authentication-module
```

### Merge nhánh feature vào main
```bash
git checkout main
git merge feature/authentication-module
```

## Push and Pull Request Workflow

Once a feature is complete and verified functional on the local Spring Boot instance, the developer pushes the feature branch to GitHub:

```bash
git push origin feature/booking-workflow
```

A Pull Request is then opened on GitHub, targeting the `develop` branch. The PR description includes:

- A summary of the Thymeleaf templates (HTML pages) added or modified
- The database tables read from or written to by the new code
- A brief verification checklist, e.g.:
  - "Tested booking flight SGN → HAN, selected Business seat, payment successful — booking_details and payments written correctly"
  - "Tested round-trip search SGN ↔ DAD — results displayed correctly for both departure and return"
  - "Tested admin adding a new flight — data saved correctly to flights and seats tables"
- Reference to any related issues or previously discussed changes

### Example Pull Request Description

```
## Summary
- Added payment.html for the payment workflow
- Updated BookingController to handle payment processing and create records in payments table
- Modified booking/detail.html to display payment info after completion

## Database tables affected
- `bookings` (READ/WRITE)
- `payments` (WRITE)
- `booking_details` (READ)

## Verification checklist
- [x] Booked flight SGN → HAN, Economy class — payment completed successfully
- [x] Booked round-trip SGN ↔ DAD, Business class — total calculated correctly
- [x] Tested payment failure scenario — error message displayed properly

## Related issues
- Closes #12
```

## Merge Conflict Resolution

Merge conflicts in the Flight_Ticket_Reservation project most commonly arise when two developers modify shared Thymeleaf templates — for example, when both the authentication module and the admin dashboard branch reference or modify `login.html`, or when two branches independently update the `layout.html` fragment structure. The resolution process follows these steps:

- Git identifies conflicting sections in the affected HTML template and marks them with standard conflict markers (`<<<<<<< HEAD`, `=======`, `>>>>>>> branch-name`).
- The developer opens the conflicting file in VS Code, where the built-in merge editor provides a visual side-by-side comparison of the **Current Change** (local branch) and the **Incoming Change** (incoming branch).
- The developer resolves the conflict by selecting the appropriate version — the current change, the incoming change, or a manually authored combination that preserves the functional intent of both contributions.
- After resolving all conflicts in all affected files, the developer stages the resolved files and completes the merge commit:

```bash
git add src/main/resources/templates/fragments/layout.html
git commit -m "merge: resolve conflict in layout.html between admin-dashboard and authentication branches"
```

- The updated branch is pushed to GitHub, the Pull Request is updated automatically, and the reviewer is notified to re-examine the resolved state.

To minimize conflict frequency, the team follows preventive practices: keeping feature branches narrowly scoped to their designated template module group, communicating proactively about any changes to shared files (`layout.html`, `login.html`, `application.properties`), keeping branches short-lived, and synchronizing with `develop` at the beginning of every development session.

## Database and Shared Resources

The Flight_Ticket_Reservation application uses a MySQL database accessed via Spring Data JPA with the MySQL Connector/J driver (`com.mysql.cj.jdbc.Driver`). The datasource is configured in `application.properties`:

```properties
spring.datasource.url=jdbc:mysql://localhost:3307/airline_reservation
spring.datasource.username=airline_user
spring.datasource.password=airline_pass
spring.datasource.driver-class-name=com.mysql.cj.jdbc.Driver
```

Because the database schema is shared across all modules, any structural changes to an entity (such as adding a field to the `Booking` entity or modifying the `Payment` relationship in `BookingController`) must be communicated to all team members immediately and reflected in the corresponding Thymeleaf templates and repository classes across all feature branches. Schema changes are tracked through dedicated commit messages:

```bash
git commit -m "refactor: add extra_baggage column to booking_details entity and update booking/new.html form"
```

Similarly, changes to the shared `static/` directory (adding or removing CSS/JS files referenced in templates via Thymeleaf syntax such as `th:href="@{/css/style.css}"`) are committed to a separate branch and merged before dependent feature branches are submitted for review.

## GitHub Repository Overview

GitHub repository main page showing the repository name (`Flight_Ticket_Reservation`), description, top-level file structure including the `src/main/java/com/airline/reservation/` directory, `src/main/resources/templates/` for Thymeleaf views, `src/main/resources/application.properties`, `pom.xml`, and `docker-compose.yml`. This screenshot confirms the creation of the shared repository and the overall project structure as reflected in the actual source code.

## Branch Visualization

GitHub repository Branches tab or Insights → Network graph showing the `main`, `develop`, and feature branches (`feature/authentication-module`, `feature/flight-search`, `feature/booking-workflow`, `feature/admin-dashboard`, `feature/admin-user-management`, `feature/admin-service-management`, `feature/layout-fragments`, `feature/static-assets`). This visualization confirms adherence to the documented branching strategy for each Thymeleaf template module group.

## Merge Conflict Evidence

VS Code merge editor or terminal output showing a merge conflict in a Thymeleaf template (e.g., `layout.html` or `login.html`) and its resolution, with the conflict markers (`<<<<<<< HEAD`, `=======`, `>>>>>>>`) visible alongside the final resolved version. This evidence demonstrates the team's competency in handling concurrent development conflicts in Thymeleaf HTML source files.

## Commit History Evidence

GitHub commit history view showing multiple commits with Conventional Commits-style messages (`feat: add payment processing in BookingController`, `fix: correct JPA relationship mapping in Booking entity`, `merge: resolve conflict in layout.html between admin-dashboard and authentication branches`), author attributions for each team member, and timestamps. This evidence demonstrates consistent, disciplined use of atomic commits mapped to specific Thymeleaf template modules.

## Pull Request Evidence

A Pull Request on GitHub for one of the feature branches (e.g., `feature/booking-workflow` → `develop`), showing the PR title (`feat: booking workflow — payment processing and round-trip support`), the file diff for `BookingController.java` illustrating the payment processing logic and `booking_details` JPA insert operations, reviewer comments, and approval status. This documents the team's code review process and peer quality assurance practice.

## Contributors Evidence

GitHub Insights → Contributors graph showing individual commit activity, additions, and deletions over the project timeline for all three team members. This confirms equitable participation across the authentication, flight-search, booking-workflow, admin-dashboard, admin-user-management, admin-service-management, layout-fragments, and static-assets modules, and sustained engagement throughout the development period.

## Project Overview and Git Workflow

The Flight Ticket Reservation System is a Spring Boot web application that manages two distinct user roles — User and Administrator — each with dedicated pages, authentication flows, and database interactions. The system is built on Spring Boot with Thymeleaf templating, Spring Data JPA connectivity to a MySQL database (`airline_reservation`), and is deployed on an embedded Tomcat server (port 8080).

Given the scope of the project and its multi-role architecture, structured source code management using Git and GitHub is essential. The team adopts a branch-based collaborative workflow inspired by the Gitflow model, adapted to the scale and timeline of the course project. This workflow ensures:

- The `main` branch always reflects a stable, deployable state of the application.
- Active development and feature integration are consolidated on the `develop` branch before promotion to `main`.
- All individual module contributions are made through dedicated feature branches originating from `develop`.
- No direct commits are made to `main` or `develop` — all changes are integrated exclusively via Pull Requests.
- At least one peer review approval is required before any Pull Request is merged.

The source code is organized following the standard Spring Boot project structure: `src/main/java/com/airline/reservation/` containing controllers, entities, repositories, services, and configuration classes; `src/main/resources/templates/` containing all Thymeleaf HTML templates organized by module (`auth/`, `flight/`, `booking/`, `admin/`, `fragments/`); and `src/main/resources/static/` for CSS and JS assets.

The application entry point is `AirlineReservationApplication.java`, which bootstraps the embedded Tomcat server. All authentication is handled by Spring Security configured in `SecurityConfig.java`, which routes users through `auth/login.html`, queries the `users` table via `UserRepository`, and redirects to the corresponding home page (`index.html` for users, `admin/dashboard.html` for administrators) upon successful credential verification.

## Main Branch Policy

The `main` branch serves as the production-grade branch of the repository. It represents the most recent stable, fully tested version of the Flight Ticket Reservation System. Direct commits to `main` are strictly prohibited. Code is promoted to `main` exclusively via Pull Requests from the `develop` branch, after integration testing has been completed and all primary workflows (login, flight search, booking, payment, admin management) have been verified functional. This ensures that `main` always represents a demonstrable, submission-ready release of the application.
