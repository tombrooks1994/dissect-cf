package iot.extension;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import hu.mta.sztaki.lpds.cloud.simulator.io.*;
import hu.mta.sztaki.lpds.cloud.simulator.*;

public class Metering extends DeferredEvent {
	int b,i;
	String sName;
	
	public Metering(String sName) {
		super(1);
		
		this.sName=sName;
		
	}

	@Override
	protected void eventAction() {
		Calendar cal = Calendar.getInstance();
		SimpleDateFormat sdf = new SimpleDateFormat("yy:MM:dd:HH:mm:ss:SS");
		
		StorageObject so = new StorageObject(Timed.getFireCount() + i+sName +" .:. " + sdf.format(cal.getTime()), 1024, false);
		for(Station s : Station.stations){
			if(s.getName().equals(this.sName)){
				s.getRepo().registerObject(so);
				Station.SOnumber++;
			}
		}
		
	}
}
