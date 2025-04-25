CREATE TABLE IF NOT EXISTS payments (
    payment_id UUID PRIMARY KEY,
    order_id UUID NOT NULL,
    amount DOUBLE PRECISION,
    delivery_price DOUBLE PRECISION,
    total_price DOUBLE PRECISION,
    status VARCHAR(50),
    username VARCHAR(255)
);
