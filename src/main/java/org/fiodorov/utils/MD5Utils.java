package org.fiodorov.utils;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import org.apache.commons.lang3.StringUtils;
import org.hibernate.annotations.common.util.impl.LoggerFactory;
import org.jboss.logging.Logger;

/**
 * @author rfiodorov
 *         on 5/12/17.
 */
public class MD5Utils {

    private static final Logger LOGGER = LoggerFactory.logger(MD5Utils.class);

    public static String hex(byte[] array) {
        StringBuffer sb = new StringBuffer();
        for (byte anArray : array) {
            sb.append(Integer.toHexString((anArray
                    & 0xFF) | 0x100).substring(1, 3));
        }
        return sb.toString();
    }

    public static String md5Hex (String message) {
        if(StringUtils.isBlank(message)){
            return "";
        }
        try {
            MessageDigest md =
                    MessageDigest.getInstance("MD5");
            return hex (md.digest(message.getBytes("CP1252")));
        } catch (NoSuchAlgorithmException | UnsupportedEncodingException e) {
            LOGGER.error(e.getMessage(), e);
        }
        return "";
    }
}

