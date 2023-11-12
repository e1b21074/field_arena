package oit.is.rumba.field_arena.model;

public class HpTest {
  private int hp = 0;
  final int defaultHp = 10;

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
