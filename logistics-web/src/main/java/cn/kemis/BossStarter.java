package cn.kemis;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@SpringBootApplication
@EnableAutoConfiguration
@EnableTransactionManagement
@ComponentScan(basePackages = {"cn.kemis", "org.jeecgframework.poi.excel.view"})
public class BossStarter {

	public static void main(String[] args) {
		SpringApplication.run(BossStarter.class, args);
	}
}
