package au.com.rmcc.dataservice;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.annotation.ComponentScan;

import springfox.documentation.swagger2.annotations.EnableSwagger2;

@SpringBootApplication
@EnableSwagger2
@ComponentScan(basePackages = { "au.com.rmcc", "au.com.rmcc.userservice.config"})
public class DataServiceApplication 
{
    public static void main( String[] args )
    {
    	new SpringApplicationBuilder(DataServiceApplication.class).run(args);
    }
}
