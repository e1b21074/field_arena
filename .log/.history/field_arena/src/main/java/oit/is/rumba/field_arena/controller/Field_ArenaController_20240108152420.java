package oit.is.rumba.field_arena.controller;

import java.security.Principal;
import java.util.ArrayList;
import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import org.springframework.transaction.annotation.Transactional;

import org.springframework.ui.Model;

import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import oit.is.rumba.field_arena.model.*;
import oit.is.rumba.field_arena.service.*;

@Controller
public class Field_ArenaController {

  @Autowired
  UserMapper userMapper;

  @Autowired
  CardMapper cardMapper;

  @Autowired
  RoomMapper roomMapper;

  @Autowired
  HpMapper hpMapper;

  @Autowired
  PlayerHandMapper playerHandMapper;

  @Autowired
  AsyncFiled_Area asyncFiled_Area;

  @GetMapping("/gamearea")
  public String gamearea() {
    return "gamearea.html";
  }

  @GetMapping("logininfo")
  public String loginInfo(ModelMap model, Principal prin) {
    String loginUser = prin.getName();
    model.addAttribute("loginUser", loginUser);
    return "gamearea.html";
  }

  @GetMapping("/User")
  public String User(ModelMap model) {
    ArrayList<User> users = userMapper.selectAllUsers();
    model.addAttribute("users", users);
    return "gamearea.html";
  }

  @GetMapping("/game")
  public String game(@RequestParam Integer roomid, ModelMap model, Principal prin) {
    ArrayList<Card> cards = cardMapper.selectAllCards();
    PlayerHand hand = new PlayerHand();
    Draw player = new Draw();
    for (int i = 0; i < 5; i++) {
      hand.setUserName(prin.getName());
      hand.setCard_id(player.getHand(cards).getId());
      playerHandMapper.setPlayerHand(hand.getUserName(), hand.getCard_id());
    }
    model.addAttribute("playerhand", sort(playerHandMapper.selectCardByUserName(prin.getName())));

    // HP処理
    //int roomsId = roomid;// 一旦定数->rommMapperを使用して受け取りたい ←部屋のidをリンクのパラムとして取得した
    String userName = prin.getName();
    // hpMapper.createHp(roomsId, userName);
    Hp hp = hpMapper.selectMyHp(roomid, userName);
    model.addAttribute("hp", hp.getHp());
    model.addAttribute("roomsId", roomid);
    // 敵のHP
    Hp enemyHp = hpMapper.selectEnemyHp(roomid, userName);
    model.addAttribute("enemy", enemyHp);
    model.addAttribute("turns", roomMapper.selectTurnsById(roomid));
    return "game.html";
  }

  @GetMapping("/draw")
  public String draw(@RequestParam String roomid, ModelMap model, Principal prin) {
    int roomsId = Integer.parseInt(roomid);// 一旦定数->rommMapperを使用して受け取りたい
    String userName = prin.getName();
    ArrayList<Card> cards = cardMapper.selectAllCards();
    PlayerHand hand = new PlayerHand();
    Draw player = new Draw();
    int handnum = playerHandMapper.selectCardByUserName(userName).size();
    if (prin.getName().equals(roomMapper.selectTurnsById(roomsId))) {
      if (handnum >= 10) {
        playerHandMapper.deletePlayerHand(playerHandMapper.selecthandByUserName(userName).get(0).getId());
      }
      hand.setUserName(prin.getName());
      hand.setCard_id(player.getHand(cards).getId());
      playerHandMapper.setPlayerHand(hand.getUserName(), hand.getCard_id());
      Hp Hps = hpMapper.selectByroomIdAndUsername(roomsId, userName);
      roomMapper.changeTurns(roomsId, Hps.getUserName());
    }
    model.addAttribute("playerhand", sort(playerHandMapper.selectCardByUserName(prin.getName())));

    // HP
    Hp hp = hpMapper.selectMyHp(roomsId, userName);
    model.addAttribute("hp", hp.getHp());
    model.addAttribute("roomsId", roomsId);
    // 敵のHP
    Hp enemyHp = hpMapper.selectEnemyHp(roomsId, userName);
    model.addAttribute("enemy", enemyHp);
    model.addAttribute("turns", roomMapper.selectTurnsById(roomsId));
    return "game.html";
  }

  @GetMapping("/room")
  @Transactional
  public String create_room(@RequestParam String roomName, Principal prin, ModelMap model) {
    if (roomMapper.checkByroomName(roomName) != null) {
      model.addAttribute("AlreadyRoom", roomName);
      return "gamearea.html";
    }
    asyncFiled_Area.createRoom(roomName, prin.getName());
    model.addAttribute("room", roomName);
    String userName = prin.getName();
    int roomId = roomMapper.selectIdByUser1Name(userName);
    hpMapper.createHp(roomId, userName);
    return "room.html";
  }

  @GetMapping("/inroom")
  public String entrRoom(@RequestParam Integer roomid, Principal prin, ModelMap model) {
    Room room = roomMapper.selectById(roomid);
    asyncFiled_Area.enterRoom(roomid, prin.getName());
    String userName = prin.getName();
    int roomsId = room.getId();
    hpMapper.createHp(roomsId, userName);
    Random rnd = new Random();
    room = roomMapper.selectById(roomsId);
    switch (rnd.nextInt(2)) {
      case 0:
        roomMapper.updateTurnById(room.getId(), room.getUser1());
        break;

      case 1:
        System.out.println(room.getUser2());
        roomMapper.updateTurnById(room.getId(), room.getUser2());
        break;
      default:
        System.out.println("error");
    }

    ArrayList<Card> cards = cardMapper.selectAllCards();
    PlayerHand hand = new PlayerHand();
    Draw player = new Draw();
    for (int i = 0; i < 5; i++) {
      hand.setUserName(prin.getName());
      hand.setCard_id(player.getHand(cards).getId());
      playerHandMapper.setPlayerHand(hand.getUserName(), hand.getCard_id());
    }
    model.addAttribute("playerhand", sort(playerHandMapper.selectCardByUserName(prin.getName())));

    // hpMapper.createHp(roomsId, userName);
    Hp hp = hpMapper.selectMyHp(roomsId, userName);
    model.addAttribute("hp", hp.getHp());
    model.addAttribute("roomsId", roomsId);
    // 敵のHP
    Hp enemyHp = hpMapper.selectEnemyHp(roomsId, userName);
    model.addAttribute("enemy", enemyHp);
    model.addAttribute("turns", roomMapper.selectTurnsById(roomid));

    return "game.html";
  }

  @GetMapping("/active")
  public SseEmitter activeRoom() {
    final SseEmitter emitter = new SseEmitter();
    this.asyncFiled_Area.asyncRoom(emitter);
    return emitter;
  }

  @GetMapping("/cardUse")
  public String cardUse(@RequestParam String id, @RequestParam Integer roomid, Model model, Principal prin) {
    Card card = cardMapper.selectCardById(Integer.parseInt(id));
    Hp Hps = hpMapper.selectByroomIdAndUsername(roomid, prin.getName());

    if (card.getCardAttribute().equals("武器") && prin.getName().equals(roomMapper.selectTurnsById(roomid))) {
      attack(card, roomid, model, prin);
      roomMapper.changeTurns(roomid, Hps.getUserName());
      return "attackWait.html";
    } else if (card.getCardAttribute().equals("回復") && prin.getName().equals(roomMapper.selectTurnsById(roomid))) {
      heal(card, roomid, model, prin);
      roomMapper.changeTurns(roomid, Hps.getUserName());
    } else {
      //int roomsId = roomid;
      String userName = prin.getName();
      Hp myHp = hpMapper.selectMyHp(roomid, userName);
      model.addAttribute("hp", myHp.getHp());
      model.addAttribute("playerhand", sort(playerHandMapper.selectCardByUserName(prin.getName())));
      Hp enemyHp = hpMapper.selectEnemyHp(roomid, userName);
      model.addAttribute("enemy", enemyHp);
      model.addAttribute("roomsId", roomid);
    }
    model.addAttribute("turns", roomMapper.selectTurnsById(roomid));
    return "game.html";
  }

  @GetMapping("/attack")
  public String attack(Card card, int roomid, Model model, Principal prin) {
    //int roomsId = roomid;
    String userName = prin.getName();
    hpMapper.updateAttackTrue(roomid, userName, card.getCardStrong());
    playerHandMapper.deletePlayerHand(playerHandMapper.selecthandnum(userName, card.getId()).get(0).getId());

    return "attackWait.html";
  }

  @GetMapping("/heal")
  public String heal(Card card, int roomid, Model model, Principal prin) {
    //int roomsId = roomid;// Integer.parseInt(id);// 一旦定数->rommMapperを使用して受け取りたい
    String userName = prin.getName();
    Hp myHp = hpMapper.selectMyHp(roomid, userName);
    myHp.plusHp(card.getCardStrong());
    hpMapper.updateMyHp(roomid, userName, myHp.getHp());
    model.addAttribute("hp", myHp.getHp());
    playerHandMapper.deletePlayerHand(playerHandMapper.selecthandnum(userName, card.getId()).get(0).getId());
    model.addAttribute("playerhand", sort(playerHandMapper.selectCardByUserName(prin.getName())));
    // 敵のHP
    Hp enemyHp = hpMapper.selectEnemyHp(roomid, userName);
    model.addAttribute("enemy", enemyHp);
    model.addAttribute("roomsId", roomid);
    return "game.html";
  }

  @GetMapping("/block")
  public String block(@RequestParam Integer id, @RequestParam Integer roomid, Model model, Principal prin) {
    //int roomsId = roomid;
    String userName = prin.getName();
    Card card = cardMapper.selectCardById(id);
    Hp myHp = hpMapper.selectMyHp(roomid, userName);
    model.addAttribute("hp", myHp.getHp());
    // 選択されたカードを削除する
    playerHandMapper.deletePlayerHand(playerHandMapper.selecthandnum(userName, card.getId()).get(0).getId());
    model.addAttribute("playerhand", sort(playerHandMapper.selectCardByUserName(prin.getName())));
    Hp enemyHp = hpMapper.selectEnemyHp(roomid, userName);
    model.addAttribute("enemy", enemyHp);
    model.addAttribute("roomsId", roomid);

    // 相手の攻撃の強さを取得
    int attackPoint = myHp.getAttackPoint();

    // 防御札の強さを取得
    int blockPoint = card.getCardStrong();

    // 防御札の強さ分攻撃の強さを減らす
    if (attackPoint < blockPoint) {
      attackPoint = 0;
    } else {
      attackPoint -= blockPoint;
    }

    myHp.setAttackFlag(false);
    myHp.setAttackPoint(attackPoint);

    // 防御したことをDBに反映
    hpMapper.updateAttackFalse(roomid, userName, attackPoint);

    model.addAttribute("blockPoint", blockPoint);
    model.addAttribute("turns", roomMapper.selectTurnsById(roomid));
    return "game.html";
  }

  @GetMapping("/start")
  public SseEmitter start() {
    final SseEmitter emitter = new SseEmitter();
    this.asyncFiled_Area.asyncEnter(emitter);
    return emitter;
  }

  @GetMapping("/HPasync")
  public SseEmitter HPasync(@RequestParam Integer roomid, Principal prin) {
    final SseEmitter emitter = new SseEmitter();
    this.asyncFiled_Area.HPAsyncEmitter(emitter, roomid, prin.getName());
    return emitter;
  }

  private ArrayList<Card> sort(ArrayList<Card> hand) {
    for (int i = 0; i < hand.size(); i++) {
      for (int j = 0; j < hand.size(); j++) {
        if (hand.get(i).getId() < hand.get(j).getId()) {
          Card tmp = hand.get(i);
          hand.set(i, hand.get(j));
          hand.set(j, tmp);
        }
      }
    }
    return hand;
  }

  // リロード用のメソッド
  @GetMapping("/reRoad")
  public String reRoad(@RequestParam Integer roomid, Model model, Principal prin) {
    //int roomsId = roomid;

    // ユーザ名の取得
    String userName = prin.getName();

    // 自身のHpの取得
    Hp myHp = hpMapper.selectMyHp(roomid, userName);

    // 自身のHpをthemyselefに登録
    model.addAttribute("hp", myHp.getHp());

    // 自身の手札の登録
    model.addAttribute("playerhand", sort(playerHandMapper.selectCardByUserName(prin.getName())));

    // 相手のHpの取得
    Hp enemyHp = hpMapper.selectEnemyHp(roomsId, userName);

    // 相手のHpの登録
    model.addAttribute("enemy", enemyHp);

    // roomidの登録
    model.addAttribute("roomsId", roomsId);

    // ターンの登録
    model.addAttribute("turns", roomMapper.selectTurnsById(roomid));

    // 勝敗
    if (enemyHp.getHp() <= 0) {
      HandReset(userName);
      model.addAttribute("result", "0");
    } else if (myHp.getHp() <= 0) {
      HandReset(userName);
      model.addAttribute("result", "1");
    }

    return "game.html";
  }

  private void HandReset(String userName) {
    ArrayList<PlayerHand> hands = playerHandMapper.selecthandByUserName(userName);
    for (int i = 0; i < hands.size(); i++) {
      playerHandMapper.deletePlayerHand(hands.get(i).getId());
    }
  }

  @GetMapping("/Wait")
  public String Wait(@RequestParam Integer roomid, Model model, Principal prin) {
    // ユーザ名を取得
    String userName = prin.getName();
    // 手札を取得
    ArrayList<Card> hands = sort(playerHandMapper.selectBlockCardByUserName(userName));
    Hp myHp = hpMapper.selectMyHp(roomid, userName);
    int attackPoint = myHp.getAttackPoint();
    model.addAttribute("playerhand", hands);
    model.addAttribute("roomsId", roomid);
    model.addAttribute("attackPoint", attackPoint);
    model.addAttribute("Hp", myHp.getHp());

    return "blockConfirmation.html";
  }

  // 防御しない時のメソッド
  @GetMapping("/noBlock")
  public String noBlock(@RequestParam Integer roomid, Model model, Principal prin) {

    String userName = prin.getName();

    int roomsId = roomid;
    Hp myHp = hpMapper.selectMyHp(roomsId, userName);

    // 相手の攻撃の強さを取得
    int attackPoint = myHp.getAttackPoint();

    hpMapper.updateAttackFalse(roomsId, userName, attackPoint);

    reRoad(roomid, model, prin);

    return "game.html";
  }

  // 相手の防御を非同期で待つメソッド
  @GetMapping("blockWait")
  public SseEmitter blockWait(@RequestParam Integer roomid, Principal prin) {
    final SseEmitter emitter = new SseEmitter();
    this.asyncFiled_Area.blockWaitAsync(emitter, roomid, prin);
    return emitter;
  }

  // 攻撃終了する際の処理
  @GetMapping("/attackFin")
  public String attackFin(@RequestParam Integer roomid, Model model, Principal prin) {
    String userName = prin.getName();

    // 相手のHpを取得
    Hp enemyHp = hpMapper.selectEnemyHp(roomid, userName);
    int hp = enemyHp.getHp();
    int attackPoint = enemyHp.getAttackPoint();
    // 相手のHpを減らす
    hp -= attackPoint;
    hpMapper.updateEnemyHp(roomid, userName, hp);
    model.addAttribute("attackPoint", attackPoint);
    reRoad(roomid, model, prin);
    return "game.html";
  }
}
