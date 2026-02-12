package com.shopwithanish.ecommerse.application.AllAboutSecurity;

import org.springframework.stereotype.Component;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

@Component
public class RazorpaySignatureUtil {

    public String generateSignature(String data, String secret) {

        try {

            Mac mac = Mac.getInstance("HmacSHA256");

            SecretKeySpec key =
                    new SecretKeySpec(secret.getBytes(), "HmacSHA256");

            mac.init(key);

            byte[] rawHmac = mac.doFinal(data.getBytes());

            StringBuilder hex = new StringBuilder();

            for (byte b : rawHmac) {
                hex.append(String.format("%02x", b));
            }

            return hex.toString();

        } catch (Exception e) {
            throw new RuntimeException("Signature generation failed", e);
        }
    }
}
