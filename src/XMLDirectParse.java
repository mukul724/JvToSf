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

	
	public static String filterPhoneNumber(String data){

		switch (data.length()) {

		case 10:
			return data;
			
		case 11:
			data=(data.charAt(0)=='0')?data.substring(1):"9999999999";
			 
			return data;
		
		case 12:
		
			data=(data.substring(0,1)=="91")?data.substring(2):"9999999999";
				 
			return data;
		
		case 13:
			data=(data.substring(0,2)=="+91")?data.substring(3):"9999999999";
			
			return data;

		default:
			return "9999999999";

		}
			
	}
	
	
	public static void main(String[] args) throws ParserConfigurationException,
			SAXException, IOException {
		DocumentBuilderFactory DBF = DocumentBuilderFactory.newInstance();
		DocumentBuilder Doc = DBF.newDocumentBuilder();

		//String inputFileName = "C:/USERS/MUKUL.KUMAR/PICTURES/UPLOADS/UNZIPPED/FLIPKARTINDIAPVTLTD_10012014-12312014_12162014233658/CANDIDATES/CANDIDATERECORDSA_";
		//String outputFileName = "C://work//Candidate_Data_Output//CandidateRecordsA_";
		String inputFileName ="C:/Users/mukul.kumar/Desktop/sample data/CandidateRecordsA_";
		String outputFileName ="C:/Users/mukul.kumar/Desktop/sample data/OutputCandidateRecordsA_";
		int candidateCount = 0;
		int inputFileIndex = 0, outputFileIndex = 0;
		int NO_OF_CANDIDATE_FILES = 1;
		int MAX_CANDIDATES_PER_SHEET = 50000;
		CSVWriter writer = null;
		for (; inputFileIndex < NO_OF_CANDIDATE_FILES; inputFileIndex++) {
			String[] JV_Columns = CSVToString.getJVColumns();

			File CandidateFile = new File(inputFileName + inputFileIndex
					+ ".xml"); // input candidate xml file

			if (CandidateFile.exists()) {
				Document doc = Doc.parse(CandidateFile);
				Element docEle = doc.getDocumentElement();

				NodeList candidateList = docEle
						.getElementsByTagName("Candidate");
				String[] tempCandidateRow = new String[JV_Columns.length];
				// NodeList[] tempList = new NodeList[JV_Columns.length];
				ArrayList<NodeList> tempList = new ArrayList<NodeList>();

				for (int i = 0; i < JV_Columns.length; i++) { // create the
																// columns
																// data for each
																// candidate
					tempList.add((NodeList) docEle
							.getElementsByTagName(JV_Columns[i]));

				}

				// for creating string per candidate to be written into CSV file
				NodeList list = null;
				for (int i = 0; i < candidateList.getLength(); i++) {
					// NodeList list = (NodeList) tempList.get(i);
					for (int k = 0; k < tempCandidateRow.length; k++)
						tempCandidateRow[k] = new String();
					for (int j = 0; j < JV_Columns.length; j++) {
						list = (NodeList) tempList.get(j);
						if (list.item(i) != null)
							tempCandidateRow[j] = getCharacterDataFromElement((Element) list
									.item(i));
						else
							tempCandidateRow[j] = "";
					}

					if (candidateCount++ % MAX_CANDIDATES_PER_SHEET == 0) { // creating
																			// new
																			// files
																			// on
																			// the
																			// basis
																			// of
																			// max
																			// candidates
																			// per
																			// file
						if (writer != null)
							writer.close();
						String csv = outputFileName + (outputFileIndex++)
								+ ".csv";
						writer = new CSVWriter(new FileWriter(csv)); // writer
																		// object
																		// writer.write(string)
						writer.writeNext(CSVToString.getSFColumns());
					}
                 if(tempCandidateRow[0]!=""&&tempCandidateRow[0]!=null){
					
                	 //2-fname, 4-lname, 5-cellphone,6-homephone, 9-city,13-dob, 15-ssn, 16-disability status,
                	 // 17-preferred location, 18-contact other position
                	 //19- comment,20 - commentator
                	 
                	 if(tempCandidateRow[2]==""||tempCandidateRow[2]==null) {
                		 tempCandidateRow[2]="NA";
                	 }
                	 
                	 if(tempCandidateRow[4]==""||tempCandidateRow[4]==null) {
                		 tempCandidateRow[4]="NA";
                	 }
                	 if(tempCandidateRow[5]==""||tempCandidateRow[5]==null) {
                		 tempCandidateRow[5]=(tempCandidateRow[6]==""||tempCandidateRow[6]==null)?"9999999999":tempCandidateRow[6];
                		 //"NA"; //9999999999
                	 }
                	 if(tempCandidateRow[6]==""||tempCandidateRow[6]==null) {
                		 tempCandidateRow[6]=tempCandidateRow[5];  
                	 }
                	 
                	 tempCandidateRow[5]= tempCandidateRow[5].replaceAll("[^0-9]", "");
                	 tempCandidateRow[6]= tempCandidateRow[6].replaceAll("[^0-9]", "");
                	 tempCandidateRow[5]= filterPhoneNumber(tempCandidateRow[5]); 
                	 tempCandidateRow[6]= filterPhoneNumber(tempCandidateRow[6]);
 
                	 if(tempCandidateRow[9]==""||tempCandidateRow[9]==null) { 
                		 tempCandidateRow[9]="NA";
                	 }
                	 

                	// if(tempCandidateRow[12]==""||tempCandidateRow[12]==null) {
                		 tempCandidateRow[12]="IN";
                	
                	 
                	 if(tempCandidateRow[13]==""||tempCandidateRow[13]==null) {
                		 tempCandidateRow[13]="2015-02-02T00:00:00";
                	 }
                	 if(tempCandidateRow[15]==""||tempCandidateRow[15]==null) {
                		 tempCandidateRow[15]="NA";
                	 }
                	 if(tempCandidateRow[16]==""||tempCandidateRow[16]==null) {
                		 tempCandidateRow[16]="No";
                	 }
                	 if(tempCandidateRow[17]==""||tempCandidateRow[17]==null) {
                		 tempCandidateRow[17]="Bengaluru";
                	 }
                	 if(tempCandidateRow[18]==""||tempCandidateRow[18]==null) {
                		 tempCandidateRow[18]="Yes";
                	 }
                	 if(tempCandidateRow[19]==""||tempCandidateRow[19]==null) {
                		 tempCandidateRow[19]="This is JobVite Data";
                	 }
                	 if(tempCandidateRow[20]==""||tempCandidateRow[20]==null) {
                		 tempCandidateRow[20]="recruit.admin";
                	 }
                	 writer.writeNext(tempCandidateRow);
					}
				}
			}
		}
		writer.close(); // close the writer
	}
}

