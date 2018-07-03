package server.servlet;

import com.alibaba.fastjson.JSON;
import server.core.Registration;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Map;

public class RegistrationServlet extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        resp.setContentType("application/json");
        resp.setStatus(HttpServletResponse.SC_OK);

        Map<String, String> response =
                Registration
                        .register(Utils.getFirst(req.getParameterMap()));
        resp.getWriter().write(JSON.toJSONString(response));
    }
}
