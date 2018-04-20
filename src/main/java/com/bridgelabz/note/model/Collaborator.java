package com.bridgelabz.note.model;
public class Collaborator {
	private int collaboratorId;//
	private int noteId;
	private  String sharedUserId;
	private int userId;
	
	public int getUserId() {
		return userId;
	}
	public void setUserId(int userId) {
		this.userId = userId;
	}
	public int getCollaboratorId() {
		return collaboratorId;
	}
	public void setCollaboratorId(int collaboratorId) {
		this.collaboratorId = collaboratorId;
	}
	public int getNoteId() {
		return noteId;
	}
	public void setNoteId(int noteId) {
		this.noteId = noteId;
	}
	public String getSharedUserId() {
		return sharedUserId;
	}
	public void setSharedUserId(String sharedUserId) {
		this.sharedUserId = sharedUserId;
	}
}
	