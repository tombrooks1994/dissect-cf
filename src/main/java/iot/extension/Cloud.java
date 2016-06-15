package iot.extension;

import hu.mta.sztaki.lpds.cloud.simulator.util.*;
import java.io.IOException;
import javax.xml.parsers.ParserConfigurationException;
import org.xml.sax.SAXException;
import hu.mta.sztaki.lpds.cloud.simulator.iaas.*;
import hu.mta.sztaki.lpds.cloud.simulator.io.VirtualAppliance;

public class Cloud {
	public IaaSService is;
	private VirtualAppliance va;
	public static VirtualAppliance v = new VirtualAppliance("BaseVA", 1000, 0, false, 1000000000); // default static VA to reach from everywhere

	public VirtualAppliance getVa() {
		return va;
	}

	public Cloud(VirtualAppliance va) throws IOException, SAXException, ParserConfigurationException {
		this.va = va;
		// String tmp =
		// "d:\\szakdoga\\dissect-cf-master\\src\\main\\java\\iot\\extension\\LPDSCloud.xml";
		String tmp = "d:\\Dokumentumok\\SZTE\\szakdoga\\dissect-cf-andrasmarkus-patch-1\\src\\main\\java\\iot\\extension\\LPDSCloud.xml";
		is = CloudLoader.loadNodes(tmp);
		is.machines.get(0).localDisk.registerObject(va);
	}
}
