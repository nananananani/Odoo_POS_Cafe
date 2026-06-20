package com.cafepos.service;

import com.cafepos.dto.request.PromotionRequest;
import com.cafepos.dto.response.PromotionResponse;
import com.cafepos.entity.*;
import com.cafepos.exception.NotFoundException;
import com.cafepos.exception.ValidationException;
import com.cafepos.repository.ProductRepository;
import com.cafepos.repository.PromotionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PromotionService {

    private final PromotionRepository promotionRepository;
    private final ProductRepository productRepository;

    @Transactional(readOnly = true)
    public List<PromotionResponse> getAllPromotions() {
        return promotionRepository.findAll().stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public PromotionResponse getPromotionById(Long id) {
        Promotion promotion = promotionRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Promotion not found with id: " + id));
        return mapToResponse(promotion);
    }

    @Transactional
    public PromotionResponse createPromotion(PromotionRequest request) {
        validatePromotion(request);

        ApplyScope applyScope = ApplyScope.valueOf(request.getApplyScope().toUpperCase());
        DiscountType discountType = DiscountType.valueOf(request.getDiscountType().toUpperCase());

        Product product = null;
        if (applyScope == ApplyScope.PRODUCT) {
            product = productRepository.findById(request.getProductId())
                    .orElseThrow(() -> new ValidationException("Product not found with id: " + request.getProductId()));
        }

        Promotion promotion = Promotion.builder()
                .name(request.getName())
                .applyScope(applyScope)
                .product(product)
                .minQty(request.getMinQty())
                .minOrderAmount(request.getMinOrderAmount())
                .discountType(discountType)
                .value(request.getValue())
                .active(request.getActive())
                .build();

        Promotion savedPromotion = promotionRepository.save(promotion);
        return mapToResponse(savedPromotion);
    }

    @Transactional
    public PromotionResponse updatePromotion(Long id, PromotionRequest request) {
        Promotion promotion = promotionRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Promotion not found with id: " + id));

        validatePromotion(request);

        ApplyScope applyScope = ApplyScope.valueOf(request.getApplyScope().toUpperCase());
        DiscountType discountType = DiscountType.valueOf(request.getDiscountType().toUpperCase());

        Product product = null;
        if (applyScope == ApplyScope.PRODUCT) {
            product = productRepository.findById(request.getProductId())
                    .orElseThrow(() -> new ValidationException("Product not found with id: " + request.getProductId()));
        }

        promotion.setName(request.getName());
        promotion.setApplyScope(applyScope);
        promotion.setProduct(product);
        promotion.setMinQty(request.getMinQty());
        promotion.setMinOrderAmount(request.getMinOrderAmount());
        promotion.setDiscountType(discountType);
        promotion.setValue(request.getValue());
        promotion.setActive(request.getActive());

        Promotion updatedPromotion = promotionRepository.save(promotion);
        return mapToResponse(updatedPromotion);
    }

    @Transactional
    public void deletePromotion(Long id) {
        if (!promotionRepository.existsById(id)) {
            throw new NotFoundException("Promotion not found with id: " + id);
        }
        promotionRepository.deleteById(id);
    }

    private void validatePromotion(PromotionRequest request) {
        ApplyScope applyScope;
        try {
            applyScope = ApplyScope.valueOf(request.getApplyScope().toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new ValidationException("Invalid apply scope: " + request.getApplyScope());
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

        if (applyScope == ApplyScope.PRODUCT) {
            if (request.getProductId() == null) {
                throw new ValidationException("Product ID is required when apply scope is PRODUCT");
            }
            if (request.getMinQty() == null || request.getMinQty() < 1) {
                throw new ValidationException("Minimum quantity of at least 1 is required when apply scope is PRODUCT");
            }
            if (request.getMinOrderAmount() != null) {
                throw new ValidationException("Minimum order amount must be null when apply scope is PRODUCT");
            }
        } else if (applyScope == ApplyScope.ORDER) {
            if (request.getMinOrderAmount() == null || request.getMinOrderAmount().compareTo(BigDecimal.ZERO) <= 0) {
                throw new ValidationException("Minimum order amount greater than zero is required when apply scope is ORDER");
            }
            if (request.getProductId() != null) {
                throw new ValidationException("Product ID must be null when apply scope is ORDER");
            }
            if (request.getMinQty() != null) {
                throw new ValidationException("Minimum quantity must be null when apply scope is ORDER");
            }
        }
    }

    private PromotionResponse mapToResponse(Promotion promotion) {
        Long productId = null;
        String productName = null;
        if (promotion.getProduct() != null) {
            productId = promotion.getProduct().getId();
            productName = promotion.getProduct().getName();
        }

        return PromotionResponse.builder()
                .id(promotion.getId())
                .name(promotion.getName())
                .applyScope(promotion.getApplyScope().name())
                .productId(productId)
                .productName(productName)
                .minQty(promotion.getMinQty())
                .minOrderAmount(promotion.getMinOrderAmount())
                .discountType(promotion.getDiscountType().name())
                .value(promotion.getValue())
                .active(promotion.getActive())
                .build();
    }
}
