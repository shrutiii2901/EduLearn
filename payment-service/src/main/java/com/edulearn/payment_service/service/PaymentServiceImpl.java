package com.edulearn.payment_service.service;

import com.edulearn.payment_service.entity.Payment;
import com.edulearn.payment_service.entity.Subscription;
import com.edulearn.payment_service.repository.PaymentRepository;
import com.edulearn.payment_service.repository.SubscriptionRepository;
import com.edulearn.payment_service.service.PaymentService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class PaymentServiceImpl implements PaymentService {

    private final PaymentRepository paymentRepo;
    private final SubscriptionRepository subscriptionRepo;

    // ── Payment ────────────────────────────────────────────────────────────────

    @Override
    @Transactional
    public Payment processPayment(Long studentId, Long courseId,
                                  BigDecimal amount, Payment.PaymentMode mode) {
        Payment payment = Payment.builder()
                .studentId(studentId)
                .courseId(courseId)
                .amount(amount)
                .mode(mode)
                .status(Payment.PaymentStatus.SUCCESS)
                .transactionId(UUID.randomUUID().toString())
                .build();
        return paymentRepo.save(payment);
    }

    @Override
    public List<Payment> getPaymentsByStudent(Long studentId) {
        return paymentRepo.findByStudentIdOrderByPaidAtDesc(studentId);
    }

    @Override
    public List<Payment> getAllPayments() {
        return paymentRepo.findAll();
    }

    @Override
    @Transactional
    public Payment refundPayment(Long paymentId) {
        Payment payment = paymentRepo.findById(paymentId)
                .orElseThrow(() -> new RuntimeException("Payment not found: " + paymentId));
        if (payment.getStatus() == Payment.PaymentStatus.REFUNDED) {
            throw new RuntimeException("Payment already refunded.");
        }
        payment.setStatus(Payment.PaymentStatus.REFUNDED);
        return paymentRepo.save(payment);
    }

    // ── Subscription ───────────────────────────────────────────────────────────

    @Override
    @Transactional
    public Subscription subscribe(Long studentId, Subscription.Plan plan, BigDecimal amount) {
        // Cancel any existing active subscription first
        subscriptionRepo.findByStudentIdAndStatus(studentId, Subscription.SubStatus.ACTIVE)
                .ifPresent(s -> {
                    s.setStatus(Subscription.SubStatus.CANCELLED);
                    subscriptionRepo.save(s);
                });

        LocalDate endDate = switch (plan) {
            case MONTHLY -> LocalDate.now().plusMonths(1);
            case ANNUAL  -> LocalDate.now().plusYears(1);
            case FREE    -> LocalDate.now().plusYears(100); // effectively unlimited
        };

        Subscription subscription = Subscription.builder()
                .studentId(studentId)
                .plan(plan)
                .amountPaid(amount)
                .endDate(endDate)
                .status(Subscription.SubStatus.ACTIVE)
                .build();

        return subscriptionRepo.save(subscription);
    }

    @Override
    public Subscription getActiveSubscription(Long studentId) {
        return subscriptionRepo.findByStudentIdAndStatus(studentId, Subscription.SubStatus.ACTIVE)
                .orElseThrow(() -> new RuntimeException("No active subscription found for student: " + studentId));
    }

    @Override
    public boolean isSubscriptionActive(Long studentId) {
        return subscriptionRepo.findByStudentIdAndStatus(studentId, Subscription.SubStatus.ACTIVE)
                .map(s -> s.getEndDate().isAfter(LocalDate.now()))
                .orElse(false);
    }

    @Override
    @Transactional
    public void cancelSubscription(Long studentId) {
        Subscription subscription = subscriptionRepo
                .findByStudentIdAndStatus(studentId, Subscription.SubStatus.ACTIVE)
                .orElseThrow(() -> new RuntimeException("No active subscription to cancel."));
        subscription.setStatus(Subscription.SubStatus.CANCELLED);
        subscriptionRepo.save(subscription);
    }

    @Override
    public List<Subscription> getAllSubscriptions() {
        return subscriptionRepo.findAll();
    }
}