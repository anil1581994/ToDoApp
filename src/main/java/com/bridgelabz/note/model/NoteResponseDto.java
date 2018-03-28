package com.bridgelabz.note.model;

import java.util.Date;

public class NoteResponseDto {

	private int noteId;

	private String title;

	private String description;

	private Date createDate;

	private Date lastUpdateDate;
	
      private int status;
      
	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public NoteResponseDto() {
	}

	public NoteResponseDto(Note note) {
		this.noteId = note.getNoteId();
		this.title = note.getTitle();
		this.description = note.getDescription();
		this.createDate = note.getCreateDate();
		this.lastUpdateDate = note.getLastUpdateDate();
		this.status=note.getStatus();
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

}
