package test;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import org.apache.http.client.ClientProtocolException;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import util.TransUtil;

public class TransTest {

	public static void main(String[] args) {
		try {
			translate();
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
	}
	
	public static void translate() throws UnsupportedEncodingException {
		String q = URLEncoder.encode("ÎÒ°®Äã£¬×æ¹ú", "utf-8");
		String from = "zh";
		String to = "en";
		try {
			JSONArray jsonArray = TransUtil.doTranslate(q, from, to).getJSONArray("trans_result");
			JSONObject transResult = jsonArray.getJSONObject(0);
			System.out.println(transResult.getString("dst"));
		} catch (ClientProtocolException e1) {
			e1.printStackTrace();
		} catch (IOException e1) {
			e1.printStackTrace();
		}
			
	}
}
