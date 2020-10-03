package au.com.rmcc.common.model;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Role {
	private Long id;
	private RoleName name;
	
	public enum  RoleName {
	    ROLE_USER,
	    ROLE_ADMIN
	}
	
}
