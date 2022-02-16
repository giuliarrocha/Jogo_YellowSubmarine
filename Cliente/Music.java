import java.io.*;
import javax.sound.sampled.*;

public class Music {
  static long clipTimePosition = 0;
  static Clip musicaFundo;

  public static void playMusic (String musicLocation, boolean loop){
    try{
      Clip clip;
      File musicPath = new File (musicLocation);
      if(musicPath.exists()){
        AudioInputStream audioInput = AudioSystem.getAudioInputStream(musicPath);
        clip = AudioSystem.getClip();
        clip.open(audioInput);
        clip.start();
        if(loop){
          clip.loop(Clip.LOOP_CONTINUOUSLY);
          Music.musicaFundo = clip;
        }
      }else{
        System.out.println("O arquivo não foi encontrado");
      }
    }catch(Exception ex){
      ex.printStackTrace();
    }
  }
  public static void pausa(Clip clip){ //pausa e guarda o segundo que a música parou
    try {
        clipTimePosition = clip.getMicrosecondPosition();
        clip.stop();        
    } catch(Exception e) {
        e.printStackTrace();
    }
  }
  public static void retorna(Clip clip){ // retorna a música de onde parou
    try {
        clip.setMicrosecondPosition(clipTimePosition);
        clip.start();
    } catch(Exception e) {
        e.printStackTrace();
    }
  }
  public static void parar(Clip clip){ // para a música
    try {
        clip.stop();
        clip.setFramePosition(0);        
    } catch(Exception e) {
        e.printStackTrace();
    }
  }
}