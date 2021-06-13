INSERT INTO public.cart_user
(user_id, active, "password", username)
VALUES(1, true , '$2a$10$Ms1BzKzUGhdpas52U28VV.DI1FAjcZFc/.3b3PmkEevf/0LA2tUPq', 'user1');


INSERT INTO public.cart_user
(user_id, active, "password", username)
VALUES(2, true , '$2a$10$Ms1BzKzUGhdpas52U28VV.DI1FAjcZFc/.3b3PmkEevf/0LA2tUPq', 'user2');


INSERT INTO public."role"
(role_id, "role")
VALUES(1, 'consumer');


INSERT INTO public.user_role
(user_id, role_id)
VALUES(1, 1);

INSERT INTO public.user_role
(user_id, role_id)
VALUES(2, 1);
