package com.rentaroom.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import com.rentaroom.converter.BookingStatusConverter;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "bookings")
public class Booking {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @ManyToOne
    @JoinColumn(name = "room_id", nullable = false)
    private Room room;
    
    @ManyToOne
    @JoinColumn(name = "renter_id", nullable = false)
    private User renter;
    
    @NotNull(message = "Check-in date is required")
    @Column(name = "check_in", nullable = false)
    private LocalDate checkIn;
    
    @NotNull(message = "Check-out date is required")
    @Column(name = "check_out", nullable = false)
    private LocalDate checkOut;
    
    @NotNull(message = "Total price is required")
    @Column(name = "total_price", nullable = false, precision = 10, scale = 2)
    private BigDecimal totalPrice;
    
    @Convert(converter = BookingStatusConverter.class)
    @Column(nullable = false)
    private BookingStatus status = BookingStatus.PENDING;
    
    @Column(name = "created_at")
    private LocalDateTime createdAt;
    
    // New field for bed-level booking
    @Column(name = "beds_booked")
    private Integer bedsBooked;
    
    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        if (bedsBooked == null) {
            bedsBooked = 1;
        }
    }
    
    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    
    public Room getRoom() { return room; }
    public void setRoom(Room room) { this.room = room; }
    
    public User getRenter() { return renter; }
    public void setRenter(User renter) { this.renter = renter; }
    
    public LocalDate getCheckIn() { return checkIn; }
    public void setCheckIn(LocalDate checkIn) { this.checkIn = checkIn; }
    
    public LocalDate getCheckOut() { return checkOut; }
    public void setCheckOut(LocalDate checkOut) { this.checkOut = checkOut; }
    
    public BigDecimal getTotalPrice() { return totalPrice; }
    public void setTotalPrice(BigDecimal totalPrice) { this.totalPrice = totalPrice; }
    
    public BookingStatus getStatus() { return status; }
    public void setStatus(BookingStatus status) { this.status = status; }
    
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
    
    public Integer getBedsBooked() { return bedsBooked; }
    public void setBedsBooked(Integer bedsBooked) { this.bedsBooked = bedsBooked; }
    
    public enum BookingStatus {
        PENDING, CONFIRMED, CANCELLED
    }
}
