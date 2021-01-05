package auto_release;

import java.util.Base64;
import java.util.UUID;
import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import net.sf.json.JSONObject;

/**
 * 
 * CpdailyExtension �������ɼ���ֵ�Ĺ�����
 *
 * @author kit chen
 * @github https://github.com/meethigher
 * @blog https://meethigher.top
 *
 */
public class CpdailyExtension {
	// �㷨��ģʽ��DES��3DES��AES��RC
	private static final String MODE_ALGORITHM = "DES";
	// �㷨�ı�׼ת������
	private static final String NAME = "DES/CBC/PKCS5Padding";
	// �����ʽ
	private static final String CHARSET = "UTF-8";
	// ����
	private static final String TEXT = "abcde";
	// ԭʼ��Կ
	private static final String KEY = "ST83=@XV";
	// ��ʼ�����������IV
	private static byte[] iv = { 1, 2, 3, 4, 5, 6, 7, 8 };

	/**
	 * Base64����
	 * 
	 * @param bytes
	 * @return String
	 */
	public static String Base64Encrypt(byte[] bytes) {
		return Base64.getEncoder().encodeToString(bytes);
	}

	/**
	 * Base64����
	 * 
	 * @param bytes
	 * @return byte[]
	 */
	public static byte[] Base64Decrypt(byte[] bytes) {
		return Base64.getDecoder().decode(bytes);
	}

	/**
	 * DES����
	 * 
	 * @param text
	 * @param key
	 * @param charset
	 * @return String ����Base64����������
	 * @throws Exception
	 */
	public static String DESEncrypt(String text, String key, String charset) throws Exception {
		// ͨ���������ֽ����鹹��һ����Կ
		SecretKeySpec sks = new SecretKeySpec(key.getBytes(charset), MODE_ALGORITHM);
		// ʹ��IV�������
		IvParameterSpec ivPS = new IvParameterSpec(iv);
		// 1.��ȡ�ӽ��ܵ��㷨������
		Cipher cipher = Cipher.getInstance(NAME);
		// 2.�Թ�������г�ʼ��
		cipher.init(Cipher.ENCRYPT_MODE, sks, ivPS);
		// 3.�ü��ܹ������������Ľ��м���
		byte[] doFinal = cipher.doFinal(text.getBytes(charset));
		// ��ֹ�������룬���Բ���Base64����
		return Base64Encrypt(doFinal);

	}

	/**
	 * DES����
	 * 
	 * @param text
	 * @param key
	 * @param charset
	 * @return String
	 * @throws Exception
	 */
	public static String DESDecrypt(byte[] text, String key, String charset) throws Exception {
		// �Ƚ���Base64����
		text = Base64Decrypt(text);
		// ͨ���������ֽ����鹹��һ����Կ
		SecretKeySpec sks = new SecretKeySpec(key.getBytes(charset), MODE_ALGORITHM);
		// ʹ��IV�������
		IvParameterSpec ivPS = new IvParameterSpec(iv);
		// 1.��ȡ�ӽ��ܵ��㷨������
		Cipher cipher = Cipher.getInstance(NAME);
		// 2.�Թ�������г�ʼ��
		cipher.init(Cipher.DECRYPT_MODE, sks, ivPS);
		// 3.�ü��ܹ������������Ľ��н���
		return new String(cipher.doFinal(text));
	}

	/**
	 * ����CpdailyExtension
	 * 
	 * @param id
	 * @return String
	 */
	public static String generateCpdailyExtension(String id) {
		JSONObject object = new JSONObject();
		object.put("systemName", "android");
		object.put("systemVersion", "11");
		object.put("model", "MI 11");
		object.put("deviceId", UUID.randomUUID().toString());
		object.put("appVersion", "8.1.11");
		// �廪��ѧˮľ�껪�ľ�γ�ȣ����д��
		object.put("lon", 116.32284422133253);
		object.put("lat", 40.00301874717021);
		// ѧ��
		object.put("userId", id);
		try {
			return DESEncrypt(object.toString(), KEY, CHARSET);
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("����CpdailyExtension����");
			return null;
		}
	}
}
