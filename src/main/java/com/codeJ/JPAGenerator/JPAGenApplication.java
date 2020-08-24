package com.codeJ.JPAGenerator;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import org.springframework.context.annotation.ComponentScan;

import com.codeJ.JPAGenerator.Comm.DBCoreGenLogger;

@ComponentScan(basePackages = "com.codeJ.JPAGenerator")
public class JPAGenApplication {
	public static void main(String[] args) {
		ServiceController maincontrol = new ServiceController();
//		System.out.println(LocalDateTime.now().format(DateTimeFormatter.ofPattern("YYYYMMddHHmmssn")));
		if ( maincontrol.readconfig() )
		{
			maincontrol.serviceProcess();
		}
	}
}

