package me.renews.data;

import java.util.Date;

import javax.jdo.annotations.IdGeneratorStrategy;
import javax.jdo.annotations.IdentityType;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;

import com.google.appengine.api.datastore.Text;

@PersistenceCapable(identityType = IdentityType.APPLICATION)
public class TextByUrl {
	@PrimaryKey
	@Persistent(valueStrategy = IdGeneratorStrategy.IDENTITY)
	private Long id;
	@Persistent
	private Date timestamp;
	@Persistent
	private String url;
	@Persistent
	private Text text;

	public TextByUrl(String url, Text text) {
		this.url = url;
		this.text = text;
		this.timestamp = new Date();
	}

	public Long getId() {
		return id;
	}

	public String getUrl() {
		return url;
	}

	public Text getText() {
		return text;
	}
}
