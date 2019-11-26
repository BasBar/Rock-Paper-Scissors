package scissors.clients;

import java.io.*;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;
import java.util.logging.Logger;

public class ScissorsClient {
    public static void main(String[] args) {

        Logger logger = Logger.getLogger("ScissorsServer");
        logger.info("Hello, im client!");

        Socket socketClient = new Socket();

        try {
            socketClient.connect(new InetSocketAddress("localhost", 9090));

            BufferedReader br = new BufferedReader(new InputStreamReader(socketClient.getInputStream(), StandardCharsets.UTF_8));
            PrintWriter pw = new PrintWriter(new OutputStreamWriter(socketClient.getOutputStream(), StandardCharsets.UTF_8),true);
            Scanner s = new Scanner(System.in);

            String clientLine;
            String serverLine;

            serverLine = br.readLine();
            logger.info(serverLine);
            serverLine = br.readLine();
            logger.info(serverLine);

            pw.println(s.nextLine());
            serverLine = br.readLine();
            logger.info(serverLine);

            while(true){
                clientLine = s.nextLine();
                if(clientLine.toUpperCase().equals("QUIT")){
                    pw.println(clientLine);
                    logger.info(br.readLine());
                    socketClient.shutdownInput();
                    socketClient.shutdownOutput();
                    break;
                }
                else if(clientLine.toUpperCase().equals("ROCK")
                        || (clientLine.toUpperCase().equals("PAPER"))
                        || (clientLine.toUpperCase().equals("SCISSORS"))){
                    pw.println(clientLine);
                    logger.info(br.readLine());
                    logger.info(br.readLine());
                }
                else{
                    pw.println(clientLine);
                    logger.info(br.readLine());
                }

            }

        } catch (IOException ie) {
            logger.warning("client service exception occured");
            ie.printStackTrace();
        }
    }
}
