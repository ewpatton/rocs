package edu.rpi.rocs.client.filters.schedule;

import com.google.gwt.user.client.ui.Widget;

import edu.rpi.rocs.client.objectmodel.Schedule;
import edu.rpi.rocs.client.objectmodel.ScheduleFilterManager;

public class MinCreditFilter implements ScheduleFilter {

	private static String DISPLAY_NAME="Minimum Credit Filter";
	private static String QUALIFIED_NAME="edu.rpi.rocs.client.filters.schedule.MinCreditFilter";
	
	public static boolean register() {
		ScheduleFilterManager.get().registerFilter(DISPLAY_NAME, QUALIFIED_NAME);
		return true;
	}

	/**
	 * The unique identifier for serialization
	 */
	private static final long serialVersionUID = -682106636533161302L;
	/**
	 * The credit specifier for this filter
	 */
	protected int mincreds = 0;
	
	/**
	 * Default filter is 0 credits minimum (could be higher)
	 */
	public MinCreditFilter() {
		mincreds = 0;
	}
	
	/**
	 * Sets a specific value for the minimum number of credits
	 * @param creds Minimum value for this filter
	 */
	public MinCreditFilter(int creds) {
		mincreds = creds;
	}
	
	/**
	 * Gets the credit threshold to successfully pass this filter
	 * @return The threshold value
	 */
	public int getThreshold() {
		return mincreds;
	}
	
	/**
	 * Sets the credit threshold for this filter
	 * @param creds The minimum number of credits needed to pass this filter
	 */
	public void setThreshold(int creds) {
		mincreds = creds;
	}
	
	/**
	 * Determines whether the schedule specified meets the criterion of this filter.
	 * @return true if the schedule has the minimum number of credits, otherwise false.
	 */
	public boolean doesScheduleSatisfyFilter(Schedule schedule) {
		// TODO Auto-generated method stub
		if(schedule.getMinCredits() >= mincreds) return true;
		return false;
	}

	/**
	 * Don't prune the tree for this filter since having a high minimum credit value
	 * can result in no schedules being generated.
	 */
	public boolean shouldPruneTreeOnFailure() {
		return false;
	}

	public Widget getWidget() {
		// TODO Auto-generated method stub
		return null;
	}

	public String getDisplayTitle() {
		// TODO Auto-generated method stub
		return DISPLAY_NAME;
	}
}
