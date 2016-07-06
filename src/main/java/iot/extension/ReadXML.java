package iot.extension;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.DocumentBuilder;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.w3c.dom.Node;
import java.io.File;


public class ReadXML{

	public static void ReadFromXML(String xlmfile,int filesize) throws Exception{
		
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

				Station.stations.add(new Station(
						eElement.getElementsByTagName("name").item(0).getTextContent(),
					Long.parseLong(	eElement.getElementsByTagName("freq").item(0).getTextContent()),
					Long.parseLong(	eElement.getElementsByTagName("time").item(0).getTextContent()),
					Integer.parseInt(eElement.getElementsByTagName("snumber").item(0).getTextContent()),filesize,
					Long.parseLong(	eElement.getElementsByTagName("maxinbw").item(0).getTextContent()),
					Long.parseLong(	eElement.getElementsByTagName("maxoutbw").item(0).getTextContent()),
					Long.parseLong(	eElement.getElementsByTagName("diskbw").item(0).getTextContent()),
					eElement.getElementsByTagName("torepo").item(0).getTextContent()
						));
			}
		}
		
		
			
	}



}