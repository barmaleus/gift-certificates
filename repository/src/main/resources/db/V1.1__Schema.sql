COMMENT ON EXTENSION plpgsql IS 'PL/pgSQL procedural language';

SET default_tablespace = '';

SET default_with_oids = false;

CREATE TYPE role_enum AS ENUM ('ADMIN', 'USER');

CREATE TABLE public.gift_user (
    id integer NOT NULL,
    login text NOT NULL,
    password text NOT NULL,
    role role_enum DEFAULT 'USER'::role_enum NOT NULL
);

alter table gift_user owner to postgres;

CREATE SEQUENCE public.gift_user_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;

ALTER TABLE public.gift_user_id_seq OWNER TO postgres;

ALTER SEQUENCE public.gift_user_id_seq OWNED BY public.gift_user.id;

CREATE TABLE public.gift_certificate (
    id integer NOT NULL,
    name text NOT NULL,
    description text,
    price numeric(10,2) NOT NULL,
    creation_date timestamp(3) with time zone DEFAULT now() NOT NULL,
    modification_date timestamp(3) with time zone,
    expiration_days integer DEFAULT 1 NOT NULL
);

ALTER TABLE public.gift_certificate OWNER TO postgres;

CREATE SEQUENCE public.gift_certificate_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;

ALTER TABLE public.gift_certificate_id_seq OWNER TO postgres;

ALTER SEQUENCE public.gift_certificate_id_seq OWNED BY public.gift_certificate.id;

CREATE TABLE public.gift_tag (
    id integer NOT NULL,
    name text NOT NULL
);

ALTER TABLE public.gift_tag OWNER TO postgres;

CREATE SEQUENCE public.gift_tag_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;

ALTER TABLE public.gift_tag_id_seq OWNER TO postgres;

ALTER SEQUENCE public.gift_tag_id_seq OWNED BY public.gift_tag.id;

CREATE TABLE public.tag_certificate (
    tag_id integer NOT NULL,
    certificate_id integer NOT NULL
);

ALTER TABLE public.tag_certificate OWNER TO postgres;

CREATE TABLE public.user_certificate (
    user_id integer NOT NULL,
    certificate_id integer NOT NULL
);

ALTER TABLE public.user_certificate OWNER TO postgres;

ALTER TABLE ONLY public.gift_user ALTER COLUMN id SET DEFAULT nextval('public.gift_user_id_seq'::regclass);

ALTER TABLE ONLY public.gift_certificate ALTER COLUMN id SET DEFAULT nextval('public.gift_certificate_id_seq'::regclass);

ALTER TABLE ONLY public.gift_tag ALTER COLUMN id SET DEFAULT nextval('public.gift_tag_id_seq'::regclass);

SELECT pg_catalog.setval('public.gift_user_id_seq', 5, true);

SELECT pg_catalog.setval('public.gift_certificate_id_seq', 71, true);

SELECT pg_catalog.setval('public.gift_tag_id_seq', 20, true);

ALTER TABLE ONLY public.gift_user
    ADD CONSTRAINT gift_user_pkey PRIMARY KEY (id);

ALTER TABLE ONLY public.gift_certificate
    ADD CONSTRAINT gift_certificate_pkey PRIMARY KEY (id);

ALTER TABLE ONLY public.tag_certificate
    ADD CONSTRAINT tag_certificate_pk UNIQUE (tag_id, certificate_id);

    ALTER TABLE ONLY public.user_certificate
        ADD CONSTRAINT user_certificate_pk UNIQUE (user_id, certificate_id);

ALTER TABLE ONLY public.gift_tag
    ADD CONSTRAINT tag_pkey PRIMARY KEY (id);

CREATE UNIQUE INDEX gift_user_login_uindex ON public.gift_user USING btree (login);

CREATE UNIQUE INDEX tag_name_uindex ON public.gift_tag USING btree (name);

ALTER TABLE ONLY public.tag_certificate
    ADD CONSTRAINT certificate_tag_fk FOREIGN KEY (certificate_id) REFERENCES public.gift_certificate(id) ON UPDATE CASCADE ON DELETE CASCADE;

ALTER TABLE ONLY public.tag_certificate
    ADD CONSTRAINT tag_fk FOREIGN KEY (tag_id) REFERENCES public.gift_tag(id);

    ALTER TABLE ONLY public.user_certificate
        ADD CONSTRAINT certificate_user_fk FOREIGN KEY (certificate_id) REFERENCES public.gift_certificate(id) ON UPDATE CASCADE ON DELETE CASCADE;

    ALTER TABLE ONLY public.user_certificate
        ADD CONSTRAINT user_fk FOREIGN KEY (user_id) REFERENCES public.gift_user(id);
