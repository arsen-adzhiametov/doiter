drop database if exists doiter_test;
create database doiter_test character set utf8 collate = utf8_general_ci;

create user 'doiter_tester'@'%' identified by 'doiter';
grant all privileges on doiter_test.* to 'doiter_tester'@'%';
