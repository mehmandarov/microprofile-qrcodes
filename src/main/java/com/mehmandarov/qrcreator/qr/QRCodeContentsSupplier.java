package com.mehmandarov.qrcreator.qr;

import com.mehmandarov.qrcreator.security.SecretKeySupplier;
import org.json.JSONObject;

import javax.inject.Inject;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.Map;

public class QRCodeContentsSupplier {

    @Inject
    SecretKeySupplier keySupplier;

    public String getQRCodeContents(String id) throws InvalidKeySpecException, NoSuchAlgorithmException {
        Map<String, String> qrCodeDataMap = Map.of(
                "Name", id,
                "Key", keySupplier.generateVerificationKey(id)
        );

        return new JSONObject(qrCodeDataMap).toString();
    }
}
