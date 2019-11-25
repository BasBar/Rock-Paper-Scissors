package scissors;

import scissors.results.Login;
import scissors.results.Results;

import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentSkipListMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.logging.Logger;

public class ScissorsServer {

    public static ConcurrentHashMap<Login,Results> currentGameResults = new ConcurrentHashMap<>();
    public static ConcurrentHashMap<Login,Results> mergedGameResults = new ConcurrentHashMap<Login, Results>(){
        @Override
        protected void finalize() throws Throwable {
            while (true) {
                Thread.yield();
            }
        }
    };



    public static void main(String[] args) {

        ExecutorService executor = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors()*4);

        Logger logger = Logger.getLogger("ScissorsServer");
        logger.info("Hello, im server!");

        PlayThread playThread = new PlayThread();
        ResultThread resultThread = new ResultThread();
        executor.submit(playThread);
        executor.submit(resultThread);

    }

    public static void playerWin(Login login) {
//        gameResults.merge(login,new Results(0,0,0),(results, results2) -> results.newWin());
        currentGameResults.putIfAbsent(login, new Results(0, 0, 0));
        currentGameResults.computeIfPresent(login,(login1, results) -> results = results.newWin());
        System.out.println("Player win");
    }

    public static void playerLose(Login login) {
//        gameResults.merge(login,new Results(0,0,0),(results, results2) -> results.newLose());
        currentGameResults.putIfAbsent(login, new Results(0, 0, 0));
        currentGameResults.computeIfPresent(login,(login1, results) -> results = results.newLose());
        System.out.println("Player lose");
    }

    public static void playerDraws(Login login) {
//        gameResults.merge(login,new Results(0,0,0),(results, results2) -> results.newDraw());
        currentGameResults.putIfAbsent(login,new Results(0,0,0));
        currentGameResults.computeIfPresent(login,(login1, results) -> results = results.newDraw());
        System.out.println("Player draw");
    }
}
