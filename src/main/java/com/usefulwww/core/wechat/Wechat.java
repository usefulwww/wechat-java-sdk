/*
 * Copyright 2014 usefulwww.com.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.usefulwww.core.wechat;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.URLEncoder;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.DOMException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import com.usefulwww.core.wechat.HttpClient.METHOD;


/**
 *
 * @author lyun@nashihou.cn
 */
public class Wechat {
    private Logger logger = LoggerFactory.getLogger(Wechat.class);
    
    private String appId;
    
    private String appSecret;
    
    private String _accesstoken;
	private Date _accesstoken_date;
	
	private String _jsticket;
	private Date _jsticket_date;
	
    public void setOptions(String appId,String appSecret){
    	this.appId = appId;
    	this.appSecret = appSecret;
    }
    
    /**
     * 回复消息
     * @param echostr
     * @param in
     * @return
     */
     public String reply(String echostr,Message msg,CallBack callback) {
    	String replay = "";
        if (echostr != null && "".equals(echostr) == false) {
            replay = echostr;
        }
        
    	if(callback!=null){
        	return replay + callback.reply(msg);
        } else {
        	return null;
        }
    }
    /**
     * 回复消息
     * @param echostr
     * @param in
     * @return
     */
    public String reply(String echostr,InputStream in,CallBack callback) {
        // 处理接收消息    
        Message msg = new Message();
        msg = WechatUtil.parse(in);

        return reply(echostr,msg,callback);
        
    }
    
    /**
     * 发送消息
     * @param msg
     * @return
     */
    public boolean send(Message msg){
    	if(null==this.appId){
    		logger.error("请使用setOptions()方法设置appId和appSecret");
    		return false;
    	}
    	 
    	String accesstoken = this.getAccessToken(this.appId,this.appSecret);
    	
    	String json = "";
    	switch (MessageType.valueOf(msg.getMsgType())) {
			case image:
				json = WechatUtil.getSend4Image(msg);
				break;
			case video:
				json = WechatUtil.getSend4Video(msg);
				break;
			case voice:
				json = WechatUtil.getSend4Voice(msg);
				break;
			case location:
				break;
			case link:
				break;
			case text:
				json = WechatUtil.getSend4Text(msg);
				break;
			case news:
				json = WechatUtil.getSend4News(msg);
			default:
				break;
		}
    	
    	logger.debug("acctoken:"+accesstoken+"/"+json);
    	
    	sendCustomMsg(accesstoken,json);
    	return false;
    }
    
    
    /**
     * 
     * @param token
     * @param signature// 微信加密签名  
     * @param timestamp// 时间戳  
     * @param nonce// 随机数  
     * @param echostr//随机字符串
     * @return
     */
    public boolean vaild (String token,String signature,String timestamp,String nonce,String echostr) {
        //验证URL真实性    
            List<String> list = new ArrayList<String>();  
            list.add(token);  
            list.add(timestamp);  
            list.add(nonce);
            //1. 将token、timestamp、nonce三个参数进行字典序排序  
            Collections.sort(list, new Comparator<String>() {  
                @Override  
                public int compare(String o1, String o2) {  
                    return o1.compareTo(o2);  
                }  
            });  
            //2. 将三个参数字符串拼接成一个字符串进行sha1加密  
            String temp = WechatUtil.SHA1(list.get(0) + list.get(1) + list.get(2));  
            logger.debug(temp+"/"+signature);
            return temp.equalsIgnoreCase(signature);
    }

   
    
    /**
     * 获取 accesstoken
     * @return
     */
    public String getAccessToken(String appid,String secret) {
    	
    	if(_accesstoken_date==null){
    		_accesstoken_date = new Date();
    	}
    	
    	Date now  =new Date();
    	long delay = now.getTime() - _accesstoken_date.getTime();
    	if(_accesstoken!=null &&  delay<=5000000){
    		return _accesstoken;
    	}
    	
    	_accesstoken_date = now;
    	HttpClient client = new HttpClient();
    	String url = "https://api.weixin.qq.com/cgi-bin/token?grant_type=client_credential&appid="+appid+"&secret="+secret;
		client.send(url,HttpClient.METHOD.GET);
		String content = client.getContent();
		logger.debug(content);
		client.close();
		
		//TODO 解析json 获取token信息
		String regex = "\"access_token\":\"([^\"]+)\"";
		  Pattern p = Pattern.compile(regex);
		  Matcher m = p.matcher(content);
		  if(m.find()){
			  _accesstoken = m.group(1);
		  }
    	
    	return _accesstoken;
    }
    public String getJsTicket(String appid,String secret){
    	if(_jsticket_date == null) {
    		_jsticket_date = new Date();
    	}
    	
    	Date now = new Date();
    	
    	long delay = now.getTime() - _jsticket_date.getTime();
    	if(_jsticket!=null && delay <=5000000){
    		return _jsticket;
    	}
    	
    	_jsticket_date = now;
    	
    	String accessToken = this.getAccessToken(appid, secret);
    	HttpClient client = new HttpClient();
    	String url = "https://api.weixin.qq.com/cgi-bin/ticket/getticket?access_token="+accessToken+"&type=jsapi";
		client.send(url,HttpClient.METHOD.GET);
		String content = client.getContent();
		logger.debug(content);
		client.close();
		
		// TODO 解析json 获取ticket信息
		String regex = "\"ticket\":\"([^\"]+)\"";
		Pattern p = Pattern.compile(regex);
		Matcher m = p.matcher(content);
		if (m.find()) {
			_jsticket = m.group(1);
		}

		return _jsticket;
    }
    
    public String getJsSign(Map<String,String> map){
    	ArrayList<String> list = new ArrayList<String>();
        for(Map.Entry<String,String> entry:map.entrySet()){
            if(entry.getValue()!=""){
                list.add(entry.getKey() + "=" + entry.getValue() + "&");
            }
        }
        int size = list.size();
        String [] arrayToSort = list.toArray(new String[size]);
        Arrays.sort(arrayToSort, String.CASE_INSENSITIVE_ORDER);
        StringBuilder sb = new StringBuilder();
        for(int i = 0; i < size; i ++) {
            sb.append(arrayToSort[i]);
        }
        String result = sb.toString().substring(0,sb.length()-1);
        logger.debug(result);
        result = WechatUtil.SHA1(result);
        return result.toLowerCase();
    }
    
    /**
     * 
     * @param appid
     * @param url
     * @param scope snsapi_base/snsapi_userinfo
     * @param state
     * @return
     * @throws UnsupportedEncodingException 
     */
    public String getOAuthUrl(String url,String scope,String state,String appid){
    	try {
			url = URLEncoder.encode(url,"UTF-8");
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	return "https://open.weixin.qq.com/connect/oauth2/authorize?appid="+appid+"&redirect_uri="+ url +"&response_type=code&scope="+scope+"&state="+state+"#wechat_redirect";
    }
    
    public Map<String,String> getOAuthAccessToken(String code,String appid,String appSecret){
    	String url="https://api.weixin.qq.com/sns/oauth2/access_token?appid="+appid+"&secret="+appSecret+"&code="+code+"&grant_type=authorization_code";
    	/*var json={
    		   "access_token":"ACCESS_TOKEN",
    		   "expires_in":7200,
    		   "refresh_token":"REFRESH_TOKEN",
    		   "openid":"OPENID",
    		   "scope":"SCOPE",
    		   "unionid": "o6_bmasdasdsad6_2sgVt7hMZOPfL"
    		}*/
    	HttpClient client = new HttpClient();
    	client.send(url, HttpClient.METHOD.GET);
    	String content = client.getContent();
    	client.close();
    	
    	Map<String,String> map = new HashMap<String, String>();
    	
    	String regex = "\"([^\"]+)\":\"([^\"]+)\"";
		Pattern p = Pattern.compile(regex);
		Matcher m = p.matcher(content);
		while (m.find()) {
			map.put( m.group(1),m.group(2));
		}
    	
    	return map;
    }
    
    /**
     * 签名算法
     * @param o 要参与签名的数据对象
     * @return 签名
     * @throws IllegalAccessException
     */
	public  String getPaySign(Map<String,String> map,String key){
        ArrayList<String> list = new ArrayList<String>();
        for(Map.Entry<String,String> entry:map.entrySet()){
            if(entry.getValue()!=""){
                list.add(entry.getKey() + "=" + entry.getValue() + "&");
            }
        }
        int size = list.size();
        String [] arrayToSort = list.toArray(new String[size]);
        Arrays.sort(arrayToSort, String.CASE_INSENSITIVE_ORDER);
        StringBuilder sb = new StringBuilder();
        for(int i = 0; i < size; i ++) {
            sb.append(arrayToSort[i]);
        }
        String post = sb.toString();
        post += "key=" + key;
        logger.debug("Sign Before MD5:" + post);
        String result = WechatUtil.MD5(post).toUpperCase();
        logger.debug("Sign Result:" + result);
        return result;
    }

  
    
    public Map<String, String> unifiedorder(Map<String,String> map){
    	StringBuilder post =new StringBuilder();
    	post.append("<xml>");
    	for(String key :map.keySet()){
    		post.append("<").append(key).append(">").append(map.get(key)).append("</").append(key).append(">");
    	}
    	post.append("</xml>");
    	
    	String url = "https://api.mch.weixin.qq.com/pay/unifiedorder";
    	
    	HttpClient client = new HttpClient();
    	
    	boolean success = client.send(url, HttpClient.METHOD.POST, post.toString());
    	
    	if(success) {
    		try {
    			String content = client.getContent();
    			logger.debug(content);
				return WechatUtil.getMapFromXML(content);
			} catch (ParserConfigurationException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			} catch (SAXException e) {
				e.printStackTrace();
			}
    	}
    	
    	return null;
    }
    
    
    /**
     * 发送客服信息
     * @param accessToken
     * @param json
     * @return
     */
    public boolean sendCustomMsg(String accessToken,String json) {
    	HttpClient client = new HttpClient();
    	String url="https://api.weixin.qq.com/cgi-bin/message/custom/send?access_token="+accessToken;
    	client.send(url,HttpClient.METHOD.POST,json);
    	String content = client.getContent();
		logger.debug(content);
		client.close();
		//TODO 验证是否发送完毕
    	return false;
    }
    
    /**
     * 菜单创建
     * @param accessToken
     * @param json
     * @return
     */
	public boolean createMenu(String accessToken,String json){
		HttpClient client = new HttpClient();
		String url = "https://api.weixin.qq.com/cgi-bin/menu/create?access_token="+accessToken;
		boolean isOk = client.send(url, HttpClient.METHOD.POST,json);
		return isOk;
	}
	
	/**
	 * 获取用户基本信息
	 * @param accessToken
	 * @param openId
	 * @return
	 * @throws ParseException 
	 */
	public User getUserInfo(String appid,String appsecret,String openId){
		String accessToken = this.getAccessToken(appid, appsecret);
		HttpClient client = new HttpClient();
		String url = "https://api.weixin.qq.com/cgi-bin/user/info?access_token="+accessToken+"&openid="+openId+"&lang=zh_CN";
		client.send(url, HttpClient.METHOD.GET);
		//解析json
		String content = client.getContent();
		
		Map<String,String> map = new HashMap<String, String>();
    	
    	String regex = "\"([^\"]+)\":\"([^\"]+)\"";
		Pattern p = Pattern.compile(regex);
		Matcher m = p.matcher(content);
		while (m.find()) {
			map.put( m.group(1),m.group(2));
		}
		
		User user = new User();
		user.setSubscribe(map.get("subscribe"));
		user.setOpenId(map.get("openid"));
		user.setNickName(map.get("nickname"));
		user.setSex(map.get("sex"));
		user.setLanguage(map.get("language"));
		user.setCity(map.get("city"));
		user.setProvince(map.get("province"));
		user.setCountry(map.get("country"));
		user.setHeadImgUrl(map.get("headImgUrl"));
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		try {
			user.setSubscribeTime(sdf.parse(map.get("subscribe_time")));
		} catch (ParseException e) {
		}
		user.setRemark(map.get("remark"));
		user.setGroupid(map.get("groupid"));
		
		return user;
	}
	
	
}
