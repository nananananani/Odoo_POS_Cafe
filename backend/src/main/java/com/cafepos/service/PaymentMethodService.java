package com.cafepos.service;

import com.cafepos.dto.request.PaymentMethodRequest;
import com.cafepos.dto.response.PaymentMethodResponse;
import com.cafepos.entity.PaymentMethod;
import com.cafepos.entity.PaymentMethodType;
import com.cafepos.exception.NotFoundException;
import com.cafepos.exception.ValidationException;
import com.cafepos.repository.PaymentMethodRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PaymentMethodService {

    private final PaymentMethodRepository paymentMethodRepository;

    @Transactional(readOnly = true)
    public List<PaymentMethodResponse> getAllPaymentMethods() {
        return paymentMethodRepository.findAll().stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public PaymentMethodResponse getPaymentMethodById(Long id) {
        PaymentMethod method = paymentMethodRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Payment method not found with id: " + id));
        return mapToResponse(method);
    }

    @Transactional
    public PaymentMethodResponse createPaymentMethod(PaymentMethodRequest request) {
        validatePaymentMethod(request);

        PaymentMethodType methodType = PaymentMethodType.valueOf(request.getType().toUpperCase());
        PaymentMethod method = PaymentMethod.builder()
                .name(request.getName())
                .type(methodType)
                .upiId(request.getUpiId())
                .active(request.getActive() != null ? request.getActive() : true)
                .build();

        PaymentMethod savedMethod = paymentMethodRepository.save(method);
        return mapToResponse(savedMethod);
    }

    @Transactional
    public PaymentMethodResponse updatePaymentMethod(Long id, PaymentMethodRequest request) {
        PaymentMethod method = paymentMethodRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Payment method not found with id: " + id));

        validatePaymentMethod(request);

        PaymentMethodType methodType = PaymentMethodType.valueOf(request.getType().toUpperCase());
        method.setName(request.getName());
        method.setType(methodType);
        method.setUpiId(request.getUpiId());
        if (request.getActive() != null) {
            method.setActive(request.getActive());
        }

        PaymentMethod updatedMethod = paymentMethodRepository.save(method);
        return mapToResponse(updatedMethod);
    }

    @Transactional
    public void deletePaymentMethod(Long id) {
        if (!paymentMethodRepository.existsById(id)) {
            throw new NotFoundException("Payment method not found with id: " + id);
        }
        paymentMethodRepository.deleteById(id);
    }

    private void validatePaymentMethod(PaymentMethodRequest request) {
        PaymentMethodType type;
        try {
            type = PaymentMethodType.valueOf(request.getType().toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new ValidationException("Invalid payment method type: " + request.getType());
        }

        if (type == PaymentMethodType.UPI) {
            String upiId = request.getUpiId();
            if (upiId == null || upiId.trim().isEmpty()) {
                throw new ValidationException("UPI ID is required for UPI payment method");
            }
            Boolean active = request.getActive();
            if (active != null && active) {
                // If it is being activated (active = true), UPI ID must not be blank (which is already verified by the check above, but let's be explicit)
                if (upiId == null || upiId.trim().isEmpty()) {
                    throw new ValidationException("Cannot activate UPI method with no UPI ID set");
                }
            }
        }
    }

    private PaymentMethodResponse mapToResponse(PaymentMethod method) {
        return PaymentMethodResponse.builder()
                .id(method.getId())
                .name(method.getName())
                .type(method.getType().name())
                .upiId(method.getUpiId())
                .active(method.getActive())
                .build();
    }
}
