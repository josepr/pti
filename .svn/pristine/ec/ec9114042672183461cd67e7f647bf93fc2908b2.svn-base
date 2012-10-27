import java.io.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.*;
import javax.servlet.http.*;

public class login extends HttpServlet {
    @Override
  public void doPost(HttpServletRequest req, HttpServletResponse res)
                    throws ServletException, IOException {
    res.setContentType("text/html");
    
        try {
            Class.forName("com.mysql.jdbc.Driver");
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(login.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    System.out.print("jsfkjdjfbedfb");
    Connection connection;
    connection = null;
    String option = req.getParameter("option");
    if(option.equals("0")){//login normal
        try
        {
            
           System.out.print("jsfkjdjfbedfb");
            connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/ptiproj", "root", "root");
            Statement statement = connection.createStatement();
            statement.setQueryTimeout(30);
            String user = req.getParameter("usr");
            String pw = req.getParameter("pw");
             System.out.print("    " + user + " " + pw);
            ResultSet rs = statement.executeQuery("select count(*) as rowCount from usuarios where id_usuario='"+user+"' and password='"+pw+"'");
            rs.next();
            if(rs.getInt("rowCount") !=1){
                res.sendRedirect("loginincorrecte.html");
            }
            else {
                 res.sendRedirect("menuass.html");
            }

	}
        catch(SQLException e)
        {
          System.err.println(e.getMessage());
        }   
        finally
        {
          try
          {
            if(connection != null)
              connection.close();
          }
          catch(SQLException e)
          {
            // connection close failed.
            System.err.println(e.getMessage());
          }
        }
    }
    else if(option.equals("1")){//login pagina d'admin
        try
        {
            connection = DriverManager.getConnection("jdbc:sqlite:/exemple.db");
            Statement statement = connection.createStatement();
            statement.setQueryTimeout(30);
            String user = req.getParameter("user");
            String pw = req.getParameter("password");
            ResultSet rs = statement.executeQuery("select count(*) as rowCount from usuarios where id_usuario='"+user+"' and password='"+pw+"' and admin='y'");
            rs.next();
            if(rs.getInt("rowCount") !=1){
                res.sendRedirect("menu_no_admin.html");
            }
            else{
                 res.sendRedirect("administracion.html");
            }

	}
        catch(SQLException e)
        {
          System.err.println(e.getMessage());
        }   
        finally
        {
          try
          {
            if(connection != null)
              connection.close();
          }
          catch(SQLException e)
          {
            // connection close failed.
            System.err.println(e.getMessage());
          }
        }
    }
  }
}
