package server.servlet;

import com.alibaba.fastjson.JSON;
import server.core.Login;
import server.core.Response;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class LoginServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        resp.setContentType("application/json");
        resp.setStatus(HttpServletResponse.SC_OK);

        Response response =
                Login.login(Utils.getFirst(req.getParameterMap()));

        resp.getWriter().write(JSON.toJSONString(response));

    }
}
