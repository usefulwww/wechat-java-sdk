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
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
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

/**
 *
 * @author lyun@nashihou.cn
 */
public class Wechat {
    private Logger logger = LoggerFactory.getLogger(Wechat.class);
    
    private HttpClient client = new HttpClient();
    
    /**
     * 回复消息
     * @param echostr
     * @param in
     * @return
     */
    public String replay(String echostr,Message msg,CallBack callback) {
    	String replay = "";
        if (echostr != null && "".equals(echostr) == false) {
            replay = echostr;
        }
        
    	if(callback!=null){
        	return replay + callback.replay(msg);
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
    public String replay(String echostr,InputStream in,CallBack callback) {
        // 处理接收消息    
        Message msg = new Message();
        msg = parse(in);

        return replay(echostr,msg,callback);
        
    }
    
    public Message parse(InputStream in) {
        Message msg = new Message();
        Field[] fields = msg.getClass().getDeclaredFields();
        try {
            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            DocumentBuilder db = dbf.newDocumentBuilder();
            Document document = db.parse(in);
            logger.debug(document.toString());
            NodeList xmls = document.getElementsByTagName("xml");
			Element xml = (Element) xmls.item(0);
			logger.debug(xml.getNodeName());
			
            NodeList childNodes = xml.getChildNodes();
            logger.debug(""+childNodes.getLength());
            
            for (int i = 0; i < childNodes.getLength(); i++) {
                Node el = childNodes.item(i);
                logger.debug(i+":"+el.getNodeName()+":"+el.getTextContent());
                for (Field f : fields) {
                    if (f.getName().equals(el.getNodeName())) {
                    	
                        try {
                        	Method m = msg.getClass().getDeclaredMethod("set"+f.getName(),f.getType());
                        	if(m!=null) {
	                            if (f.getType() == java.lang.Long.class) {
	                            	m.invoke(msg, Long.parseLong(el.getTextContent()));
	                            } else {
	                            	m.invoke(msg,el.getTextContent());
	                            }
                        	}
                        } catch (IllegalArgumentException ex) {
                            logger.error(ex.getMessage());
                            ex.printStackTrace();
                        } catch (IllegalAccessException ex) {
                            logger.error(ex.getMessage());
                            ex.printStackTrace();
                        } catch (NoSuchMethodException e) {
							e.printStackTrace();
						} catch (SecurityException e) {
							e.printStackTrace();
						} catch (InvocationTargetException e) {
							e.printStackTrace();
						} catch (DOMException e) {
							e.printStackTrace();
						}
                        break;
                    }
                }
            }
        } catch (ParserConfigurationException e) {
            logger.error(e.getMessage());
            e.printStackTrace();
        } catch (SAXException e) {
            logger.error(e.getMessage());
            e.printStackTrace();
        } catch (IOException e) {
            logger.error(e.getMessage());
            e.printStackTrace();
        }
        return msg;
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
            String temp = SHA1(list.get(0) + list.get(1) + list.get(2));  
            return temp.equals(signature);
    }

    /**
     *  SHA1 加密
     * @param str 代加密字符串
     * @return
     */
    public String SHA1(String str) {
        if (str == null) {
            return null;
        }
        try {
            MessageDigest messageDigest = MessageDigest.getInstance("SHA1");
            messageDigest.update(str.getBytes());
            byte[] b = messageDigest.digest();
            String hs = "";
    		String stmp = "";
    		for (int n = 0; n < b.length; n++) {
    			stmp = (java.lang.Integer.toHexString(b[n] & 0XFF));
    			if (stmp.length() == 1)
    				hs = hs + "0" + stmp;
    			else
    				hs = hs + stmp;
    		}
    		return hs.toUpperCase();
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }
    
    /**
     * 获取 accesstoken
     * @return
     */
    public String getAccessToken(String appid,String secret) {
		String url = "https://api.weixin.qq.com/cgi-bin/token?grant_type=client_credential&appid="+appid+"&secret="+secret;
		client.sendGet(url);
		String content = client.getContent();
		logger.debug(content);
		client.close();
		
		//TODO 解析json 获取token信息
		String accessToken = "";
		String regex = "\"access_token\":\"([^\"]+)\"";
		  Pattern p = Pattern.compile(regex);
		  Matcher m = p.matcher(content);
		  if(m.find()){
			  accessToken = m.group(1);
		  }
    	
    	return accessToken;
    }
    
    /**
     * 发送客服信息
     * @param accessToken
     * @param json
     * @return
     */
    public boolean sendCustomMsg(String accessToken,String json) {
    	String url="https://api.weixin.qq.com/cgi-bin/message/custom/send?access_token="+accessToken;
    	client.sendPost(url,json);
    	String content = client.getContent();
		logger.debug(content);
		client.close();
		//TODO 验证是否发送完毕
    	return false;
    }

	
	public String getReplay4Text(Message msg) {
        StringBuilder xml = new StringBuilder();
        xml.append("<xml>");
		xml.append("<ToUserName><![CDATA[").append(msg.getToUserName()).append("]]></ToUserName>");
		xml.append("<FromUserName><![CDATA[").append(msg.getFromUserName()).append("]]></FromUserName>");
		xml.append("<CreateTime>").append(msg.getCreateTime()).append("</CreateTime>");
		xml.append("<MsgType><![CDATA[text]]></MsgType>");
		xml.append("<Content><![CDATA[").append(msg.getContent()).append("]]></Content>");
        xml.append("</xml>");
        return xml.toString();
	}
	
	public String getReplay4Image(Message msg) {
        StringBuilder xml = new StringBuilder();
        xml.append("<xml>");
		xml.append("<ToUserName><![CDATA[").append(msg.getToUserName()).append("]]></ToUserName>");
		xml.append("<FromUserName><![CDATA[").append(msg.getFromUserName()).append("]]></FromUserName>");
		xml.append("<CreateTime>").append(msg.getCreateTime()).append("</CreateTime>");
		xml.append("<MsgType><![CDATA[image]]></MsgType>");
		xml.append("<Image><MediaId><![CDATA[").append(msg.getMediaId()).append("]]></MediaId></Image>");
        xml.append("</xml>");
        return xml.toString();
	}
	
	public String getReplay4Voice(Message msg) {
        StringBuilder xml = new StringBuilder();
        xml.append("<xml>");
		xml.append("<ToUserName><![CDATA[").append(msg.getToUserName()).append("]]></ToUserName>");
		xml.append("<FromUserName><![CDATA[").append(msg.getFromUserName()).append("]]></FromUserName>");
		xml.append("<CreateTime>").append(msg.getCreateTime()).append("</CreateTime>");
		xml.append("<MsgType><![CDATA[voice]]></MsgType>");
		xml.append("<Voice><MediaId><![CDATA[").append(msg.getMediaId()).append("]]></MediaId></Voice>");
        xml.append("</xml>");
        return xml.toString();
	}
	
	public String getReplay4Video(Message msg) {
        StringBuilder xml = new StringBuilder();
        xml.append("<xml>");
		xml.append("<ToUserName><![CDATA[").append(msg.getToUserName()).append("]]></ToUserName>");
		xml.append("<FromUserName><![CDATA[").append(msg.getFromUserName()).append("]]></FromUserName>");
		xml.append("<CreateTime>").append(msg.getCreateTime()).append("</CreateTime>");
		xml.append("<MsgType><![CDATA[video]]></MsgType>");
		xml.append("<Video><MediaId><![CDATA[").append(msg.getMediaId()).append("]]></MediaId></Video>");
        xml.append("</xml>");
        return xml.toString();
	}
	
	public String getReplay4Music(Message msg) {
        StringBuilder xml = new StringBuilder();
        xml.append("<xml>");
		xml.append("<ToUserName><![CDATA[").append(msg.getToUserName()).append("]]></ToUserName>");
		xml.append("<FromUserName><![CDATA[").append(msg.getFromUserName()).append("]]></FromUserName>");
		xml.append("<CreateTime>").append(msg.getCreateTime()).append("</CreateTime>");
		xml.append("<MsgType><![CDATA[music]]></MsgType>");
		xml.append("<Music><Title><![CDATA[")
		.append(msg.getTitle()).append("]]></Title><Description><![CDATA[")
		.append(msg.getDescription()).append("]]></Description><MusicUrl><![CDATA[")
		.append(msg.getMusicUrl()).append("]]></MusicUrl><HQMusicUrl><![CDATA[")
		.append(msg.getHQMusicUrl()).append("]]></HQMusicUrl><ThumbMediaId><![CDATA[")
		.append(msg.getMediaId()).append("]]></ThumbMediaId></Music>");
        xml.append("</xml>");
        return xml.toString();
	}
	
	
	public String getReplay4News(Message msg) {
        StringBuilder xml = new StringBuilder();
        xml.append("<xml>");
		xml.append("<ToUserName><![CDATA[").append(msg.getToUserName()).append("]]></ToUserName>");
		xml.append("<FromUserName><![CDATA[").append(msg.getFromUserName()).append("]]></FromUserName>");
		xml.append("<CreateTime>").append(msg.getCreateTime()).append("</CreateTime>");
		xml.append("<MsgType><![CDATA[news]]></MsgType>");
		List<Message> articles = msg.getArticles();
		if(articles !=null && articles.size()>0) {
        	xml .append("<Articles>");
        	for(Message it : articles) {
				xml.append( "<item>");
				xml.append("<Title><![CDATA["+it.getTitle()+"]]></Title>");
				xml.append("<Description><![CDATA["+it.getDescription()+"]]></Description>");
				xml.append("<PicUrl><![CDATA["+it.getPicUrl()+"]]></PicUrl>");
				xml.append("<Url><![CDATA["+it.getUrl()+"]]></Url>");
				xml.append("</item>");
        	}
        	xml.append("</Articles>");
        }
        xml.append("</xml>");
        return xml.toString();
	}
	
	public String getSend4Text(Message msg) {
		 StringBuilder json = new StringBuilder();
	        json.append("{\"touser\":\"").append(msg.getToUserName()).append("\",\"msgtype\":\"text\",\"text\":{\"content\":\"").append(msg.getContent()).append("\"}}");
	        return json.toString();
	}

	
	public String getSend4Image(Message msg) {
		 StringBuilder json = new StringBuilder();
	        json.append("{\"touser\":\"").append(msg.getToUserName()).append("\",\"msgtype\":\"image\",\"image\":{\"media_id\":\"").append(msg.getMediaId()).append("\"}}");
	        return json.toString();
	}
	
	
	public String getSend4Voice(Message msg) {
		 StringBuilder json = new StringBuilder();
	        json.append("{\"touser\":\"").append(msg.getToUserName()).append("\",\"msgtype\":\"voice\",\"voice\":{\"media_id\":\"").append(msg.getMediaId()).append("\"}}");
	        return json.toString();
	}
	
	public String getSend4Video(Message msg) {
		 StringBuilder json = new StringBuilder();
	        json.append("{\"touser\":\"").append(msg.getToUserName()).append("\",\"msgtype\":\"video\",\"video\":{\"media_id\":\"").append(msg.getMediaId()).append("\",\"thumb_media_id\":\"").append(msg.getThumbMediaId()).append("\",\"title\":\"").append(msg.getTitle()).append("\",\"description\":\"").append(msg.getDescription()).append("\"}}");
	        return json.toString();
	}
	
	public String getSend4Music(Message msg) {
		 StringBuilder json = new StringBuilder();
	        json.append("{\"touser\":\"").append(msg.getToUserName())
	        .append("\",\"msgtype\":\"music\",\"music\":{\"thumb_media_id\":\"")
	        .append(msg.getThumbMediaId())
	        .append("\",\"musicurl\":\"").append(msg.getMusicUrl())
	        .append("\",\"hqmusicurl\":\"").append(msg.getHQMusicUrl())
	        .append("\",\"title\":\"").append(msg.getTitle())
	        .append("\",\"description\":\"").append(msg.getDescription()).append("\"}}");
	        return json.toString();
	}
	
	public String getSend4News(Message msg) {
		 StringBuilder json = new StringBuilder();
	        json.append("{\"touser\":\"").append(msg.getToUserName())
	        .append("\",\"msgtype\":\"news\",\"news\":{\"article\":[");
	        List<Message> articles = msg.getArticles();
	        for(int i =0;i < articles.size();i++) {
	        	Message it = articles.get(i);
	        	json.append("{\"title\":\""+it.getTitle()
	        			+"\",\"description\":\""+it.getDescription()
	        			+"\",\"url\":\""+it.getUrl()
	        			+"\",\"picurl\":\""+it.getPicUrl()+"\"}");
	        	if(i < articles.size()-1){
	        		json.append(",");
	        	}
	        }
	        json.append("]}}");
	        return json.toString();
	}

	public String getReplay4Xml(Message msg) {
        List<Message> articles = msg.getArticles();
        String xml = "<xml>";
        Field[] fields = msg.getClass().getDeclaredFields();
        for (Field field : fields) {
        	String val = "";
            try {
            	if(field.get(this) == null){
            		continue;
            	}
                if(field.getType() == List.class) {
                	logger.debug(field.getType().toString());
                } else if (field.getType() == java.lang.String.class) {
                    val = "<![CDATA[" + field.get(this).toString() + "]]>";
                } else if(field.getType() == java.lang.Long.class) {
                    val = String.valueOf(field.getLong(this));
                }
                if(val!=null && "".equals(val) == false){
                	xml += "<" + field.getName() + ">" + val + "<" + field.getName() + ">";
                }
            } catch (IllegalArgumentException ex) {
               logger.error(ex.getMessage());
            } catch (IllegalAccessException ex) {
               logger.error(ex.getMessage());
            }
        }
        if(articles != null && articles.size()>0) {
        	xml +="<Articles>";
        	for(Message it : articles) {
				xml += "<item>";
				xml += "<Title><![CDATA["+it.getTitle()+"]]></Title>";
				xml += "<Description><![CDATA["+it.getDescription()+"]]></Description>";
				xml += "<PicUrl><![CDATA["+it.getPicUrl()+"]]></PicUrl>";
				xml += "<Url><![CDATA["+it.getUrl()+"]]></Url>";
				xml += "</item>";
        	}
        	xml +="</Articles>";
        }
        xml += "</xml>";
        return xml;
    }


}
