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

			//System.out.println(" indao date" + note.getReminder());

		///User user = new User();
	//	user.setId(userId);
	///	note.setUser(user);
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

	// Label Mapper
	class LabelMapper implements RowMapper {

		public Label mapRow(ResultSet rs, int rowNum) throws SQLException {
			Label label = new Label();
			label.setLabelId(rs.getInt("labelId"));
			label.setLabelTitle(rs.getString("labelTitle"));
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
    //labels belong to particular note
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

	@Override
	public List<Label> getNoteLabels(int noteId) {
		String sql = "select Note_Label.noteId,Labels.labelId,Labels.labelTitle From Note_Label inner join Labels on Note_Label.labelId=Labels.labelId\n" + 
				"where Note_Label.noteId=?";
	List<Label> labels= jdbcTemplate.query(sql, new Object[] { noteId }, new LabelMapper());
	return labels;
		
	}
	//-------------------------------------------------------------------------------------
	//ok
	@Override
	public List<CollaboratorResponseDto> getCollaboratorsByNote(int noteId) {
		String sql="SELECT Users.name,Users.email FROM Users INNER JOIN Collaborators ON Users.id=Collaborators.userId where Collaborators.noteId=?";
/*    String sql="SELECT Collaborators.sharedUserId,( select name from Users where Users.email=Collaborators.sharedUserId)\n" + 
    		"     FROM Users\n" + 
    		"     INNER JOIN Collaborators\n" + 
    		"     ON Users.id=Collaborators.userId\n" + 
    		"    where Collaborators.noteId=?;";
*/		List<CollaboratorResponseDto> list = jdbcTemplate.query(sql, new Object[] {noteId}, new GetCollaboratorMapper());
        return list.size() > 0 ? list : null;
		
		
	}
	class GetCollaboratorMapper implements RowMapper {

		public CollaboratorResponseDto mapRow(ResultSet rs, int rowNum) throws SQLException 
		   {
            //future along with email you need to get photo from user S3
		    CollaboratorResponseDto collaboratorResponseDto=new CollaboratorResponseDto();
		    collaboratorResponseDto.setName(rs.getString("name"));//
	     	collaboratorResponseDto.setEmail(rs.getString("email"));
	     	return collaboratorResponseDto;
	     	}
	}

	@Override
	public boolean isLabelExists(String labelTitle) {
		 String sql = "select count(*) from Labels where labelTitle = ?";
		    boolean result = false;//if label is not available
		    int count = jdbcTemplate.queryForObject(sql, new Object[] { labelTitle }, Integer.class);
		    if (count > 0) {
		      result = true;
		    }
		    return result;
	}
//ok
	@Override
	public boolean saveCollaborator(Collaborator collaborator,int userId) {
		String query = "insert into Collaborators values (?,?,?,?)";
		int update = jdbcTemplate.update(query,
				new Object[] { collaborator.getCollaboratorId(),collaborator.getNoteId(),userId,collaborator.getSharedUserId()});

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
	


     //userMapper
	class UserMapper implements RowMapper {

		public User mapRow(ResultSet rs, int rowNum) throws SQLException {
			User user = new User();
			user.setEmail(rs.getString("email"));
			
			return user;

		}
	}
    //collaborator object...0k
	@Override
	public List<Collaborator> getCollaboratorBySharedId(String email)
	{
		String query="select * from Collaborators where sharedUserId=?";
	     List<Collaborator> list = jdbcTemplate.query(query, new Object[] {email}, new CollaboratorMapper());
	      return list.size() > 0 ? list : null;
		
    }
	
	class CollaboratorMapper implements RowMapper {

		public Collaborator mapRow(ResultSet rs, int rowNum) throws SQLException {

			Collaborator collaborator=new Collaborator();
			collaborator.setNoteId(rs.getInt("noteId"));
			collaborator.setCollaboratorId(rs.getInt("collaboratorId"));
			collaborator.setUserId(rs.getInt("userId"));
			collaborator.setSharedUserId(rs.getString("sharedUserId"));
			return collaborator;
			
		}
	}
   //shared note details..from collaborator object
	@Override
	public CollaboratorResponseDto getSharedNotes(int noteId, int userId) {
	
		 String sql="SELECT Notes.title,Notes.description,Users.name\n" + 
		            "FROM Notes,Users \n" + 
		            "where Notes.noteId=? and Users.id=? ;";
		      
		      List<CollaboratorResponseDto> list = jdbcTemplate.query(sql, new Object[] {noteId,userId}, new GetSharedNotes());
		      return list.size() > 0 ? list.get(0) : null;
		     
	}
	
	class GetSharedNotes implements org.springframework.jdbc.core.RowMapper<CollaboratorResponseDto>
	{
	   public CollaboratorResponseDto mapRow(ResultSet rs, int rowNum) throws SQLException
	   {
		   CollaboratorResponseDto sharedNote = new CollaboratorResponseDto();
	      
		   sharedNote.setTitle(rs.getString("title"));
		   sharedNote.setDescription(rs.getString("description"));
		   sharedNote.setName(rs.getString("name"));
		   return sharedNote;
	   }
	}


	@Override
	public User getsharedUserByEmail(String email)
	{
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
		int count = jdbcTemplate.update(sql, new Object[] { collaborator.getNoteId(),collaborator.getSharedUserId()});
		if (count == 0) {
			throw new DatabaseException();
		}
	}



}
