package dto;

public class CharacterDTO {
    private int id;
    private String name;
    private int level;
    private int hp;
    private float attackPower;

    // Constructor
    public CharacterDTO(int id, String name, int level, int hp, float attackPower) {
        this.id = id;
        this.name = name;
        this.level = level;
        this.hp = hp;
        this.attackPower = attackPower;
    }

    // Overloaded constructor without id (for inserting new characters)
    public CharacterDTO(String name, int level, int hp, float attackPower) {
        this.name = name;
        this.level = level;
        this.hp = hp;
        this.attackPower = attackPower;
    }

    // Getters and Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public int getLevel() { return level; }
    public void setLevel(int level) { this.level = level; }

    public int getHp() { return hp; }
    public void setHp(int hp) { this.hp = hp; }

    public float getAttackPower() { return attackPower; }
    public void setAttackPower(float attackPower) { this.attackPower = attackPower; }

    @Override
    public String toString() {
        return String.format("Character [id=%d, name=%s, level=%d, hp=%d, attackPower=%.1f]",
                id, name, level, hp, attackPower);
    }
}
