package au.com.rmcc.dataservice.model;

import lombok.Data;

@Data
public class SignUpRequest {
	String name;
	String username;
	String email;
	String password;
}
