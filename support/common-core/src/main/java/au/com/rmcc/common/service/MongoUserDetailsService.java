package au.com.rmcc.common.service;

import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import au.com.rmcc.common.model.User;
import au.com.rmcc.common.model.UserPrincipal;
import au.com.rmcc.common.repository.UserRepository;

@Component
public class MongoUserDetailsService implements UserDetailsService {
	private static final Logger logger = LoggerFactory.getLogger(MongoUserDetailsService.class);
	
	@Autowired
	UserRepository userRepo;
		
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		logger.info("Looking for user with username: {}", username);
		User user = userRepo.findByUsername(username);
		logger.info("Found user in db: {}, with username: {}", user, username);
		if(user == null) {
			throw new UsernameNotFoundException("User not found");
		}

		return UserPrincipal.create(user);
	}
	
	public UserDetails loadUserById(String id) throws UsernameNotFoundException {
		Optional<User> userOptional = userRepo.findById(id);
		
		if(!userOptional.isPresent()) {
			throw new UsernameNotFoundException("User not found");
		}

		return UserPrincipal.create(userOptional.get());
	}
	
	public User findByUsername(String username) {
		return userRepo.findByUsername(username);
	}
	
	public User findById(String userId) {
		Optional<User> user = userRepo.findById(userId);
		if(user.isPresent()) {
			return user.get();
		} else {
			return null;
		}
	}
	
	public String getCurrentUserId() {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		if (!(authentication instanceof AnonymousAuthenticationToken)) {
		    String currentUserName = authentication.getName();
		    return findByUsername(currentUserName).getId();
		}
		return "";
	}

}
