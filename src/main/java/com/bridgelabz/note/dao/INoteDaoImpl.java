package com.bridgelabz.note.dao;

import java.sql.Connection;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import com.bridgelabz.exceptions.DatabaseException;
import com.bridgelabz.note.model.Label;
import com.bridgelabz.note.model.Note;
import com.bridgelabz.note.model.NoteLabel;
import com.bridgelabz.user.model.User;

@Repository
public class INoteDaoImpl implements INoteDao {
	@Autowired
	private JdbcTemplate jdbcTemplate;

	public void saveNote(Note note) {
		String INSERT_SQL = "insert into Notes values(?,?,?,?,?,?,?,?,?)";
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
				ps.setInt(7, note.getStatus());
				ps.setString(8, note.getColor());
				ps.setDate(9, null);
				return ps;
			}
		}, holder);

		int noteId = holder.getKey().intValue();
		note.setNoteId(noteId);
	}

	@Override
	public boolean updateNote(Note note) {

		int count = 0;
		String sql = "UPDATE Notes SET title=?,description=?,lastUpdateDate=?,status=?,color=?,reminder=? where noteId=?";

		count = jdbcTemplate.update(sql, new Object[] { note.getTitle(), note.getDescription(),
				note.getLastUpdateDate(), note.getStatus(), note.getColor(), note.getReminder(), note.getNoteId() });
		System.out.println(count);
		if (count == 0) {
			return false;
		} else {
			return true;
		}
	}

	@Override
	public Note getNoteById(int noteId) {
		String sql = "select * from Notes where noteId= ?";
		List<Note> list = jdbcTemplate.query(sql, new Object[] { noteId }, new NoteMapper());
		if (list.size() > 0) {
			System.out.println(list.get(0));
			return list.get(0);
		} else {
			return null;
		}

	}

	@Override
	public boolean deleteNote(int noteId) {
		String sql = "delete from Notes where noteId=?";
		int count = jdbcTemplate.update(sql, noteId);
		if (count == 0) {
			return false;
		} else {
			return true;
		}

	}

	@Override
	public List<Note> getAllNotes(int userId) {
		String sql = "select * from Notes where userId = ?";
		List<Note> notes = jdbcTemplate.query(sql, new Object[] { userId }, new NoteMapper());
		return notes;
	}

	public int noteCreatorByNoteId(int noteId) {
		String query = "select userId from Notes where noteId=?";
		Object[] obj = new Object[] { noteId };
		int creatorId = jdbcTemplate.queryForObject(query, obj, Integer.class);
		return creatorId;
	}

	class NoteMapper implements RowMapper<Note> {

		public Note mapRow(ResultSet rs, int rowNum) throws SQLException {
			Note note = new Note();
			note.setNoteId(rs.getInt("noteId"));
			note.setTitle(rs.getString("title"));
			note.setDescription(rs.getString("description"));
			note.setCreateDate(rs.getDate("createDate"));
			note.setLastUpdateDate(rs.getDate("lastUpdateDate"));
			note.setStatus(rs.getInt("status"));
			note.setColor(rs.getString("color"));

			int userId = rs.getInt("userId");

			try {
				java.util.Date date = null;
				Timestamp timestamp = rs.getTimestamp("reminder");
				if (timestamp != null)
					date = new java.util.Date(timestamp.getTime());
				note.setReminder(date);
			} catch (Exception e) {
				e.printStackTrace();
			}

			System.out.println(" indao date" + note.getReminder());

			User user = new User();
			user.setId(userId);
			note.setUser(user);
			return note;
		}
	}
	// label insertion

	public void saveLabel(Label label) {

		String query = "insert into Labels values (?,?,?)";
		int update = jdbcTemplate.update(query,
				new Object[] { label.getLabelId(), label.getLabelTitle(), label.getUser().getId() });

		if (update != 1) {
			throw new DatabaseException();
		}
	}

	@Override
	public List<Label> getAllLabels(int userId) {
		String sql = "select * from Labels where userId = ?";
		List<Label> labels = jdbcTemplate.query(sql, new Object[] { userId }, new LabelMapper());
		return labels;
	}

	// Label Mapper
	class LabelMapper implements RowMapper {

		public Label mapRow(ResultSet rs, int rowNum) throws SQLException {
			Label label = new Label();
			label.setLabelId(rs.getInt("labelId"));
			label.setLabelTitle(rs.getString("labelTitle"));
			int userId = rs.getInt("userId");
			User user = new User();
			user.setId(userId);
			label.setUser(user);
			return label;

		}
	}

	@Override
	public boolean updateLabel(Label label) {

		int count = 0;
		String sql = "UPDATE Labels SET labelTitle=? where labelId=?";

		count = jdbcTemplate.update(sql, new Object[] { label.getLabelTitle(), label.getLabelId() });
		if (count == 0) {
			return false;
		} else {
			return true;
		}
	}

	@Override
	public boolean deleteLabel(int labelId) {
		String sql = "delete from Labels where labelId=?";
		int count = jdbcTemplate.update(sql, labelId);
		if (count == 0) {
			return false;
		} else {
			return true;
		}
	}

	@Override
	public Label getLabelById(int labelId) {
		String sql = "select * from Labels where labelId= ?";
		List<Label> list = jdbcTemplate.query(sql, new Object[] { labelId }, new LabelMapper());
		if (list.size() > 0) {
			return list.get(0);
		} else {
			return null;
		}

	}

	@Override
	public Set<Label> getLabelsByNote(Note note) {
		String sql = "select * from Note_Label where noteId=?";
		List<NoteLabel> list = jdbcTemplate.query(sql, new Object[] { note.getNoteId() }, new NoteLabelMapper());
		Set<Label> labels = new HashSet<>();
		for (NoteLabel noteLabel : list) {
			labels.add(getLabelById(noteLabel.getLabelId()));
		}
		return labels;
	}

	class NoteLabelMapper implements RowMapper {

		public NoteLabel mapRow(ResultSet rs, int rowNum) throws SQLException {
			NoteLabel noteLabel = new NoteLabel();
			noteLabel.setLabelId(rs.getInt("labelId"));
			return noteLabel;

		}
	}

	@Override
	public void addLabel(NoteLabel noteLabel) {
		String query = "insert into Note_Label values (?,?)";
		int update = jdbcTemplate.update(query, new Object[] { noteLabel.getNoteId(), noteLabel.getLabelId() });

		if (update != 1) {
			throw new DatabaseException();
		}

	}

	@Override
	public void deleteLabelFromNote(NoteLabel noteLabel) {
		String sql = "delete from Note_Label where labelId=? and noteId=?";
		int count = jdbcTemplate.update(sql, new Object[] { noteLabel.getLabelId(), noteLabel.getNoteId() });
		if (count == 0) {
			throw new DatabaseException();
		}

	}

}
