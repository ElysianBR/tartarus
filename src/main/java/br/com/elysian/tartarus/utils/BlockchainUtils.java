package br.com.elysian.tartarus.utils;

import jakarta.servlet.http.HttpServletRequest;
import org.web3j.crypto.Keys;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.methods.request.Transaction;
import org.web3j.protocol.core.methods.response.EthGasPrice;
import org.web3j.tx.gas.ContractGasProvider;
import org.web3j.tx.gas.StaticGasProvider;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.SecretKeySpec;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.RoundingMode;
import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;
import java.util.concurrent.*;


public class BlockchainUtils {

    public static final BigDecimal HUNDRED = new BigDecimal(100);

    public static boolean isValidEthereumAddress(String address) {
        return address != null && address.matches("^0x[0-9a-fA-F]{40}$") && Keys.toChecksumAddress(address).equals(address);
    }


    public static BigInteger getGasPrice(Web3j web3j) throws IOException {
        return web3j.ethGasPrice().send().getGasPrice();
    }


    public static BigInteger getEstimatedGasPriceToDeploy(Web3j web3j, BigInteger gasPrice, String from, String bytecode) throws IOException {
        return web3j.ethEstimateGas(Transaction.createContractTransaction(
                from,
                null,
                gasPrice,
                bytecode
        )).send().getAmountUsed();
    }


    public static BigInteger getEstimatedGasPriceToDeploy(Web3j web3j, String from, String bytecode) throws IOException {
        return getEstimatedGasPriceToDeploy(web3j, getGasPrice(web3j), from, bytecode);
    }


    public static ContractGasProvider getGasProvider(Web3j web3j, BigDecimal maxGasPercentUnderGasPrice) throws IOException {
        BigInteger gasPrice = BlockchainUtils.getGasPrice(web3j);
        BigDecimal maxPercentDecimal = maxGasPercentUnderGasPrice.divide(HUNDRED, RoundingMode.HALF_UP);
        BigDecimal percent = BigDecimal.ONE.add(maxPercentDecimal);
        BigDecimal maxGasPrice = new BigDecimal(gasPrice).multiply(percent);
        return new StaticGasProvider(gasPrice, maxGasPrice.toBigInteger());
    }

    public static ContractGasProvider getGasProvider(Web3j web3j) throws IOException {
        BigInteger gasPrice = BlockchainUtils.getGasPrice(web3j);
        return new StaticGasProvider(gasPrice, gasPrice);
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

//    public static <T> T executeExternalCall(HttpServletRequest request, Callable<T> call) {
//        try (ExecutorService executor = Executors.newSingleThreadExecutor()) {
//
//            Future<T> future = executor.submit(call);
//
//            request.startAsync();
//
//            while (!future.isDone()) {
//                System.out.println("ENTREI AQ");
//                if (request.isAsyncStarted() && request.getSession(false) == null) {
//                    future.cancel(true);
//                    throw new InterruptedException("Sessão encerrada. A chamada externa foi cancelada.");
//                }
//            }
//
//            return future.get();
//        } catch (ExecutionException | InterruptedException e) {
//            throw new RuntimeException(e);
//        }
//    }

}
