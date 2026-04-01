package com.airline.reservation.config;

import com.airline.reservation.entity.*;
import com.airline.reservation.repository.*;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Component
public class DataInitializer implements CommandLineRunner {

    private final UserRepository userRepository;
    private final AirportRepository airportRepository;
    private final AirplaneRepository airplaneRepository;
    private final SeatClassRepository seatClassRepository;
    private final FlightRepository flightRepository;
    private final PasswordEncoder passwordEncoder;

    public DataInitializer(UserRepository userRepository, AirportRepository airportRepository,
                           AirplaneRepository airplaneRepository, SeatClassRepository seatClassRepository,
                           FlightRepository flightRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.airportRepository = airportRepository;
        this.airplaneRepository = airplaneRepository;
        this.seatClassRepository = seatClassRepository;
        this.flightRepository = flightRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void run(String... args) {
        // Tạo admin nếu chưa có
        if (!userRepository.existsByEmail("admin@ars.com")) {
            userRepository.save(User.builder()
                    .fullName("Admin")
                    .email("admin@ars.com")
                    .password(passwordEncoder.encode("admin123"))
                    .phone("0900000000")
                    .role(User.Role.ADMIN)
                    .status(User.UserStatus.ACTIVE)
                    .build());
        }

        // Tạo sân bay mẫu
        if (airportRepository.count() == 0) {
            airportRepository.save(Airport.builder()
                    .airportCode("SGN").airportName("Tân Sơn Nhất")
                    .city("TP. Hồ Chí Minh").country("Việt Nam").build());
            airportRepository.save(Airport.builder()
                    .airportCode("HAN").airportName("Nội Bài")
                    .city("Hà Nội").country("Việt Nam").build());
            airportRepository.save(Airport.builder()
                    .airportCode("DAD").airportName("Đà Nẵng")
                    .city("Đà Nẵng").country("Việt Nam").build());
            airportRepository.save(Airport.builder()
                    .airportCode("CXR").airportName("Cam Ranh")
                    .city("Nha Trang").country("Việt Nam").build());
            airportRepository.save(Airport.builder()
                    .airportCode("PQC").airportName("Phú Quốc")
                    .city("Phú Quốc").country("Việt Nam").build());
        }

        // Tạo hạng vé mẫu
        if (seatClassRepository.count() == 0) {
            seatClassRepository.save(SeatClass.builder()
                    .className("Economy").description("Hạng phổ thông")
                    .extraPrice(BigDecimal.ZERO).build());
            seatClassRepository.save(SeatClass.builder()
                    .className("Business").description("Hạng thương gia")
                    .extraPrice(new BigDecimal("1500000")).build());
            seatClassRepository.save(SeatClass.builder()
                    .className("First Class").description("Hạng nhất")
                    .extraPrice(new BigDecimal("5000000")).build());
        }

        // Tạo máy bay mẫu
        if (airplaneRepository.count() == 0) {
            airplaneRepository.save(Airplane.builder()
                    .airplaneName("VN-A001").airplaneModel("Boeing 787")
                    .totalSeats(300).status(Airplane.AirplaneStatus.ACTIVE).build());
            airplaneRepository.save(Airplane.builder()
                    .airplaneName("VN-A002").airplaneModel("Airbus A321")
                    .totalSeats(180).status(Airplane.AirplaneStatus.ACTIVE).build());
        }

        // Tạo chuyến bay mẫu tháng 4/2026
        if (flightRepository.count() == 0) {
            Airport sgn = airportRepository.findAll().stream()
                    .filter(a -> "SGN".equals(a.getAirportCode())).findFirst().orElse(null);
            Airport han = airportRepository.findAll().stream()
                    .filter(a -> "HAN".equals(a.getAirportCode())).findFirst().orElse(null);
            Airport dad = airportRepository.findAll().stream()
                    .filter(a -> "DAD".equals(a.getAirportCode())).findFirst().orElse(null);
            Airport cxr = airportRepository.findAll().stream()
                    .filter(a -> "CXR".equals(a.getAirportCode())).findFirst().orElse(null);
            Airport pqc = airportRepository.findAll().stream()
                    .filter(a -> "PQC".equals(a.getAirportCode())).findFirst().orElse(null);

            Airplane boeing = airplaneRepository.findAll().stream()
                    .filter(a -> "VN-A001".equals(a.getAirplaneName())).findFirst().orElse(null);
            Airplane airbus = airplaneRepository.findAll().stream()
                    .filter(a -> "VN-A002".equals(a.getAirplaneName())).findFirst().orElse(null);

            // 1. SGN -> HAN: 1/4 sáng (bắt buộc)
            flightRepository.save(Flight.builder()
                    .flightCode("VN100")
                    .airplane(boeing).departureAirport(sgn).arrivalAirport(han)
                    .departureTime(LocalDateTime.of(2026, 4, 1, 6, 0))
                    .arrivalTime(LocalDateTime.of(2026, 4, 1, 8, 10))
                    .basePrice(new BigDecimal("1500000")).availableSeats(280)
                    .status(Flight.FlightStatus.SCHEDULED).build());

            // 2. HAN -> SGN: 5/4 chiều (bắt buộc)
            flightRepository.save(Flight.builder()
                    .flightCode("VN101")
                    .airplane(boeing).departureAirport(han).arrivalAirport(sgn)
                    .departureTime(LocalDateTime.of(2026, 4, 5, 14, 0))
                    .arrivalTime(LocalDateTime.of(2026, 4, 5, 16, 10))
                    .basePrice(new BigDecimal("1600000")).availableSeats(280)
                    .status(Flight.FlightStatus.SCHEDULED).build());

            // 3. SGN -> DAD: 3/4
            flightRepository.save(Flight.builder()
                    .flightCode("VN200")
                    .airplane(airbus).departureAirport(sgn).arrivalAirport(dad)
                    .departureTime(LocalDateTime.of(2026, 4, 3, 8, 30))
                    .arrivalTime(LocalDateTime.of(2026, 4, 3, 9, 50))
                    .basePrice(new BigDecimal("900000")).availableSeats(160)
                    .status(Flight.FlightStatus.SCHEDULED).build());

            // 4. DAD -> HAN: 5/4
            flightRepository.save(Flight.builder()
                    .flightCode("VN201")
                    .airplane(airbus).departureAirport(dad).arrivalAirport(han)
                    .departureTime(LocalDateTime.of(2026, 4, 5, 10, 0))
                    .arrivalTime(LocalDateTime.of(2026, 4, 5, 11, 20))
                    .basePrice(new BigDecimal("850000")).availableSeats(160)
                    .status(Flight.FlightStatus.SCHEDULED).build());

            // 5. HAN -> DAD: 10/4
            flightRepository.save(Flight.builder()
                    .flightCode("VN202")
                    .airplane(airbus).departureAirport(han).arrivalAirport(dad)
                    .departureTime(LocalDateTime.of(2026, 4, 10, 7, 0))
                    .arrivalTime(LocalDateTime.of(2026, 4, 10, 8, 20))
                    .basePrice(new BigDecimal("800000")).availableSeats(160)
                    .status(Flight.FlightStatus.SCHEDULED).build());

            // 6. SGN -> CXR: 8/4
            flightRepository.save(Flight.builder()
                    .flightCode("VN300")
                    .airplane(airbus).departureAirport(sgn).arrivalAirport(cxr)
                    .departureTime(LocalDateTime.of(2026, 4, 8, 9, 0))
                    .arrivalTime(LocalDateTime.of(2026, 4, 8, 10, 0))
                    .basePrice(new BigDecimal("700000")).availableSeats(160)
                    .status(Flight.FlightStatus.SCHEDULED).build());

            // 7. CXR -> SGN: 12/4
            flightRepository.save(Flight.builder()
                    .flightCode("VN301")
                    .airplane(airbus).departureAirport(cxr).arrivalAirport(sgn)
                    .departureTime(LocalDateTime.of(2026, 4, 12, 15, 0))
                    .arrivalTime(LocalDateTime.of(2026, 4, 12, 16, 0))
                    .basePrice(new BigDecimal("700000")).availableSeats(160)
                    .status(Flight.FlightStatus.SCHEDULED).build());

            // 8. SGN -> PQC: 15/4
            flightRepository.save(Flight.builder()
                    .flightCode("VN400")
                    .airplane(airbus).departureAirport(sgn).arrivalAirport(pqc)
                    .departureTime(LocalDateTime.of(2026, 4, 15, 11, 0))
                    .arrivalTime(LocalDateTime.of(2026, 4, 15, 12, 0))
                    .basePrice(new BigDecimal("600000")).availableSeats(160)
                    .status(Flight.FlightStatus.SCHEDULED).build());

            // 9. PQC -> SGN: 20/4
            flightRepository.save(Flight.builder()
                    .flightCode("VN401")
                    .airplane(airbus).departureAirport(pqc).arrivalAirport(sgn)
                    .departureTime(LocalDateTime.of(2026, 4, 20, 16, 30))
                    .arrivalTime(LocalDateTime.of(2026, 4, 20, 17, 30))
                    .basePrice(new BigDecimal("600000")).availableSeats(160)
                    .status(Flight.FlightStatus.SCHEDULED).build());

            // 10. HAN -> SGN: 25/4 (chuyến thêm)
            flightRepository.save(Flight.builder()
                    .flightCode("VN102")
                    .airplane(boeing).departureAirport(han).arrivalAirport(sgn)
                    .departureTime(LocalDateTime.of(2026, 4, 25, 19, 0))
                    .arrivalTime(LocalDateTime.of(2026, 4, 25, 21, 10))
                    .basePrice(new BigDecimal("1550000")).availableSeats(280)
                    .status(Flight.FlightStatus.SCHEDULED).build());
        }

        // Thêm chuyến bay đầu tháng 4 (nếu chưa có)
        addEarlyAprilFlights();
    }

    private void addEarlyAprilFlights() {
        // Chỉ thêm nếu chưa tồn tại (kiểm tra bằng flight code)
        if (flightRepository.findAll().stream().anyMatch(f -> "VN103".equals(f.getFlightCode()))) {
            return;
        }

        Airport sgn = airportRepository.findAll().stream()
                .filter(a -> "SGN".equals(a.getAirportCode())).findFirst().orElse(null);
        Airport han = airportRepository.findAll().stream()
                .filter(a -> "HAN".equals(a.getAirportCode())).findFirst().orElse(null);
        Airport dad = airportRepository.findAll().stream()
                .filter(a -> "DAD".equals(a.getAirportCode())).findFirst().orElse(null);
        Airport cxr = airportRepository.findAll().stream()
                .filter(a -> "CXR".equals(a.getAirportCode())).findFirst().orElse(null);
        Airport pqc = airportRepository.findAll().stream()
                .filter(a -> "PQC".equals(a.getAirportCode())).findFirst().orElse(null);

        Airplane boeing = airplaneRepository.findAll().stream()
                .filter(a -> "VN-A001".equals(a.getAirplaneName())).findFirst().orElse(null);
        Airplane airbus = airplaneRepository.findAll().stream()
                .filter(a -> "VN-A002".equals(a.getAirplaneName())).findFirst().orElse(null);

        if (sgn == null || han == null) return;

        // --- SGN <-> HAN thêm nhiều chuyến đầu tháng 4 ---
        // 1/4: thêm chuyến chiều SGN->HAN
        flightRepository.save(Flight.builder()
                .flightCode("VN103").airplane(airbus).departureAirport(sgn).arrivalAirport(han)
                .departureTime(LocalDateTime.of(2026, 4, 1, 14, 0))
                .arrivalTime(LocalDateTime.of(2026, 4, 1, 16, 10))
                .basePrice(new BigDecimal("1650000")).availableSeats(160)
                .status(Flight.FlightStatus.SCHEDULED).build());

        // 2/4: SGN->HAN sáng
        flightRepository.save(Flight.builder()
                .flightCode("VN104").airplane(boeing).departureAirport(sgn).arrivalAirport(han)
                .departureTime(LocalDateTime.of(2026, 4, 2, 7, 30))
                .arrivalTime(LocalDateTime.of(2026, 4, 2, 9, 40))
                .basePrice(new BigDecimal("1400000")).availableSeats(280)
                .status(Flight.FlightStatus.SCHEDULED).build());

        // 2/4: HAN->SGN chiều
        flightRepository.save(Flight.builder()
                .flightCode("VN105").airplane(boeing).departureAirport(han).arrivalAirport(sgn)
                .departureTime(LocalDateTime.of(2026, 4, 2, 15, 0))
                .arrivalTime(LocalDateTime.of(2026, 4, 2, 17, 10))
                .basePrice(new BigDecimal("1450000")).availableSeats(280)
                .status(Flight.FlightStatus.SCHEDULED).build());

        // 3/4: SGN->HAN trưa
        flightRepository.save(Flight.builder()
                .flightCode("VN106").airplane(airbus).departureAirport(sgn).arrivalAirport(han)
                .departureTime(LocalDateTime.of(2026, 4, 3, 12, 0))
                .arrivalTime(LocalDateTime.of(2026, 4, 3, 14, 10))
                .basePrice(new BigDecimal("1350000")).availableSeats(160)
                .status(Flight.FlightStatus.SCHEDULED).build());

        // 3/4: HAN->SGN sáng
        flightRepository.save(Flight.builder()
                .flightCode("VN107").airplane(boeing).departureAirport(han).arrivalAirport(sgn)
                .departureTime(LocalDateTime.of(2026, 4, 3, 8, 0))
                .arrivalTime(LocalDateTime.of(2026, 4, 3, 10, 10))
                .basePrice(new BigDecimal("1500000")).availableSeats(280)
                .status(Flight.FlightStatus.SCHEDULED).build());

        // 4/4: SGN->HAN sáng
        flightRepository.save(Flight.builder()
                .flightCode("VN108").airplane(boeing).departureAirport(sgn).arrivalAirport(han)
                .departureTime(LocalDateTime.of(2026, 4, 4, 6, 30))
                .arrivalTime(LocalDateTime.of(2026, 4, 4, 8, 40))
                .basePrice(new BigDecimal("1550000")).availableSeats(280)
                .status(Flight.FlightStatus.SCHEDULED).build());

        // 4/4: HAN->SGN chiều
        flightRepository.save(Flight.builder()
                .flightCode("VN109").airplane(airbus).departureAirport(han).arrivalAirport(sgn)
                .departureTime(LocalDateTime.of(2026, 4, 4, 16, 0))
                .arrivalTime(LocalDateTime.of(2026, 4, 4, 18, 10))
                .basePrice(new BigDecimal("1700000")).availableSeats(160)
                .status(Flight.FlightStatus.SCHEDULED).build());

        // 5/4: SGN->HAN sáng (thêm chuyến)
        flightRepository.save(Flight.builder()
                .flightCode("VN110").airplane(airbus).departureAirport(sgn).arrivalAirport(han)
                .departureTime(LocalDateTime.of(2026, 4, 5, 9, 0))
                .arrivalTime(LocalDateTime.of(2026, 4, 5, 11, 10))
                .basePrice(new BigDecimal("1480000")).availableSeats(160)
                .status(Flight.FlightStatus.SCHEDULED).build());

        // 6/4: SGN->HAN tối
        flightRepository.save(Flight.builder()
                .flightCode("VN111").airplane(boeing).departureAirport(sgn).arrivalAirport(han)
                .departureTime(LocalDateTime.of(2026, 4, 6, 19, 0))
                .arrivalTime(LocalDateTime.of(2026, 4, 6, 21, 10))
                .basePrice(new BigDecimal("1300000")).availableSeats(280)
                .status(Flight.FlightStatus.SCHEDULED).build());

        // 6/4: HAN->SGN sáng
        flightRepository.save(Flight.builder()
                .flightCode("VN112").airplane(airbus).departureAirport(han).arrivalAirport(sgn)
                .departureTime(LocalDateTime.of(2026, 4, 6, 7, 0))
                .arrivalTime(LocalDateTime.of(2026, 4, 6, 9, 10))
                .basePrice(new BigDecimal("1380000")).availableSeats(160)
                .status(Flight.FlightStatus.SCHEDULED).build());

        // 7/4: SGN->HAN sáng
        flightRepository.save(Flight.builder()
                .flightCode("VN113").airplane(boeing).departureAirport(sgn).arrivalAirport(han)
                .departureTime(LocalDateTime.of(2026, 4, 7, 8, 0))
                .arrivalTime(LocalDateTime.of(2026, 4, 7, 10, 10))
                .basePrice(new BigDecimal("1420000")).availableSeats(280)
                .status(Flight.FlightStatus.SCHEDULED).build());

        // 7/4: HAN->SGN chiều
        flightRepository.save(Flight.builder()
                .flightCode("VN114").airplane(boeing).departureAirport(han).arrivalAirport(sgn)
                .departureTime(LocalDateTime.of(2026, 4, 7, 14, 30))
                .arrivalTime(LocalDateTime.of(2026, 4, 7, 16, 40))
                .basePrice(new BigDecimal("1520000")).availableSeats(280)
                .status(Flight.FlightStatus.SCHEDULED).build());

        // --- Thêm chuyến DAD, CXR, PQC đầu tháng 4 ---
        if (dad != null) {
            // 1/4: SGN->DAD
            flightRepository.save(Flight.builder()
                    .flightCode("VN203").airplane(airbus).departureAirport(sgn).arrivalAirport(dad)
                    .departureTime(LocalDateTime.of(2026, 4, 1, 10, 0))
                    .arrivalTime(LocalDateTime.of(2026, 4, 1, 11, 20))
                    .basePrice(new BigDecimal("850000")).availableSeats(160)
                    .status(Flight.FlightStatus.SCHEDULED).build());

            // 2/4: DAD->SGN
            flightRepository.save(Flight.builder()
                    .flightCode("VN204").airplane(airbus).departureAirport(dad).arrivalAirport(sgn)
                    .departureTime(LocalDateTime.of(2026, 4, 2, 13, 0))
                    .arrivalTime(LocalDateTime.of(2026, 4, 2, 14, 20))
                    .basePrice(new BigDecimal("880000")).availableSeats(160)
                    .status(Flight.FlightStatus.SCHEDULED).build());

            // 4/4: HAN->DAD
            flightRepository.save(Flight.builder()
                    .flightCode("VN205").airplane(airbus).departureAirport(han).arrivalAirport(dad)
                    .departureTime(LocalDateTime.of(2026, 4, 4, 9, 0))
                    .arrivalTime(LocalDateTime.of(2026, 4, 4, 10, 20))
                    .basePrice(new BigDecimal("750000")).availableSeats(160)
                    .status(Flight.FlightStatus.SCHEDULED).build());

            // 6/4: DAD->HAN
            flightRepository.save(Flight.builder()
                    .flightCode("VN206").airplane(airbus).departureAirport(dad).arrivalAirport(han)
                    .departureTime(LocalDateTime.of(2026, 4, 6, 11, 0))
                    .arrivalTime(LocalDateTime.of(2026, 4, 6, 12, 20))
                    .basePrice(new BigDecimal("820000")).availableSeats(160)
                    .status(Flight.FlightStatus.SCHEDULED).build());
        }

        if (cxr != null) {
            // 2/4: SGN->CXR
            flightRepository.save(Flight.builder()
                    .flightCode("VN302").airplane(airbus).departureAirport(sgn).arrivalAirport(cxr)
                    .departureTime(LocalDateTime.of(2026, 4, 2, 8, 0))
                    .arrivalTime(LocalDateTime.of(2026, 4, 2, 9, 0))
                    .basePrice(new BigDecimal("650000")).availableSeats(160)
                    .status(Flight.FlightStatus.SCHEDULED).build());

            // 5/4: CXR->SGN
            flightRepository.save(Flight.builder()
                    .flightCode("VN303").airplane(airbus).departureAirport(cxr).arrivalAirport(sgn)
                    .departureTime(LocalDateTime.of(2026, 4, 5, 17, 0))
                    .arrivalTime(LocalDateTime.of(2026, 4, 5, 18, 0))
                    .basePrice(new BigDecimal("680000")).availableSeats(160)
                    .status(Flight.FlightStatus.SCHEDULED).build());
        }

        if (pqc != null) {
            // 3/4: SGN->PQC
            flightRepository.save(Flight.builder()
                    .flightCode("VN402").airplane(airbus).departureAirport(sgn).arrivalAirport(pqc)
                    .departureTime(LocalDateTime.of(2026, 4, 3, 7, 0))
                    .arrivalTime(LocalDateTime.of(2026, 4, 3, 8, 0))
                    .basePrice(new BigDecimal("550000")).availableSeats(160)
                    .status(Flight.FlightStatus.SCHEDULED).build());

            // 7/4: PQC->SGN
            flightRepository.save(Flight.builder()
                    .flightCode("VN403").airplane(airbus).departureAirport(pqc).arrivalAirport(sgn)
                    .departureTime(LocalDateTime.of(2026, 4, 7, 18, 0))
                    .arrivalTime(LocalDateTime.of(2026, 4, 7, 19, 0))
                    .basePrice(new BigDecimal("580000")).availableSeats(160)
                    .status(Flight.FlightStatus.SCHEDULED).build());
        }
    }
}
