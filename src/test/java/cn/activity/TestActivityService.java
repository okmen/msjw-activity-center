package cn.activity;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.alibaba.fastjson.JSON;

import cn.activity.bean.NormalApptInfoVo;
import cn.activity.service.IActivityService;
import cn.sdk.bean.BaseBean;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath:junit-test.xml" })
public class TestActivityService {

	@Autowired
	@Qualifier("activityService")
	private IActivityService activityService;
	
	@Test
	public void testgetNormalApptDate() throws Exception{
		BaseBean baseBean = activityService.getNormalApptDate("C");
		System.out.println(JSON.toJSONString(baseBean));
	}
	
	@Test
	public void testgetQuotaInfoByApptDate() throws Exception{
		BaseBean baseBean = activityService.getQuotaInfoByApptDate("2017-09-23", "1", "C");
		System.out.println(JSON.toJSONString(baseBean));
	}
	
	@Test
	public void testaddNormalApptInfo() throws Exception{
		NormalApptInfoVo info = new NormalApptInfoVo();
		info.setApptDate("2017-09-23");
		info.setApptDistrict("1");//1-梅沙片区,2-大鹏片区 
		info.setApptInterval("1");//1-0点到12点,2-12点到24点
		info.setMobilePhone("15818534918");
		info.setPlateNo("粤A12345");
		info.setPlateType("02");
		info.setVinLastFour("1234");
		BaseBean baseBean = activityService.addNormalApptInfo(info, "C", "openid123123");
		System.out.println(JSON.toJSONString(baseBean));//{"code":"0000","data":"预约编号为：DB100000000641","msg":"预约成功"}
	}
	
	@Test
	public void testgetApptHistoryRecord() throws Exception{
		//BaseBean baseBean = activityService.getApptHistoryRecord("粤A12345", "02", "1234", "15818534918", "C");
		BaseBean baseBean = activityService.getApptHistoryRecord("粤A12345", "02", "1234", "13800138000", "C");
		System.out.println(JSON.toJSONString(baseBean));
	}
	
	@Test
	public void testcancelNormalApptInfo() throws Exception{
		BaseBean baseBean = activityService.cancelNormalApptInfo("DB100000000642", "开发测试", "C");
		System.out.println(JSON.toJSONString(baseBean));
	}
	
	@Test
	public void testaddTempApptInfo() throws Exception{
		NormalApptInfoVo info = new NormalApptInfoVo();
		info.setApptDate("2017-09-23");
		info.setApptDistrict("1");//1-梅沙片区,2-大鹏片区 
		info.setApptInterval("2");//1-0点到12点,2-12点到24点
		info.setMobilePhone("13800138000");
		info.setPlateNo("粤A12345");
		info.setPlateType("02");
		info.setVinLastFour("1234");
		BaseBean baseBean = activityService.addTempApptInfo(info, "C", "openid123123");
		System.out.println(JSON.toJSONString(baseBean));//{"code":"0000","data":"预约编号为:DB100000000642","msg":"临时预约成功"}
	}
}
