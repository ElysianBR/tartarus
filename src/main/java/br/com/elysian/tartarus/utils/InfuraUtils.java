package br.com.elysian.tartarus.utils;


import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Getter;
import org.apache.hc.client5.http.async.methods.SimpleHttpRequest;
import org.apache.hc.client5.http.async.methods.SimpleHttpResponse;
import org.apache.hc.client5.http.async.methods.SimpleRequestBuilder;
import org.apache.hc.client5.http.impl.async.CloseableHttpAsyncClient;
import org.apache.hc.client5.http.impl.async.HttpAsyncClients;
import org.apache.hc.client5.http.impl.nio.PoolingAsyncClientConnectionManager;
import org.apache.hc.client5.http.impl.nio.PoolingAsyncClientConnectionManagerBuilder;
import org.apache.hc.client5.http.ssl.ClientTlsStrategyBuilder;
import org.apache.hc.client5.http.ssl.NoopHostnameVerifier;
import org.apache.hc.core5.http.nio.ssl.TlsStrategy;
import org.apache.hc.core5.ssl.SSLContexts;
import org.apache.hc.core5.ssl.TrustStrategy;
import org.web3j.tx.gas.ContractGasProvider;
import org.web3j.tx.gas.StaticGasProvider;

import javax.net.ssl.SSLContext;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.RoundingMode;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;


public class InfuraUtils {

    @Getter
    private final String url;
    @Getter
    private final String key;
    private final PoolingAsyncClientConnectionManager cm;


    public InfuraUtils(String url, String key) throws NoSuchAlgorithmException, KeyStoreException, KeyManagementException {
        this.url = url;
        this.key = key;

        TrustStrategy acceptingTrustStrategy = (certificate, authType) -> true;
        SSLContext sslContext = SSLContexts.custom().loadTrustMaterial(null, acceptingTrustStrategy).build();
        TlsStrategy tlsStrategy = ClientTlsStrategyBuilder.create().setHostnameVerifier(NoopHostnameVerifier.INSTANCE).setSslContext(sslContext).build();
        this.cm = PoolingAsyncClientConnectionManagerBuilder.create().setTlsStrategy(tlsStrategy).build();
    }

    private CloseableHttpAsyncClient getClient() {
        return HttpAsyncClients.custom().setConnectionManager(cm).build();
    }


    public BigInteger getGasPrice() throws IOException, ExecutionException, InterruptedException {
        try (CloseableHttpAsyncClient httpClient = this.getClient()) {

            SimpleHttpRequest req = SimpleRequestBuilder.post(
                    String.format("%s/%s", url, key)
            ).build();

            Future<SimpleHttpResponse> future = httpClient.execute(req, null);
            SimpleHttpResponse response = future.get();

            int statusCode = response.getCode();
            if (statusCode == 200) {
                String outBody = response.getBodyText();
                ObjectMapper objectMapper = new ObjectMapper();
                return new BigInteger(
                        objectMapper.readValue(
                                outBody,
                                InfuraResponse.class
                        ).getResult()
                );
            }

            throw new IOException("Request fail. Status Code: " + statusCode);
        }
    }


    public ContractGasProvider getGasProvider(BigInteger maxGas) throws IOException, ExecutionException, InterruptedException {
        BigInteger gasPrice = this.getGasPrice();
        return new StaticGasProvider(gasPrice, maxGas);
    }


    public ContractGasProvider getGasProvider(BigDecimal maxGasPercentUnderGasPrice) throws IOException, ExecutionException, InterruptedException {
        BigInteger gasPrice = this.getGasPrice();

        // Increment percent of gas price
        final BigDecimal HUNDRED = new BigDecimal(100);
        BigDecimal maxPercentDecimal = maxGasPercentUnderGasPrice.divide(HUNDRED, RoundingMode.HALF_UP);
        BigDecimal gasPriceDecimal = new BigDecimal(gasPrice);
        BigDecimal increment = gasPriceDecimal.multiply(maxPercentDecimal);
        BigInteger incrementBigInt = increment.toBigInteger();
        BigInteger maxGas = gasPrice.add(incrementBigInt);

        return new StaticGasProvider(gasPrice, maxGas);
    }


    public ContractGasProvider getGasProvider() throws IOException, ExecutionException, InterruptedException {
        return this.getGasProvider(BigInteger.ONE);
    }

}

@Getter
class InfuraResponse {

    private String jsonrpc;
    private int id;
    private String result;

}