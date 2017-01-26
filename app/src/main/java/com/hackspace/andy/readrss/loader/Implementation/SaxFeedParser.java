package com.hackspace.andy.readrss.loader.Implementation;

import com.hackspace.andy.readrss.loader.ILoaderData;
import com.hackspace.andy.readrss.model.Entity.Message;

import org.xml.sax.InputSource;
import org.xml.sax.XMLReader;

import java.util.List;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

public class SaxFeedParser extends BaseFeedParser<List<Message>> {

	private SAXParserFactory mFactoryParser = SAXParserFactory.newInstance();
	private SAXParser SaxParser;
	private XMLReader XmlReader;
	private FeedHandler mFeedHandler;
	private InputSource mInputSource;

	public SaxFeedParser(String feedUrl, ILoaderData<List<Message>> endDataPoint) {
		super(feedUrl, endDataPoint);
	}
	
	public List<Message> parse() {
		try {
			SaxParser = mFactoryParser.newSAXParser();
			XmlReader = SaxParser.getXMLReader();
			mFeedHandler = new FeedHandler();
			XmlReader.setContentHandler(mFeedHandler);

			mInputSource = new InputSource(this.getInputStream());

			XmlReader.parse(mInputSource);

			return mFeedHandler.getMessages();
		} catch (Exception e) {
			//TODO Check all similar cases, remember do not ignore exceptions like this, only if it's not necessary.
			throw new RuntimeException(e);
		}
	}
}