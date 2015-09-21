package com.usefulwww.core.wechat;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.util.*;

import javax.xml.parsers.ParserConfigurationException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xml.sax.SAXException;

/**
 * 抽象类，需要继承后实现相应函数
 *
 */
public abstract class WechatAction {
	
	protected  Wechat wx = new Wechat();

	Logger logger = LoggerFactory.getLogger(WechatAction.class);
	
	public WechatAction(){
		super();
	}
	
	
	protected Map<String, String> jsConfig(String url){
		Setting set = this.getSetting();
		
		String  timestamp = Long.toString(System.currentTimeMillis() / 1000);
				
		Map<String,String> map = new HashMap<String, String>();
		
		map.put("noncestr",WechatUtil.getRandomStringByLength(32));
		map.put("jsapi_ticket",wx.getJsTicket(set.getAppId(), set.getAppSecret()));
		map.put("timestamp", timestamp);
		map.put("url", url);
		
		map.put("signature", wx.getJsSign(map));
		
		map.put("appid", set.getAppId());
		
		return map;
	}
	
	protected abstract Setting getSetting();
	
	protected abstract String getPayNotifyUrl();
	
	protected abstract String getPayOrderNo(String orderId);
	
	protected abstract String getPayOrderSubject(String orderId);
	
	protected abstract long getPayOrderFee(String orderId);
	

	protected abstract String getRequestRemoteHost();
	

	public String payPre4H5(String orderId){

		long fee = this.getPayOrderFee(orderId);
		
		Wechat wechat = new Wechat();
		
		Map<String,String> params =new HashMap<String, String>();
		Setting set = this.getSetting();
		params.put("appid", set.getAppId());
		params.put("mch_id", set.getMchId());
		params.put("nonce_str", WechatUtil.getRandomStringByLength(32));
		params.put("body", getPayOrderSubject(orderId));
		params.put("out_trade_no", getPayOrderNo(orderId));
		params.put("total_fee", String.valueOf(fee));
		params.put("spbill_create_ip", this.getRequestRemoteHost());
		params.put("notify_url", this.getPayNotifyUrl());
		params.put("trade_type", "WAP");
		
		String sign = wechat.getPaySign(params, set.getMchKey());
		
		params.put("sign", sign);
		
		Map<String, String> prepay = wechat.unifiedorder(params);
		
		String  timestamp = Long.toString(System.currentTimeMillis() / 1000);
		
		StringBuilder json = new StringBuilder();
		json.append("{\"status\":200,\"message\":\"success!\",\"option\":{\"appId\":\"")
		.append(params.get("appid"))
		.append("\",\"timestamp\":\"").append(timestamp)
		.append("\",\"nonceStr\":\"").append(prepay.get("nonce_str"))
		.append("\",\"package\":\"prepay_id=").append(prepay.get("prepay_id"))
		.append("\",\"signType\":\"").append("MD5")
		.append("\",\"paySign\":\"").append(prepay.get("sign"))
		.append("\"}}");
		
		return json.toString();
	}
	
	public String payPre4Js(String orderId,String openId){

		long fee = this.getPayOrderFee(orderId);
		
		Wechat wechat = new Wechat();
		
		Map<String,String> params =new HashMap<String, String>();
		Setting set = this.getSetting();
		params.put("openid", openId);
		params.put("appid", set.getAppId());
		params.put("mch_id", set.getMchId());
		params.put("nonce_str", WechatUtil.getRandomStringByLength(32));
		params.put("body", getPayOrderSubject(orderId));
		params.put("out_trade_no", getPayOrderNo(orderId));
		params.put("total_fee", String.valueOf(fee));
		params.put("spbill_create_ip", this.getRequestRemoteHost());
		params.put("notify_url", this.getPayNotifyUrl());
		params.put("trade_type", "JSAPI");
		
		String sign = wechat.getPaySign(params, set.getMchKey());
		
		params.put("sign", sign);
		
		Map<String, String> prepay = wechat.unifiedorder(params);
		
		String  timestamp = Long.toString(System.currentTimeMillis() / 1000);
		
		StringBuilder json = new StringBuilder();
		json.append("{\"status\":200,\"message\":\"success!\",\"option\":{\"appId\":\"")
		.append(params.get("appid"))
		.append("\",\"timestamp\":\"").append(timestamp)
		.append("\",\"nonceStr\":\"").append(prepay.get("nonce_str"))
		.append("\",\"package\":\"prepay_id=").append(prepay.get("prepay_id"))
		.append("\",\"signType\":\"").append("MD5")
		.append("\",\"paySign\":\"").append(prepay.get("sign"))
		.append("\"}}");
		
		return json.toString();
	}
	

	public abstract String  payNotify(InputStream requestInputStream);
	
	/*
	public String  payNotify(InputStream requestInputStream)
	{
		try {
			
			BufferedReader br = new BufferedReader(new InputStreamReader(requestInputStream));
			String line = br.readLine();
			StringBuffer xmlString = new StringBuffer();
			while(line !=null){
				xmlString.append(line);
			}
			br.close();
			
			logger.debug("订单支付结果:"+xmlString.toString());
			
			Map<String, String> result= WeixinUtil.getMapFromXML(xmlString.toString() );

			String return_code = result.get("return_code");
			String result_code = result.get("result_code");
			if("SUCCESS".equals(return_code)){
				String orderNo = result.get("out_trade_no"); 
				String payId = result.get("transaction_id");
				String openId = result.get("openid");
				//Date payTime = Util.parseDate(result.get("time_end"),"YYYYMMddHHmmss") ;
				
				long fee = 0;
				try {
					fee = Long.parseLong(result.get("total_fee"));
				} catch (NumberFormatException e) {
				}
				
				Pay pay = new Pay();
				
				pay.setId(Util.randomUUID());
				pay.setSiteId(this.getSiteId());
				
				pay.setOpenId(openId);
				pay.setOrderNo(orderNo);
				pay.setPayId(payId);
				//pay.setPayTime(payTime);
				pay.setFee(fee);
				
				
				if("SUCCESS".equals(result_code)){
					pay.setStatus("SUCCESS");//支付成功
					paySuccess(pay);
				} else {
					pay.setStatus("FAIL");//支付失败
					payFail(pay);
				}
				
				//保存到数据库
				Dao.save(pay);
			
		}
		
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ParserConfigurationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SAXException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return "<xml><return_code><![CDATA[SUCCESS]]></return_code><return_msg><![CDATA[OK]]></return_msg></xml>";
		
	}
	
	*/
}
