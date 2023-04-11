package com.duelivotni.joborobot;

import com.duelivotni.joborobot.auth.client.HeadHunterOAuthClient;
import com.duelivotni.joborobot.auth.controller.AuthController;
import com.duelivotni.joborobot.auth.controller.TestController;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.ImportAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.cloud.openfeign.FeignAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@EnableFeignClients
@ImportAutoConfiguration({FeignAutoConfiguration.class})
//@ComponentScan(basePackageClasses = {TestController.class, AuthController.class})
public class JoboRobotApplication {

	public static void main(String[] args) {
		SpringApplication.run(JoboRobotApplication.class, args);
	}

}
