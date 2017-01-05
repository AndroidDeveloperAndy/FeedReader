package com.hackspace.andy.readrss.loader;

import android.annotation.TargetApi;
import android.os.Build;
import android.sax.Element;
import android.sax.RootElement;
import android.util.Log;
import android.util.Xml;

import com.hackspace.andy.readrss.model.Message;

import java.util.ArrayList;
import java.util.List;

public class AndroidSaxFeedParser extends BaseFeedParser<List<Message>> {

    private static final String TAG = AndroidSaxFeedParser.class.getName();;
    private Element channel,item;

	static final String RSS = "rss";

	public AndroidSaxFeedParser(String feedUrl, ILoaderData<List<Message>> endDataPoint) {
		super(feedUrl, endDataPoint);
	}

    public List<Message> parse() {
		final Message currentMessage = new Message();
		final RootElement root = new RootElement(RSS);
		final List<Message> messages = new ArrayList<>();

		try {
		channel = root.getChild(CHANNEL);
		item = channel.getChild(ITEM);
		item.setEndElementListener(() -> messages.add(currentMessage.copy()));
		item.getChild(TITLE).setEndTextElementListener(body -> currentMessage.setTitle(body));
		item.getChild(LINK).setEndTextElementListener(body -> currentMessage.setLink(body));
		item.getChild(DESCRIPTION).setEndTextElementListener(body -> currentMessage.setDescription(body));
		item.getChild(PUB_DATE).setEndTextElementListener(body -> currentMessage.setDate(body));

			Xml.parse(this.getInputStream(), Xml.Encoding.UTF_8, root.getContentHandler());
		} catch (Exception e) {
            Log.e(TAG, "Error parse AndroidSax feed!", e);
			throw new RuntimeException(e);
		}

		return messages;
	}
}
