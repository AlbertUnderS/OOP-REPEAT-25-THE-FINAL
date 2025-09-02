package main;

import dao.CharacterDAO;
import dao.CharacterDAOImpl;
import dto.CharacterDTO;

import java.util.List;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
        CharacterDAO dao = new CharacterDAOImpl();
        Scanner scanner = new Scanner(System.in);
        int choice;

        do {
            System.out.println("\n=== Paper Mario Character Menu ===");
            System.out.println("1. List all characters");
            System.out.println("2. View character by ID");
            System.out.println("0. Exit");
            System.out.print("Enter choice: ");
            choice = scanner.nextInt();
            scanner.nextLine(); // consume newline

            switch (choice) {
                case 1:
                    List<CharacterDTO> characters = dao.getAllCharacters();
                    characters.forEach(System.out::println);
                    break;
                case 2:
                    System.out.print("Enter character ID: ");
                    int id = scanner.nextInt();
                    scanner.nextLine();
                    CharacterDTO character = dao.getCharacterById(id);
                    System.out.println(character != null ? character : "Character not found");
                    break;

                case 0:
                    System.out.println("Exiting program.");
                    break;
                default:
                    System.out.println("Invalid choice. Try again.");
            }

        } while (choice != 0);

        scanner.close();
    }
}
