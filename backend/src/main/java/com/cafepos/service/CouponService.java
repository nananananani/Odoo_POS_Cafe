package com.cafepos.service;

import com.cafepos.dto.request.CouponRequest;
import com.cafepos.dto.response.CouponResponse;
import com.cafepos.entity.Coupon;
import com.cafepos.entity.DiscountType;
import com.cafepos.exception.NotFoundException;
import com.cafepos.exception.ValidationException;
import com.cafepos.repository.CouponRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CouponService {

    private final CouponRepository couponRepository;

    @Transactional(readOnly = true)
    public List<CouponResponse> getAllCoupons() {
        return couponRepository.findAll().stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public CouponResponse getCouponById(Long id) {
        Coupon coupon = couponRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Coupon not found with id: " + id));
        return mapToResponse(coupon);
    }

    @Transactional
    public CouponResponse createCoupon(CouponRequest request) {
        validateCoupon(request, null);

        DiscountType discountType = DiscountType.valueOf(request.getDiscountType().toUpperCase());
        Coupon coupon = Coupon.builder()
                .name(request.getName())
                .code(request.getCode())
                .discountType(discountType)
                .value(request.getValue())
                .active(request.getActive())
                .build();

        Coupon savedCoupon = couponRepository.save(coupon);
        return mapToResponse(savedCoupon);
    }

    @Transactional
    public CouponResponse updateCoupon(Long id, CouponRequest request) {
        Coupon coupon = couponRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Coupon not found with id: " + id));

        validateCoupon(request, id);

        DiscountType discountType = DiscountType.valueOf(request.getDiscountType().toUpperCase());
        coupon.setName(request.getName());
        coupon.setCode(request.getCode());
        coupon.setDiscountType(discountType);
        coupon.setValue(request.getValue());
        coupon.setActive(request.getActive());

        Coupon updatedCoupon = couponRepository.save(coupon);
        return mapToResponse(updatedCoupon);
    }

    @Transactional
    public void deleteCoupon(Long id) {
        if (!couponRepository.existsById(id)) {
            throw new NotFoundException("Coupon not found with id: " + id);
        }
        couponRepository.deleteById(id);
    }

    private void validateCoupon(CouponRequest request, Long existingId) {
        boolean duplicate;
        if (existingId == null) {
            duplicate = couponRepository.existsByCode(request.getCode());
        } else {
            duplicate = couponRepository.existsByCodeAndIdNot(request.getCode(), existingId);
        }

        if (duplicate) {
            throw new ValidationException("Coupon code already exists");
        }

        DiscountType discountType;
        try {
            discountType = DiscountType.valueOf(request.getDiscountType().toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new ValidationException("Invalid discount type: " + request.getDiscountType());
        }

        if (discountType == DiscountType.PERCENT) {
            if (request.getValue().compareTo(new BigDecimal("100")) > 0) {
                throw new ValidationException("Percentage discount value cannot exceed 100");
            }
        }
    }

    private CouponResponse mapToResponse(Coupon coupon) {
        return CouponResponse.builder()
                .id(coupon.getId())
                .name(coupon.getName())
                .code(coupon.getCode())
                .discountType(coupon.getDiscountType().name())
                .value(coupon.getValue())
                .active(coupon.getActive())
                .build();
    }
}
