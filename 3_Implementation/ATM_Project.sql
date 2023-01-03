 CREATE DATABASE atm;

 USE atm;

CREATE TABLE transactions (
    transaction_id INT NOT NULL AUTO_INCREMENT,
    account_number INT NOT NULL,
    amount DECIMAL (10,2) NOT NULL,
    date_time DATETIME NOT NULL,
    PRIMARY KEY (transaction_id)
);

alter table transactions drop column date_time

create table customer(
name varchar(100),
account_number int not null primary key,
phone_number int(10),
address varchar(100),
email_id varchar(100),
balance decimal(10,2),
isActive varchar(2)
);

alter table customer add (password varchar(14),atm_pin int(4));
update customer set atm_pin=1234 where isActive='Y';
set sql_safe_updates=0;
select * from transactions;

create table customer_details(
account_number int,
atm_pin int(4),
pan varchar(10),
password varchar(14),
foreign key (account_number) references customer(account_number)
);

alter table transactions add (type varchar(20),recipient_account_number int(20));

alter table customer_details add isActive varchar(2);

INSERT INTO customer (name, account_number, phone_number, address, email_id, balance, isActive)
VALUES 
('Amit Kumar', 12345678, 987645321, 'Delhi, India', 'amit@gmail.com', 12000.00, 'Y'),
('Suresh Babu', 87654321, 987456321, 'Mumbai, India', 'suresh@gmail.com', 15000.00, 'Y'),
('Vivek Sharma', 56789123, 987654231, 'Kolkata, India', 'vivek@gmail.com', 18000.00, 'Y'),
('Rajesh Verma', 34567890, 987654321, 'Chennai, India', 'rajesh@gmail.com', 21000.00, 'Y'),
('Sachin Sharma', 12304567, 987456321, 'Hyderabad, India', 'sachin@gmail.com', 24000.00, 'Y'),
('Rohit Kumar', 89012345, 987654231, 'Gurugram, India', 'rohit@gmail.com', 27000.00, 'Y'),
('Kamal Verma', 78901234, 987654321, 'Pune, India', 'kamal@gmail.com', 30000.00, 'Y'),
('Ajay Gupta', 67890123, 987456321, 'Ahmedabad, India', 'ajay@gmail.com', 33000.00, 'Y'),
('Rahul Bhandari', 56789012, 987654231, 'Bengaluru, India', 'rahul@gmail.com', 36000.00, 'Y'),
('Kunal Jain', 45678901, 987654321, 'Chandigarh, India', 'kunal@gmail.com', 39000.00, 'Y');

-- Inserting customer details table
INSERT INTO customer_details (account_number, atm_pin, pan, password)
VALUES 
(12345678, 1234, 'ABCDE1234F', '12345678'),
(87654321, 2345, 'ABCDE2345F', '87654321'),
(56789123, 3456, 'ABCDE3456F', '56789123'),
(34567890, 4567, 'ABCDE4567F', '34567890'),
(12304567, 5678, 'ABCDE5678F', '12304567'),
(89012345, 6789, 'ABCDE6789F', '89012345'),
(78901234, 7890, 'ABCDE7890F', '78901234'),
(67890123, 8901, 'ABCDE8901F', '67890123'),
(56789012, 9012, 'ABCDE9012F', '56789012'),
(45678901, 0123, 'ABCDE0123F', '45678901');