-- DROP TABLE IF EXISTS USER_CREDENTIALS;
-- DROP TABLE IF EXISTS USERS;
-- DROP TABLE IF EXISTS CARDS;
-- DROP TABLE IF EXISTS AUCTION_INFO;
-- DROP TABLE IF EXISTS PHONES;
-- DROP TABLE IF EXISTS PHONES_IN_AUCTION;
-- DROP TABLE IF EXISTS REVIEWS;

CREATE TABLE USERS (
  id VARCHAR(255) NOT NULL,
  name VARCHAR(255) NOT NULL,
  birthday DATE NOT NULL,
  address_detail VARCHAR(255) NOT NULL,
  city VARCHAR(255) NOT NULL,
  country VARCHAR(255) NOT NULL,
  cards_id VARCHAR(255) NOT NULL,
  user_credentials_id VARCHAR(255) NOT NULL
);

CREATE TABLE USER_CREDENTIALS (
  id VARCHAR(255) NOT NULL,
  username VARCHAR(255) NOT NULL,
  password VARCHAR(255) NOT NULL
);

CREATE TABLE CARDS (
  id VARCHAR(255) NOT NULL,
  card_number VARCHAR(255) NOT NULL,
  card_expiry_date VARCHAR(255) NOT NULL,
  card_cvv VARCHAR(255) NOT NULL,
  cardholder_name VARCHAR(255) NOT NULL
);

CREATE TABLE AUCTION_INFO (
  id VARCHAR(255) NOT NULL,
  datetime_start TIMESTAMP NOT NULL,
  datetime_end TIMESTAMP NOT NULL,
  is_validated BOOLEAN NOT NULL,
  is_successfully_done BOOLEAN NOT NULL,
  seller_id VARCHAR(255) NOT NULL,
  additional_info VARCHAR(255) NOT NULL,
  starting_price DOUBLE NOT NULL,
  target_price DOUBLE NOT NULL,
  current_price_bidded DOUBLE NOT NULL
);

CREATE TABLE PHONES (
  id VARCHAR(255) NOT NULL,
  phone_brand VARCHAR(255) NOT NULL,
  phone_model VARCHAR(255) NOT NULL
);

CREATE TABLE PHONES_IN_AUCTION (
  id VARCHAR(255) NOT NULL,
  auction_info_id VARCHAR(255) NOT NULL,
  phone_id VARCHAR(255) NOT NULL
);

CREATE TABLE REVIEWS (
  id VARCHAR(255) NOT NULL,
  reviewer_id VARCHAR(255) NOT NULL, --only the buyer reviews
  auction_info_id VARCHAR(255) NOT NULL,
  stars INTEGER NOT NULL,
  review VARCHAR(255) NOT NULL
);

CREATE TABLE BIDS (
  id VARCHAR(255) NOT NULL,
  auction_info_id VARCHAR(255) NOT NULL,
  buyer_id VARCHAR(255) NOT NULL,
  datetime_bidded TIMESTAMP NOT NULL,
  price_bidded DOUBLE NOT NULL
);