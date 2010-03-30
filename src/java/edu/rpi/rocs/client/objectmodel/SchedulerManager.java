package edu.rpi.rocs.client.objectmodel;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

import com.allen_sauer.gwt.log.client.Log;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.rpc.IsSerializable;

import edu.rpi.rocs.client.filters.schedule.ScheduleFilter;
import edu.rpi.rocs.client.objectmodel.SemesterManager.SemesterManagerCallback;
import edu.rpi.rocs.client.services.schedulemanager.ScheduleManagerService;

/**
 * The Schedule Manager is responsible for keeping track of the current state of the scheduler on the
 * client side and for interfacing with the ScheduleManagerService to update the server and store
 * and retrieve schedules from the SQL database. 
 * 
 * @author ewpatton
 *
 */
public class SchedulerManager implements IsSerializable {

	/**
	 * The unique identifier for serialization
	 */
	private transient static final long serialVersionUID = 2193647582216874345L;
	/**
	 * Schedules generated by the scheduler
	 */
	private transient ArrayList<Schedule> generatedSchedules=null;
	/**
	 * True if the courses have changed
	 */
	private transient boolean m_changed = false;
	/**
	 * The schedule currently selected by the user
	 */
	private Schedule currentSchedule=null;
	
	private class LocalSemesterManagerCallback implements SemesterManagerCallback {
		public CrossListing cl;
		public Course c;
		public Section s;
		public LocalSemesterManagerCallback(CrossListing cl, Course c, Section s) {
			this.cl = cl; this.c = c; this.s = s;
		}
		public void didChangeCourse(Course c) {
			mgrCallback.didChangeCourse(c);
		}
		public void didChangeCrosslisting(CrossListing cl) {
			mgrCallback.didChangeCrosslisting(cl);
		}
		public void didChangeSection(Section s) {
			mgrCallback.didChangeSection(s);
		}
		public void semesterLoaded(Semester semester) {
			mgrCallback.semesterLoaded(semester);
		}
		public void semesterUpdated(Semester semester) {
			mgrCallback.semesterUpdated(semester);
		}
	}
	
	private transient SemesterManagerCallback mgrCallback = new SemesterManagerCallback() {

		public void didChangeCourse(Course c) {
			
		}

		public void didChangeCrosslisting(CrossListing cl) {
			
		}

		public void didChangeSection(Section s) {
			if(s.isClosed()) {
				Window.alert("Section "+s.getNumber()+" of course "+s.getParent().getName()+" has closed. Please revise your schedule.");
			}
		}

		public void semesterLoaded(Semester semester) {
			
		}

		public void semesterUpdated(Semester semester) {
			
		}
	};
	
	private transient ArrayList<LocalSemesterManagerCallback> changeEventCallbacks = new ArrayList<LocalSemesterManagerCallback>();

	
	/**
	 * The current set of courses for this session.
	 */
	private HashMap<Course, CourseStatusObject> currentCourses = new HashMap<Course, CourseStatusObject>();
	
	/**
	 * The ScheduleManager instance
	 */
	private transient static SchedulerManager theInstance=null;
	
	/**
	 * Listeners for when a course is added
	 */
	
	private transient HashSet<CourseAddedHandler> courseAddHandlers=new HashSet<CourseAddedHandler>();
	private transient HashSet<CourseRemovedHandler> courseRemoveHandlers=new HashSet<CourseRemovedHandler>();
	private transient HashSet<CourseRequiredHandler> courseRequiredHandlers=new HashSet<CourseRequiredHandler>();
	private transient HashSet<CourseOptionalHandler> courseOptionalHandlers=new HashSet<CourseOptionalHandler>();
	
	/**
	 * Hidden for singleton management
	 */
	public SchedulerManager() {
	}
	
	public List<CourseStatusObject> getSelectedCourses() {
		return new ArrayList<CourseStatusObject>(currentCourses.values());
	}
	
	public void addCourseAddedEventHandler(CourseAddedHandler e) {
		courseAddHandlers.add(e);
	}
	
	public void removeCourseAddedEventHandler(CourseAddedHandler e) {
		courseAddHandlers.remove(e);
	}
	
	public void addCourseRemovedEventHandler(CourseRemovedHandler e) {
		courseRemoveHandlers.add(e);
	}
	
	public void removeCourseRemovedEventHandler(CourseRemovedHandler e) {
		courseRemoveHandlers.remove(e);
	}
	
	public void addCourseRequiredEventHandler(CourseRequiredHandler e) {
		courseRequiredHandlers.add(e);
	}
	
	public void removeCourseRequiredEventHandler(CourseRequiredHandler e) {
		courseRequiredHandlers.remove(e);
	}
	
	public void addCourseOptionalEventHandler(CourseOptionalHandler e) {
		courseOptionalHandlers.add(e);
	}
	
	public void removeCourseOptionalEventHandler(CourseOptionalHandler e) {
		courseOptionalHandlers.remove(e);
	}
	
	/**
	 * Gets the singleton ScheduleManager instance
	 * @return The ScheduleManager
	 */
	public static SchedulerManager getInstance() {
		if(theInstance==null) theInstance = new SchedulerManager();
		if(SemesterManager.getInstance().getCurrentSemester()!=null)
			theInstance.setSemesterId(SemesterManager.getInstance().getCurrentSemester().getSemesterId());
		return theInstance;
	}
	
	/**
	 * Gets the list of saved schedules from the server.
	 * @param callback The AsyncCallback to call after the list has been retrieved
	 */
	
	public void getScheduleList(AsyncCallback<List<String>> callback) {
		ScheduleManagerService.Singleton.getInstance().getScheduleList(User.getUserID(), callback);
	}
	
	public interface CourseModificationHandler extends EventHandler {
		public void handleEvent(CourseStatusObject status);
	}
	
	public interface CourseAddedHandler extends CourseModificationHandler {
	}
	
	public interface CourseRemovedHandler extends CourseModificationHandler {
	}
	
	public interface CourseRequiredHandler extends CourseModificationHandler {
	}
	
	public interface CourseOptionalHandler extends CourseModificationHandler {
	}
	
	/**
	 * Adds a course to the current course listing
	 * @param c The course to add
	 */
	public void addCourse(Course c) {
		CourseStatusObject status =new CourseStatusObject(c, true); 
		currentCourses.put(c, status);
		m_changed = true;
		
		for(CourseAddedHandler e : courseAddHandlers) {
			e.handleEvent(status);
		}
		
	}
	
	/**
	 * Sets  a course to be required to appear for a valid schedule
	 * @param c The course to make required
	 */
	public void setCourseRequired(Course c) {
		m_changed = true;
		CourseStatusObject obj = currentCourses.get(c);
		if(obj!=null) {
			if (!obj.getRequired()) CourseSearchPanel.getInstance().addFS(c);
			obj.setRequired(true);
			
			for(CourseRequiredHandler e : courseRequiredHandlers) {
				e.handleEvent(obj);
			}
			
		}
	}
	
	/**
	 * Sets a course to be optional to appear for a valid schedule
	 * @param c The course to make optional
	 */
	public void setCourseOptional(Course c) {
		m_changed = true;
		CourseStatusObject obj = currentCourses.get(c);
		if(obj!=null) {
			obj.setRequired(false);
			
			for(CourseOptionalHandler e : courseOptionalHandlers) {
				e.handleEvent(obj);
			}
			
		}
	}
	
	/**
	 * Returns whether a course is required for a schedule
	 * @param c The course to check
	 * @return Whether the course is required or not
	 */
	public boolean isCourseRequired(Course c) {
		CourseStatusObject obj = currentCourses.get(c);
		if(obj!=null) {
			return obj.getRequired();
		}
		return false;
	}
	
	/**
	 * Removes a course from the user's list of courses
	 * @param c The course to remove
	 */
	public void removeCourse(Course c) {
		m_changed = true;
		CourseStatusObject status = currentCourses.get(c);
		currentCourses.remove(c);
		
		for(CourseRemovedHandler e : courseRemoveHandlers) {
			e.handleEvent(status);
		}
		
	}
	
	/**
	 * Generates a new schedule session and clears any state information from the last session
	 */
	public void newSchedule() {
		currentCourses.clear();
		currentSchedule=null;
		generatedSchedules=null;
	}
	
	/**
	 * Returns the current Schedule selected by the user.
	 * @return The current schedule being displayed by the scheduler
	 */
	public Schedule getCurrentSchedule() {
		return currentSchedule;
	}
	
	/**
	 * Returns all of the valid schedules computed by the scheduler.
	 * @return An ArrayList of valid schedules.
	 */
	public ArrayList<Schedule> getAllSchedules() {
		return generatedSchedules;
	}
	
	/**
	 * Saves the current Schedule to the server.
	 * @param name The name to give the schedule for later reference.
	 */
	public void saveCurrentSchedule(String name) {
		ScheduleManagerService.Singleton.getInstance().saveSchedule(name, this, new AsyncCallback<Void>() {

			public void onFailure(Throwable caught) {
				// TODO Auto-generated method stub
				
			}

			public void onSuccess(Void result) {
				// TODO Auto-generated method stub
				
			}
			
		});
	}

	public void generateSchedules() {
		// TODO Auto-generated method stub
		ArrayList<Course> requiredCourses = new ArrayList<Course>();
		ArrayList<Course> optionalCourses = new ArrayList<Course>();
		for(CourseStatusObject status : currentCourses.values()) {
			if(status.getRequired()) {
				requiredCourses.add(status.getCourse());
			}
			else {
				optionalCourses.add(status.getCourse());
			}
		}
		
		HashSet<ScheduleFilter> filters = ScheduleFilterManager.getInstance().getFilters();
		generatedSchedules = Schedule.buildAllSchedulesGivenCoursesAndFilters(requiredCourses, optionalCourses, filters);
		if(generatedSchedules != null)
			Log.trace("Generated " + generatedSchedules.size() + " schedules");
		if(generatedSchedules != null && generatedSchedules.size()>0) {
			currentSchedule = generatedSchedules.get(0);
			currentSchedule.setOwner(getUserId());
		}
		m_changed = false;
		
	}
	
	public boolean hasChanged() { return m_changed; }
	public void setCurrentCourses(Map<Course, CourseStatusObject> map) {
		currentCourses = new HashMap<Course, CourseStatusObject>(map);
	}
	public Map<Course, CourseStatusObject> getCurrentCourses() {
		return new HashMap<Course, CourseStatusObject>(currentCourses);
	}
	
	private String m_uid=null;
	public String getUserId() {
		if(m_uid==null) m_uid = User.getUserID();
		return m_uid;
	}
	
	public void setUserId(String uid) {
		m_uid = uid;
	}
	
	public void setCurrentSchedule(Schedule s) {
		currentSchedule = s;
		if(GWT.isClient()) setChangeHandlers();
	}
	
	private void setChangeHandlers() {
		for(LocalSemesterManagerCallback h : changeEventCallbacks) {
			if(h.cl!=null)
				SemesterManager.getInstance().removeCrossListChangeHandler(h.cl, h);
			if(h.c!=null)
				SemesterManager.getInstance().removeCourseChangeHandler(h.c, h);
			if(h.s!=null)
				SemesterManager.getInstance().removeSectionChangeHandler(h.s, h);
		}
		changeEventCallbacks.clear();
		if(currentSchedule!=null) {
			for(Section s : currentSchedule.getSections()) {
				LocalSemesterManagerCallback h = new LocalSemesterManagerCallback(null, null, s);
				SemesterManager.getInstance().addSectionChangeHandler(s, h);
				changeEventCallbacks.add(h);
			}
		}
	}

	private Long dbid=null;
	public Long getDbid() {
		return dbid;
	}
	public void setDbid(Long id) {
		dbid = id;
	}
	
	private String name="";
	public void setName(String n) {
		name = n;
	}
	public String getName() {
		return name;
	}

	private int semesterId;
	public int getSemesterId() {
		return semesterId;
	}
	
	public void setSemesterId(int id) {
		semesterId = id;
	}
	
	public static interface RestorationEventHandler {
		public void restore();
	}
	
	private transient static HashSet<RestorationEventHandler> restoreHandlers = new HashSet<RestorationEventHandler>();
	
	public void addRestorationEventHandler(RestorationEventHandler h) {
		restoreHandlers.add(h);
	}
	
	public void removeRestorationEventHandler(RestorationEventHandler h) {
		restoreHandlers.remove(h);
	}
	
	public void restoreSchedule(SchedulerManager mgr) {
		theInstance = mgr;
		mgr.generatedSchedules = new ArrayList<Schedule>();
		mgr.generatedSchedules.add(mgr.currentSchedule);
		setChangeHandlers();
		for(RestorationEventHandler h : restoreHandlers) {
			h.restore();
		}
	}
}
