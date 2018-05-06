INSERT INTO cb_role (id, authority) SELECT nextval('hibernate_sequence'), 'ROLE_ADMIN';

INSERT INTO cb_user (id, account_non_expired, account_non_locked, credentials_non_expired, email, enabled, password, username) SELECT nextval('hibernate_sequence'), TRUE, TRUE, TRUE, 'peper.pleban@gmail.com', TRUE, '$2a$04$k4tTT3p4C.Np6o/hr9NOOuVR692/uR93J3CVX93ipPgnGnBOzjt1e', 'peper11111';

INSERT INTO cb_user_authorities (user_id, authorities_id) SELECT cb_user.id, cb_role.id FROM cb_user, cb_role WHERE cb_user.username = 'peper11111' AND cb_role.authority = 'ROLE_ADMIN';
