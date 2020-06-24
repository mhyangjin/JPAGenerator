package com.udmtek.DBCoreGen;

import org.springframework.context.annotation.ComponentScan;

import com.udmtek.DBCoreGen.Comm.DBCoreGenLogger;

@ComponentScan(basePackages = "com.udmtek.DBCoreGen")
public class DbCoreGenApplication {
	public static void main(String[] args) {
		ServiceController maincontrol = new ServiceController();
		if ( maincontrol.readconfig() )
		{
			maincontrol.serviceProcess();
		}
	}
}

