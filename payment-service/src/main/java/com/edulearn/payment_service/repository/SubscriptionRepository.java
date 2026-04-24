package com.edulearn.payment_service.repository;

import com.edulearn.payment_service.entity.Subscription;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface SubscriptionRepository extends JpaRepository<Subscription, Long> {
    Optional<Subscription> findByStudentIdAndStatus(Long studentId, Subscription.SubStatus status);
    List<Subscription> findByEndDateBeforeAndStatus(LocalDate date, Subscription.SubStatus status);
    long countByPlan(Subscription.Plan plan);
}