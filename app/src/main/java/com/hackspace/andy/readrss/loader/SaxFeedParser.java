package com.hackspace.andy.readrss.loader;

import com.hackspace.andy.readrss.Message;
import com.hackspace.andy.readrss.RssHandler;

import org.xml.sax.InputSource;
import org.xml.sax.XMLReader;

import java.util.List;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

public class SaxFeedParser extends BaseFeedParser<List<Message>> {

	private SAXParserFactory factoryParser;
	private SAXParser saxParser;
	private XMLReader xmlReader;
	private RssHandler rssHandler;
	private InputSource inputSource;

	public SaxFeedParser(String feedUrl, ILoaderData<List<Message>> endDataPoint) {
		super(feedUrl, endDataPoint);
	}
	
	public List<Message> parse() {

		factoryParser = SAXParserFactory.newInstance();

		try {
			saxParser = factoryParser.newSAXParser();
			xmlReader = saxParser.getXMLReader();
			rssHandler = new RssHandler();
			xmlReader.setContentHandler(rssHandler);

			inputSource = new InputSource(this.getInputStream());

			xmlReader.parse(inputSource);

			return rssHandler.getMessages();
		} catch (Exception e) {
			throw new RuntimeException(e);
		} 
	}


}