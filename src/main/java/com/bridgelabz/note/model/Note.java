package com.bridgelabz.note.model;

import java.util.Date;

import com.bridgelabz.user.model.User;

public class Note {
	private int noteId;
	private String title;
	private String description;
	private Date createDate;
	private Date lastUpdateDate;
	private User user;
	private int status;
	private String color;

	

	public Note() {
	}

	public Note(NoteRequestDto noteRequestDto) {

		this.title = noteRequestDto.getTitle();
		this.description = noteRequestDto.getDescription();
	}

	public Note(UpdateNoteDto updateNoteDto) {
		this.noteId = updateNoteDto.getNoteId();
		this.title = updateNoteDto.getTitle();
		this.description = updateNoteDto.getDescription();
		this.lastUpdateDate = updateNoteDto.getLastUpdateDate();
		this.status=updateNoteDto.getStatus();
	}

	public int getNoteId() {
		return noteId;
	}

	public void setNoteId(int noteId) {
		this.noteId = noteId;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Date getCreateDate() {
		return createDate;
	}

	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}

	public Date getLastUpdateDate() {
		return lastUpdateDate;
	}

	public void setLastUpdateDate(Date lastUpdateDate) {
		this.lastUpdateDate = lastUpdateDate;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}
	public String getColor() {
		return color;
	}

	public void setColor(String color) {
		this.color = color;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}
}
