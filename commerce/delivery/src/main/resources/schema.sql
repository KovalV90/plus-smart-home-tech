CREATE TABLE IF NOT EXISTS deliveries (
    delivery_id UUID PRIMARY KEY,
    order_id UUID,
    address TEXT,
    weight DOUBLE PRECISION,
    volume DOUBLE PRECISION,
    fragile BOOLEAN,
    state VARCHAR(50)
);
