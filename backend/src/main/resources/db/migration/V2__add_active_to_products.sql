-- V2__add_active_to_products.sql
ALTER TABLE products ADD COLUMN active BOOLEAN NOT NULL DEFAULT TRUE;
