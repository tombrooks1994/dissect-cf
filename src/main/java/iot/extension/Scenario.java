package iot.extension;

import java.io.File;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.w3c.dom.Node;
import hu.mta.sztaki.lpds.cloud.simulator.Timed;
import hu.mta.sztaki.lpds.cloud.simulator.iaas.PhysicalMachine;
import hu.mta.sztaki.lpds.cloud.simulator.io.VirtualAppliance;

public class Scenario {
	/**
	 * Konstruktorral indithato szimulacio
	 * 
	 * @param va
	 *            VirtualApplience, ha null akkor a Cloud default VA-jat
	 *            hasznalja
	 * @param delay
	 *            Metering kesleltese
	 * @param filesize
	 *            Metering altal letrehozott fajl merete
	 * @param datafile
	 *            A Station-oket tartalmazo XML fajl helye a rendszerben
	 * @param actuators
	 *            Statikus aktuator betoltse, null, ha nincs
	 * @param VMnumber
	 *            a VM szama
	 * @param t
	 *            a szimulalt ido
	 * @param sensornumber
	 *            a Station szenzorainak a szama
	 * @throws Exception
	 */
	public Scenario(VirtualAppliance va, int filesize, String datafile) throws Exception {
		if (va == null) {
			 new Cloud(Cloud.v); // IaaS letrehozasa defaulta VA-val
		} else {
			new Cloud(va);
		}
		if (filesize <= 0) {
			System.out.println("Filesize nem lehet 0-nal kisebb");
			System.exit(0);
		}
		if (datafile.isEmpty()) {
			System.out.println("Datafile nem lehet null");
			System.exit(0);
		} else {
			File fXmlFile = new File(datafile);
			DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
			Document doc = dBuilder.parse(fXmlFile);
			doc.getDocumentElement().normalize();
			NodeList nList = doc.getElementsByTagName("Station");
			for (int temp = 0; temp < nList.getLength(); temp++) {
				Node nNode = nList.item(temp);
				if (nNode.getNodeType() == Node.ELEMENT_NODE) {
					Element eElement = (Element) nNode;
						
					final long freq = Long.parseLong(	eElement.getElementsByTagName("freq").item(0).getTextContent());
						if(freq<=0) {System.out.println("rossz freq ertek! ");System.exit(0);}
					final long time = Long.parseLong(	eElement.getElementsByTagName("time").item(0).getTextContent());
					if(time<=0) {System.out.println("rossz time ertek! ");System.exit(0);}
					final long starttime = Long.parseLong(	eElement.getElementsByTagName("starttime").item(0).getTextContent());
					if(starttime<-1 || starttime>=time) {System.out.println("rossz starttime ertek! ");System.exit(0);}
					final int snumber=Integer.parseInt(eElement.getElementsByTagName("snumber").item(0).getTextContent());
					if(snumber<=0) {System.out.println("rossz snumber ertek! ");System.exit(0);}
					final long maxinbw  = Long.parseLong(	eElement.getElementsByTagName("maxinbw").item(0).getTextContent());
					if(maxinbw<=0) {System.out.println("rossz maxinbw ertek! ");System.exit(0);}
					final long maxoutbw  = Long.parseLong(	eElement.getElementsByTagName("maxoutbw").item(0).getTextContent());
					if(maxoutbw<=0) {System.out.println("rossz maxoutbw ertek! ");System.exit(0);}
					final long diskbw  = Long.parseLong(	eElement.getElementsByTagName("diskbw").item(0).getTextContent());
					if(diskbw<=0) {System.out.println("rossz diskbw ertek! ");System.exit(0);}
					final long reposize  = Long.parseLong(	eElement.getElementsByTagName("reposize").item(0).getTextContent());
					if(reposize<=0) {System.out.println("rossz reposize ertek! ");System.exit(0);}
					
					Station.stations.add(new Station(
						eElement.getElementsByTagName("name").item(0).getTextContent(),
						freq,
						time,
						starttime,
						snumber,filesize,
						maxinbw,
						maxoutbw,
						diskbw,
						eElement.getElementsByTagName("torepo").item(0).getTextContent(),
						reposize
						));
				}
			}
		}
		
		Timed.simulateUntilLastEvent();
		// hasznos infok:
		System.out.println("~~~~~~~~~~~~");
		System.out.println(Cloud.iaas.repositories.toString());
		System.out.println("~~~~~~~~~~~~");
		for (PhysicalMachine p : Cloud.iaas.machines) {
			if (/*!p.isHostingVMs()*/ p.localDisk.getFreeStorageCapacity()!=p.localDisk.getMaxStorageCapacity()) {
				System.out.println(p);
			}
		}
		System.out.println("~~~~~~~~~~~~");
		for(Station s : Station.stations){
			System.out.println(s.toString());
		}		
	}
}