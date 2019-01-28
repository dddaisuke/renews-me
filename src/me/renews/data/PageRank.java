package me.renews.data;

import java.util.Date;

import javax.jdo.annotations.IdGeneratorStrategy;
import javax.jdo.annotations.IdentityType;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;

@PersistenceCapable(identityType = IdentityType.APPLICATION)
public class PageRank {
	@PrimaryKey
	@Persistent(valueStrategy = IdGeneratorStrategy.IDENTITY)
	private Long id;
	@Persistent
	private Date timestamp;
	@Persistent
	private String url;
	@Persistent
	private Integer ranking;

	public PageRank(String url, Integer ranking) {
		this.url = url;
		this.ranking = ranking;
		this.timestamp = new Date();
	}
}
