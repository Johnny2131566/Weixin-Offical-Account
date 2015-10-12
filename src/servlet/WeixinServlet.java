package servlet;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Date;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.dom4j.DocumentException;

import message.TextMessage;
import util.CheckUtil;
import util.MessageUtil;
import util.TransUtil;

public class WeixinServlet extends HttpServlet {

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		//super.doPost(req, resp);
		req.setCharacterEncoding("UTF-8");
		resp.setCharacterEncoding("UTF-8");
		
		PrintWriter out = resp.getWriter();
		try {
			Map<String, String> map = MessageUtil.xmlToMap(req);
			String toUserName = map.get("ToUserName");
			String fromUserName = map.get("FromUserName");
			String msgType = map.get("MsgType");
			String content = map.get("Content");
			
			String message = null;
			if(MessageUtil.MESSAGE_TEXT.equals(msgType)) {
				//返回消息，要把发送方和接收方调换位置
				if("1".equals(content)) {
					message = MessageUtil.initTextMessage(toUserName, fromUserName, MessageUtil.firstMenu());
					//message = MessageUtil.initMusicMessage(toUserName, fromUserName);
				} else if("2".equals(content)) {
					message = MessageUtil.initNewsMessage(toUserName, fromUserName);
				} else if("3".equals(content)) {
					message = MessageUtil.initImageMessage(toUserName, fromUserName);;
				} else if("4".equals(content)) {
					message = MessageUtil.initMusicMessage(toUserName, fromUserName);
				} else if("?".equals(content) || "？".equals(content)) {
					message = MessageUtil.initTextMessage(toUserName, fromUserName, MessageUtil.menuText());
				} else if(content.startsWith("翻译")) {
					message = MessageUtil.initTextMessage(toUserName, fromUserName, TransUtil.doTransJob(content));
				}
			} else if(MessageUtil.MESSAGE_EVENT.equals(msgType)) {
				String eventType = map.get("Event");
				if(MessageUtil.MESSAGE_SUBSCRIBE.equals(eventType)) {
					message = MessageUtil.initTextMessage(toUserName, fromUserName, MessageUtil.menuText());
				}else if(MessageUtil.MESSAGE_CLICK.equals(eventType)) {
					message = MessageUtil.initTextMessage(toUserName, fromUserName, MessageUtil.menuText());
				}else if(MessageUtil.MESSAGE_VIEW.equals(eventType)) {
					String url = map.get("EventKey");
					message = MessageUtil.initTextMessage(toUserName, fromUserName, url);
				}else if(MessageUtil.MESSAGE_SCANCODE.equals(eventType)) {
					String key = map.get("EventKey");
					message = MessageUtil.initTextMessage(toUserName, fromUserName, key);
				}
			} else if(MessageUtil.MESSAGE_LOCATION.equals(msgType)) {
				String label = map.get("Label");
				message = MessageUtil.initTextMessage(toUserName, fromUserName, label);
			}
			//////////////
			System.out.println(message);
			out.print(message);
			
		} catch (DocumentException e) {
			e.printStackTrace();
		} finally {
			out.close();
		}
		
	}

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		//super.doGet(req, resp);
		String signature = req.getParameter("signature");
		String timestamp = req.getParameter("timestamp");
		String nonce = req.getParameter("nonce");
		String echostr = req.getParameter("echostr");
		
		PrintWriter out = resp.getWriter();
		if(CheckUtil.checkSignature(signature, timestamp, nonce)) {
			out.print(echostr);
		}
	}
	
}
