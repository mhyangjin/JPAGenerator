package com.udmtek.DBCoreGen;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import org.springframework.context.annotation.ComponentScan;

import com.udmtek.DBCoreGen.Comm.DBCoreGenLogger;

@ComponentScan(basePackages = "com.udmtek.DBCoreGen")
public class DbCoreGenApplication {
	public static void main(String[] args) {
		ServiceController maincontrol = new ServiceController();
//		System.out.println(LocalDateTime.now().format(DateTimeFormatter.ofPattern("YYYYMMddHHmmssn")));
		if ( maincontrol.readconfig() )
		{
			maincontrol.serviceProcess();
		}
	}
}

