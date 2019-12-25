INSERT INTO USERS (id, name, birthday, address_detail, city, country, cards_id, user_credentials_id) VALUES
('dd60294b-6f21-4449-8796-646ce0c5a98a', 'Catalin Pirvu', '1998-11-09', 'Bd. Pierre de Coubertin', 'Bucuresti', 'Romania', '8f294277-21bd-4d71-977c-ac4b8518814f', '820e7f90-4d0c-481f-ae13-db6874f84173'),
('21e63e7f-ac6d-4ccc-a60b-6cb0fb249329', 'Stefan Anca', '1998-6-15', 'Bd. Regiei', 'Bucuresti', 'Romania', '88ee5a42-3747-4edb-bfca-effdd5cd146d', '7e882609-a2b9-4876-b241-02095b204781'),
('461c08bf-79dc-4eb2-be18-5ce1125af796', 'Admin', '1998-11-09', 'localhost', 'localhost', 'localhost', 'e61fe61a-236b-4f6c-a2c7-9429af8a2416', '3cf14d7c-9472-4aae-a94e-34852c5c7a93'),
('d7262666-81eb-4e12-9a08-715b53aa641d', 'Carina Stoica', '1998-11-08', 'Bd. Iuliu Maniu 111', 'Bucuresti', 'Romania', '95c7a836-5443-4a73-b405-c96c9d1d0110', 'a8293003-0996-4496-9a0f-93bf9b5c86cf');

INSERT INTO USER_CREDENTIALS (id, username, password) VALUES
('820e7f90-4d0c-481f-ae13-db6874f84173', 'catalin', '$2a$10$CI6xhgAlVMSpQeMGhqWwge8eLTiaBNk7Z71CWSCcLy7fEn7PEcQae'),-- username "catalin" , password "pirvu"
('7e882609-a2b9-4876-b241-02095b204781', 'stefan', '$2a$10$DbSH/fSKWFvD6FZ5DXboAuBZ1kCdVoyn/eyRN4NQKLo.FNQf6XF.e'),-- username "stefan" , password "anca"
('3cf14d7c-9472-4aae-a94e-34852c5c7a93', 'admin', '$2a$10$ZQWIolotQrZ5z/RybJi3kO5jKoVhqdrF9p5F9yqLTHm3mrb6v4XYa'),-- username "admin" , password "nimda"
('a8293003-0996-4496-9a0f-93bf9b5c86cf', 'carina', '$2a$10$/vRJ1RjbxsH6MrNSFLyzVezP4oERzHUVykO37Seg67ceRZuTrG2Hi');-- username "carina" , password "stoica"

INSERT INTO CARDS (id, card_number, card_expiry_date, card_cvv, cardholder_name) VALUES
('8f294277-21bd-4d71-977c-ac4b8518814f', '4018359857520680', '11/23', '994', 'PIRVU CATALIN'),
('88ee5a42-3747-4edb-bfca-effdd5cd146d', '5127994616377793', '07/24', '451', 'ANCA STEFAN'),
('e61fe61a-236b-4f6c-a2c7-9429af8a2416', '1444444444444444', '12/99', '000', 'ADMIN'),
('95c7a836-5443-4a73-b405-c96c9d1d0110', '5168441223630339', '10/22', '123', 'STOICA CARINA');

INSERT INTO AUCTION_INFO (id, datetime_start, datetime_end, is_validated, is_successfully_done, seller_id, additional_info, starting_price, target_price, current_price_bidded) VALUES
('9c98bdb3-fc8f-4a6b-aed3-260883c541ed', parsedatetime('29-10-2019 18:47:52', 'dd-MM-yyyy hh:mm:ss'), parsedatetime('17-03-2020 16:50:52', 'dd-MM-yyyy hh:mm:ss'), true, false, 'dd60294b-6f21-4449-8796-646ce0c5a98a', 'If the target price is not reached, I''m going to cancel the auction!',100,500,450),
('d35e7d4c-dd5b-465e-a7b1-2ecc425c115a', parsedatetime('29-10-2019 18:30:52', 'dd-MM-yyyy hh:mm:ss'), parsedatetime('01-12-2019 17:52:52', 'dd-MM-yyyy hh:mm:ss'), true, false, '21e63e7f-ac6d-4ccc-a60b-6cb0fb249329', 'Cheap price, won''t last for long!',50,200,0),
('009fa935-ed0a-4c38-955c-19778cf9c379', parsedatetime('29-09-2019 15:40:52', 'dd-MM-yyyy hh:mm:ss'), parsedatetime('29-10-2019 23:59:59', 'dd-MM-yyyy hh:mm:ss'), true, true, '21e63e7f-ac6d-4ccc-a60b-6cb0fb249329', 'Nice looking phones.',100,250,300),
('99911c3e-2d96-4668-a1c3-cfe76e0eed93', parsedatetime('21-11-2019 10:45:32', 'dd-MM-yyyy hh:mm:ss'), parsedatetime('29-03-2020 22:39:42', 'dd-MM-yyyy hh:mm:ss'), false, false, 'dd60294b-6f21-4449-8796-646ce0c5a98a', 'Nice looking phones.',40,105,0);

INSERT INTO PHONES (id, phone_brand, phone_model) VALUES
('3c95f85b-04a1-497c-acd4-422f69fd27fa', 'SAMSUNG', 'Galaxy Note 10'),
('90280bc9-2a6a-4b54-8fc6-e0ec53c7b7d8', 'APPLE', 'iPhone 9'),
('7da5b26d-fe45-4c82-ba71-63a64651dfea', 'APPLE', 'iPhone 8');

INSERT INTO PHONES_IN_AUCTION (id, auction_info_id, phone_id) VALUES
('f359f7bf-1baa-4bfe-95c2-7563ea248fa9', '9c98bdb3-fc8f-4a6b-aed3-260883c541ed', '90280bc9-2a6a-4b54-8fc6-e0ec53c7b7d8'),
('c84f857e-954f-42bd-95c9-b43a9b3768b8', '9c98bdb3-fc8f-4a6b-aed3-260883c541ed', '7da5b26d-fe45-4c82-ba71-63a64651dfea'),
('7844832c-e98d-49e2-bafb-a5d83b419a2b', 'd35e7d4c-dd5b-465e-a7b1-2ecc425c115a', '3c95f85b-04a1-497c-acd4-422f69fd27fa'),
('e7c91d12-f26e-4169-8da8-32d668816377', '009fa935-ed0a-4c38-955c-19778cf9c379', '3c95f85b-04a1-497c-acd4-422f69fd27fa'),
('4c68a5a7-67ac-4e7e-8d0a-0a91eb841475', '009fa935-ed0a-4c38-955c-19778cf9c379', '90280bc9-2a6a-4b54-8fc6-e0ec53c7b7d8'),
('be5d2ae6-3e81-4a56-b86a-9faa8909e88a', '009fa935-ed0a-4c38-955c-19778cf9c379', '7da5b26d-fe45-4c82-ba71-63a64651dfea'),
('06afda90-218a-48d5-a95e-ae243fa63db2', '99911c3e-2d96-4668-a1c3-cfe76e0eed93', '90280bc9-2a6a-4b54-8fc6-e0ec53c7b7d8');

INSERT INTO REVIEWS (id, reviewer_id, auction_info_id, stars, review) VALUES
('bccf0841-63aa-4c6f-a27f-ff179b3b2238', 'd7262666-81eb-4e12-9a08-715b53aa641d', '009fa935-ed0a-4c38-955c-19778cf9c379', 5, 'The products were as described. Totally recommend Stefan!');

INSERT INTO BIDS (id, auction_info_id, buyer_id, datetime_bidded, price_bidded) VALUES
('3437bed8-3abf-4b83-a4b1-6de720ac6415','9c98bdb3-fc8f-4a6b-aed3-260883c541ed','d7262666-81eb-4e12-9a08-715b53aa641d',parsedatetime('10-12-2019 18:30:20', 'dd-MM-yyyy hh:mm:ss'),350),
('108bf895-9dd9-4719-aaa8-3e9d94f28846','9c98bdb3-fc8f-4a6b-aed3-260883c541ed','21e63e7f-ac6d-4ccc-a60b-6cb0fb249329',parsedatetime('13-12-2019 14:47:00', 'dd-MM-yyyy hh:mm:ss'),351),
('16270c95-f4fb-4fef-ba56-e5b8546ff91f','9c98bdb3-fc8f-4a6b-aed3-260883c541ed','d7262666-81eb-4e12-9a08-715b53aa641d',parsedatetime('13-12-2019 19:50:52', 'dd-MM-yyyy hh:mm:ss'),450),
('eb249348-4e59-4f77-b2d5-92ccac8bd8c2','009fa935-ed0a-4c38-955c-19778cf9c379','dd60294b-6f21-4449-8796-646ce0c5a98a',parsedatetime('01-10-2019 12:50:22', 'dd-MM-yyyy hh:mm:ss'),110),
('3b505da7-6014-434f-ad9a-86180b785ece','009fa935-ed0a-4c38-955c-19778cf9c379','d7262666-81eb-4e12-9a08-715b53aa641d',parsedatetime('15-10-2019 19:05:12', 'dd-MM-yyyy hh:mm:ss'),240),
('8be7cda2-7a94-4548-9eb8-6787d27a9e22','009fa935-ed0a-4c38-955c-19778cf9c379','dd60294b-6f21-4449-8796-646ce0c5a98a',parsedatetime('27-10-2019 12:30:09', 'dd-MM-yyyy hh:mm:ss'),250),
('ee7d3009-b150-4a73-912a-67fb630abf4b','009fa935-ed0a-4c38-955c-19778cf9c379','d7262666-81eb-4e12-9a08-715b53aa641d',parsedatetime('28-10-2019 18:10:12', 'dd-MM-yyyy hh:mm:ss'),300);