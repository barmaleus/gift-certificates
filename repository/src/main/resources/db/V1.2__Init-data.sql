INSERT INTO public.gift_certificate (id, name, description, price, creation_date, modification_date, expiration_days) VALUES (1, 'Certificate for 50% sale for dress', null, 10.00, '2018-11-06 00:00:00.000', null, 1);
INSERT INTO public.gift_certificate (id, name, description, price, creation_date, modification_date, expiration_days) VALUES (71, 'Certificate for 50% sale for dress1', null, 500.00, '2018-12-14 13:58:37.154', null, 5);
INSERT INTO public.gift_tag (tag_id, name) VALUES (0, 'funny');
INSERT INTO public.gift_tag (tag_id, name) VALUES (1, 'wedding');
INSERT INTO public.gift_tag (tag_id, name) VALUES (4, 'tour');
INSERT INTO public.gift_tag (tag_id, name) VALUES (8, 'tratatag');
INSERT INTO public.gift_tag (tag_id, name) VALUES (19, 'tratata');
INSERT INTO public.tag_certificate (tag_id, certificate_id) VALUES (1, 1);