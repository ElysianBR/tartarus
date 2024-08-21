const fs = require('fs');
const path = require('path');

// Recebe o caminho para a pasta com os arquivos JSON como argumento
const folderPath = process.argv[2];

if (!folderPath) {
    console.error('Por favor, forneça o caminho para a pasta contendo os arquivos de build em formato .json.');
    process.exit(1);
}

// Lê o conteúdo da pasta
fs.readdir(folderPath, (err, files) => {
    if (err) {
        console.error(`Erro ao ler a pasta: ${err.message}`);
        process.exit(1);
    }

    // Filtra apenas os arquivos JSON
    const jsonFiles = files.filter(file => path.extname(file) === '.json');

    if (jsonFiles.length === 0) {
        console.warn('Nenhum arquivo JSON encontrado na pasta.');
        process.exit(0);
    }

    // Processa cada arquivo JSON
    jsonFiles.forEach(file => {
        const filePath = path.join(folderPath, file);

        fs.readFile(filePath, 'utf8', (err, data) => {
            if (err) {
                console.error(`Erro ao ler o arquivo JSON "${file}": ${err.message}`);
                return;
            }

            try {
                const json = JSON.parse(data);

                const contractName = json.contractName;
                // Verifica se o JSON possui a propriedade 'contracts'
                if (!contractName) {
                    console.warn(`O arquivo JSON "${file}" não contém a propriedade "contractName".`);
                    return;
                }


                // Verifica se o contrato possui ABI e BIN
                if (json.abi && json.bytecode) {
                    // Define os caminhos para salvar os arquivos
                    const baseDir = path.dirname(filePath);
                    const abiFilePath = path.join(baseDir, `${contractName}.abi`);
                    const binFilePath = path.join(baseDir, `${contractName}.bin`);

                    // Salva o ABI em um arquivo
                    fs.writeFile(abiFilePath, JSON.stringify(json.abi, null, 2), (err) => {
                        if (err) {
                            console.error(`Erro ao salvar o ABI do contrato "${contractName}" em "${abiFilePath}": ${err.message}`);
                        } else {
                            console.log(`ABI do contrato "${contractName}" salvo em: ${abiFilePath}`);
                        }
                    });

                    // Salva o BIN em um arquivo
                    fs.writeFile(binFilePath, json.bytecode, (err) => {
                        if (err) {
                            console.error(`Erro ao salvar o BIN do contrato "${contractName}" em "${binFilePath}": ${err.message}`);
                        } else {
                            console.log(`BIN do contrato "${contractName}" salvo em: ${binFilePath}`);
                        }
                    });
                } else {
                    console.warn(`O contrato "${contractName}" no arquivo "${file}" não contém "abi" ou "bytecode".`);
                }


            } catch (parseErr) {
                console.error(`Erro ao parsear o arquivo JSON "${file}": ${parseErr.message}`);
            }
        });
    });
});
