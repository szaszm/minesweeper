package tk.szaszm.minesweeper;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

/**
 * Created by marci on 2016.11.28..
 */
class Board extends JPanel {
    private ArrayList<Field> fields;
    private int boardWidth;
    private int boardHeight;
    private boolean ended;

    Board(int boardWidth, int boardHeight, FieldGraphicsProvider fieldGraphicsProvider) {
        super(new GridLayout(boardHeight, boardWidth));

        this.boardHeight = boardHeight;
        this.boardWidth = boardWidth;
        setMinimumSize(new Dimension(boardWidth*Field.SIZE, boardHeight*Field.SIZE));
        setPreferredSize(new Dimension(boardWidth*Field.SIZE, boardHeight*Field.SIZE));
        fields = new ArrayList<>();
        for (int y = 0; y < boardHeight; ++y) {
            for (int x = 0; x < boardWidth; x++) {
                Field field = new Field(x, y, fieldGraphicsProvider, this);
                fields.add(field);
                add(field);
            }
        }

        for (int i = 0; i < boardWidth * boardHeight; i++) {
            Field field = fields.get(i);
            Field topLeft = getFieldAt(field.getFieldX() - 1, field.getFieldY() - 1);
            Field topCenter = getFieldAt(field.getFieldX(), field.getFieldY() - 1);
            Field topRight = getFieldAt(field.getFieldX() + 1, field.getFieldY() - 1);
            Field middleLeft = getFieldAt(field.getFieldX() - 1, field.getFieldY());
            Field middleRight = getFieldAt(field.getFieldX() + 1, field.getFieldY());
            Field bottomLeft = getFieldAt(field.getFieldX() - 1, field.getFieldY() + 1);
            Field bottomCenter = getFieldAt(field.getFieldX(), field.getFieldY() + 1);
            Field bottomRight = getFieldAt(field.getFieldX() + 1, field.getFieldY() + 1);
            if(topLeft != null) field.setTopLeft(topLeft);
            if(topCenter != null) field.setTopCenter(topCenter);
            if(topRight != null) field.setTopRight(topRight);
            if(middleLeft != null) field.setMiddleLeft(middleLeft);
            if(middleRight != null) field.setMiddleRight(middleRight);
            if(bottomLeft != null) field.setBottomLeft(bottomLeft);
            if(bottomCenter != null) field.setBottomCenter(bottomCenter);
            if(bottomRight != null) field.setBottomRight(bottomRight);
        }
    }

    public Field getFieldAt(int id) {
        if (id < 0 || id > boardWidth * boardHeight) return null;
        return fields.get(id);
    }

    public Field getFieldAt(int x, int y) {
        if (x < 0 || y < 0 || x >= boardWidth || y >= boardHeight) return null;
        return getFieldAt(y * boardWidth + x);
    }

    public int getBoardWidth() {
        return boardWidth;
    }

    public int getBoardHeight() {
        return boardHeight;
    }

    void check() {
        if(bombRevealed()) {
            ended = true;
            JOptionPane.showMessageDialog(this, "Vesztettél!");
        }

        if(areBombsMarked() || allRevealed()) {
            ended = true;
            JOptionPane.showMessageDialog(this, "Nyertél!");
        }
    }

    private boolean areBombsMarked() {
        for (Field field: fields) {
            if((field.isBomb() && field.getFieldState() != FieldState.MARKED)
                    || (!field.isBomb() && field.getFieldState() == FieldState.MARKED)) {
                return false;
            }
        }
        return true;
    }

    private boolean allRevealed() {
        for(Field field: fields) {
            if(!field.isBomb() && field.getFieldState() != FieldState.REVEALED) {
                return false;
            }
        }
        return true;
    }

    private boolean bombRevealed() {
        for(Field field: fields) {
            if(field.isBomb() && field.getFieldState() == FieldState.REVEALED) return true;
        }

        return false;
    }

    public boolean isEnded() {
        return ended;
    }
}
