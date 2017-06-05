package cn.activity.cache.impl;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import cn.activity.cache.IActivityCached;
import cn.sdk.cache.ICacheManger;
import cn.sdk.serialization.ISerializeManager;



@Service
public class IActivityCachedImpl implements IActivityCached{
	protected Logger log = Logger.getLogger(this.getClass());
	
	/**
	 * 用户id
	 */
	@Value("${userid}")
    private String userid;
	/**
	 * 用户密码
	 */
    @Value("${userpwd}")
    private String userpwd;
    
    /**
     * 请求地址
     */
    @Value("${url}")
    private String url;
    /**
     * 方法
     */
    @Value("${method}")
    private String method;
    /**
     * 秘钥
     */
    @Value("${key}")
    private String key;
    
    @Value("${refreshTokenTime}")
    private int refreshTokenTime;
    
    @Value("${encyptAccessTokenTime}")
    private int encyptAccessTokenTime;
    
    @Value("${accessTokentime}")
    private int accessTokenTime;
    
	@Autowired
	@Qualifier("jedisCacheManagerImpl")
	private ICacheManger<String> cacheManger;
	
	@Autowired
	@Qualifier("jedisCacheManagerImpl")
	private ICacheManger<Object> objectcacheManger;
	
	@Autowired
	private ISerializeManager< Map<String, String> > serializeManager;
	
    public static final String arrayToString(byte[] bytes)
    {
        StringBuffer buff = new StringBuffer();
        for (int i = 0; i < bytes.length; i++)
        {
            buff.append(bytes[i] + " ");
        }
        return buff.toString();
    }

	public Logger getLog() {
		return log;
	}

	public void setLog(Logger log) {
		this.log = log;
	}

	public String getUserid() {
		return userid;
	}

	public void setUserid(String userid) {
		this.userid = userid;
	}

	public String getUserpwd() {
		return userpwd;
	}

	public void setUserpwd(String userpwd) {
		this.userpwd = userpwd;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getMethod() {
		return method;
	}

	public void setMethod(String method) {
		this.method = method;
	}

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public int getRefreshTokenTime() {
		return refreshTokenTime;
	}

	public void setRefreshTokenTime(int refreshTokenTime) {
		this.refreshTokenTime = refreshTokenTime;
	}

	public int getEncyptAccessTokenTime() {
		return encyptAccessTokenTime;
	}

	public void setEncyptAccessTokenTime(int encyptAccessTokenTime) {
		this.encyptAccessTokenTime = encyptAccessTokenTime;
	}

	public int getAccessTokenTime() {
		return accessTokenTime;
	}

	public void setAccessTokenTime(int accessTokenTime) {
		this.accessTokenTime = accessTokenTime;
	}

	public ICacheManger<String> getCacheManger() {
		return cacheManger;
	}

	public void setCacheManger(ICacheManger<String> cacheManger) {
		this.cacheManger = cacheManger;
	}

	public ICacheManger<Object> getObjectcacheManger() {
		return objectcacheManger;
	}

	public void setObjectcacheManger(ICacheManger<Object> objectcacheManger) {
		this.objectcacheManger = objectcacheManger;
	}

	public ISerializeManager<Map<String, String>> getSerializeManager() {
		return serializeManager;
	}

	public void setSerializeManager(ISerializeManager<Map<String, String>> serializeManager) {
		this.serializeManager = serializeManager;
	}

	@Override
	public void updateAccessToken(String userId, String accessToken) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void updateRefreshToken(String userId, String refreshToken) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void insertEncyptAccessToken(String encyptAccessToken, String AccessToken) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public String getAccessTokenFromEncypt(String encyptAccessToken) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void insertUserValidateCode(String mobilephone, String validateCode) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void sendSmsFreqLimit(String mobilephone) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public String getSendSmsFreqLimit(String mobilephone) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getUserValidateCode(String mobilephone) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getDocumentByKey(String key) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean setDucoment(String key, String value) {
		// TODO Auto-generated method stub
		return false;
	}
    
    
}
