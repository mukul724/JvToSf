import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import com.opencsv.CSVWriter;

public class FilterDataForJV {

	public static void main(String[] args) throws ParserConfigurationException,
			SAXException, IOException {
		DocumentBuilderFactory DBF = DocumentBuilderFactory.newInstance();
		DocumentBuilder Doc = DBF.newDocumentBuilder();
		ArrayList<String> myJVColums = new ArrayList<String>();
		Scanner in = new Scanner(System.in);
		String NextLineData = "x";

		String csv = "C://work//CandidateRecordsAll_Columns_0.csv"; // target CSV
																	// file
		CSVWriter writer = new CSVWriter(new FileWriter(csv)); // writer object
																// writer.write(string)

		while (!NextLineData.equals("") && !NextLineData.equals("\n")) {
			NextLineData = in.nextLine().trim();
			myJVColums.add(NextLineData);
		}

		 for(String x: myJVColums){ System.out.println(x); }
		 
		String[] JVAllColumns = new String[myJVColums.size()];

		for (int i = 0; i < myJVColums.size(); i++) {
			JVAllColumns[i] = myJVColums.get(i);
		}

		writer.writeNext(JVAllColumns); // adding the Header names Row
		in.close();

		
		 // for(String x: myJVColums){ System.out.println(x); }
		 

		File CandidateFile = new File(
				"C:/Users/mukul.kumar/Pictures/uploads/unzipped/FlipkartIndiaPvtLtd_10012014-12312014_12162014233658/Candidates/CandidateRecordsA_0.xml");

		if (CandidateFile.exists()) {
			Document doc = Doc.parse(CandidateFile);
			Element docEle = doc.getDocumentElement();
			NodeList candidateList = docEle.getElementsByTagName("Candidate");
			ArrayList<NodeList> tempList = new ArrayList<NodeList>(); //getting headers from console

			for (int i = 0; i < JVAllColumns.length; i++) { // create the
															// columns
				tempList.add((NodeList) docEle
						.getElementsByTagName(JVAllColumns[i]));
			}

			String tempCandidateRow[] = new String[JVAllColumns.length];
			
			NodeList list = null;
			
			for (int i = 0; i < candidateList.getLength(); i++) {

				for (int k = 0; k < tempCandidateRow.length; k++)
					tempCandidateRow[k] = new String();
				for (int j = 0; j < JVAllColumns.length; j++) {
					list = (NodeList) tempList.get(j);
					if (list.item(i) != null)
						tempCandidateRow[j] = XMLDirectParse
								.getCharacterDataFromElement((Element) list
										.item(i));
					else
						tempCandidateRow[j] = "";
				}

				writer.writeNext(tempCandidateRow);

			}

		}

		writer.close();
	}

}
