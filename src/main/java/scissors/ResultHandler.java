package scissors;

import scissors.results.Login;
import scissors.results.Results;

import java.io.*;
import java.net.InetAddress;
import java.net.Socket;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Logger;

public class ResultHandler implements Runnable {

    Socket socket;
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

            pw.println("Best players (with most wins): ");
            ScissorsServer.currentGameResults.forEach((login, results) ->
                    ScissorsServer.mergedGameResults.merge(login, results, (v1, v2) ->
                            new Results(v1.getWin() + v2.getWin(), v1.getLose() + v2.getLose(), v1.getDraw() + v2.getDraw())));

           LinkedHashMap<Login,Results> sorted = sortByWins(ScissorsServer.mergedGameResults);


            sorted.entrySet().stream().limit(3).forEach(pw::println);
//            results.clear();
//            ScissorsServer.currentGameResults.clear();
            // pw.println(sortedTop.entrySet());


        } catch (IOException ioe) {
            ioe.printStackTrace();
        }
    }
//
        public static LinkedHashMap<Login, Results> sortByWins(ConcurrentHashMap<Login, Results> chm) {

            Comparator<Map.Entry<Login, Results>> valueComparator = new Comparator<Map.Entry<Login, Results>>() {


                @Override
                public int compare(Map.Entry<Login, Results> o1, Map.Entry<Login, Results> o2) {
                    Integer win1 = o1.getValue().getWin();
                    Integer win2 = o2.getValue().getWin();
                    return win1.compareTo(win2);
                }
            };
//            ScissorsServer.sortedTop.forEach((login, results) -> ScissorsServer.gameResults.merge(login,results,(v1,v2)->
//                    new Results(v1.getWin()+v2.getWin(),v1.getLose()+v2.getLose(),v1.getDraw()+v2.getDraw())));

            List<Map.Entry<Login, Results>> entryList = new ArrayList<Map.Entry<Login, Results>>(chm.entrySet());
            Collections.sort(entryList, Collections.reverseOrder(valueComparator));
            LinkedHashMap<Login, Results> sorted = new LinkedHashMap<Login, Results>(entryList.size());
            for (Map.Entry<Login, Results> entry : entryList) {
                sorted.put(entry.getKey(), entry.getValue());
//                sorted.computeIfPresent(entry.getKey(),(login, results) -> results);
//                sorted.putIfAbsent(entry.getKey(),entry.getValue());
            }

            return sorted;
        }
}

//        return chm.entrySet()
//                .stream()
//                .sorted(Map.Entry.comparingByValue(Comparator.comparing(Results::getWin)))
//                .collect(Collectors.toMap(Map.Entry::getKey,Map.Entry::getValue,(e1, e2) -> e1, chm));
//
//}
//}
