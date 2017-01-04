package com.hackspace.andy.readrss.loader;

import com.hackspace.andy.readrss.Message;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

public class DomFeedParser extends BaseFeedParser<List<Message>> {

	private DocumentBuilderFactory builderFactory;
	private List<Message> messagesList;
	private DocumentBuilder builder;
	private Document domParser;
	private Element root;
	private NodeList itemsList,properties,chars;
	private Message message;
	private Node item,propertyNode;
	private String name;
	private StringBuilder textMessage;

	public DomFeedParser(String feedUrl, ILoaderData<List<Message>> endDataPoint) {
		super(feedUrl, endDataPoint);
	}

	public List<Message> parse() {
		builderFactory = DocumentBuilderFactory.newInstance();
		messagesList = new ArrayList<>();
		try {
			builder = builderFactory.newDocumentBuilder();
			domParser = builder.parse(this.getInputStream());
			root = domParser.getDocumentElement();
			itemsList = root.getElementsByTagName(ITEM);
			for (int i=0;i<itemsList.getLength();i++){
				message = new Message();
				item = itemsList.item(i);
				properties = item.getChildNodes();
				for (int j=0;j<properties.getLength();j++){
					propertyNode = properties.item(j);
					name = propertyNode.getNodeName();
					if (name.equalsIgnoreCase(TITLE)){
						message.setTitle(propertyNode.getFirstChild().getNodeValue());
					} else if (name.equalsIgnoreCase(LINK)){
						message.setLink(propertyNode.getFirstChild().getNodeValue());
					} else if (name.equalsIgnoreCase(DESCRIPTION)){
						textMessage = new StringBuilder();
						chars = propertyNode.getChildNodes();
						for (int k=0;k<chars.getLength();k++){
							textMessage.append(chars.item(k).getNodeValue());
						}
						message.setDescription(textMessage.toString());
					} else if (name.equalsIgnoreCase(PUB_DATE)){
						message.setDate(propertyNode.getFirstChild().getNodeValue());
					}
				}
				messagesList.add(message);
			}
		} catch (Exception e) {
			throw new RuntimeException(e);
		}

		return messagesList;
	}
}
