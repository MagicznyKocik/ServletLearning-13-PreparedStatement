package pl.adamLupinski.servlet;

import javax.management.Query;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;

@WebServlet("/login")
public class LoginServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        /* Get and initialize data source */

        DataSource ds = null;

        try {
            ds = getDataSource();
        } catch (NamingException e){
            e.printStackTrace();
            response.sendError(500);
        }

        /* prepare query */
        final String query = "SELECT username, password FROM user WHERE username=? AND password=?";

        /* connect with db */

        try(Connection connection = ds.getConnection();
            PreparedStatement statement = connection.prepareStatement(query)){

            String username = request.getParameter("username");
            String password = request.getParameter("password");
            statement.setString(1, username);
            statement.setString(2, password);
            ResultSet resultSet = statement.executeQuery();


            if (resultSet.next()){
                String userFound = resultSet.getString("username");
                request.getSession().setAttribute("username", userFound );
                if ("admin".equals(userFound)){
                    request.getSession().setAttribute("privileges", "all");
                } else {
                    request.getSession().setAttribute("privileges", "view");
                }
            } else  {
                request.getSession().setAttribute("username", "stranger");
                request.getSession().setAttribute("privileges", "non");
            }

            request.getRequestDispatcher("result.jsp").forward(request, response);
        } catch (Exception e){
            e.printStackTrace();
            response.sendError(500);
        }

    }

    private DataSource getDataSource() throws NamingException {
        Context initialContext = new InitialContext();
        Context envContext = (Context) initialContext.lookup("java:comp/env");
        return (DataSource) envContext.lookup("jdbc/users");
    }


}
