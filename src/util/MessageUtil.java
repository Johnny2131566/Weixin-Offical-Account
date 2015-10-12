package util;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import com.thoughtworks.xstream.XStream;

import entity.Image;
import entity.Music;
import entity.News;
import message.ImageMessage;
import message.MusicMessage;
import message.NewsMessage;
import message.TextMessage;

public class MessageUtil {
	
	//消息类型
	public static final String MESSAGE_TEXT = "text";//文本
	public static final String MESSAGE_NEWS = "news";//文本
	public static final String MESSAGE_IMAGE = "image";//图片
	public static final String MESSAGE_VOICE = "voice";//语音
	public static final String MESSAGE_MUSIC = "music";//地理位置
	public static final String MESSAGE_VIDEO = "video";//视频
	public static final String MESSAGE_LINK = "link";//链接
	public static final String MESSAGE_LOCATION = "location";//地理位置
	//事件推送
	public static final String MESSAGE_EVENT = "event";//事件推送
	public static final String MESSAGE_SUBSCRIBE = "subscribe";//事件推送——>关注
	public static final String MESSAGE_UNSUBSCRIBE = "unsubscribe";//事件推送——>取消关注
	public static final String MESSAGE_CLICK = "CLICK";//事件推送——>菜单点击
	public static final String MESSAGE_VIEW = "VIEW";//事件推送——>跳转链接
	public static final String MESSAGE_SCANCODE = "scancode_push";//事件推送——>扫码事件
	
	//测试用的imageid
	//public static final String MEDIA_ID = "Jd0vhwz_5Rpac6h_rVJbaI-iEr1afbQD6z-CoxtmTZPJhCNN8sGOzlMex7xNzw_O";
	/**
	 * xml转换为map类型
	 * @param request
	 * @return
	 * @throws IOException
	 * @throws DocumentException
	 */
	public static Map<String, String> xmlToMap(HttpServletRequest request) throws IOException, DocumentException {
		Map<String, String> map = new HashMap<String, String>();
		SAXReader reader = new SAXReader();
		
		InputStream ins = request.getInputStream();
		Document doc = reader.read(ins);
		
		Element root = doc.getRootElement();
		List<Element> list = root.elements();
		
		for(Element e : list) {
			map.put(e.getName(), e.getText());
		}
		
		ins.close();
		return map;
	}
	
	/**
	 * 将文本消息对象转换为xml
	 * @param textMessage
	 * @return
	 */
	public static String textMessageToXml(TextMessage textMessage) {
		XStream xstream = new XStream();
		xstream.alias("xml", textMessage.getClass());//将xml的根节点设置为xml
		return xstream.toXML(textMessage);
	}
	
	/**
	 * 将图文消息对象转换为xml
	 * @param newsMessage
	 * @return
	 */
	public static String newsMessageToXml(NewsMessage newsMessage) {
		XStream xstream = new XStream();
		xstream.alias("xml", newsMessage.getClass());//将xml的根节点设置为xml
		xstream.alias("item", new News().getClass());
		return xstream.toXML(newsMessage);
	}
	
	/**
	 * 将图片消息对象转换为xml
	 * @param imageMessage
	 * @return
	 */
	public static String imageMessageToXml(ImageMessage imageMessage) {
		XStream xstream = new XStream();
		xstream.alias("xml", imageMessage.getClass());//将xml的根节点设置为xml
		
		return xstream.toXML(imageMessage);
	}
	
	public static String musicMessageToXml(MusicMessage musicMessage) {
		XStream xstream = new XStream();
		xstream.alias("xml", musicMessage.getClass());
		
		return xstream.toXML(musicMessage);
	}
	
	/**
	 * 文本消息的拼接
	 * @param toUserName
	 * @param fromUserName
	 * @param content
	 * @return
	 */
	public static String initTextMessage(String toUserName, String fromUserName, String content) {
		TextMessage text = new TextMessage();
		//返回消息，要把发送方和接收方调换位置
		text.setFromUserName(toUserName);
		text.setToUserName(fromUserName);
		text.setMsgType("text");
		text.setCreateTime("" + new Date().getTime());
		text.setContent(content);
		//text.setContent("http://johnwoods.tunnel.mobi/Weixin/image/running.jpg");
		
		return MessageUtil.textMessageToXml(text);
	}
	
	/**
	 * 图文消息的拼接
	 * @param toUserName
	 * @param fromUserName
	 * @return
	 */
	public static String initNewsMessage(String toUserName, String fromUserName) {
		String message = null;
		List<News> newsList = new ArrayList<News>();
		NewsMessage newsMessage = new NewsMessage();
		
		News news = new News();
		//news.setTitle("");
		news.setTitle("开心一刻");
		//news.setDescription("");
		news.setDescription("我学经济我苦恼，纸上谈兵阅历少。我学广告我闹心，收礼只收脑白金。"
				+ "我学程序我无助，代码从来靠百度。我学建筑我命苦，天天抱着混凝土。"
				+ "我学金融我骄傲，赔了期货赔股票。我学仪器我想笑，毕业拧拧螺丝帽。"
				+ "我学环境我不爽，毕业分配垃圾场。");
		news.setPicUrl("http://johnwoods.tunnel.mobi/Weixin/image/hhdx.gif");
		news.setUrl("http://www.qiushibaike.com/");
		newsList.add(news);
		
		newsMessage.setToUserName(fromUserName);
		newsMessage.setFromUserName(toUserName);
		newsMessage.setCreateTime("" + new Date().getTime());
		newsMessage.setMsgType(MESSAGE_NEWS);
		newsMessage.setArticles(newsList);
		newsMessage.setArticleCount(newsList.size());
		
		message = newsMessageToXml(newsMessage);
		return message;
	}
	
	/**
	 * 图像消息的拼接
	 * @param toUserName
	 * @param fromUserName
	 * @return
	 */
	public static String initImageMessage(String toUserName, String fromUserName) {
		String message = null;
		
		Image image = new Image();
		image.setMediaId("UpC4pXluuO5QfNeXuBk6GeCYKrLR8SRW74e57_fyOTIEV999VtySF8RkyJZyXAE4");
		
		ImageMessage imageMessage = new ImageMessage();
		imageMessage.setToUserName(fromUserName);
		imageMessage.setFromUserName(toUserName);
		imageMessage.setMsgType(MESSAGE_IMAGE);
		imageMessage.setCreateTime("" + new Date().getTime());
		imageMessage.setImage(image);
		
		message = imageMessageToXml(imageMessage);
		return message;
	}
	
	/**
	 * 音乐消息的拼接
	 * @param toUserName
	 * @param fromUserName
	 * @return
	 */
	public static String initMusicMessage(String toUserName, String fromUserName) {
		String message = "";
		
		Music music = new Music();
		//不加media_id，就可以推送成功
		//music.setThumbMediaId("UpC4pXluuO5QfNeXuBk6GeCYKrLR8SRW74e57_fyOTIEV999VtySF8RkyJZyXAE4");
		//music.setThumbMediaId("");
		music.setDescription("一首简单的小情歌，送给你！^_^");
		music.setMusicUrl("http://johnwoods.tunnel.mobi/Weixin/music/xqg_sdv.mp3");
		music.setHQMusicUrl("http://johnwoods.tunnel.mobi/Weixin/music/xqg_sdv.mp3");
		music.setTitle("小情歌");
		
		
		MusicMessage musicMessage = new MusicMessage();
		musicMessage.setToUserName(fromUserName);
		musicMessage.setFromUserName(toUserName);
		musicMessage.setMsgType(MESSAGE_MUSIC);
		musicMessage.setCreateTime("" + new Date().getTime());
		musicMessage.setMusic(music);
		
		message = musicMessageToXml(musicMessage);
		return message;
	}
	
	
	/**
	 * 拼接形成主菜单
	 * @return
	 */
	public static String menuText() {
		StringBuffer sb = new StringBuffer();
		sb.append("欢迎您的关注，请按照菜单提示进行操作：\n\n");
		sb.append("1.回复你一条文本消息\n");
		sb.append("2.回复你一条图文消息\n");
		sb.append("3.回复你一张图片消息\n");
		sb.append("4.一首歌，送给你\n\n");
		sb.append("回复？显示此菜单。");
		
		return sb.toString();
	}
	
	/**
	 * 回复“1”的返回消息
	 * @return
	 */
	public static String firstMenu() {
		StringBuffer sb = new StringBuffer();
		sb.append("越是憧憬，越是要风雨兼程");
		
		return sb.toString();
	}
	
}
