package com.edulearn.payment_service.entity;


import jakarta.persistence.*;
import lombok.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "payments")
@Data @NoArgsConstructor @AllArgsConstructor @Builder
public class Payment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long paymentId;

    @Column(nullable = false)
    private Long studentId;

    private Long courseId;  // null for subscription payments

    @Column(nullable = false)
    private BigDecimal amount;

    private String currency = "INR";

    @Enumerated(EnumType.STRING)
    private PaymentStatus status = PaymentStatus.SUCCESS;

    @Enumerated(EnumType.STRING)
    private PaymentMode mode;

    @Column(unique = true)
    private String transactionId;

    private LocalDateTime paidAt;

    @PrePersist
    public void prePersist() { this.paidAt = LocalDateTime.now(); }

    public enum PaymentStatus { PENDING, SUCCESS, FAILED, REFUNDED }
    public enum PaymentMode   { CARD, UPI, WALLET, NET_BANKING }
}