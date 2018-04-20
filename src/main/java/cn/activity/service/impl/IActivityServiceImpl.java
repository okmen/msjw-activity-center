package cn.activity.service.impl;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.google.gson.JsonObject;

import cn.activity.bean.ApptDistrictAndTimeVo;
import cn.activity.bean.ApptHistoryRecordVo;
import cn.activity.bean.ApptInfoDetailVo;
import cn.activity.bean.HotelApptHistoryRecordVo;
import cn.activity.bean.HotelApptInfoVo;
import cn.activity.bean.HotelApptResultVo;
import cn.activity.bean.HotelInfoVo;
import cn.activity.bean.NormalApptInfoVo;
import cn.activity.cache.impl.IActivityCachedImpl;
import cn.activity.service.IActivityService;
import cn.activity.utils.ActivityDateUtil;
import cn.activity.utils.TransferThirdParty;
import cn.sdk.bean.BaseBean;
import cn.sdk.util.MsgCode;
import cn.sdk.util.StringUtil;

/**
 * 活动中心
 * @author Mbenben
 *
 */
@Service("activityService")
@SuppressWarnings(value="all")
public class IActivityServiceImpl implements IActivityService {
	
	private final Logger logger = LoggerFactory.getLogger(getClass());
	
	@Autowired
	private IActivityCachedImpl iActivityCached;

	/**
	 * 获取预约场次信息
	 * @Description: TODO(获取预约场次信息)
	 * @param sourceOfCertification 获取来源
	 * @throws Exception 
	 */
	public BaseBean getNormalApptDate(String sourceOfCertification ,String apptDistrict) throws Exception {
		logger.info("获取预约场次信息采集WebService...");
		
		BaseBean baseBean = new BaseBean();	//创建返回信息
		ArrayList<ApptDistrictAndTimeVo> list = new ArrayList<>();
		
		try {
			String url = iActivityCached.getUrl(); 			//webservice请求url
			String method = iActivityCached.getMethod(); 	//webservice请求方法名称
			String userId = iActivityCached.getUserid(); 	//webservice登录账号
			String userPwd = iActivityCached.getUserpwd(); 	//webservice登录密码
			String key = iActivityCached.getKey();			//秘钥
			
			//调用第三方接口
			JSONObject respJson = TransferThirdParty.getNormalApptDate(sourceOfCertification, apptDistrict ,url, method, userId, userPwd, key);
			String code = respJson.getString("code");
			String msg = null;
			
			if(MsgCode.success.equals(code)){
				JSONObject jsonBody = respJson.getJSONObject("msg").getJSONObject("response").getJSONObject("body");
//				JSONObject jsonBody = respJson.getJSONObject("body");
				if(jsonBody != null){
					Object obj = jsonBody.get("ret");
					if(obj instanceof JSONObject){
						ApptDistrictAndTimeVo vo = new ApptDistrictAndTimeVo();
						JSONObject jsonObject = (JSONObject) obj;
						
						//String dateConvert = ActivityDateUtil.dateConvert(jsonObject.getString("yyrq"));//10-06-17 00:00:00.0 转换为 2017-06-10
						vo.setApptDate(jsonObject.getString("yyrq"));		//预约日期
						vo.setApptDistrict(jsonObject.getString("yyqy"));
						vo.setApptInterval(jsonObject.getString("sjd"));
						vo.setCch(jsonObject.getString("cch"));
						vo.setCxrq(jsonObject.getString("cxrq"));
						vo.setLeftQuota(Integer.parseInt(jsonObject.getString("yype"))-Integer.parseInt(jsonObject.getString("yyy")) + "");
						vo.setTotalQuota(jsonObject.getString("yype"));
						list.add(vo);
					}
					else if(obj instanceof JSONArray){
						JSONArray jsonArray = (JSONArray) obj;
						if(jsonArray != null){	//查询记录有多个
							for(int i = 0; i < jsonArray.size(); i++){
								ApptDistrictAndTimeVo vo = new ApptDistrictAndTimeVo();
								JSONObject jsonObject = jsonArray.getJSONObject(i);
								
								//String dateConvert = ActivityDateUtil.dateConvert(jsonObject.getString("yyrq"));//10-06-17 00:00:00.0 转换为 2017-06-10
								vo.setApptDate(jsonObject.getString("yyrq"));		//预约日期
								vo.setApptDistrict(jsonObject.getString("yyqy"));
								vo.setApptInterval(jsonObject.getString("sjd"));
								vo.setCch(jsonObject.getString("cch"));
								vo.setCxrq(jsonObject.getString("cxrq"));
								vo.setLeftQuota(Integer.parseInt(jsonObject.getString("yype"))-Integer.parseInt(jsonObject.getString("yyy")) + "");
								vo.setTotalQuota(jsonObject.getString("yype"));
								list.add(vo);
							}
						}
					}
					baseBean.setData(list);
				}
			}else if(MsgCode.webServiceCallError.equals(code)){//1000
				msg = "系统繁忙，请稍后重试";
			}else if("9999".equals(code)){//警司通错误
				msg = MsgCode.webServiceCallMsg;
			}
			baseBean.setCode(code);
			baseBean.setMsg(msg);
			
			
//			String dateFormat = ActivityDateUtil.DateFormat(ccrq);	//17-JUN-10转化为2017-06-10
			logger.info("获取预约场次信息采集返回结果:" + JSON.toJSONString(baseBean));
		} catch (Exception e) {
			logger.error("获取预约场次信息采集失败！", e);
			throw e;
		}
		
		return baseBean;
	}

	/**
	 * 获取指定场次个人配额信息
	 * @Description: TODO(获取指定场次个人配额信息)
	 * @param sourceOfCertification 获取来源
	 * @throws Exception
	 */
	public BaseBean getQuotaInfoByApptDate(String apptDate, String apptDistrict, String sourceOfCertification) throws Exception {
		logger.info("获取指定场次个人配额信息采集WebService...");
		
		BaseBean baseBean = new BaseBean();	//创建返回信息
		
		try {
			String url = iActivityCached.getUrl(); 			//webservice请求url
			String method = iActivityCached.getMethod(); 	//webservice请求方法名称
			String userId = iActivityCached.getUserid(); 	//webservice登录账号
			String userPwd = iActivityCached.getUserpwd(); 	//webservice登录密码
			String key = iActivityCached.getKey();			//秘钥
			
			long start = System.currentTimeMillis();
			//调用第三方接口
			JSONObject respJson = TransferThirdParty.getQuotaInfoByApptDate(apptDate, apptDistrict, sourceOfCertification, url, method, userId, userPwd, key);
			long end = System.currentTimeMillis();
			
			logger.info("调用TransferThirdParty.getQuotaInfoByApptDate 耗时:" + (end - start));
			
			String code = respJson.getString("code");
			
			if(MsgCode.success.equals(code)){
				ApptDistrictAndTimeVo vo = new ApptDistrictAndTimeVo();
				JSONObject jsonBody = respJson.getJSONObject("body");
				vo.setApptDistrict(jsonBody.getString("yyqy"));	//预约片区
				vo.setApptDate(jsonBody.getString("yyrq"));		//预约日期
				vo.setTotalQuota(jsonBody.getString("zyype"));	//总预约配额
				vo.setLeftQuota(jsonBody.getString("kyype"));	//可预约配额
				baseBean.setData(vo);
			}else if(MsgCode.webServiceCallError.equals(code)){//1000
				baseBean.setMsg("系统繁忙，请稍后重试");
			}else if("9999".equals(code)){//警司通错误
				baseBean.setMsg(MsgCode.webServiceCallMsg);
			}else{
				baseBean.setMsg(respJson.getString("msg"));
			}
			baseBean.setCode(code);
			
			logger.info("获取指定场次个人配额信息采集返回结果:" + JSON.toJSONString(baseBean));
		} catch (Exception e) {
			logger.error("获取指定场次个人配额信息采集失败！", e);
			throw e;
		}
		return baseBean;
	}

	/**
	 * 个人预约信息写入
	 * @Description: TODO(个人预约信息写入)
	 * @param info 个人预约信息
	 * @param sourceOfCertification 获取来源
	 * @param openId 微信公众号唯一标识
	 * @throws Exception
	 */
	public BaseBean addNormalApptInfo(NormalApptInfoVo info, String sourceOfCertification, String openId) throws Exception {
		logger.info("个人预约信息写入采集WebService...");
		
		BaseBean baseBean = new BaseBean();	//创建返回信息
		
		try {
			String url = iActivityCached.getUrl(); 			//webservice请求url
			String method = iActivityCached.getMethod(); 	//webservice请求方法名称
			String userId = iActivityCached.getUserid(); 	//webservice登录账号
			String userPwd = iActivityCached.getUserpwd(); 	//webservice登录密码
			String key = iActivityCached.getKey();			//秘钥
			
			//调用第三方接口
			JSONObject respJson = TransferThirdParty.addNormalApptInfo(info, sourceOfCertification, openId, url, method, userId, userPwd, key);
			
			String code = respJson.getString("code");	//返回状态码
			String msg = respJson.getString("msg");		//返回消息描述
			
			if(MsgCode.success.equals(code)){//预约成功
				String yyid = respJson.getJSONObject("body").getString("yyid");
				baseBean.setData("预约编号为："+yyid);
			}else if(MsgCode.webServiceCallError.equals(code)){//1000
				msg = "系统繁忙，请稍后重试";
			}else if("9999".equals(code)){//警司通错误
				msg = MsgCode.webServiceCallMsg;
			}
			
			baseBean.setCode(code);		
			baseBean.setMsg(msg);
			
			logger.info("个人预约信息写入采集返回结果:" + JSON.toJSONString(baseBean));
		} catch (Exception e) {
			logger.error("个人预约信息写入采集失败！", e);
			throw e;
		}
		return baseBean;
	}

	/**
     * 查询个人预约信息
     * @Description: TODO(查询个人预约信息)
     * @param plateNo	号牌号码
     * @param plateType 号牌种类
     * @param vinLastFour 车架后4位
     * @param mobilePhone 手机号码
     * @param sourceOfCertification 查询来源
     */
	public BaseBean getApptHistoryRecord(String plateNo, String plateType, String vinLastFour, String mobilePhone, String sourceOfCertification) throws Exception {
		logger.info("查询个人预约信息采集WebService...");
		
		BaseBean baseBean = new BaseBean();	//创建返回信息
		ArrayList<ApptHistoryRecordVo> list = new ArrayList<>();
		
		try {
			String url = iActivityCached.getUrl(); 			//webservice请求url
			String method = iActivityCached.getMethod(); 	//webservice请求方法名称
			String userId = iActivityCached.getUserid(); 	//webservice登录账号
			String userPwd = iActivityCached.getUserpwd(); 	//webservice登录密码
			String key = iActivityCached.getKey();			//秘钥
			
			//调用第三方接口
			JSONObject respJson = TransferThirdParty.getApptHistoryRecord(plateNo, plateType, vinLastFour, mobilePhone, sourceOfCertification, url, method, userId, userPwd, key);
			
			String code = respJson.getString("code");	//返回状态码
			String msg = respJson.getString("msg");		//返回消息描述
			//查询成功
			if(MsgCode.success.equals(code)){
				//JSONObject jsonBody = respJson.getJSONObject("msg").getJSONObject("response").getJSONObject("body");
				JSONObject jsonBody = respJson.getJSONObject("body");
				if(jsonBody != null){
					Object obj = jsonBody.get("ret");
					if(obj instanceof JSONObject){
						ApptHistoryRecordVo vo = new ApptHistoryRecordVo();
						JSONObject jsonObject = (JSONObject) obj;
						
						//String dateConvert = ActivityDateUtil.dateConvert(jsonObject.getString("yyrq"));//10-06-17 00:00:00.0 转换为 2017-06-10
						vo.setApptDate(jsonObject.getString("yyrq"));		//预约日期
						vo.setApptId(jsonObject.getString("yyid"));			//预约编号
						vo.setApptDistrict(jsonObject.getString("yyqy"));	//预约片区
						vo.setApptInterval(jsonObject.getString("yysjd"));	//预约时间段
						vo.setApptStatus(jsonObject.getString("zt"));		//状态
						list.add(vo);
					}
					else if(obj instanceof JSONArray){
						JSONArray jsonArray = (JSONArray) obj;
						if(jsonArray != null){	//查询记录有多个
							for(int i = 0; i < jsonArray.size(); i++){
								ApptHistoryRecordVo vo = new ApptHistoryRecordVo();
								JSONObject jsonObject = jsonArray.getJSONObject(i);
								
								//String dateConvert = ActivityDateUtil.dateConvert(jsonObject.getString("yyrq"));//10-06-17 00:00:00.0 转换为 2017-06-10
								vo.setApptDate(jsonObject.getString("yyrq"));		//预约日期
								vo.setApptId(jsonObject.getString("yyid"));			//预约编号
								vo.setApptDistrict(jsonObject.getString("yyqy"));	//预约片区
								vo.setApptInterval(jsonObject.getString("yysjd"));	//预约时间段
								vo.setApptStatus(jsonObject.getString("zt"));		//状态
								list.add(vo);
							}
						}
					}
					baseBean.setData(list);
				}
			}else if(MsgCode.webServiceCallError.equals(code)){//1000
				msg = "系统繁忙，请稍后重试";
			}else if("9999".equals(code)){//警司通错误
				msg = MsgCode.webServiceCallMsg;
			}
			
			baseBean.setMsg(msg);
			baseBean.setCode(code);		
			
			logger.info("查询个人预约信息采集返回结果:" + JSON.toJSONString(baseBean));
		} catch (Exception e) {
			logger.error("查询个人预约信息采集失败！", e);
			throw e;
		}
		return baseBean;
	}
	
	/**
     * 取消个人预约信息
     * @Description: TODO(取消个人预约信息)
     * @param apptId 预约编号
     * @param cancelReason 取消原因
	 * @param sourceOfCertification 获取来源
	 * @throws Exception 
	 */
	public BaseBean cancelNormalApptInfo(String apptId, String cancelReason, String sourceOfCertification)
			throws Exception {
		logger.info("取消个人预约信息采集WebService...");
		
		BaseBean baseBean = new BaseBean();	//创建返回信息
		
		try {
			String url = iActivityCached.getUrl(); 			//webservice请求url
			String method = iActivityCached.getMethod(); 	//webservice请求方法名称
			String userId = iActivityCached.getUserid(); 	//webservice登录账号
			String userPwd = iActivityCached.getUserpwd(); 	//webservice登录密码
			String key = iActivityCached.getKey();			//秘钥
			
			//调用第三方接口
			JSONObject respJson = TransferThirdParty.cancelNormalApptInfo(apptId, cancelReason, sourceOfCertification, url, method, userId, userPwd, key);
			
			String code = respJson.getString("code");	//返回状态码
			String msg = respJson.getString("msg");		//返回消息描述
			
			if(MsgCode.webServiceCallError.equals(code)){//1000
				msg = "系统繁忙，请稍后重试";
			}else if("9999".equals(code)){//警司通错误
				msg = MsgCode.webServiceCallMsg;
			}

			baseBean.setCode(code);		
			baseBean.setMsg(msg);
			
			logger.info("取消个人预约信息采集返回结果:" + JSON.toJSONString(baseBean));
		} catch (Exception e) {
			logger.error("取消个人预约信息采集失败！", e);
			throw e;
		}
		return baseBean;
	}

	/**
	 * 临时个人预约信息写入
	 * @Description: TODO(临时个人预约信息写入)
	 * @param info 个人预约信息
	 * @param sourceOfCertification 获取来源
	 * @param openId 微信公众号唯一标识
	 * @throws Exception
	 */
	public BaseBean addTempApptInfo(NormalApptInfoVo info, String sourceOfCertification, String openId) throws Exception {
		logger.info("临时个人预约信息写入采集WebService...");
		
		BaseBean baseBean = new BaseBean();	//创建返回信息
		
		try {
			String url = iActivityCached.getUrl(); 			//webservice请求url
			String method = iActivityCached.getMethod(); 	//webservice请求方法名称
			String userId = iActivityCached.getUserid(); 	//webservice登录账号
			String userPwd = iActivityCached.getUserpwd(); 	//webservice登录密码
			String key = iActivityCached.getKey();			//秘钥
			
			//调用第三方接口
			JSONObject respJson = TransferThirdParty.addTempApptInfo(info, sourceOfCertification, openId, url, method, userId, userPwd, key);
			
			String code = respJson.getString("code");	//返回状态码
			String msg = respJson.getString("msg");		//返回消息描述
			
			//预约成功
			if(MsgCode.success.equals(code)){
				String yyid = respJson.getJSONObject("body").getString("yyid");
				baseBean.setData("预约编号为:"+yyid);
			}else if(MsgCode.webServiceCallError.equals(code)){//1000
				msg = "系统繁忙，请稍后重试";
			}else if("9999".equals(code)){//警司通错误
				msg = MsgCode.webServiceCallMsg;
			}
			
			baseBean.setCode(code);		
			baseBean.setMsg(msg);
			
			logger.info("临时个人预约信息写入采集返回结果:" + JSON.toJSONString(baseBean));
		} catch (Exception e) {
			logger.error("临时个人预约信息写入采集失败！", e);
			throw e;
		}
		return baseBean;
	}

	/**
	 * 获取酒店分店信息
	 * @Description: TODO(获取酒店分店信息)
	 * @param agencyCode 组织机构代码或社会统一信用代码
	 * @throws Exception
	 */
	public BaseBean getHotelInfoByCode(String agencyCode) throws Exception {
		logger.info("获取酒店分店信息采集WebService...");
		
		BaseBean baseBean = new BaseBean();	//创建返回信息
		List<HotelInfoVo> list = new ArrayList<>();
		
		try {
			String url = iActivityCached.getUrl(); 			//webservice请求url
			String method = iActivityCached.getMethod(); 	//webservice请求方法名称
			String userId = iActivityCached.getUserid(); 	//webservice登录账号
			String userPwd = iActivityCached.getUserpwd(); 	//webservice登录密码
			String key = iActivityCached.getKey();			//秘钥
			
			//调用第三方接口
			JSONObject respJson = TransferThirdParty.getHotelInfoByCode(agencyCode, url, method, userId, userPwd, key);
			
			String code = respJson.getString("code");	//返回状态码
			String msg = "";							//返回消息描述
			
			if(MsgCode.success.equals(code)){
				Object obj = respJson.getJSONObject("msg").getJSONObject("response").getJSONObject("body").get("ret");
				if(obj instanceof JSONObject){
					JSONObject jsonObj = (JSONObject) obj;
					addHotelInfoToList(list, jsonObj);
				}
				else if(obj instanceof JSONArray){
					JSONArray jsonArray = (JSONArray) obj;
					for(int i = 0; i < jsonArray.size(); i++){
						JSONObject jsonObj = jsonArray.getJSONObject(i);
						addHotelInfoToList(list, jsonObj);
					}
				}
				baseBean.setData(list);
				msg = respJson.getJSONObject("msg").getJSONObject("response").getString("msg");	//成功消息描述
			}else if(MsgCode.webServiceCallError.equals(code)){//1000
				baseBean.setMsg("系统繁忙，请稍后重试");
			}else if("9999".equals(code)){//警司通错误
				baseBean.setMsg(MsgCode.webServiceCallMsg);
			}else{
				msg = respJson.getString("msg");	//失败消息描述
			}
			
			baseBean.setCode(code);
			baseBean.setMsg(msg);
			
			logger.info("获取酒店分店信息采集返回结果:" + JSON.toJSONString(baseBean));
		} catch (Exception e) {
			logger.error("获取酒店分店信息采集失败！", e);
			throw e;
		}
		return baseBean;
	}
	/**
	 * 封装酒店信息
	 */
	private void addHotelInfoToList(List<HotelInfoVo> list, JSONObject jsonObj) {
		//封装酒店信息
		HotelInfoVo vo = new HotelInfoVo();
		vo.setAgencyCode(jsonObj.getString("zzjgdm"));	//机构代码
		vo.setBranchCode(jsonObj.getString("fdbm"));    //分店编号
		vo.setHotelName(jsonObj.getString("gsmc"));     //酒店名称
		vo.setBranchName(jsonObj.getString("fdmc"));    //分店名称
		list.add(vo);
	}
	
	/**
	 * 酒店分店登录
	 * @Description: TODO(酒店分店登录)
	 * @param vo 酒店信息
	 * @param password 登录密码
	 * @param sourceOfCertification 请求来源(可为空)
	 * @throws Exception
	 */
	public BaseBean loginViaHotel(HotelInfoVo vo, String password, String sourceOfCertification) throws Exception {
		logger.info("酒店分店登录采集WebService...");
		
		BaseBean baseBean = new BaseBean();	//创建返回信息
		
		try {
			String url = iActivityCached.getUrl(); 			//webservice请求url
			String method = iActivityCached.getMethod(); 	//webservice请求方法名称
			String userId = iActivityCached.getUserid(); 	//webservice登录账号
			String userPwd = iActivityCached.getUserpwd(); 	//webservice登录密码
			String key = iActivityCached.getKey();			//秘钥
			
			//调用第三方接口
			JSONObject respJson = TransferThirdParty.loginViaHotel(vo, password, sourceOfCertification, url, method, userId, userPwd, key);
			
			String code = respJson.getString("code");	//返回状态码
			String msg = respJson.getString("msg");		//返回消息描述
			if(MsgCode.webServiceCallError.equals(code)){//1000
				baseBean.setMsg("系统繁忙，请稍后重试");
			}else if("9999".equals(code)){//警司通错误
				baseBean.setMsg(MsgCode.webServiceCallMsg);
			}
			baseBean.setCode(code);
			baseBean.setMsg(msg);
			
			logger.info("酒店分店登录采集返回结果:" + JSON.toJSONString(baseBean));
		} catch (Exception e) {
			logger.error("酒店分店登录采集失败！", e);
			throw e;
		}
		return baseBean;
	}

	/**
	 * 获取酒店配额信息
	 * @Description: TODO(获取酒店配额信息)
	 * @param apptDate 预约日期
	 * @param apptDistrict 预约片区
	 * @param info 酒店信息
	 * @param sourceOfCertification 请求来源
	 * @throws Exception
	 */
	public BaseBean getHotelQuotaInfo(String apptDate, String apptDistrict, HotelInfoVo info, String sourceOfCertification) throws Exception {
		logger.info("获取酒店配额信息采集WebService...");
		
		BaseBean baseBean = new BaseBean();	//创建返回信息
		
		try {
			String url = iActivityCached.getUrl(); 			//webservice请求url
			String method = iActivityCached.getMethod(); 	//webservice请求方法名称
			String userId = iActivityCached.getUserid(); 	//webservice登录账号
			String userPwd = iActivityCached.getUserpwd(); 	//webservice登录密码
			String key = iActivityCached.getKey();			//秘钥
			
			//调用第三方接口
			JSONObject respJson = TransferThirdParty.getHotelQuotaInfo(apptDate, apptDistrict, info, sourceOfCertification, url, method, userId, userPwd, key);
			
			String code = respJson.getString("code");
			
			if(MsgCode.success.equals(code)){
				JSONObject jsonBody = respJson.getJSONObject("body");
				ApptDistrictAndTimeVo vo = new ApptDistrictAndTimeVo();		//预约片区及时间段
				vo.setApptDistrict(jsonBody.getString("yyqy"));				//预约片区
				vo.setApptDate(jsonBody.getString("yyrq"));					//预约日期
				vo.setTotalQuota(jsonBody.getString("zyype"));				//总预约配额
				vo.setLeftQuota(jsonBody.getString("kyype"));				//可预约配额
				baseBean.setData(vo);
			}else if(MsgCode.webServiceCallError.equals(code)){//1000
				baseBean.setMsg("系统繁忙，请稍后重试");
			}else if("9999".equals(code)){//警司通错误
				baseBean.setMsg(MsgCode.webServiceCallMsg);
			}else{
				baseBean.setMsg(respJson.getString("msg"));//失败消息描述
			}
			baseBean.setCode(code);
			
			logger.info("获取酒店配额信息采集返回结果:" + JSON.toJSONString(baseBean));
		} catch (Exception e) {
			logger.error("获取酒店配额信息采集失败！", e);
			throw e;
		}
		return baseBean;
	}

	/**
	 * 酒店预约信息写入
	 * @Description: TODO(酒店预约信息写入)
	 * @param agencyCode 组织机构代码
	 * @param branchCode 分店编号
	 * @param apptInfoList 预约信息集合
	 */
	public BaseBean addHotelApptInfo(String agencyCode, String branchCode, List<HotelApptInfoVo> apptInfoList) throws Exception {
		logger.info("酒店预约信息写入采集WebService...");
		
		BaseBean baseBean = new BaseBean();	//创建返回信息
		List<HotelApptResultVo> list = new ArrayList<>();
		
		try {
			String url = iActivityCached.getUrl(); 			//webservice请求url
			String method = iActivityCached.getMethod(); 	//webservice请求方法名称
			String userId = iActivityCached.getUserid(); 	//webservice登录账号
			String userPwd = iActivityCached.getUserpwd(); 	//webservice登录密码
			String key = iActivityCached.getKey();			//秘钥
			
			long before = System.currentTimeMillis();
			//调用第三方接口
			JSONObject respJson = TransferThirdParty.addHotelApptInfo(agencyCode, branchCode, apptInfoList, url, method, userId, userPwd, key);
			long after = System.currentTimeMillis();
			logger.info("【酒店预约信息写入耗时】: " + (after - before) + " ms");
			
			String code = respJson.getString("code");
			String msg = "";
			StringBuffer sb = new StringBuffer();
			
			if(MsgCode.success.equals(code)){
				Object obj = respJson.getJSONObject("body").get("ret");
				//预约一个时,返回一个结果
				if(obj instanceof JSONObject){
					JSONObject jsonObj = (JSONObject) obj;
					addResultVoToList(list, jsonObj);
					//预约结果信息拼接
					sb.append(jsonObj.getString("hphm")).append("，").append(jsonObj.getString("jgms")).append("。");
				}
				//预约多个时,返回多个结果
				else if(obj instanceof JSONArray){
					JSONArray jsonArray = (JSONArray) obj;
					for(int i = 0; i < jsonArray.size(); i++){
						JSONObject jsonObj = jsonArray.getJSONObject(i);
						addResultVoToList(list, jsonObj);
						//预约结果信息拼接
						sb.append(jsonObj.getString("hphm")).append("，").append(jsonObj.getString("jgms")).append("。");
					}
				}
				msg = sb.toString();
				baseBean.setData(list);
			}else if(MsgCode.webServiceCallError.equals(code)){//1000
				baseBean.setMsg("系统繁忙，请稍后重试");
			}else if("9999".equals(code)){//警司通错误
				baseBean.setMsg(MsgCode.webServiceCallMsg);
			}else{
				msg = respJson.getString("msg");//失败消息描述
			}
			baseBean.setMsg(msg);
			baseBean.setCode(code);
			
			logger.info("酒店预约信息写入采集返回结果:" + JSON.toJSONString(baseBean));
		} catch (Exception e) {
			logger.error("酒店预约信息写入采集失败！", e);
			throw e;
		}
		return baseBean;
	}
	/**
	 * 封装信息写入的结果
	 */
	private void addResultVoToList(List<HotelApptResultVo> list, JSONObject jsonObj) {
		//封装预约结果数据
		HotelApptResultVo resultVo = new HotelApptResultVo();
		resultVo.setPlateNo(jsonObj.getString("hphm"));		//号牌号码
		resultVo.setPlateType(jsonObj.getString("hpzl"));	//号牌种类
		resultVo.setResult(jsonObj.getString("yyjg"));		//预约结果 0-失败，1-成功
		resultVo.setDesc(jsonObj.getString("jgms"));		//结果描述
		resultVo.setApptId(jsonObj.getString("yyid"));		//预约ID
		list.add(resultVo);
	}

	/**
	 * 获取酒店预约信息列表
	 * @Description: TODO(获取酒店预约信息列表)
	 * @param vo 酒店信息
	 * @param apptDate 预约日期
	 * @throws Exception
	 */
	public BaseBean getHotelApptHistoryByDate(HotelInfoVo vo, String apptDate) throws Exception {
		logger.info("获取酒店预约信息列表采集WebService...");
		
		BaseBean baseBean = new BaseBean();	//创建返回信息
		List<HotelApptHistoryRecordVo> list = new ArrayList<>();
		
		try {
			String url = iActivityCached.getUrl(); 			//webservice请求url
			String method = iActivityCached.getMethod(); 	//webservice请求方法名称
			String userId = iActivityCached.getUserid(); 	//webservice登录账号
			String userPwd = iActivityCached.getUserpwd(); 	//webservice登录密码
			String key = iActivityCached.getKey();			//秘钥
			
			//调用第三方接口
			JSONObject respJson = TransferThirdParty.getHotelApptHistoryByDate(vo, apptDate, url, method, userId, userPwd, key);
			
			String code = respJson.getString("code");
			
			if(MsgCode.success.equals(code)){
				Object obj = respJson.getJSONObject("body").get("ret");
				if(obj instanceof JSONObject){
					JSONObject jsonObj = (JSONObject) obj;
					addRecordVoToList(list, jsonObj, apptDate);
				}
				else if(obj instanceof JSONArray){
					JSONArray jsonArray = (JSONArray) obj;
					for(int i = 0; i < jsonArray.size(); i++){
						JSONObject jsonObj = jsonArray.getJSONObject(i);
						addRecordVoToList(list, jsonObj, apptDate);
					}
				}
				//有查询结果,按预约编号倒叙排序
				apptIdDescSort(list);
				baseBean.setData(list);
			}else if(MsgCode.webServiceCallError.equals(code)){//1000
				baseBean.setMsg("系统繁忙，请稍后重试");
			}else if("9999".equals(code)){//警司通错误
				baseBean.setMsg(MsgCode.webServiceCallMsg);
			}else{
				baseBean.setMsg(respJson.getString("msg"));//失败消息描述
			}
			baseBean.setCode(code);
			
			logger.info("获取酒店预约信息列表采集返回结果:" + JSON.toJSONString(baseBean));
		} catch (Exception e) {
			logger.error("获取酒店预约信息列表采集失败！", e);
			throw e;
		}
		return baseBean;
	}
	/**
	 * 封装酒店列表结果
	 */
	private void addRecordVoToList(List<HotelApptHistoryRecordVo> list, JSONObject jsonObj, String apptDate) {
		//封装预约结果数据
		HotelApptHistoryRecordVo recordVo = new HotelApptHistoryRecordVo();
		recordVo.setPlateNo(jsonObj.getString("hphm"));			//号牌号码
		recordVo.setMobilePhone(jsonObj.getString("sjhm"));		//手机号码
		recordVo.setApptName(jsonObj.getString("xm"));			//姓名
		recordVo.setApptId(jsonObj.getString("yyid"));			//预约id
		recordVo.setApptInterval(jsonObj.getString("yysjd"));	//预约时间段
		recordVo.setApptStatus(jsonObj.getString("zt"));		//状态
		recordVo.setApptDate(apptDate);							//预约日期 -警司通没有返回
		recordVo.setApptDistrict("1");							//预约片区 -警司通没有返回,写死为1-梅沙片区
		list.add(recordVo);
	}
	/**
	 * 按预约编号倒叙排序
	 */
    private void apptIdDescSort(List<HotelApptHistoryRecordVo> list) throws Exception {
        Collections.sort(list, new Comparator<HotelApptHistoryRecordVo>() {
            @Override
            public int compare(HotelApptHistoryRecordVo vo1, HotelApptHistoryRecordVo vo2) {
                try {
                    return vo1.getApptId().compareTo(vo2.getApptId());
                } catch (Exception e) {
                	logger.error("按预约编号倒叙排序异常", e);
                }
                return 0;
            }
        });
        Collections.reverse(list);
    }
	
	
	/**
	 * 获取酒店预约信息根据查询类型
	 * @Description: TODO(获取酒店预约信息根据查询类型)
	 * @param apptInfo 预约信息
	 * @param hotelInfo 酒店信息
	 * @param queryType 查询类型 1:根据号牌号码查询，2:根据姓名查询，3:根据手机号码查询
	 * @throws Exception
	 */
	public BaseBean getHotelApptInfoByQueryType(HotelApptInfoVo apptInfo, HotelInfoVo hotelInfo, String queryType)
			throws Exception {
		logger.info("获取酒店预约信息根据查询类型采集WebService...");
		
		BaseBean baseBean = new BaseBean();	//创建返回信息
		List<HotelApptHistoryRecordVo> list = new ArrayList<>();
		
		try {
			String url = iActivityCached.getUrl(); 			//webservice请求url
			String method = iActivityCached.getMethod(); 	//webservice请求方法名称
			String userId = iActivityCached.getUserid(); 	//webservice登录账号
			String userPwd = iActivityCached.getUserpwd(); 	//webservice登录密码
			String key = iActivityCached.getKey();			//秘钥
			
			//调用第三方接口
			JSONObject respJson = TransferThirdParty.getHotelApptInfoByQueryType(apptInfo, hotelInfo, queryType, url, method, userId, userPwd, key);
			
			String code = respJson.getString("code");
			
			if(MsgCode.success.equals(code)){
				Object obj = respJson.getJSONObject("body").get("ret");
				if(obj instanceof JSONObject){
					JSONObject jsonObj = (JSONObject) obj;
					addRecordVoToList(list, jsonObj);
				}
				else if(obj instanceof JSONArray){
					JSONArray jsonArray = (JSONArray) obj;
					for(int i = 0; i < jsonArray.size(); i++){
						JSONObject jsonObj = jsonArray.getJSONObject(i);
						addRecordVoToList(list, jsonObj);
					}
				}
				//按预约日期倒叙排序
				apptDateDescSort(list);
				//有查询结果
				baseBean.setData(list);
			}else if(MsgCode.webServiceCallError.equals(code)){//1000
				baseBean.setMsg("系统繁忙，请稍后重试");
			}else if("9999".equals(code)){//警司通错误
				baseBean.setMsg(MsgCode.webServiceCallMsg);
			}else{
				baseBean.setMsg(respJson.getString("msg"));//失败消息描述
			}
			baseBean.setCode(code);
			
			logger.info("获取酒店预约信息根据查询类型采集返回结果:" + JSON.toJSONString(baseBean));
		} catch (Exception e) {
			logger.error("获取酒店预约信息根据查询类型采集失败！", e);
			throw e;
		}
		return baseBean;
	}
	/**
	 * 封装酒店查询类型结果
	 */
	private void addRecordVoToList(List<HotelApptHistoryRecordVo> list, JSONObject jsonObj) {
		//封装预约结果数据
		HotelApptHistoryRecordVo recordVo = new HotelApptHistoryRecordVo();
		recordVo.setPlateNo(jsonObj.getString("hphm"));			//号牌号码
		recordVo.setMobilePhone(jsonObj.getString("sjhm"));		//手机号码
		recordVo.setApptName(jsonObj.getString("xm"));			//姓名
		recordVo.setApptId(jsonObj.getString("yyid"));			//预约id
		recordVo.setApptInterval(jsonObj.getString("yysjd"));	//预约时间段
		recordVo.setApptStatus(jsonObj.getString("zt"));		//状态
		recordVo.setApptDate(jsonObj.getString("yyrq"));		//预约日期
		recordVo.setApptDistrict(jsonObj.getString("yyqy"));	//预约片区
		list.add(recordVo);
	}
	/**
	 * 按预约日期倒叙排序
	 */
    private void apptDateDescSort(List<HotelApptHistoryRecordVo> list) throws Exception {
        Collections.sort(list, new Comparator<HotelApptHistoryRecordVo>() {
            @Override
            public int compare(HotelApptHistoryRecordVo vo1, HotelApptHistoryRecordVo vo2) {
                SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
                try {
                    Date dt1 = format.parse(vo1.getApptDate());
                    Date dt2 = format.parse(vo2.getApptDate());
                    if (dt1.getTime() > dt2.getTime()) {
                        return -1;
                    } else if (dt1.getTime() < dt2.getTime()) {
                        return 1;
                    } else {
                        return 0;
                    }
                } catch (Exception e) {
                	logger.error("按预约日期倒叙排序异常", e);
                }
                return 0;
            }
        });
    }
	
	
	/**
     * 取消酒店预约信息
     * @Description: TODO(取消酒店预约信息)
     * @param apptId 预约编号
     * @param cancelReason 取消原因
	 * @param sourceOfCertification 获取来源
	 * @throws Exception
	 */
	public BaseBean cancelHotelApptInfo(String apptId, String cancelReason, String sourceOfCertification)
			throws Exception {
		logger.info("取消酒店预约信息采集WebService...");
		
		BaseBean baseBean = new BaseBean();	//创建返回信息
		
		try {
			String url = iActivityCached.getUrl(); 			//webservice请求url
			String method = iActivityCached.getMethod(); 	//webservice请求方法名称
			String userId = iActivityCached.getUserid(); 	//webservice登录账号
			String userPwd = iActivityCached.getUserpwd(); 	//webservice登录密码
			String key = iActivityCached.getKey();			//秘钥
			
			//调用第三方接口
			JSONObject respJson = TransferThirdParty.cancelHotelApptInfo(apptId, cancelReason, sourceOfCertification, url, method, userId, userPwd, key);
			
			String code = respJson.getString("code");	//返回状态码
			String msg = "";							//返回消息描述
			
			if(MsgCode.webServiceCallError.equals(code)){//1000
				msg = "系统繁忙，请稍后重试";
			}else if("9999".equals(code)){//警司通错误
				msg = MsgCode.webServiceCallMsg;
			}else{
				msg = respJson.getString("msg");
			}
			baseBean.setCode(code);
			baseBean.setMsg(msg);
			
			logger.info("取消酒店预约信息采集返回结果:" + JSON.toJSONString(baseBean));
		} catch (Exception e) {
			logger.error("取消酒店预约信息采集失败！", e);
			throw e;
		}
		return baseBean;
	}

	/**
	 * 酒店预约信息详情
	 * @Description: TODO(酒店预约信息详情)
	 * @param apptId 预约编号
	 * @throws Exception
	 */
	public BaseBean getApptInfoDetailByApptId(String apptId) throws Exception {
		logger.info("酒店预约信息详情采集WebService...");
		
		BaseBean baseBean = new BaseBean();	//创建返回信息
		
		try {
			String url = iActivityCached.getUrl(); 			//webservice请求url
			String method = iActivityCached.getMethod(); 	//webservice请求方法名称
			String userId = iActivityCached.getUserid(); 	//webservice登录账号
			String userPwd = iActivityCached.getUserpwd(); 	//webservice登录密码
			String key = iActivityCached.getKey();			//秘钥
			
			//调用第三方接口
			JSONObject respJson = TransferThirdParty.getApptInfoDetailByApptId(apptId, url, method, userId, userPwd, key);
			
			String code = respJson.getString("code");	//返回状态码
			
			if(MsgCode.success.equals(code)){
				JSONObject jsonObj = respJson.getJSONObject("body").getJSONObject("ret");
				ApptInfoDetailVo detail = new ApptInfoDetailVo();
				detail.setApptId(jsonObj.getString("yyid"));		//预约编号
				detail.setPlateNo(jsonObj.getString("hphm"));		//号牌号码
				detail.setPlateType(jsonObj.getString("hpzl"));		//号码种类
				detail.setApptName(jsonObj.getString("xm"));		//姓名
				detail.setMobilePhone(jsonObj.getString("sjhm"));	//手机号码
				detail.setApptDistrict(jsonObj.getString("yyqy"));	//预约片区
				detail.setApptDate(jsonObj.getString("yyrq"));		//预约日期
				detail.setApptInterval(jsonObj.getString("yysjd"));	//预约时间段
				detail.setApptStatus(jsonObj.getString("zt"));		//状态
				baseBean.setData(detail);
			}else if(MsgCode.webServiceCallError.equals(code)){//1000
				baseBean.setMsg("系统繁忙，请稍后重试");
			}else if("9999".equals(code)){//警司通错误
				baseBean.setMsg(MsgCode.webServiceCallMsg);
			}else{
				baseBean.setMsg(respJson.getString("msg"));//失败消息描述
			}
			baseBean.setCode(code);
			
			logger.info("酒店预约信息详情采集返回结果:" + JSON.toJSONString(baseBean));
		} catch (Exception e) {
			logger.error("酒店预约信息详情采集失败！", e);
			throw e;
		}
		return baseBean;
	}
	
}
