package com.example.gridview.dao;

public class DataManager {
	private static DataManager manager = new DataManager();
	private Comment comment;
	
	private DataManager(){
		
	}
	
	public static DataManager getInstance(){
		return manager;
	}

	public Comment getComment() {
		return comment;
	}

	public void setComment(Comment comment) {
		this.comment = comment;
	}
	
	
}
