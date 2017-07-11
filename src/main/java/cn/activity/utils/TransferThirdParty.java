package cn.activity.utils;

import java.util.List;

import org.apache.log4j.Logger;

import com.alibaba.fastjson.JSONObject;

import cn.activity.bean.HotelApptInfoVo;
import cn.activity.bean.HotelInfoVo;
import cn.activity.bean.NormalApptInfoVo;
import cn.sdk.util.MsgCode;
import cn.sdk.webservice.WebServiceClient;

/**
 * 调用第三方封装
 * @author Mbenben
 *
 */
@SuppressWarnings(value="all")
public class TransferThirdParty {
	private static Logger log = Logger.getLogger(TransferThirdParty.class);
	/**
	 * 获取预约场次信息
	 * @Description: TODO(获取预约场次信息)
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
		.append("<yyly>").append("C").append("</yyly>")		//此处写死为C微信
		.append("</request>");
		
		log.info("getNormalApptDate 接口 xml请求参数: " + sb);
		JSONObject respJson = new JSONObject();
		try {
			respJson = WebServiceClient.getInstance().requestWebService(url, method, interfaceNumber, sb.toString(), userId, userPwd, key);
		} catch (Exception e) {
			respJson.put("code", MsgCode.webServiceCallError);
			respJson.put("msg", MsgCode.webServiceCallMsg);
			throw e;
		}
		return respJson;
	}
	
	/**
	 * 获取指定场次个人配额信息
	 * @Description: TODO(获取指定场次个人配额信息)
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
		
		log.info("getQuotaInfoByApptDate 接口 xml请求参数: " + sb);
		JSONObject respJson = new JSONObject();
		try {
			respJson = WebServiceClient.getInstance().requestWebService(url, method, interfaceNumber, sb.toString(), userId, userPwd, key);
		} catch (Exception e) {
			respJson.put("code", MsgCode.webServiceCallError);
			respJson.put("msg", MsgCode.webServiceCallMsg);
			throw e;
		}
		return respJson;
	}
	
	/**
	 * 个人预约信息写入
	 * @Description: TODO(个人预约信息写入)
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
		.append("<yysjd>").append(info.getApptInterval()).append("</yysjd>")//预约时间段	1-0点到12点,2-12点到24点
		.append("<yyly>").append(sourceOfCertification).append("</yyly>")	//获取来源
		.append("<openid>").append(openId).append("</openid>")				//微信公众号唯一标识			
		.append("<yyjs>").append("1").append("</yyjs>")						//预约角色		默认1-个人
		.append("</request>");
		
		log.info("addNormalApptInfo 接口 xml请求参数: " + sb);
		JSONObject respJson = new JSONObject();
		try {
			respJson = WebServiceClient.getInstance().requestWebService(url, method, interfaceNumber, sb.toString(), userId, userPwd, key);
		} catch (Exception e) {
			respJson.put("code", MsgCode.webServiceCallError);
			respJson.put("msg", MsgCode.webServiceCallMsg);
			throw e;
		}
		return respJson;
	}
	
	/**
     * 查询个人预约信息
     * @Description: TODO(查询个人预约信息)
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
		.append("<yyly>").append("C").append("</yyly>")	//查询来源
		.append("</request>");

		log.info("getApptHistoryRecord 接口 xml请求参数: " + sb);
		JSONObject respJson = new JSONObject();
		try {
			respJson = WebServiceClient.getInstance().requestWebService(url, method, interfaceNumber, sb.toString(), userId, userPwd, key);
		} catch (Exception e) {
			respJson.put("code", MsgCode.webServiceCallError);
			respJson.put("msg", MsgCode.webServiceCallMsg);
			throw e;
		}
		return respJson;
	}
	
	/**
     * 取消个人预约信息
     * @Description: TODO(取消个人预约信息)
     * @param apptId 预约编号
     * @param cancelReason 取消原因
	 * @param sourceOfCertification 获取来源
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
		
		log.info("cancelNormalApptInfo 接口 xml请求参数: " + sb);
		JSONObject respJson = new JSONObject();
		try {
			respJson = WebServiceClient.getInstance().requestWebService(url, method, interfaceNumber, sb.toString(), userId, userPwd, key);
		} catch (Exception e) {
			respJson.put("code", MsgCode.webServiceCallError);
			respJson.put("msg", MsgCode.webServiceCallMsg);
			throw e;
		}
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
		.append("<yysjd>").append(info.getApptInterval()).append("</yysjd>")//预约时间段	1-0点到12点,2-12点到24点
		.append("<yyly>").append(sourceOfCertification).append("</yyly>")	//获取来源
		.append("<openid>").append(openId).append("</openid>")				//微信公众号唯一标识			
		.append("<yyjs>").append("1").append("</yyjs>")						//预约角色		默认1-个人
		.append("</request>");
		
		log.info("addTempApptInfo 接口 xml请求参数: " + sb);
		JSONObject respJson = new JSONObject();
		try {
			respJson = WebServiceClient.getInstance().requestWebService(url, method, interfaceNumber, sb.toString(), userId, userPwd, key);
		} catch (Exception e) {
			respJson.put("code", MsgCode.webServiceCallError);
			respJson.put("msg", MsgCode.webServiceCallMsg);
			throw e;
		}
		return respJson;
	}

	/**
	 * 获取酒店分店信息
	 * @Description: TODO(获取酒店分店信息)
	 * @param agencyCode 组织机构代码或社会统一信用代码
	 * @throws Exception
	 */
	public static JSONObject getHotelInfoByCode(String agencyCode, String url, String method, String userId,
			String userPwd, String key) throws Exception {
		
		String interfaceNumber = "dbjq07";	//接口编号
		//拼装xml数据
		StringBuffer sb = new StringBuffer();
		sb.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?><request>")
		.append("<zzjgdm>").append(agencyCode).append("</zzjgdm>")		//组织机构代码或社会统一信用代码
		.append("</request>");
		
		log.info("getHotelInfoByCode 接口拼接xml请求报文:" + sb);
		JSONObject respJson = new JSONObject();
		try {
			respJson = WebServiceClient.getInstance().requestWebService(url, method, interfaceNumber, sb.toString(), userId, userPwd, key);
		} catch (Exception e) {
			respJson.put("code", MsgCode.webServiceCallError);
			respJson.put("msg", MsgCode.webServiceCallMsg);
			throw e;
		}
		return respJson;
	}

	/**
	 * 酒店分店登录
	 * @Description: TODO(酒店分店登录)
	 * @param vo 酒店信息
	 * @param password 登录密码
	 * @param sourceOfCertification 请求来源
	 * @throws Exception
	 */
	public static JSONObject loginViaHotel(HotelInfoVo vo, String password, String sourceOfCertification, String url,
			String method, String userId, String userPwd, String key) throws Exception {
		
		String interfaceNumber = "dbjq08";	//接口编号
		//拼装xml数据
		StringBuffer sb = new StringBuffer();
		sb.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?><request>")
		.append("<zzjgdm>").append(vo.getAgencyCode()).append("</zzjgdm>")	//组织机构代码或社会统一信用代码
		.append("<fdbm>").append(vo.getBranchCode()).append("</fdbm>")		//分店编号
		.append("<login_pwd>").append(password).append("</login_pwd>")		//登录密码
		.append("<yhly>").append(sourceOfCertification).append("</yhly>")	//请求来源(可为空)
		.append("</request>");
		
		log.info("loginViaHotel 接口拼接xml请求报文:" + sb);
		JSONObject respJson = new JSONObject();
		try {
			respJson = WebServiceClient.getInstance().requestWebService(url, method, interfaceNumber, sb.toString(), userId, userPwd, key);
		} catch (Exception e) {
			respJson.put("code", MsgCode.webServiceCallError);
			respJson.put("msg", MsgCode.webServiceCallMsg);
			throw e;
		}
		return respJson;
	}
	
	/**
	 * 获取酒店配额信息
	 * @Description: TODO(获取酒店配额信息)
	 * @param apptDate 预约日期
	 * @param apptDistrict 预约片区
	 * @param vo 酒店信息
	 * @param sourceOfCertification 请求来源
	 * @throws Exception
	 */
	public static JSONObject getHotelQuotaInfo(String apptDate, String apptDistrict, HotelInfoVo vo, String sourceOfCertification, String url,
			String method, String userId, String userPwd, String key) throws Exception {
		
		String interfaceNumber = "dbjq09";	//接口编号
		//拼装xml数据
		StringBuffer sb = new StringBuffer();
		sb.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?><request>")
		.append("<yyrq>").append(apptDate).append("</yyrq>")				//预约日期
		.append("<yyqy>").append(apptDistrict).append("</yyqy>")			//预约片区
		.append("<zzjgdm>").append(vo.getAgencyCode()).append("</zzjgdm>")	//组织机构代码或社会统一信用代码
		.append("<fdbm>").append(vo.getBranchCode()).append("</fdbm>")		//分店编号
		.append("<yyly>").append(sourceOfCertification).append("</yyly>")	//请求来源
		.append("</request>");
		
		log.info("getHotelQuotaInfo 接口拼接xml请求报文:" + sb);
		JSONObject respJson = new JSONObject();
		try {
			respJson = WebServiceClient.getInstance().requestWebService(url, method, interfaceNumber, sb.toString(), userId, userPwd, key);
		} catch (Exception e) {
			respJson.put("code", MsgCode.webServiceCallError);
			respJson.put("msg", MsgCode.webServiceCallMsg);
			throw e;
		}
		return respJson;
	}
	
	/**
	 * 酒店预约信息写入
	 * @Description: TODO(酒店预约信息写入)
	 * @param agencyCode 组织机构代码
	 * @param branchCode 分店编号
	 * @param apptInfoList 预约信息集合
	 * @throws Exception
	 */
	public static JSONObject addHotelApptInfo(String agencyCode, String branchCode, List<HotelApptInfoVo> apptInfoList, String url,
			String method, String userId, String userPwd, String key) throws Exception {
		
		String interfaceNumber = "dbjq11";	//接口编号
		//拼装xml数据
		StringBuffer sb = new StringBuffer();
		sb.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?><request>")
		.append("<yyjs>").append("2").append("</yyjs>")							//预约角色  2-酒店
		.append("<zzjgdm>").append(agencyCode).append("</zzjgdm>")				//组织机构代码或社会统一信用代码
		.append("<fdbm>").append(branchCode).append("</fdbm>");					//分店编号
		
		for (HotelApptInfoVo apptInfo : apptInfoList) {
			sb.append("<ret>")	//一个预约对象--开始
			.append("<hphm>").append(apptInfo.getPlateNo()).append("</hphm>")			//号牌号码
			.append("<hpzl>").append(apptInfo.getPlateType()).append("</hpzl>")			//车牌种类
			.append("<sfzmhm>").append(apptInfo.getIdentityCard()).append("</sfzmhm>")	//身份证号码
			.append("<xm>").append(apptInfo.getApptName()).append("</xm>")				//姓名
			.append("<sjhm>").append(apptInfo.getMobilePhone()).append("</sjhm>")		//手机号码
			.append("<yyqy>").append(apptInfo.getApptDistrict()).append("</yyqy>")		//预约片区
			.append("<yyrq>").append(apptInfo.getApptDate()).append("</yyrq>")			//预约日期
			.append("<yysjd>").append(apptInfo.getApptInterval()).append("</yysjd>")	//预约时间段
			.append("</ret>");	//一个预约对象--结束
		}
		
		sb.append("</request>");
		
		log.info("addHotelApptInfo 接口拼接xml请求报文:" + sb);
		JSONObject respJson = new JSONObject();
		try {
			respJson = WebServiceClient.getInstance().requestWebService(url, method, interfaceNumber, sb.toString(), userId, userPwd, key);
		} catch (Exception e) {
			respJson.put("code", MsgCode.webServiceCallError);
			respJson.put("msg", MsgCode.webServiceCallMsg);
			throw e;
		}
		return respJson;
	}
	
	/**
	 * 获取酒店预约信息列表
	 * @Description: TODO(获取酒店预约信息列表)
	 * @param vo 酒店信息
	 * @param apptDate 预约日期
	 * @throws Exception
	 */
	public static JSONObject getHotelApptHistoryByDate(HotelInfoVo vo, String apptDate,
			String url, String method, String userId, String userPwd, String key) throws Exception {
		
		String interfaceNumber = "dbjq12";	//接口编号
		//拼装xml数据
		StringBuffer sb = new StringBuffer();
		sb.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?><request>")
		.append("<zzjgdm>").append(vo.getAgencyCode()).append("</zzjgdm>")	//组织机构代码或社会统一信用代码
		.append("<fdbm>").append(vo.getBranchCode()).append("</fdbm>")		//分店编码
		.append("<yyrq>").append(apptDate).append("</yyrq>")				//预约日期
		.append("</request>");
		

		log.info("getHotelApptHistoryByDate 接口xml请求参数: " + sb);
		JSONObject respJson = new JSONObject();
		try {
			respJson = WebServiceClient.getInstance().requestWebService(url, method, interfaceNumber, sb.toString(), userId, userPwd, key);
		} catch (Exception e) {
			respJson.put("code", MsgCode.webServiceCallError);
			respJson.put("msg", MsgCode.webServiceCallMsg);
			throw e;
		}
		return respJson;
	}
	
	/**
	 * 获取酒店预约信息根据查询类型
	 * @Description: TODO(获取酒店预约信息根据查询类型)
	 * @param apptInfo 预约信息
	 * @param hotelInfo 酒店信息
	 * @param queryType 查询类型 1:根据号牌号码查询，2:根据姓名查询，3:根据手机号码查询
	 * @throws Exception
	 */
	public static JSONObject getHotelApptInfoByQueryType(HotelApptInfoVo apptInfo, HotelInfoVo hotelInfo, String queryType,
			String url, String method, String userId, String userPwd, String key) throws Exception {
		
		String interfaceNumber = "dbjq10";	//接口编号
		//拼装xml数据
		StringBuffer sb = new StringBuffer();
		sb.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?><request>")
		.append("<hphm>").append(apptInfo.getPlateNo()).append("</hphm>")			//号牌号码
		.append("<hpzl>").append(apptInfo.getPlateType()).append("</hpzl>")			//号牌种类
		.append("<xm>").append(apptInfo.getApptName()).append("</xm>")				//姓名
		.append("<sjhm>").append(apptInfo.getMobilePhone()).append("</sjhm>")		//手机号码
		.append("<zzjgdm>").append(hotelInfo.getAgencyCode()).append("</zzjgdm>")	//组织机构代码或社会统一信用代码
		.append("<fdbm>").append(hotelInfo.getBranchCode()).append("</fdbm>")		//分店编码
		.append("<cxlx>").append(queryType).append("</cxlx>")						//查询类型 1:根据号牌号码查询，2:根据姓名查询，3:根据手机号码查询
		.append("</request>");
		
		log.info("getHotelApptInfoByQueryType 接口xml请求参数: " + sb);
		JSONObject respJson = new JSONObject();
		try {
			respJson = WebServiceClient.getInstance().requestWebService(url, method, interfaceNumber, sb.toString(), userId, userPwd, key);
		} catch (Exception e) {
			respJson.put("code", MsgCode.webServiceCallError);
			respJson.put("msg", MsgCode.webServiceCallMsg);
			throw e;
		}
		return respJson;
	}
	
	/**
     * 取消酒店预约信息
     * @Description: TODO(取消酒店预约信息)
     * @param apptId 预约编号
     * @param cancelReason 取消原因
	 * @param sourceOfCertification 获取来源
	 * @throws Exception
	 */
	public static JSONObject cancelHotelApptInfo(String apptId, String cancelReason, String sourceOfCertification,
			String url, String method, String userId, String userPwd, String key) throws Exception {
		
		String interfaceNumber = "dbjq02";	//接口编号
		//拼装xml数据
		StringBuffer sb = new StringBuffer();
		sb.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?><request>")
		.append("<yyid>").append(apptId).append("</yyid>")					//预约编号
		.append("<qxyy>").append(cancelReason).append("</qxyy>")			//取消原因
		.append("<yyly>").append(sourceOfCertification).append("</yyly>")	//获取来源
		.append("<yyjs>").append("2").append("</yyjs>")						//预约角色		2-酒店
		.append("</request>");
		
		log.info("cancelHotelApptInfo 接口xml请求参数: " + sb);
		JSONObject respJson = new JSONObject();
		try {
			respJson = WebServiceClient.getInstance().requestWebService(url, method, interfaceNumber, sb.toString(), userId, userPwd, key);
		} catch (Exception e) {
			respJson.put("code", MsgCode.webServiceCallError);
			respJson.put("msg", MsgCode.webServiceCallMsg);
			throw e;
		}
		return respJson;
	}
	
	/**
	 * 酒店预约信息详情
	 * @Description: TODO(酒店预约信息详情)
	 * @param apptId 预约编号
	 * @throws Exception
	 */
	public static JSONObject getApptInfoDetailByApptId(String apptId, String url, String method, String userId, String userPwd, String key) throws Exception {
		
		String interfaceNumber = "dbjq13";	//接口编号
		//拼装xml数据
		StringBuffer sb = new StringBuffer();
		sb.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?><request>")
		.append("<com_userid>").append(userId).append("</com_userid>")		//用户名
		.append("<com_userpwd>").append(userPwd).append("</com_userpwd>")	//密码
		.append("<com_jkid>").append(interfaceNumber).append("</com_jkid>")	//接口编号
		.append("<zjz>").append(apptId).append("</zjz>")					//预约编号
		.append("</request>");
		
		log.info("getApptInfoDetailByApptId 接口xml请求参数: " + sb);
		JSONObject respJson = new JSONObject();
		try {
			respJson = WebServiceClient.getInstance().complexWebService(url, method, interfaceNumber, sb.toString(), userId, userPwd, key);
		} catch (Exception e) {
			respJson.put("code", MsgCode.webServiceCallError);
			respJson.put("msg", MsgCode.webServiceCallMsg);
			throw e;
		}
		return respJson;
	}
}
