package test;

import java.io.IOException;
import java.net.URLDecoder;

import org.apache.http.ParseException;
import org.apache.http.client.ClientProtocolException;

import entity.AccessToken;
import net.sf.json.JSONObject;
import util.WeixinUtil;

public class WeixinTest {
	
	public static void main(String[] args) throws ClientProtocolException, IOException {
		AccessToken token = getNewToken();
		
		
		System.out.println(token.getToken());
		System.out.println(token.getExpires_in());
		
		//��ȡһ��media_id
		//getMediaId(token);
		
		//post�Զ���˵���΢�ŷ�����
/*		try {
			postMenu(token);
		} catch (ParseException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}*/
		
		getMenu(token);//��������
		//deleteMenu(token);
		
	}
	
	/**
	 * �ύ�Զ���Ĳ˵���΢�ŷ�����������ֱ�Ӹ��Ƿ��������Ѵ洢�Ĳ˵�
	 * @param token
	 * @throws ParseException
	 * @throws IOException
	 */
	public static void postMenu(AccessToken token) throws ParseException, IOException {
		String menu = JSONObject.fromObject(WeixinUtil.initMenu()).toString();
		
		System.out.println(menu);
		
		int result = WeixinUtil.createMenu(token.getToken(), menu);
		if(result == 0) {
			System.out.println("�����˵��ɹ�");
		} else {
			System.out.println("�����룺" + result);
		}
	}
	
	/**
	 * ��ȡ���������Ѵ洢���Զ���˵�
	 * @param token
	 * @throws IOException 
	 * @throws ClientProtocolException 
	 */
	public static void getMenu(AccessToken token) throws ClientProtocolException, IOException {
		JSONObject jsonObject = WeixinUtil.queryMenu(token.getToken());
		String menu = URLDecoder.decode(jsonObject.toString(), "gbk");
		System.out.println(menu);
	}
	
	/**
	 * ɾ�����������Ѿ��洢���Զ���˵�
	 * @param token
	 * @throws ClientProtocolException
	 * @throws IOException
	 */
	public static void deleteMenu(AccessToken token) throws ClientProtocolException, IOException {
		int errcode = WeixinUtil.deleteMenu(token.getToken());
		if(errcode == 0) {
			System.out.println("��ɾ���������ϵ��Զ���˵�");
		} else {
			System.out.println("ɾ���Զ���˵�ʧ�ܣ������룺" +  errcode);
		}
	}
	
	/**
	 * �ϴ�һ��ͼƬ����ȡ����media_id
	 * @param token
	 */
	public static void getMediaId(AccessToken token) {
		//�ϴ�һ����ʱͼƬ�ļ�
		String path = "D:/running.jpg";
		try {
			String thumbMediaId = WeixinUtil.upload(path, token.getToken(), "thumb");
			System.out.println(thumbMediaId);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * ��ȡ�µ�AccessToken
	 * @return
	 * @throws IOException 
	 * @throws ClientProtocolException 
	 */
	public static AccessToken getNewToken() throws ClientProtocolException, IOException {
		return WeixinUtil.getAccessToken();
	}
}
