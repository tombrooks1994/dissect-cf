package iot.extension;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.DocumentBuilder;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.w3c.dom.Node;
import org.w3c.dom.Element;
import java.io.File;
import java.util.ArrayList;

public class ReadXML{

	public static ArrayList<Station> ReadFromXML(Cloud cloud,String xlmfile) throws Exception{
		ArrayList<Station> stations = new ArrayList<Station>();
		File fXmlFile = new File(xlmfile);
		DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
		DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
		Document doc = dBuilder.parse(fXmlFile);
		doc.getDocumentElement().normalize();

		NodeList nList = doc.getElementsByTagName("Station");
				
		for (int temp = 0; temp < nList.getLength(); temp++) {
			Node nNode = nList.item(temp);		
			if (nNode.getNodeType() == Node.ELEMENT_NODE) {
				Element eElement = (Element) nNode;
				stations.add(new Station(cloud,eElement.getAttribute("name")));
			}
		}
		return stations;
	}



}