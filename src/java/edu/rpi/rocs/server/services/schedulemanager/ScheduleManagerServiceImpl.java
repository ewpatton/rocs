package edu.rpi.rocs.server.services.schedulemanager;

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;

import edu.rpi.rocs.client.objectmodel.Schedule;

public class ScheduleManagerServiceImpl extends RemoteServiceServlet implements
		edu.rpi.rocs.client.services.schedulemanager.ScheduleManagerService {

	/**
	 * 
	 */
	private static final long serialVersionUID = 943641426558410281L;
	
	public void saveSchedule(String name, Schedule schedule) {
		
	}

	public List<String> getScheduleList() {
		// TODO Auto-generated method stub
		ArrayList<String> temp = new ArrayList<String>();
		temp.add("pattoe-fall09-01");
		temp.add("pattoe-fall09-02");
		temp.add("pattoe-fall09-03");
		temp.add("pattoe-spring09-01");
		return temp;
	}

}