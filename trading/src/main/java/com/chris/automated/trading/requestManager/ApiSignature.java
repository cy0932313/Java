package com.chris.automated.trading.requestManager;

/**
 * @description:
 * @author: Chris.Y
 * @create: 2019-05-19 23:10
 **/

import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Base64;
import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

class ApiSignature {
    private static final String accessKeyId = "AccessKeyId";
    private static final String signatureMethod = "SignatureMethod";
    private static final String signatureMethodValue = "HmacSHA256";
    private static final String signatureVersion = "SignatureVersion";
    private static final String signatureVersionValue = "2";
    private static final String timestamp = "Timestamp";
    private static final String signature = "Signature";

    private static final DateTimeFormatter DT_FORMAT = DateTimeFormatter
            .ofPattern("uuuu-MM-dd'T'HH:mm:ss");
    private static final ZoneId ZONE_GMT = ZoneId.of("Z");

    private static final String accessKey = "ht4tgq1e4t-c6cecdb5-8d573de4-c1e0e" ;
    private static final String secretKey = "9a6b487d-5a9aa8a6-06b9b1b9-3f8bf" ;
    private static final String host = "api.huobi.pro" ;


    void createSignature(String method,String uri, UrlParamsBuilder builder) {
        StringBuilder sb = new StringBuilder(1024);

        if (accessKey == null || "".equals(accessKey) || secretKey == null || "".equals(secretKey)) {
            System.out.println("API key and secret key are required");
        }

        sb.append(method.toUpperCase()).append('\n')
                .append(host.toLowerCase()).append('\n')
                .append(uri).append('\n');

        builder.putToUrl(accessKeyId, accessKey)
                .putToUrl(signatureVersion, signatureVersionValue)
                .putToUrl(signatureMethod, signatureMethodValue)
                .putToUrl(timestamp, gmtNow());

        sb.append(builder.buildSignature());
        Mac hmacSha256 = null;
        try {
            hmacSha256 = Mac.getInstance(signatureMethodValue);
            SecretKeySpec secKey = new SecretKeySpec(secretKey.getBytes(StandardCharsets.UTF_8),
                    signatureMethodValue);
            hmacSha256.init(secKey);
        } catch (NoSuchAlgorithmException e) {
            System.out.println( "[Signature] No such algorithm: " + e.getMessage());
        } catch (InvalidKeyException e) {
            System.out.println( "[Signature]Invalid key: " + e.getMessage());
        }
        String payload = sb.toString();
        byte[] hash = hmacSha256.doFinal(payload.getBytes(StandardCharsets.UTF_8));

        String actualSign = Base64.getEncoder().encodeToString(hash);

        builder.putToUrl(signature, actualSign);

    }

    private static long epochNow() {
        return Instant.now().getEpochSecond();
    }

    static String gmtNow() {
        return Instant.ofEpochSecond(epochNow()).atZone(ZONE_GMT).format(DT_FORMAT);
    }
}
