package com.bridgelabz.note.service;

import java.util.List;

import com.bridgelabz.note.model.Label;
import com.bridgelabz.note.model.Note;
import com.bridgelabz.note.model.NoteRequestDto;
import com.bridgelabz.note.model.NoteResponseDto;
import com.bridgelabz.note.model.UpdateNoteDto;
import com.bridgelabz.user.model.User;

public interface INoteService {
	
	NoteResponseDto createNote(NoteRequestDto noteRequestDto, int noteId);

	boolean updateNote(UpdateNoteDto updateNoteDto);
	
    Note getNoteById(int noteId);

	List<NoteResponseDto> getAllNotes(int userId);

	void deleteNote(int noteId, int userId);
	
	void createLabel(Label label,int userId);
	
	List<Label> getAllLabels(int userId);
	
	boolean updateLabel(Label label);
	
	void deleteLabel(int labelId, int userId);
	

}
