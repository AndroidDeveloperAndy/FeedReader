package com.hackspace.andy.readrss.model.Entity;

import android.util.Log;

import org.androidannotations.annotations.EBean;

import java.text.ParseException;
import java.text.SimpleDateFormat;

import io.realm.RealmObject;
import io.realm.annotations.Required;

@EBean
public class Message extends RealmObject  {

	private static final String DATE_FORMAT = "EEE, dd MMM yyyy HH:mm:ss Z";
	private static final SimpleDateFormat FORMATTER = new SimpleDateFormat(DATE_FORMAT);

	@Required
	private String title;

	@Required
	private String link;

	@Required
	private String description;

	@Required
	private String date;

	private static final String TAG = Message.class.getName();

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
		try {
			this.date = String.valueOf(FORMATTER.parse(date.trim()));
		} catch (ParseException e) {
			Log.e(TAG, "Error set date!", e);
		}
	}
}
