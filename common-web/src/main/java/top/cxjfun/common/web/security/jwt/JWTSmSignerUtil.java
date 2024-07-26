package top.cxjfun.common.web.security.jwt;

import cn.hutool.core.lang.Assert;
import cn.hutool.jwt.signers.*;
import top.cxjfun.common.core.crypto.SmAlgorithm;

import java.security.Key;
import java.security.KeyPair;

public class JWTSmSignerUtil {

    public static JWTSigner none() {
        return NoneJWTSigner.NONE;
    }

    public static JWTSigner sm2(Key key) {
        return createSigner(SmAlgorithm.SM2.getValue(), key);
    }

    public static JWTSigner sm3(Key key) {
        return createSigner(SmAlgorithm.SM3.getValue(), key);
    }

    public static JWTSigner sm4(Key key) {
        return createSigner(SmAlgorithm.SM4.getValue(), key);
    }

    public static JWTSigner sm3(byte[] key) {
        return createSigner(SmAlgorithm.SM3.getValue(), key);
    }

    public static JWTSigner sm4(byte[] key) {
        return createSigner(SmAlgorithm.SM4.getValue(), key);
    }

    public static JWTSigner createSigner(String algorithmId, byte[] key) {
        Assert.notNull(key, "Signer key must be not null!", new Object[0]);
        return (JWTSigner) (null != algorithmId && !"none".equals(algorithmId) ? new SmJWTSigner(algorithmId, key) : none());
    }

    public static JWTSigner createSigner(String algorithmId, KeyPair keyPair) {
        Assert.notNull(keyPair, "Signer key pair must be not null!", new Object[0]);
        return (JWTSigner) (null != algorithmId && !"none".equals(algorithmId) ? new SmJWTSigner(algorithmId, keyPair) : none());
    }

    public static JWTSigner createSigner(String algorithmId, Key key) {
        Assert.notNull(key, "Signer key must be not null!", new Object[0]);
        return (JWTSigner) (null != algorithmId && !"none".equals(algorithmId) ? new SmJWTSigner(algorithmId, key) : none());
    }

}
