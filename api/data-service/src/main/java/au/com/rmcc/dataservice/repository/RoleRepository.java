package au.com.rmcc.dataservice.repository;

import org.springframework.data.mongodb.repository.MongoRepository;

import au.com.anymoove.common.model.Role;

public interface RoleRepository extends MongoRepository<Role, String> {
	
	Role findByName(String name);

}
