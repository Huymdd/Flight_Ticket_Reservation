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
