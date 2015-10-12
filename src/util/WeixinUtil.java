package util;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import javax.servlet.http.HttpServletResponse;

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

import entity.AccessToken;
import menu.Button;
import menu.ClickButton;
import menu.Menu;
import menu.SubButton;
import menu.ViewButton;
import net.sf.json.JSONObject;

public class WeixinUtil {

	private static final String APPID = "***************";
	private static final String APPSECRET = "***************************";
	
	private static final String ACCESS_TOKEN_URL = "https://api.weixin.qq.com/cgi-bin/token?grant_type=client_credential&appid=APPID&secret=APPSECRET";
	private static final String UPLOAD_URL = "https://api.weixin.qq.com/cgi-bin/media/upload?access_token=ACCESS_TOKEN&type=TYPE";
	private static final String CREATE_MENU_URL = "https://api.weixin.qq.com/cgi-bin/menu/create?access_token=ACCESS_TOKEN";
	private static final String QUERY_MENU_URL = "https://api.weixin.qq.com/cgi-bin/menu/get?access_token=ACCESS_TOKEN";
	private static final String DELETE_MENU_URL = "https://api.weixin.qq.com/cgi-bin/menu/delete?access_token=ACCESS_TOKEN";
	
	/**
	 * Get����
	 * @param url
	 * @return
	 * @throws IOException 
	 * @throws ClientProtocolException 
	 */
	public static JSONObject doGetStr(String url) throws ClientProtocolException, IOException {
		//DefaultHttpClient httpClient = new DefaultHttpClient();//deprecated, ��HttpClientBuilder����
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
	 * Post����
	 * @param url
	 * @param outStr
	 * @return
	 */
	public static JSONObject doPostStr(String url, String outStr) {
		//DefaultHttpClient httpClient = new DefaultHttpClient();//deprecated, ��HttpClientBuilder����
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
	 * access_token�Ļ�ȡ
	 * @return
	 * @throws IOException 
	 * @throws ClientProtocolException 
	 */
	public static AccessToken getAccessToken() throws ClientProtocolException, IOException {
		AccessToken token = new AccessToken();
		String url = ACCESS_TOKEN_URL.replace("APPID", APPID).replace("APPSECRET", APPSECRET);
		JSONObject jsonObject = doGetStr(url);
		if(jsonObject != null) {
			token.setToken(jsonObject.getString("access_token"));
			token.setExpires_in(jsonObject.getInt("expires_in"));
		}
		
		return token;
	}
	
	/**
	 * �ӷ������ϻ�ȡ�Ѿ�������Ĳ˵�
	 * @param token
	 * @return
	 * @throws IOException 
	 * @throws ClientProtocolException 
	 */
	public static JSONObject queryMenu(String token) throws ClientProtocolException, IOException {
		String url = QUERY_MENU_URL.replace("ACCESS_TOKEN", token);
		return  doGetStr(url);
	}
	
	public static int deleteMenu(String token) throws ClientProtocolException, IOException {
		String url = DELETE_MENU_URL.replace("ACCESS_TOKEN", token);
		return doGetStr(url).getInt("errcode");
	}
	
	/**
	 * Post�˵���΢�ŷ�����
	 * @param token
	 * @param menu
	 * @return
	 */
	public static int createMenu(String token, String menu) throws ParseException, IOException {
		int result = 0;
		String url = CREATE_MENU_URL.replace("ACCESS_TOKEN", token);
		JSONObject jsonObject = doPostStr(url, menu);
		if(jsonObject != null) {
			result = jsonObject.getInt("errcode");
		}
		return result;
	}
	
	/**
	 * ��װ�Զ���˵�
	 * @return
	 */
	public static Menu initMenu() {
		Menu menu = new Menu();
		
		ClickButton button1 = new ClickButton(); 
		button1.setType("click");
		button1.setName("���˵�");
		button1.setKey("11");
		
		ViewButton button2 = new ViewButton();
		button2.setType("view");
		button2.setName("��ҳ");
		button2.setUrl("http://www.imooc.com");
		
		ClickButton button31 = new ClickButton();
		button31.setType("scancode_push");
		button31.setName("ɨһɨ");
		button31.setKey("31");

		ClickButton button32 = new ClickButton();
		button32.setType("location_select");
		button32.setName("��λ");
		button32.setKey("32");
		
		SubButton button3 = new SubButton();
		button3.setName("���ܲ˵�");
		button3.setSub_button(new Button[]{button31, button32});
		
		menu.setButton(new Button[]{button1, button2, button3});
		
		return menu;
	}

	/**
	 * �ϴ���ʱͼƬ
	 * @param filePath
	 * @param accessToken
	 * @param type
	 * @return
	 * @throws IOException
	 */
	public static String upload(String filePath, String accessToken, String type) throws IOException {
		File file = new File(filePath);
		if(!file.exists() || !file.isFile()) {
			throw new IOException("File does not exist!");
		}
		
		String url = UPLOAD_URL.replace("ACCESS_TOKEN", accessToken).replace("TYPE", type);
		
		URL urlObj = new URL(url);
		//����
		HttpURLConnection con = (HttpURLConnection)urlObj.openConnection();
		
		con.setRequestMethod("POST");
		con.setDoInput(true);
		con.setDoOutput(true);
		con.setUseCaches(false);
		
		//��������ͷ��Ϣ
		con.setRequestProperty("Connection", "Keep-Alive");
		con.setRequestProperty("Charset", "UTF-8");
		
		//���ñ߽�
		String BOUNDARY = "----------" + System.currentTimeMillis();
		con.setRequestProperty("Content-Type", "multipart/form-data; boundary=" + BOUNDARY);
		
		StringBuilder sb = new StringBuilder();
		sb.append("--");
		sb.append(BOUNDARY);
		sb.append("\r\n");
		sb.append("Content-Disposition:form-data;name=\"file\";filename=\"" + file.getName() + "\"\r\n");
		sb.append("Content-Type:application/octet-stream\r\n\r\n");
		
		byte[] head = sb.toString().getBytes("utf-8");
		
		System.out.println(sb);
		
		//��������
		OutputStream out = new DataOutputStream(con.getOutputStream());
		//�����ͷ
		out.write(head);
		
		//�ļ����Ĳ���
		//���ļ������ļ��ķ�ʽ����url��
		DataInputStream in = new DataInputStream(new FileInputStream(file));
		int bytes = 0;
		byte[] bufferOut = new byte[1024];
		while((bytes = in.read(bufferOut)) != -1) {
			out.write(bufferOut, 0, bytes);
		}
		in.close();
		
		//��β����
		byte[] foot = ("\r\n--" + BOUNDARY + "--\r\n").getBytes("utf-8");//����������ݷָ���
		
		out.write(foot);
		
		out.flush();
		out.close();
		
		StringBuffer buffer = new StringBuffer();
		BufferedReader reader = null;
		String result = null;
		
		try {
			//����BufferedReader����������ȡURL����Ӧ
			reader = new BufferedReader(new InputStreamReader(con.getInputStream()));
			String line = null;
			while((line = reader.readLine()) != null) {
				buffer.append(line);
			}
			if(result == null) {
				result = buffer.toString();
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if(reader != null) {
				reader.close();
			}
		}
		
		JSONObject jsonObject = JSONObject.fromObject(result);
		System.out.println(jsonObject);
		
		String typeName = "media_id";
		if(!"image".equals(type))
			typeName = type + "_media_id";
		String mediaId = jsonObject.getString(typeName);
			
		return mediaId;
	}
}
