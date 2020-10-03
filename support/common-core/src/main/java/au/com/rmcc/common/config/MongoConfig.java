package au.com.rmcc.common.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.config.AbstractMongoConfiguration;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

import com.mongodb.MongoClient;

@EnableMongoRepositories(basePackages = "au.com.rmcc")
@Configuration
public class MongoConfig extends AbstractMongoConfiguration {
	
	@Value("${db.mongo.ip}")
	private String mongoIP;
	
	@Value("${db.mongo.port}")
	private int mongoPort;
	
	@Value("${db.mongo.name}")
	private String name;

	@Override
	public MongoClient mongoClient() {
		return new MongoClient(mongoIP, mongoPort);
	}

	@Override
	protected String getDatabaseName() {
		return name;
	}

}
