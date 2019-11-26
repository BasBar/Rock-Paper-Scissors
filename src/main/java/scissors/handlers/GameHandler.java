package scissors.handlers;

import scissors.server.ScissorsServer;
import scissors.data.Login;
import scissors.data.Move;

import java.io.*;
import java.net.InetAddress;
import java.net.Socket;
import java.util.Random;
import java.util.logging.Logger;

public class GameHandler implements Runnable {

    private Socket socket;
    private Logger logger = Logger.getLogger("GameHandler");

    public GameHandler(Socket socket) {
        this.socket = socket;
        logger.info("Thread active");
    }

    @Override
    public void run() {
        logger.info("processing... ");
        try (Socket s = socket) {
            InetAddress clientAddress = s.getInetAddress();
            logger.info("Client " + clientAddress.toString() + " connected");
            BufferedReader br = new BufferedReader(new InputStreamReader(s.getInputStream()));
            PrintWriter pw = new PrintWriter(new OutputStreamWriter(s.getOutputStream()), true);
            String clientLine;

            pw.println("Welcome in Rock Paper Scissors game! ");
            pw.println("Enter your login ");
            clientLine = br.readLine();
            Login login = new Login(clientLine);
            pw.println("Type rock, paper, scissors or quit");
            while (true) {
                clientLine = br.readLine();
                Move computerMove;
                Move playerMove;
                if (clientLine.toUpperCase().equals("QUIT")) {
                    pw.println("Goodbye!");
                    logger.info("Player leaves");
                    logger.info("finishing... ");
                    s.shutdownOutput();
                    s.shutdownInput();
                    break;
                } else if (clientLine.toUpperCase().equals("ROCK")
                        || (clientLine.toUpperCase().equals("PAPER"))
                        || (clientLine.toUpperCase().equals("SCISSORS"))
                ) {
                    logger.info("player typed " + clientLine);
                    computerMove = randomMove();
                    playerMove = getPlayerMove(clientLine);
                    logger.info("computer opponent typed: " + computerMove);
                    pw.println("Your: " + playerMove + " vs computer typed: " + computerMove);
                    String result = getResult(playerMove,computerMove);

                    if(result.equals("YOU WIN"))
                        ScissorsServer.playerWin(login);
                    else if (result.equals("YOU LOSE"))
                        ScissorsServer.playerLose(login);
                    else ScissorsServer.playerDraws(login);

                    pw.println(result);
                } else {
                    logger.info("Player typed " + clientLine);
                    pw.println("Type rock, paper, scissors or quit");
                }
            }
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }
    }

    private Move randomMove() {
        Random random = new Random();
        switch (random.nextInt(3)) {
            case 1:
                return Move.ROCK;
            case 2:
                return Move.PAPER;
        }
        return Move.SCISSORS;
    }

    private Move getPlayerMove(String playerMove){
        if(playerMove.toUpperCase().equals("ROCK"))
            return Move.ROCK;
        else if(playerMove.toUpperCase().equals("PAPER"))
            return Move.PAPER;
        else
            return Move.SCISSORS;
    }

    private String getResult(Move playerMove, Move computerMove){
        String result;
        if((playerMove.equals(Move.ROCK))&&(computerMove.equals(Move.PAPER))
            ||(playerMove.equals(Move.PAPER))&&(computerMove.equals(Move.SCISSORS))
                ||(playerMove.equals(Move.SCISSORS))&&(computerMove.equals(Move.ROCK)))
                result = "YOU LOSE";
        else if(playerMove.equals(computerMove))
            result = "DRAW";
        else result = "YOU WIN";
        return result;
    }
}
