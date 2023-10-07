drop table if exists BOOKING_CONVENIENCE;
DROP table if exists BOOKING;
DROP table if exists FAVOURITE_OBJECT;
DROP table if exists RATING;
drop table if exists CONVENIENCE;
DROP table if exists ACCOMMODATION;
DROP table if exists USERS;
DROP table if exists ADDRESS;
CREATE TABLE ADDRESS
(
    ADDRESS_ID       BIGINT AUTO_INCREMENT NOT NULL,
    CITY             VARCHAR(255) NOT NULL,
    HOUSE_NUMBER     VARCHAR(255) NOT NULL,
    APARTMENT_NUMBER VARCHAR(255),
    POSTAL_CODE      VARCHAR(255) NOT NULL,
    POST             VARCHAR(255) NOT NULL,
    COUNTY           VARCHAR(255) NOT NULL,
    PROVINCE         VARCHAR(255) NOT NULL,
    COUNTRY          VARCHAR(255) NOT NULL,
    STREET           VARCHAR(255),
    CONSTRAINT PK_ADDRESS PRIMARY KEY (ADDRESS_ID)
);

CREATE TABLE USERS
(
    USER_ID      BINARY(16) NOT NULL,
    FIRST_NAME   VARCHAR(255),
    LAST_NAME    VARCHAR(255),
    PHONE_NUMBER VARCHAR(9),
    CONSTRAINT PK_USER PRIMARY KEY (USER_ID)
);

CREATE TABLE ACCOMMODATION
(
    ACCOMMODATION_ID BIGINT AUTO_INCREMENT NOT NULL,
    NAME             varchar(100)   not null,
    ADDRESS_ID       BIGINT         NOT NULL,
    MAX_NO_OF_PEOPLE INT            NOT NULL,
    USER_ID          BINARY(16) NOT NULL,
    PRICE_FOR_DAY    DECIMAL(10, 2) NOT NULL,
    SQUARE_FOOTAGE   DECIMAL(10, 2),
    DESCRIPTION      VARCHAR(300),
    CONSTRAINT PK_ACCOMMODATION PRIMARY KEY (ACCOMMODATION_ID),
    CONSTRAINT FK_USER_ACCOMMODATION FOREIGN KEY (USER_ID) REFERENCES USERS (USER_ID),
    CONSTRAINT FK_ADDRESS_ACCOMMODATION FOREIGN KEY (ADDRESS_ID) REFERENCES ADDRESS (ADDRESS_ID)
);

CREATE TABLE RATING
(
    RATING_ID        BIGINT AUTO_INCREMENT NOT NULL,
    USER_ID          BINARY(16) NOT NULL,
    ACCOMMODATION_ID BIGINT NOT NULL,
    GRADE            INT    NOT NULL,
    DESCRIPTION      VARCHAR(300),
    CONSTRAINT PK_RATING PRIMARY KEY (RATING_ID),
    CONSTRAINT FK_USER_RATING FOREIGN KEY (USER_ID) REFERENCES USERS (USER_ID),
    CONSTRAINT FK_ACCOMMODATION_RATING FOREIGN KEY (ACCOMMODATION_ID) REFERENCES ACCOMMODATION (ACCOMMODATION_ID)
);

CREATE TABLE FAVOURITE_OBJECT
(
    USER_ID          BINARY(16) NOT NULL,
    ACCOMMODATION_ID BIGINT NOT NULL,
    CONSTRAINT PK_FAVOURITE_OBJECT PRIMARY KEY (USER_ID, ACCOMMODATION_ID),
    CONSTRAINT FK_USER_FAVOURITE_OBJECT FOREIGN KEY (USER_ID) REFERENCES USERS (USER_ID),
    CONSTRAINT FK_ACCOMMODATION_FAVOURITE_OBJECT FOREIGN KEY (ACCOMMODATION_ID) REFERENCES ACCOMMODATION (ACCOMMODATION_ID)
);

CREATE TABLE BOOKING
(
    BOOKING_ID       BIGINT AUTO_INCREMENT NOT NULL,
    USER_ID          BINARY(16) NOT NULL,
    ACCOMMODATION_ID BIGINT         NOT NULL,
    START_DATE       TIMESTAMP      NOT NULL,
    END_DATE         TIMESTAMP      NOT NULL,
    BOOKING_DATE     TIMESTAMP      NOT NULL,
    PRICE            DECIMAL(10, 2) NOT NULL,
    STATUS           VARCHAR(255)   NOT NULL,
    CONSTRAINT PK_BOOKING PRIMARY KEY (BOOKING_ID),
    CONSTRAINT FK_USER_BOOKING FOREIGN KEY (USER_ID) REFERENCES USERS (USER_ID),
    CONSTRAINT FK_ACCOMMODATION_BOOKING FOREIGN KEY (ACCOMMODATION_ID) REFERENCES ACCOMMODATION (ACCOMMODATION_ID)
);

CREATE TABLE CONVENIENCE
(
    ACCOMMODATION_ID BIGINT      NOT NULL,
    CONVENIENCE_TYPE VARCHAR(50) NOT NULL,
    PRICE            DECIMAL(10, 2),
    CONSTRAINT PK_CONVENIENCE PRIMARY KEY (ACCOMMODATION_ID, CONVENIENCE_TYPE),
    CONSTRAINT FK_ACCOMMODATION_CONVENIENCE FOREIGN KEY (ACCOMMODATION_ID) REFERENCES ACCOMMODATION (ACCOMMODATION_ID)
);

CREATE TABLE BOOKING_CONVENIENCE
(
    BOOKING_ID       BIGINT      NOT NULL,
    CONVENIENCE_TYPE VARCHAR(50) NOT NULL,
    PRICE            DECIMAL(10, 2),
    CONSTRAINT PK_BOOKING_CONVENIENCE PRIMARY KEY (BOOKING_ID, CONVENIENCE_TYPE),
    CONSTRAINT FK_BOOKING_CONVENIENCE FOREIGN KEY (BOOKING_ID) REFERENCES BOOKING (BOOKING_ID)
)