drop table if exists comments;


create table comments (id serial unique, created timestamp (60), emotion varchar(16), comment varchar(500), commenter varchar(100), location varchar(150));