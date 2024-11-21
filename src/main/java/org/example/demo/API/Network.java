package org.example.demo.API;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.Timer;
import java.util.TimerTask;
import javafx.application.Platform;

public class Network {

  private static boolean connection=false;

  private static Timer timer;

  static {
    timer=new Timer();
    timer.scheduleAtFixedRate(new TimerTask() {
      @Override
      public void run() {
        new Thread(()->{
          boolean check=checkConnection();
          Platform.runLater(()->{
            connection=check;
          });
        }).start();
      }
    },0,5000);
  }

  private Network(){

  }

  public static void close(){
    if(timer!=null){
      timer.cancel();
    }
  }

  public static boolean isConnected(){
    return connection;
  }

  private static boolean checkConnection(){
    Socket socket=null;
    boolean reachable=false;
    try{
      socket=new Socket();
      socket.connect(new InetSocketAddress("www.google.com",80),3000);
      reachable=true;
    }
    catch (IOException e){
      e.printStackTrace();
    }
    finally {
      if(socket!=null){
        try {
          socket.close();
        }
        catch (IOException e){
          System.out.println("Không có mạng");
        }
      }
    }
    return reachable;
  }
}
