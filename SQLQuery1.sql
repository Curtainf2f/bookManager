create database bookManager
go
use bookManager

create table readerType(
	readerTypeId int not null primary key,
	readerTypeName varchar(20)
)

create table reader(
	readerId int not null primary key,
	readerTypeId int not null foreign key references readerType(readerTypeId),
	readerName varchar(10) not null,
	readerAge int,
	readerSex varchar(4),
	readerPhone varchar(12),
	readerDept varchar(20),
	regDate datetime,
)

create table bookType(
	bookTypeId int not null primary key,
	bookTypeName varchar(20)
)

create table book(
	ISBN int not null primary key,
	bookTypeId int foreign key references bookType(bookTypeId),
	bookName varchar(30) not null,
	bookauthor varchar(30),
	bookPublish varchar(30),
	bookPublishDate datetime,
	bookPublishTimes int,
	bookPrice money
)

create table users(
	userName varchar(16) not null primary key,
	userPassword varchar(16) constraint CK_passwordLength check (len(userPassword) >= 6) not null,
	readerId int foreign key references reader(readerId),
)

create table borrowBook(
	readerId varchar(10),
	ISBN varchar(10),
	borrowDate datetime not null,
	returnDate datetime,
)

create table adminUsers(
	adminUserName varchar(16) not null primary key,
	adminUserPassword varchar(16) constraint CK_adminPasswordLength check (len(adminUserPassword) >= 6) not null,
)

create table freeReader(
	freeReaderId int
)

create table freeReaderType(
	freeReaderType int
)

create table freeBook(
	freeISBN int
)

create table freeBookType(
	freeBookTypeId int
)

delete from adminUsers
delete from book
delete from bookType
delete from borrowBook
delete from freeBook
delete from freeBookType
delete from freeReader
delete from freeReaderType
delete from reader
delete from readerType
delete from users

insert into adminUsers values('admin', '123456')
insert into users values('1', '123456', null)

select * from adminUsers
select * from book
select * from bookType
select * from borrowBook
select * from freeBook
select * from freeBookType
select * from freeReader
select * from freeReaderType
select * from reader
select * from readerType
select * from users