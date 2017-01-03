package com.hackspace.andy.readrss.loader;

import com.hackspace.andy.readrss.Message;
import com.hackspace.andy.readrss.RssHandler;

import java.util.List;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

public class SaxFeedParser extends BaseFeedParser<List<Message>> {

	public SaxFeedParser(String feedUrl, ILoaderData<List<Message>> endDataPoint) {
		super(feedUrl, endDataPoint);
	}
	
	public List<Message> parse() {
		SAXParserFactory factory = SAXParserFactory.newInstance();
		try {
			SAXParser parser = factory.newSAXParser();
			RssHandler handler = new RssHandler();
			parser.parse(this.getInputStream(), handler);
			return handler.getMessages();
		} catch (Exception e) {
			throw new RuntimeException(e);
		} 
	}


}