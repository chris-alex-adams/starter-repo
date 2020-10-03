package au.com.rmcc.common.repository;

import org.springframework.data.mongodb.repository.MongoRepository;

import au.com.rmcc.common.model.User;

public interface UserRepository extends MongoRepository<User, String> {
	
	User findByUsername(String username);
	
	boolean existsByEmail(String email);
	boolean existsByUsername(String username);

}
