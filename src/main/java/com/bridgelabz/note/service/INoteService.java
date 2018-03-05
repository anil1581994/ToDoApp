package com.bridgelabz.note.service;

import java.util.List;

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
	int noteCreatorByNoteId(int noteId);

}
