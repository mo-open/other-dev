package mds.test.web;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author Dongsong
 */
@WebServlet(name = "AsyncServlet2", urlPatterns = {"/as2"}, asyncSupported = true)
public class AsyncServlet2 extends AsyncServlet {

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        stateChanger.start();
        super.doGet(request, response);
    }
}
