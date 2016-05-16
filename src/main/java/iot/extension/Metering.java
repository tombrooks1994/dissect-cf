package iot.extension;


import java.text.SimpleDateFormat;
import java.util.Calendar;
import hu.mta.sztaki.lpds.cloud.simulator.io.*;
import hu.mta.sztaki.lpds.cloud.simulator.*;



public class Metering extends DeferredEvent{

public Metering(long delay) {
		super(delay);
	}

	
	@Override
	protected void eventAction() {

			Calendar cal = Calendar.getInstance();
			SimpleDateFormat sdf = new SimpleDateFormat("yy:MM:dd:HH:mm:ss:SS");
			StorageObject so = new StorageObject(Timed.getFireCount()+" .:. "+sdf.format(cal.getTime()), 256, false);
			Station.repo.registerObject(so);
			//Metering m = new Metering(1);
	}
}
