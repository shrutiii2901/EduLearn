package com.edulearn.payment_service.controller;

import com.edulearn.payment_service.entity.Payment;
import com.edulearn.payment_service.entity.Subscription;
import com.edulearn.payment_service.service.PaymentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class PaymentResourceController {

    private final PaymentService paymentService;

    // Payment apis
    @Tag(name = "Payments")
    @PostMapping("/payments/process")
    @Operation(summary = "Process a one-time course payment (Student)")
    public ResponseEntity<Payment> processPayment(@RequestParam Long studentId,
                                                  @RequestParam Long courseId,
                                                  @RequestParam BigDecimal amount,
                                                  @RequestParam Payment.PaymentMode mode) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(paymentService.processPayment(studentId, courseId, amount, mode));
    }

    @Tag(name = "Payments")
    @GetMapping("/payments/student/{studentId}")
    @Operation(summary = "Get full payment history for a student")
    public ResponseEntity<List<Payment>> byStudent(@PathVariable Long studentId) {
        return ResponseEntity.ok(paymentService.getPaymentsByStudent(studentId));
    }

    @Tag(name = "Payments")
    @GetMapping("/payments")
    @Operation(summary = "Get all payments — Admin view")
    public ResponseEntity<List<Payment>> all() {
        return ResponseEntity.ok(paymentService.getAllPayments());
    }

    @Tag(name = "Payments")
    @PutMapping("/payments/{paymentId}/refund")
    @Operation(summary = "Refund a payment (Admin)")
    public ResponseEntity<Payment> refund(@PathVariable Long paymentId) {
        return ResponseEntity.ok(paymentService.refundPayment(paymentId));
    }


    //Subscription apis

    @Tag(name = "Subscriptions")
    @PostMapping("/subscriptions")
    @Operation(summary = "Subscribe to a plan — FREE, MONTHLY, or ANNUAL (Student)")
    public ResponseEntity<Subscription> subscribe(@RequestParam Long studentId,
                                                  @RequestParam Subscription.Plan plan,
                                                  @RequestParam BigDecimal amount) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(paymentService.subscribe(studentId, plan, amount));
    }

    @Tag(name = "Subscriptions")
    @GetMapping("/subscriptions/student/{studentId}")
    @Operation(summary = "Get active subscription for a student")
    public ResponseEntity<Subscription> getSubscription(@PathVariable Long studentId) {
        return ResponseEntity.ok(paymentService.getActiveSubscription(studentId));
    }

    @Tag(name = "Subscriptions")
    @GetMapping("/subscriptions/student/{studentId}/active")
    @Operation(summary = "Check if a student has an active subscription")
    public ResponseEntity<Boolean> isActive(@PathVariable Long studentId) {
        return ResponseEntity.ok(paymentService.isSubscriptionActive(studentId));
    }

    @Tag(name = "Subscriptions")
    @DeleteMapping("/subscriptions/student/{studentId}")
    @Operation(summary = "Cancel active subscription (Admin or Student)")
    public ResponseEntity<String> cancel(@PathVariable Long studentId) {
        paymentService.cancelSubscription(studentId);
        return ResponseEntity.ok("Subscription cancelled.");
    }

    @Tag(name = "Subscriptions")
    @GetMapping("/subscriptions")
    @Operation(summary = "Get all subscriptions — Admin view")
    public ResponseEntity<List<Subscription>> allSubs() {
        return ResponseEntity.ok(paymentService.getAllSubscriptions());
    }
}