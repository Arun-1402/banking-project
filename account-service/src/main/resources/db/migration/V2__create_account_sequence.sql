create SEQUENCE account_seq start 1000000000 increment by 1;

create table account_audit (
	id BIGSERIAL primary key,
	account_number varchar(30) unique not null,
	user_id BIGINT not null,
	action varchar(100) not null,
	ip varchar(100),
	device_id varchar(255),
	created_at TIMESTAMP not null
);

create index idx_account_audit_account_number on account_audit(account_number);
create index idx_account_audit_user_id on account_audit(user_id);