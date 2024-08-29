INSERT INTO "tartarus".role(id, description, active)
SELECT 'ADMIN',
       'Admin Group (warning)',
       TRUE
WHERE NOT EXISTS (SELECT 1 FROM "tartarus".role WHERE id = 'ADMIN');


INSERT INTO "tartarus".role(id, description, active)
SELECT 'ACCOUNT',
       'Simple Account Group',
       TRUE
WHERE NOT EXISTS (SELECT 1 FROM "tartarus".role WHERE id = 'ACCOUNT');


INSERT INTO "tartarus".role(id, description, active)
SELECT 'BUSINESS_ADMIN',
       'Business Administrator Group',
       TRUE
WHERE NOT EXISTS (SELECT 1 FROM "tartarus".role WHERE id = 'BUSINESS_ADMIN');


INSERT INTO "tartarus".account(active, id, first_name, last_name, email, password)
SELECT true, 0, 'SUPER', 'ADMIN', 'super@admin.com.br', '$2a$08$YqDAVHmSnvH0CpJ3ogtOTOhLxfZEwNVCZY2Tc76MGQa3zQXO/FFVy'
WHERE NOT EXISTS (SELECT 1 FROM "tartarus".account WHERE id = 0);


INSERT INTO "tartarus".company(id, active, cnpj, company_name)
SELECT 0,
       TRUE,
       '000000/000000',
       'company for development'
WHERE NOT EXISTS (SELECT 1 FROM "tartarus".company WHERE cnpj = '000000/000000');


INSERT INTO "tartarus".company_account(account_id, company_id)
SELECT 0,
       0
WHERE NOT EXISTS (SELECT 1 FROM "tartarus".company_account WHERE account_id = 0 AND company_id = 0);


INSERT INTO "tartarus".account_role(account_id, role_id)
SELECT 0,
       'ACCOUNT'
WHERE NOT EXISTS (SELECT 1 FROM "tartarus".account_role WHERE account_id = 0 AND role_id = 'ACCOUNT');


INSERT INTO "tartarus".account_role(account_id, role_id)
SELECT 0,
       'ADMIN'
WHERE NOT EXISTS (SELECT 1 FROM "tartarus".account_role WHERE account_id = 0 AND role_id = 'ADMIN');


INSERT INTO "tartarus".account_role(account_id, role_id)
SELECT 0,
       'BUSINESS_ADMIN'
WHERE NOT EXISTS (SELECT 1 FROM "tartarus".account_role WHERE account_id = 0 AND role_id = 'BUSINESS_ADMIN');


INSERT INTO "tartarus".confirmation_type(id, description)
SELECT 1,
       'E-mail - Enable Account'
WHERE NOT EXISTS (SELECT 1 FROM "tartarus".confirmation_type WHERE id = 1);


INSERT INTO "tartarus".confirmation_type(id, description)
SELECT 2,
       'E-mail - 2F'
WHERE NOT EXISTS (SELECT 1 FROM "tartarus".confirmation_type WHERE id = 2);


INSERT INTO "tartarus".blockchain(id, active, name, tag, description)
SELECT 1,
       true,
       'Ethereum',
       'ETH',
       'Ethereum Blockchain (Powered by EVM)'
WHERE NOT EXISTS (SELECT 1 FROM "tartarus".blockchain WHERE id = 1);


INSERT INTO "tartarus".network_type(id, type_name)
SELECT 1,
       'Testnet'
WHERE NOT EXISTS (SELECT 1 FROM "tartarus".network_type WHERE id = 1);


INSERT INTO "tartarus".network_type(id, type_name)
SELECT 2,
       'Production'
WHERE NOT EXISTS (SELECT 1 FROM "tartarus".network_type WHERE id = 2);


INSERT INTO "tartarus".network(active, type_id, tag, blockchain_id, id, name, description, network_url)
SELECT true,
       1,
       'SEPOL',
       1,
       1,
       'Sepolia ETH',
       'Testnet Ethereum for development',
       'https://rpc.sepolia.org'
WHERE NOT EXISTS (SELECT 1 FROM "tartarus".network WHERE id = 1);


INSERT INTO "tartarus".company_network(company_id, blockchain_id, max_gas_wei)
SELECT 0,
       1,
       0
WHERE NOT EXISTS(SELECT 1 FROM "tartarus".company_network WHERE company_id = 0 AND blockchain_id = 1);


INSERT INTO "tartarus".contract(class_name, name, version, gas_price_wei)
SELECT 'br.com.elysian.tartarus.blockchain.contracts.ethereum.v1.SupplyChainBase',
       'SupplyChainBase',
       'v1',
       0
WHERE NOT EXISTS (SELECT 1 FROM "tartarus".contract WHERE name = 'SupplyChainBase' AND version = 'v1');


INSERT INTO "tartarus".contract(class_name, name, version, gas_price_wei)
SELECT 'br.com.elysian.tartarus.blockchain.contracts.ethereum.v1.ProductRegistry',
       'ProductRegistry',
       'v1',
       0
WHERE NOT EXISTS (SELECT 1 FROM "tartarus".contract WHERE name = 'ProductRegistry' AND version = 'v1');


INSERT INTO "tartarus".contract(class_name, name, version, gas_price_wei)
SELECT 'br.com.elysian.tartarus.blockchain.contracts.ethereum.v1.ProductMovement',
       'ProductMovement',
       'v1',
       0
WHERE NOT EXISTS (SELECT 1 FROM "tartarus".contract WHERE name = 'ProductMovement' AND version = 'v1');


INSERT INTO "tartarus".contract(class_name, name, version, gas_price_wei)
SELECT 'br.com.elysian.tartarus.blockchain.contracts.ethereum.v1.InventoryManagement',
       'InventoryManagement',
       'v1',
       0
WHERE NOT EXISTS (SELECT 1 FROM "tartarus".contract WHERE name = 'InventoryManagement' AND version = 'v1');


INSERT INTO "tartarus".company_network_contract(company_network_blockchain_id, company_network_company_id,
                                                contract_address, contract_name, contract_version, max_gas_wei)
SELECT 1,
       0,
       '',
       'SupplyChainBase',
       'v1',
       0
WHERE NOT EXISTS (SELECT 1
                  FROM "tartarus".company_network_contract
                  WHERE company_network_blockchain_id = 1
                    AND company_network_company_id = 0
                    AND contract_name = 'SupplyChainBase'
                    AND contract_version = 'V1')