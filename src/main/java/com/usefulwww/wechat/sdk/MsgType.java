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

/**
 * 
 * @author lyun@nashihou.cn
 */
public enum MsgType {
	Text("text"), Image("image"), Music("music"), Video("video"), Voice("voice"), Location("location"), Link("link"),Event("event");
	
	private String msgType = "";

	MsgType(String msgType) {
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
