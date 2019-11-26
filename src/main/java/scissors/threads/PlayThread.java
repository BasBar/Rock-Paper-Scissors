package scissors.threads;

import scissors.handlers.GameHandler;

import java.io.IOException;
import java.net.BindException;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;

public class PlayThread implements Runnable {

    public PlayThread() {
    }

    @Override
    public void run() {

        ExecutorService executor = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors()*4);

        Logger logger = Logger.getLogger("ScissorsServer");
        logger.info("Play thread started");

        try (ServerSocket serverSocket = new ServerSocket()) {
            serverSocket.bind(new InetSocketAddress("localhost", 9090));
            while (true) {
                Socket socket;
                socket = serverSocket.accept();
                GameHandler handler = new GameHandler(socket);
                executor.submit(handler);
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

