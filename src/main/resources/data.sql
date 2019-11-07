INSERT INTO USERS (id, name, birthday, address_detail, city, country, cards_id, user_credentials_id) VALUES
('dd60294b-6f21-4449-8796-646ce0c5a98a', 'Catalin Pirvu', '1998-11-09', 'Bd. Pierre de Coubertin', 'Bucuresti', 'Romania', '8f294277-21bd-4d71-977c-ac4b8518814f', '820e7f90-4d0c-481f-ae13-db6874f84173'),
('21e63e7f-ac6d-4ccc-a60b-6cb0fb249329', 'Stefan Anca', '1998-6-15', 'Bd. Regiei', 'Bucuresti', 'Romania', '88ee5a42-3747-4edb-bfca-effdd5cd146d', '7e882609-a2b9-4876-b241-02095b204781'),
('461c08bf-79dc-4eb2-be18-5ce1125af796', 'Admin', '1998-11-09', 'localhost', 'localhost', 'localhost', 'e61fe61a-236b-4f6c-a2c7-9429af8a2416', '3cf14d7c-9472-4aae-a94e-34852c5c7a93');

INSERT INTO USER_CREDENTIALS (id, username, password) VALUES
('820e7f90-4d0c-481f-ae13-db6874f84173', 'catalin', '$2a$10$CI6xhgAlVMSpQeMGhqWwge8eLTiaBNk7Z71CWSCcLy7fEn7PEcQae'),-- username "catalin" , password "pirvu"
('7e882609-a2b9-4876-b241-02095b204781', 'stefan', '$2a$10$DbSH/fSKWFvD6FZ5DXboAuBZ1kCdVoyn/eyRN4NQKLo.FNQf6XF.e'),-- username "stefan" , password "anca"
('3cf14d7c-9472-4aae-a94e-34852c5c7a93', 'admin', '$2a$10$ZQWIolotQrZ5z/RybJi3kO5jKoVhqdrF9p5F9yqLTHm3mrb6v4XYa');-- username "admin" , password "nimda"

INSERT INTO CARDS (id, card_number, card_expiry_date, card_cvv, cardholder_name) VALUES
('8f294277-21bd-4d71-977c-ac4b8518814f', '4018359857520680', '11/23', '994', 'PIRVU CATALIN'),
('88ee5a42-3747-4edb-bfca-effdd5cd146d', '5127994616377793', '07/24', '451', 'ANCA STEFAN'),
('e61fe61a-236b-4f6c-a2c7-9429af8a2416', '1444444444444444', '12/99', '000', 'ADMIN');

INSERT INTO AUCTION_INFO (id, datetime_start, datetime_end, is_validated, seller_id, additional_info, starting_price, target_price, current_price_bidded) VALUES
('9c98bdb3-fc8f-4a6b-aed3-260883c541ed', parsedatetime('29-10-2019 18:47:52', 'dd-MM-yyyy hh:mm:ss'), parsedatetime('30-11-2019 18:47:52', 'dd-MM-yyyy hh:mm:ss'), true, 'dd60294b-6f21-4449-8796-646ce0c5a98a', 'If the target price is not reached, I''m going to cancel the auction!',100,500,0),
('d35e7d4c-dd5b-465e-a7b1-2ecc425c115a', parsedatetime('29-10-2019 18:30:52', 'dd-MM-yyyy hh:mm:ss'), parsedatetime('01-12-2019 17:52:52', 'dd-MM-yyyy hh:mm:ss'), true, '21e63e7f-ac6d-4ccc-a60b-6cb0fb249329', 'Cheap price, won''t last for long!',50,200,0);

INSERT INTO PHONES (id, phone_brand, phone_model) VALUES
('3c95f85b-04a1-497c-acd4-422f69fd27fa', 'SAMSUNG', 'Galaxy Note 10'),
('90280bc9-2a6a-4b54-8fc6-e0ec53c7b7d8', 'APPLE', 'iPhone 9'),
('7da5b26d-fe45-4c82-ba71-63a64651dfea', 'APPLE', 'iPhone 8');

INSERT INTO PHONES_IN_AUCTION (id, auction_info_id, phone_id) VALUES
('f359f7bf-1baa-4bfe-95c2-7563ea248fa9', '9c98bdb3-fc8f-4a6b-aed3-260883c541ed', '90280bc9-2a6a-4b54-8fc6-e0ec53c7b7d8'),
('c84f857e-954f-42bd-95c9-b43a9b3768b8', '9c98bdb3-fc8f-4a6b-aed3-260883c541ed', '7da5b26d-fe45-4c82-ba71-63a64651dfea'),
('3485ea59-c0ed-4b82-8c7a-45d7e279264c', 'd35e7d4c-dd5b-465e-a7b1-2ecc425c115a', '3c95f85b-04a1-497c-acd4-422f69fd27fa');

