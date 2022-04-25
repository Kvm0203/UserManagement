package user.management.application.web;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import user.management.application.bean.User;
import user.management.application.dao.UserDao;

/**
 * Servlet implementation class UserServlet
 */
@WebServlet("/")
public class UserServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private UserDao userDao;
   
	public void init() throws ServletException {
		userDao = new UserDao();
	}
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}


	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		String action=request.getServletPath();
		
		
		try {
			
			switch(action) 
			{
			  
			case "/new" :
				showNewForm(request,response);
				break;
				
			case "/insert" :
				insertUser(request,response);
				break;
				
			case "/delete" :
				deleteUser(request, response);
				break;
				
			case "/edit" :
				showEditForm(request, response);
				break;
				
			case "/update" :
				updateUser(request, response);
				break;
				
				default :
					listUser(request, response);
					break;
			}
		} catch (SQLException ex) {
			// TODO: handle exception
			  throw new ServletException(ex);
		}
	}
	
	
	// new user
	private void showNewForm(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		RequestDispatcher dispatcher=request.getRequestDispatcher("userform.jsp");
		dispatcher.forward(request, response);
	}
	
	// insert user
	
	private void insertUser(HttpServletRequest request, HttpServletResponse response) throws SQLException, IOException {
		
		String name=request.getParameter("name");
		String email=request.getParameter("email");
		String country=request.getParameter("country");
		
		User newUser=new User(name,email,country);
		
		userDao.insertUser(newUser);
		response.sendRedirect("list");
	}
	
	// delete user
	
	private void deleteUser(HttpServletRequest request, HttpServletResponse response) throws SQLException , IOException {
		
		int id=Integer.parseInt(request.getParameter("id"));
		try {
			userDao.deleteUser(id);
		}catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		response.sendRedirect("list");
	}
	
	// edit
	
	private void showEditForm(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException, SQLException {
		int id = Integer.parseInt(request.getParameter("id"));
		
		User existingUser;
		try {
			existingUser = userDao.selectUser(id);
			RequestDispatcher dispatcher=request.getRequestDispatcher("userform.jsp");
			request.setAttribute("user", existingUser);
			dispatcher.forward(request, response);
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
	}
	
	private void updateUser(HttpServletRequest request, HttpServletResponse response) throws SQLException, IOException {
		 
		int id = Integer.parseInt(request.getParameter("id"));
		String name=request.getParameter("name");
		String email=request.getParameter("email");
		String country = request.getParameter("country");
		
		User user = new User(id,name,email,country);
		
		userDao.updateUser(user);
		response.sendRedirect("list");
	}
	
	// default 
	
	private void listUser(HttpServletRequest request, HttpServletResponse response) throws SQLException, ServletException, IOException {
		
		try {
			List<User> listUser=userDao.selectAllUsers();
			request.setAttribute("listUser", listUser);
			RequestDispatcher dispatcher=request.getRequestDispatcher("userlist.jsp");
			dispatcher.forward(request, response);
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
	}

	
	
}
