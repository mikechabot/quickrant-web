drop table if exists rants;
drop table if exists ranter;

create table rants (id serial unique, created timestamp (60), emotion varchar(16), question varchar(500), rant varchar(500), ranter varchar(100), location varchar(150), ranter_id integer);
create table ranter (id serial unique, created timestamp (60), cookievalue varchar(50), cookieissued bigint, cookieactive boolean, complete boolean, ipaddress varchar(50), useragent varchar(200), lastrant bigint, rantattempts smallint, screenheight smallint, screenwidth smallint, screencolor smallint, key varchar(500));  