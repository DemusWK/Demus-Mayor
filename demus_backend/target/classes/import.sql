insert into country(name, currency, country_code, created_date, status, int_calling_code) values ('Nigeria', 'N', 'NGN', CURRENT_DATE, 'ACTIVE', '+234')

insert into permission(name, description, created_date, status) values ('ACCOUNTANT', 'Can read all financial related information', CURRENT_DATE, 'ACTIVE')
insert into permission(name, description, created_date, status) values ('SUBSCRIBER', 'Can read and update a subscriber account on the platform', CURRENT_DATE, 'ACTIVE')
insert into permission(name, description, created_date, status) values ('VENDOR', 'Can read product information', CURRENT_DATE, 'ACTIVE')
insert into permission(name, description, created_date, status) values ('PROCUREMENT', 'Can read and update product information', CURRENT_DATE, 'ACTIVE')
insert into permission(name, description, created_date, status) values ('SALES', 'Can read product performance information', CURRENT_DATE, 'ACTIVE')
insert into permission(name, description, created_date, status) values ('SUPPORT', 'Can read a subscriber information and trigger operations on subscriber account', CURRENT_DATE, 'ACTIVE')
insert into permission(name, description, created_date, status) values ('ADMINISTRATOR', 'Can do it all', CURRENT_DATE, 'ACTIVE')
insert into permission(name, description, created_date, status) values ('BACKEND', 'Can access the backend url', CURRENT_DATE, 'ACTIVE')
insert into permission(name, description, created_date, status) values ('PAYMENT_GATEWAY', 'Can lodge payments to the platform', CURRENT_DATE, 'ACTIVE')

insert into role(name, role_code, description, created_date, status) values ('Subscriber', 'SUBSCRIBER', 'Platform Subscriber', CURRENT_DATE, 'ACTIVE')
insert into role_permission(role_id, permission_id, created_date, status) values (1, 2, CURRENT_DATE, 'ACTIVE')

insert into role(name, role_code, description, created_date, status) values ('Administrator', 'ADMINISTRATOR', 'Platform Administrator', CURRENT_DATE, 'ACTIVE')
insert into role_permission(role_id, permission_id, created_date, status) values (2, 1, CURRENT_DATE, 'ACTIVE')
insert into role_permission(role_id, permission_id, created_date, status) values (2, 2, CURRENT_DATE, 'ACTIVE')
insert into role_permission(role_id, permission_id, created_date, status) values (2, 3, CURRENT_DATE, 'ACTIVE')
insert into role_permission(role_id, permission_id, created_date, status) values (2, 4, CURRENT_DATE, 'ACTIVE')
insert into role_permission(role_id, permission_id, created_date, status) values (2, 5, CURRENT_DATE, 'ACTIVE')
insert into role_permission(role_id, permission_id, created_date, status) values (2, 6, CURRENT_DATE, 'ACTIVE')
insert into role_permission(role_id, permission_id, created_date, status) values (2, 7, CURRENT_DATE, 'ACTIVE')
insert into role_permission(role_id, permission_id, created_date, status) values (2, 8, CURRENT_DATE, 'ACTIVE')

insert into role(name, role_code, description, created_date, status) values ('Payment Gateway', 'PAYMENT_GATEWAY', 'Can read or monitor transactions from a payment gateway', CURRENT_DATE, 'ACTIVE')
insert into role_permission(role_id, permission_id, created_date, status) values (3, 9, CURRENT_DATE, 'ACTIVE')

insert into role(name, role_code, description, created_date, status) values ('Vendor', 'VENDOR', 'Can read or monitor product performance', CURRENT_DATE, 'ACTIVE')
insert into role_permission(role_id, permission_id, created_date, status) values (4, 3, CURRENT_DATE, 'ACTIVE')

insert into subscriber(name, phone_number, password, pin, email, gender, date_of_birth, role_id, created_date, status) values ('Administrator Demus', '08000000001', '$2a$10$m5Fc7rBHZVVlPIcr/atyeezam/ea.eO9OFpbLSA5TJdhgzmTBZkby', '$2a$10$eaPc6u4uhdJ9xK65AZ937.dW3mcLId7LQwpklIEW8VQX3MQ2cbPje', 'admin@demusmayor.com', 'MALE', '31-10-0000','2', CURRENT_DATE, 'ACTIVE')

insert into subscriber_role(role_id, subscriber_id, created_date, status) values (2,1, CURRENT_DATE, 'ACTIVE')
insert into subscriber_role(role_id, subscriber_id, created_date, status) values (2,2, CURRENT_DATE, 'ACTIVE')
insert into subscriber_role(role_id, subscriber_id, created_date, status) values (2,3, CURRENT_DATE, 'ACTIVE')
insert into subscriber_role(role_id, subscriber_id, created_date, status) values (2,4, CURRENT_DATE, 'ACTIVE')
insert into subscriber_role(role_id, subscriber_id, created_date, status) values (2,5, CURRENT_DATE, 'ACTIVE')
insert into subscriber_role(role_id, subscriber_id, created_date, status) values (2,6, CURRENT_DATE, 'ACTIVE')
insert into subscriber_role(role_id, subscriber_id, created_date, status) values (2,7, CURRENT_DATE, 'ACTIVE')
insert into subscriber_role(role_id, subscriber_id, created_date, status) values (2,8, CURRENT_DATE, 'ACTIVE')
insert into subscriber_role(role_id, subscriber_id, created_date, status) values (4,9, CURRENT_DATE, 'ACTIVE')

insert into wallet(wallet_id, subscriber_id, wallet_balance, mobile_network_operator, created_date, status, min_balance_trigger) values ('08000000001', 1, 0.00, 'GLO', CURRENT_DATE, 'ACTIVE', 100.00)
insert into wallet(wallet_id, subscriber_id, wallet_balance, mobile_network_operator, created_date, status, min_balance_trigger) values ('08000000002', 2, 0.00, 'GLO', CURRENT_DATE, 'ACTIVE', 100.00)
insert into wallet(wallet_id, subscriber_id, wallet_balance, mobile_network_operator, created_date, status, min_balance_trigger) values ('08000000003', 3, 0.00, 'GLO', CURRENT_DATE, 'ACTIVE', 100.00)
insert into wallet(wallet_id, subscriber_id, wallet_balance, mobile_network_operator, created_date, status, min_balance_trigger) values ('08000000004', 4, 0.00, 'GLO', CURRENT_DATE, 'ACTIVE', 100.00)
insert into wallet(wallet_id, subscriber_id, wallet_balance, mobile_network_operator, created_date, status, min_balance_trigger) values ('08000000005', 5, 0.00, 'GLO', CURRENT_DATE, 'ACTIVE', 100.00)
insert into wallet(wallet_id, subscriber_id, wallet_balance, mobile_network_operator, created_date, status, min_balance_trigger) values ('08000000006', 6, 0.00, 'GLO', CURRENT_DATE, 'ACTIVE', 100.00)
insert into wallet(wallet_id, subscriber_id, wallet_balance, mobile_network_operator, created_date, status, min_balance_trigger) values ('08000000007', 7, 0.00, 'GLO', CURRENT_DATE, 'ACTIVE', 100.00)
insert into wallet(wallet_id, subscriber_id, wallet_balance, mobile_network_operator, created_date, status, min_balance_trigger) values ('08000000008', 8, 0.00, 'GLO', CURRENT_DATE, 'ACTIVE', 100.00)
insert into wallet(wallet_id, subscriber_id, wallet_balance, mobile_network_operator, created_date, status, min_balance_trigger) values ('switch4eclever', 9, 0.00, 'GLO', CURRENT_DATE, 'ACTIVE', 100.00)

insert into bank_operator(name, code, country_id, created_date, status) values ('Access Bank', '044', 1, CURRENT_DATE, 'ACTIVE')
insert into bank_operator(name, code, country_id, created_date, status) values ('Diamond Bank', '063', 1, CURRENT_DATE, 'ACTIVE')
insert into bank_operator(name, code, country_id, created_date, status) values ('Ecobank Bank', '050', 1, CURRENT_DATE, 'ACTIVE')
insert into bank_operator(name, code, country_id, created_date, status) values ('FCMB', '214', 1, CURRENT_DATE, 'ACTIVE')
insert into bank_operator(name, code, country_id, created_date, status) values ('Fidelity Bank', '070', 1, CURRENT_DATE, 'ACTIVE')
insert into bank_operator(name, code, country_id, created_date, status) values ('First Bank', '011', 1, CURRENT_DATE, 'ACTIVE')
insert into bank_operator(name, code, country_id, created_date, status) values ('GT Bank', '058', 1, CURRENT_DATE, 'ACTIVE')
insert into bank_operator(name, code, country_id, created_date, status) values ('Heritage Bank', '030', 1, CURRENT_DATE, 'ACTIVE')
insert into bank_operator(name, code, country_id, created_date, status) values ('Jaiz Bank', '301', 1, CURRENT_DATE, 'ACTIVE')
insert into bank_operator(name, code, country_id, created_date, status) values ('Keystone Bank', '082', 1, CURRENT_DATE, 'ACTIVE')
insert into bank_operator(name, code, country_id, created_date, status) values ('Skye Bank', '076', 1, CURRENT_DATE, 'ACTIVE')
insert into bank_operator(name, code, country_id, created_date, status) values ('Stanbic IBTC Bank', '039', 1, CURRENT_DATE, 'ACTIVE')
insert into bank_operator(name, code, country_id, created_date, status) values ('Sterlin Bank', '232', 1, CURRENT_DATE, 'ACTIVE')
insert into bank_operator(name, code, country_id, created_date, status) values ('UBA', '033', 1, CURRENT_DATE, 'ACTIVE')
insert into bank_operator(name, code, country_id, created_date, status) values ('Union Bank', '032', 1, CURRENT_DATE, 'ACTIVE')
insert into bank_operator(name, code, country_id, created_date, status) values ('Unity Bank', '215', 1, CURRENT_DATE, 'ACTIVE')
insert into bank_operator(name, code, country_id, created_date, status) values ('Wema Bank', '035', 1, CURRENT_DATE, 'ACTIVE')
insert into bank_operator(name, code, country_id, created_date, status) values ('Zenith Bank', '057', 1, CURRENT_DATE, 'ACTIVE')

insert into product_category (name, code, order_index, created_date, status) values ('Airtime', 'AIRTIME', 1, CURRENT_DATE, 'ACTIVE')
insert into product_category (name, code, order_index, created_date, status) values ('Data', 'DATA', 2, CURRENT_DATE, 'ACTIVE')

INSERT INTO `product` (`id`,`created_date`,`last_modified_date`,`status`,`commission`,`description`,`icon_url`,`min_purchase_quantity`,`name`,`price`,`code`,`stock_count`,`created_by_id`,`last_modified_by_id`,`category_id`,`country_id`,`vendor_id`,`cost_price`) VALUES (1,'2016-10-19 00:00:00','2019-09-20 11:54:50','ACTIVE',0.005,'Mtn Airtime',NULL,50,'MTN AIRTIME',1,'13',1989752331,NULL,1013,1,1,1,0);
INSERT INTO `product` (`id`,`created_date`,`last_modified_date`,`status`,`commission`,`description`,`icon_url`,`min_purchase_quantity`,`name`,`price`,`code`,`stock_count`,`created_by_id`,`last_modified_by_id`,`category_id`,`country_id`,`vendor_id`,`cost_price`) VALUES (2,'2016-10-19 00:00:00','2019-09-19 19:58:40','ACTIVE',0.02,'Airtel Airtime',NULL,50,'AIRTEL AIRTIME',1,'1',1999005201,NULL,1013,1,1,1,0);
INSERT INTO `product` (`id`,`created_date`,`last_modified_date`,`status`,`commission`,`description`,`icon_url`,`min_purchase_quantity`,`name`,`price`,`code`,`stock_count`,`created_by_id`,`last_modified_by_id`,`category_id`,`country_id`,`vendor_id`,`cost_price`) VALUES (3,'2016-10-19 00:00:00','2019-09-19 11:58:44','ACTIVE',0.02,'Etisalat Airtime',NULL,50,'ETISALAT AIRTIME',1,'2',1998730207,NULL,1013,1,1,1,0);
INSERT INTO `product` (`id`,`created_date`,`last_modified_date`,`status`,`commission`,`description`,`icon_url`,`min_purchase_quantity`,`name`,`price`,`code`,`stock_count`,`created_by_id`,`last_modified_by_id`,`category_id`,`country_id`,`vendor_id`,`cost_price`) VALUES (4,'2016-10-19 00:00:00',NULL,'ACTIVE',0.02,'Visafone Airtime',NULL,100,'VISAFONE AIRTIME',1,'3',2000000000,NULL,NULL,1,1,1,0);
INSERT INTO `product` (`id`,`created_date`,`last_modified_date`,`status`,`commission`,`description`,`icon_url`,`min_purchase_quantity`,`name`,`price`,`code`,`stock_count`,`created_by_id`,`last_modified_by_id`,`category_id`,`country_id`,`vendor_id`,`cost_price`) VALUES (5,'2016-10-19 00:00:00','2019-09-20 12:07:20','ACTIVE',0.02,'Glo Airtime',NULL,45,'GLO AIRTIME',1,'6',1999450580,NULL,1013,1,1,1,0);
INSERT INTO `product` (`id`,`created_date`,`last_modified_date`,`status`,`commission`,`description`,`icon_url`,`min_purchase_quantity`,`name`,`price`,`code`,`stock_count`,`created_by_id`,`last_modified_by_id`,`category_id`,`country_id`,`vendor_id`,`cost_price`) VALUES (26,'2017-04-15 00:00:00',NULL,'ACTIVE',0,'Mtn 30mb',NULL,1,'MTN 30MB DATA',100,'10',2000000000,NULL,NULL,2,1,1,0);
INSERT INTO `product` (`id`,`created_date`,`last_modified_date`,`status`,`commission`,`description`,`icon_url`,`min_purchase_quantity`,`name`,`price`,`code`,`stock_count`,`created_by_id`,`last_modified_by_id`,`category_id`,`country_id`,`vendor_id`,`cost_price`) VALUES (27,'2017-04-15 00:00:00',NULL,'ACTIVE',0,'Mtn 150mb',NULL,1,'MTN 150MB DATA',200,'12',2000000000,NULL,NULL,2,1,1,0);
INSERT INTO `product` (`id`,`created_date`,`last_modified_date`,`status`,`commission`,`description`,`icon_url`,`min_purchase_quantity`,`name`,`price`,`code`,`stock_count`,`created_by_id`,`last_modified_by_id`,`category_id`,`country_id`,`vendor_id`,`cost_price`) VALUES (28,'2017-04-15 00:00:00',NULL,'ACTIVE',0,'Mtn 1.5gb',NULL,1,'MTN 1.5GB DATA',1000,'16',2000000000,NULL,NULL,2,1,1,0);
INSERT INTO `product` (`id`,`created_date`,`last_modified_date`,`status`,`commission`,`description`,`icon_url`,`min_purchase_quantity`,`name`,`price`,`code`,`stock_count`,`created_by_id`,`last_modified_by_id`,`category_id`,`country_id`,`vendor_id`,`cost_price`) VALUES (29,'2017-04-15 00:00:00',NULL,'ACTIVE',0,'Mtn 3.5gb',NULL,1,'MTN 3.5GB DATA',2000,'17',2000000000,NULL,NULL,2,1,1,0);
INSERT INTO `product` (`id`,`created_date`,`last_modified_date`,`status`,`commission`,`description`,`icon_url`,`min_purchase_quantity`,`name`,`price`,`code`,`stock_count`,`created_by_id`,`last_modified_by_id`,`category_id`,`country_id`,`vendor_id`,`cost_price`) VALUES (31,'2017-04-15 00:00:00',NULL,'ACTIVE',0,'Etisalat 200mb',NULL,1,'ETISALAT 200MB DATA',200,'308',2000000000,NULL,NULL,2,1,1,0);
INSERT INTO `product` (`id`,`created_date`,`last_modified_date`,`status`,`commission`,`description`,`icon_url`,`min_purchase_quantity`,`name`,`price`,`code`,`stock_count`,`created_by_id`,`last_modified_by_id`,`category_id`,`country_id`,`vendor_id`,`cost_price`) VALUES (32,'2017-04-15 00:00:00',NULL,'ACTIVE',0,'Etisalat 500mb',NULL,1,'ETISALAT 500MB DATA',500,'109',2000000000,NULL,NULL,2,1,1,0);
INSERT INTO `product` (`id`,`created_date`,`last_modified_date`,`status`,`commission`,`description`,`icon_url`,`min_purchase_quantity`,`name`,`price`,`code`,`stock_count`,`created_by_id`,`last_modified_by_id`,`category_id`,`country_id`,`vendor_id`,`cost_price`) VALUES (33,'2017-04-15 00:00:00','2017-05-19 21:04:53','ACTIVE',0,'Etisalat 1.5gb',NULL,1,'ETISALAT 1.5GB DATA',1200,'381',1999999998,NULL,6,2,1,1,0);
INSERT INTO `product` (`id`,`created_date`,`last_modified_date`,`status`,`commission`,`description`,`icon_url`,`min_purchase_quantity`,`name`,`price`,`code`,`stock_count`,`created_by_id`,`last_modified_by_id`,`category_id`,`country_id`,`vendor_id`,`cost_price`) VALUES (34,'2017-04-15 00:00:00',NULL,'ACTIVE',0,'Etisalat 2.5gb',NULL,1,'ETISALAT 2.5GB DATA',2000,'68',2000000000,NULL,NULL,2,1,1,0);
INSERT INTO `product` (`id`,`created_date`,`last_modified_date`,`status`,`commission`,`description`,`icon_url`,`min_purchase_quantity`,`name`,`price`,`code`,`stock_count`,`created_by_id`,`last_modified_by_id`,`category_id`,`country_id`,`vendor_id`,`cost_price`) VALUES (35,'2017-04-15 00:00:00',NULL,'ACTIVE',0,'Etisalat 5gb',NULL,1,'ETISALAT 5GB DATA',3500,'307',2000000000,NULL,NULL,2,1,1,0);
INSERT INTO `product` (`id`,`created_date`,`last_modified_date`,`status`,`commission`,`description`,`icon_url`,`min_purchase_quantity`,`name`,`price`,`code`,`stock_count`,`created_by_id`,`last_modified_by_id`,`category_id`,`country_id`,`vendor_id`,`cost_price`) VALUES (38,'2017-04-15 00:00:00',NULL,'ACTIVE',0,'Airtel 30mb',NULL,1,'AIRTEL 30MB DATA',99,'4',2000000000,NULL,NULL,2,1,1,0);
INSERT INTO `product` (`id`,`created_date`,`last_modified_date`,`status`,`commission`,`description`,`icon_url`,`min_purchase_quantity`,`name`,`price`,`code`,`stock_count`,`created_by_id`,`last_modified_by_id`,`category_id`,`country_id`,`vendor_id`,`cost_price`) VALUES (39,'2017-04-15 00:00:00',NULL,'ACTIVE',0,'Airtel 500mb',NULL,1,'AIRTEL 500MB DATA',499,'5',2000000000,NULL,NULL,2,1,1,0);
INSERT INTO `product` (`id`,`created_date`,`last_modified_date`,`status`,`commission`,`description`,`icon_url`,`min_purchase_quantity`,`name`,`price`,`code`,`stock_count`,`created_by_id`,`last_modified_by_id`,`category_id`,`country_id`,`vendor_id`,`cost_price`) VALUES (46,'2017-04-15 00:00:00',NULL,'ACTIVE',0,'Airtel 1.5gb',NULL,1,'AIRTEL 1.5GB DATA',999,'6',2000000000,NULL,NULL,2,1,1,0);
INSERT INTO `product` (`id`,`created_date`,`last_modified_date`,`status`,`commission`,`description`,`icon_url`,`min_purchase_quantity`,`name`,`price`,`code`,`stock_count`,`created_by_id`,`last_modified_by_id`,`category_id`,`country_id`,`vendor_id`,`cost_price`) VALUES (47,'2017-04-15 00:00:00',NULL,'ACTIVE',0,'Airtel 3.5gb',NULL,1,'AIRTEL 3.5GB DATA',1999,'7',2000000000,NULL,NULL,2,1,1,0);
INSERT INTO `product` (`id`,`created_date`,`last_modified_date`,`status`,`commission`,`description`,`icon_url`,`min_purchase_quantity`,`name`,`price`,`code`,`stock_count`,`created_by_id`,`last_modified_by_id`,`category_id`,`country_id`,`vendor_id`,`cost_price`) VALUES (51,'2017-05-16 00:00:00',NULL,'ACTIVE',0,'Mtn 750mb',NULL,1,'MTN 750MB DATA',500,'15',2000000000,NULL,NULL,2,1,1,0);
INSERT INTO `product` (`id`,`created_date`,`last_modified_date`,`status`,`commission`,`description`,`icon_url`,`min_purchase_quantity`,`name`,`price`,`code`,`stock_count`,`created_by_id`,`last_modified_by_id`,`category_id`,`country_id`,`vendor_id`,`cost_price`) VALUES (52,'2017-05-16 00:00:00',NULL,'ACTIVE',0,'Etisalat 1gb',NULL,1,'ETISALAT 1GB DATA',1000,'67',2000000000,NULL,NULL,2,1,1,0);
INSERT INTO `product` (`id`,`created_date`,`last_modified_date`,`status`,`commission`,`description`,`icon_url`,`min_purchase_quantity`,`name`,`price`,`code`,`stock_count`,`created_by_id`,`last_modified_by_id`,`category_id`,`country_id`,`vendor_id`,`cost_price`) VALUES (53,'2017-05-16 00:00:00',NULL,'ACTIVE',0,'Airtel 6.5gb',NULL,1,'AIRTEL 6.5GB DATA',3499,'301',2000000000,NULL,NULL,2,1,1,0);
INSERT INTO `product` (`id`,`created_date`,`last_modified_date`,`status`,`commission`,`description`,`icon_url`,`min_purchase_quantity`,`name`,`price`,`code`,`stock_count`,`created_by_id`,`last_modified_by_id`,`category_id`,`country_id`,`vendor_id`,`cost_price`) VALUES (54,'2017-05-16 00:00:00',NULL,'ACTIVE',0,'Generic Data',NULL,1,'DATA',1,'999',2000000000,NULL,NULL,2,1,1,0);
INSERT INTO `product` (`id`,`created_date`,`last_modified_date`,`status`,`commission`,`description`,`icon_url`,`min_purchase_quantity`,`name`,`price`,`code`,`stock_count`,`created_by_id`,`last_modified_by_id`,`category_id`,`country_id`,`vendor_id`,`cost_price`) VALUES (55,'2017-05-16 00:00:00','2018-05-24 18:38:18','ACTIVE',0,'Electricity',NULL,1,'ELECTRICITY',1,'9990',1999996900,NULL,6,3,1,1,0);

