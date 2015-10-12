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
		
		//获取一个media_id
		//getMediaId(token);
		
		//post自定义菜单到微信服务器
/*		try {
			postMenu(token);
		} catch (ParseException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}*/
		
		getMenu(token);//中文乱码
		//deleteMenu(token);
		
	}
	
	/**
	 * 提交自定义的菜单到微信服务器，可以直接覆盖服务器上已存储的菜单
	 * @param token
	 * @throws ParseException
	 * @throws IOException
	 */
	public static void postMenu(AccessToken token) throws ParseException, IOException {
		String menu = JSONObject.fromObject(WeixinUtil.initMenu()).toString();
		
		System.out.println(menu);
		
		int result = WeixinUtil.createMenu(token.getToken(), menu);
		if(result == 0) {
			System.out.println("创建菜单成功");
		} else {
			System.out.println("错误码：" + result);
		}
	}
	
	/**
	 * 获取服务器上已存储的自定义菜单
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
	 * 删除服务器上已经存储的自定义菜单
	 * @param token
	 * @throws ClientProtocolException
	 * @throws IOException
	 */
	public static void deleteMenu(AccessToken token) throws ClientProtocolException, IOException {
		int errcode = WeixinUtil.deleteMenu(token.getToken());
		if(errcode == 0) {
			System.out.println("已删除服务器上的自定义菜单");
		} else {
			System.out.println("删除自定义菜单失败，错误码：" +  errcode);
		}
	}
	
	/**
	 * 上传一张图片，获取它的media_id
	 * @param token
	 */
	public static void getMediaId(AccessToken token) {
		//上传一个临时图片文件
		String path = "D:/running.jpg";
		try {
			String thumbMediaId = WeixinUtil.upload(path, token.getToken(), "thumb");
			System.out.println(thumbMediaId);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 获取新的AccessToken
	 * @return
	 * @throws IOException 
	 * @throws ClientProtocolException 
	 */
	public static AccessToken getNewToken() throws ClientProtocolException, IOException {
		return WeixinUtil.getAccessToken();
	}
}
