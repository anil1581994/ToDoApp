package com.bridgelabz.note.service;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.bridgelabz.note.dao.INoteDao;
import com.bridgelabz.note.model.Note;
import com.bridgelabz.note.model.NoteRequestDto;
import com.bridgelabz.note.model.NoteResponseDto;
import com.bridgelabz.note.model.UpdateNoteDto;
import com.bridgelabz.user.dao.UserDao;
import com.bridgelabz.user.model.User;

@Service
public class INoteServiceImpl implements INoteService {
	@Autowired
	INoteDao noteDao;
	@Autowired
	UserDao userDao;

	@Override
	public NoteResponseDto createNote(NoteRequestDto noteRequestDto, int userId) {
		User user = userDao.getUserById(userId);
		if (user != null) {
			Date date = new Date();// util
			// Date date=new java.sql.Date(dateObj.getTime());
			Note note = new Note(noteRequestDto);
			note.setCreateDate(date);
			note.setLastUpdateDate(date);
			note.setUser(user);
			noteDao.saveNote(note);
			NoteResponseDto noteResponseDto = new NoteResponseDto(note);
			return noteResponseDto;
		}
		return null;
	}

	@Override
	public boolean updateNote(UpdateNoteDto updateNoteDto) {
		Note note = new Note(updateNoteDto);
		boolean status = noteDao.updateNote(note);
		return status;
	}

	@Override
	public Note getNoteById(int noteId) {
		Note note = noteDao.getNoteById(noteId);
		return note;
	}

	@Override
	public boolean deleteNote(int noteId) {
		boolean status =noteDao.deleteNote(noteId);
		return status;
	}

	@Override
	public List<Note> getAllNotes(int userId) {
		List list=noteDao.getAllNotes(userId);
		return list;
	}
}
