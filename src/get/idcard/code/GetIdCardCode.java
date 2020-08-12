package get.idcard.code;

import java.time.LocalDate;
import java.util.Scanner;
import java.util.regex.Pattern;

/**
 * @author April
 * �������֤ǰ17λ�����֤��У����
 */
public class GetIdCardCode {

	@SuppressWarnings("unused")
	private static ResuletFields calcCheckCode(int[] coef, String[] checkCode, String idNoStr) {
		String idNo = null;		//���յ����֤����
		Integer idSum = 0;	//У��� (���ܸ�null���������ֿ�ָ���쳣)
		String checkStr = null;	//У��λ�ַ�
		
		String[] noStr = idNoStr.split("");	//�������ǰ17λ�ַ������֤��һ��һ����
		int[] noInt = new int[noStr.length];	//���ڴ�����֤��������
		
		//1��ѭ���ַ������鲢��ÿһλ�ַ�ת��Ϊ���ֲ���ӵ���������
		try {
			for (int i = 0; i < noStr.length; i++) {
				Pattern pattern = Pattern.compile("[0-9]*");
				if (!pattern.matcher(noStr[i]).matches()) {
					return new ResuletFields(0,"����������֤�����д��ڷǷ��ַ�������������...");
				}
				//��ȫ��������ת��noInt[]
				noInt[i] = Integer.parseInt(noStr[i]);
			}
		} catch (NumberFormatException e) {
			return new ResuletFields(0,"��û�����룬������...");
		}
		
		//2�������ʽ����Ҫ��Ϳ�ʼ����
		if (noInt.length!=17) {
			return new ResuletFields(0,"λ�����ԣ�������17λ������������...");
		}else if(noInt[0]==0 || noInt[0]==7 || noInt[0]==8 || noInt[0]==9 || noInt[0]==1&noInt[1]==0){
			return new ResuletFields(0,"������0��7��8��9��10��ͷ������������...");
		}else if(yearsFlay(noInt)){
			//�жϳ�������Ƿ���ȷ
			return new ResuletFields(0,"������������⣬����������...");
		}else if(monthFlay(noInt)){
			//�жϳ����·��Ƿ���ȷ
			return new ResuletFields(0,"�����·������⣬����������...");
		}else if(dayFlay(noInt)){
			//�жϳ��������Ƿ���ȷ
			return new ResuletFields(0,"�������������⣬����������...");
		}else{
			//��ÿһλ���֤�������Ӧ��ÿһλϵ����˲�����У���
			for (int i = 0; i < coef.length; i++) {
				idSum += (coef[i]*noInt[i]);	//�����㷨
			}
			//3�����ж�У����Ƿ�Ϊ�գ������ָ��
			if (idSum == null) {
				System.out.println("У���Ϊ�գ����ƴ�ӣ���");
			} else{
				//��У�����11����õ�����
				int rem = idSum % 11;
				//ѭ��У��������
				for (int i = 0; i < checkCode.length; i++) {
					//��������͵�ǰ�±���Ⱦͽ���Ӧ��У���븳����У��λ���֡�
					if (rem == i) {
						checkStr = checkCode[i];	//�Ա���ֵ
					}
				}
			}
		}
		
		//4��ƴ�����֤�ַ�����ǰ17λ+У��λ�ַ���
		idNo = idNoStr+checkStr;
		return new ResuletFields(1,idNo);	//��������[��0��true]����Ϣ���⣩
	}
	//���У��
	private static boolean yearsFlay(int[] noInt) {
		int getYear = Integer.parseInt(noInt[6]+""+noInt[7]+""+noInt[8]+""+noInt[9]);
		LocalDate taday = LocalDate.now();
		if (getYear>taday.getYear()) {
			return true;
		}
		return false;
	}
	//�·�У��
	private static boolean monthFlay(int[] noInt) {
		int getMouth = Integer.parseInt(noInt[10]+""+noInt[11]);
		if (getMouth<1 || getMouth>12) {
			return true;
		}
		return false;
	}
	//����У��
	private static boolean dayFlay(int[] noInt) {
		int getDay = Integer.parseInt(noInt[12]+""+noInt[13]);
		if (getDay<1 || getDay>31) {
			return true;
		}
		return false;
	}

	@SuppressWarnings("resource")
	public static void main(String[] args) {
		//ϵ����
		int[] coef = {7,9,10,5,8,4,2,1,6,3,7,9,10,5,8,4,2};
		//У�����
		String[] checkCode = {"1","0","X","9","8","7","6","5","4","3","2"};
		System.out.println("���������֤ǰ17λ��");
		
		while (true) {
			Scanner input = new Scanner(System.in);
			String idNoStr = input.nextLine();	//��Ϊ�ַ�������Ϊ������Ҫ���ַ������λ����
			
			if ("exit".equals(idNoStr)) {
				System.out.println("���˳���");
				break;
			}
			//���㿪ʼ
			ResuletFields returnMsg = calcCheckCode(coef,checkCode,idNoStr);
			//����ж�
			if (returnMsg.getCode()==0) {
				System.err.println("�����������       ԭ��"+returnMsg.getMsg());
			} else {
				System.out.println("���յ����֤����λ��\n\t"+returnMsg.getMsg());
			}
		}
	}
}
 