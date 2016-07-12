package iot.extension;

import java.util.ArrayList;
import java.util.EnumMap;
import java.util.HashMap;
import hu.mta.sztaki.lpds.cloud.simulator.io.*;
import hu.mta.sztaki.lpds.cloud.simulator.io.NetworkNode.NetworkException;
import hu.mta.sztaki.lpds.cloud.simulator.util.PowerTransitionGenerator;
import hu.mta.sztaki.lpds.cloud.simulator.*;
import hu.mta.sztaki.lpds.cloud.simulator.energy.powermodelling.PowerState;
import hu.mta.sztaki.lpds.cloud.simulator.iaas.PhysicalMachine;
import hu.mta.sztaki.lpds.cloud.simulator.iaas.resourcemodel.*;
import hu.mta.sztaki.lpds.cloud.simulator.iaas.resourcemodel.ResourceConsumption.ConsumptionEvent;

public class Station extends Timed {
	private Repository repo;
	private Repository torepo;
	private String repoID;
	private String toRepoID;
	private String name;
	private long time;
	private long starttime;
	private HashMap<String, Integer> lmap;
	private int lat;
	private int filesize;
	private int sensornumber;
	private PhysicalMachine pm;
	private boolean isWorking;
	private long reposize;
	public static ArrayList<Station> stations = new ArrayList<Station>();
	private static final double minpower = 20;
	private static final double idlepower = 200;
	private static final double maxpower = 300;
	private static final double diskDivider = 10;
	private static final double netDivider = 20;
	private static EnumMap<PhysicalMachine.PowerStateKind, EnumMap<PhysicalMachine.State, PowerState>> defaultTransitions;
	static {
		try {
			defaultTransitions = PowerTransitionGenerator.generateTransitions(minpower, idlepower, maxpower,
					diskDivider, netDivider);
		} catch (Exception e) {
			throw new IllegalStateException("Cannot initialize the default transitions");
		}
	}

	/**
	 * 
	 * @param name a station neve, egyedinek kell lennie : String
	 * @param freq a station frekvenciaja : Long
	 * @param time a teljes meres ideje : long
	 * @param starttime a meres kezdetenek az ideje: Long
	 * @param sensornumber a szenzorok szama : int
	 * @param filesize a meres altal generalt file merete : int
	 * @param maxinbw a repo savszelessege : Long
	 * @param maxoutbw a repo savszelessege : Long
	 * @param diskbw a repo savszelessege : Long
	 * @param torepo a celrepo : String
	 * @param reposize a station repo-janak a merete: Long
	 */
	public Station(String name, final long freq, long time,long starttime, int sensornumber, int filesize, long maxinbw, long maxoutbw,
			long diskbw, String torepo,long reposize) {
		this.reposize = reposize;
		this.starttime = starttime;		
		isWorking = starttime==-1 ? false : true;
		this.name = name;
		this.filesize = filesize;
		this.time = time;
		this.sensornumber = sensornumber;
		lmap = new HashMap<String, Integer>();
		lat = 11;
		toRepoID = torepo;
		repoID = this.name;
		lmap.put(repoID, lat);
		lmap.put(toRepoID, lat);
		repo = new Repository(this.reposize, repoID, maxinbw, maxoutbw, diskbw, lmap);
		pm = new PhysicalMachine(8, 0.00155875, 8000000000L, repo, 89000, 29000, defaultTransitions);
		this.torepo = this.findRepo(this.toRepoID);
		this.startMeter(freq);	
	}

	/*
	 * Getterek&Setterek
	 */
	public long getTime() {
		return time;
	}
	public boolean isWorking() {
		return isWorking;
	}
	public int getSensornumber() {
		return sensornumber;
	}
	public String getName() {
		return name;
	}
	public Repository getRepo() {
		return this.repo;
	}

	/**
	 *	Ha sikeresen megerkezett a celrepo-ba a SO, torli a Station repojabol
	 */
	private class StorObjEvent implements ConsumptionEvent {
		private String so;

		private StorObjEvent(String soid) {
			this.so = soid;
		}

		@Override
		public void conComplete() {
			repo.deregisterObject(this.so);

		}

		@Override
		public void conCancelled(ResourceConsumption problematic) {
			System.out.println("conCanelled meghivodott!");
		}
	}

	/**
	 * Elkuldi az object-eket a celreponak
	 * @param r a cel repo
	 * @throws NetworkException
	 */
	private void startCommunicate(Repository r) throws NetworkException {
		for (StorageObject so : repo.contents()) {
			StorObjEvent soe = new StorObjEvent(so.id);
			repo.requestContentDelivery(so.id, r, soe);
		}
	}

	/**
	 * Elinditja a Station mukodeset
	 * 
	 * @param interval
	 *            frekvencia
	 */
	private void startMeter(final long interval) {
		if (isWorking) {
			subscribe(interval);
		}			
	}

	/**
	 * leallitja a Station mukodeset
	 */
	private void stopMeter() {
		isWorking= false;
		unsubscribe();
	}

	/**
	 * megkeresi a cel repository-t az iaas felhoben
	 * 
	 * @param torepo
	 *            a cel repo azonositoja
	 * @return
	 */
	private Repository findRepo(String torepo) {
		Repository r = null;
		for (Repository tmp : Cloud.iaas.repositories) {
			if (tmp.getName().equals(torepo)) {
				r = tmp;
			} else {
				for (PhysicalMachine pm : Cloud.iaas.machines) {
					if (pm.localDisk.getName().equals(torepo)) {
						r = pm.localDisk;
					}
				}
			}
		}
		return r;
	}

	/**
	 * Ha letelt a Station mukodesenek ideje, leallitja azt Adatot gyujt &
	 * elkuldi a reponak
	 */
	@Override
	public void tick(long fires) {
		// a meres a megadott ideig tart csak
		if (Timed.getFireCount() < time  && Timed.getFireCount()>=starttime) {
			for (int i = 0; i < sensornumber; i++) {
				new Metering(this.name, i, filesize);
			}
		}
		// de a station mukodese addig amig az osszes SO el nem lett kuldve
		if (this.repo.getFreeStorageCapacity() == this.reposize && Timed.getFireCount() > time) {
			this.stopMeter();
		}
		// megkeresi a celrepo-t es elkuldeni annak
		try {
			if (this.torepo!= null) {
				this.startCommunicate(this.torepo);
			} else {
				System.out.println("Nincs kapcsolat a repo-k kozott!");
			}
		} catch (NetworkException e) {
			e.printStackTrace();
		}
	}

	/**
	 * toString metodus a Station lenyeges adatainak kiiratashoz,debugolashoz
	 */
	@Override
	public String toString() {
		return "Station [" + "Working:"+this.isWorking +" ,reposize:" + this.repo.getMaxStorageCapacity() + " ,toRepoID=" + toRepoID + ", name=" + name + ", time=" + time
				+ ", filesize=" + filesize + ", sensornumber=" + sensornumber + "]";
	}
	
}
