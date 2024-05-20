INSERT INTO "tartarus".role(id, description, active)
VALUES ('ADMIN', 'admin group (warning)', TRUE);

INSERT INTO "tartarus".role(id, description, active)
VALUES ('ACCOUNT', 'a standard account', TRUE);

INSERT INTO "tartarus".account(active, id, first_name, last_name, email, password)
VALUES (true, 0, 'SUPER', 'ADMIN',  'super@admin.com.br', '123456789');

INSERT INTO "tartarus".business(active,  cnpj, company_name) VALUES (TRUE, '233242/000135', 'business for development');

INSERT INTO "tartarus".business_account(id_account, id_business) VALUES (0, 1);

INSERT INTO "tartarus".account_role(id_account, id_role) VALUES (0, 'ADMIN');

INSERT INTO "tartarus".confirmation_type(description) VALUES ('E-mail - Enable Account');

INSERT INTO "tartarus".confirmation_type(description) VALUES ('E-mail - 2F');