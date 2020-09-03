package com.codeJ.JPAGenerator;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.ComponentScan;

@ComponentScan(basePackages = "com.codeJ.JPAGenerator")
public class JPAGenApplication {
	private static Logger logger = LoggerFactory.getLogger(JPAGenApplication.class);
	
	public static void main(String[] args) {
		ServiceController maincontrol = new ServiceController();
//		System.out.println(LocalDateTime.now().format(DateTimeFormatter.ofPattern("YYYYMMddHHmmssn")));
		if ( maincontrol.readconfig() )
		{
			maincontrol.serviceProcess();
		}
	}
}

