package model;

import java.util.Objects;

public class Move {

    private final short value;

    public Move(short value) {
        this.value = value;
    }

    @Override
    public String toString() {
        // TODO: 10/19/2024
        return "";
    }

    public short getTargetRow() {
        // TODO: 10/19/2024
        return 0;
    }

    public short getTargetCol() {
        // TODO: 10/19/2024
        return 0;
    }

    public short getValue() {
        // TODO: 10/19/2024
        return 0;
    }

    public boolean isTargetEmpty() {
        // TODO: 10/19/2024
        return false;
    }

    public boolean isTargetFriendly() {
        // TODO: 10/19/2024
        return false;
    }

    public boolean isTargetEnemy() {
        // TODO: 10/19/2024
        return false;
    }

    public byte getInitial() {
        // TODO: 10/19/2024
        return 0;
    }

    public byte getTarget() {
        // TODO: 10/19/2024
        return 0;
    }

    public byte getTargetType() {
        // TODO: 10/19/2024
        return 0;
    }

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
