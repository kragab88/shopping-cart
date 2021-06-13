create EXTENSION IF NOT EXISTS "uuid-ossp";
-- public.cart definition

-- Drop table

-- DROP TABLE public.cart;

CREATE TABLE public.cart (
	id uuid NOT NULL DEFAULT uuid_generate_v4(),
	country_code varchar(2) NOT NULL ,
	created TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
	currency varchar(3) NOT NULL,
	updated TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
	username varchar(255) NOT NULL,
	CONSTRAINT cart_pkey PRIMARY KEY (id),
	CONSTRAINT uk_4lga3gtrv9yq5c1t6dkdens5y UNIQUE (username),
	CONSTRAINT uk_4n190ax1ijqrx94bi2nmimyar UNIQUE (currency),
	CONSTRAINT uk_57shu3e264mj9wp8vbok8yi9i UNIQUE (country_code)
);


-- public.cart_user definition

-- Drop table

-- DROP TABLE public.cart_user;

CREATE TABLE public.cart_user (
	user_id int4 NOT NULL,
	active bool NULL,
	"password" varchar(255) NULL,
	username varchar(255) NULL,
	CONSTRAINT cart_user_pkey PRIMARY KEY (user_id)
);


-- public.product definition

-- Drop table

-- DROP TABLE public.product;

CREATE TABLE public.product (
	id uuid NOT NULL DEFAULT uuid_generate_v4(),
	category varchar(255) NOT NULL,
	created TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
	description varchar(255) NOT NULL,
	price float8 NOT NULL,
	updated TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
	CONSTRAINT product_pkey PRIMARY KEY (id),
	CONSTRAINT uk_q2n3melweyrl5d4rqkg7pq6ra UNIQUE (description)
);


-- public."role" definition

-- Drop table

-- DROP TABLE public."role";

CREATE TABLE public."role" (
	role_id int4 NOT NULL,
	"role" varchar(255) NULL,
	CONSTRAINT role_pkey PRIMARY KEY (role_id)
);


-- public.cart_product definition

-- Drop table

-- DROP TABLE public.cart_product;

CREATE TABLE public.cart_product (
	cart_id uuid NOT NULL,
	product_id uuid NOT NULL,
	CONSTRAINT fk2kdlr8hs2bwl14u8oop49vrxi FOREIGN KEY (product_id) REFERENCES product(id),
	CONSTRAINT fklv5x4iresnv4xspvomrwd8ej9 FOREIGN KEY (cart_id) REFERENCES cart(id)
);


-- public.user_role definition

-- Drop table

-- DROP TABLE public.user_role;

CREATE TABLE public.user_role (
	user_id int4 NOT NULL,
	role_id int4 NOT NULL,
	CONSTRAINT user_role_pkey PRIMARY KEY (user_id, role_id),
	CONSTRAINT fk2o58yp5xpsjv5xlb8lh9ocrqy FOREIGN KEY (user_id) REFERENCES cart_user(user_id),
	CONSTRAINT fka68196081fvovjhkek5m97n3y FOREIGN KEY (role_id) REFERENCES role(role_id)
);