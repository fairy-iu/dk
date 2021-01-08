package auto_cookie;

import java.util.Calendar;
import java.util.Date;
import java.util.Scanner;

/**
 * 
 * Main
 *
 * @author kit chen
 * @github https://github.com/meethigher
 * @blog https://meethigher.top
 * @time 2021��1��6��
 */
public class Main {
	private static int sh;
	private static int eh;
	static {
		System.out.println("��ȡ�����ļ�..");
		System.out.println("ѧ�ţ�" + Data.id);
		System.out.println("��ַ��" + Data.address);
		System.out.println("��ǰʱ�䣺" + new Date().toLocaleString());
		Scanner scanner = new Scanner(System.in);
		System.out.print("����������MOD_AUTH_CAS��");
		Data.modAuthCas = scanner.nextLine();
		System.out.print("�����뿪ʼ����Сʱ��");
		sh = scanner.nextInt();
		System.out.print("�������������Сʱ��");
		eh = scanner.nextInt();
		scanner.close();
		System.out.println("2021-01-06�汾��������...");
	}

	public static String submit() {
		String[] form = CpDaily.getForm();
		if ("yes".equals(form[0])) {
			if ("1".equals(form[1])) {
				System.out.println("�����Ѿ��ύ���ˣ�");
				return "submited";
			} else {
				String schoolTaskWid = CpDaily.getSchool(form);
				String message = CpDaily.submit(form, schoolTaskWid, Data.address);
				if ("SUCCESS".equals(message)) {
					System.out.println("���ձ��ѳɹ��ύ��");
					System.out.println(SendMail.send(new String[] { "�ʾ��ύ�ɹ�֪ͨ", "ʱ�䣺" + new Date().toLocaleString()
							+ "\n" + "ѧ�ţ�" + Data.id + "\n��ַ��" + Data.address + "\n�ύ�ؼ��֣�" + Data.key }));
					return "success";
				} else {
					System.out.println("���ձ��ύʧ�ܣ�ʧ�ܱ���->" + message);
					System.out.println(SendMail.send(new String[] { "�ʾ��ύʧ��֪ͨ", "ʱ�䣺" + new Date().toLocaleString()
							+ "\n" + "ʧ�ܣ�" + message }));
					return "error";
				}
			}
		} else {
			System.out.println("��δ�������±���");
			return "noform";
		}
	}

	/*
	 * ��ȥ1����ȥ���������е�ʱ��
	 */
	public static int getDelay(String result, Calendar c) {
		int delay;
		if ("success".equals(result) || "error".equals(result)) {
			delay = 1000 * 60 * 60 * 24 - 1000 * 1;
		} else if ("noform".equals(result)) {
			delay = 1000 * 60 * 30;
		} else {
			delay = (24 - c.get(Calendar.HOUR_OF_DAY) + sh) * 60 * 60 * 1000;
		}
		System.out.println("��������" + delay / 1000 / 60 + "���Ӻ��ٴ�ִ��..");
		return delay;
	}

	public static void main(String[] args) {
		// �������ֻỰ�߳�
		new Thread(() -> {
			while (true) {
				HttpUtil.sendGet(Data.keepingUrl, Data.getHeaders());
				try {
					Thread.sleep(1000 * 60 * 10);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}).start();
		// �����ؼ��߳�
		new Thread(() -> {
			Calendar c;
			String result = null;
			boolean flag = false;
			while (true) {
				c = Calendar.getInstance();
				int currentHour = c.get(Calendar.HOUR_OF_DAY);
				if (sh <= currentHour && currentHour <= eh) {
					flag = true;
				} else {
					flag = false;
				}
				if (flag)
					result = submit();
				try {
					Thread.sleep(getDelay(result, c));
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}).start();
	}
}
