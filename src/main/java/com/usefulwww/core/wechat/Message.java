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
    
    //Event 	事件类型，scancode_waitmsg 
    private String ScanCodeInfo;// 	扫描信息
    private String ScanType;// 	扫描类型，一般是qrcode
    private String ScanResult;// 	扫描结果，即二维码对应的字符串信息 
    
    //Event 	事件类型，pic_sysphoto 
    //Event 	事件类型，pic_photo_or_album
    //Event 	事件类型，pic_weixin 
    private String SendPicsInfo;// 	发送的图片信息
    private Long Count;// 	发送的图片数量
    private String PicList;// 	图片列表
    private String PicMd5Sum;// 	图片的MD5值，开发者若需要，可用于验证接收到图片 
    
    //Event 	事件类型，location_select 
    private String SendLocationInfo;// 	发送的位置信息
    private String Location_X;// 	X坐标信息
    private String Location_Y;// 	Y坐标信息
    private String Poiname;// 	朋友圈POI的名字，可能为空 
    
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
}
