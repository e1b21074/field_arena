package oit.is.rumba.field_arena.model;

public class Room {
  int id;
  String roomName;
  String user1;
  String user2;
  int usernum;
  boolean isActive;
  String turns;

  public int getUsernum() {
    return usernum;
  }

  public void setUsernum(int usernum) {
    this.usernum = usernum;
  }

  public String getUser1() {
    return user1;
  }

  public void setUser1(String user1) {
    this.user1 = user1;
  }

  public String getUser2() {
    return user2;
  }

  public void setUser2(String user2) {
    this.user2 = user2;
  }

  public int getId() {
    return id;
  }

  public void setId(int id) {
    this.id = id;
  }

  public String getRoomName() {
    return roomName;
  }

  public void setRoomName(String roomName) {
    this.roomName = roomName;
  }

  public boolean isActive() {
    return isActive;
  }

  public void setActive(boolean isActive) {
    this.isActive = isActive;
  }

  public String getTurns() {
    return turns;
  }

  public void setTurns(String turns) {
    this.turns = turns;
  }
  
}
