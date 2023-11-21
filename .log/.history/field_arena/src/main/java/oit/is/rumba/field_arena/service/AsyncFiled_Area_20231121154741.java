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
  boolean Room_enter = false; //入室用のフラグ
  int cnt = 0;
  int ent_cnt = 0;            //入室を表示した回数

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
    try{
      while(true){
        if(Room_falg==false){
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
    }catch (Exception e) {
      logger.warn("Exception:" + e.getClass().getName() + ":" + e.getMessage());
    } finally {
      emitter.complete();
    }
    System.out.println("asyncRoom complete");
  }

  public void enterRoom(int id, String user2){
    roomMapper.updateById(id, user2);
    Room_enter=true;
  }

  @Async
  public void asyncEnter(SseEmitter emitter){
    try{
      while(true){
        if(Room_enter==false){
          TimeUnit.MILLISECONDS.sleep(500);
          continue;
        }
        Room room = roomMapper.selectAllByAtiveandNum();
        emitter.send(room);
        ent_cnt++;
        if (ent_cnt == 2) {
          Room_enter = false;
          ent_cnt = 0;
        }
        TimeUnit.MILLISECONDS.sleep(1000);
      }
    }catch (Exception e) {
      logger.warn("Exception:" + e.getClass().getName() + ":" + e.getMessage());
    } finally {
      emitter.complete();
    }
    System.out.println("asyncRoom complete");
  }

  @Async
  public void HPAsyncEmitter(SseEmitter emitter, int roomid){
    try{
      while(true){
        ArrayList<Hp> hps = hpMapper.selectByRoomId(roomid);
        emitter.send(hps);
        TimeUnit.MILLISECONDS.sleep(1000);
      }
    }catch (Exception e) {
      logger.warn("Exception:" + e.getClass().getName() + ":" + e.getMessage());
    } finally {
      emitter.complete();
    }
    System.out.println("asyncRoom complete");
  }
}
