package com.bridgelabz.note.dao;

import java.util.List;

import java.util.Set;

import com.bridgelabz.note.model.Label;
import com.bridgelabz.note.model.Note;
import com.bridgelabz.note.model.NoteLabel;
import com.bridgelabz.user.model.User;

public interface INoteDao {

	void saveNote(Note note);

	boolean updateNote(Note note);

	boolean deleteNote(int noteId);

	Note getNoteById(int noteId);

	List<Note> getAllNotes(int userId);

	int noteCreatorByNoteId(int noteId);
	
   //.............Label......................//
	void saveLabel(Label label);

	List<Label> getAllLabels(int userId);
	
	boolean updateLabel(Label label);
	
	boolean deleteLabel(int labelId);
	
	Label getLabelById(int labelId);
//	
//    boolean updateLabelToNote(int noteId,int labelId);
//    
//    boolean  deleteLabelFromNote(int noteId,int labelId);

	Set<Label> getLabelsByNote(Note note);
	void addLabel(NoteLabel noteLabel);
	void deleteLabelFromNote(NoteLabel noteLabel);
	 List<Label> getNoteLabels(int noteId);
	 boolean isLabelExists(String labelTitle);

	

}
