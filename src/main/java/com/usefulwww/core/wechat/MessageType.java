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

/**
 * 
 * @author lyun@nashihou.cn
 */
public enum MessageType {
	/**
	 * 文本消息
	 */
	Text("text"),
	/**
	 * 图片消息
	 */
	Image("image"),
	/**
	 * 音乐消息
	 */
	Music("music"), 
	Video("video"), 
	Voice("voice"), 
	Location("location"), 
	Link("link"),
	Event("event"),
	/**
	 * 菜单点击弹出网页事件
	 */
	EventClick("CLICK"),
	/**
	 * 菜单点击返回消息事件
	 */
	EventView("VIEW"),
	/**
	 * 关注事件
	 */
	EventSubscribe("subscribe"),
	/**
	 * 取消关注事件
	 */
	EventUnSubscribe("unsubscribe");
	
	private String msgType = "";

	MessageType(String msgType) {
		this.msgType = msgType;
	}

	/**
	 * @return the msgType
	 */
	@Override
	public String toString() {
		return msgType;
	}
}
