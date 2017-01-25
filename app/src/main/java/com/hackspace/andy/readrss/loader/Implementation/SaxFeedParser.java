package com.hackspace.andy.readrss.loader.Implementation;

import com.hackspace.andy.readrss.loader.ILoaderData;
import com.hackspace.andy.readrss.model.Entity.Message;

import org.xml.sax.InputSource;
import org.xml.sax.XMLReader;

import java.util.List;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

public class SaxFeedParser extends BaseFeedParser<List<Message>> {

	//TODO init singleton in top of class file instead.
	private SAXParserFactory factoryParser = SAXParserFactory.newInstance();
	private SAXParser saxParser;
	private XMLReader xmlReader; //TODO naming code conventions m for members fields etc...
	private FeedHandler feedHandler;
	private InputSource inputSource;

	public SaxFeedParser(String feedUrl, ILoaderData<List<Message>> endDataPoint) {
		super(feedUrl, endDataPoint);
	}
	
	public List<Message> parse() {
		try {
			saxParser = factoryParser.newSAXParser();
			xmlReader = saxParser.getXMLReader();
			feedHandler = new FeedHandler();
			xmlReader.setContentHandler(feedHandler);

			inputSource = new InputSource(this.getInputStream());

			xmlReader.parse(inputSource);

			return feedHandler.getMessages();
		} catch (Exception e) {
			//TODO Check all similar cases, remember do not ignore exceptions like this, only if it's not necessary.
			throw new RuntimeException(e);
		}
	}
}