drop table if exists rants;
drop table if exists cookies;

create table rants (id serial unique, created timestamp (60), emotion varchar(16), question varchar(500), rant varchar(500), ranter varchar(100), location varchar(150));
create table cookies (id serial unique, created timestamp (60), issued bigint, cookie varchar(50));