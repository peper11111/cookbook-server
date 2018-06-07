INSERT INTO cb_cuisine (id, name) SELECT nextval('hibernate_sequence'), 'CUISINE_AMERICAN';
INSERT INTO cb_cuisine (id, name) SELECT nextval('hibernate_sequence'), 'CUISINE_ASIAN';
INSERT INTO cb_cuisine (id, name) SELECT nextval('hibernate_sequence'), 'CUISINE_CZECH';
INSERT INTO cb_cuisine (id, name) SELECT nextval('hibernate_sequence'), 'CUISINE_FRENCH';
INSERT INTO cb_cuisine (id, name) SELECT nextval('hibernate_sequence'), 'CUISINE_GREEK';
INSERT INTO cb_cuisine (id, name) SELECT nextval('hibernate_sequence'), 'CUISINE_ITALIAN';
INSERT INTO cb_cuisine (id, name) SELECT nextval('hibernate_sequence'), 'CUISINE_POLISH';
INSERT INTO cb_cuisine (id, name) SELECT nextval('hibernate_sequence'), 'CUISINE_SPANISH';

INSERT INTO cb_role (id, authority) SELECT nextval('hibernate_sequence'), 'ROLE_ADMIN';

INSERT INTO cb_user (id, account_non_expired, account_non_locked, credentials_non_expired, description, email, enabled, password, username) SELECT nextval('hibernate_sequence'), TRUE, TRUE, TRUE, 'Lorem ipsum dolor sit amet, consectetur adipiscing elit. Proin nibh augue, suscipit a, scelerisque sed, lacinia in, mi. Cras vel lorem. Etiam pellentesque aliquet tellus. Phasellus pharetra nulla ac diam.', 'peper11111@example.com', TRUE, '$2a$04$k4tTT3p4C.Np6o/hr9NOOuVR692/uR93J3CVX93ipPgnGnBOzjt1e', 'peper11111';
INSERT INTO cb_user (id, account_non_expired, account_non_locked, credentials_non_expired, description, email, enabled, password, username) SELECT nextval('hibernate_sequence'), TRUE, TRUE, TRUE, 'Lorem ipsum dolor sit amet, consectetur adipiscing elit. Proin nibh augue, suscipit a, scelerisque sed, lacinia in, mi. Cras vel lorem. Etiam pellentesque aliquet tellus. Phasellus pharetra nulla ac diam.', 'lalalal@example.com', TRUE, '$2a$04$k4tTT3p4C.Np6o/hr9NOOuVR692/uR93J3CVX93ipPgnGnBOzjt1e', 'lalala';
INSERT INTO cb_user (id, account_non_expired, account_non_locked, credentials_non_expired, description, email, enabled, password, username) SELECT nextval('hibernate_sequence'), TRUE, TRUE, TRUE, 'Lorem ipsum dolor sit amet, consectetur adipiscing elit. Proin nibh augue, suscipit a, scelerisque sed, lacinia in, mi. Cras vel lorem. Etiam pellentesque aliquet tellus. Phasellus pharetra nulla ac diam.', 'koko@example.com', TRUE, '$2a$04$k4tTT3p4C.Np6o/hr9NOOuVR692/uR93J3CVX93ipPgnGnBOzjt1e', 'koko';
INSERT INTO cb_user (id, account_non_expired, account_non_locked, credentials_non_expired, description, email, enabled, password, username) SELECT nextval('hibernate_sequence'), TRUE, TRUE, TRUE, 'Lorem ipsum dolor sit amet, consectetur adipiscing elit. Proin nibh augue, suscipit a, scelerisque sed, lacinia in, mi. Cras vel lorem. Etiam pellentesque aliquet tellus. Phasellus pharetra nulla ac diam.', 'perlage@example.com', TRUE, '$2a$04$k4tTT3p4C.Np6o/hr9NOOuVR692/uR93J3CVX93ipPgnGnBOzjt1e', 'perlage';
INSERT INTO cb_user (id, account_non_expired, account_non_locked, credentials_non_expired, description, email, enabled, password, username) SELECT nextval('hibernate_sequence'), TRUE, TRUE, TRUE, 'Lorem ipsum dolor sit amet, consectetur adipiscing elit. Proin nibh augue, suscipit a, scelerisque sed, lacinia in, mi. Cras vel lorem. Etiam pellentesque aliquet tellus. Phasellus pharetra nulla ac diam.', 'fitnut@example.com', TRUE, '$2a$04$k4tTT3p4C.Np6o/hr9NOOuVR692/uR93J3CVX93ipPgnGnBOzjt1e', 'fitnut';

INSERT INTO cb_user_authorities (user_id, authorities_id) SELECT cb_user.id, cb_role.id FROM cb_user, cb_role WHERE cb_user.username = 'peper11111' AND cb_role.authority = 'ROLE_ADMIN';
