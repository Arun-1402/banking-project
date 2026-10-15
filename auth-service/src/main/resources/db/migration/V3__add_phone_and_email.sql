alter table users add column phone_number varchar(20);
alter table users add column email varchar(50);
alter table users add column account_non_locked boolean default true, add column failed_attempts INT default 0,
add column lock_time timestamp;