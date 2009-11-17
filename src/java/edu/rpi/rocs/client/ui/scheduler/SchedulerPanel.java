package edu.rpi.rocs.client.ui.scheduler;

import java.util.ArrayList;

import com.google.gwt.user.client.ui.Panel;
import com.google.gwt.user.client.ui.SimplePanel;

import edu.rpi.rocs.client.objectmodel.Schedule;
import edu.rpi.rocs.client.objectmodel.ScheduleFilterManager;
import edu.rpi.rocs.client.objectmodel.SchedulerManager;

public class SchedulerPanel extends SimplePanel {
	private static SchedulerPanel theInstance=null;
	private Panel currentChild=null;
	
	public enum SchedulerPage {
		FilterPage,
		SchedulePage
	}
	
	private SchedulerPanel() {
		this.setHeight("100%");
		currentChild = new SchedulerIntroPanel();
		this.add(currentChild);
	}
	
	public static SchedulerPanel get() {
		if(theInstance==null) theInstance = new SchedulerPanel();
		return theInstance;
	}
	
	public void switchTo(SchedulerPage page) {
		switch(page) {
		case FilterPage:
			this.remove(currentChild);
			currentChild = SchedulerFilterDisplayPanel.get();
			this.add(currentChild);
			break;
		case SchedulePage:
			if(ScheduleFilterManager.get().filtersChanged()) {
				SchedulerManager.get().generateSchedules();
				ArrayList<Schedule> schedules = SchedulerManager.get().getAllSchedules();
				if(schedules==null || schedules.size()==0) {
					return;
				}
				this.remove(currentChild);
				currentChild = SchedulerDisplayPanel.get();
				SchedulerDisplayPanel.get().setSchedules(schedules);
				this.add(currentChild);
				break;
			}
			else {
				this.remove(currentChild);
				currentChild = SchedulerDisplayPanel.get();
				this.add(currentChild);
			}
		}
	}
}
