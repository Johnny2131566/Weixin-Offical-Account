/**
 * 音乐消息格式
 * 
	<xml>
	<ToUserName><![CDATA[toUser]]></ToUserName>
	<FromUserName><![CDATA[fromUser]]></FromUserName>
	<CreateTime>12345678</CreateTime>
	<MsgType><![CDATA[music]]></MsgType>
	<Music>
	<Title><![CDATA[TITLE]]></Title>
	<Description><![CDATA[DESCRIPTION]]></Description>
	<MusicUrl><![CDATA[MUSIC_Url]]></MusicUrl>
	<HQMusicUrl><![CDATA[HQ_MUSIC_Url]]></HQMusicUrl>
	<ThumbMediaId><![CDATA[media_id]]></ThumbMediaId>
	</Music>
	</xml>
 * 
 */

package message;

import entity.Music;

public class MusicMessage extends BaseMessage {
	
	private Music Music;

	public Music getMusic() {
		return Music;
	}

	public void setMusic(Music music) {
		this.Music = music;
	}
}
