package selectcontract08;

/**
 * @author Karan Dahiya
 */
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.SortedSet;
import java.util.TreeSet;
import javax.swing.SpinnerModel;
import javax.swing.SpinnerNumberModel;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

/**
 *
 * @author Karan Dahiya
 */
public class ContractModel {

    private ArrayList<Contract> theContracts;
    private int contractCounter;
    private ArrayList<Contract> theContractsAll;
    private SortedSet<String> originCityList;

    // Constructor
    public ContractModel() {
        this.theContracts = new ArrayList<>();
        this.theContractsAll = new ArrayList<>(); // Initialize theContractsAll
        this.contractCounter = 0; // Start with the first contract
        this.originCityList = new TreeSet<>();
        
        //Lab 8 Additon, Changed the file format to xml from txt
        String fileName = System.getProperty("user.dir") + "\\src\\selectcontract08\\contracts.xml";
        loadTheContracts(fileName);

    }
    
    // Lab 8 addition, load the contracts to view window when program launched
    public void loadTheContracts(String fileName) {
        try {
            File file = new File(fileName);
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.parse(file);

            doc.getDocumentElement().normalize();
            NodeList nodeList = doc.getElementsByTagName("contract");

            for (int i = 0; i < nodeList.getLength(); i++) {
                Element element = (Element) nodeList.item(i);

                String contractID = element.getElementsByTagName("contractID").item(0).getTextContent();
                String originCity = element.getElementsByTagName("originCity").item(0).getTextContent();
                String destCity = element.getElementsByTagName("destCity").item(0).getTextContent();
                String orderItem = element.getElementsByTagName("orderItem").item(0).getTextContent();

                Contract contract = new Contract(contractID, originCity, destCity, orderItem);
                theContracts.add(contract);
                theContractsAll.add(contract); // Add to all contracts list

                originCityList.add(originCity);
            }

            originCityList.add("All"); // Add "All" to originCityList

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
    
    // Helper methods
    public boolean foundContracts() {
        return !theContracts.isEmpty();
    }

    public Contract getTheContract() {
        return theContracts.get(contractCounter);
    }

    public int getContractCount() {
        return theContracts.size();
    }

    public int getCurrentContractNum() {
        return contractCounter;
    }

    // Navigation methods
    public void nextContract() {
        if (contractCounter < theContracts.size() - 1) {
            contractCounter++;
        }
    }

    public void prevContract() {
        if (contractCounter > 0) {
            contractCounter--;
        }
    }

    // Setter for contractCounter (useful for testing or specific navigation)
    public void setContractCounter(int index) {
        if (index >= 0 && index < theContracts.size()) {
            contractCounter = index;
        }
    }

    public void updateContactList(String city) {
        theContracts = new ArrayList<>(theContractsAll);
        theContracts.clear(); // Clear current list

        if ("All".equals(city)) {
            // If "All" is selected, add all contracts to the list
            theContracts.addAll(theContractsAll);
        } else {
            theContracts.removeIf(s -> !s.contains(city));
            // Otherwise, add matching contracts based on selected city
            for (Contract contract : theContractsAll) {
                if (contract.getOriginCity().equals(city)) {
                    theContracts.add(contract);
                }
            }
        }

        contractCounter = 0;
    }

    public String[] getOriginCityList() {
        String[] a;
        a = originCityList.toArray(new String[originCityList.size()]);
        return a;
    }
    // Getter for allContracts if needed

    public ArrayList<Contract> getAllContracts() {
        return theContractsAll;
    }

    //Part -2 Lab6
    SpinnerModel model = new SpinnerNumberModel(100, 100, 10000, 50);

    public ArrayList<String> getAllContractIDs() {
        ArrayList<String> contractIDs = new ArrayList<>();
        for (Contract contract : theContractsAll) {
            contractIDs.add(contract.getContractID());
        }
        return contractIDs;
    }

    // Lab 7 the method which writes the content on the file
    void addContract(String contractID, String origin, String destination, String orderItem) throws IOException {

        String fileName = System.getProperty("user.dir") + "\\src\\selectcontract08\\contracts.xml";

        Contract newlyAdded = new Contract(contractID, origin, destination, orderItem);
        theContracts.add(newlyAdded);
        
        saveContractToXML(fileName, newlyAdded);
    }
    
    
    // Lab 8 addition saves the contracts to xml file when new contracts added from program 
   public void saveContractToXML(String fileName, Contract contract) {
    try {
        DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();

        File file = new File(fileName);
        Document doc;

        if (file.exists()) {
            // If file exists, parse it to update
            doc = dBuilder.parse(file);
        } else {
            // If file doesn't exist, create a new document
            doc = dBuilder.newDocument();
            Element rootElement = doc.createElement("contracts");
            doc.appendChild(rootElement);
        }

        // Add new contract element
        Element contractElement = doc.createElement("contract");

        // Create contractID element
        Element contractID = doc.createElement("contractID");
        contractID.appendChild(doc.createTextNode(contract.getContractID()));
        contractElement.appendChild(contractID);

        // Create originCity element
        Element originCity = doc.createElement("originCity");
        originCity.appendChild(doc.createTextNode(contract.getOriginCity()));
        contractElement.appendChild(originCity);

        // Create destCity element
        Element destCity = doc.createElement("destCity");
        destCity.appendChild(doc.createTextNode(contract.getDestCity()));
        contractElement.appendChild(destCity);

        // Create orderItem element
        Element orderItem = doc.createElement("orderItem");
        orderItem.appendChild(doc.createTextNode(contract.getOrderItem()));
        contractElement.appendChild(orderItem);

        // Append contract element to root element
        doc.getDocumentElement().appendChild(contractElement);

        // Write the updated XML document back to file with correct formatting
        TransformerFactory transformerFactory = TransformerFactory.newInstance();
        Transformer transformer = transformerFactory.newTransformer();
        transformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "no");
        transformer.setOutputProperty(OutputKeys.METHOD, "xml");
        transformer.setOutputProperty(OutputKeys.INDENT, "yes"); // Ensure indentation
        transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "2");

        // Use StringWriter to ensure no extra line breaks are introduced
        StringWriter writer = new StringWriter();
        transformer.transform(new DOMSource(doc), new StreamResult(writer));

        // Write to file
        FileWriter fileWriter = new FileWriter(fileName);
        fileWriter.write(writer.getBuffer().toString());
        fileWriter.close();

    } catch (Exception ex) {
        ex.printStackTrace();
    }
}
}