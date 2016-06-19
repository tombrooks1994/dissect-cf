package iot.extension;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import hu.mta.sztaki.lpds.cloud.simulator.io.*;
import hu.mta.sztaki.lpds.cloud.simulator.*;

public class Metering extends DeferredEvent {
	int b,i;
	String sName;
	
	public Metering(int b,String sName,int i) {
		super(1);
		this.b = b;
		this.sName=sName;
		this.i=i;
	}

	@Override
	protected void eventAction() {
		Calendar cal = Calendar.getInstance();
		SimpleDateFormat sdf = new SimpleDateFormat("yy:MM:dd:HH:mm:ss:SS");
		
		StorageObject so = new StorageObject(Timed.getFireCount() + i+sName +" .:. " + sdf.format(cal.getTime()), 1024, false);
		//Station.getRepo().registerObject(so);
		for(Station station : Scenario.stations){
			if(station.name.equals(this.sName)){
				station.getRepo().registerObject(so);
			}
		}
	}
}
