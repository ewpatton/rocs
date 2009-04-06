package edu.rpi.rocs.client.ui.entry;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.user.client.ui.RootPanel;

import edu.rpi.rocs.client.ui.semesterselect.SemesterSelectionPanel;

public class MainEntryPoint implements EntryPoint {

	public void onModuleLoad() {
		RootPanel.get("uid").add(new SemesterSelectionPanel());
	}

}
