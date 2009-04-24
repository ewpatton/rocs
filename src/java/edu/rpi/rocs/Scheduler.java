package edu.rpi.rocs;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Map;

import javax.portlet.ActionRequest;
import javax.portlet.ActionResponse;
import javax.portlet.GenericPortlet;
import javax.portlet.PortletException;
import javax.portlet.PortletRequest;
import javax.portlet.RenderRequest;
import javax.portlet.RenderResponse;

import edu.rpi.rocs.server.objectmodel.CourseDBImpl;

/**
 * The main Scheduler interface, an instance of JSR-168 Porlet.
 * 
 * @author ewpatton
 *
 */
public class Scheduler extends GenericPortlet {
	
	public void init() {
		try {
			CourseDBImpl.addCourseDB("http://pattoe.stu.rpi.edu/rocs-portlet/current.xml");
		}
		catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Processes an action generated by the user.
	 * 
	 * @param aRequest The ActionRequest object from JSR-168 Portal
	 * @param aResponse The ActionResponse object describing render parameters
	 * @throws PortletException
	 */
	public void processAction(ActionRequest aRequest, ActionResponse aResponse)
		throws PortletException {
		/*
		try {
			CASPortletUtils.establishSession(aRequest, this.ptvFactory);
		}
		catch(CASAuthenticationException e) {
			throw new PortletException(e);
		}
		*/
	}
	
	/**
	 * Processes a view render request.
	 * 
	 * @param aRequest The RenderRequest object from JSR-168 Portal
	 * @param aResponse The RenderResponse object describing the rendered content
	 * @throws PortletException
	 * @throws IOException
	 */
	@SuppressWarnings("unchecked")
	public void doView(RenderRequest aRequest, RenderResponse aResponse)
		throws PortletException, IOException {
		aResponse.setContentType("text/html");
		PrintWriter out = aResponse.getWriter();
		Map userinfo = (Map)aRequest.getAttribute(PortletRequest.USER_INFO);
		if(userinfo == null) {
			out.println("<p>You must be logged in to the portal to use ROCS.</p>");
			return;
		}
		String userName = (String)userinfo.get("user.login.id");
		String givenName = (String)userinfo.get("user.name.given");
		String familyName = (String)userinfo.get("user.name.family");
		System.out.println("Entering Scheduler.doView");
		out.println("<h1>Rensselaer Open Course Scheduler</h1>");
		out.print("<p>Welcome " + givenName + " " + familyName + " (" + userName + ")</p>");
		
		out.println("<br><br><p>");
		out.println("<script language='javascript' src='" + aRequest.getContextPath() + "/rocs.gwt/rocs.gwt.nocache.js'></script>");
		out.println("Scheduler Test:");
		out.println("<div id='uid'></div>");
		out.println("<div id='semesterSelect'></div>");
		out.println("<div id='searchFilterPanel'></div>");
		out.println("<div id='searchResultPanel'></div>");
		out.print("</p>");
	}
	
	/**
	 * Processes an edit render request.
	 * 
	 * @param aRequest The RenderRequest object from JSR-168 Portal
	 * @param aResponse The RenderResponse object describing the rendered content
	 * @throws PortletException
	 * @throws IOException
	 */
	public void doEdit(RenderRequest aRequest, RenderResponse aResponse)
		throws PortletException, IOException {
		
	}
	
	/**
	 * Processes a help render request.
	 * 
	 * @param aRequest The RenderRequest object from JSR-168 Portal
	 * @param aResponse The RenderResponse object describing the rendered content
	 * @throws PortletException
	 * @throws IOException
	 */
	public void doHelp(RenderRequest aRequest, RenderResponse aResponse)
		throws PortletException, IOException {
		
	}
}
