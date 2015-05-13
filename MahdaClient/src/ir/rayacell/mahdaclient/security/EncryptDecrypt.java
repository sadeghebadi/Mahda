package ir.rayacell.mahdaclient.security;

import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.SecretKeySpec;

public class EncryptDecrypt {

	public static Cipher getCipher(String synchro1, String synchro2,
			String synchro3, String synchro4, boolean isEncryptMode)
			throws NoSuchAlgorithmException, NoSuchPaddingException,
			InvalidKeyException {
		byte raw[] = (synchro1 + synchro2 + synchro3 + synchro4).getBytes();
		SecretKeySpec skeySpec = new SecretKeySpec(raw, "AES");
		Cipher cipher = Cipher.getInstance("AES");
		if (isEncryptMode) {
			cipher.init(Cipher.ENCRYPT_MODE, skeySpec);
		} else {
			cipher.init(Cipher.DECRYPT_MODE, skeySpec);
		}
		return cipher;
	}

	public static byte[] hexToByte(String hex) {
		byte bts[] = new byte[hex.length() / 2];
		for (int i = 0; i < bts.length; i++) {
			bts[i] = (byte) Integer.parseInt(hex.substring(2 * i, 2 * i + 2),
					16);
		}

		return bts;
	}

	public static String toHexString(byte bytes[]) {
		StringBuffer retString = new StringBuffer();
		for (int i = 0; i < bytes.length; i++)
			retString.append(Integer.toHexString(256 + (bytes[i] & 0xff))
					.substring(1));

		return retString.toString();
	}

	public static String encrypt(String text) throws InvalidKeyException,
			NoSuchAlgorithmException, NoSuchPaddingException,
			IllegalBlockSizeException, BadPaddingException {
		javax.crypto.Cipher cipher = getCipher("bb5", "1860", "17a74", "213f",
				true);
		return toHexString(cipher.doFinal(text.getBytes()));
	}
	public static String decrypt(String text) throws IllegalBlockSizeException,
			BadPaddingException, InvalidKeyException, NoSuchAlgorithmException,
			NoSuchPaddingException {
		javax.crypto.Cipher cipher = getCipher("bb5", "1860", "17a74", "213f",
				false);
		String st = new String(cipher.doFinal(hexToByte(text)));
		return st;
	}

	public static void main(String[] str) throws Exception {
		String secretText = "javalearning";
		String enSecretText = encrypt(secretText);

		System.out.println("\"" + secretText + "\" after encryption---->"
				+ enSecretText);

		String deSecretText = decrypt(enSecretText);
		System.out.println("After decryption --->" + deSecretText);
	}
	
	public static int ASCII_START_POS = 65;
	public static int ALPHABET_COUNT = 26;

//	public static String cipher(String plaintext, int shift) {
//	    // only interested in the alphabet
//	    StringBuilder ciphertext = new StringBuilder();
//
//	    for (char c : plaintext.toCharArray())
//	        ciphertext.append((char) (((c - ASCII_START_POS + shift) % ALPHABET_COUNT) + ASCII_START_POS));
//
//	    return ciphertext.toString();
//	}

}
