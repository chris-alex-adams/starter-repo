package au.com.rmcc.common.repository;

import java.util.List;

import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Component;

import au.com.rmcc.common.model.CommonTicker;


@Component
public interface CommonTickerRepository extends MongoRepository<CommonTicker, String>{
	
	List<CommonTicker> findByCoinIdAndUnits(String coinId, String units, Sort sort);
	
	List<CommonTicker> findByCoinIdAndUnitsAndExchangeName(String coinId, String units, String exchangeName);
}
