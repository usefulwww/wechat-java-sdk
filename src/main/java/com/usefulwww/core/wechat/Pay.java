package com.usefulwww.core.wechat;

import java.util.Date;


public class Pay {

	/**
	 * 编号
	 */
	private String id;
	
	/**
	 * 订单号
	 */
	private String orderId;
	
	/**
	 * 自定义订单号
	 */
	private String orderNo;
	
	/**
	 * openId
	 */
	private String openId;
		
	/**
	 * 预支付号
	 */
	private String prepayId;
	
	
	/**
	 * 支付号
	 */
	private String payId;
	
	/**
	 * 银行
	 */
	private String bank;
	
	/**
	 * 总金额(分)
	 */
	private long fee;
	
	/**
	 * 状态
	 */
	private String status;
	
	/**
	 * 支付时间
	 */
	private Date payTime;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getOrderId() {
		return orderId;
	}

	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}

	public String getOrderNo() {
		return orderNo;
	}

	public void setOrderNo(String orderNo) {
		this.orderNo = orderNo;
	}

	public String getPrepayId() {
		return prepayId;
	}

	public void setPrepayId(String prepayId) {
		this.prepayId = prepayId;
	}

	public String getPayId() {
		return payId;
	}

	public void setPayId(String payId) {
		this.payId = payId;
	}

	public String getBank() {
		return bank;
	}

	public void setBank(String bank) {
		this.bank = bank;
	}

	public long getFee() {
		return fee;
	}

	public void setFee(long fee) {
		this.fee = fee;
	}

	public Date getPayTime() {
		return payTime;
	}

	public void setPayTime(Date payTime) {
		this.payTime = payTime;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getOpenId() {
		return openId;
	}

	public void setOpenId(String openId) {
		this.openId = openId;
	}
	
}
