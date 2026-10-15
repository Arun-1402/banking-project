create table transactions(
	id BIGSERIAL primary key,
	txn_id varchar(50) unique not null,
	account_number varchar(30) not null,
	type varchar(20),
	amount DECIMAL(15,2),
	status varchar(20),
	reference_id varchar(100),
	created_at timestamp,
	updated_at timestamp
);

create table transaction_audit(
	id BIGSERIAL primary key,
	txn_id varchar(50),
	user_id BIGINT,
	customer_id BIGINT,
	action varchar(50),
	ip varchar(100),	
	device_id varchar(255),
	created_at timestamp
);

create index idx_txn_account ON transactions(account_number);