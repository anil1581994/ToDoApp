package com.bridgelabz.note.model;

import com.bridgelabz.user.model.User;

public class NoteLabel {
	private int labelId;
	private int noteId;
	public int getLabelId() {
		return labelId;
	}
	public void setLabelId(int labelId) {
		this.labelId = labelId;
	}
	public int getNoteId() {
		return noteId;
	}
	public void setNoteId(int noteId) {
		this.noteId = noteId;
	}
}
