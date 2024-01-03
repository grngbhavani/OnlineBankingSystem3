create database OnlineBankingSystem;
drop database OnlineBankingSystem;
use OnlineBankingSystem;
create table InfoAccountant(
ename varchar(20),
email varchar(25),
epass varchar(20)
);
drop table InfoCustomer;
insert into InfoAccountant values('renu','renu@gmail.com','renu@');
insert into InfoAccountant values('admin','admin','admin');


select * from InfoCustomer;
create table InfoCustomer(
cname varchar(20),
cmail varchar(25),
cpass varchar(20),
cmob varchar(10),
cadd varchar(20),
cid int primary key auto_increment
);

select * from InfoCustomer;

create table Account(
cACno bigint primary key auto_increment,
cbal int,
cid int,
foreign key(cid) references InfoCustomer(cid) on delete cascade
);
insert into Account values(12345,1000,1);
insert into Account values(54321,2000,2);
insert into Account values(56789,3000,3);
insert into Account values(98765,4000,4);
select * from Account;

create table Transaction(
cACno bigint not null,
deposit int not null,
withdraw int not null,
foreign key(cACno) 
references Account(cACno) on delete cascade
);

select * from Transaction;
