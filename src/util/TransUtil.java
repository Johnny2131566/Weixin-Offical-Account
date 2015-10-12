package util;

import java.io.IOException;
import java.net.URLEncoder;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.ParseException;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;

import net.sf.json.JSONObject;

public class TransUtil {

	public static final String API_KEY = "irNGQk9NSsHuLo7UrZ8QPwnG";
	public static final String SECRET_KEY = "Ti7p5y9lATHg9jtCeBmZn6YHmmjSb36P";
	public static final String GET_TANS_RESULT = "http://openapi.baidu.com/public/2.0/bmt/translate?client_id=YourApiKey&q=CONTEXT&from=SOURCE&to=DESTINATION";
	
	/**
	 * Get请求
	 * @param url
	 * @return
	 * @throws IOException 
	 * @throws ClientProtocolException 
	 */
	public static JSONObject doGetStr(String url) throws ClientProtocolException, IOException {
		//DefaultHttpClient httpClient = new DefaultHttpClient();//deprecated, 用HttpClientBuilder代替
		HttpClient httpClient = HttpClientBuilder.create().build();
		HttpGet httpGet = new HttpGet(url);
		JSONObject jsonObject = null;
	
		HttpResponse response = httpClient.execute(httpGet);
		
		HttpEntity entity = response.getEntity();
		if(entity != null) {
			String result = EntityUtils.toString(entity);
			jsonObject = JSONObject.fromObject(result);
		}
		
		return jsonObject;
	}

	/**
	 * Post请求
	 * @param url
	 * @param outStr
	 * @return
	 */
	public static JSONObject doPostStr(String url, String outStr) {
		//DefaultHttpClient httpClient = new DefaultHttpClient();//deprecated, 用HttpClientBuilder代替
		HttpClient httpClient = HttpClientBuilder.create().build();
		HttpPost httpPost = new HttpPost(url);
		JSONObject jsonObject = null;
		
		try {
			httpPost.setEntity(new StringEntity(outStr, "UTF-8"));
			HttpResponse response = httpClient.execute(httpPost);
			String result = EntityUtils.toString(response.getEntity(), "UTF-8");
			jsonObject = JSONObject.fromObject(result);
		} catch (ParseException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return jsonObject;
	}
	
	/**
	 * 对外接口
	 * 将原始翻译字符串处理后，提交给doTranslate()方法
	 * @param originalStr
	 * @return
	 * @throws ClientProtocolException
	 * @throws IOException
	 */
	public static String doTransJob(String originalStr) throws ClientProtocolException, IOException {
		String result = "";

		String temp = originalStr.substring(2).trim();
		if(temp.equals("")) {
			return "什么都没有，翻译你妹啊！";
		}
		String context = URLEncoder.encode(temp, "utf-8");
		JSONObject jsonObject = doTranslate(context, "auto", "en");
		JSONObject transResult = jsonObject.getJSONArray("trans_result").getJSONObject(0);
		if(jsonObject.containsKey("error_code")) {
			result = "翻译出现错误";
		} else {
			result = transResult.getString("dst");
		}
		return result;
	}
	
	/**
	 * 调用百度翻译的入口，获取翻译结果
	 * @param source
	 * @param from
	 * @param to
	 * @return
	 * @throws ClientProtocolException
	 * @throws IOException
	 */
	public static JSONObject doTranslate(String context, String from, String to) throws ClientProtocolException, IOException {
		String url = GET_TANS_RESULT.replace("SOURCE", from).replace("DESTINATION", to).replace("CONTEXT", context).replace("YourApiKey", API_KEY);
		return doGetStr(url);
	}
	
}
