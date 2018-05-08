package com.bridgelabz.note.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class NoteResponse {

	private int noteId;

	private String title;

	private String description;

	private Date createDate;

	private Date lastUpdateDate;

	private int status;

	private String color;

	private Date reminder;

	private CollaboratorResponseDto owner;

	private List<CollaboratorResponseDto> collaborators = new ArrayList<>();

	private Set<Label> labels = new HashSet<>();

	public NoteResponse() {

	}

	public NoteResponse(Note note) {
		this.noteId = note.getNoteId();
		this.title = note.getTitle();
		this.description = note.getDescription();
		this.createDate = note.getCreateDate();
		this.lastUpdateDate = note.getLastUpdateDate();
		this.status = note.getStatus();
		this.color = note.getColor();
		this.reminder = note.getReminder();
		this.collaborators = note.getCollaboratorResponseDto();
		this.labels = note.getLabels();
	}

	public Set<Label> getLabels() {
		return labels;
	}

	public void setLabels(Set<Label> labels) {
		this.labels = labels;
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

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public String getColor() {
		return color;
	}

	public void setColor(String color) {
		this.color = color;
	}

	public Date getReminder() {
		return reminder;
	}

	public void setReminder(Date reminder) {
		this.reminder = reminder;
	}

	public CollaboratorResponseDto getOwner() {
		return owner;
	}

	public void setOwner(CollaboratorResponseDto owner) {
		this.owner = owner;
	}

	public List<CollaboratorResponseDto> getCollaborators() {
		return collaborators;
	}

	public void setCollaborators(List<CollaboratorResponseDto> collaborators) {
		this.collaborators = collaborators;
	}

}
