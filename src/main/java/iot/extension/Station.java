package iot.extension;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import hu.mta.sztaki.lpds.cloud.simulator.io.*;
import hu.mta.sztaki.lpds.cloud.simulator.*;

public class Station extends DeferredEvent {

	List<StorageObject> so;
	static Repository repo;
	HashMap<String, Integer> lmap;
	int lat;
	String repoID;

	public Station(long delay) {
		super(delay);
		so = new ArrayList<StorageObject>();
		lmap = new HashMap<String, Integer>();
		lat = 11;
		repoID = "StationRepo";
		lmap.put(repoID, lat);
		repo = new Repository(600000000L, repoID, 100000L, 100000L, 100000L, lmap);
	}

	@Override
	protected void eventAction() {
		
		for(int i=0;i<1000;i++){
			Calendar cal = Calendar.getInstance();
			SimpleDateFormat sdf = new SimpleDateFormat("yy:MM:dd:HH:mm:ss:SSSS");
			StorageObject so = new StorageObject(sdf.format(cal.getTime()), 256, false);
			repo.registerObject(so);
		
			//System.out.println(so.toString() + "  .:. "+ repo.toString());
		}
		
		
		

	}
	
	
	public static void main(String[] args){
		Station s = new Station(1);
		Timed.simulateUntilLastEvent();
		
	}

}
