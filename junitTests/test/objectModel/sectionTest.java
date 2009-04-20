//-S.Whitney

package test.objectModel;

import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.junit.Before;
import org.junit.Test;
import org.w3c.dom.Document;
import org.w3c.dom.Node;

import edu.rpi.rocs.server.objectmodel.SectionImpl;
import junit.framework.TestCase;

public class sectionTest extends TestCase{

	ArrayList<SectionImpl> testObject = new ArrayList<SectionImpl>();
	
	String xmlLoc = "http://pattoe.stu.rpi.edu/rocs-portlet/sample.xml";
	
	@Before
	public void setUp(){
		try{
			URL xml = new URL(xmlLoc);
			InputStream stream = xml.openStream();
			
			DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
	    	DocumentBuilder db = dbf.newDocumentBuilder();
	    	Document doc = db.parse(stream);
	    	Node root = doc.getDocumentElement();
	    	if(root.getNodeName() == "CourseDB") {
	    		for(Node c = root.getFirstChild(); c.getNextSibling() != null; c = c.getNextSibling()) {
	    			if(c.getNodeName() == "Course" && c.hasChildNodes()){
						for(Node s = c.getFirstChild(); s.getNextSibling() != null; s = s.getNextSibling()) {
							if(s.getNodeName() == "Section") {
								SectionImpl n = new SectionImpl(s);
								testObject.add(n);
							}//end if crosslisting
						}//end for section children nodes
	    			}//end if course
	    		}//end for course nodes
			}//end if coursedb
	    	else{fail("invalid XML file provided"); }
		} catch (Exception e) {
			e.printStackTrace();
		}
	}//end setup

	@Test
	public void testExistance(){
		assertFalse(testObject.size() == 0);
	}
	
	@Test
	public void testCRN(){
		assertTrue(testObject.get(0).getCRN() == 44307);
		assertTrue(testObject.get(1).getCRN() == 42571);
		
		testObject.get(0).setCRN(12345);
		assertTrue(testObject.get(0).getCRN() == 12345);
	}//end crn tests
	

	@Test
	public void testNum(){
		assertTrue(testObject.get(0).getNumber() == 0);
		assertTrue(testObject.get(1).getNumber() == 1);
		
		testObject.get(0).setNumber(123);
		assertTrue((testObject.get(0).getNumber()) == 123);
	}//end num tests
	

	@Test
	public void testStudents(){
		assertTrue(testObject.get(0).getStudents() == 0);
		assertTrue(testObject.get(1).getStudents() == 6);
		
		testObject.get(0).setStudents(456);
		assertTrue((testObject.get(0).getStudents()) == 456);	
	}//end students tests
	

	@Test
	public void testSeats(){
		assertTrue(testObject.get(0).getSeats() == 0);
		assertTrue(testObject.get(1).getSeats() == 30);
		
		testObject.get(0).setSeats(789);
		assertTrue((testObject.get(0).getSeats()) == 789);	
	}//end seats tests
	

	@Test
	public void testClosed(){
		assertTrue(testObject.get(0).getClosed() == true);
		assertTrue(testObject.get(1).getClosed() == false);
		
		testObject.get(0).setClosed(true);
		assertTrue(testObject.get(0).getClosed() == true);
	}

	
}//end sectionTests