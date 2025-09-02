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
            System.out.println("3. Delete character");
            System.out.println("4. Add new character");
            System.out.println("5. View character(s) above a certain level");
            System.out.println("6. retrive all characters by json");
            System.out.println("7. retrieve a single character by json");
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
                case 3:
                    System.out.print("Enter ID to delete: ");
                    int deleteId = scanner.nextInt();
                    scanner.nextLine();
                    boolean deleted = dao.deleteCharacter(deleteId);
                    System.out.println(deleted ? "Character deleted!" : "Delete failed.");
                    break;
                case 4:
                    System.out.print("Name: ");
                    String name = scanner.nextLine();
                    System.out.print("Level: ");
                    int level = scanner.nextInt();
                    System.out.print("HP: ");
                    int hp = scanner.nextInt();
                    System.out.print("Attack Power: ");
                    float attack = scanner.nextFloat();
                    scanner.nextLine();
                    boolean inserted = dao.insertCharacter(new CharacterDTO(name, level, hp, attack));
                    System.out.println(inserted ? "Character added!" : "Failed to add character.");
                    break;
                case 5:
                    System.out.print("Enter minimum level to filter: ");
                    int minLevel = scanner.nextInt();
                    scanner.nextLine();

                    List<CharacterDTO> filtered = dao.findCharactersByFilter(c -> c.getLevel() >= minLevel);

                    if (filtered.isEmpty()) {
                        System.out.println("No characters match the filter.");
                    } else {
                        filtered.forEach(System.out::println);
                    }
                    break;
                case 6: // JSON all characters
                    System.out.println(dao.findAllCharactersJson());
                    break;
                case 7: // JSON single character
                    System.out.print("Enter character ID: ");
                    int jsonId = scanner.nextInt();
                    scanner.nextLine();
                    System.out.println(dao.findCharacterByIdJson(jsonId));
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
