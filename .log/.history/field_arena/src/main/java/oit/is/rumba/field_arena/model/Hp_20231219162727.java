package oit.is.rumba.field_arena.model;

public class Hp {
  int id;
  int roomId;
  String userName;
  int hp;
  boolean attackFlag;
  int attackPoint;

  final int defaultHp = 30;



  public Hp(boolean attackFlag) {
    this.attackFlag = attackFlag;
  }

  public int getAttackPoint() {
    return attackPoint;
  }

  public void setAttackPoint(int attackPoint) {
    this.attackPoint = attackPoint;
  }

  public int getDefaultHp() {
    return defaultHp;
  }

  public int getId() {
    return id;
  }

  public void setId(int id) {
    this.id = id;
  }

  public String getUserName() {
    return userName;
  }

  public void setUserName(String userName) {
    this.userName = userName;
  }

  public int getRoomId() {
    return roomId;
  }

  public void setRoomId(int roomId) {
    this.roomId = roomId;
  }

  public int getHp() {
    return hp;
  }

  public void setHp(int hp) {
    this.hp = hp;
  }

  public void minusHp(int strong) {
    this.hp-=strong;
  }

  public void plusHp(int healPoint) {
    this.hp+=healPoint;
  }

  public void initHp() {
    this.hp = defaultHp;
  }

  public boolean isAttackFlag() {
    return attackFlag;
  }

  public void setAttackFlag(boolean attackFlag) {
    this.attackFlag = attackFlag;
  }
}
