package com.example.paymentservice.repository;

import com.example.paymentservice.models.PaymentEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PaymentRepository extends JpaRepository<PaymentEntity,String> {
}
