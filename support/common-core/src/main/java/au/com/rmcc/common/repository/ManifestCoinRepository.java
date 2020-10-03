package au.com.rmcc.common.repository;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Component;

import au.com.rmcc.common.model.ManifestCoin;

@Component
public interface ManifestCoinRepository extends MongoRepository<ManifestCoin, String> {
	List<ManifestCoin> findBySymbol(String symbol);
}
