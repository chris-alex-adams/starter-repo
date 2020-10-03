package au.com.rmcc.dataload;

import java.io.IOException;
import java.util.Properties;

import org.quartz.spi.JobFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.config.PropertiesFactoryBean;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceTransactionManagerAutoConfiguration;
import org.springframework.boot.autoconfigure.orm.jpa.HibernateJpaAutoConfiguration;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.core.io.ClassPathResource;
import org.springframework.scheduling.annotation.EnableScheduling;

import au.com.rmcc.dataload.config.SpringJobFactory;

@EnableScheduling
@SpringBootApplication(exclude={DataSourceAutoConfiguration.class, HibernateJpaAutoConfiguration.class, DataSourceTransactionManagerAutoConfiguration.class})
@ComponentScan({"au.com.rmcc", "au.com.rmcc.dataservice.repository"})
public class ExchangeDataLoad 
{
	
	Logger logger = LoggerFactory.getLogger(getClass());
	
    @Bean("quartzProperties")
    public Properties quartzProperties() throws IOException {
       PropertiesFactoryBean propertiesFactoryBean = new PropertiesFactoryBean();
       propertiesFactoryBean.setLocation(new ClassPathResource(
             "/quartz.properties"));
       propertiesFactoryBean.afterPropertiesSet();
       return propertiesFactoryBean.getObject();
    }
    
    @Bean
    public JobFactory jobFactory(ApplicationContext applicationContext) {
       SpringJobFactory jobFactory = new SpringJobFactory();
       jobFactory.setApplicationContext(applicationContext);
       return jobFactory;
    }
	
    public static void main( String[] args )
    {
    	 new SpringApplicationBuilder(ExchangeDataLoad.class).run(args);
    }
    
}
