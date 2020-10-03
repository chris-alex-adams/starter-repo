package au.com.rmcc.CoinDictionaryLoad;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.annotation.ComponentScan;

import au.com.rmcc.common.service.CoinDictionaryService;

@SpringBootApplication
@ComponentScan("au.com.rmcc")
public class CoinDictionaryLoadApplication 
{
	@Autowired
	CoinDictionaryService service;
	
    public static void main( String[] args )
    {
    	new SpringApplicationBuilder(CoinDictionaryLoadApplication.class).run(args);
    }
    
    @PostConstruct
    public void loadCoinInformation() {
    	String manifestFile = getClass().getResource("/manifest.json").getPath();
    	String iconFolder = getClass().getResource("/icon").getPath();
    	service.loadAllCoins(manifestFile, iconFolder);
    }
}
