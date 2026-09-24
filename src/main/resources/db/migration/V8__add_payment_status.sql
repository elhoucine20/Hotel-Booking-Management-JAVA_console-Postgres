ALTER TABLE payments
ADD COLUMN payment_status VARCHAR(30) NOT NULL DEFAULT 'PENDING';

ALTER TABLE payments
ADD CONSTRAINT chk_payment_status
CHECK (payment_status IN ('PENDING','PAID','FAILD'));
