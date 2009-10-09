package edu.rpi.rocs.client.filters.schedule;

import java.util.ArrayList;
import java.util.HashMap;

import com.google.gwt.user.client.ui.Widget;

import edu.rpi.rocs.client.objectmodel.Schedule;
import edu.rpi.rocs.client.objectmodel.Time;
import edu.rpi.rocs.client.ui.filters.ScheduleTimeBlockFilterWidget;

public class TimeSchedulerFilter implements ScheduleFilter {

	/**
	 * 
	 */
	private static final long serialVersionUID = -7735119145993404979L;
	private ScheduleTimeBlockFilterWidget widget=null;
	
	public HashMap<Integer, ArrayList<Time>> getTimes() {
		return widget.getBlockedTimes();
	}
	
	public boolean doesScheduleSatisfyFilter(Schedule schedule) {
		// TODO Auto-generated method stub
		return false;
	}

	public boolean shouldPruneTreeOnFailure() {
		// TODO Auto-generated method stub
		return true;
	}

	public Widget getWidget() {
		// TODO Auto-generated method stub
		if(widget==null) widget = new ScheduleTimeBlockFilterWidget();
		return widget;
	}

	public String getDisplayTitle() {
		// TODO Auto-generated method stub
		return "Disallowed Times Filter";
	}

}