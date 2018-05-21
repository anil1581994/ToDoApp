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
import com.bridgelabz.note.model.Collaborator;
import com.bridgelabz.note.model.CollaboratorResponseDto;
import com.bridgelabz.note.model.Label;
import com.bridgelabz.note.model.Note;
import com.bridgelabz.note.model.NoteLabel;
//import com.bridgelabz.user.dao.GetSharedNotes;
//import com.bridgelabz.user.dao.UserDaoImpl.UserMapper;
import com.bridgelabz.user.model.User;
import com.bridgelabz.user.util.UrlData;

@Repository
public class INoteDaoImpl implements INoteDao {
	@Autowired
	private JdbcTemplate jdbcTemplate;

	public void saveNote(Note note) {
		String INSERT_SQL = "insert into Notes values(?,?,?,?,?,?,?,?,?,?)";
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
				ps.setString(10, note.getImage());
				return ps;
			}
		}, holder);

		int noteId = holder.getKey().intValue();
		note.setNoteId(noteId);
	}

	@Override
	public boolean updateNote(Note note) {

		int count = 0;
		String sql = "UPDATE Notes SET title=?,description=?,lastUpdateDate=?,status=?,color=?,reminder=?,image=? where noteId=?";

		count = jdbcTemplate.update(sql,
				new Object[] { note.getTitle(), note.getDescription(), note.getLastUpdateDate(), note.getStatus(),
						note.getColor(), note.getReminder(), note.getImage(), note.getNoteId() });
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
			System.out.println("note titile" + list.get(0).getTitle());
			return list.get(0);
		} else {
			return null;
		}

	}

	public boolean isLabeled(int noteId) {
		String query = "select * from Note_Label where noteId=?";
		List<NoteLabel> list = jdbcTemplate.query(query, new Object[] { noteId }, new NoteLabelMapper());

		if (list.size() > 0) {
			return true;
		} else {
			return false;
		}
	}

	@Override
	public boolean deleteNote(int noteId) {

		if (isLabeled(noteId)) {
			try {
				deleteNoteLabel(noteId);
			} catch (Exception e) {

			}
		}

		String sql = "delete from Notes where noteId=?";
		int count = jdbcTemplate.update(sql, noteId);
		if (count == 0) {
			return false;
		} else {
			return true;
		}

	}

	public boolean deleteNoteLabel(int noteId) {
		String sql = "delete from  Note_Label where noteId=?";
		int count = jdbcTemplate.update(sql, noteId);

		if (count == 0) {
			throw new DatabaseException();
		}
		return true;
	}

	@Override
	public List<Note> getAllNotes(int userId) {
		//String sql = "select * from Notes where userId = ?";
		//String sql = "select * from Notes where userId = ? order by noteId desc";
		String sql="select * from (select * from Notes  order by noteId desc) e where userId=?";
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
			note.setImage(rs.getString("image"));
			User user = new User();
			int userId = rs.getInt("userId");
			user.setId(userId);
			note.setUser(user);
			try {
				java.util.Date date = null;
				Timestamp timestamp = rs.getTimestamp("reminder");
				if (timestamp != null)
					date = new java.util.Date(timestamp.getTime());
				note.setReminder(date);
			} catch (Exception e) {
				e.printStackTrace();
			}

			return note;
		}
	}

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

	class LabelMapper implements RowMapper {

		public Label mapRow(ResultSet rs, int rowNum) throws SQLException {
			Label label = new Label();
			label.setLabelId(rs.getInt("labelId"));
			label.setLabelTitle(rs.getString("labelTitle"));
			User user = new User();
			int userId = rs.getInt("userId");
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
		//delete the label from Note_label where pk--fk relation
		if (isLabel(labelId)) {
			try {
				deletedialogLabel(labelId);
			} catch (Exception e) {

			}
		}
		String sql = "delete from Labels where labelId=?";
		int count = jdbcTemplate.update(sql, labelId);
		if (count == 0) {
			return false;
		} else {
			return true;
		}
	}
      public boolean isLabel(int labelId) {
    	  String query = "select * from Note_Label where labelId=?";
  		List<NoteLabel> list = jdbcTemplate.query(query, new Object[] { labelId }, new NoteLabelMapper());

  		if (list.size() > 0) {
  			return true;
  		} else {
  			return false;
  		}
    	  
      }
     public boolean deletedialogLabel(int labelId)
      {
    	  String sql = "delete from  Note_Label where labelId=?";
  		int count = jdbcTemplate.update(sql, labelId);

  		if (count == 0) {
  			throw new DatabaseException();
  		}
  		return true;
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
			noteLabel.setNoteId(rs.getInt("noteId"));
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

	@Override
	public List<Label> getNoteLabels(int noteId) {
		String sql = "select Note_Label.noteId,Labels.labelId,Labels.labelTitle From Note_Label inner join Labels on Note_Label.labelId=Labels.labelId\n"
				+ "where Note_Label.noteId=?";
		List<Label> labels = jdbcTemplate.query(sql, new Object[] { noteId }, new LabelMapper());
		return labels;

	}

	@Override
	public List<CollaboratorResponseDto> getCollaboratorsByNote(int noteId) {
		String sql = "select Users.name,Users.email from  Users inner join Collaborators on Users.id=Collaborators.sharedUserId where Collaborators.noteId=?";
		List<CollaboratorResponseDto> list = jdbcTemplate.query(sql, new Object[] { noteId },
				new GetCollaboratorMapper());
		return list.size() > 0 ? list : null;
	}

	class GetCollaboratorMapper implements RowMapper {

		public CollaboratorResponseDto mapRow(ResultSet rs, int rowNum) throws SQLException {
			CollaboratorResponseDto collaboratorResponseDto = new CollaboratorResponseDto();
			collaboratorResponseDto.setName(rs.getString("name"));//
			collaboratorResponseDto.setEmail(rs.getString("email"));
			return collaboratorResponseDto;
		}
	}

	@Override
	public boolean isLabelExists(String labelTitle) {
		String sql = "select count(*) from Labels where labelTitle = ?";
		boolean result = false;
		int count = jdbcTemplate.queryForObject(sql, new Object[] { labelTitle }, Integer.class);
		if (count > 0) {
			result = true;
		}
		return result;
	}

	@Override
	public boolean saveCollaborator(Collaborator collaborator, int userId) {
		String query = "insert into Collaborators values (?,?,?,?)";
		int update = jdbcTemplate.update(query, new Object[] { collaborator.getCollaboratorId(),
				collaborator.getNoteId(), collaborator.getSharedUserId(), userId });

		if (update != 1) {
			throw new DatabaseException();
		}
		return false;
	}

	@Override
	public User getUserById(int userId) {
		String sql = "select * from Users where id= ?";
		List<User> list = jdbcTemplate.query(sql, new Object[] { userId }, new UserMapper());
		if (list.size() > 0) {
			System.out.println(list.get(0));
			return list.get(0);
		} else {
			return null;
		}
	}

	class UserMapper implements RowMapper {

		public User mapRow(ResultSet rs, int rowNum) throws SQLException {
			User user = new User();
			user.setEmail(rs.getString("email"));

			return user;

		}
	}

	@Override
	public List<Collaborator> getCollaboratorNoteIdAndUserId(int userId) {
		String query = "select * from Collaborators where sharedUserId=?";
		List<Collaborator> list = jdbcTemplate.query(query, new Object[] {userId }, new CollaboratorMapper());
		return list.size() > 0 ? list : null;

	}

	class CollaboratorMapper implements RowMapper {

		public Collaborator mapRow(ResultSet rs, int rowNum) throws SQLException {

			Collaborator collaborator = new Collaborator();
			collaborator.setNoteId(rs.getInt("noteId"));
			collaborator.setCollaboratorId(rs.getInt("collaboratorId"));
			collaborator.setUserId(rs.getInt("userId"));
			collaborator.setSharedUserId(rs.getInt("sharedUserId"));
			return collaborator;

		}
	}

	@Override
	public CollaboratorResponseDto getSharedNotes(int noteId, int sharedUserId) {
<<<<<<< HEAD
	//1
		
//		String sql="SELECT Usersname,Users.email FROM Users INNER JOIN Collaborators ON Users.email=Collaborators.sharedUserId where Collaborators.noteId=?;";
		 String sql="SELECT Notes.title,Notes.description,Users.name\n" + 
		            "FROM Notes,Users \n" + 
		            "where Notes.noteId=? and Users.id=? ;";
		      
		      List<CollaboratorResponseDto> list = jdbcTemplate.query(sql, new Object[] {noteId,sharedUserId}, new GetSharedNotes());
		      return list.size() > 0 ? list.get(0) : null;
		     
	}

	class GetSharedNotes implements org.springframework.jdbc.core.RowMapper<CollaboratorResponseDto>
	{
	   public CollaboratorResponseDto mapRow(ResultSet rs, int rowNum) throws SQLException
	   {
		   CollaboratorResponseDto sharedNote = new CollaboratorResponseDto();
	      
		   sharedNote.setTitle(rs.getString("title"));
		   sharedNote.setDescription(rs.getString("description"));
		   sharedNote.setName(rs.getString("name"));//person-2
		   return sharedNote;
	   }
=======

		String sql = "SELECT Notes.title,Notes.description,Users.name\n" + "FROM Notes,Users \n"
				+ "where Notes.noteId=? and Users.id=?";

		List<CollaboratorResponseDto> list = jdbcTemplate.query(sql, new Object[] { noteId, sharedUserId },
				new GetSharedNotes());
		return list.size() > 0 ? list.get(0) : null;

>>>>>>> e3806fb236aa17bcf5ebe6870e5c48db1f8e85e4
	}

	class GetSharedNotes implements org.springframework.jdbc.core.RowMapper<CollaboratorResponseDto> {
		public CollaboratorResponseDto mapRow(ResultSet rs, int rowNum) throws SQLException {
			CollaboratorResponseDto sharedNote = new CollaboratorResponseDto();

			sharedNote.setTitle(rs.getString("title"));
			sharedNote.setDescription(rs.getString("description"));
			sharedNote.setName(rs.getString("name"));
			return sharedNote;
		}
	}

	@Override
	public User getsharedUserByEmail(String email) {
		String sql = "select * from Users where email = ?";
		List<User> list = jdbcTemplate.query(sql, new Object[] { email }, new SharedUserMapper());
		if (list.size() > 0) {
			System.out.println(list.get(0));
			return list.get(0);
		} else {
			return null;
		}
	}

	class SharedUserMapper implements RowMapper {

		public User mapRow(ResultSet rs, int rowNum) throws SQLException {
			User user = new User();
			user.setEmail(rs.getString("email"));
			user.setName(rs.getString("name"));
			return user;

		}
	}

	@Override
	public void removeCollaborator(Collaborator collaborator) {
		String sql = "delete from Collaborators where noteId=? and sharedUserId=?";
		int count = jdbcTemplate.update(sql, new Object[] { collaborator.getNoteId(), collaborator.getSharedUserId() });
		if (count == 0) {
			throw new DatabaseException();
		}
	}

	@Override
<<<<<<< HEAD
	public List<UrlData> getAllUrls(int noteId) {
		// TODO Auto-generated method stub
		return null;
=======
	public List<Note> getLabeldNotes(int labelId) {
		String sql= "SELECT * FROM Notes INNER JOIN  Note_Label ON Notes.noteId= Note_Label.noteId where Note_Label.labelId=?";
		List<Note> notes = jdbcTemplate.query(sql, new Object[] {labelId}, new NoteMapper());
		return notes;
	
>>>>>>> e3806fb236aa17bcf5ebe6870e5c48db1f8e85e4
	}

}