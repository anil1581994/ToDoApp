package com.bridgelabz.note.model;

public class CollaboratorResponseDto {
 private String name;
 private String email;
 private String title;
 private int noteId;
 private int ownerId;
 
 public int getOwnerId() {
	return ownerId;
}
public void setOwnerId(int ownerId) {
	this.ownerId = ownerId;
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
public void setDescription(String description) 
{
	this.description = description;
}
private String description;
 
public String getName() {
	return name;
}
public void setName(String name) {
	this.name = name;
}
public String getEmail() {
	return email;
}
public void setEmail(String email) {
	this.email = email;
}
}
