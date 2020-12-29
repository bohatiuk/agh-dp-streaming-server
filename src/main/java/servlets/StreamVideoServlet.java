package servlets;

import core.VideoManager;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Map;

@WebServlet(name = "StreamVideoServlet")
public class StreamVideoServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doGet(request, response);
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        if (request.getSession().getAttribute("username") != null) {
            String username = (String) request.getSession().getAttribute("username");
            String videoname = request.getParameter("videoname");

            if (videoname == null) {
                response.setStatus(400); // 400 -> Bad request, invalid req syntax
                return;
            }

            Map.Entry<Boolean, String> result = VideoManager.onStreamVideo(username, videoname);

            if (result.getKey())
                response.setStatus(200); // 200 -> OK
            else
                response.setStatus(404);

            PrintWriter out = response.getWriter();
            out.print(result.getValue());
            out.flush();
        } else {
            response.setStatus(511); // 511 -> Network Authentication Required
        }


    }
}
