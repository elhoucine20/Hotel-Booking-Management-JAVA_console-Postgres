ALTER TABLE invoices DROP CONSTRAINT fk_invoice_reservation;
ALTER TABLE invoices DROP COLUMN reservation_id;
ALTER TABLE invoices ADD COLUMN payment_id UUID NOT NULL;
ALTER TABLE invoices ADD CONSTRAINT fk_invoice_payment FOREIGN KEY (payment_id) REFERENCES payments(id);