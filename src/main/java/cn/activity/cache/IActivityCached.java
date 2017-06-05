package cn.activity.cache;


public interface IActivityCached extends ICacheKey {
    /**
     * 更新accessToken
     * @param userId
     * @param accessToken
     * @return
     */
    public void updateAccessToken(String userId, String accessToken);
    /**
     * 更新refreshToken
     * @param userId
     * @param refreshToken
     * @return
     */
    public void updateRefreshToken(String userId, String refreshToken);
    
    /**
     * 插入加密token
     * @param userId
     */
    public void insertEncyptAccessToken(String encyptAccessToken, String AccessToken);
    /**
     * 获取加密token
     * @param userId
     * @return
     */
    public String getAccessTokenFromEncypt(String encyptAccessToken);
    
    /**
     * 添加用户验证码到redis
     * @param mobilephone
     * @param validateCode
     */
    public void insertUserValidateCode(String mobilephone,String validateCode);
    /**
     * 限制一个手机号短信5秒发一次
     * @param mobilephone
     */
    public void sendSmsFreqLimit(String mobilephone);
    /**
     * 根据手机号+limit获取
     * @param mobilephone
     * @return
     */
    public String getSendSmsFreqLimit(String mobilephone);
    /**
     * 根据手机号从redis取验证码
     * @param mobilephone
     * @return
     */
    public String getUserValidateCode(String mobilephone);
    /**
     * 根据key查询须知文档
     * @param key
     * @return
     */
    public String getDocumentByKey(String key);
    /**
     * 添加须知到缓存
     * @param key
     * @param value
     * @return
     */
    public boolean setDucoment(String key,String value);
}
