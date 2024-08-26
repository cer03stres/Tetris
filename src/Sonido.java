
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import javax.swing.JFrame;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Cer03stres
 */
public class Sonido extends JFrame{
private ArrayList<File> EFX= new ArrayList<>();
    
    public Sonido(){
        
        introducirFx("Musica5.wav");
        introducirFx("Musica2.wav");
        introducirFx("Musica3.wav");
        introducirFx("Musica4.wav");
        introducirFx("Musica.wav");
        
        

        
        
    
 
        
    }
    public void introducirFx(String Ruta){
        try{
            File file= new File("").getAbsoluteFile();
            String rutt = file+ "/Efectos/" + Ruta;
            
            file= new File(rutt);
            
            EFX.add(file);
            
        }catch(NullPointerException e){
            System.out.println("Error, ruta de audio no encontrada... ");
        }
        
    }
    
    public void Fx(int indice){
        try {
            File file = EFX.get(indice);
            
            Clip sonido= AudioSystem.getClip();
            
            sonido.open(AudioSystem.getAudioInputStream(file));
            
            sonido.start();
            } catch (LineUnavailableException ex) {
            Logger.getLogger(Sonido.class.getName()).log(Level.SEVERE, null, ex);
        } catch (UnsupportedAudioFileException ex) {
            Logger.getLogger(Sonido.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(Sonido.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        
    }
    
}
