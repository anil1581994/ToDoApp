package com.bridgelabz.note.service;

import java.util.List;

import com.bridgelabz.note.model.Collaborator;
import com.bridgelabz.note.model.CollaboratorResponseDto;
import com.bridgelabz.note.model.Label;
import com.bridgelabz.note.model.Note;
import com.bridgelabz.note.model.NoteLabel;
import com.bridgelabz.note.model.NoteRequestDto;
import com.bridgelabz.note.model.NoteResponseDto;
import com.bridgelabz.note.model.UpdateNoteDto;
import com.bridgelabz.user.model.User;

/**<p>This is NoteService interface</p>
 * @author bridgeit
 *
 */
public interface INoteService {

	/**<p>This is createNote service to create a new Note</p>
	 * @param noteRequestDto
	 * @param noteId
	 * @return
	 */
	NoteResponseDto createNote(NoteRequestDto noteRequestDto, int noteId);

	/**<p>This is updateNote service is used to update Note on the basis of noteId </p>
	 * @param updateNoteDto
	 * @return
	 */
	boolean updateNote(UpdateNoteDto updateNoteDto);

	/**<p>This is getNoteById service is used to get particular Note of requested noteId </p>
	 * @param noteId
	 * @return
	 */
	Note getNoteById(int noteId);

	/**<p>This is getAllNotes service is used to get AllNotes of requested userId </p>
	 * @param userId
	 * @return
	 */
	List<NoteResponseDto> getAllNotes(int userId);

	/**<p>This is deleteNote service is used to delete note of particular noteId and userId</p>
	 * @param noteId
	 * @param userId
	 */
	void deleteNote(int noteId, int userId);

	/**<p>This is createLAbel service is used create a new label for particular user</p>
	 * @param label
	 * @param userId
	 */
	void createLabel(Label label, int userId);

	/**<p>This is getAllLabels  service is used to get All labels of particular userId</p>
	 * @param userId
	 * @return
	 */
	List<Label> getAllLabels(int userId);

	/**<p>This is updateLabel service is used to update label of particular labelId</p>
	 * @param label
	 * @return
	 */
	boolean updateLabel(Label label);

	/**<p>This is deleteLabel service is used to delete label of particular user on the basis 
	 * of labelId </p>
	 * @param labelId
	 * @param userId
	 */
	void deleteLabel(int labelId, int userId);

	/**<p>This addLabel service is going to add label on particular noteId</p>
	 * @param noteId
	 * @param labelId
	 */
	void addLabel(int noteId, int labelId);

	/**<p>This deleteLabelFromNote service is going to remove label of particular noteId</p>
	 * @param noteId
	 * @param labelId
	 */
	void deleteLabelFromNote(int noteId, int labelId);

	/**<p>This is getNoteLabels service is used to get all labels of particular noteId
	 * @param noteId
	 * @return
	 */
	List<Label> getNoteLabels(int noteId);

	/**<p>This is isLabelExists service is used to check particular 
	 * label is already exist or not</p>
	 * 
	 * @param labelTitle
	 * @return
	 */
	boolean isLabelExists(String labelTitle);

	/**<p>This is remove collaborator service is used remove collaborator from 
	 * particular Note and on the basis of sharedUser email
	 * </p>
	 * @param sharedUserId
	 * @param noteId
	 * @param userId
	 */
	void removeCollaborator(String sharedUserId, int noteId, int userId);

	/**<p>This is addCollaborator service is used add collaborator to Note</p>
	 * @param sharedUserId
	 * @param noteId
	 * @param userId
	 * @return
	 */
	int addCollaborator(String sharedUserId, int noteId, int userId);

	 }
