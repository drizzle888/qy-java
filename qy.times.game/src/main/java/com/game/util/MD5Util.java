package com.game.util;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

import org.apache.commons.lang3.StringUtils;

public class MD5Util {

	public static final String privateKey = "passwd@buoumall.com";

	public static final String gameKey = "a6343d3fa201ea25";
	private static String encodingCharset = "UTF-8";
	
	public static boolean check_hmac_md5(String aValue, String md5) {
		if (StringUtils.isNotBlank(aValue) && StringUtils.isNotBlank(md5)) {
			String h = hmac_md5(aValue);
			return md5.equals(h);
		} else {
			return false;
		}
		
	}
	public static String hmac_md5(String aValue) {
		return hmac_md5(aValue, gameKey);
	}
	public static String hmac_md5(String aValue, String key) {
		byte k_ipad[] = new byte[64];
		byte k_opad[] = new byte[64];
		byte keyb[];
		byte value[];
		try {
			keyb = key.getBytes(encodingCharset);
			value = aValue.getBytes(encodingCharset);
		} catch (UnsupportedEncodingException e) {
			keyb = key.getBytes();
			value = aValue.getBytes();
		}

		Arrays.fill(k_ipad, keyb.length, 64, (byte) 54);
		Arrays.fill(k_opad, keyb.length, 64, (byte) 92);
		for (int i = 0; i < keyb.length; i++) {
			k_ipad[i] = (byte) (keyb[i] ^ 0x36);
			k_opad[i] = (byte) (keyb[i] ^ 0x5c);
		}

		MessageDigest md = null;
		try {
			md = MessageDigest.getInstance("MD5");
		} catch (NoSuchAlgorithmException e) {

			return null;
		}
		md.update(k_ipad);
		md.update(value);
		byte dg[] = md.digest();
		md.reset();
		md.update(k_opad);
		md.update(dg, 0, 16);
		dg = md.digest();
		return toHex(dg);
	}

	public static String toHex(byte input[]) {
		if (input == null)
			return null;
		StringBuffer output = new StringBuffer(input.length * 2);
		for (int i = 0; i < input.length; i++) {
			int current = input[i] & 0xff;
			if (current < 16)
				output.append("0");
			output.append(Integer.toString(current, 16));
		}

		return output.toString();
	}

	/**
	 * 
	 * @param args
	 * @return
	 */
	public static String hmac_md5(String[] args) {
		if (args == null || args.length == 0) {
			return (null);
		}
		StringBuffer str = new StringBuffer();
		for (int i = 0; i < args.length; i++) {
			str.append(args[i]);
		}
		return (hmac_md5(str.toString()));
	}
	
	public static String hmac_sha1(String value) { 
		return hmac_sha1(value, gameKey);
	}

	public static String md5(String sourceStr){
		String signStr = "";
		try {
			byte[] bytes = sourceStr.getBytes("utf-8");
			MessageDigest md5 = MessageDigest.getInstance("MD5"); md5.update(bytes);
			byte[] md5Byte = md5.digest();
			if(md5Byte != null){
			signStr = HexBin.encode(md5Byte); }
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return signStr;
	}
	
	public static String hmac_sha1(String value, String key) {  
        try {  
            // Get an hmac_sha1 key from the raw key bytes  
            byte[] keyBytes = key.getBytes();             
            SecretKeySpec signingKey = new SecretKeySpec(keyBytes, "HmacSHA1");  
  
            // Get an hmac_sha1 Mac instance and initialize with the signing key  
            Mac mac = Mac.getInstance("HmacSHA1");  
            mac.init(signingKey);  
  
            // Compute the hmac on input data bytes  
            byte[] rawHmac = mac.doFinal(value.getBytes());  
  
            // Convert raw bytes to Hex  
            String hexBytes = byte2hex(rawHmac);  
            return hexBytes;  
        } catch (Exception e) {  
            throw new RuntimeException(e);  
        }  
    }
	
	private static String byte2hex(final byte[] b){  
        String hs="";  
        String stmp="";  
        for (int n=0; n<b.length; n++){  
            stmp=(java.lang.Integer.toHexString(b[n] & 0xFF));  
            if (stmp.length()==1) hs=hs+"0"+stmp;  
                else hs=hs+stmp;  
        }  
        return hs;  
    }   
	
	public static void main(String[] args) {
		String s = hmac_md5("username" + "passwd");
		boolean b = check_hmac_md5("username" + "passwd", s);
		System.out.println(b);
	}

}
