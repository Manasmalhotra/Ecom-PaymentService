package com.example.paymentservice.repository;

import com.example.paymentservice.models.PaymentEntity;
import com.example.paymentservice.models.PaymentStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface PaymentRepository extends JpaRepository<PaymentEntity,String> {
    PaymentEntity findByOrderId(long orderId);
    List<PaymentEntity> findAllByPaymentStatusAndCreatedOnBetween(PaymentStatus paymentStatus,LocalDateTime start, LocalDateTime end);
}
