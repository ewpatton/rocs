package edu.rpi.rocs.client.services.coursedb;

import java.util.List;

import com.google.gwt.user.client.rpc.AsyncCallback;

import edu.rpi.rocs.client.objectmodel.CourseDB;
import edu.rpi.rocs.client.objectmodel.SemesterDescription;

public interface CourseDBServiceAsync {
	public void getSemesterList( AsyncCallback<List<SemesterDescription>> callback);
	public void getSemesterData(Integer semesterId, AsyncCallback<CourseDB> callback);
	public void getCurrentSemester(AsyncCallback<SemesterDescription> callback); 
}