package oit.is.rumba.field_arena.model;

public class Card {
  int id;
  String cardAttribute;
  int cardStrong;

  public int getId() {
    return id;
  }

  public void setId(int id) {
    this.id = id;
  }

  public String getCardAttribute() {
    return cardAttribute;
  }

  public void setCardAttribute(String cardAttribute) {
    this.cardAttribute = cardAttribute;
  }

  public int getCardStrong() {
    return cardStrong;
  }

  public void setCardStrong(int cardStrong) {
    this.cardStrong = cardStrong;
  }

}
