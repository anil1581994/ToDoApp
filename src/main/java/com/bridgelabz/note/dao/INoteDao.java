package com.bridgelabz.note.dao;

import java.util.List;

import com.bridgelabz.note.model.Note;
import com.bridgelabz.user.model.User;

public interface INoteDao {

	void saveNote(Note note);

	boolean updateNote(Note note);

	Note getNoteById(int userId);

	boolean deleteNote(int noteId);
	
	List<Note> getAllNotes(int userId);
	


}
