CREATE TABLE invoices (
                          id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
                          reservation_id UUID NOT NULL,
                          invoice_number VARCHAR(50) NOT NULL UNIQUE,
                          subtotal_ht NUMERIC(10, 2) NOT NULL,  -- montant sans TVA
                          vat_amount NUMERIC(10, 2) NOT NULL,   -- montant de TVA
                          total_ttc NUMERIC(10, 2) NOT NULL,    --montant total
                          created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,

                          CONSTRAINT fk_invoice_reservation
                              FOREIGN KEY (reservation_id)
                                  REFERENCES reservations(id),

                          CONSTRAINT chk_invoice_amounts
                              CHECK (
                                  subtotal_ht > 0
                                      AND vat_amount >= 0
                                      AND total_ttc > 0
                                  ),

                          CONSTRAINT chk_invoice_total
                              CHECK (total_ttc = subtotal_ht + vat_amount)
);