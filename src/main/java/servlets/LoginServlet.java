package servlets;

import core.VideoManager;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

@WebServlet(name = "LoginServlet")
public class LoginServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");

        String username = request.getParameter("username");
        String userpass = request.getParameter("userpass");


        if (VideoManager.onLogin(username, userpass)) {
            HttpSession session = request.getSession();
            session.setAttribute("username", username); // session ends in 30 minutes
            System.out.println(session.getAttribute("username"));
            response.setStatus(200);
        }
        else {
            response.setStatus(404); // user not found
        }


    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }
}
