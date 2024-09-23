package br.com.elysian.tartarus.utils;

import br.com.elysian.tartarus.dto.v1.blockchain.RequestSpecDTO;
import org.web3j.crypto.Keys;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.methods.request.Transaction;
import org.web3j.protocol.core.methods.response.EthBaseFee;
import org.web3j.tx.TransactionManager;
import org.web3j.tx.gas.StaticEIP1559GasProvider;
import org.web3j.utils.Convert;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.SecretKeySpec;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;


public class BlockchainUtils {

    private static final BigInteger DEFAULT_MAX_PRIORITY_FEE_PER_GAS = Convert.toWei("1.5", Convert.Unit.GWEI).toBigInteger(); // 1.5 Gwei
    public static final BigDecimal HUNDRED = new BigDecimal(100);

    public static boolean isValidEthereumAddress(String address) {
        return address != null && address.matches("^0x[0-9a-fA-F]{40}$") && Keys.toChecksumAddress(address).equals(address);
    }


    public static BigInteger getGasPrice(Web3j web3j) throws IOException {
        return web3j.ethGasPrice().send().getGasPrice();
    }

    public static BigInteger getBaseFee(Web3j web3j) throws IOException {
        EthBaseFee response = web3j.ethBaseFee().send();
        return response.getBaseFee();
    }


    public static BigInteger getEstimatedGasConstructor(Web3j web3j, BigInteger gasPrice, String from, String bytecode) throws IOException {
        return web3j.ethEstimateGas(Transaction.createContractTransaction(
                from,
                null,
                gasPrice,
                bytecode
        )).send().getAmountUsed();
    }

    public static BigInteger getEstimatedGasFunction(Web3j web3j, BigInteger gasPrice, String from, String contractAddress, String data) throws IOException {
        return web3j.ethEstimateGas(Transaction.createFunctionCallTransaction(
                from,
                null,
                gasPrice,
                null,
                contractAddress,
                null,
                data
        )).send().getAmountUsed();
    }


    public static StaticEIP1559GasProvider constructEIP1559GasProvider(Web3j web3j, BigInteger maxFeePerGas, BigInteger maxPriorityFeePerGas, BigInteger estimateGas) throws IOException {
        long chainId = web3j.ethChainId().send().getChainId().longValue();
        return new StaticEIP1559GasProvider(chainId, maxFeePerGas, maxPriorityFeePerGas, estimateGas);
    }

    public static StaticEIP1559GasProvider getEIP1559GasProvider(Web3j web3j, BigInteger maxFeePerGas, BigInteger maxPriorityFeePerGas, BigInteger estimateGas) throws IOException {
        return constructEIP1559GasProvider(web3j, maxFeePerGas, maxPriorityFeePerGas, estimateGas);
    }

    public static StaticEIP1559GasProvider getEIP1559GasProvider(Web3j web3j, BigInteger maxFeePerGas, BigInteger estimateGas) throws IOException {
        return getEIP1559GasProvider(web3j, maxFeePerGas, DEFAULT_MAX_PRIORITY_FEE_PER_GAS, estimateGas);
    }

    public static StaticEIP1559GasProvider getGasProvider(Web3j web3j, TransactionManager transactionManager, String binary) throws IOException {
        return getGasProvider(web3j, transactionManager, (String) null, binary);
    }

    public static StaticEIP1559GasProvider getGasProvider(Web3j web3j, TransactionManager transactionManager, String contractAddress, String binary) throws IOException {
        String fromAddress = transactionManager.getFromAddress();
        BigInteger baseFee = getGasPrice(web3j);
        BigInteger maxFeePerGas = baseFee.add(DEFAULT_MAX_PRIORITY_FEE_PER_GAS);
        BigInteger estimateGas;
        if (contractAddress == null) {
            estimateGas = getEstimatedGasConstructor(web3j, baseFee, fromAddress, binary);
        } else {
            estimateGas = getEstimatedGasFunction(web3j, baseFee, fromAddress, contractAddress, binary);
        }
        return getEIP1559GasProvider(web3j, maxFeePerGas, estimateGas);
    }

    public static StaticEIP1559GasProvider getGasProvider(Web3j web3j, TransactionManager transactionManager, String binary, RequestSpecDTO specDTO) throws IOException {
        return getGasProvider(web3j, transactionManager, null, binary, specDTO);
    }

    public static StaticEIP1559GasProvider getGasProvider(Web3j web3j, TransactionManager transactionManager, String contractAddress, String binary, RequestSpecDTO specDTO) throws IOException {
        if (specDTO.getGasPriceWei() != null || specDTO.getMaxPriorityFeePerGasGwei() != null || specDTO.getGasLimitWei() != null) {
            BigInteger baseFee;
            BigInteger maxPriorityFeePerGas;
            BigInteger gasLimit;

            if (specDTO.getGasPriceWei() != null) {
                baseFee = specDTO.getGasPriceWei();
            } else {
                baseFee = getBaseFee(web3j);
            }

            if (specDTO.getMaxPriorityFeePerGasGwei() != null) {
                maxPriorityFeePerGas = specDTO.getMaxPriorityFeePerGasGwei();
            } else {
                maxPriorityFeePerGas = DEFAULT_MAX_PRIORITY_FEE_PER_GAS;
            }

            if (specDTO.getGasLimitWei() != null) {
                gasLimit = specDTO.getGasLimitWei();
            } else {
                String fromAddress = transactionManager.getFromAddress();
                if (contractAddress == null) {
                    gasLimit = getEstimatedGasConstructor(web3j, baseFee, fromAddress, binary);
                } else {
                    gasLimit = getEstimatedGasFunction(web3j, baseFee, fromAddress, contractAddress, binary);
                }
            }

            BigInteger maxFeePerGas = baseFee.add(maxPriorityFeePerGas);

            return getEIP1559GasProvider(web3j, maxFeePerGas, maxPriorityFeePerGas, gasLimit);
        }
        return getGasProvider(web3j, transactionManager, contractAddress, binary);
    }


    public static SecretKeySpec getKeyFromPassword(String password) throws NoSuchAlgorithmException {
        MessageDigest sha = MessageDigest.getInstance("SHA-256");
        byte[] key = sha.digest(password.getBytes(StandardCharsets.UTF_8));
        return new SecretKeySpec(key, "AES");
    }


    public static String encryptPrivateKey(String privateKey, SecretKeySpec key) throws NoSuchPaddingException, NoSuchAlgorithmException, InvalidKeyException, IllegalBlockSizeException, BadPaddingException {
        Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
        cipher.init(Cipher.ENCRYPT_MODE, key);
        byte[] encrypted = cipher.doFinal(privateKey.getBytes(StandardCharsets.UTF_8));
        return Base64.getEncoder().encodeToString(encrypted);
    }


    public static String decryptPrivateKey(String encryptedPrivateKey, SecretKeySpec key) throws NoSuchPaddingException, NoSuchAlgorithmException, InvalidKeyException, IllegalBlockSizeException, BadPaddingException {
        Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
        cipher.init(Cipher.DECRYPT_MODE, key);
        byte[] decoded = Base64.getDecoder().decode(encryptedPrivateKey);
        byte[] decrypted = cipher.doFinal(decoded);
        return new String(decrypted, StandardCharsets.UTF_8);
    }

}
