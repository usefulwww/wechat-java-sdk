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
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.Map;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
*
* @author lyun@nashihou.cn
*/
public class HttpClient {
	private static Logger logger = LoggerFactory.getLogger(HttpClient.class);
	
	public enum METHOD {POST,GET}
	
	private HttpURLConnection urlConnection = null;

	private String _content;

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
			logger.debug("url:"+urlString);
			URL url = new URL(urlString);
			if(urlString.startsWith("https://")){
				HttpsURLConnection conn = (HttpsURLConnection)url.openConnection();
				
				SSLContext sc = SSLContext.getInstance("SSL");
		        sc.init(null, new TrustManager[] { new TrustAnyTrustManager() },new java.security.SecureRandom());
		        conn.setSSLSocketFactory(sc.getSocketFactory());
		        conn.setHostnameVerifier(new TrustAnyHostnameVerifier());
		        conn.setDoOutput(true);
		        
		        urlConnection = conn;
			} else {
				urlConnection = (HttpURLConnection)url.openConnection();
			}
			urlConnection.setRequestMethod(method_str);
			urlConnection.setDoOutput(true);
			urlConnection.setDoInput(true);
			urlConnection.setUseCaches(false);
			urlConnection.setInstanceFollowRedirects(true);
			
			urlConnection.setRequestProperty("Accept", "*/*");
			urlConnection.setRequestProperty("Content-Type", "*/*");

			urlConnection.setRequestProperty("connection", "Keep-Alive");
			
			if (propertys != null){
				for (String key : propertys.keySet()) {
					urlConnection.addRequestProperty(key, propertys.get(key));
				}
			}

			OutputStream out = urlConnection.getOutputStream();
			
			if(null != param){
				out.write(param.toString().getBytes("UTF-8"));
			}
			if(null != data){
				out.write(data);
			}
			
			out.flush();
			out.close();
			
			_content = getContent();
			
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
		if(_content != null){
			return _content;
		}
		try {
			InputStream in = urlConnection.getInputStream();
			if(in.available()<=0){
				logger.debug("没有返回值");
				return null;
			}
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
			if (ecod == null){
				ecod = "UTF-8";
			}
			_content = new String(temp.toString().getBytes(), ecod);
			return _content;
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
	
	private class TrustAnyTrustManager implements X509TrustManager {

		@Override
		public void checkClientTrusted(X509Certificate[] chain, String authType)
				throws CertificateException {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void checkServerTrusted(X509Certificate[] chain, String authType)
				throws CertificateException {
			// TODO Auto-generated method stub
			
		}

		@Override
		public X509Certificate[] getAcceptedIssuers() {
			// TODO Auto-generated method stub
			return new X509Certificate[] {};
		}
		
	}

	private class TrustAnyHostnameVerifier implements HostnameVerifier {
		@Override
		public boolean verify(String hostname, SSLSession session) {
			return true;
		}
	}

}
