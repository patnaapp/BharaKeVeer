package appthemartyrs.in.nic.bih.onlineapp.appthemartyrs.util;

import java.security.spec.KeySpec;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;
import javax.crypto.spec.IvParameterSpec;

public class Encrypter {

	private static byte[] sharedkey = { 0x01, 0x02, 0x03, 0x05, 0x07, 0x0B,
			0x0D, 0x11, 0x12, 0x11, 0x0D, 0x0B, 0x07, 0x02, 0x04, 0x08, 0x01,
			0x02, 0x03, 0x05, 0x07, 0x0B, 0x0D, 0x11 };

	private static byte[] sharedvector = { 0x01, 0x02, 0x03, 0x05, 0x07, 0x0B,
			0x0D, 0x11 };

	/*
	 * public static String encrypt(String plaintext) throws Exception { Cipher
	 * c = Cipher.getInstance("DESede/CBC/PKCS5Padding");
	 * c.init(Cipher.ENCRYPT_MODE, new SecretKeySpec(sharedkey, "DESede"), new
	 * IvParameterSpec(sharedvector)); byte[] encrypted =
	 * c.doFinal(plaintext.getBytes("UTF-8")); String encText=
	 * Base64.encode(encrypted); return encText; }
	 */

	public String encryptText(String cipherText) throws Exception {

		String plainKey = "LlU9JJLJAu8=";
		String plainIV = "x7hkVXoObeI=";

		KeySpec ks = new DESKeySpec(plainKey.getBytes("UTF-8"));
		SecretKey key = SecretKeyFactory.getInstance("DES").generateSecret(ks);

		/*
		 * IvParameterSpec iv = new IvParameterSpec(
		 * org.apache.commons.codec.binary
		 * .Hex.decodeHex(plainIV.toCharArray()));
		 */

		IvParameterSpec iv = new IvParameterSpec(plainIV.getBytes());

		Cipher cipher = Cipher.getInstance("DES/CBC/PKCS5Padding");
		cipher.init(Cipher.ENCRYPT_MODE, key, iv);

		byte[] decoded = cipher.doFinal(cipherText.getBytes("UTF-8"));

		return new String(decoded, "UTF-8");
	}
}
