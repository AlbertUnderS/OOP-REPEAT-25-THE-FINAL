package dao;

import dto.CharacterDTO;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import java.util.HashSet;
import java.util.Set;

import org.json.JSONArray;
import org.json.JSONObject;

public class CharacterDAOImpl implements CharacterDAO {
    private final String URL = "jdbc:mysql://localhost:3306/paper_mario_db";
    private final String USER = "root"; // your DB username
    private final String PASSWORD = ""; // your DB password

    private Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }

    @Override
    public List<CharacterDTO> getAllCharacters() {
        List<CharacterDTO> characters = new ArrayList<>();
        String query = "SELECT * FROM characters";

        try (Connection conn = getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {

            while (rs.next()) {
                characters.add(new CharacterDTO(
                        rs.getInt("id"),
                        rs.getString("name"),
                        rs.getInt("level"),
                        rs.getInt("hp"),
                        rs.getFloat("attackPower")
                ));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return characters;
    }

    @Override
    public CharacterDTO getCharacterById(int id) {
        if (!characterIdCache.contains(id)) {
            System.out.println("ID not found in cache. Skipping DB query.");
            return null;
        }

        String query = "SELECT * FROM characters WHERE id = ?";
        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {

            pstmt.setInt(1, id);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return new CharacterDTO(
                        rs.getInt("id"),
                        rs.getString("name"),
                        rs.getInt("level"),
                        rs.getInt("hp"),
                        rs.getFloat("attackPower")
                );
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public boolean insertCharacter(CharacterDTO character) {
        String query = "INSERT INTO characters (name, level, hp, attackPower) VALUES (?, ?, ?, ?)";
        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {

            pstmt.setString(1, character.getName());
            pstmt.setInt(2, character.getLevel());
            pstmt.setInt(3, character.getHp());
            pstmt.setFloat(4, character.getAttackPower());
            int rows = pstmt.executeUpdate();

            if (rows > 0) {
                ResultSet rs = pstmt.getGeneratedKeys();
                if (rs.next()) {
                    character.setId(rs.getInt(1));
                    characterIdCache.add(character.getId()); // update cache
                }
                return true;
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public boolean updateCharacter(CharacterDTO character) {
        String query = "UPDATE characters SET name = ?, level = ?, hp = ?, attackPower = ? WHERE id = ?";
        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {

            pstmt.setString(1, character.getName());
            pstmt.setInt(2, character.getLevel());
            pstmt.setInt(3, character.getHp());
            pstmt.setFloat(4, character.getAttackPower());
            pstmt.setInt(5, character.getId());
            return pstmt.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public boolean deleteCharacter(int id) {
        String query = "DELETE FROM characters WHERE id = ?";
        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {

            pstmt.setInt(1, id);
            boolean deleted = pstmt.executeUpdate() > 0;
            if (deleted) {
                characterIdCache.remove(id); // remove from cache
            }
            return deleted;

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public List<CharacterDTO> findCharactersByFilter(Predicate<CharacterDTO> filter) {
        List<CharacterDTO> all = getAllCharacters(); // get all characters from DB
        return all.stream()
                .filter(filter)           // keep only those matching the condition
                .collect(Collectors.toList());
    }

    private Set<Integer> characterIdCache = new HashSet<>();

    public CharacterDAOImpl() {
        populateCache();
    }

    private void populateCache() {
        characterIdCache.clear();
        for (CharacterDTO c : getAllCharacters()) {
            characterIdCache.add(c.getId());
        }
    }




    @Override
    public String findAllCharactersJson() {
        List<CharacterDTO> all = getAllCharacters();
        JSONArray jsonArray = new JSONArray();

        for (CharacterDTO c : all) {
            JSONObject obj = new JSONObject();
            obj.put("id", c.getId());
            obj.put("name", c.getName());
            obj.put("level", c.getLevel());
            obj.put("hp", c.getHp());
            obj.put("attackPower", c.getAttackPower());
            jsonArray.put(obj);
        }

        return jsonArray.toString(); // returns JSON string of all characters
    }



}
