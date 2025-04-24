-- Схема: shopping_store
CREATE SCHEMA IF NOT EXISTS shopping_cart;
SET search_path TO shopping_cart;

-- shopping_cart: Корзина пользователя
CREATE TABLE IF NOT EXISTS shopping_cart (
    id UUID PRIMARY KEY,
    username VARCHAR(255) NOT NULL,
    active BOOLEAN NOT NULL DEFAULT true,
    created_at TIMESTAMP NOT NULL DEFAULT NOW(),
    updated_at TIMESTAMP NOT NULL DEFAULT NOW()
);

-- shopping_cart_item: Позиции товаров в корзине
CREATE TABLE IF NOT EXISTS shopping_cart_item (
    id UUID PRIMARY KEY,
    cart_id UUID NOT NULL REFERENCES shopping_cart(id) ON DELETE CASCADE,
    product_id UUID NOT NULL,
    quantity BIGINT NOT NULL CHECK (quantity >= 0),
    created_at TIMESTAMP NOT NULL DEFAULT NOW(),
    updated_at TIMESTAMP NOT NULL DEFAULT NOW(),

    CONSTRAINT unique_cart_product UNIQUE (cart_id, product_id)
);