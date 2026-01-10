package com.rentaroom.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "rooms")
public class Room {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @ManyToOne
    @JoinColumn(name = "host_id", nullable = false)
    private User host;
    
    @NotBlank(message = "Title is required")
    @Size(max = 150, message = "Title must be less than 150 characters")
    @Column(nullable = false, length = 150)
    private String title;
    
    @Column(columnDefinition = "TEXT")
    private String description;
    
    @NotBlank(message = "Location is required")
    @Column(nullable = false)
    private String location;
    
    @NotNull(message = "Price is required")
    @DecimalMin(value = "0.0", inclusive = false, message = "Price must be greater than 0")
    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal price;
    
    @NotNull(message = "Capacity is required")
    @Min(value = 1, message = "Capacity must be at least 1")
    @Column(nullable = false)
    private Integer capacity;
    
    @Column(name = "available_from")
    private LocalDate availableFrom;
    
    @Column(name = "available_to")
    private LocalDate availableTo;
    
    @Column(name = "created_at")
    private LocalDateTime createdAt;
    
    @OneToMany(mappedBy = "room", cascade = CascadeType.ALL)
    private List<Booking> bookings;
    
    @OneToMany(mappedBy = "room", cascade = CascadeType.ALL)
    private List<Review> reviews;
    
    // New field for bed-level rental (for dormitory style rooms)
    @Column(name = "beds_available")
    private Integer bedsAvailable;
    
    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        if (bedsAvailable == null) {
            bedsAvailable = capacity;
        }
    }
    
    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    
    public User getHost() { return host; }
    public void setHost(User host) { this.host = host; }
    
    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }
    
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    
    public String getLocation() { return location; }
    public void setLocation(String location) { this.location = location; }
    
    public BigDecimal getPrice() { return price; }
    public void setPrice(BigDecimal price) { this.price = price; }
    
    public Integer getCapacity() { return capacity; }
    public void setCapacity(Integer capacity) { this.capacity = capacity; }
    
    public LocalDate getAvailableFrom() { return availableFrom; }
    public void setAvailableFrom(LocalDate availableFrom) { this.availableFrom = availableFrom; }
    
    public LocalDate getAvailableTo() { return availableTo; }
    public void setAvailableTo(LocalDate availableTo) { this.availableTo = availableTo; }
    
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
    
    public List<Booking> getBookings() { return bookings; }
    public void setBookings(List<Booking> bookings) { this.bookings = bookings; }
    
    public List<Review> getReviews() { return reviews; }
    public void setReviews(List<Review> reviews) { this.reviews = reviews; }
    
    public Integer getBedsAvailable() { return bedsAvailable; }
    public void setBedsAvailable(Integer bedsAvailable) { this.bedsAvailable = bedsAvailable; }
}
