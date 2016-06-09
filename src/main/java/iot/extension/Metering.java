package iot.extension;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import hu.mta.sztaki.lpds.cloud.simulator.io.*;
import hu.mta.sztaki.lpds.cloud.simulator.*;

public class Metering extends DeferredEvent {
	int b;

	public Metering(long delay, int b) {
		super(delay);
		this.b = b;
	}

	@Override
	protected void eventAction() {
		Calendar cal = Calendar.getInstance();
		SimpleDateFormat sdf = new SimpleDateFormat("yy:MM:dd:HH:mm:ss:SS");
		StorageObject so = new StorageObject(Timed.getFireCount() + " .:. " + sdf.format(cal.getTime()), b, false);
		Station.getRepo().registerObject(so);
	}
}
