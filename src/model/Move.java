package model;

public interface Move {
    @Override
    String toString() ;

    short getTargetRow();

    short getTargetCol();

    short getValue();

    boolean isTargetEmpty();

    boolean isTargetFriendly();

    boolean isTargetEnemy();

    byte getInitial();

    byte getTarget();

    byte getTargetType();
}
