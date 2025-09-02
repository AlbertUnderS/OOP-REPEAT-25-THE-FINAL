package Server;

import dao.CharacterDAO;
import dto.CharacterDTO;
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
                }
                if(action.equals("GET_ALL")) {
                    out.println(dao.findAllCharactersJson());
                }
                if(action.equals("INSERT")) {
                    JSONObject charObj = jsonRequest.getJSONObject("character");

                    CharacterDTO c = new CharacterDTO(
                            charObj.getString("name"),
                            charObj.getInt("level"),
                            charObj.getInt("hp"),
                            (float) charObj.getDouble("attackPower")
                    );

                    boolean success = dao.insertCharacter(c);

                    if(success) {
                        // Return the inserted character as JSON
                        out.println(dao.findCharacterByIdJson(c.getId()));
                    } else {
                        out.println(new JSONObject().put("error", "Insert failed").toString());
                    }
                } else {
                    out.println(new JSONObject().put("error","Unknown action").toString());
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
