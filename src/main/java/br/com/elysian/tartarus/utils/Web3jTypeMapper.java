package br.com.elysian.tartarus.utils;

import org.web3j.abi.TypeDecoder;
import org.web3j.abi.TypeReference;
import org.web3j.abi.datatypes.*;


public class Web3jTypeMapper {

    public static Type convertToWeb3Type(String typeName, Object value) throws Exception {
        return TypeDecoder.instantiateType(typeName, value);
    }

    public static TypeReference<?> getOutputTypeReference(String typeName) throws Exception {
        return TypeReference.makeTypeReference(typeName);
    }
}
