INSERT INTO "tartarus".role(id, description, active)
VALUES ('ADMIN', 'admin group (warning)', TRUE);

INSERT INTO "tartarus".role(id, description, active)
VALUES ('ACCOUNT', 'a standard account', TRUE);

INSERT INTO "tartarus".account(active, id, first_name, last_name, email, password)
VALUES (true, 0, 'SUPER', 'ADMIN',  'super@admin.com.br', '123456789');

INSERT INTO "tartarus".account_role(id_account, id_role) VALUES (0, 'ADMIN');

INSERT INTO "tartarus".confirmation_type(id, description) VALUES (nextval('tartarus.confirmation_type_id_seq'), 'E-mail - Enable Account');

INSERT INTO "tartarus".confirmation_type(id, description) VALUES (nextval('tartarus.confirmation_type_id_seq'), 'E-mail - 2F');