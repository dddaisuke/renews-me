package me.renews.data;

import java.util.Date;

import javax.jdo.annotations.IdGeneratorStrategy;
import javax.jdo.annotations.IdentityType;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;

@PersistenceCapable(identityType = IdentityType.APPLICATION)
public class WordByUrl {
	@PrimaryKey
	@Persistent(valueStrategy = IdGeneratorStrategy.IDENTITY)
	private Long id;
	@Persistent
	private Date timestamp;
	@Persistent
	private String url;
	@Persistent
	private String word;
	@Persistent
	private Integer count;
	@Persistent
	private Integer rating;

	public WordByUrl(String url, String word, Integer count, Integer rating) {
		this.url = url;
		this.word = word;
		this.count = count;
		this.rating = rating;
		this.timestamp = new Date();
	}

	public String getUrl() {
		return url;
	}

	public String getWord() {
		return word;
	}

	public Integer getCount() {
		return count;
	}

	public Integer getRating() {
		return rating;
	}
}
