CREATE TABLE payments (
                          id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
                          reservation_id UUID NOT NULL,
                          amount NUMERIC(10, 2) NOT NULL,
                          payment_date TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
                          --status VARCHAR(30) NOT NULL,
                          payment_method  VARCHAR(30) NOT NULL,

                          CONSTRAINT fk_payment_reservation
                              FOREIGN KEY (reservation_id)
                                  REFERENCES reservations(id),

                          CONSTRAINT chk_payment_amount
                              CHECK (amount > 0),

                          CONSTRAINT chk_payment_method
                              CHECK ( payment_method  IN ('cash','paypal') )

);