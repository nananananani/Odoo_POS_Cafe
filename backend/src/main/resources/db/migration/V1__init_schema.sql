-- V1__init_schema.sql

-- 1. Users Table
CREATE TABLE users (
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    email VARCHAR(255) NOT NULL UNIQUE,
    password_hash VARCHAR(255) NOT NULL,
    role VARCHAR(50) NOT NULL,
    status VARCHAR(50) NOT NULL,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

-- 2. Categories Table
CREATE TABLE categories (
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    colors TEXT NOT NULL
);

-- 3. Products Table
CREATE TABLE products (
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    category_id BIGINT NOT NULL REFERENCES categories(id) ON DELETE RESTRICT,
    price DECIMAL(12,2) NOT NULL,
    uom VARCHAR(50) NOT NULL,
    tax_pct DECIMAL(5,2) NOT NULL,
    description TEXT,
    show_on_kds BOOLEAN NOT NULL DEFAULT TRUE
);
CREATE INDEX idx_products_category_id ON products(category_id);

-- 4. Payment Methods Table
CREATE TABLE payment_methods (
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    type VARCHAR(50) NOT NULL,
    upi_id VARCHAR(255),
    active BOOLEAN NOT NULL DEFAULT TRUE
);

-- 5. Floors Table
CREATE TABLE floors (
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR(255) NOT NULL
);

-- 6. Tables Table (pos_table to avoid SQL keyword conflicts)
CREATE TABLE pos_table (
    id BIGSERIAL PRIMARY KEY,
    floor_id BIGINT NOT NULL REFERENCES floors(id) ON DELETE RESTRICT,
    number INT NOT NULL,
    seats INT NOT NULL,
    active BOOLEAN NOT NULL DEFAULT TRUE
);
CREATE INDEX idx_pos_table_floor_id ON pos_table(floor_id);

-- 7. Coupons Table
CREATE TABLE coupons (
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    code VARCHAR(255) NOT NULL UNIQUE,
    discount_type VARCHAR(50) NOT NULL,
    value DECIMAL(12,2) NOT NULL,
    active BOOLEAN NOT NULL DEFAULT TRUE
);

-- 8. Promotions Table
CREATE TABLE promotions (
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    apply_scope VARCHAR(50) NOT NULL,
    product_id BIGINT REFERENCES products(id) ON DELETE SET NULL,
    min_qty INT,
    min_order_amount DECIMAL(12,2),
    discount_type VARCHAR(50) NOT NULL,
    value DECIMAL(12,2) NOT NULL,
    active BOOLEAN NOT NULL DEFAULT TRUE
);
CREATE INDEX idx_promotions_product_id ON promotions(product_id);

-- 9. Customers Table
CREATE TABLE customers (
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    email VARCHAR(255),
    phone VARCHAR(50)
);

-- 10. POS Sessions Table
CREATE TABLE pos_sessions (
    id BIGSERIAL PRIMARY KEY,
    employee_id BIGINT NOT NULL REFERENCES users(id) ON DELETE RESTRICT,
    opened_at TIMESTAMP NOT NULL,
    closed_at TIMESTAMP,
    closing_amount DECIMAL(12,2)
);
CREATE INDEX idx_pos_sessions_employee_id ON pos_sessions(employee_id);

-- 11. Orders Table
CREATE TABLE orders (
    id BIGSERIAL PRIMARY KEY,
    session_id BIGINT NOT NULL REFERENCES pos_sessions(id) ON DELETE RESTRICT,
    table_id BIGINT REFERENCES pos_table(id) ON DELETE SET NULL,
    customer_id BIGINT REFERENCES customers(id) ON DELETE SET NULL,
    order_number INT NOT NULL,
    status VARCHAR(50) NOT NULL,
    source VARCHAR(50) NOT NULL,
    coupon_id BIGINT REFERENCES coupons(id) ON DELETE SET NULL,
    promotion_id BIGINT REFERENCES promotions(id) ON DELETE SET NULL,
    subtotal DECIMAL(12,2) NOT NULL,
    tax_total DECIMAL(12,2) NOT NULL,
    discount_total DECIMAL(12,2) NOT NULL,
    total DECIMAL(12,2) NOT NULL,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);
CREATE INDEX idx_orders_session_id ON orders(session_id);
CREATE INDEX idx_orders_table_id ON orders(table_id);
CREATE INDEX idx_orders_customer_id ON orders(customer_id);
CREATE INDEX idx_orders_coupon_id ON orders(coupon_id);
CREATE INDEX idx_orders_promotion_id ON orders(promotion_id);
CREATE INDEX idx_orders_status ON orders(status);

-- 12. Order Items Table
CREATE TABLE order_items (
    id BIGSERIAL PRIMARY KEY,
    order_id BIGINT NOT NULL REFERENCES orders(id) ON DELETE CASCADE,
    product_id BIGINT NOT NULL REFERENCES products(id) ON DELETE RESTRICT,
    qty INT NOT NULL,
    unit_price DECIMAL(12,2) NOT NULL,
    line_total DECIMAL(12,2) NOT NULL,
    kds_status VARCHAR(50) NOT NULL
);
CREATE INDEX idx_order_items_order_id ON order_items(order_id);
CREATE INDEX idx_order_items_product_id ON order_items(product_id);
CREATE INDEX idx_order_items_kds_status ON order_items(kds_status);

-- 13. Payments Table
CREATE TABLE payments (
    id BIGSERIAL PRIMARY KEY,
    order_id BIGINT NOT NULL REFERENCES orders(id) ON DELETE CASCADE,
    method_id BIGINT NOT NULL REFERENCES payment_methods(id) ON DELETE RESTRICT,
    amount DECIMAL(12,2) NOT NULL,
    cash_received DECIMAL(12,2),
    change_due DECIMAL(12,2),
    reference VARCHAR(255),
    status VARCHAR(50) NOT NULL,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);
CREATE INDEX idx_payments_order_id ON payments(order_id);
CREATE INDEX idx_payments_method_id ON payments(method_id);

-- 14. Self Order Configs Table
CREATE TABLE self_order_configs (
    id BIGSERIAL PRIMARY KEY,
    enabled BOOLEAN NOT NULL DEFAULT TRUE,
    mode VARCHAR(50) NOT NULL,
    bg_color VARCHAR(50),
    bg_images TEXT
);

-- 15. Table QR Tokens Table
CREATE TABLE table_qr_tokens (
    id BIGSERIAL PRIMARY KEY,
    table_id BIGINT NOT NULL REFERENCES pos_table(id) ON DELETE CASCADE,
    token VARCHAR(255) NOT NULL UNIQUE
);
CREATE INDEX idx_table_qr_tokens_table_id ON table_qr_tokens(table_id);
