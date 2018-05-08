package com.bridgelabz.user.dao;

import java.sql.ResultSet;

import java.sql.SQLException;
import java.util.List;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import com.bridgelabz.exceptions.DatabaseException;
import com.bridgelabz.note.model.CollaboratorResponseDto;
import com.bridgelabz.user.model.User;
import com.bridgelabz.user.model.UserDto;

@Repository
public class UserDaoImpl implements UserDao {
	@Autowired
	private DataSource dataSource;
	@Autowired
	private JdbcTemplate jdbcTemplate;

	public void register(User user) {

		String query = "insert into Users values (?,?,?,?,?,?,?)";
		int update = jdbcTemplate.update(query, new Object[] { user.getId(), user.getName(), user.getEmail(),
				user.getPassword(), user.getMobileNumber(), user.getRandomID(), user.getIsActive() });
		System.out.println(update);

		if (update != 1) {
			throw new DatabaseException();
		}
	}

	public User getUserByEmail(User user) {
		String sql = "select * from Users where email = ?";
		System.out.println(user.getEmail());
		List<User> list = jdbcTemplate.query(sql, new Object[] { user.getEmail() }, new UserMapper());
		System.out.println(list.get(0));
		if (list.size() > 0) {
			return list.get(0);
		} else {
			return null;
		}

	}

	@SuppressWarnings("unchecked")
	public User getUserByEmailId(String email) {

		String sql = "select * from Users where email = ?";
		List<User> list = jdbcTemplate.query(sql, new Object[] { email }, new UserMapper());
		if (list.size() > 0) {
			System.out.println(list.get(0));
			return list.get(0);
		} else {
			return null;
		}

	}

	public boolean saveUsernameUid(String name, String randomUUID) {
		int count = 0;
		System.out.println(name);
		String sql = "update Users set randomID = ? where email=?";
		count = jdbcTemplate.update(sql, new Object[] { randomUUID, name });
		System.out.println(count);
		if (count == 0) {
			return false;
		} else {
			return true;
		}
	}

	public int getIdByEmail(String email) {

		String query = "select id from Users where email=?";
		Object[] obj = new Object[] { email };
		int id = jdbcTemplate.queryForObject(query, obj, Integer.class);

		return id;
	}

	public String getUserNameById(String randomUUID) {

		String query = "select name from Users where randomID=?";
		Object[] obj = new Object[] { randomUUID };
		String username = jdbcTemplate.queryForObject(query, obj, String.class);

		return username;
	}

	public boolean resetPassword(String email, String password) {
		int record = 0;
		String sqlUpdate = "UPDATE Users set password=? where email=?";
		record = jdbcTemplate.update(sqlUpdate, new Object[] { password, email });
		if (record == 0) {
			return false;
		}
		{
			return true;
		}

	}

	public boolean activeUser(User user) {
		int count = 0;

		String sql = "update Users set isActive = ? where email=?";
		count = jdbcTemplate.update(sql, new Object[] { user.getIsActive(), user.getEmail() });
		System.out.println(count);
		if (count == 0) {
			return false;
		} else {
			return true;
		}
	}

	@Override
	public User getUserByUIID(String randomUUID) {
		String sql = "select * from Users where randomID= ?";
		List<User> list = jdbcTemplate.query(sql, new Object[] { randomUUID }, new UserMapper());
		if (list.size() > 0) {
			System.out.println(list.get(0));
			return list.get(0);
		} else {
			return null;
		}

	}

	@Override
	public String getUserEmailId(String randomUUID) {

		String query = "select email from Users where randomID=?";
		Object[] obj = new Object[] { randomUUID };
		String email = jdbcTemplate.queryForObject(query, obj, String.class);

		return email;

	}

	@Override
	public User getUserById(int id) {
		String sql = "select * from Users where id= ?";
		List<User> list = jdbcTemplate.query(sql, new Object[] { id }, new UserMapper());
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
			user.setName(rs.getString("name"));
			user.setEmail(rs.getString("email"));
			user.setPassword(rs.getString("password"));
			user.setId(rs.getInt("id"));
			user.setActive(rs.getBoolean("isActive"));
			return user;

		}
	}

	@Override
	public User getOwnerEmail(int userId) {
		String sql = "select * from Users where id= ?";
		List<User> list = jdbcTemplate.query(sql, new Object[] { userId }, new UserMapper());
		if (list.size() > 0) {
			System.out.println(list.get(0));
			return list.get(0);
		} else {
			return null;
		}
	}

}
