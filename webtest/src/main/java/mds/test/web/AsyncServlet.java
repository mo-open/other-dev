package mds.test.web;

import com.thistech.vex.common.m3u8.v2.model.tags.ExtM3uTag;
import org.apache.commons.configuration.ConfigurationUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.AsyncContext;
import javax.servlet.AsyncEvent;
import javax.servlet.AsyncListener;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * @author Dongsong
 */

@WebServlet(name = "AsyncServlet", urlPatterns = {"/as1"}, asyncSupported = true)
public class AsyncServlet extends HttpServlet {
    private static final Logger log = LoggerFactory.getLogger(AsyncServlet.class);

    final static Map<String, AsyncContext> contexts = new ConcurrentHashMap();

    final static StateChanger stateChanger = new StateChanger();
    final static Random random = new Random();

    public static class StateChanger implements Runnable {

        AtomicBoolean started = new AtomicBoolean(false);
        Thread thread = null;

        public StateChanger() {
            thread = new Thread(this);
        }

        public void start() {
            if (started.get()) return;
            started.set(true);
            this.thread.start();
        }

        @Override
        public void run() {
            while (true) {
                try {
                    int i = 0;
                    for (Map.Entry<String, AsyncContext> entry : contexts.entrySet()) {
                        try {
                            entry.getValue().complete();
                        } catch (Exception ex) {
                            log.error("", ex);
                            contexts.remove(entry.getKey());
                            continue;
                        }
                        contexts.remove(entry.getKey());
                        if (i++ > 200) {
                            break;
                        }
                    }

                    Thread.sleep(4000);

                } catch (Exception ex) {

                }
            }
        }
    }

    class TestListener implements AsyncListener {

        private int id;
        private boolean isTimeout = false;
        private int count = 0;

        public TestListener(int id) {
            this.id = id;
        }

        @Override
        public void onComplete(AsyncEvent asyncEvent) throws IOException {
            String response = "AsyncContext Complete: " + id;
            if (isTimeout) {
                response = "AsyncContext Timeout: " + id;
            }
            log.info(response);
            java.io.PrintWriter writer = asyncEvent.getSuppliedResponse().getWriter();
            writer.write(response);
            count++;
            if (count > 1) {
                log.error("ERROR:  twice COMPLETE ........ERR");
            }
        }

        @Override
        public void onTimeout(AsyncEvent asyncEvent) throws IOException {
            isTimeout = true;
            contexts.remove(id);
        }

        @Override
        public void onError(AsyncEvent asyncEvent) throws IOException {
            contexts.remove(id);
        }

        @Override
        public void onStartAsync(AsyncEvent asyncEvent) throws IOException {
            //To change body of implemented methods use File | Settings | File Templates.
        }
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        int id = random.nextInt();
        log.info("Request reach: " + id);
        AsyncContext asyncContext = request.startAsync(request, response);
        asyncContext.setTimeout(6000);
        ExtM3uTag tag = new ExtM3uTag();
        String definitionsFileName="HLSDefinitions.xml";
        log.info("--------------" + ConfigurationUtils.locate("HLSDefinitions.xml"));
        log.info("0------"+Thread.currentThread().getContextClassLoader().getResource(definitionsFileName));
        asyncContext.addListener(new TestListener(id), request, response);
        contexts.put(String.valueOf(id), asyncContext);
    }
}
