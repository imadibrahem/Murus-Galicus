package model.move;

import java.util.Objects;

public class Move {

    private final short value;

    public Move(short value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return (value >> 7) + "=>" + getDirection() + "(" +getTargetType() + ") | ";
    }

    public short getValue() {
        return value;
    }

    public int getDirection() {
        return (value >> 3) & 15;
    }

    public int getInitialLocation(boolean isBlue) {
        return isBlue ? (value >> 7)  : 55 - (value >> 7);
    }

    public int getInitialRow(boolean isBlue) {
        int location = getInitialLocation(isBlue);
        return 7 - (location / 8);
    }

    public int getInitialCol(boolean isBlue) {
        int location = getInitialLocation(isBlue);
        return (location % 8) + 1;
    }

    public int getTargetType() {
        return value & 7;
    }

    public boolean isTargetEmpty() {
        return getTargetType() == 0;
    }

    public boolean isTargetNearFriendly() {
        return getTargetType() == 1;
    }

    public boolean isTargetFarFriendly() {
        return getTargetType() == 2;
    }

    public boolean isTargetBothFriendly() {
        return getTargetType() == 3;
    }

    public boolean isTargetEnemy() {
        return getTargetType() == 4;
    }

    /*

    public int getTargetLocation() {
        // TODO: 10/19/2024
        return 0;
    }

    public int getTargetRow() {
        // TODO: 10/19/2024
        return 0;
    }

    public int getTargetCol() {
        // TODO: 10/19/2024
        return 0;
    }

 */


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Move that = (Move) o;
        return value == that.value;
    }

    @Override
    public int hashCode() {
        return Objects.hash(value);
    }
}
