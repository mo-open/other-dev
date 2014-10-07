package mds.test.web;

import org.apache.commons.lang3.StringUtils;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

/**
 * @author Dongsong
 */
@WebServlet(name = "TestManifestGet", urlPatterns = {"/test/manifestGet"}, asyncSupported = true)
public class TestManifestGet extends HttpServlet {
    private static ManifestManager manifestManager = ManifestManager.instance();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String manifest = manifestManager.getManifest();
        java.io.PrintWriter writer = resp.getWriter();
        writer.write(manifest);
        // writer.flush();
    }
}
