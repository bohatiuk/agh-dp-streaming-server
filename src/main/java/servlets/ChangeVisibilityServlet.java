package servlets;

import core.VideoManager;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

@WebServlet(name = "ChangeVisibilityServlet")
public class ChangeVisibilityServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doGet(request, response);
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");

        if (request.getSession().getAttribute("username") != null) {
            String username = (String) request.getSession().getAttribute("username");

            String videoname = request.getParameter("videoname");
            String type = request.getParameter("change-type");

            VideoManager.VisibilityType vtype = null;
            if (type.equals("private"))
                vtype = VideoManager.VisibilityType.Private;
            else if (type.equals("public"))
                vtype = VideoManager.VisibilityType.Public;

            if (videoname == null || type == null || vtype == null) {
                response.setStatus(400); // 400 -> Bad request, invalid req syntax
                return;
            }

            if (VideoManager.onChangeVisibility(username, videoname, vtype))
                response.setStatus(200); // 200 -> OK
            else
                response.setStatus(406); // 406 Not Acceptable


        }
        else {
            response.setStatus(511); // 511 -> Network Authentication Required
        }
    }
}
