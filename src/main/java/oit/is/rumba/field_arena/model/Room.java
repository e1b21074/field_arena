package oit.is.rumba.field_arena.model;

public class Room {
  int id;
  String roomName;
  boolean isActive;
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

}
