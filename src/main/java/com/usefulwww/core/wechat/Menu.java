package com.usefulwww.core.wechat;

import java.util.Date;

/**
 * 微信菜单实体类
 * @author Administrator
 *
 */
public class Menu {
	private int id;
	private String name;
	private String parentId;
	private String type;
	private String key;
	private String url;
	private int status;
	private String sub_button;
	private String parentName;
	private Date createTime;
	public Date getCreateTime() {
		return createTime;
	}
	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}
	public String getParentName() {
		return parentName;
	}
	public void setParentName(String parentName) {
		this.parentName = parentName;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getParentId() {
		return parentId;
	}
	public void setParentId(String parentId) {
		this.parentId = parentId;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getKey() {
		return key;
	}
	public void setKey(String key) {
		this.key = key;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public int getStatus() {
		return status;
	}
	public void setStatus(int status) {
		this.status = status;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getSub_button() {
		return sub_button;
	}
	public void setSub_button(String sub_button) {
		this.sub_button = sub_button;
	}
	@Override
	public String toString() {
		return "Menu [id=" + id + ", name=" + name + ", parentId=" + parentId
				+ ", type=" + type + ", key=" + key + ", url=" + url
				+ ", status=" + status + ", sub_button=" + sub_button + "]";
	}
	
}
