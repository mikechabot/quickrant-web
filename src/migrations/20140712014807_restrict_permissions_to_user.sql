grant usage, select on all sequences in schema public to admins;
grant select, update, insert on table visitors to group admins;
grant select, update, insert on table rants to group admins;
alter group admins add user quickrant;