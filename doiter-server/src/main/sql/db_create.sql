drop database if exists doiter;
create database doiter character set utf8 collate = utf8_general_ci;

create user 'doiter'@'%' identified by 'doiter';
grant all privileges on doiter.* to 'doiter'@'%';
