package cn.activity.service.impl;

import java.util.ArrayList;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import cn.activity.bean.ApptDistrictAndTimeVo;
import cn.activity.bean.ApptHistoryRecordVo;
import cn.activity.bean.NormalApptInfoVo;
import cn.activity.cache.impl.IActivityCachedImpl;
import cn.activity.service.IActivityService;
import cn.activity.utils.ActivityDateUtil;
import cn.activity.utils.TransferThirdParty;
import cn.sdk.bean.BaseBean;
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
	public BaseBean getNormalApptDate(String sourceOfCertification) throws Exception {
		logger.info("获取预约场次信息采集WebService...");
		
		BaseBean baseBean = new BaseBean();	//创建返回信息
		
		try {
			String url = iActivityCached.getUrl(); 			//webservice请求url
			String method = iActivityCached.getMethod(); 	//webservice请求方法名称
			String userId = iActivityCached.getUserid(); 	//webservice登录账号
			String userPwd = iActivityCached.getUserpwd(); 	//webservice登录密码
			String key = iActivityCached.getKey();			//秘钥
			
			//调用第三方接口
			JSONObject respJson = TransferThirdParty.getNormalApptDate(sourceOfCertification, url, method, userId, userPwd, key);
			
			JSONObject jsonBody = respJson.getJSONObject("body");
			if(jsonBody != null){
				//预约日期
				String ccrq = jsonBody.get("ccrq").toString();	
				/*//预约日期为空
				if(StringUtil.isBlank(ccrq)){
					baseBean.setCode("0001");
					baseBean.setMsg("暂无可预约日期");
					return baseBean;
				}*/
				baseBean.setData(ccrq);
			}else{
				baseBean.setCode(respJson.get("code").toString());
				baseBean.setMsg(respJson.get("msg").toString());
			}
			
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
			
			JSONObject jsonBody = respJson.getJSONObject("body");
			String code = respJson.get("code").toString();
			
			
			if(jsonBody != null){
				String yyqy = jsonBody.get("yyqy").toString();	//预约片区
				String yyrq = jsonBody.get("yyrq").toString();	//预约日期
				String zyype = jsonBody.get("zyype").toString();	//总预约配额
				String kyype = jsonBody.get("kyype").toString();	//可预约配额
				
				ApptDistrictAndTimeVo vo = new ApptDistrictAndTimeVo();		//预约片区及时间段
				vo.setApptDistrict(yyqy);
				vo.setApptDate(yyrq);
				vo.setTotalQuota(zyype);
				vo.setLeftQuota(kyype);
				
				baseBean.setData(vo);
			}else{
				//查询失败
				baseBean.setMsg(respJson.get("msg").toString());
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
			
			String code = respJson.get("code").toString();	//返回状态码 
			String msg = respJson.get("msg").toString();	//返回消息描述
			
			//预约成功
			if("0000".equals(code)){
				String yyid = respJson.getJSONObject("body").get("yyid").toString();
				baseBean.setData("预约编号为："+yyid);
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
			
			String code = respJson.get("code").toString();	//返回状态码 
			String msg = respJson.get("msg").toString();	//返回消息描述
			//查询成功
			if("0000".equals(code)){
				JSONObject jsonBody = respJson.getJSONObject("msg").getJSONObject("response").getJSONObject("body");
				if(jsonBody != null){
					Object obj = jsonBody.get("ret");
					if(obj instanceof JSONObject){
						ApptHistoryRecordVo vo = new ApptHistoryRecordVo();
						JSONObject jsonObject = (JSONObject) obj;
						
						String dateConvert = ActivityDateUtil.dateConvert(jsonObject.get("yyrq").toString());//10-06-17 00:00:00.0 转换为 2017-06-10
						vo.setApptDate(dateConvert);							//预约日期
						vo.setApptId(jsonObject.get("yyid").toString());		//预约编号
						vo.setApptDistrict(jsonObject.get("yyqy").toString());	//预约片区
						vo.setApptInterval(jsonObject.get("yysjd").toString());	//预约时间段
						vo.setApptStatus(jsonObject.get("zt").toString());		//状态
						list.add(vo);
					}
					if(obj instanceof JSONArray){
						JSONArray jsonArray = (JSONArray) obj;
						if(jsonArray != null){	//查询记录有多个
							for(int i = 0; i < jsonArray.size(); i++){
								ApptHistoryRecordVo vo = new ApptHistoryRecordVo();
								JSONObject jsonObject = jsonArray.getJSONObject(i);
								
								String dateConvert = ActivityDateUtil.dateConvert(jsonObject.get("yyrq").toString());//10-06-17 00:00:00.0 转换为 2017-06-10
								vo.setApptDate(dateConvert);							//预约日期
								vo.setApptId(jsonObject.get("yyid").toString());		//预约编号
								vo.setApptDistrict(jsonObject.get("yyqy").toString());	//预约片区
								vo.setApptInterval(jsonObject.get("yysjd").toString());	//预约时间段
								vo.setApptStatus(jsonObject.get("zt").toString());		//状态
								list.add(vo);
							}
						}
					}
					baseBean.setData(list);
				}else{
					baseBean.setData("您暂无预约信息");
				}
			}
			//查询失败
			else{
				baseBean.setMsg(msg);
			}
			
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
			
			String code = respJson.get("code").toString();	//返回状态码 
			String msg = respJson.get("msg").toString();	//返回消息描述
			
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
			
			String code = respJson.get("code").toString();	//返回状态码 
			String msg = respJson.get("msg").toString();	//返回消息描述
			
			//预约成功
			if("0000".equals(code)){
				String yyid = respJson.getJSONObject("body").get("yyid").toString();
				baseBean.setData("预约编号为:"+yyid);
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
}
