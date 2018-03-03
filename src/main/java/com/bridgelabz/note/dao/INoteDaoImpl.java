package com.bridgelabz.note.dao;

import java.sql.Connection;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import com.bridgelabz.note.model.Note;
import com.bridgelabz.user.model.User;

@Repository
public class INoteDaoImpl implements INoteDao {
	@Autowired
	INoteDao noteDao;
	@Autowired
	JdbcTemplate jdbcTemplate;

	public void saveNote(Note note) {
		String INSERT_SQL = "insert into Notes values(?,?,?,?,?,?)";
		KeyHolder holder = new GeneratedKeyHolder();
		jdbcTemplate.update(new PreparedStatementCreator() {

			@Override
			public PreparedStatement createPreparedStatement(Connection connection) throws SQLException {
				PreparedStatement ps = connection.prepareStatement(INSERT_SQL, Statement.RETURN_GENERATED_KEYS);
				ps.setInt(1, note.getNoteId());
				ps.setString(2, note.getTitle());
				ps.setString(3, note.getDescription());
				ps.setDate(4, new Date(note.getCreateDate().getTime()));
				ps.setDate(5, new Date(note.getLastUpdateDate().getTime()));
				ps.setInt(6, note.getUser().getId());
				return ps;
			}
		}, holder);

		int noteId = holder.getKey().intValue();
		note.setNoteId(noteId);

	}

	@Override
	public boolean updateNote(Note note) {

		int count = 0;
		String sql = "UPDATE Notes SET title=?,description=?,lastUpdateDate=? where noteId=?";

		count = jdbcTemplate.update(sql,
				new Object[] { note.getTitle(), note.getDescription(), note.getLastUpdateDate() });
		System.out.println(count);
		if (count == 0) {
			return false;
		} else {
			return true;
		}
	}

	@Override
	public Note getNoteById(int noteId) {
		String sql = "select * from Notes where userId= ?";
		List<Note> list = jdbcTemplate.query(sql, new Object[] {noteId}, new NoteMapper());
		if (list.size() > 0) {
			System.out.println(list.get(0));
			return list.get(0);
		} else {
			return null;
		}

	}


	@Override
	public boolean deleteNote(int noteId) {
		String sql="delete from Notes where noteId=?";
		int count=jdbcTemplate.update(sql,noteId);
		if (count == 0) {
			return false;
		} else {
			return true;
		}
		
	}
	@Override
	public List<Note> getAllNotes(int userid) {
		String sql="select * from notes where userid = ?";
		List<Note> notes=jdbcTemplate.query(sql,  new NoteMapper());
         return notes;
	}

	
	class NoteMapper implements RowMapper {

		public Note mapRow(ResultSet rs, int rowNum) throws SQLException {
			Note note=new Note();
			note.setNoteId(rs.getInt("noteId"));
			note.setTitle(rs.getString("title"));
			note.setDescription(rs.getString("description"));
			note.setCreateDate(rs.getDate("createDate"));
			note.setLastUpdateDate(rs.getDate("lastUpdateDate"));
			return note;
		}
	}

	


}
