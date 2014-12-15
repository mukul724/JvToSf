import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.CharacterData;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import com.opencsv.CSVWriter;

public class XMLDirectParse {

	public static String getCharacterDataFromElement(Element e) {
		Node child = e.getFirstChild();
		if (child instanceof CharacterData) {
			CharacterData cd = (CharacterData) child;
			return cd.getData();
		}
		return "";
	}

	public static void main(String[] args) throws ParserConfigurationException,
			SAXException, IOException {
		DocumentBuilderFactory DBF = DocumentBuilderFactory.newInstance();
		DocumentBuilder Doc = DBF.newDocumentBuilder();

		String csv = "C://work//CandidateData.csv"; // target CSV file
		CSVWriter writer = new CSVWriter(new FileWriter(csv)); // writer object
																// writer.write(string)

		
	//	CSVWriter writer = new CSVWriter(new FileWriter(csv), CSVWriter.DEFAULT_SEPARATOR, CSVWriter.NO_QUOTE_CHARACTER);
		
		String[] JV_Columns = CSVToString.getJVColumns();

		File CandidateFile = new File("C:/Work/CandRecordTest.xml");

		if (CandidateFile.exists()) {
			Document doc = Doc.parse(CandidateFile);
			Element docEle = doc.getDocumentElement();

			NodeList candidateList = docEle.getElementsByTagName("Candidate");
			writer.writeNext(CSVToString.getSFColumns()); // for writing the
															// column names in
															// the first row

			String[] tempCandidateRow = new String[JV_Columns.length];
			// NodeList[] tempList = new NodeList[JV_Columns.length];
			ArrayList<NodeList> tempList = new ArrayList<NodeList>();

			for (int i = 0; i < JV_Columns.length; i++) { // create the columns
															// data for each
															// candidate
				tempList.add((NodeList) docEle
						.getElementsByTagName(JV_Columns[i]));
				
			}

			// for creating string per candidate to be written into CSV file
			NodeList list=null;
			for (int i = 0; i < candidateList.getLength(); i++) {
				//NodeList list = (NodeList) tempList.get(i);
				tempCandidateRow[i] = new String();
				for (int j = 0; j < JV_Columns.length; j++) {
					list = (NodeList) tempList.get(j);
                  if(list.item(i)!=null)
					tempCandidateRow[j] = getCharacterDataFromElement((Element) list.item(i));
                  else
                	  tempCandidateRow[j] ="";  
				}
				
              writer.writeNext(tempCandidateRow);
			}

			writer.close(); // close the writer
		}
	}
}
