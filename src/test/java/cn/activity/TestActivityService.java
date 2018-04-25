package cn.activity;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.alibaba.fastjson.JSON;

import cn.activity.bean.HotelInfoVo;
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
		BaseBean baseBean = activityService.getNormalApptDate("Z","DPPQ");
		System.out.println(JSON.toJSONString(baseBean));
	}
	
	@Test
	public void testgetQuotaInfoByApptDate() throws Exception{
		BaseBean baseBean = activityService.getQuotaInfoByApptDate("2018-04-29", "DPPQ", "C");
		System.out.println(JSON.toJSONString(baseBean));
	}
	
	@Test
	public void testaddNormalApptInfo() throws Exception{
		NormalApptInfoVo info = new NormalApptInfoVo();
		info.setApptDate("2018-04-29");
		info.setApptDistrict("DPPQ");//1-梅沙片区,2-大鹏片区 
		info.setApptInterval("1");//1-0点到12点,2-12点到24点
		info.setMobilePhone("15818534918");
		info.setPlateNo("粤A12346");
		info.setPlateType("02");
		info.setVinLastFour("1234");
		info.setCch("D10001");
		info.setShi("测试");
		info.setQu("测试");
		info.setLuduan("测试");
		info.setJiedao("测试");
		info.setCfdd("测试");
		BaseBean baseBean = activityService.addNormalApptInfo(info, "C", "openid123123");
		System.out.println(JSON.toJSONString(baseBean));//{"code":"0000","data":"预约编号为：DB100000000641","msg":"预约成功"}
	}
	
	@Test
	public void testgetApptHistoryRecord() throws Exception{
		//BaseBean baseBean = activityService.getApptHistoryRecord("粤A12345", "02", "1234", "15818534918", "C");
//		BaseBean baseBean = activityService.getApptHistoryRecord("粤A12347", "02", "1234", "13800138000", "C");
		BaseBean baseBean = activityService.getApptHistoryRecord("", "", "", "13510021480", "C");
		System.out.println(JSON.toJSONString(baseBean));
	}
	
	@Test
	public void testcancelNormalApptInfo() throws Exception{
		BaseBean baseBean = activityService.cancelNormalApptInfo("LS100000000681", null, "C");
		System.out.println(JSON.toJSONString(baseBean));
	}
	
	@Test
	public void testaddTempApptInfo() throws Exception{
		NormalApptInfoVo info = new NormalApptInfoVo();
		info.setApptDate("2018-04-22");
		info.setApptDistrict("DPPQLS");//1-梅沙片区,2-大鹏片区 
		info.setApptInterval("2");//1-0点到12点,2-12点到24点
		info.setMobilePhone("13800138000");
		info.setPlateNo("粤A12347");
		info.setPlateType("02");
		info.setVinLastFour("1234");
		info.setCch("");
		info.setShi("测试");
		info.setQu("测试");
		info.setLuduan("");
		info.setJiedao("测试");
		info.setCfdd("测试");
		BaseBean baseBean = activityService.addTempApptInfo(info, "C", "openid123123");
		System.out.println(JSON.toJSONString(baseBean));//{"code":"0000","data":"预约编号为:DB100000000642","msg":"临时预约成功"}
	}
//	组织机构代码：100006585
//	密码：888888
	
	@Test
	public void testgetHotelInfoByCode() throws Exception{
		BaseBean baseBean = activityService.getHotelInfoByCode("100006585");
		System.out.println(JSON.toJSONString(baseBean));
	}
	
	@Test
	public void testloginViaHotel() throws Exception{
		HotelInfoVo vo = new HotelInfoVo();
		vo.setAgencyCode("100006585");
		vo.setBranchCode("003");
		BaseBean baseBean = activityService.loginViaHotel(vo, "888888", "C");
		System.out.println(JSON.toJSONString(baseBean));
	}
	
	
	
	@Test
	public void testgetHotelQuotaInfo() throws Exception{
		HotelInfoVo vo = new HotelInfoVo();
		vo.setAgencyCode("100006585");
		vo.setBranchCode("003");
		BaseBean baseBean = activityService.getHotelQuotaInfo("2018-04-22", "DPPQ", vo, "C");
		System.out.println(JSON.toJSONString(baseBean));
	}
}
