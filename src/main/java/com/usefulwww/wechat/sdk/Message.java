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

package com.usefulwww.wechat.sdk;

import java.lang.reflect.Field;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author lyun@nashihou.cn
 */
public class Message {
	
	private Logger logger = LoggerFactory.getLogger(this.getClass());
	
    private String ToUserName;  
    private String FromUserName;  
    private Long CreateTime;  
    private String MsgType = "text";  
    private Long MsgId;  
    // 文本消息  
    private String Content;  
    // 图片消息  
    private String PicUrl;  
    // 位置消息  
    private String LocationX;  
    private String LocationY;  
    private Long Scale;  
    private String Label;  
    // 链接消息  
    private String Title;  
    private String Description;  
    private String Url;  
    // 语音信息  
    private String MediaId;  
    private String Format;  
    private String Recognition;  
    // 事件  
    private String Event;  
    private String EventKey;  
    private String Ticket;  
    // 音乐
    private String MusicUrl;
    private String HQMusicUrl;
    private String ThumbMediaId;
    
    // 图文信息
    private List<Message> Articles;
  
    public String getToUserName() {  
        return ToUserName;  
    }  
  
    public void setToUserName(String toUserName) {  
        ToUserName = toUserName;  
    }  
  
    public String getFromUserName() {  
        return FromUserName;  
    }  
  
    public void setFromUserName(String fromUserName) {  
        FromUserName = fromUserName;  
    }  
  
    public Long getCreateTime() {  
        return CreateTime;  
    }  
  
    public void setCreateTime(Long createTime) {  
        CreateTime = createTime;  
    }  
  
    public String getMsgType() {  
        return MsgType;  
    }  
  
    public void setMsgType(String msgType) {  
        MsgType = msgType;  
    }  
  
    public Long getMsgId() {  
        return MsgId;  
    }  
  
    public void setMsgId(Long msgId) {  
        MsgId = msgId;  
    }  
  
    public String getContent() {  
        return Content;  
    }  
  
    public void setContent(String content) {  
        Content = content;  
    }  
  
    public String getPicUrl() {  
        return PicUrl;  
    }  
  
    public void setPicUrl(String picUrl) {  
        PicUrl = picUrl;  
    }  
  
    public String getLocationX() {  
        return LocationX;  
    }  
  
    public void setLocationX(String locationX) {  
        LocationX = locationX;  
    }  
  
    public String getLocationY() {  
        return LocationY;  
    }  
  
    public void setLocationY(String locationY) {  
        LocationY = locationY;  
    }  
  
    public Long getScale() {  
        return Scale;  
    }  
  
    public void setScale(Long scale) {  
        Scale = scale;  
    }  
  
    public String getLabel() {  
        return Label;  
    }  
  
    public void setLabel(String label) {  
        Label = label;  
    }  
  
    public String getTitle() {  
        return Title;  
    }  
  
    public void setTitle(String title) {  
        Title = title;  
    }  
  
    public String getDescription() {  
        return Description;  
    }  
  
    public void setDescription(String description) {  
        Description = description;  
    }  
  
    public String getUrl() {  
        return Url;  
    }  
  
    public void setUrl(String url) {  
        Url = url;  
    }  
  
    public String getEvent() {  
        return Event;  
    }  
  
    public void setEvent(String event) {  
        Event = event;  
    }  
  
    public String getEventKey() {  
        return EventKey;  
    }  
  
    public void setEventKey(String eventKey) {  
        EventKey = eventKey;  
    }  
  
    public String getMediaId() {  
        return MediaId;  
    }  
  
    public void setMediaId(String mediaId) {  
        MediaId = mediaId;  
    }  
  
    public String getFormat() {  
        return Format;  
    }  
  
    public void setFormat(String format) {  
        Format = format;  
    }  
  
    public String getRecognition() {  
        return Recognition;  
    }  
  
    public void setRecognition(String recognition) {  
        Recognition = recognition;  
    }  
  
    public String getTicket() {  
        return Ticket;  
    }  
  
    public void setTicket(String ticket) {  
        Ticket = ticket;  
    }  
    
    public String getMusicUrl() {
		return MusicUrl;
	}

	public void setMusicUrl(String musicUrl) {
		MusicUrl = musicUrl;
	}

	public String getHQMusicUrl() {
		return HQMusicUrl;
	}

	public void setHQMusicUrl(String hQMusicUrl) {
		HQMusicUrl = hQMusicUrl;
	}

	public String getThumbMediaId() {
		return ThumbMediaId;
	}

	public void setThumbMediaId(String thumbMediaId) {
		ThumbMediaId = thumbMediaId;
	}

	public List<Message> getArticles() {
		return Articles;
	}

	public void setArticles(List<Message> articles) {
		Articles = articles;
	}
	
	public String toReplay4Text() {
        StringBuilder xml = new StringBuilder();
        xml.append("<xml>");
		xml.append("<ToUserName><![CDATA[").append(this.getToUserName()).append("]]></ToUserName>");
		xml.append("<FromUserName><![CDATA[").append(this.getFromUserName()).append("]]></FromUserName>");
		xml.append("<CreateTime>").append(this.getCreateTime()).append("</CreateTime>");
		xml.append("<MsgType><![CDATA[text]]></MsgType>");
		xml.append("<Content><![CDATA[").append(this.getContent()).append("]]></Content>");
        xml.append("</xml>");
        return xml.toString();
	}
	
	public String toReplay4Image() {
        StringBuilder xml = new StringBuilder();
        xml.append("<xml>");
		xml.append("<ToUserName><![CDATA[").append(this.getToUserName()).append("]]></ToUserName>");
		xml.append("<FromUserName><![CDATA[").append(this.getFromUserName()).append("]]></FromUserName>");
		xml.append("<CreateTime>").append(this.getCreateTime()).append("</CreateTime>");
		xml.append("<MsgType><![CDATA[image]]></MsgType>");
		xml.append("<Image><MediaId><![CDATA[").append(this.getMediaId()).append("]]></MediaId></Image>");
        xml.append("</xml>");
        return xml.toString();
	}
	
	public String toReplay4Voice() {
        StringBuilder xml = new StringBuilder();
        xml.append("<xml>");
		xml.append("<ToUserName><![CDATA[").append(this.getToUserName()).append("]]></ToUserName>");
		xml.append("<FromUserName><![CDATA[").append(this.getFromUserName()).append("]]></FromUserName>");
		xml.append("<CreateTime>").append(this.getCreateTime()).append("</CreateTime>");
		xml.append("<MsgType><![CDATA[voice]]></MsgType>");
		xml.append("<Voice><MediaId><![CDATA[").append(this.getMediaId()).append("]]></MediaId></Voice>");
        xml.append("</xml>");
        return xml.toString();
	}
	
	public String toReplay4Video() {
        StringBuilder xml = new StringBuilder();
        xml.append("<xml>");
		xml.append("<ToUserName><![CDATA[").append(this.getToUserName()).append("]]></ToUserName>");
		xml.append("<FromUserName><![CDATA[").append(this.getFromUserName()).append("]]></FromUserName>");
		xml.append("<CreateTime>").append(this.getCreateTime()).append("</CreateTime>");
		xml.append("<MsgType><![CDATA[video]]></MsgType>");
		xml.append("<Video><MediaId><![CDATA[").append(this.getMediaId()).append("]]></MediaId></Video>");
        xml.append("</xml>");
        return xml.toString();
	}
	
	public String toReplay4Music() {
        StringBuilder xml = new StringBuilder();
        xml.append("<xml>");
		xml.append("<ToUserName><![CDATA[").append(this.getToUserName()).append("]]></ToUserName>");
		xml.append("<FromUserName><![CDATA[").append(this.getFromUserName()).append("]]></FromUserName>");
		xml.append("<CreateTime>").append(this.getCreateTime()).append("</CreateTime>");
		xml.append("<MsgType><![CDATA[music]]></MsgType>");
		xml.append("<Music><Title><![CDATA[")
		.append(this.getTitle()).append("]]></Title><Description><![CDATA[")
		.append(this.getDescription()).append("]]></Description><MusicUrl><![CDATA[")
		.append(this.getMusicUrl()).append("]]></MusicUrl><HQMusicUrl><![CDATA[")
		.append(this.getHQMusicUrl()).append("]]></HQMusicUrl><ThumbMediaId><![CDATA[")
		.append(this.getMediaId()).append("]]></ThumbMediaId></Music>");
        xml.append("</xml>");
        return xml.toString();
	}
	
	
	public String toReplay4News() {
        StringBuilder xml = new StringBuilder();
        xml.append("<xml>");
		xml.append("<ToUserName><![CDATA[").append(this.getToUserName()).append("]]></ToUserName>");
		xml.append("<FromUserName><![CDATA[").append(this.getFromUserName()).append("]]></FromUserName>");
		xml.append("<CreateTime>").append(this.getCreateTime()).append("</CreateTime>");
		xml.append("<MsgType><![CDATA[news]]></MsgType>");
		if(this.Articles !=null && this.Articles.size()>0) {
        	xml .append("<Articles>");
        	for(Message msg : this.Articles) {
				xml.append( "<item>");
				xml.append("<Title><![CDATA["+msg.getTitle()+"]]></Title>");
				xml.append("<Description><![CDATA["+msg.getDescription()+"]]></Description>");
				xml.append("<PicUrl><![CDATA["+msg.getPicUrl()+"]]></PicUrl>");
				xml.append("<Url><![CDATA["+msg.getUrl()+"]]></Url>");
				xml.append("</item>");
        	}
        	xml.append("</Articles>");
        }
        xml.append("</xml>");
        return xml.toString();
	}
	
	public String toSend4Text() {
		 StringBuilder json = new StringBuilder();
	        json.append("{\"touser\":\"").append(this.getToUserName()).append("\",\"msgtype\":\"text\",\"text\":{\"content\":\"").append(this.getContent()).append("\"}}");
	        return json.toString();
	}

	
	public String toSend4Image() {
		 StringBuilder json = new StringBuilder();
	        json.append("{\"touser\":\"").append(this.getToUserName()).append("\",\"msgtype\":\"image\",\"image\":{\"media_id\":\"").append(this.getMediaId()).append("\"}}");
	        return json.toString();
	}
	
	
	public String toSend4Voice() {
		 StringBuilder json = new StringBuilder();
	        json.append("{\"touser\":\"").append(this.getToUserName()).append("\",\"msgtype\":\"voice\",\"voice\":{\"media_id\":\"").append(this.getMediaId()).append("\"}}");
	        return json.toString();
	}
	
	public String toSend4Video() {
		 StringBuilder json = new StringBuilder();
	        json.append("{\"touser\":\"").append(this.getToUserName()).append("\",\"msgtype\":\"video\",\"video\":{\"media_id\":\"").append(this.getMediaId()).append("\",\"thumb_media_id\":\"").append(this.getThumbMediaId()).append("\",\"title\":\"").append(this.getTitle()).append("\",\"description\":\"").append(this.getDescription()).append("\"}}");
	        return json.toString();
	}
	
	public String toSend4Music() {
		 StringBuilder json = new StringBuilder();
	        json.append("{\"touser\":\"").append(this.getToUserName())
	        .append("\",\"msgtype\":\"music\",\"music\":{\"thumb_media_id\":\"")
	        .append(this.getThumbMediaId())
	        .append("\",\"musicurl\":\"").append(this.getMusicUrl())
	        .append("\",\"hqmusicurl\":\"").append(this.getHQMusicUrl())
	        .append("\",\"title\":\"").append(this.getTitle())
	        .append("\",\"description\":\"").append(this.getDescription()).append("\"}}");
	        return json.toString();
	}
	
	public String toSend4News() {
		 StringBuilder json = new StringBuilder();
	        json.append("{\"touser\":\"").append(this.getToUserName())
	        .append("\",\"msgtype\":\"news\",\"news\":{\"article\":[");
	        for(int i =0;i < Articles.size();i++) {
	        	Message msg = Articles.get(i);
	        	json.append("{\"title\":\""+msg.getTitle()
	        			+"\",\"description\":\""+msg.getDescription()
	        			+"\",\"url\":\""+msg.getUrl()
	        			+"\",\"picurl\":\""+msg.getPicUrl()+"\"}");
	        	if(i < Articles.size()-1){
	        		json.append(",");
	        	}
	        }
	        json.append("]}}");
	        return json.toString();
	}

	
	public String toString() {
        List<Message> articles = this.getArticles();
        String xml = "<xml>";
        Field[] fields = this.getClass().getDeclaredFields();
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
        	for(Message msg : articles) {
				xml += "<item>";
				xml += "<Title><![CDATA["+msg.getTitle()+"]]></Title>";
				xml += "<Description><![CDATA["+msg.getDescription()+"]]></Description>";
				xml += "<PicUrl><![CDATA["+msg.getPicUrl()+"]]></PicUrl>";
				xml += "<Url><![CDATA["+msg.getUrl()+"]]></Url>";
				xml += "</item>";
        	}
        	xml +="</Articles>";
        }
        xml += "</xml>";
        return xml;
    }
}
