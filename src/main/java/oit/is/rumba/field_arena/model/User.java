package oit.is.rumba.field_arena.model;

public class User {
  int id;
  String userName;
  boolean isActive;



  public User(boolean isActive) {
    this.isActive = isActive;
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
  public boolean isActive() {
    return isActive;
  }
  public void setActive(boolean isActive) {
    this.isActive = isActive;
  }

}
