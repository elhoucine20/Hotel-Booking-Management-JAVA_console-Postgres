CREATE TABLE reservations (
                              id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
                              reservationCode VARCHAR(50) NOT NULL UNIQUE,
                              user_id UUID NOT NULL,
                              room_id UUID NOT NULL,
                              numberOfGuests INTEGER NOT NULL,
                              check_in DATE NOT NULL,
                              check_out DATE NOT NULL,
                              reservationStatus VARCHAR(30) NOT NULL,
                              total_amount NUMERIC(10, 2) NOT NULL,
                              created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,

                              CONSTRAINT fk_reservation_user
                                  FOREIGN KEY (user_id)
                                      REFERENCES users(id),

                              CONSTRAINT fk_reservation_room
                                  FOREIGN KEY (room_id)
                                      REFERENCES rooms(id),

                              CONSTRAINT chk_reservation_guests
                                  CHECK (numberOfGuests > 0),

                              CONSTRAINT chk_reservation_dates
                                  CHECK (check_out > check_in),

                              CONSTRAINT chk_reservation_amount
                                  CHECK (total_amount > 0),

                              CONSTRAINT chk_reservation_status
                                  CHECK (
                                      reservationStatus IN ('CONFIRMED','CANCELLED', 'COMPLETED' ) )
);