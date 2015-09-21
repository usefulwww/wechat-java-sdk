package com.usefulwww.core.wechat;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

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

public abstract class WechatUtil {
	
	private static Logger logger = LoggerFactory.getLogger(WechatUtil.class);
	
	
	 /**
     *  SHA1 加密
     * @param str 代加密字符串
     * @return
     */
    public static String SHA1(String str) {
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
            
    		//以下算法结果同上
//            int len = b.length;  
//            StringBuilder buf = new StringBuilder(len * 2);  
//            // 把密文转换成十六进制的字符串形式  
//            char[] HEX_DIGITS = {'0', '1', '2', '3', '4', '5',  
//                    '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f'}; 
//            for (int j = 0; j < len; j++) {  
//                buf.append(HEX_DIGITS[(b[j] >> 4) & 0x0f]);  
//                buf.append(HEX_DIGITS[b[j] & 0x0f]);  
//            }  
//            return buf.toString().toUpperCase(); 
            
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
        

    }
    
    /**
	 * MD5编码
	 * @param origin 原始字符串
	 * @return 经过MD5加密之后的结果
	 */
	public static String MD5(String origin) {
		 String[] hexDigits = {"0", "1", "2", "3", "4", "5", "6", "7",
			        "8", "9", "a", "b", "c", "d", "e", "f"};
		 
	    String resultString = null;
	    try {
	        resultString = origin;
	        MessageDigest md = MessageDigest.getInstance("MD5");
	        
	        byte[] b = md.digest(resultString.getBytes());
	        
	      //转换字节数组为16进制字串
		    StringBuilder resultSb = new StringBuilder();
		    for (byte aB : b) {
		    	//转换byte到16进制
		    	int n = aB;
		 	    if (n < 0) {
		 	        n = 256 + n;
		 	    }
		 	    int d1 = n / 16;
		 	    int d2 = n % 16;
		    	String hex = hexDigits[d1] + hexDigits[d2];
		        resultSb.append(hex);
		    }
	        
	        resultString = resultSb.toString();
	    } catch (Exception e) {
	        e.printStackTrace();
	    }
	    return resultString;
	}
		
    /**
     * 获取一定长度的随机字符串
     * @param length 指定字符串长度
     * @return 一定长度的字符串
     */
    public static  String getRandomStringByLength(int length) {
        String base = "abcdefghijklmnopqrstuvwxyz0123456789";
        Random random = new Random();
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < length; i++) {
            int number = random.nextInt(base.length());
            sb.append(base.charAt(number));
        }
        return sb.toString();
    }
	

    public static Map<String,String> getMapFromXML(String xmlString) throws ParserConfigurationException, IOException, SAXException {

        //这里用Dom的方式解析回包的最主要目的是防止API新增回包字段
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        
        InputStream is =   new ByteArrayInputStream(xmlString.getBytes());
        Document document = builder.parse(is);

        //获取到document里面的全部结点
        NodeList allNodes = document.getFirstChild().getChildNodes();
        Node node;
        Map<String, String> map = new HashMap<String, String>();
        int i=0;
        while (i < allNodes.getLength()) {
            node = allNodes.item(i);
            if(node instanceof Element){
                map.put(node.getNodeName(),node.getTextContent());
            }
            i++;
        }
        return map;

    }
    
    public static Message parse(InputStream in) {
        Message msg = null;
        
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
            
            msg = new Message();
            Field[] fields = msg.getClass().getDeclaredFields();
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

	public static String getReply4Text(Message msg) {
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
	
	public static String getReply4Image(Message msg) {
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
	
	public static String getReply4Voice(Message msg) {
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
	
	public static String getReply4Video(Message msg) {
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
	
	public static String getReply4Music(Message msg) {
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
	
	
	public static String getReply4News(Message msg) {
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
	
	public static String getSend4Text(Message msg) {
		 StringBuilder json = new StringBuilder();
	        json.append("{\"touser\":\"").append(msg.getToUserName()).append("\",\"msgtype\":\"text\",\"text\":{\"content\":\"").append(msg.getContent()).append("\"}}");
	        return json.toString();
	}

	
	public static String getSend4Image(Message msg) {
		 StringBuilder json = new StringBuilder();
	        json.append("{\"touser\":\"").append(msg.getToUserName()).append("\",\"msgtype\":\"image\",\"image\":{\"media_id\":\"").append(msg.getMediaId()).append("\"}}");
	        return json.toString();
	}
	
	
	public static String getSend4Voice(Message msg) {
		 StringBuilder json = new StringBuilder();
	        json.append("{\"touser\":\"").append(msg.getToUserName()).append("\",\"msgtype\":\"voice\",\"voice\":{\"media_id\":\"").append(msg.getMediaId()).append("\"}}");
	        return json.toString();
	}
	
	public static String getSend4Video(Message msg) {
		 StringBuilder json = new StringBuilder();
	        json.append("{\"touser\":\"").append(msg.getToUserName()).append("\",\"msgtype\":\"video\",\"video\":{\"media_id\":\"").append(msg.getMediaId()).append("\",\"thumb_media_id\":\"").append(msg.getThumbMediaId()).append("\",\"title\":\"").append(msg.getTitle()).append("\",\"description\":\"").append(msg.getDescription()).append("\"}}");
	        return json.toString();
	}
	
	public static String getSend4Music(Message msg) {
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
	
	public static String getSend4News(Message msg) {
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

	public static String getReply4Xml(Message msg) {
        List<Message> articles = msg.getArticles();
        String xml = "<xml>";
        Field[] fields = msg.getClass().getDeclaredFields();
        for (Field field : fields) {
        	String val = "";
            try {
            	if(field.get(msg) == null){
            		continue;
            	}
                if(field.getType() == List.class) {
                	logger.debug(field.getType().toString());
                } else if (field.getType() == java.lang.String.class) {
                    val = "<![CDATA[" + field.get(msg).toString() + "]]>";
                } else if(field.getType() == java.lang.Long.class) {
                    val = String.valueOf(field.getLong(msg));
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
