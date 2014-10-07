package mds.test.web;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.concurrent.atomic.AtomicLong;

/**
 * @author Dongsong
 */
@WebServlet(name = "TestServlet1", urlPatterns = {"/test/test1"}, asyncSupported = true)
public class TestServlet extends HttpServlet {
    private static final Logger log = LoggerFactory.getLogger(AsyncServlet.class);
    private static final AtomicLong lastCounter = new AtomicLong();
    private static final AtomicLong counter = new AtomicLong();
    private static final AtomicLong lastTime = new AtomicLong();

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String responseString = "AsyncContext Complete: ";
        java.io.PrintWriter writer = response.getWriter();
        writer.println(responseString);
        long count = counter.incrementAndGet();
        if (count % 3000 == 0) {
            long spentTime = System.nanoTime() - lastTime.get();
            if (spentTime > 1000000000) {
                long throughouput = count - lastCounter.get();
                lastCounter.set(count);
                lastTime.set(System.nanoTime());
                log.info("Response throughouput: " + (throughouput * 1000000000) / spentTime);
            }
        }
    }
}