package com.marks.common.util.http;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpConnectionManager;
import org.apache.commons.httpclient.HttpMethod;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.MultiThreadedHttpConnectionManager;
import org.apache.commons.httpclient.NameValuePair;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.methods.RequestEntity;
import org.apache.commons.httpclient.methods.StringRequestEntity;
import org.apache.commons.httpclient.util.IdleConnectionTimeoutThread;
import org.apache.log4j.Logger;
import org.json.JSONObject;

import com.marks.common.domain.JsonResult;
import com.marks.common.util.center.SysCode;

/**
 * Created with IntelliJ IDEA. User: sunshine Date: 13-7-24 Time: 上午11:11 To
 * change this template use File | Settings | File Templates.
 */
public class HttpUtils2 {
	private static Logger logger = Logger.getLogger(HttpUtils2.class);
	private static Integer statusCode = 0;
	private static int connectiontimeout = 5000;// 链接超时
	private static int sotimeout = 15000;// 读取超时
	private static final char[] hexDigit = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A',

			'B', 'C', 'D', 'E', 'F' };

	/** 闲置连接超时时间, 由bean factory设置，缺省为60秒钟 */
	private int defaultIdleConnTimeout = 60000;

	private int defaultMaxConnPerHost = 30;

	private int defaultMaxTotalConn = 80;

	/** 默认等待HttpConnectionManager返回连接超时（只有在达到最大连接数时起作用）：1秒 */
	private static final long defaultHttpConnectionManagerTimeout = 3 * 1000;

	private static HttpUtils2 httpProtocolHandler = new HttpUtils2();
	/**
	 * HTTP连接管理器，该连接管理器必须是线程安全的.
	 */
	private HttpConnectionManager connectionManager;

	/**
	 * 工厂方法
	 * 
	 * @return
	 */

	public static HttpUtils2 getInstance() {
		return httpProtocolHandler;
	}

	/**
	 * 私有的构造方法
	 */
	private HttpUtils2() {
		// 创建一个线程安全的HTTP连接池
		connectionManager = new MultiThreadedHttpConnectionManager();
		connectionManager.getParams().setDefaultMaxConnectionsPerHost(defaultMaxConnPerHost);
		connectionManager.getParams().setMaxTotalConnections(defaultMaxTotalConn);

		IdleConnectionTimeoutThread ict = new IdleConnectionTimeoutThread();
		ict.addConnectionManager(connectionManager);
		ict.setConnectionTimeout(defaultIdleConnTimeout);

		ict.start();
	}
	public JsonResult doGet(String url, Map<String, String> params, Map<String, String> header, String charSet)
			throws IOException {
		JsonResult jsonObject = null;
		HttpMethod method = null;
		if (params != null && params.size() > 0) {
			StringBuffer paramUrl = new StringBuffer("");
			Set<String> keySet = params.keySet();
			Iterator<String> it = keySet.iterator();
			while (it.hasNext()) {
				String key = it.next();
				String value = params.get(key);
				paramUrl.append("&" + key + "=" + value);
			}
			if (url.indexOf("?") == -1) {
				method = new GetMethod(url + "?" + paramUrl);
			} else {
				method = new GetMethod(url + paramUrl);
			}
			logger.info(method.getURI());
		} else {
			method = new GetMethod(url);
		}
		if (header != null && header.size() > 0) {
			Set<String> headerKeySet = header.keySet();
			Iterator<String> headerIt = headerKeySet.iterator();
			while (headerIt.hasNext()) {
				String headerKey = headerIt.next();
				String headerValue = params.get(headerKey);
				method.setRequestHeader(headerKey, headerValue);
			}

		}
		jsonObject = requestHttp(method, charSet);
		return jsonObject;
	}

	public JsonResult doPost(String url, Map<String, String> params, Map<String, String> header, String charSet)
			throws IOException {
		JsonResult jsonObject = null;
		HttpMethod method = new PostMethod(url);

		if (params != null && params.size() > 0) {
			NameValuePair[] data = null;
			data = new NameValuePair[params.size()];
			Set<String> keySet = params.keySet();
			Iterator<String> it = keySet.iterator();
			int i = 0;
			while (it.hasNext()) {
				String key = it.next();
				String value = params.get(key);
				data[i] = new NameValuePair(key, value);
				i++;
			}
			((PostMethod) method).addParameters(data);
			method.addRequestHeader("Content-Type", "application/x-www-form-urlencoded; text/html; charset=" + charSet);
		}

		if (header != null && header.size() > 0) {
			Set<String> headerKeySet = header.keySet();
			Iterator<String> headerIt = headerKeySet.iterator();
			while (headerIt.hasNext()) {
				String headerKey = headerIt.next();
				String headerValue = header.get(headerKey);
				method.setRequestHeader(headerKey, headerValue);
			}

		}
		jsonObject = requestHttp(method, charSet);

		return jsonObject;
	}

	public JsonResult doPostJson(String url, JSONObject jsonObj, Map<String, String> header, String charSet)
			throws IOException {
		JsonResult jsonObject = null;
		HttpMethod method = new PostMethod(url);

		if (jsonObj != null) {
			RequestEntity requestEntity = new StringRequestEntity(jsonObj.toString(), "text/json", "UTF-8");
			((PostMethod) method).setRequestEntity(requestEntity);
		}
		if (header != null && header.size() > 0) {
			Set<String> headerKeySet = header.keySet();
			Iterator<String> headerIt = headerKeySet.iterator();
			while (headerIt.hasNext()) {
				String headerKey = headerIt.next();
				String headerValue = header.get(headerKey);
				method.setRequestHeader(headerKey, headerValue);
			}

		}
		jsonObject = requestHttp(method, charSet);

		return jsonObject;
	}

	private JsonResult requestHttp(HttpMethod method, String charSet) {
		JsonResult jsonObject = new JsonResult();
		HttpClient httpClient = new HttpClient(connectionManager);
		// 链接超时
		httpClient.getHttpConnectionManager().getParams().setConnectionTimeout(connectiontimeout);
		httpClient.getHttpConnectionManager().getParams().setSoTimeout(sotimeout);
		// 设置等待ConnectionManager释放connection的时间
		httpClient.getParams().setConnectionManagerTimeout(defaultHttpConnectionManagerTimeout);
		/* method.releaseConnection(); */
		// 设置Http Header中的User-Agent属性
		method.addRequestHeader("User-Agent", "Mozilla/5.0 (Windows NT 10.0; WOW64; Trident/7.0; Touch; rv:11.0) like Gecko");
		try {
			statusCode = httpClient.executeMethod(method);
			jsonObject.setErrorCode(statusCode + "");
			if (statusCode != HttpStatus.SC_OK) {
				jsonObject.setSuccess(Boolean.FALSE);
				jsonObject.setErrorMsg("服务器繁忙，请稍等···");
			} else {
				jsonObject.setErrorCode(SysCode.SUCCESS);
				String searchResult = "";
				jsonObject.setSuccess(Boolean.TRUE);
				BufferedInputStream ins = new BufferedInputStream(method.getResponseBodyAsStream());
				byte resultBytes[] = readUrlStream(ins);
				if (resultBytes != null && resultBytes.length > 0) {
					searchResult = new String(resultBytes, charSet);
				}
				jsonObject.setResult(searchResult);
			}
			logger.info("腾讯返回报文>>" + jsonObject.getResult());
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			jsonObject.setSuccess(Boolean.FALSE);
			jsonObject.setErrorCode("4000");
			jsonObject.setErrorMsg("系统异常·");
		} finally {
			method.releaseConnection();
		}
		return jsonObject;

	}

	private static byte[] readUrlStream(BufferedInputStream bufferedInputStream) throws IOException {
		ByteArrayOutputStream swapStream = new ByteArrayOutputStream();
		int byteNum = 1024;
		byte[] buff = new byte[byteNum]; // buff用于存放循环读取的临时数据
		int rc = 0;
		while ((rc = bufferedInputStream.read(buff, 0, byteNum)) > 0) {
			swapStream.write(buff, 0, rc);
		}

		return swapStream.toByteArray();
	}

	public static String toEncodedUnicode(String theString, boolean escapeSpace) {

		int len = theString.length();

		int bufLen = len * 2;

		if (bufLen < 0) {

			bufLen = Integer.MAX_VALUE;

		}

		StringBuffer outBuffer = new StringBuffer(bufLen);

		for (int x = 0; x < len; x++) {

			char aChar = theString.charAt(x);

			// Handle common case first, selecting largest block that

			// avoids the specials below

			if ((aChar > 61) && (aChar < 127)) {

				if (aChar == '\\') {

					outBuffer.append('\\');

					outBuffer.append('\\');

					continue;

				}

				outBuffer.append(aChar);

				continue;

			}

			switch (aChar) {

			case ' ':

				if (x == 0 || escapeSpace)
					outBuffer.append('\\');

				outBuffer.append(' ');

				break;

			case '\t':

				outBuffer.append('\\');

				outBuffer.append('t');

				break;

			case '\n':

				outBuffer.append('\\');

				outBuffer.append('n');

				break;

			case '\r':

				outBuffer.append('\\');

				outBuffer.append('r');

				break;

			case '\f':

				outBuffer.append('\\');

				outBuffer.append('f');

				break;

			case '=': // Fall through

			case ':': // Fall through

			case '#': // Fall through

			case '!':

				outBuffer.append('\\');

				outBuffer.append(aChar);

				break;

			default:

				if ((aChar < 0x0020) || (aChar > 0x007e)) {

					// 每个unicode有16位，每四位对应的16进制从高位保存到低位

					outBuffer.append('\\');

					outBuffer.append('u');

					outBuffer.append(toHex((aChar >> 12) & 0xF));

					outBuffer.append(toHex((aChar >> 8) & 0xF));

					outBuffer.append(toHex((aChar >> 4) & 0xF));

					outBuffer.append(toHex(aChar & 0xF));

				} else {

					outBuffer.append(aChar);

				}

			}

		}

		return outBuffer.toString();

	}

	private static char toHex(int nibble) {
		return hexDigit[(nibble & 0xF)];
	}

	public JsonResult download(String url, String fileName) {
		JsonResult jsonObject = new JsonResult();
		GetMethod method = null;
		FileOutputStream output = null;
		File saveFile =null;
		try {

			method = new GetMethod(url);
			HttpClient httpClient = new HttpClient(connectionManager);
			// 链接超时
			httpClient.getHttpConnectionManager().getParams().setConnectionTimeout(connectiontimeout);
			httpClient.getHttpConnectionManager().getParams().setSoTimeout(sotimeout);
			// 设置等待ConnectionManager释放connection的时间
			httpClient.getParams().setConnectionManagerTimeout(defaultHttpConnectionManagerTimeout);
			/* method.releaseConnection(); */
			// 设置Http Header中的User-Agent属性
			method.addRequestHeader("User-Agent", "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/56.0.2924.87 Safari/537.36");

			statusCode = httpClient.executeMethod(method);
			jsonObject.setErrorCode(statusCode + "");
			if (statusCode != HttpStatus.SC_OK) {
				jsonObject.setSuccess(Boolean.FALSE);
				jsonObject.setErrorMsg("服务器繁忙，请稍等···");
			} else {
				jsonObject.setSuccess(Boolean.TRUE);
				jsonObject.setErrorCode(SysCode.SUCCESS);
				BufferedInputStream ins = new BufferedInputStream(method.getResponseBodyAsStream());
				saveFile = new File(fileName);
				output = new FileOutputStream(saveFile, true);
				/*int size = 0;
				byte[] buf = new byte[1024 * 1024 * 10];
				while ((size = ins.read(buf)) != -1) {
					output.write(buf, 0, size);
					output.flush();
				}*/
				byte resultBytes[] = readUrlStream(ins);
				output.write(resultBytes);
				output.flush();
			}
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			jsonObject.setSuccess(Boolean.FALSE);
			jsonObject.setErrorCode("4000");
			jsonObject.setErrorMsg("系统异常·");
		} finally {
			try {
				if (output != null) {
					output.close();
				}
			} catch (IOException e) {
				logger.error(e.getMessage(), e);
			}
			method.releaseConnection();
		}

		return jsonObject;

	}

}
