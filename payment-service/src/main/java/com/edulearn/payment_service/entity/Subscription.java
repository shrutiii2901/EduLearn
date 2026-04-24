package com.edulearn.payment_service.entity;

import jakarta.persistence.*;
import lombok.*;
import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "subscriptions")
@Data @NoArgsConstructor @AllArgsConstructor @Builder
public class Subscription {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long subscriptionId;

    @Column(nullable = false)
    private Long studentId;

    @Enumerated(EnumType.STRING)
    private Plan plan;

    private LocalDate startDate;
    private LocalDate endDate;

    @Enumerated(EnumType.STRING)
    private SubStatus status = SubStatus.ACTIVE;

    private BigDecimal amountPaid;
    private boolean autoRenew = false;

    @PrePersist
    public void prePersist() { this.startDate = LocalDate.now(); }

    public enum Plan      { FREE, MONTHLY, ANNUAL }
    public enum SubStatus { ACTIVE, CANCELLED, EXPIRED }
}