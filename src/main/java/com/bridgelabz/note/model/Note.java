package com.bridgelabz.note.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

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
	private Date reminder;
	private String name;
	private int ownerId;
	private String collaboratorName;
	private String image;

	public String getImage() {
		return image;
	}

	public void setImage(String image) {
		this.image = image;
	}

	public int getOwnerId() {
		return ownerId;
	}

	public void setOwnerId(int ownerId) {
		this.ownerId = ownerId;
	}

	public String getCollaboratorName() {
		return collaboratorName;
	}

	public void setCollaboratorName(String collaboratorName) {
		this.collaboratorName = collaboratorName;
	}

	private Set<Label> labels = new HashSet<>();
	private List<CollaboratorResponseDto> CollaboratorResponseDto = new ArrayList();

	public List<CollaboratorResponseDto> getCollaboratorResponseDto() {
		return CollaboratorResponseDto;
	}

	public void setCollaboratorResponseDto(List<CollaboratorResponseDto> collaboratorResponseDto) {
		CollaboratorResponseDto = collaboratorResponseDto;
	}

	public Set<Label> getLabels() {
		return labels;
	}

	public void setLabels(Set<Label> labels) {
		this.labels = labels;
	}

	public Note() {
	}

	public Note(NoteRequestDto noteRequestDto) {

		this.title = noteRequestDto.getTitle();
		this.description = noteRequestDto.getDescription();
	}

	public Note(CollaboratorResponseDto collaboratorResponseDto) {
		this.title = collaboratorResponseDto.getTitle();
		this.description = collaboratorResponseDto.getDescription();
		this.noteId = collaboratorResponseDto.getNoteId();
		this.collaboratorName = collaboratorResponseDto.getName();//
		this.ownerId = collaboratorResponseDto.getOwnerId();
	}

	public Note(UpdateNoteDto updateNoteDto) {
		this.noteId = updateNoteDto.getNoteId();
		this.title = updateNoteDto.getTitle();
		this.description = updateNoteDto.getDescription();
		this.lastUpdateDate = updateNoteDto.getLastUpdateDate();
		this.status = updateNoteDto.getStatus();
		this.color = updateNoteDto.getColor();
		this.image = updateNoteDto.getImage();
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

	public Date getReminder() {
		return reminder;
	}

	public void setReminder(Date reminder) {
		this.reminder = reminder;
	}
}
