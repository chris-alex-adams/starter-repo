package au.com.rmcc.dataservice.model;

import lombok.Data;

@Data
public class ResponseMessage {
	private int status;
	private String message;

}
