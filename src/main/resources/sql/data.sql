INSERT INTO "tartarus".role(id, description, active)
SELECT 'ADMIN', 'admin group (warning)', TRUE WHERE NOT EXISTS (SELECT 1 FROM "tartarus".role WHERE id = 'ADMIN');

INSERT INTO "tartarus".role(id, description, active)
SELECT 'ACCOUNT', 'a standard account', TRUE WHERE NOT EXISTS (SELECT 1 FROM "tartarus".role WHERE id = 'ACCOUNT');

INSERT INTO "tartarus".account(active, id, first_name, last_name, email, password)
SELECT true, 0, 'SUPER', 'ADMIN',  'super@admin.com.br', '123456789' WHERE NOT EXISTS (SELECT 1 FROM "tartarus".account WHERE id = 0);

INSERT INTO "tartarus".company(active,  cnpj, company_name)
SELECT TRUE, '233242/000135', 'company for development' WHERE NOT EXISTS (SELECT 1 FROM "tartarus".company WHERE cnpj = '233242/000135');

INSERT INTO "tartarus".company_account(id_account, id_company)
SELECT 0, 1 WHERE NOT EXISTS (SELECT 1 FROM "tartarus".company_account WHERE id_account = 0 AND id_company = 1);

INSERT INTO "tartarus".account_role(id_account, id_role)
SELECT 0, 'ADMIN' WHERE NOT EXISTS (SELECT 1 FROM "tartarus".account_role WHERE id_account = 0 AND id_role = 'ADMIN');

INSERT INTO "tartarus".confirmation_type(id, description)
SELECT 1, 'E-mail - Enable Account' WHERE NOT EXISTS (SELECT 1 FROM "tartarus".confirmation_type WHERE id = 1);

INSERT INTO "tartarus".confirmation_type(id, description)
SELECT 3, 'E-mail - 2F' WHERE NOT EXISTS (SELECT 1 FROM "tartarus".confirmation_type WHERE id = 2);