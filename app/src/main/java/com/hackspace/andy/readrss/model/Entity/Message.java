package com.hackspace.andy.readrss.model.Entity;

import io.realm.RealmObject;
import io.realm.annotations.Required;

public class Message extends RealmObject  {

	@Required private String title;
	@Required private String link;
	@Required private String description;
	@Required private String date;

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title.trim();
	}

	public String getLink() {
		return link;
	}

	public void setLink(String link) {
		this.link = link;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description.trim();
	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
			this.date = date;
	}
}
