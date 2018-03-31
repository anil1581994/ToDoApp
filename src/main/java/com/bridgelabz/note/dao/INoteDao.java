package com.bridgelabz.note.dao;

import java.util.List;

import com.bridgelabz.note.model.Label;
import com.bridgelabz.note.model.Note;
import com.bridgelabz.user.model.User;

public interface INoteDao {

	void saveNote(Note note);

	boolean updateNote(Note note);

	boolean deleteNote(int noteId);

	Note getNoteById(int noteId);

	List<Note> getAllNotes(int userId);
	
	int noteCreatorByNoteId(int noteId);
	 void saveLabel(Label label);
	 List<Label> getAllLabels(int userId);

}
