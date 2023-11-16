package oit.is.rumba.field_arena.model;

public class Hp {
  int id;
  int roomId;
  String userName;
  int hp;

  final int defaultHp = 10;

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

  public void minusHp() {
    this.hp--;
  }

  public void plusHp() {
    this.hp++;
  }

  public void initHp() {
    this.hp = defaultHp;
  }
}
