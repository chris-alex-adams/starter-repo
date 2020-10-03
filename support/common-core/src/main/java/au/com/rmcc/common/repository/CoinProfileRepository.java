package au.com.rmcc.common.repository;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;

import au.com.rmcc.common.model.CoinProfile;

public interface CoinProfileRepository extends MongoRepository<CoinProfile, String> {
	List<CoinProfile> findBySymbol(String symbol);
}
