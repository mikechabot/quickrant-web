alter table rants drop column if exists emotion;
alter table rants add column emotion_id integer;
alter table rants add constraint emotions_fkey foreign key (emotion_id) references emotions (id) match full;