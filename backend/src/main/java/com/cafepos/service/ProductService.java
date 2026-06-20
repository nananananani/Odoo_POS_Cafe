package com.cafepos.service;

import com.cafepos.dto.request.BulkActionRequest;
import com.cafepos.dto.request.ProductRequest;
import com.cafepos.dto.response.ProductResponse;
import com.cafepos.entity.Category;
import com.cafepos.entity.Product;
import com.cafepos.exception.NotFoundException;
import com.cafepos.exception.ValidationException;
import com.cafepos.repository.CategoryRepository;
import com.cafepos.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;

    @Transactional(readOnly = true)
    public List<ProductResponse> getProducts(Long categoryId, String search) {
        List<Product> products;
        if (categoryId != null || search != null) {
            products = productRepository.searchProducts(categoryId, search);
        } else {
            products = productRepository.findAll();
        }
        return products.stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public ProductResponse getProductById(Long id) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Product not found with id: " + id));
        return mapToResponse(product);
    }

    @Transactional
    public ProductResponse createProduct(ProductRequest request) {
        Category category = categoryRepository.findById(request.getCategoryId())
                .orElseThrow(() -> new ValidationException("Category not found with id: " + request.getCategoryId()));

        Product product = Product.builder()
                .name(request.getName())
                .category(category)
                .price(request.getPrice())
                .uom(request.getUom())
                .taxPct(request.getTaxPct())
                .description(request.getDescription())
                .showOnKds(request.getShowOnKds() != null ? request.getShowOnKds() : true)
                .active(request.getActive() != null ? request.getActive() : true)
                .build();

        Product savedProduct = productRepository.save(product);
        return mapToResponse(savedProduct);
    }

    @Transactional
    public ProductResponse updateProduct(Long id, ProductRequest request) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Product not found with id: " + id));

        Category category = categoryRepository.findById(request.getCategoryId())
                .orElseThrow(() -> new ValidationException("Category not found with id: " + request.getCategoryId()));

        product.setName(request.getName());
        product.setCategory(category);
        product.setPrice(request.getPrice());
        product.setUom(request.getUom());
        product.setTaxPct(request.getTaxPct());
        product.setDescription(request.getDescription());
        if (request.getShowOnKds() != null) {
            product.setShowOnKds(request.getShowOnKds());
        }
        if (request.getActive() != null) {
            product.setActive(request.getActive());
        }

        Product updatedProduct = productRepository.save(product);
        return mapToResponse(updatedProduct);
    }

    @Transactional
    public void deleteProduct(Long id) {
        if (!productRepository.existsById(id)) {
            throw new NotFoundException("Product not found with id: " + id);
        }
        productRepository.deleteById(id);
    }

    @Transactional
    public void bulkAction(BulkActionRequest request) {
        if ("delete".equalsIgnoreCase(request.getAction())) {
            productRepository.deleteAllByIdInBatch(request.getIds());
        } else if ("archive".equalsIgnoreCase(request.getAction())) {
            List<Product> products = productRepository.findAllById(request.getIds());
            for (Product product : products) {
                product.setActive(false);
            }
            productRepository.saveAll(products);
        }
    }

    private ProductResponse mapToResponse(Product product) {
        return ProductResponse.builder()
                .id(product.getId())
                .name(product.getName())
                .categoryId(product.getCategory().getId())
                .categoryName(product.getCategory().getName())
                .categoryColors(product.getCategory().getColors())
                .price(product.getPrice())
                .uom(product.getUom())
                .taxPct(product.getTaxPct())
                .description(product.getDescription())
                .showOnKds(product.getShowOnKds())
                .active(product.getActive())
                .build();
    }
}
