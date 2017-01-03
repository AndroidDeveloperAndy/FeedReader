package com.hackspace.andy.readrss.loader;

import android.util.Log;
import android.util.Xml;

import com.hackspace.andy.readrss.Message;

import org.xmlpull.v1.XmlPullParser;

import java.util.ArrayList;
import java.util.List;

public class XmlPullFeedParser extends BaseFeedParser<List<Message>> {

	public XmlPullFeedParser(String feedUrl, ILoaderData<List<Message>> endDataPoint) {
		super(feedUrl, endDataPoint);
	}
	public List<Message> parse() {
		List<Message> messages = null;
		XmlPullParser parser = Xml.newPullParser();
		try {
			parser.setInput(this.getInputStream(), null);
			int eventType = parser.getEventType();
			Message currentMessage = null;
			boolean done = false;
			while (eventType != XmlPullParser.END_DOCUMENT && !done){
				String name;
				switch (eventType){
					case XmlPullParser.START_DOCUMENT:
						messages = new ArrayList<>();
						break;
					case XmlPullParser.START_TAG:
						name = parser.getName();
						if (name.equalsIgnoreCase(ITEM)){
							currentMessage = new Message();
						} else if (currentMessage != null){
							if (name.equalsIgnoreCase(LINK)){
								currentMessage.setLink(parser.nextText());
							} else if (name.equalsIgnoreCase(DESCRIPTION)){
								currentMessage.setDescription(parser.nextText());
							} else if (name.equalsIgnoreCase(PUB_DATE)){
								currentMessage.setDate(parser.nextText());
							} else if (name.equalsIgnoreCase(TITLE)){
								currentMessage.setTitle(parser.nextText());
							}	
						}
						break;
					case XmlPullParser.END_TAG:
						name = parser.getName();
						if (name.equalsIgnoreCase(ITEM) && currentMessage != null){
							messages.add(currentMessage);
						} else if (name.equalsIgnoreCase(CHANNEL)){
							done = true;
						}
						break;
				}
				eventType = parser.next();
			}
		} catch (Exception e) {
			Log.e("AndroidNews::PullFeedParser", e.getMessage(), e);
			throw new RuntimeException(e);
		}
		return messages;
	}
}
