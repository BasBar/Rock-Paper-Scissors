package scissors.threads;

import scissors.handlers.ResultHandler;

import java.io.IOException;
import java.net.BindException;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;

public class ResultThread implements Runnable {

    public ResultThread() {
    }

    @Override
    public void run() {
        ExecutorService executor = Executors.newFixedThreadPool(5);
        Logger logger = Logger.getLogger("ResultThread");
        logger.info("Result thread started");

        try (ServerSocket serverSocketResults = new ServerSocket()){

            serverSocketResults.bind(new InetSocketAddress("localhost",7070));
            while(true){
                Socket socketR;
                socketR = serverSocketResults.accept();
                ResultHandler rh = new ResultHandler(socketR);
                executor.submit(rh);
            }

        } catch (BindException be) {
            logger.warning("Unable to bind server");
            be.printStackTrace();
        } catch (IOException ie) {
            logger.warning("I/O error");
            ie.printStackTrace();
        } finally {
            executor.shutdown();
            try {
                if (!executor.awaitTermination(10, TimeUnit.SECONDS)) {
                    executor.shutdown();
                }
            } catch (InterruptedException ie) {
                ie.printStackTrace();
            }
        }
    }
}
