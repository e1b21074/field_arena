package oit.is.rumba.field_arena.service;

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
        if (Room_falg == false) {
          TimeUnit.MILLISECONDS.sleep(500);
          continue;
        }
        ArrayList<Room> rooms = roomMapper.selectByActive();
        emitter.send(rooms);
        cnt++;
        if (cnt == 2) {
          Room_falg = false;
          cnt = 0;
        }
        TimeUnit.MILLISECONDS.sleep(10000);
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
        if (Room_enter == false) {
          TimeUnit.MILLISECONDS.sleep(500);
          continue;
        }
        Room room = roomMapper.selectAllByAtiveandNum();
        emitter.send(room);
        ent_cnt++;
        if (ent_cnt == 2) {
          Room_enter = false;
          ent_cnt = 0;
          roomMapper.updateActiveById(room.getId());
        }
        TimeUnit.MILLISECONDS.sleep(1000);
      }
    } catch (Exception e) {
      logger.warn("Exception:" + e.getClass().getName() + ":" + e.getMessage());
    } finally {
      emitter.complete();
    }
    System.out.println("asyncRoom complete");
  }

  @Async
  public void HPAsyncEmitter(SseEmitter emitter, int roomid, String userName) {
    int user1Hp;
    int user2Hp;
    ArrayList<Hp> st_hps = hpMapper.selectByRoomId(roomid);

    // それぞれのユーザのHpの初期値を保存
    // DBの上の方のユーザがuser1と仮定
    user1Hp = st_hps.get(0).getHp();
    user2Hp = st_hps.get(1).getHp();

    try {
      while (true) {
        ArrayList<Hp> hps = hpMapper.selectByRoomId(roomid);
        // HPの変動が無ければ少し待ってcontinuで次の繰り返しへ
        if (user1Hp == hps.get(0).getHp() && user2Hp == hps.get(1).getHp()) {
          TimeUnit.MILLISECONDS.sleep(1000);
          continue;
        }

        // 変更があったということなので更新
        user1Hp = hps.get(0).getHp();
        user2Hp = hps.get(1).getHp();

        // リストの2つ目のユーザ名を取得
        String user2 = hps.get(1).getUserName();

        // リストの２つ目のユーザ名とログインユーザ名が同じ場合リストの順番を変更
        if (user2.equals(userName)) {
          Hp tmp = hps.get(0);
          hps.remove(0);
          hps.add(tmp);
        }

        // データを送信
        emitter.send(hps);

        TimeUnit.MILLISECONDS.sleep(1000);
      }
    } catch (Exception e) {
      logger.warn("Exception:" + e.getClass().getName() + ":" + e.getMessage());
    } finally {
      emitter.complete();
    }
    System.out.println("asyncRoom complete");
  }
}
