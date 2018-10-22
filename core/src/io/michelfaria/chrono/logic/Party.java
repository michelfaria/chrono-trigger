package io.michelfaria.chrono.logic;

import com.badlogic.gdx.utils.Array;
import io.michelfaria.chrono.actor.PartyCharacter;

public class Party {
    private final Array<PartyCharacter> characters = new Array<>(PartyCharacter.class);

    public Party() {
    }

    public void add(PartyCharacter character) {
        characters.add(character);
        refresh();
    }

    public void remove(PartyCharacter character) {
        characters.removeValue(character, true);
        refresh();
    }

    public void refresh() {
        for (int i = 0; i < characters.size; i++) {
            PartyCharacter character = characters.get(i);
            if (i == 0) {
                character.setInputHandler(character.new InputHandler());
            } else {
                character.setInputHandler(null);
            }
        }
    }

    public int indexOf(PartyCharacter character) {
        return characters.indexOf(character, true);
    }

    public PartyCharacter getLeader() {
        return characters.size == 0 ? null : characters.get(0);
    }

    public Array<PartyCharacter> getCharacters() {
        return characters;
    }
}
