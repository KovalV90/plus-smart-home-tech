CREATE TABLE IF NOT EXISTS payments (
    payment_id UUID PRIMARY KEY,
    amount DOUBLE PRECISION,
    status VARCHAR(50),
    username VARCHAR(255)
);
