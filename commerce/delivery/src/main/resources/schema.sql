CREATE TABLE IF NOT EXISTS deliveries (
    delivery_id UUID PRIMARY KEY,
    order_id UUID,

    -- Address To
    address_country VARCHAR(100),
    address_city VARCHAR(100),
    address_street VARCHAR(100),
    address_house VARCHAR(50),
    address_flat VARCHAR(50),

    -- Address From
    from_address_country VARCHAR(100),
    from_address_city VARCHAR(100),
    from_address_street VARCHAR(100),
    from_address_house VARCHAR(50),
    from_address_flat VARCHAR(50),

    weight DOUBLE PRECISION,
    volume DOUBLE PRECISION,
    fragile BOOLEAN,

    state VARCHAR(50)
);
