package br.com.elysian.tartarus.utils;


import br.com.elysian.tartarus.dto.v1.blockchain.ParameterDTO;
import org.jetbrains.annotations.NotNull;
import org.web3j.abi.TypeReference;
import org.web3j.abi.datatypes.Address;
import org.web3j.abi.datatypes.Int;
import org.web3j.abi.datatypes.Type;
import org.web3j.abi.datatypes.Utf8String;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ParameterUtils {

    private static final Map<ParameterDTO.Type, Class<? extends Type>> CASTINGS = Map.of(
            ParameterDTO.Type.ADDRESS, Address.class,
            ParameterDTO.Type.INT, Int.class,
            ParameterDTO.Type.STRING, Utf8String.class
    );


    public static List<Type> castAndList(ParameterDTO[] params) throws NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException {
        if (params.length > 0) {
            List<Type> listParams = new ArrayList<>();
            for (ParameterDTO param : params) {
                Class<? extends Type> clazz = CASTINGS.get(param.getType());

                Constructor<? extends Type> constructor;


                if (param.getType().equals(ParameterDTO.Type.INT)) {
                    constructor = clazz.getDeclaredConstructor(BigInteger.class);
                    listParams.add(constructor.newInstance(new BigInteger((String) param.getValue())));
                    continue;
                }


                constructor = clazz.getDeclaredConstructor(String.class);
                listParams.add(constructor.newInstance((String) param.getValue()));

            }

            return listParams;
        }
        return null;
    }


    public static List<TypeReference> asTypeReference(ParameterDTO[] params) throws InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException {
        List<Type> types = castAndList(params);
        if (types != null && !types.isEmpty()){
            List<TypeReference> listParams = new ArrayList<>();
            for (Type type : types) {
                listParams.add(new TypeReference<>() {});
            }
        }
        return null;
    }


    private <T extends Type> TypeReference<T> getTypeReference(T type) {
        return new TypeReference<T>() {};
    }

}
