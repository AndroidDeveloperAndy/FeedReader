package com.hackspace.andy.readrss.loader.implementation;

import android.util.Log;

import com.hackspace.andy.readrss.loader.interfaces.ILoaderData;
import com.hackspace.andy.readrss.model.Entity.Message;
import com.hackspace.andy.readrss.view.implementation.PrimaryFeedActivity;
import com.hackspace.andy.readrss.view.interfaces.PrimaryFeedView;

import org.xml.sax.InputSource;
import org.xml.sax.XMLReader;

import java.util.List;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import static com.hackspace.andy.readrss.util.ResourceUtils.TAG_SAX_PARSER;

public class SaxFeedParser extends BaseFeedParser<List<Message>> {

	private SAXParserFactory mFactoryParser = SAXParserFactory.newInstance();
	private PrimaryFeedView mFeedView = new PrimaryFeedActivity();

	SaxFeedParser(ILoaderData<List<Message>> endDataPoint) {
		super(endDataPoint);
	}
	
	public List<Message> parse() {
		try {
			SAXParser saxParser = mFactoryParser.newSAXParser();
			XMLReader xmlReader = saxParser.getXMLReader();
			FeedHandler mFeedHandler = new FeedHandler();
			xmlReader.setContentHandler(mFeedHandler);
			InputSource mInputSource = new InputSource(this.getInputStream());
			xmlReader.parse(mInputSource);
			return mFeedHandler.getMessages();
		} catch (Exception e) {
			mFeedView.showError();
			Log.d(TAG_SAX_PARSER,"parse: ",e);
			throw new RuntimeException(e);
		}
	}
}