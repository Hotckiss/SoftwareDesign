package ru.roguelike.models.objects.movable;

import com.googlecode.lanterna.input.KeyStroke;
import com.googlecode.lanterna.input.KeyType;
import com.googlecode.lanterna.screen.Screen;
import ru.roguelike.logic.GameModel;
import ru.roguelike.logic.Movable;
import ru.roguelike.logic.Move;
import ru.roguelike.models.Position;
import ru.roguelike.models.objects.base.AbstractGameObject;
import ru.roguelike.models.objects.base.AbstractGameParticipant;
import ru.roguelike.models.objects.map.Wall;

import java.io.IOException;
import java.util.List;

import static java.sql.DriverManager.println;

public class Player extends AbstractGameParticipant {

    public Player(Position position) {
        this.position = position;
        this.isAvailable = false;

        this.fullHealth = 100;
        this.health = 100;
        this.physicalDamage = 20;
        this.fireDamage = 0;
        this.freezeProbability = 0;
        this.fireProbability = 0;
        this.physicalDamageMultiplier = 1;
        this.fireDamageMultiplier = 1;
        this.regeneration = 5;
    }

    @Override
    public Character getDrawingFigure() {
        return 'P';
    }

    @Override
    public Move move(Screen screen, GameModel model) throws IOException {
        KeyStroke keyStroke = screen.readInput();

        if (keyStroke.getKeyType() == KeyType.Escape) {
            return Move.NONE;
        }

        List<List<AbstractGameObject>> field = model.getField();
        int x = position.getX();
        int y = position.getY();

        //TODO: apply artifact
        switch (keyStroke.getCharacter()) {
            case 'w':
                return (x > 0 && !(field.get(x - 1).get(y) instanceof Wall)) ? Move.TOP : Move.NONE;
            case 'a':
                return (y > 0 && !(field.get(x).get(y - 1) instanceof Wall)) ? Move.LEFT : Move.NONE;
            case 's':
                return (x + 1 < field.size() && !(field.get(x + 1).get(y) instanceof Wall)) ? Move.DOWN : Move.NONE;
            case 'd':
                return (y + 1 < field.get(0).size() && !(field.get(x).get(y + 1) instanceof Wall)) ? Move.RIGHT : Move.NONE;
            case 'e':
                return Move.NONE;
            case 'q':
                return Move.NONE;
        }

        return Move.NONE;
    }
}
