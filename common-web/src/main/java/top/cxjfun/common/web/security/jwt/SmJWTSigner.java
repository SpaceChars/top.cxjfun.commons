package top.cxjfun.common.web.security.jwt;

import cn.hutool.core.codec.Base64;
import cn.hutool.core.util.CharsetUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.crypto.asymmetric.Sign;
import cn.hutool.crypto.digest.HMac;
import cn.hutool.crypto.digest.mac.SM4MacEngine;
import cn.hutool.jwt.signers.JWTSigner;
import org.bouncycastle.crypto.CipherParameters;
import org.bouncycastle.crypto.params.KeyParameter;
import top.cxjfun.common.core.crypto.SmAlgorithm;

import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.Charset;
import java.security.Key;
import java.security.KeyPair;
import java.security.PrivateKey;
import java.security.PublicKey;

public class SmJWTSigner implements JWTSigner {

    private Charset charset;

    private final HMac hMac;

    private final Sign sign;

    public SmJWTSigner(String algorithm, byte[] key) {
        this(algorithm, (Key) (new SecretKeySpec(key, algorithm)));
    }

    public SmJWTSigner(String algorithm, Key key) {
        this.charset = CharsetUtil.CHARSET_UTF_8;
        if (algorithm.equalsIgnoreCase(SmAlgorithm.SM2.getValue())) {
            this.hMac = new HMac(algorithm, key);
            PublicKey publicKey = key instanceof PublicKey ? (PublicKey) key : null;
            PrivateKey privateKey = key instanceof PrivateKey ? (PrivateKey) key : null;
            this.sign = new Sign(algorithm, privateKey, publicKey);

        } else if (algorithm.equalsIgnoreCase(SmAlgorithm.SM3.getValue())) {
            this.hMac = new HMac(algorithm, key);
            this.sign = null;
        } else {
            this.hMac = createSm4HMac(key);
            this.sign = null;
        }

    }

    public SmJWTSigner(String algorithm, KeyPair keyPair) {
        this.charset = CharsetUtil.CHARSET_UTF_8;
        this.sign = new Sign(algorithm, keyPair);
        this.hMac = null;
    }

    public SmJWTSigner setCharset(Charset charset) {
        this.charset = charset;
        return this;
    }

    protected HMac createSm4HMac(Key key) {
        return new HMac(new SM4MacEngine((CipherParameters) new KeyParameter(key.getEncoded())));
    }

    public String digestSign(String headerBase64, String payloadBase64) {
        return this.hMac.digestBase64(StrUtil.format("{}.{}", new Object[]{headerBase64, payloadBase64}), this.charset, true);
    }

    public boolean digestVerify(String headerBase64, String payloadBase64, String signBase64) {
        String sign = this.sign(headerBase64, payloadBase64);
        return this.hMac.verify(StrUtil.bytes(sign, this.charset), StrUtil.bytes(signBase64, this.charset));
    }

    public String asymmetricSign(String headerBase64, String payloadBase64) {
        return Base64.encodeUrlSafe(this.sign.sign(StrUtil.format("{}.{}", new Object[]{headerBase64, payloadBase64})));
    }

    public boolean asymmetricVerify(String headerBase64, String payloadBase64, String signBase64) {
        return this.sign.verify(StrUtil.bytes(StrUtil.format("{}.{}", new Object[]{headerBase64, payloadBase64}), this.charset), Base64.decode(signBase64));
    }

    @Override
    public String sign(String s, String s1) {
        if (getAlgorithm().equalsIgnoreCase(SmAlgorithm.SM2.getValue())) {
            return asymmetricSign(s, s1);
        } else if (getAlgorithm().equalsIgnoreCase(SmAlgorithm.SM3.getValue())) {
            return digestSign(s, s1);
        } else {
            return digestSign(s, s1);
        }
    }

    @Override
    public boolean verify(String s, String s1, String s2) {
        if (getAlgorithm().equalsIgnoreCase(SmAlgorithm.SM2.getValue())) {
            return asymmetricVerify(s, s1, s2);
        } else if (getAlgorithm().equalsIgnoreCase(SmAlgorithm.SM3.getValue())) {
            return digestVerify(s, s1, s2);
        } else {
            return digestVerify(s, s1, s2);
        }
    }

    @Override
    public String getAlgorithm() {
        return this.hMac.getAlgorithm();
    }
}
