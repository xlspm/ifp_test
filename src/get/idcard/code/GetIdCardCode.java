package get.idcard.code;

import java.time.LocalDate;
import java.util.Scanner;
import java.util.regex.Pattern;

/**
 * @author April
 * 根据身份证前17位求身份证的校验码
 */
public class GetIdCardCode {

	@SuppressWarnings("unused")
	private static ResuletFields calcCheckCode(int[] coef, String[] checkCode, String idNoStr) {
		String idNo = null;		//最终的身份证号码
		Integer idSum = 0;	//校验和 (不能附null，否则会出现空指针异常)
		String checkStr = null;	//校验位字符
		
		String[] noStr = idNoStr.split("");	//将输入的前17位字符串身份证号一个一个拆开
		int[] noInt = new int[noStr.length];	//用于存放身份证数字数组
		
		//1、循环字符串数组并将每一位字符转换为数字并添加到数字数组
		try {
			for (int i = 0; i < noStr.length; i++) {
				Pattern pattern = Pattern.compile("[0-9]*");
				if (!pattern.matcher(noStr[i]).matches()) {
					return new ResuletFields(0,"您输入的身份证号码中存在非法字符，请重新输入...");
				}
				//若全是数字则转给noInt[]
				noInt[i] = Integer.parseInt(noStr[i]);
			}
		} catch (NumberFormatException e) {
			return new ResuletFields(0,"您没有输入，请输入...");
		}
		
		//2、如果格式满足要求就开始计算
		if (noInt.length!=17) {
			return new ResuletFields(0,"位数不对，必须是17位，请重新输入...");
		}else if(noInt[0]==0 || noInt[0]==7 || noInt[0]==8 || noInt[0]==9 || noInt[0]==1&noInt[1]==0){
			return new ResuletFields(0,"不能是0，7，8，9，10开头，请重新输入...");
		}else if(yearsFlay(noInt)){
			//判断出生年份是否正确
			return new ResuletFields(0,"出生年份有问题，请重新输入...");
		}else if(monthFlay(noInt)){
			//判断出生月份是否正确
			return new ResuletFields(0,"出生月份有问题，请重新输入...");
		}else if(dayFlay(noInt)){
			//判断出生日期是否正确
			return new ResuletFields(0,"出生日期有问题，请重新输入...");
		}else{
			//将每一位身份证数字与对应的每一位系数相乘并生成校验和
			for (int i = 0; i < coef.length; i++) {
				idSum += (coef[i]*noInt[i]);	//核心算法
			}
			//3、先判断校验和是否为空，避免空指针
			if (idSum == null) {
				System.out.println("校验和为空，阻断拼接！！");
			} else{
				//将校验和与11相与得到余数
				int rem = idSum % 11;
				//循环校验码数组
				for (int i = 0; i < checkCode.length; i++) {
					//如果余数和当前下标相等就将对应的校验码赋给“校验位数字”
					if (rem == i) {
						checkStr = checkCode[i];	//对比填值
					}
				}
			}
		}
		
		//4、拼接身份证字符串（前17位+校验位字符）
		idNo = idNoStr+checkStr;
		return new ResuletFields(1,idNo);	//（错误码[非0及true]，消息主题）
	}
	//年份校验
	private static boolean yearsFlay(int[] noInt) {
		int getYear = Integer.parseInt(noInt[6]+""+noInt[7]+""+noInt[8]+""+noInt[9]);
		LocalDate taday = LocalDate.now();
		if (getYear>taday.getYear()) {
			return true;
		}
		return false;
	}
	//月份校验
	private static boolean monthFlay(int[] noInt) {
		int getMouth = Integer.parseInt(noInt[10]+""+noInt[11]);
		if (getMouth<1 || getMouth>12) {
			return true;
		}
		return false;
	}
	//日期校验
	private static boolean dayFlay(int[] noInt) {
		int getDay = Integer.parseInt(noInt[12]+""+noInt[13]);
		if (getDay<1 || getDay>31) {
			return true;
		}
		return false;
	}

	@SuppressWarnings("resource")
	public static void main(String[] args) {
		//系数表
		int[] coef = {7,9,10,5,8,4,2,1,6,3,7,9,10,5,8,4,2};
		//校验码表
		String[] checkCode = {"1","0","X","9","8","7","6","5","4","3","2"};
		System.out.println("请输入身份证前17位。");
		
		while (true) {
			Scanner input = new Scanner(System.in);
			String idNoStr = input.nextLine();	//存为字符串是因为后面需要按字符串拆分位数组
			
			if ("exit".equals(idNoStr)) {
				System.out.println("已退出。");
				break;
			}
			//计算开始
			ResuletFields returnMsg = calcCheckCode(coef,checkCode,idNoStr);
			//结果判断
			if (returnMsg.getCode()==0) {
				System.err.println("程序出错！！！       原因："+returnMsg.getMsg());
			} else {
				System.out.println("最终的身份证号码位：\n\t"+returnMsg.getMsg());
			}
		}
	}
}
 