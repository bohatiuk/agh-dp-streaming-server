package servlets;

import core.VideoManager;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

@WebServlet(name = "UploadVideoServlet")
public class UploadVideoServlet extends HttpServlet {
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


            if (VideoManager.onUploadVideo(username, videoname, VideoManager.VisibilityType.Public))
                response.setStatus(200); // 200 -> OK
            else
                response.setStatus(409); // 409 -> conflict, entry with username and videoname exists
        }
        else {
            response.setStatus(511); // 511 -> Network Authentication Required
        }

    }
}
