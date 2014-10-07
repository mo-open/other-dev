package mds.test.web;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Enumeration;

/**
 * @author Dongsong
 */
@WebServlet(name = "TestRequestURL", urlPatterns = {"/test/request"}, asyncSupported = true)
public class TestRequestURL extends HttpServlet {
    private static final Logger log = LoggerFactory.getLogger(TestRequestURL.class);

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("RequestMethod:").append(request.getMethod()).append("\r\n");
        stringBuilder.append("RequestURL:").append(request.getRequestURL()).append("\r\n");
        stringBuilder.append("ServletPath:").append(request.getServletPath()).append("\r\n");
        stringBuilder.append("RequestURI:").append(request.getRequestURI()).append("\r\n");
        stringBuilder.append("ContextPath:").append(request.getContextPath()).append("\r\n");
        stringBuilder.append("RemoteUser:").append(request.getRemoteUser()).append("\r\n");
        stringBuilder.append("PathInfo:").append(request.getPathInfo()).append("\r\n");
        stringBuilder.append("QueryString:").append(request.getQueryString()).append("\r\n");
        stringBuilder.append("RemoteAddr:").append(request.getRemoteAddr()).append("\r\n");
        stringBuilder.append("RemoteHost:").append(request.getRemoteHost()).append("\r\n");
        stringBuilder.append("RemotePort:").append(request.getRemotePort()).append("\r\n");
        stringBuilder.append("Scheme:").append(request.getScheme()).append("\r\n");
        stringBuilder.append("ServerName:").append(request.getServerName()).append("\r\n");
        stringBuilder.append("ServerPort:").append(request.getServerPort()).append("\r\n");
        stringBuilder.append("Headers---: \r\n");
        for (Enumeration<String> e = request.getHeaderNames(); e.hasMoreElements(); ) {
            String headerName = e.nextElement();
            stringBuilder.append(headerName)
                    .append(":").append(request.getHeader(headerName)).append("\r\n");
        }
        log.info(stringBuilder.toString());
        java.io.PrintWriter writer = response.getWriter();
        writer.write(stringBuilder.toString());
    }
}
