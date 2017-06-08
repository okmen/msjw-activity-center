package cn.activity.utils;

import com.alibaba.fastjson.JSONObject;

import cn.activity.bean.NormalApptInfoVo;
import cn.sdk.webservice.WebServiceClient;

/**
 * 调用第三方封装
 * @author Mbenben
 *
 */
@SuppressWarnings(value="all")
public class TransferThirdParty {
	
	/**
	 * 获取预约场次信息
	 * @param sourceOfCertification 获取来源
	 * @return
	 * @throws Exception
	 */
	public static JSONObject getNormalApptDate(String sourceOfCertification, String url, String method,
			String userId, String userPwd, String key) throws Exception{
		
		String interfaceNumber = "dbjq04";	//接口编号
		
		//拼装xml数据
		StringBuffer sb = new StringBuffer();
		sb.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?><request>")
		.append("<yyly>").append(sourceOfCertification).append("</yyly>")		//C微信,Z支付宝
		.append("</request>");
		
		JSONObject respJson = WebServiceClient.getInstance().requestWebService(url, method, interfaceNumber, sb.toString(), userId, userPwd, key);
		
		return respJson;
	}
	
	/**
	 * 获取指定场次个人配额信息
	 * @param sourceOfCertification 获取来源
	 * @return
	 * @throws Exception
	 */
	public static JSONObject getQuotaInfoByApptDate(String apptDate, String apptDistrict, String sourceOfCertification, String url, String method,
			String userId, String userPwd, String key) throws Exception{
		
		String interfaceNumber = "dbjq05";	//接口编号
		
		//拼装xml数据
		StringBuffer sb = new StringBuffer();
		sb.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?><request>")
		.append("<yyrq>").append(apptDate).append("</yyrq>")
		.append("<yyqy>").append(apptDistrict).append("</yyqy>")
		.append("<yyly>").append(sourceOfCertification).append("</yyly>")
		.append("</request>");
		
		JSONObject respJson = WebServiceClient.getInstance().requestWebService(url, method, interfaceNumber, sb.toString(), userId, userPwd, key);
		return respJson;
	}
	
	/**
	 * 个人预约信息写入
	 * @param info 个人预约信息
	 * @param sourceOfCertification 获取来源
	 * @param openId 微信公众号唯一标识
	 * @throws Exception
	 */
	public static JSONObject addNormalApptInfo(NormalApptInfoVo info, String sourceOfCertification, String openId, String url, String method,
			String userId, String userPwd, String key) throws Exception{
		
		String interfaceNumber = "dbjq01";	//接口编号
		
		//拼装xml数据
		StringBuffer sb = new StringBuffer();
		sb.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?><request>")
		.append("<hphm>").append(info.getPlateNo()).append("</hphm>")		//车牌号码
		.append("<hpzl>").append(info.getPlateType()).append("</hpzl>")		//车牌种类
		.append("<cjh4>").append(info.getVinLastFour()).append("</cjh4>")	//车架号后4位                   
		.append("<sjhm>").append(info.getMobilePhone()).append("</sjhm>")	//手机号码                              
		.append("<yyqy>").append(info.getApptDistrict()).append("</yyqy>")	//预约片区		1-梅沙片区,2-大鹏片区                         
		.append("<yyrq>").append(info.getApptDate()).append("</yyrq>")		//预约日期                     
		.append("<yysjd>").append(info.getApptDistrict()).append("</yysjd>")//预约时间段	1-0点到12点,2-12点到24点
		.append("<yyly>").append(sourceOfCertification).append("</yyly>")	//获取来源
		.append("<openid>").append(openId).append("</openid>")				//微信公众号唯一标识			
		.append("<yyjs>").append("1").append("</yyjs>")						//预约角色		默认1-个人
		.append("</request>");
		
		JSONObject respJson = WebServiceClient.getInstance().requestWebService(url, method, interfaceNumber, sb.toString(), userId, userPwd, key);
		return respJson;
	}
	
	/**
     * 查询个人预约信息
     * @param plateNo	号牌号码
     * @param plateType 号牌种类
     * @param vinLastFour 车架后4位
     * @param mobilePhone 手机号码
     * @param sourceOfCertification 查询来源
	 * @throws Exception 
     */
	public static JSONObject getApptHistoryRecord(String plateNo, String plateType, String vinLastFour, String mobilePhone, 
			String sourceOfCertification, String url, String method, String userId, String userPwd, String key) throws Exception {
		String interfaceNumber = "dbjq03";	//接口编号
		
		//拼装xml数据
		StringBuffer sb = new StringBuffer();
		sb.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?><request>")
		.append("<hphm>").append(plateNo).append("</hphm>")					//号牌号码
		.append("<hpzl>").append(plateType).append("</hpzl>")				//号牌种类
		.append("<cjh4>").append(vinLastFour).append("</cjh4>")				//车架后4位
		.append("<sjhm>").append(mobilePhone).append("</sjhm>")				//手机号码
		.append("<yyly>").append(sourceOfCertification).append("</yyly>")	//查询来源
		.append("</request>");

		JSONObject respJson = WebServiceClient.getInstance().requestWebService(url, method, interfaceNumber, sb.toString(), userId, userPwd, key);
		return respJson;
	}
	
	/**
     * 取消个人预约信息
     * @param apptId 预约编号
     * @param cancelReason 取消原因
	 * @param sourceOfCertification 获取来源
	 * @param apptRole 预约角色
	 * @throws Exception 
	 */
	public static JSONObject cancelNormalApptInfo(String apptId, String cancelReason, String sourceOfCertification,
			String url, String method, String userId, String userPwd, String key) throws Exception {
		String interfaceNumber = "dbjq02";	//接口编号
		
		//拼装xml数据
		StringBuffer sb = new StringBuffer();
		sb.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?><request>")
		.append("<yyid>").append(apptId).append("</yyid>")					//预约编号
		.append("<qxyy>").append(cancelReason).append("</qxyy>")			//取消原因
		.append("<yyly>").append(sourceOfCertification).append("</yyly>")	//获取来源
		.append("<yyjs>").append("1").append("</yyjs>")						//预约角色		1-个人
		.append("</request>");
		
		JSONObject respJson = WebServiceClient.getInstance().requestWebService(url, method, interfaceNumber, sb.toString(), userId, userPwd, key);
		return respJson;
	}
	
	/**
	 * 临时个人预约信息写入
	 * @Description: TODO(临时个人预约信息写入)
	 * @param info 个人预约信息
	 * @param sourceOfCertification 获取来源
	 * @param openId 微信公众号唯一标识
	 * @throws Exception
	 */
	public static JSONObject addTempApptInfo(NormalApptInfoVo info, String sourceOfCertification, String openId,
			String url, String method, String userId, String userPwd, String key) throws Exception {
		String interfaceNumber = "dbjq06";	//接口编号
		
		//拼装xml数据
		StringBuffer sb = new StringBuffer();
		sb.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?><request>")
		.append("<hphm>").append(info.getPlateNo()).append("</hphm>")		//车牌号码
		.append("<hpzl>").append(info.getPlateType()).append("</hpzl>")		//车牌种类
		.append("<cjh4>").append(info.getVinLastFour()).append("</cjh4>")	//车架号后4位                   
		.append("<sjhm>").append(info.getMobilePhone()).append("</sjhm>")	//手机号码                              
		.append("<yyqy>").append(info.getApptDistrict()).append("</yyqy>")	//预约片区		1-梅沙片区,2-大鹏片区                         
		.append("<yyrq>").append(info.getApptDate()).append("</yyrq>")		//预约日期                     
		.append("<yysjd>").append(info.getApptDistrict()).append("</yysjd>")//预约时间段	1-0点到12点,2-12点到24点
		.append("<yyly>").append(sourceOfCertification).append("</yyly>")	//获取来源
		.append("<openid>").append(openId).append("</openid>")				//微信公众号唯一标识			
		.append("<yyjs>").append("1").append("</yyjs>")						//预约角色		默认1-个人
		.append("</request>");
		
		JSONObject respJson = WebServiceClient.getInstance().requestWebService(url, method, interfaceNumber, sb.toString(), userId, userPwd, key);
		return respJson;
	}
}
