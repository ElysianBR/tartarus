package br.com.elysian.tartarus.utils;

import org.web3j.abi.TypeReference;
import org.web3j.abi.datatypes.*;
import org.web3j.abi.datatypes.generated.Uint256;

import java.math.BigInteger;
import java.util.HashMap;
import java.util.Map;

public class Web3jTypeMapper {

    public enum Types {
        address,
        uint256,
        uint,
        bool,
        string
    }

    private static final Map<Types, Class<? extends Type>> typeMap = new HashMap<>();

    static {
        typeMap.put(Types.address, Address.class);
        typeMap.put(Types.uint256, Uint256.class);
        typeMap.put(Types.uint, Uint.class);
        typeMap.put(Types.bool, Bool.class);
        typeMap.put(Types.string, Utf8String.class);
    }

    public static Type convertToWeb3Type(Types typeName, Object value) throws Exception {
        Class<? extends Type> typeClass = typeMap.get(typeName);
        if (typeClass != null) {

            if (typeClass.equals(Address.class)) {
                return new Address((String) value);
            } else if (typeClass.equals(Uint256.class)) {
                return new Uint256(new BigInteger((String) value));
            } else if (typeClass.equals(Uint.class)) {
                return new Uint(new BigInteger((String) value));
            } else if (typeClass.equals(Bool.class)) {
                return new Bool(Boolean.parseBoolean((String) value));
            } else if (typeClass.equals(Utf8String.class)) {
                return new Utf8String((String) value);
            }
        }
        throw new IllegalArgumentException("Tipo desconhecido: " + typeName);
    }

    public static TypeReference<?> getOutputTypeReference(String typeName) throws Exception {
        Class<? extends Type> typeClass = typeMap.get(typeName);
        if (typeClass != null) {
            return TypeReference.create(typeClass);
        }
        throw new IllegalArgumentException("Tipo desconhecido: " + typeName);
    }
}
