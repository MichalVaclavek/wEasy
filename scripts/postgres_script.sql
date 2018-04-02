
-- ======== USER ================================
	
-- Table: public.t_user

-- DROP TABLE public.t_user CASCADE;

CREATE TABLE public.t_user
(
    id serial NOT NULL,
    username character varying COLLATE pg_catalog."default" NOT NULL,
    email character varying COLLATE pg_catalog."default" NOT NULL,
    password character varying COLLATE pg_catalog."default" NOT NULL,
    first_name character varying(80) COLLATE pg_catalog."default",
    last_name character varying(120) COLLATE pg_catalog."default",
    CONSTRAINT t_user_pkey PRIMARY KEY (id),
    CONSTRAINT username_unique UNIQUE (username)
)
WITH (
    OIDS = FALSE
)
TABLESPACE pg_default;

-- ALTER TABLE public.t_user
--   OWNER to postgres;
	
-- ======== USER PROFILE ================================
	
-- Table: public.t_user_profile

-- DROP TABLE public.t_user_profile CASCADE;

CREATE TABLE public.t_user_profile
(
    id bigint NOT NULL,
    type character varying(15) COLLATE pg_catalog."default" NOT NULL,
    CONSTRAINT t_user_profile_pkey PRIMARY KEY (id),
    CONSTRAINT t_user_profile_type_key UNIQUE (type)
)
WITH (
    OIDS = FALSE
)
TABLESPACE pg_default;

-- ALTER TABLE public.t_user_profile
--    OWNER to postgres;
	
-- ======== USER TO USER PROFILE ================================
	
-- Table: public.user_to_user_profile

-- DROP TABLE public.user_to_user_profile;

CREATE TABLE public.user_to_user_profile
(
    user_id bigint NOT NULL,
    user_profile_id bigint NOT NULL,
    CONSTRAINT fk_app_user FOREIGN KEY (user_id)
        REFERENCES public.t_user (id) MATCH SIMPLE
        ON UPDATE CASCADE
        ON DELETE CASCADE,
    CONSTRAINT fk_profile_id FOREIGN KEY (user_profile_id)
        REFERENCES public.t_user_profile (id) MATCH SIMPLE
        ON UPDATE CASCADE
        ON DELETE CASCADE
)
WITH (
    OIDS = FALSE
)
TABLESPACE pg_default;

-- ALTER TABLE public.user_to_user_profile
--     OWNER to postgres;
	
	
-- ======== CATEGORY ================================	
	
-- Table: public.t_category

-- DROP TABLE public.t_category CASCADE;

CREATE TABLE public.t_category
(
    id serial NOT NULL,
    name character varying COLLATE pg_catalog."default" NOT NULL,
    CONSTRAINT t_category_pkey PRIMARY KEY (id),
    CONSTRAINT name UNIQUE (name)
)
WITH (
    OIDS = FALSE
)
TABLESPACE pg_default;

-- ALTER TABLE public.t_category
--     OWNER to postgres;	

-- ======== ARTICLE ================================	

-- Table: public.t_article

-- DROP TABLE public.t_article CASCADE;

CREATE TABLE public.t_article
(
    id serial NOT NULL,
    saved timestamp with time zone NOT NULL,
    category_id integer,
    directory_id integer,
    header character varying(1000) COLLATE pg_catalog."default" NOT NULL,
    text text COLLATE pg_catalog."default" NOT NULL,
    CONSTRAINT t_article_pkey PRIMARY KEY (id),
    CONSTRAINT t_article_category_id_fkey FOREIGN KEY (category_id)
        REFERENCES public.t_category (id) MATCH SIMPLE
        ON UPDATE CASCADE
        ON DELETE NO ACTION
)
WITH (
    OIDS = FALSE
)
TABLESPACE pg_default;

-- ALTER TABLE public.t_article
--     OWNER to postgres;
	
	
-- ======== COMMENT ================================	
	
-- Table: public.t_comment

-- DROP TABLE public.t_comment;

CREATE TABLE public.t_comment
(
    id serial NOT NULL,
    text text COLLATE pg_catalog."default" NOT NULL,
    created timestamp with time zone NOT NULL,
    article_id integer NOT NULL,
    user_id integer NOT NULL,
    CONSTRAINT t_comment_pkey PRIMARY KEY (id),
    CONSTRAINT t_comment_article_id_fkey FOREIGN KEY (article_id)
        REFERENCES public.t_article (id) MATCH SIMPLE
        ON UPDATE CASCADE
        ON DELETE NO ACTION,
    CONSTRAINT t_comment_user_id_fkey FOREIGN KEY (user_id)
        REFERENCES public.t_user (id) MATCH SIMPLE
        ON UPDATE CASCADE
        ON DELETE NO ACTION
)
WITH (
    OIDS = FALSE
)
TABLESPACE pg_default;

-- ALTER TABLE public.t_comment
--     OWNER to postgres;
	
	
-- ======== CONTACT MESSAGE ==========================	
	
-- Table: public.t_contact_message

-- DROP TABLE public.t_contact_message;

CREATE TABLE public.t_contact_message
(
	id serial NOT NULL,
    created_at timestamp with time zone NOT NULL,
    author_name character varying(60) COLLATE pg_catalog."default" NOT NULL,
    email character varying(60) COLLATE pg_catalog."default" NOT NULL,
    message text COLLATE pg_catalog."default" NOT NULL,
    CONSTRAINT t_contact_message_pkey PRIMARY KEY (id)
)
WITH (
    OIDS = FALSE
)
TABLESPACE pg_default;

-- ALTER TABLE public.t_contact_message
--     OWNER to postgres;
	
	
-- ======== DIRECTORY ================================
	
-- Table: public.t_directory

-- DROP TABLE public.t_directory CASCADE;

CREATE TABLE public.t_directory
(
    id serial NOT NULL,
    category_id integer,
    name character varying(1000) COLLATE pg_catalog."default" NOT NULL,
    CONSTRAINT t_directory_pkey PRIMARY KEY (id),
    CONSTRAINT category_directory_unique UNIQUE (category_id, name),
    CONSTRAINT t_directory_category_id_fkey FOREIGN KEY (category_id)
        REFERENCES public.t_category (id) MATCH SIMPLE
        ON UPDATE CASCADE
        ON DELETE CASCADE
)
WITH (
    OIDS = FALSE
)
TABLESPACE pg_default;

-- ALTER TABLE public.t_directory
--     OWNER to postgres;
	

-- ======== IMAGE ================================
	
-- Table: public.t_image

-- DROP TABLE public.t_image;

CREATE TABLE public.t_image
(
    id serial NOT NULL,
    directory_id integer,
    saved timestamp without time zone NOT NULL,
    name character varying(255) COLLATE pg_catalog."default" NOT NULL,
    data oid,
    CONSTRAINT t_image_pkey PRIMARY KEY (id),
    CONSTRAINT t_image_directory_id_fkey FOREIGN KEY (directory_id)
        REFERENCES public.t_directory (id) MATCH SIMPLE
        ON UPDATE CASCADE
        ON DELETE NO ACTION
)
WITH (
    OIDS = FALSE
)
TABLESPACE pg_default;

-- ALTER TABLE public.t_image
--     OWNER to postgres;
	
	
-- ======== TRACKING ================================
	
-- Table: public.t_tracking

-- DROP TABLE public.t_tracking;

CREATE TABLE public.t_tracking
(
    id serial NOT NULL,
    ip character varying(1000) COLLATE pg_catalog."default",
    url character varying(1000) COLLATE pg_catalog."default",
    session character varying(100) COLLATE pg_catalog."default",
    "time" timestamp with time zone,
    CONSTRAINT t_tracking_pkey PRIMARY KEY (id)
)
WITH (
    OIDS = FALSE
)
TABLESPACE pg_default;

-- ALTER TABLE public.t_tracking
--     OWNER to postgres;

