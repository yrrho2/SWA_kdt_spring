CREATE  TABLE customers(
    customerID BINARY(16) PRIMARY KEY,
    name        varchar(20) NOT NULL ,
    email       varchar(50) NOT NULL,
    last_login_at datetime(6)   DEFAULT NULL,
    create_at datetime(6) NOT NULL DEFAULT CRRENT_TIMESTAMP(6),
    CONSTRAINT unq_user_email UNIQUE (email)
);