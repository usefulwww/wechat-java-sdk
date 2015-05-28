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

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.Map;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLSession;

/**
*
* @author lyun@nashihou.cn
*/
public class HttpClient {
	
	public enum METHOD {POST,GET}
	
	private HttpURLConnection urlConnection = null;

	
	HostnameVerifier hv = new HostnameVerifier() {
		@Override
		public boolean verify(String urlHostName, SSLSession session) {
			System.out.println("Warning: URL Host: " + urlHostName + " vs. "
					+ session.getPeerHost());
			return true;
		}
	};
	    
	public boolean send(String urlString, METHOD method){
		return this.send(urlString,method,null);
	}
	/**
	 * 发送HTTP请求
	 * 
	 */
	public boolean send(String urlString, METHOD method, String json) {
		byte[] data = null;
		if(null!=json && "".equals(json)==false){
			try {
				data = json.getBytes("UTF-8");
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}
		}
		return this.send(urlString, method, null, null, data);
	}
		
	/**
	 * 发送HTTP请求
	 * 
	 */
	public boolean send(String urlString, METHOD method,
			Map<String, String> parameters, Map<String, String> propertys) {
		return this.send(urlString, method, parameters, propertys,null);
	}
	
	/**
	 * 发送HTTP请求
	 * 
	 */
	public boolean send(String urlString, METHOD method,
			Map<String, String> parameters, Map<String, String> propertys,byte[] data) {
		StringBuffer param = new StringBuffer();
		String method_str = "GET";
		if(null != parameters){
			switch (method) {
			case GET:
				if(urlString.indexOf('?')>0){
					urlString += param.toString();
				} else {
					urlString += "?" + param.toString().substring(1);
				}
				break;
			case POST:
				method_str="POST";
				for (String key : parameters.keySet()) {
					param.append("&");
					param.append(key).append("=").append(parameters.get(key));
				}
				break;
			default:
				break;
			}

		}
		
		
		try {
			URL url = new URL(urlString);
			urlConnection = (HttpURLConnection) url.openConnection();
			urlConnection.setRequestMethod(method_str);
			urlConnection.setDoOutput(true);
			urlConnection.setDoInput(true);
			urlConnection.setUseCaches(false);
			urlConnection.setInstanceFollowRedirects(true);
			urlConnection.setRequestProperty("Accept", "application/json"); // 设置接收数据的格式
			urlConnection.setRequestProperty("Content-Type", "application/json"); // 设置发送数据的格式
			if (propertys != null){
				for (String key : propertys.keySet()) {
					urlConnection.addRequestProperty(key, propertys.get(key));
				}
			}
			
			if (method==METHOD.POST) {
				if(null != param){
					urlConnection.getOutputStream().write(param.toString().getBytes("UTF-8"));
				}
				if(null != data){
					urlConnection.getOutputStream().write(data);
				}
				urlConnection.getOutputStream().flush();
				urlConnection.getOutputStream().close();
			}
			
			return true;
					
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ProtocolException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return false;
	}

	public String getContent() {
		try {
			InputStream in = urlConnection.getInputStream();
			BufferedReader bufferedReader = new BufferedReader(
					new InputStreamReader(in));
			StringBuffer temp = new StringBuffer();
			String line = bufferedReader.readLine();
			String lineSeparator = System.getProperty("line.separator", "\n"); 
			while (line != null) {
				temp.append(line).append(lineSeparator);
				line = bufferedReader.readLine();
			}
			bufferedReader.close();

			String ecod = urlConnection.getContentEncoding();
			if (ecod == null)
				ecod = "UTF-8";
			String content = new String(temp.toString().getBytes(), ecod);
			return content;
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return null;

	}

	public boolean close() {
		if (urlConnection != null)
			urlConnection.disconnect();

		return true;
	}

}
