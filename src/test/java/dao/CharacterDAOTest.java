package dao;

import dao.CharacterDAO;
import dao.CharacterDAOImpl;
import dto.CharacterDTO;
import org.junit.jupiter.api.*;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class CharacterDAOTest {

    private static CharacterDAO dao;

    @BeforeAll
    static void setup() {
        dao = new CharacterDAOImpl();
    }

    @Test
    @Order(1)
    void testGetAllCharacters() {
        List<CharacterDTO> characters = dao.getAllCharacters();
        assertNotNull(characters);
        assertTrue(characters.size() > 0);
    }

    @Test
    @Order(2)
    void testInsertAndGetCharacter() {
        CharacterDTO newChar = new CharacterDTO("TestChar", 5, 20, 3.5f);
        boolean inserted = dao.insertCharacter(newChar);
        assertTrue(inserted);
        assertTrue(newChar.getId() > 0);

        CharacterDTO retrieved = dao.getCharacterById(newChar.getId());
        assertNotNull(retrieved);
        assertEquals("TestChar", retrieved.getName());

        // Clean up
        dao.deleteCharacter(newChar.getId());
    }

    @Test
    @Order(3)
    void testDeleteCharacter() {
        CharacterDTO tempChar = new CharacterDTO("TempChar", 3, 15, 2.0f);
        dao.insertCharacter(tempChar);
        int id = tempChar.getId();
        assertTrue(dao.deleteCharacter(id));
        assertNull(dao.getCharacterById(id));
    }

    @Test
    @Order(4)
    void testFindCharactersByFilter() {
        List<CharacterDTO> filtered = dao.findCharactersByFilter(c -> c.getLevel() >= 5);
        assertNotNull(filtered);
        filtered.forEach(c -> assertTrue(c.getLevel() >= 5));
    }

    @Test
    @Order(5)
    void testFindAllCharactersJson() {
        String json = dao.findAllCharactersJson();
        assertNotNull(json);
        assertTrue(json.startsWith("[") && json.endsWith("]"));
    }

    @Test
    @Order(6)
    void testFindCharacterByIdJson() {
        CharacterDTO char1 = dao.getAllCharacters().get(0);
        String json = dao.findCharacterByIdJson(char1.getId());
        assertNotNull(json);
        assertTrue(json.contains(char1.getName()));

        String jsonError = dao.findCharacterByIdJson(99999);
        assertTrue(jsonError.contains("error"));
    }
}
