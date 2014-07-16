alter table questions add column emotion_id integer;
alter table questions add constraint emotions_fkey foreign key (emotion_id) references emotions (id) match full;