package cn.activity.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 时间格式转换工具类
 * @author Mbenben
 *
 */

public class ActivityDateUtil {
	
	
	//10-06-17 00:00:00.0 转换为 2017-06-10
	public static String dateConvert(String curDate) throws ParseException{
		SimpleDateFormat sdf1 = new SimpleDateFormat("dd-MM-yy");
		SimpleDateFormat sdf2 = new SimpleDateFormat("yyyy-MM-dd");
		
		String[] split = curDate.split(" ");
		Date d = sdf1.parse(split[0]);
		String newDate = sdf2.format(d);
		return newDate;
	}
	

	public static final String[] MONTH_ADDR = 
		{"N/A","JAN","FEB","MAR","APR","MAY","JUN","JUL","AUG","SEP","OCT","NOV","DEC"};
	
	public static String DateFormat(String dateStr){
		String digMonth = null;
		String[] strs = dateStr.split("-");
		String strYear = strs[0];
		String strMonth = strs[1];
		String engMonth = strMonth;
		
		if(engMonth.equals(MONTH_ADDR[1])){
			digMonth = "01";
		}
		else if(engMonth.equals(MONTH_ADDR[2])){
			digMonth = "02";
		}
		else if(engMonth.equals(MONTH_ADDR[3])){
			digMonth = "03";
		}
		else if(engMonth.equals(MONTH_ADDR[4])){
			digMonth = "04";
		}
		else if(engMonth.equals(MONTH_ADDR[5])){
			digMonth = "05";
		}
		else if(engMonth.equals(MONTH_ADDR[6])){
			digMonth = "06";
		}
		else if(engMonth.equals(MONTH_ADDR[7])){
			digMonth = "07";
		}
		else if(engMonth.equals(MONTH_ADDR[8])){
			digMonth = "08";
		}
		else if(engMonth.equals(MONTH_ADDR[9])){
			digMonth = "09";
		}
		else if(engMonth.equals(MONTH_ADDR[10])){
			digMonth = "10";
		}
		else if(engMonth.equals(MONTH_ADDR[11])){
			digMonth = "11";
		}
		else if(engMonth.equals(MONTH_ADDR[12])){
			digMonth = "12";
		}
		else{
			return "ActivityDateUtil时间格式转换不匹配!";
		}
		
		String dateFormat = dateStr.replace(strMonth, digMonth).replace(strYear, "20"+strYear);
		return dateFormat;
	}
}
