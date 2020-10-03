package au.com.rmcc.common.model;

import java.util.HashSet;
import java.util.Set;

import org.springframework.data.annotation.Id;

import lombok.Data;

@Data
public class User extends DateAudit {
    @Id
    private String id;
    public String name;
    public String username;
    public String email;
    public String password;
    private Set<Role> roles = new HashSet<>();
    
    public User(String name, String username, String email, String password) {
    	this.name = name;
    	this.username = username;
    	this.email = email;
    	this.password = password;
    }
}
