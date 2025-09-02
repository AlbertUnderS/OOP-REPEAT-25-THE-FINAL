package Server;

import dao.CharacterDAO;
import dao.CharacterDAOImpl;
import java.net.ServerSocket;
import java.net.Socket;

public class ServerMain {
    public static void main(String[] args) {
        int port = 12345;
        try (ServerSocket serverSocket = new ServerSocket(port)) {
            System.out.println("Server listening on port " + port);
            CharacterDAO dao = new CharacterDAOImpl();

            while (true) {
                Socket clientSocket = serverSocket.accept();
                new Thread(new ClientHandler(clientSocket, dao)).start();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
