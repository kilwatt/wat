package com.kilowatt.Compiler.Builtins.Libraries.Utils;

import com.kilowatt.Compiler.WattCompiler;
import com.kilowatt.Errors.WattRuntimeError;
import com.kilowatt.WattVM.VmAddress;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

/*
Utils -> Криптография
 */
public class UtilsCrypto {
    // получение алгоритма шифрования
    public MessageDigest digest(String algo) {
        try {
            return MessageDigest.getInstance(algo);
        } catch (NoSuchAlgorithmException e) {
            VmAddress address = WattCompiler.vm.getCallsHistory().getLast().getAddress();
            throw new WattRuntimeError(
                address.getLine(),
                address.getFileName(),
                algo + " crypto algo not found.",
                    "available: sha-256, md5, sha-512, sha-1."
            );
        }
    }

    /*
    работа с base64
     */
    public Base64.Decoder get_base64_decoder() {
        return Base64.getDecoder();
    }

    public Base64.Encoder get_base64_encoder() {
        return Base64.getEncoder();
    }
}
