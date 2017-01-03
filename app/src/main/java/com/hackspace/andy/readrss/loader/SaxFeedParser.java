package com.hackspace.andy.readrss.loader;

import com.hackspace.andy.readrss.Message;
import com.hackspace.andy.readrss.RssHandler;

import org.xml.sax.InputSource;
import org.xml.sax.XMLReader;

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
			SAXParser saxParser = factory.newSAXParser();
			XMLReader xmlReader = saxParser.getXMLReader();
			RssHandler rssHandler = new RssHandler();
			xmlReader.setContentHandler(rssHandler);

			InputSource inputSource = new InputSource(this.getInputStream());

			/*После этой команды происходит считывание и парсинг файла файла RSS ленты*/
			xmlReader.parse(inputSource);

			return rssHandler.getMessages();
		} catch (Exception e) {
			throw new RuntimeException(e);
		} 
	}


}