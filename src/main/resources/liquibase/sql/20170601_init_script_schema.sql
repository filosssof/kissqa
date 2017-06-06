CREATE TABLE user_group (
	id int8 NOT NULL,
	"name" varchar(255) NULL,
	CONSTRAINT user_group_pkey PRIMARY KEY (id)
)
WITH (
OIDS=FALSE
) ;

SELECT 1;

CREATE TABLE "user" (
	id int8 NOT NULL DEFAULT nextval('user_id_seq'::regclass),
	first_name varchar(255) NULL,
	last_name varchar(255) NULL,
	"rank" int4 NULL,
	user_group_id int8 NULL,
	facebook_id varchar(255) NULL,
	email varchar(255) NULL,
	enabled bool NULL,
	date_of_birth date NULL,
	password bpchar(255) NULL,
	CONSTRAINT user_pkey PRIMARY KEY (id),
	CONSTRAINT user_un UNIQUE (email),
	CONSTRAINT fkd5uhmsqhax1l70pck9lmgphjr FOREIGN KEY (user_group_id) REFERENCES app.user_group(id)
)
WITH (
OIDS=FALSE
) ;
CREATE INDEX user_email_idx ON "user" (email DESC) ;

CREATE TABLE question (
  id int8 NOT NULL,
  content varchar(2047) NULL,
  created_date timestamp NULL,
  deleted bool NULL,
  edited_date timestamp NULL,
  "rank" int4 NULL,
  is_answered bool NOT NULL DEFAULT false,
  title varchar(255) NOT NULL,
  views int4 NOT NULL DEFAULT 0,
  created_by int8 NULL,
  edited_by int8 NULL,
  CONSTRAINT question_pkey PRIMARY KEY (id),
  CONSTRAINT fkmpr7f6l76w5oguah9nf2cujdb FOREIGN KEY (edited_by) REFERENCES "user"(id),
  CONSTRAINT fkr9df6rhov22x7ygo0a4mdjc8t FOREIGN KEY (created_by) REFERENCES "user"(id)
)
WITH (
OIDS=FALSE
) ;



CREATE TABLE answer (
	id int8 NOT NULL,
	content varchar(2047) NULL,
	created_date timestamp NULL,
	deleted bool NULL,
	edited_date timestamp NULL,
	"rank" int4 NULL,
	is_best bool NOT NULL DEFAULT false,
	created_by int8 NULL,
	edited_by int8 NULL,
	question int8 NULL,
	CONSTRAINT answer_pkey PRIMARY KEY (id),
	CONSTRAINT fk98t55tlk1ywnoun4kcecdtgcy FOREIGN KEY (created_by) REFERENCES "user"(id),
	CONSTRAINT fka1kyy3s4hnol8ip73ux1pv48b FOREIGN KEY (edited_by) REFERENCES "user"(id),
	CONSTRAINT fks4yfxjrfvek48fcsvg9ndcktu FOREIGN KEY (question) REFERENCES question(id)
)
WITH (
	OIDS=FALSE
) ;

CREATE TABLE answer_user_votes (
  id int8 NOT NULL,
  user_id int8 NOT NULL,
  answer_id int8 NOT NULL,
  vote bpchar(5) NOT NULL,
  CONSTRAINT answer_user_votes_pk PRIMARY KEY (id),
  CONSTRAINT answer_user_votes_un UNIQUE (user_id,answer_id),
  CONSTRAINT answer_user_votes_answer_fk FOREIGN KEY (answer_id) REFERENCES answer(id) ON DELETE CASCADE ON UPDATE RESTRICT,
  CONSTRAINT answer_user_votes_user_fk FOREIGN KEY (user_id) REFERENCES "user"(id) ON DELETE CASCADE ON UPDATE CASCADE
)
WITH (
OIDS=FALSE
) ;
CREATE UNIQUE INDEX answer_user_votes_user_id_idx ON answer_user_votes (user_id DESC,answer_id DESC) ;

CREATE TABLE question_user_votes (
  id int8 NOT NULL,
  user_id int8 NOT NULL,
  vote bpchar(5) NOT NULL,
  question_id int8 NOT NULL,
  CONSTRAINT user_votes_pk PRIMARY KEY (id),
  CONSTRAINT user_votes_un UNIQUE (user_id,question_id),
  CONSTRAINT user_votes_question_fk FOREIGN KEY (question_id) REFERENCES question(id),
  CONSTRAINT user_votes_user_fk FOREIGN KEY (user_id) REFERENCES "user"(id) ON DELETE CASCADE ON UPDATE RESTRICT
)
WITH (
OIDS=FALSE
) ;

