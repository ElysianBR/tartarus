INSERT INTO "tartarus".role(id, description, active)
SELECT 'ADMIN', 'Admin Group (warning)', TRUE
WHERE NOT EXISTS (SELECT 1 FROM "tartarus".role WHERE id = 'ADMIN');

INSERT INTO "tartarus".role(id, description, active)
SELECT 'ACCOUNT', 'Simple Account Group', TRUE
WHERE NOT EXISTS (SELECT 1 FROM "tartarus".role WHERE id = 'ACCOUNT');

INSERT INTO "tartarus".role(id, description, active)
SELECT 'BUSINESS_ADMIN', 'Business Administrator Group', TRUE
WHERE NOT EXISTS (SELECT 1 FROM "tartarus".role WHERE id = 'BUSINESS_ADMIN');

INSERT INTO "tartarus".account(active, id, first_name, last_name, email, password)
SELECT true, 0, 'SUPER', 'ADMIN', 'super@admin.com.br', '$2a$08$YqDAVHmSnvH0CpJ3ogtOTOhLxfZEwNVCZY2Tc76MGQa3zQXO/FFVy'
WHERE NOT EXISTS (SELECT 1 FROM "tartarus".account WHERE id = 0);

INSERT INTO "tartarus".company(id, active, cnpj, company_name)
SELECT 0, TRUE, '000000/000000', 'company for development'
WHERE NOT EXISTS (SELECT 1 FROM "tartarus".company WHERE cnpj = '000000/000000');

INSERT INTO "tartarus".company_account(account_id, company_id)
SELECT 0, 0
WHERE NOT EXISTS (SELECT 1 FROM "tartarus".company_account WHERE account_id = 0 AND company_id = 0);

INSERT INTO "tartarus".account_role(account_id, role_id)
SELECT 0, 'ACCOUNT'
WHERE NOT EXISTS (SELECT 1 FROM "tartarus".account_role WHERE account_id = 0 AND role_id = 'ACCOUNT');

INSERT INTO "tartarus".account_role(account_id, role_id)
SELECT 0, 'ADMIN'
WHERE NOT EXISTS (SELECT 1 FROM "tartarus".account_role WHERE account_id = 0 AND role_id = 'ADMIN');

INSERT INTO "tartarus".account_role(account_id, role_id)
SELECT 0, 'BUSINESS_ADMIN'
WHERE NOT EXISTS (SELECT 1 FROM "tartarus".account_role WHERE account_id = 0 AND role_id = 'BUSINESS_ADMIN');

INSERT INTO "tartarus".confirmation_type(id, description)
SELECT 1, 'E-mail - Enable Account'
WHERE NOT EXISTS (SELECT 1 FROM "tartarus".confirmation_type WHERE id = 1);

INSERT INTO "tartarus".confirmation_type(id, description)
SELECT 2, 'E-mail - 2F'
WHERE NOT EXISTS (SELECT 1 FROM "tartarus".confirmation_type WHERE id = 2);

INSERT INTO "tartarus".blockchain_type(id, type_name)
SELECT 0, 'Testnet'
WHERE NOT EXISTS (SELECT 1 FROM "tartarus".blockchain_type WHERE id = 0);

INSERT INTO "tartarus".blockchain_type(id, type_name)
SELECT 1, 'Prodution'
WHERE NOT EXISTS (SELECT 1 FROM "tartarus".blockchain_type WHERE id = 1);

INSERT INTO "tartarus".blockchain(id, active, name, network_url, tag, type_id)
SELECT 0, true, 'Sepolia ETH', 'https://rpc.sepolia.org', 'SEPOL', 0
WHERE NOT EXISTS (SELECT 1 FROM "tartarus".blockchain WHERE id = 0);

INSERT INTO "tartarus".company_blockchain(adress, company_id, blockchain_id, max_gas_wei)
SELECT '0x612f733F3809D2868EAbC02F675AA173c23dB548', 0, 0, 0
WHERE NOT EXISTS (SELECT 1 FROM "tartarus".company_blockchain WHERE company_id = 0 AND blockchain_id = 0);