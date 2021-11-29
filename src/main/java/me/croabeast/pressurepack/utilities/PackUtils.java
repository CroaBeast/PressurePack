package me.croabeast.pressurepack.utilities;

import jakarta.xml.bind.*;
import org.apache.logging.log4j.util.*;

import java.io.*;
import java.net.*;
import java.security.*;
import java.util.*;

public class PackUtils {

    public static byte[] parseHex(String code) {
        return DatatypeConverter.parseHexBinary(code);
    }

    public static void perform(String url, String hash, TriConsumer<byte[], byte[], Boolean> consumer) throws Exception {
        MessageDigest digest = MessageDigest.getInstance("SHA-1");
        InputStream fis = new URL(url).openStream();
        int n = 0;
        byte[] buffer = new byte[8192];
        while (n != -1) {
            n = fis.read(buffer);
            if (n > 0) digest.update(buffer, 0, n);
        }
        fis.close();
        final byte[] urlBytes = digest.digest();
        final byte[] configBytes = parseHex(hash);
        consumer.accept(urlBytes, configBytes, Arrays.equals(urlBytes, configBytes));
    }
}
