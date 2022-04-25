package user.management.application.dao;

import java.sql.*;
import java.util.*;

import user.management.application.bean.User;

public class UserDao {

	private String jdbcURL="jdbc:mysql://localhost:3306/userdb?useSSL=false";
	private String jdbcUsername="root";
	private String jdbcPassword="12345";
	private String jdbcDriver="com.mysql.jdbc.Driver";
	
	private static final String Insert_Users = "insert into users"+ " (name,email,country) values "+ "(?,?,?);";
	private static final String Select_User_By_ID = "select id,name,email,country from usesr where id=?";
	private static final String Select_All_Users="select * from users";
	private static final String Delete_User_By_ID="delete from users where id=?";
	private static final String Update_User_By_ID= " update users set name=?, email=?, country=?  where id=?";
	public UserDao() {
	}
	
	
	protected Connection getConnection()
	{
		Connection connection = null;
		try {
			  Class.forName(jdbcDriver);
			  connection=DriverManager.getConnection(jdbcURL, jdbcUsername, jdbcPassword);
		}catch (SQLException e) {
			// TODO: handle exception
			e.printStackTrace();
		}catch (ClassNotFoundException e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		 return connection;
		
		}
	
	//insert user
	public void insertUser(User user) throws SQLException {
		
		try(Connection connection =getConnection(); PreparedStatement preparedStatement=connection.prepareStatement(Insert_Users)) {
			preparedStatement.setString(1, user.getName());
			preparedStatement.setString(2, user.getEmail());
			preparedStatement.setString(3, user.getCountry());
			System.out.println(preparedStatement);
			preparedStatement.executeUpdate();
		} catch (SQLException e) {
			// TODO: handle exception
			printSQLException(e);
		}
	}

	//select user by id
	public User selectUser(int id) {
		User user=null;
		try(Connection connection=getConnection(); PreparedStatement preparedStatement=connection.prepareStatement(Select_User_By_ID);) {
			preparedStatement.setInt(1, id);
			System.out.println(preparedStatement);
			
			ResultSet resultSet=preparedStatement.executeQuery();
			
			while(resultSet.next()) {
				String name=resultSet.getString("name");
				String email=resultSet.getString("email");
				String country=resultSet.getString("country");
				user =new User(id,name,email,country);
			}
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			printSQLException(e);
		}
		return user;
	}
	
	//select all users
	public List<User> selectAllUsers(){
		//using try-with-resources to avoid closing resources(boiler plate code)
		List<User> users=new ArrayList<>();
		try(Connection connection=getConnection(); PreparedStatement preparedStatement=connection.prepareStatement(Select_All_Users);){
			System.out.println(preparedStatement);
			ResultSet resultSet=preparedStatement.executeQuery();
			while(resultSet.next()) {
				int id=resultSet.getInt("id");
				String name=resultSet.getString("name");
				String email=resultSet.getString("email");
				String country=resultSet.getString("country");
				users.add(new User(id,name,email,country));
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			printSQLException(e);
		}
		return users;
		
	}
	//update user by id
	public boolean updateUser(User user) throws SQLException {
		boolean rowUpdated;
		try(Connection connection=getConnection(); PreparedStatement preparedStatement=connection.prepareStatement(Update_User_By_ID);){
			System.out.println("Updated User :"+ preparedStatement);
			preparedStatement.setString(1, user.getName());
			preparedStatement.setString(2, user.getEmail());
			preparedStatement.setString(3, user.getCountry());
			preparedStatement.setInt(4, user.getId());
			
			rowUpdated = preparedStatement.executeUpdate() >0;
			
		} 
		return rowUpdated;
	}
	
	//delete user by id
	
	public boolean deleteUser(int id) throws SQLException {
		boolean rowDeleted;
		try(Connection connection=getConnection(); PreparedStatement preparedStatement=connection.prepareStatement(Delete_User_By_ID);){
			preparedStatement.setInt(1, id);
			
			rowDeleted=preparedStatement.executeUpdate() > 0;
		}
		return rowDeleted;
	}
	
	private void printSQLException(SQLException ex) {
		// TODO Auto-generated method stub
		for(Throwable e: ex) {
			if(e instanceof SQLException) {
				e.printStackTrace(System.err);
				System.err.println("SQL State :"+ ((SQLException) e).getSQLState());
				System.err.println("Error code :"+ ((SQLException) e).getErrorCode());
				System.err.println("Message :" +e.getMessage());
				Throwable t =ex.getCause();
				while (t!= null) {
					System.out.println("Cause :" +t);
					t=t.getCause();
				}
			}
		}
	}
	
}
