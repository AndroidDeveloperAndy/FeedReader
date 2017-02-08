package com.hackspace.andy.readrss.loader.Implementation;

import com.hackspace.andy.readrss.loader.ILoaderData;
import com.hackspace.andy.readrss.model.Entity.Message;
import com.hackspace.andy.readrss.view.Implementation.PrimaryFeedActivity;
import com.hackspace.andy.readrss.view.PrimaryFeedView;

import org.xml.sax.InputSource;
import org.xml.sax.XMLReader;

import java.util.List;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

public class SaxFeedParser extends BaseFeedParser<List<Message>> {

	private SAXParserFactory mFactoryParser = SAXParserFactory.newInstance();
	private PrimaryFeedView mFeedView = new PrimaryFeedActivity();
	private SAXParser SaxParser;
	private XMLReader XmlReader;
	private FeedHandler mFeedHandler;
	private InputSource mInputSource;

	public SaxFeedParser(ILoaderData<List<Message>> endDataPoint) {
		super(endDataPoint);
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
			mFeedView.messageBox("parse",e.getMessage());
			throw new RuntimeException(e);
		}
	}
}