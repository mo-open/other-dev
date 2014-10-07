package mds.test.web;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author Dongsong
 */
@WebServlet(name = "TestManifestGet", urlPatterns = {"/test/manifestSet"}, asyncSupported = true)
public class TestManifestSet extends HttpServlet {
    private static final Logger log = LoggerFactory.getLogger(TestManifestSet.class);
    private static ManifestManager manifestManager = ManifestManager.instance();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        int length = 1024;
        boolean compress, defaultCompress;
        try {
            length = Integer.parseInt(req.getParameter("len"));
            compress = Boolean.parseBoolean(req.getParameter("compress"));
            defaultCompress = Boolean.parseBoolean(req.getParameter("default"));
        } catch (Exception ex) {
            log.error("Failed to get parameters", ex);
            return;
        }
        manifestManager.set(length, compress, defaultCompress);
        // writer.flush();
    }
}
