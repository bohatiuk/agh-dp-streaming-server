package servlets;

import core.VideoManager;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet(name = "DeleteVideoServlet")
public class DeleteVideoServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doGet(request, response);
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");

        if (request.getSession().getAttribute("username") != null) {

            String username = (String) request.getSession().getAttribute("username");
            String videoname = request.getParameter("videoname");


            if (videoname == null) {
                response.setStatus(400); // 400 -> Bad request, invalid req syntax
                return;
            }


            if (VideoManager.onDeleteVideo(username, videoname))
                response.setStatus(200); // 200 -> OK
            else
                response.setStatus(404);
        }
        else {
            response.setStatus(511); // 511 -> Network Authentication Required
        }
    }
}
