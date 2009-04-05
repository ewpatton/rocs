package edu.rpi.rocs.client.objectmodel;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import edu.rpi.rocs.client.objectmodel.CourseDB;
import edu.rpi.rocs.client.objectmodel.CrossListing;

public class CourseDB {
//coursedb is an object and return an instance of it
    
    //class variables
    protected Integer timestamp;
    protected Integer semesterId;
    protected String semesterdesc;
    protected HashMap<String, Course> courses;
    protected HashMap<Integer, CrossListing> crosslistings;
    protected int counter;
    
    //accessor functions
    public CourseDB(int aTimeStamp, int aSemesterNumber, String aSemesterDesc){
    	counter = 0;
        timestamp = aTimeStamp;
        semesterId = aSemesterNumber;
        semesterdesc = aSemesterDesc;
        courses = new HashMap<String, Course>();
        crosslistings = new HashMap<Integer, CrossListing>();
    }
    
    public int getTimeStamp(){
        return timestamp;
    }
    
    public int getSemesterId(){
        return semesterId;
    }
    
    public String getSemesterDesc(){
        return semesterdesc;
    }
    
    public List<Course> getCourses() {
    	return new ArrayList<Course>(courses.values());
    }
    
    public List<CrossListing> getCrossListings() {
    	return new ArrayList<CrossListing>(crosslistings.values());
    }
}
