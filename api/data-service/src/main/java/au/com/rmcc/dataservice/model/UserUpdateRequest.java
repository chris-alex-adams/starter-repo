package au.com.rmcc.dataservice.model;

import lombok.Data;

@Data
public class UserUpdateRequest {
	private String oldUsername;
	private String username;
	private String email;
	private String newPassword;
	private String oldPassword;

}
