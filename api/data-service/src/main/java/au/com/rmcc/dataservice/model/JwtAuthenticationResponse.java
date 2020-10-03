package au.com.rmcc.dataservice.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class JwtAuthenticationResponse {
	
	@JsonProperty("accessToken")
	private String accessToken;
	
	@JsonIgnore
	private String tokenType = "Bearer";
	
	@JsonCreator
    public JwtAuthenticationResponse(@JsonProperty("accessToken") String accessToken) {
        this.accessToken = accessToken;
    }
}
