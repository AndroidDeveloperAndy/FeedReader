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

	private List<Message> messages;
	private Message currentMessage;
	private StringBuilder builder;
	
	public List<Message> getMessages(){
		return this.messages;
	}

	@Override
	public void characters(char[] ch, int start, int length)
			throws SAXException {
		super.characters(ch, start, length);

		builder.append(ch, start, length);
	}

	@Override
	public void endElement(String uri, String localName, String name)
			throws SAXException {
		super.endElement(uri, localName, name);

		if (this.currentMessage != null){
			//TODO Use switch case instead of if else block.
			/*switch (localName) {
				case TITLE:
					currentMessage.setTitle(builder.toString());
				break;
				default:
					break;
			}*/
			if (localName.equalsIgnoreCase(TITLE)){
				currentMessage.setTitle(builder.toString());
			} else if (localName.equalsIgnoreCase(CHANNEL)){
				currentMessage.setLink(builder.toString());
			} else if (localName.equalsIgnoreCase(LINK)){
				currentMessage.setLink(builder.toString());
			} else if (localName.equalsIgnoreCase(DESCRIPTION)){
				currentMessage.setDescription(builder.toString());
			} else if (localName.equalsIgnoreCase(PUB_DATE)){
				currentMessage.setDate(builder.toString());
			} else if (localName.equalsIgnoreCase(ITEM)){
				messages.add(currentMessage);
			}
			builder.setLength(0);	
		}
	}

	@Override
	public void startDocument() throws SAXException {
		super.startDocument();

		messages = new ArrayList<>();
		builder = new StringBuilder();
	}

	@Override
	public void startElement(String uri, String localName, String name,
							 Attributes attributes) throws SAXException {
		super.startElement(uri, localName, name, attributes);

		if (localName.equalsIgnoreCase(ITEM)){
			this.currentMessage = new Message();
		}
	}
}