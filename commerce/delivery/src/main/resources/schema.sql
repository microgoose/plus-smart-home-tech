CREATE SCHEMA IF NOT EXISTS delivery;
SET search_path TO delivery;

CREATE TABLE IF NOT EXISTS deliveries (
    delivery_id UUID PRIMARY KEY,
    order_id UUID NOT NULL,

    -- Адрес отправления (fromAddress)
    from_country VARCHAR(255),
    from_city VARCHAR(255),
    from_street VARCHAR(255),
    from_house VARCHAR(255),
    from_flat VARCHAR(255),

    -- Адрес назначения (toAddress)
    to_country VARCHAR(255),
    to_city VARCHAR(255),
    to_street VARCHAR(255),
    to_house VARCHAR(255),
    to_flat VARCHAR(255),

    -- Статус доставки
    delivery_state VARCHAR(20) NOT NULL
);
