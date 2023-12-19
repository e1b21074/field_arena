package oit.is.rumba.field_arena.service;

import java.security.Principal;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import oit.is.rumba.field_arena.model.*;

@Service
public class AsyncFiled_Area {
  boolean Room_falg = false;
  boolean Room_enter = false; // 入室用のフラグ
  int cnt = 0;
  int ent_cnt = 0; // 入室を表示した回数
  int turn_flag = 0;

  private final Logger logger = LoggerFactory.getLogger(AsyncFiled_Area.class);

  @Autowired
  RoomMapper roomMapper;

  @Autowired
  HpMapper hpMapper;

  @Transactional
  public void createRoom(String roomName, String user1) {
    roomMapper.insertName(roomName, user1);
    Room_falg = true;
  }

  @Async
  public void asyncRoom(SseEmitter emitter) {
    try {
      while (true) {
        System.out.println("ok");
        if (Room_falg == false) {
          TimeUnit.MILLISECONDS.sleep(500);
          continue;
        }
        ArrayList<Room> rooms = roomMapper.selectByActive();
        emitter.send(rooms);
        cnt++;
        if (cnt != 2) {
          TimeUnit.MILLISECONDS.sleep(1000);
          continue;
        }

        Room_falg = false;
        cnt = 0;
        break;
      }
    } catch (Exception e) {
      logger.warn("Exception:" + e.getClass().getName() + ":" + e.getMessage());
    } finally {
      emitter.complete();
    }
    System.out.println("asyncRoom complete");
  }

  public void enterRoom(int id, String user2) {
    roomMapper.updateById(id, user2);
    Room_enter = true;
  }

  @Async
  public void asyncEnter(SseEmitter emitter) {
    try {
      while (true) {
        System.out.println("enter ok!");
        if (Room_enter == false) {
          TimeUnit.MILLISECONDS.sleep(500);
          continue;
        }
        Room room = roomMapper.selectAllByAtiveandNum();
        emitter.send(room);
        roomMapper.updateActiveById(room.getId());
        Room_enter = false;
        System.out.println("break");
        break;
      }
    } catch (Exception e) {
      logger.warn("Exception:" + e.getClass().getName() + ":" + e.getMessage());
    } finally {
      emitter.complete();
    }
    System.out.println("asyncEnter complete");
  }

  @Async
  public void HPAsyncEmitter(SseEmitter emitter, int roomid, String userName) {
    int user1Hp;
    int user2Hp;
    ArrayList<Hp> st_hps = hpMapper.selectByRoomId(roomid);
    String turn = "";
    boolean attackFlag = false;

    // それぞれのユーザのHpの初期値を保存
    // DBの上の方のユーザがuser1と仮定
    user1Hp = st_hps.get(0).getHp();
    user2Hp = st_hps.get(1).getHp();
    turn = roomMapper.selectTurnsById(roomid);

    try {
      while (true) {
        System.out.println( userName+" HpAsync OK!");
        ArrayList<Hp> hps = hpMapper.selectByRoomId(roomid);
        String tmp_turn = roomMapper.selectTurnsById(roomid);
        attackFlag = hpMapper.selectFlag(roomid, userName);
        // HPの変動が無ければ少し待ってcontinuで次の繰り返しへ
        if ((user1Hp == hps.get(0).getHp() && user2Hp == hps.get(1).getHp()) && tmp_turn.equals(turn) && attackFlag==false) {
          TimeUnit.MILLISECONDS.sleep(500);
          continue;
        }

        // 変更があったということなので更新
        user1Hp = hps.get(0).getHp();
        user2Hp = hps.get(1).getHp();
        turn = tmp_turn;

        // データを送信
        emitter.send(attackFlag);

        TimeUnit.MILLISECONDS.sleep(1000);
      }
    } catch (Exception e) {
      logger.warn("Exception:" + e.getClass().getName() + ":" + e.getMessage());
    } finally {
      emitter.complete();
    }
    System.out.println("HPAsyncEmitter complete");
  }


  //相手の防御を非同期でまつ用のメソッド
  @Async
  public void blockWaitAsync(SseEmitter emitter,int roomid,Principal prin) {
    boolean flag;
    String userName = prin.getName();
    try {
      while (true) {
        System.out.println("Attack Ok!");
        flag = hpMapper.selectAttackFlag(roomid, userName);
        if (flag == true) {
          TimeUnit.MILLISECONDS.sleep(500);
          continue;
        }
        emitter.send(flag);
        break;
      }
    } catch (Exception e) {
      logger.warn("Exception:" + e.getClass().getName() + ":" + e.getMessage());
    } finally {
      emitter.complete();
    }
    System.out.println("blockWaitAsync complete");
  }

}
