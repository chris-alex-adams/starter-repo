package au.com.rmcc.common.model;

import java.time.Instant;

import lombok.Data;

@Data
public class DateAudit{
	
	private Instant createdAt;
	private Instant updatedAt;

}
