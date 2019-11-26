package scissors.handlers;

import scissors.server.ScissorsServer;
import scissors.data.Login;
import scissors.data.Results;

import java.io.*;
import java.net.InetAddress;
import java.net.Socket;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Logger;

public class ResultHandler implements Runnable {

    private Socket socket;
    private Logger logger = Logger.getLogger("ResultHandler");

    public ResultHandler(Socket socket) {
        this.socket = socket;
    }

    @Override
    public void run() {

        logger.info("Processing... ");

        try (Socket s = socket) {
            InetAddress clientResultAddress = s.getInetAddress();
            logger.info("ResultClient: " + clientResultAddress.toString() + " connected");
            BufferedReader br = new BufferedReader(new InputStreamReader(s.getInputStream()));
            PrintWriter pw = new PrintWriter(new OutputStreamWriter(s.getOutputStream()), true);

            pw.println("Top 3 (with most wins): ");
            LinkedHashMap<Login, Results> sorted = sortByWins(ScissorsServer.currentGameResults);

            sorted.entrySet().stream().limit(3).forEach(pw::println);

        } catch (IOException ioe) {
            ioe.printStackTrace();
        }
    }

    private LinkedHashMap<Login, Results> sortByWins(ConcurrentHashMap<Login, Results> chm) {

        Comparator<Map.Entry<Login, Results>> valueComparator = (o1, o2) -> {
            Integer win1 = o1.getValue().getWin();
            Integer win2 = o2.getValue().getWin();
            return win1.compareTo(win2);
        };

        List<Map.Entry<Login, Results>> entryList = new ArrayList<>(chm.entrySet());
        entryList.sort(Collections.reverseOrder(valueComparator));
        LinkedHashMap<Login, Results> sorted = new LinkedHashMap<>(entryList.size());
        for (Map.Entry<Login, Results> entry : entryList) {
            sorted.put(entry.getKey(), entry.getValue());
        }

        return sorted;
    }
}
