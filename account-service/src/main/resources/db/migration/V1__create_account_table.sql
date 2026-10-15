CREATE TABLE accounts (
	id BIGSERIAL primary key,
	account_number varchar(20) unique not null,
	customer_id BIGINT not null,
	account_type varchar(20),
	status varchar(20),
	balance decimal(15,2),
	created_at timestamp,
	updated_at timestamp
);