package scissors;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.logging.Logger;

public class ScissorsResultsClient {
    public static void main(String[] args) {

        Logger logger = Logger.getLogger("ResultsClientLogger");
        logger.info("Hello, im ResultsClient");

        Socket socketResultsClient = new Socket();
        try {
            socketResultsClient.connect(new InetSocketAddress("localhost",7070));
            BufferedReader br = new BufferedReader(new InputStreamReader(socketResultsClient.getInputStream(), "UTF-8"));
            System.out.println(br.readLine());
            for(int i =0;i<3;i++) {
                try {
                System.out.println(i+1 + ". " + br.readLine().replace("="," "));}
                catch (NullPointerException npe){                         // I suppress because I can't call method
                    npe.getSuppressed();                                  // replace on null(when less than 3 players)
                }
            }
            socketResultsClient.shutdownInput();

        }catch (IOException ie) {
            logger.warning("client service exception occured");
            ie.printStackTrace();
        }
    }
}
