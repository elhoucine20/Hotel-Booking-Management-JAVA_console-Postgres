CREATE TABLE rooms (
                       id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
                       roomNumber VARCHAR(20) NOT NULL UNIQUE,
                       user_id UUID NOT NULL ,
                       roomType VARCHAR(30) NOT NULL,
                       pricePerNight NUMERIC(10, 2) NOT NULL,
                       roomStatus VARCHAR(30) NOT NULL,
                       capacity INTEGER NOT NULL ,

                       CONSTRAINT fk_room_user
                           FOREIGN KEY (user_id)
                               REFERENCES users(id),

                       CONSTRAINT chk_room_price
                           CHECK (pricePerNight > 0),

                       CONSTRAINT chk_room_type
                           CHECK (roomType IN ('SINGLE','DOUBLE','SUITE')),

                       CONSTRAINT chk_room_status
                           CHECK (roomStatus IN ('AVAILABLE', 'OCCUPIED', 'MAINTENANCE'))
);