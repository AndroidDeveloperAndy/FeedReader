package com.hackspace.andy.readrss.loader.Implementation;

import com.hackspace.andy.readrss.model.Entity.Message;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import java.util.ArrayList;
import java.util.List;

import static com.hackspace.andy.readrss.loader.Implementation.BaseFeedParser.CHANNEL;
import static com.hackspace.andy.readrss.loader.Implementation.BaseFeedParser.DESCRIPTION;
import static com.hackspace.andy.readrss.loader.Implementation.BaseFeedParser.ITEM;
import static com.hackspace.andy.readrss.loader.Implementation.BaseFeedParser.LINK;
import static com.hackspace.andy.readrss.loader.Implementation.BaseFeedParser.PUB_DATE;
import static com.hackspace.andy.readrss.loader.Implementation.BaseFeedParser.TITLE;

public class FeedHandler extends DefaultHandler {

	private List<Message> mMessages;
	private Message mCurrentMessage;
	private StringBuilder mBuilder;
	
	public List<Message> getMessages(){
		return this.mMessages;
	}

	@Override
	public void characters(char[] ch, int start, int length)
			throws SAXException {
		super.characters(ch, start, length);

		mBuilder.append(ch, start, length);
	}

	@Override
	public void endElement(String uri, String localName, String name)
			throws SAXException {
		super.endElement(uri, localName, name);

		if (this.mCurrentMessage != null){
			switch (localName) {
 				case TITLE:
					mCurrentMessage.setTitle(mBuilder.toString());
 				break;
 				case CHANNEL:
					mCurrentMessage.setLink(mBuilder.toString());
 				break;
 				case LINK:
					mCurrentMessage.setLink(mBuilder.toString());
 				break;
 				case DESCRIPTION:
					mCurrentMessage.setDescription(mBuilder.toString());
 				break;
				case PUB_DATE:
					mCurrentMessage.setDate(mBuilder.toString());
					break;
				case ITEM:
					mMessages.add(mCurrentMessage);
					break;
				default:
					break;
			}
			mBuilder.setLength(0);
		}
	}

	@Override
	public void startDocument() throws SAXException {
		super.startDocument();

		mMessages = new ArrayList<>();
		mBuilder = new StringBuilder();
	}

	@Override
	public void startElement(String uri, String localName, String name,
							 Attributes attributes) throws SAXException {
		super.startElement(uri, localName, name, attributes);

		if (localName.equalsIgnoreCase(ITEM)){
			this.mCurrentMessage = new Message();
		}
	}
}