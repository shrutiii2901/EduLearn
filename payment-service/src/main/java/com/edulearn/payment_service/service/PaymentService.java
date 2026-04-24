package com.edulearn.payment_service.service;

import com.edulearn.payment_service.entity.Payment;
import com.edulearn.payment_service.entity.Subscription;
import java.math.BigDecimal;
import java.util.List;

public interface PaymentService {

    // ── Payment ────────────────────────────────────────────────────────────────
    Payment processPayment(Long studentId, Long courseId,
                           BigDecimal amount, Payment.PaymentMode mode);
    List<Payment> getPaymentsByStudent(Long studentId);
    List<Payment> getAllPayments();
    Payment refundPayment(Long paymentId);

    // ── Subscription ───────────────────────────────────────────────────────────
    Subscription subscribe(Long studentId, Subscription.Plan plan, BigDecimal amount);
    Subscription getActiveSubscription(Long studentId);
    boolean isSubscriptionActive(Long studentId);
    void cancelSubscription(Long studentId);
    List<Subscription> getAllSubscriptions();
}