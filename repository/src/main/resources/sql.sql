--
-- PostgreSQL database dump
--

-- Dumped from database version 9.6.10
-- Dumped by pg_dump version 10.4

-- Started on 2018-12-20 11:08:31 UTC

SET statement_timeout = 0;
SET lock_timeout = 0;
SET idle_in_transaction_session_timeout = 0;
SET client_encoding = 'UTF8';
SET standard_conforming_strings = on;
SELECT pg_catalog.set_config('search_path', '', false);
SET check_function_bodies = false;
SET client_min_messages = warning;
SET row_security = off;

--
-- TOC entry 1 (class 3079 OID 12393)
-- Name: plpgsql; Type: EXTENSION; Schema: -; Owner: 
--

CREATE EXTENSION IF NOT EXISTS plpgsql WITH SCHEMA pg_catalog;


--
-- TOC entry 2159 (class 0 OID 0)
-- Dependencies: 1
-- Name: EXTENSION plpgsql; Type: COMMENT; Schema: -; Owner: 
--

COMMENT ON EXTENSION plpgsql IS 'PL/pgSQL procedural language';


SET default_tablespace = '';

SET default_with_oids = false;

--
-- TOC entry 186 (class 1259 OID 16393)
-- Name: gift_certificate; Type: TABLE; Schema: public; Owner: postgres
--

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

--
-- TOC entry 189 (class 1259 OID 32771)
-- Name: gift_certificate_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.gift_certificate_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.gift_certificate_id_seq OWNER TO postgres;

--
-- TOC entry 2160 (class 0 OID 0)
-- Dependencies: 189
-- Name: gift_certificate_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE public.gift_certificate_id_seq OWNED BY public.gift_certificate.id;


--
-- TOC entry 185 (class 1259 OID 16385)
-- Name: gift_tag; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.gift_tag (
    id integer NOT NULL,
    name text NOT NULL
);


ALTER TABLE public.gift_tag OWNER TO postgres;

--
-- TOC entry 188 (class 1259 OID 32768)
-- Name: gift_tag_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.gift_tag_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.gift_tag_id_seq OWNER TO postgres;

--
-- TOC entry 2161 (class 0 OID 0)
-- Dependencies: 188
-- Name: gift_tag_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE public.gift_tag_id_seq OWNED BY public.gift_tag.tag_id;


--
-- TOC entry 187 (class 1259 OID 16403)
-- Name: tag_certificate; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.tag_certificate (
    tag_id integer NOT NULL,
    certificate_id integer NOT NULL
);


ALTER TABLE public.tag_certificate OWNER TO postgres;

--
-- TOC entry 2020 (class 2604 OID 32773)
-- Name: gift_certificate id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.gift_certificate ALTER COLUMN id SET DEFAULT nextval('public.gift_certificate_id_seq'::regclass);


--
-- TOC entry 2017 (class 2604 OID 32770)
-- Name: gift_tag tag_id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.gift_tag ALTER COLUMN id SET DEFAULT nextval('public.gift_tag_id_seq'::regclass);


--
-- TOC entry 2148 (class 0 OID 16393)
-- Dependencies: 186
-- Data for Name: gift_certificate; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.gift_certificate (id, name, description, price, creation_date, modification_date, expiration_days) FROM stdin;
1	Certificate for 50% sale for dress	\N	10.00	2018-11-06 00:00:00+00	\N	1
71	Certificate for 50% sale for dress1	\N	500.00	2018-12-14 13:58:37.154+00	\N	5
\.


--
-- TOC entry 2147 (class 0 OID 16385)
-- Dependencies: 185
-- Data for Name: gift_tag; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.gift_tag (id, name) FROM stdin;
0	funny
1	wedding
4	tour
8	tratatag
19	tratata
\.


--
-- TOC entry 2149 (class 0 OID 16403)
-- Dependencies: 187
-- Data for Name: tag_certificate; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.tag_certificate (tag_id, certificate_id) FROM stdin;
1	1
\.


--
-- TOC entry 2162 (class 0 OID 0)
-- Dependencies: 189
-- Name: gift_certificate_id_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('public.gift_certificate_id_seq', 89, true);


--
-- TOC entry 2163 (class 0 OID 0)
-- Dependencies: 188
-- Name: gift_tag_id_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('public.gift_tag_id_seq', 149, true);


--
-- TOC entry 2025 (class 2606 OID 16402)
-- Name: gift_certificate gift_certificate_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.gift_certificate
    ADD CONSTRAINT gift_certificate_pkey PRIMARY KEY (id);


--
-- TOC entry 2027 (class 2606 OID 32784)
-- Name: tag_certificate tag_certificate_pk; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.tag_certificate
    ADD CONSTRAINT tag_certificate_pk UNIQUE (tag_id, certificate_id);


--
-- TOC entry 2023 (class 2606 OID 16392)
-- Name: gift_tag tag_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.gift_tag
    ADD CONSTRAINT tag_pkey PRIMARY KEY (id);


--
-- TOC entry 2021 (class 1259 OID 32775)
-- Name: tag_name_uindex; Type: INDEX; Schema: public; Owner: postgres
--

CREATE UNIQUE INDEX tag_name_uindex ON public.gift_tag USING btree (name);


--
-- TOC entry 2029 (class 2606 OID 40968)
-- Name: tag_certificate certificate_fk; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.tag_certificate
    ADD CONSTRAINT certificate_fk FOREIGN KEY (certificate_id) REFERENCES public.gift_certificate(id) ON UPDATE CASCADE ON DELETE CASCADE;


--
-- TOC entry 2028 (class 2606 OID 32790)
-- Name: tag_certificate tag_fk; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.tag_certificate
    ADD CONSTRAINT tag_fk FOREIGN KEY (tag_id) REFERENCES public.gift_tag(id);


-- Completed on 2018-12-20 11:08:31 UTC

--
-- PostgreSQL database dump complete
--

