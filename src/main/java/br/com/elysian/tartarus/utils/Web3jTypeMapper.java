package br.com.elysian.tartarus.utils;

import org.web3j.abi.TypeDecoder;
import org.web3j.abi.TypeReference;
import org.web3j.abi.datatypes.*;
import org.web3j.abi.datatypes.generated.Uint256;

import java.math.BigInteger;
import java.util.HashMap;
import java.util.Map;

public class Web3jTypeMapper {

    public static Type<?> convertToWeb3Type(String typeName, Object value) throws Exception {
        return TypeDecoder.instantiateType(typeName, value);
    }

    public static TypeReference<?> getOutputTypeReference(String typeName) throws Exception {
        return TypeReference.makeTypeReference(typeName);
    }
}
