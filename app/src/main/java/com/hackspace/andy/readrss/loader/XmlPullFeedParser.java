package com.hackspace.andy.readrss.loader;

import android.util.Log;
import android.util.Xml;

import com.hackspace.andy.readrss.Message;

import org.xmlpull.v1.XmlPullParser;

import java.util.ArrayList;
import java.util.List;

public class XmlPullFeedParser extends BaseFeedParser<List<Message>> {

	private List<Message> messages = null;
	private Message currentMessage = null;
	private XmlPullParser xmlPullParser;
	private int eventType;
	private boolean done = false;
	private String name;

	public XmlPullFeedParser(String feedUrl, ILoaderData<List<Message>> endDataPoint) {
		super(feedUrl, endDataPoint);
	}

	public List<Message> parse() {
		xmlPullParser = Xml.newPullParser();
		try {
			xmlPullParser.setInput(this.getInputStream(), null);
			eventType = xmlPullParser.getEventType();
			while (eventType != XmlPullParser.END_DOCUMENT && !done){
				switch (eventType){
					case XmlPullParser.START_DOCUMENT:
						messages = new ArrayList<>();
						break;
					case XmlPullParser.START_TAG:
						name = xmlPullParser.getName();
						if (name.equalsIgnoreCase(ITEM)){
							currentMessage = new Message();
						} else if (currentMessage != null){
							if (name.equalsIgnoreCase(LINK)){
								currentMessage.setLink(xmlPullParser.nextText());
							} else if (name.equalsIgnoreCase(DESCRIPTION)){
								currentMessage.setDescription(xmlPullParser.nextText());
							} else if (name.equalsIgnoreCase(PUB_DATE)){
								currentMessage.setDate(xmlPullParser.nextText());
							} else if (name.equalsIgnoreCase(TITLE)){
								currentMessage.setTitle(xmlPullParser.nextText());
							}	
						}
						break;
					case XmlPullParser.END_TAG:
						name = xmlPullParser.getName();
						if (name.equalsIgnoreCase(ITEM) && currentMessage != null){
							messages.add(currentMessage);
						} else if (name.equalsIgnoreCase(CHANNEL)){
							done = true;
						}
						break;
				}
				eventType = xmlPullParser.next();
			}
		} catch (Exception e) {
			Log.e("AndroidNews::PullFeedParser", e.getMessage(), e);
			throw new RuntimeException(e);
		}

		return messages;
	}
}
