package dao;

import dto.CharacterDTO;
import java.util.List;

public interface CharacterDAO {
    List<CharacterDTO> getAllCharacters();
    CharacterDTO getCharacterById(int id);
    boolean insertCharacter(CharacterDTO character);
    boolean updateCharacter(CharacterDTO character);
    boolean deleteCharacter(int id);
}
