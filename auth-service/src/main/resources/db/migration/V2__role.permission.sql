create table permissions(
	id BIGSERIAL primary key,
	name varchar(100) unique not null
);
create table role_permissions(
	role_id BIGINT not null,
	permission_id BIGINT not null,
	primary key(role_id, permission_id),
	constraint fk_role_permission_role foreign key(role_id) references roles(id),
	constraint fk_role_permission_permission foreign key(permission_id) references permissions(id)
);
insert into roles(name) values
('ROLE_ADMIN'),
('ROLE_BANKER'),
('ROLE_CUSTOMER');

insert into permissions(name) values
('CREATE-ACCOUNT'),
('VIEW_ACCOUNT'),
('TRANSFER_MONEY'),
('APPROVE_TRANSACTION');