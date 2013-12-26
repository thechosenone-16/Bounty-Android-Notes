package com.harsh.bountynotes;

public class NoteItem {

	String title;
	String notetext;
	String timetoset;
	String timeatcreated;

	public NoteItem() {
		this.notetext = " ";
		this.timeatcreated = " ";
		this.timetoset = " ";
		this.title = " ";
	}

	public NoteItem(String notetext, String timetoset, String timecreatedat) {
		this.notetext = notetext;
		this.timeatcreated = timecreatedat;
		this.timetoset = timetoset;
		this.title = " ";
	}

	public String getNoteText() {
		return notetext;
	}

	public String getNoteTitle() {
		return title;
	}

	public String getNoteSetTime() {
		return timetoset;
	}

	public String getNoteCreatedTime() {
		return timeatcreated;
	}

	public void setNoteText(String notetext) {
		this.notetext = notetext;
	}

	public void setNoteSetTime(String timetoset) {
		this.timetoset = timetoset;
	}

	public void setNoteCreatedTime(String timeatcreated) {
		this.timeatcreated = timeatcreated;
	}

	public void setNoteTitle(String notetitle) {
		this.title = notetitle;
	}

}
