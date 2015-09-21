package com.usefulwww.core.wechat;

import java.io.ByteArrayInputStream;
import java.util.Date;

import junit.framework.TestCase;

public class WechatTest extends TestCase{

	public void testSubScribe() {
		//模拟从微信传来的信息
		String send = "<xml>";
		send += "<ToUserName><![CDATA[toUser]]>user1</ToUserName>";
		send += "<FromUserName><![CDATA[FromUser]]>server1</FromUserName>";
		send += "<CreateTime>123456789</CreateTime>";
		send += "<MsgType><![CDATA[event]]></MsgType>";
		send += "<Event><![CDATA[subscribe]]></Event>";
		send += "</xml>";
		
		ByteArrayInputStream in = new ByteArrayInputStream(send.getBytes()); 

		final Wechat wechat = new Wechat();
		
		CallBack callback = new CallBack(){
			@Override
			public String reply(Message send) {
				String type = send.getMsgType();
				String event = send.getEvent();
				if(MessageType.event.toString().equals(type)){
					if(MessageType.subscribe.toString().equals(event)){
						Message replay = new Message();
						replay.setEvent(MessageType.text.toString());
						replay.setFromUserName(send.getToUserName());
						replay.setToUserName(send.getFromUserName());
						replay.setContent("感谢您关注");
						replay.setCreateTime(new Date().getTime());
						return WechatUtil.getReply4Text(replay);
					}
					
					return MessageType.event.toString();
				}
				
				return "error";
			}
		};
		
		String echostr = "123";
		
		//待回复的信息
		String replay =wechat.reply(echostr, in, callback);
		
		System.out.println(replay);
	}
	
	
	public void testCustomMsg(){
		final Wechat wechat = new Wechat();
		String appId="";
		String appSecret="";

		wechat.setOptions(appId, appSecret);
		
		String openId="";
		String content="测试客服消息";
		
		Message message = new Message();
		message.setToUserName(openId);
		message.setMsgType(MessageType.text.toString());
		message.setContent(content);
		
		boolean b = wechat.send(message);
		
		assertTrue(b);
	}
}
