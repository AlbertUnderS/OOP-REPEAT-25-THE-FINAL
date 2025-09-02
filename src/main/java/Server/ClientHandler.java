package Server;

import dao.CharacterDAO;
import org.json.JSONObject;
import java.net.Socket;
import java.io.*;

public class ClientHandler implements Runnable {

    private Socket socket;
    private CharacterDAO dao;

    public ClientHandler(Socket socket, CharacterDAO dao) {
        this.socket = socket;
        this.dao = dao;
    }

    @Override
    public void run() {
        try (BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
             PrintWriter out = new PrintWriter(socket.getOutputStream(), true)) {

            String request;
            while ((request = in.readLine()) != null) {
                JSONObject jsonRequest = new JSONObject(request);
                String action = jsonRequest.getString("action");

                if(action.equals("GET_BY_ID")) {
                    int id = jsonRequest.getInt("id");
                    String characterJson = dao.findCharacterByIdJson(id);
                    out.println(characterJson);
                } else {
                    out.println(new JSONObject().put("error","Unknown action").toString());
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
