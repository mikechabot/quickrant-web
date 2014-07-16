alter table rants drop column if exists question;
alter table rants add column question_id integer;