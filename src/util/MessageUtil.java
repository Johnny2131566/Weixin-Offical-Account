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
	
	//��Ϣ����
	public static final String MESSAGE_TEXT = "text";//�ı�
	public static final String MESSAGE_NEWS = "news";//�ı�
	public static final String MESSAGE_IMAGE = "image";//ͼƬ
	public static final String MESSAGE_VOICE = "voice";//����
	public static final String MESSAGE_MUSIC = "music";//����λ��
	public static final String MESSAGE_VIDEO = "video";//��Ƶ
	public static final String MESSAGE_LINK = "link";//����
	public static final String MESSAGE_LOCATION = "location";//����λ��
	//�¼�����
	public static final String MESSAGE_EVENT = "event";//�¼�����
	public static final String MESSAGE_SUBSCRIBE = "subscribe";//�¼����͡���>��ע
	public static final String MESSAGE_UNSUBSCRIBE = "unsubscribe";//�¼����͡���>ȡ����ע
	public static final String MESSAGE_CLICK = "CLICK";//�¼����͡���>�˵����
	public static final String MESSAGE_VIEW = "VIEW";//�¼����͡���>��ת����
	public static final String MESSAGE_SCANCODE = "scancode_push";//�¼����͡���>ɨ���¼�
	
	//�����õ�imageid
	//public static final String MEDIA_ID = "Jd0vhwz_5Rpac6h_rVJbaI-iEr1afbQD6z-CoxtmTZPJhCNN8sGOzlMex7xNzw_O";
	/**
	 * xmlת��Ϊmap����
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
	 * ���ı���Ϣ����ת��Ϊxml
	 * @param textMessage
	 * @return
	 */
	public static String textMessageToXml(TextMessage textMessage) {
		XStream xstream = new XStream();
		xstream.alias("xml", textMessage.getClass());//��xml�ĸ��ڵ�����Ϊxml
		return xstream.toXML(textMessage);
	}
	
	/**
	 * ��ͼ����Ϣ����ת��Ϊxml
	 * @param newsMessage
	 * @return
	 */
	public static String newsMessageToXml(NewsMessage newsMessage) {
		XStream xstream = new XStream();
		xstream.alias("xml", newsMessage.getClass());//��xml�ĸ��ڵ�����Ϊxml
		xstream.alias("item", new News().getClass());
		return xstream.toXML(newsMessage);
	}
	
	/**
	 * ��ͼƬ��Ϣ����ת��Ϊxml
	 * @param imageMessage
	 * @return
	 */
	public static String imageMessageToXml(ImageMessage imageMessage) {
		XStream xstream = new XStream();
		xstream.alias("xml", imageMessage.getClass());//��xml�ĸ��ڵ�����Ϊxml
		
		return xstream.toXML(imageMessage);
	}
	
	public static String musicMessageToXml(MusicMessage musicMessage) {
		XStream xstream = new XStream();
		xstream.alias("xml", musicMessage.getClass());
		
		return xstream.toXML(musicMessage);
	}
	
	/**
	 * �ı���Ϣ��ƴ��
	 * @param toUserName
	 * @param fromUserName
	 * @param content
	 * @return
	 */
	public static String initTextMessage(String toUserName, String fromUserName, String content) {
		TextMessage text = new TextMessage();
		//������Ϣ��Ҫ�ѷ��ͷ��ͽ��շ�����λ��
		text.setFromUserName(toUserName);
		text.setToUserName(fromUserName);
		text.setMsgType("text");
		text.setCreateTime("" + new Date().getTime());
		text.setContent(content);
		//text.setContent("http://johnwoods.tunnel.mobi/Weixin/image/running.jpg");
		
		return MessageUtil.textMessageToXml(text);
	}
	
	/**
	 * ͼ����Ϣ��ƴ��
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
		news.setTitle("����һ��");
		//news.setDescription("");
		news.setDescription("��ѧ�����ҿ��գ�ֽ��̸�������١���ѧ��������ģ�����ֻ���԰׽�"
				+ "��ѧ����������������������ٶȡ���ѧ���������࣬���챧�Ż�������"
				+ "��ѧ�����ҽ����������ڻ����Ʊ����ѧ��������Ц����ҵšš��˿ñ��"
				+ "��ѧ�����Ҳ�ˬ����ҵ������������");
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
	 * ͼ����Ϣ��ƴ��
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
	 * ������Ϣ��ƴ��
	 * @param toUserName
	 * @param fromUserName
	 * @return
	 */
	public static String initMusicMessage(String toUserName, String fromUserName) {
		String message = "";
		
		Music music = new Music();
		//����media_id���Ϳ������ͳɹ�
		//music.setThumbMediaId("UpC4pXluuO5QfNeXuBk6GeCYKrLR8SRW74e57_fyOTIEV999VtySF8RkyJZyXAE4");
		//music.setThumbMediaId("");
		music.setDescription("һ�׼򵥵�С��裬�͸��㣡^_^");
		music.setMusicUrl("http://johnwoods.tunnel.mobi/Weixin/music/xqg_sdv.mp3");
		music.setHQMusicUrl("http://johnwoods.tunnel.mobi/Weixin/music/xqg_sdv.mp3");
		music.setTitle("С���");
		
		
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
	 * ƴ���γ����˵�
	 * @return
	 */
	public static String menuText() {
		StringBuffer sb = new StringBuffer();
		sb.append("��ӭ���Ĺ�ע���밴�ղ˵���ʾ���в�����\n\n");
		sb.append("1.�ظ���һ���ı���Ϣ\n");
		sb.append("2.�ظ���һ��ͼ����Ϣ\n");
		sb.append("3.�ظ���һ��ͼƬ��Ϣ\n");
		sb.append("4.һ�׸裬�͸���\n\n");
		sb.append("�ظ�����ʾ�˲˵���");
		
		return sb.toString();
	}
	
	/**
	 * �ظ���1���ķ�����Ϣ
	 * @return
	 */
	public static String firstMenu() {
		StringBuffer sb = new StringBuffer();
		sb.append("Խ���㽣�Խ��Ҫ������");
		
		return sb.toString();
	}
	
}
