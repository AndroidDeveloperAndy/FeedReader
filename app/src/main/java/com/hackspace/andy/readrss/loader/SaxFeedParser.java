package com.hackspace.andy.readrss.loader;

import android.util.Log;

import com.hackspace.andy.readrss.model.Message;

import org.xml.sax.InputSource;
import org.xml.sax.XMLReader;

import java.util.List;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

public class SaxFeedParser extends BaseFeedParser<List<Message>> {

	private static final String TAG = SaxFeedParser.class.getName();
	private SAXParserFactory factoryParser;
	private SAXParser saxParser;
	private XMLReader xmlReader;
	private FeedHandler feedHandler;
	private InputSource inputSource;

	public SaxFeedParser(String feedUrl, ILoaderData<List<Message>> endDataPoint) {
		super(feedUrl, endDataPoint);
	}
	
	public List<Message> parse() {

		factoryParser = SAXParserFactory.newInstance();

		try {
			saxParser = factoryParser.newSAXParser();
			xmlReader = saxParser.getXMLReader();
			feedHandler = new FeedHandler();
			xmlReader.setContentHandler(feedHandler);

			inputSource = new InputSource(this.getInputStream());

			xmlReader.parse(inputSource);

			return feedHandler.getMessages();
		} catch (Exception e) {
			Log.e(TAG, "Error parse Sax feed!", e);
			throw new RuntimeException(e);
		} 
	}


}