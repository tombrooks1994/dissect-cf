package iot.extension;

import hu.mta.sztaki.lpds.cloud.simulator.util.*;
import java.io.IOException;
import java.util.concurrent.atomic.AtomicStampedReference;

import javax.xml.parsers.ParserConfigurationException;
import org.xml.sax.SAXException;
import hu.mta.sztaki.lpds.cloud.simulator.iaas.*;

public class Cloud {
	IaaSService is;

	public Cloud() throws IOException, SAXException, ParserConfigurationException {
		String tmp = "c:\\szakdoga\\dissect-cf-master\\src\\main\\java\\iot\\extension\\LPDSCloud.xml";
		is = CloudLoader.loadNodes(tmp);
	}
}
