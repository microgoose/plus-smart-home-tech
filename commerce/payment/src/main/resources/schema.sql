CREATE SCHEMA IF NOT EXISTS payment;
SET search_path TO payment;

CREATE TABLE IF NOT EXISTS payments (
    payment_id UUID PRIMARY KEY,
    order_id UUID NOT NULL,
    product_cost DOUBLE PRECISION NOT NULL,
    delivery_cost DOUBLE PRECISION NOT NULL,
    tax DOUBLE PRECISION NOT NULL,
    total_cost DOUBLE PRECISION NOT NULL,
    status VARCHAR(20) NOT NULL
);
