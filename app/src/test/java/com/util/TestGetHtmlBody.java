package com.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URLEncoder;
import java.util.Date;

import org.apache.commons.lang.StringUtils;
import org.apache.http.HttpEntity;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;


public class TestGetHtmlBody {
	private static String accept = "*/*";
	private static String userAgent = "Mozilla/4.0 (compatible; MSIE 8.0; Windows NT 5.1; Trident/4.0; .NET CLR 2.0.50727; .NET CLR 3.0.04506.648; .NET CLR 3.5.21022; .NET4.0C; .NET4.0E)";
	private static int timeout = 5000;
	private static String contentType = "text/html; charset=UTF-8";
	private static String encoding = "UTF-8";
	public static String url_list = "http://s.m.taobao.com/search?event_submit_do_new_search_auction=1&_input_charset=utf-8&searchfrom=1&action=home%3Aredirect_app_action&from=1&q={0}&sst=1&n=20&buying=buyitnow&m=api4h5&wlsort=10&page=1";

	public static String url_detail="http://acs.m.taobao.com/h5/mtop.taobao.detail.getdetail/6.0/?data=%7B%22itemNumId%22%3A%22571902010276%22%7D&qq-pf-to=pcqq.group";
	/**
	 * @param args
	 */
	public static void main(String[] args) throws Exception{
		// TODO Auto-generated method stub
		//System.out.println(getList("ƤЬ"));
		System.out.println(getDetail("569787063470"));

	}
	
	
	public static String getDetail(String numiid)
			throws ClientProtocolException, IOException {
//		System.out.println("Begin Time:"+new Date());
		StringBuffer sb = new StringBuffer("");
		CloseableHttpClient client = HttpClients.createDefault();
//		String result = "";
		//keyname = URLEncoder.encode(keyname, "utf-8");
		String tmpUrl=StringUtils.replace(url_detail, "{0}", numiid);
		HttpGet request = new HttpGet(tmpUrl);
		CloseableHttpResponse response = null;
		response = client.execute(request);
		String statusLine=response.getStatusLine().toString();
		int statusCode = response.getStatusLine().getStatusCode();
		if(statusCode==200){
			HttpEntity entity = response.getEntity();
			BufferedReader br = new BufferedReader(new InputStreamReader(entity
					.getContent(), "utf-8"));
			String line = "";
			while ((line = br.readLine()) != null) {
				sb.append(line);
			}
			br.close();
	
			client.close();
	
//			if (!sb.equals("")) {
//				result = sb.toString().replaceAll("jsonp\\d*\\(", "");
//				result = result.replaceAll("\\)", "");
//			}
//			System.out.println("End Time:"+new Date());
			return sb.toString();
			
		}else{
			throw new ClientProtocolException(statusLine);
		}
		
	}
	

	public static String getList(String keyname)
			throws ClientProtocolException, IOException {
//		System.out.println("Begin Time:"+new Date());
		StringBuffer sb = new StringBuffer("");
		CloseableHttpClient client = HttpClients.createDefault();
//		String result = "";
		keyname = URLEncoder.encode(keyname, "utf-8");
		String tmpUrl=StringUtils.replace(url_list, "{0}", keyname);
		HttpGet request = new HttpGet(tmpUrl);
		CloseableHttpResponse response = null;
		response = client.execute(request);
		String statusLine=response.getStatusLine().toString();
		int statusCode = response.getStatusLine().getStatusCode();
		if(statusCode==200){
			HttpEntity entity = response.getEntity();
			BufferedReader br = new BufferedReader(new InputStreamReader(entity
					.getContent(), "utf-8"));
			String line = "";
			while ((line = br.readLine()) != null) {
				sb.append(line);
			}
			br.close();
	
			client.close();
	
//			if (!sb.equals("")) {
//				result = sb.toString().replaceAll("jsonp\\d*\\(", "");
//				result = result.replaceAll("\\)", "");
//			}
//			System.out.println("End Time:"+new Date());
			return sb.toString();
			
		}else{
			throw new ClientProtocolException(statusLine);
		}
		
	}

}
