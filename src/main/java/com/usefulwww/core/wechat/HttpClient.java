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

/**
*
* @author lyun@nashihou.cn
*/
public class HttpClient {

	private HttpURLConnection urlConnection = null;

	/**
	 * 发送GET请求
	 * 
	 * @param urlString
	 *            URL地址
	 * 
	 */
	public boolean sendGet(String urlString) {
		return this.send(urlString, "GET", null, null);
	}

	/**
	 * 发送GET请求
	 * 
	 * @param urlString
	 *            URL地址
	 * @param params
	 *            参数集合
	 * 
	 * @throws IOException
	 */
	public boolean sendGet(String urlString, Map<String, String> params){
		return this.send(urlString, "GET", params, null);
	}

	/**
	 * 发送GET请求
	 * 
	 * @param urlString
	 *            URL地址
	 * @param params
	 *            参数集合
	 * @param propertys
	 *            请求属性
	 * 
	 * @throws IOException
	 */
	public boolean sendGet(String urlString, Map<String, String> params,
			Map<String, String> propertys) {
		return this.send(urlString, "GET", params, propertys);
	}

	/**
	 * 发送POST请求
	 * 
	 * @param urlString
	 *            URL地址
	 * 
	 * @throws IOException
	 */
	public boolean sendPost(String urlString) {
		return this.send(urlString, "POST", null, null);
	}

	/**
	 * 发送POST请求
	 * 
	 * @param urlString
	 *            URL地址
	 * @param params
	 *            参数集合
	 * 
	 * @throws IOException
	 */
	public boolean sendPost(String urlString, Map<String, String> params){
		return this.send(urlString, "POST", params, null);
	}

	/**
	 * 发送POST请求
	 * 
	 * @param urlString
	 *            URL地址
	 * @param params
	 *            参数集合
	 * @param propertys
	 *            请求属性
	 * 
	 * @throws IOException
	 */
	public boolean sendPost(String urlString, Map<String, String> params,
			Map<String, String> propertys) {
		return this.send(urlString, "POST", params, propertys);
	}

	/**
	 * 发送POST请求
	 * 
	 * @param urlString
	 *            URL地址
	 * @param data
	 *            POST内容
	 */
	public boolean sendPost(String urlString, String data) {
		try {
			URL url = new URL(urlString);
			urlConnection = (HttpURLConnection) url.openConnection();
			urlConnection.setRequestMethod("POST");
			urlConnection.setDoOutput(true);
			urlConnection.setDoInput(true);
			urlConnection.setUseCaches(false);
			if(data !=null && data!="") {
				urlConnection.getOutputStream().write(data.getBytes());
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
		}

		return false;
	}
	
	/**
	 * 发送HTTP请求
	 * 
	 */
	public boolean send(String urlString, String method,
			Map<String, String> parameters, Map<String, String> propertys) {
		StringBuffer param = new StringBuffer();
		for (String key : parameters.keySet()) {
			param.append("&");
			param.append(key).append("=").append(parameters.get(key));
		}
		
		if (method.equalsIgnoreCase("GET") && parameters != null) {
			if(urlString.indexOf('?')>0){
				urlString += param.toString();
			} else {
				urlString += "?" + param.toString().substring(1);
			}
		}
		
		try {
			URL url = new URL(urlString);
			urlConnection = (HttpURLConnection) url.openConnection();
			urlConnection.setRequestMethod(method);
			urlConnection.setDoOutput(true);
			urlConnection.setDoInput(true);
			urlConnection.setUseCaches(false);

			if (propertys != null){
				for (String key : propertys.keySet()) {
					urlConnection.addRequestProperty(key, propertys.get(key));
				}
			}
			
			if (method.equalsIgnoreCase("POST") && parameters != null) {
				urlConnection.getOutputStream().write(param.toString().getBytes());
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
