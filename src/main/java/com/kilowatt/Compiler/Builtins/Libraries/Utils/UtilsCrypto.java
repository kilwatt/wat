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
    В base64
     */
    public String b64(String text) {
        return Base64.getEncoder().encodeToString(text.getBytes());
    }
    public String de_b64(String text) {
        return new String(Base64.getDecoder().decode(text.getBytes()));
    }

    /*
    В hex
     */
    public String to_hex(byte[] bytes) {
        StringBuilder sb = new StringBuilder(bytes.length * 2);
        for(byte b: bytes)
            sb.append(String.format("%02x", b));
        return sb.toString();
    }
}
