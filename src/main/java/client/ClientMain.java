package client;

import org.json.JSONObject;
import java.net.Socket;
import java.io.*;
import java.util.Scanner;

public class ClientMain {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        try (Socket socket = new Socket("localhost", 12345);
             BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
             PrintWriter out = new PrintWriter(socket.getOutputStream(), true)) {

            int choice;
            do {
                System.out.println("\n=== Paper Mario Client Menu ===");
                System.out.println("1. Display character by ID");
                System.out.println("2. Display all characters");
                System.out.println("3. Add a character");
                System.out.println("0. Exit");
                System.out.print("Choice: ");
                choice = scanner.nextInt();
                scanner.nextLine();

                JSONObject request = new JSONObject();

                switch(choice) {
                    case 1:
                        System.out.print("Enter ID: ");
                        int id = scanner.nextInt();
                        scanner.nextLine();
                        request.put("action","GET_BY_ID");
                        request.put("id", id);
                        out.println(request.toString());
                        String response = in.readLine();
                        JSONObject jsonResponse = new JSONObject(response);
                        if(jsonResponse.has("error")) {
                            System.out.println("Error: " + jsonResponse.getString("error"));
                        } else {
                            System.out.println("Character Details:");
                            System.out.println("ID: " + jsonResponse.getInt("id"));
                            System.out.println("Name: " + jsonResponse.getString("name"));
                            System.out.println("Level: " + jsonResponse.getInt("level"));
                            System.out.println("HP: " + jsonResponse.getInt("hp"));
                            System.out.println("Attack Power: " + jsonResponse.getDouble("attackPower"));
                        }
                        break;
                    case 2:
                        JSONObject requestAll = new JSONObject();
                        requestAll.put("action", "GET_ALL");
                        out.println(requestAll.toString());

                        String responseAll = in.readLine();
                        org.json.JSONArray array = new org.json.JSONArray(responseAll);

                        System.out.println("All Characters:");
                        for (int i = 0; i < array.length(); i++) {
                            JSONObject c = array.getJSONObject(i);
                            System.out.println("ID: " + c.getInt("id") +
                                    ", Name: " + c.getString("name") +
                                    ", Level: " + c.getInt("level") +
                                    ", HP: " + c.getInt("hp") +
                                    ", Attack: " + c.getDouble("attackPower"));
                        }
                        break;
                    case 3:
                        JSONObject newChar = new JSONObject();

                        System.out.print("Name: ");
                        newChar.put("name", scanner.nextLine());

                        System.out.print("Level: ");
                        newChar.put("level", scanner.nextInt());

                        System.out.print("HP: ");
                        newChar.put("hp", scanner.nextInt());

                        System.out.print("Attack Power: ");
                        newChar.put("attackPower", scanner.nextFloat());
                        scanner.nextLine(); // consume newline

                        JSONObject requestInsert = new JSONObject();
                        requestInsert.put("action", "INSERT");
                        requestInsert.put("character", newChar);

                        out.println(requestInsert.toString());

                        String insertResponse = in.readLine();
                        JSONObject insertedChar = new JSONObject(insertResponse);

                        if(insertedChar.has("error")) {
                            System.out.println("Error: " + insertedChar.getString("error"));
                        } else {
                            System.out.println("New character added:");
                            System.out.println("ID: " + insertedChar.getInt("id"));
                            System.out.println("Name: " + insertedChar.getString("name"));
                            System.out.println("Level: " + insertedChar.getInt("level"));
                            System.out.println("HP: " + insertedChar.getInt("hp"));
                            System.out.println("Attack Power: " + insertedChar.getDouble("attackPower"));
                        }
                        break;


                    case 0:
                        System.out.println("Exiting.");
                        break;
                    default:
                        System.out.println("Invalid choice.");
                }

            } while(choice != 0);

        } catch(Exception e) {
            e.printStackTrace();
        }

        scanner.close();
    }
}
