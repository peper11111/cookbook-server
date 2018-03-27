INSERT INTO cb_role (id, authority) VALUES (1, 'ROLE_ADMIN');

INSERT INTO cb_user (id, account_non_expired, account_non_locked, credentials_non_expired, email, enabled, password, username) VALUES (1, TRUE, TRUE, TRUE, 'peper.pleban@gmail.com', TRUE, '$2a$04$k4tTT3p4C.Np6o/hr9NOOuVR692/uR93J3CVX93ipPgnGnBOzjt1e', 'peper11111');

INSERT INTO cb_user_authorities (user_id, authorities_id) VALUES (1, 1);
