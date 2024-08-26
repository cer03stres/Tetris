/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package taller2;

import java.io.IOException;
import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

/**
 *
 * @author Cer03stres
 */
public class Administrador extends Application {
    
     private Stage primaryStage;
    private BorderPane rootLayout;
    
    public void start(Stage stage) throws Exception
    {
        primaryStage= stage;
        primaryStage.setTitle("");
        initRootLayout();
    }
    
    public void initRootLayout()
    {
        try
        {
            FXMLLoader loader= new FXMLLoader();
            loader.setLocation(Administrador.class.getResource("/taller2/ventana.fxml"));
            rootLayout= (BorderPane) loader.load();
            
            //Se muestra la escena que contiene el rootLayout
            Scene scene= new Scene(rootLayout);
            
            primaryStage.setScene(scene);
            primaryStage.setResizable(false);
            
            VentanaController controlador= loader.getController();
            controlador.setAdmin(this);
            primaryStage.show();
                   
            
        }catch(IOException ex)
        {
            ex.printStackTrace();
        }
    }
   public void cargarNuevaVentana()
    {
        try
        {
            FXMLLoader loader= new FXMLLoader();
            loader.setLocation(Administrador.class.getResource("/taller2/ventana2.fxml"));
            BorderPane segundaVentana= (BorderPane) loader.load();
            
            
            primaryStage.setTitle("");
            
            Scene scene= new Scene(segundaVentana);
            
            primaryStage.setScene(scene);
            Ventana2Controller ventanita= loader.getController();
            ventanita.setAdmin(this);
            
            
            
        }catch(IOException ex)
        {
            ex.printStackTrace();
        }
    }
   public void cargarTerceraVentana()
   {
        try
        {
            FXMLLoader loader= new FXMLLoader();
            loader.setLocation(Administrador.class.getResource("/taller2/ventana3.fxml"));
            BorderPane terceraVentana= (BorderPane) loader.load();
            
            
            primaryStage.setTitle("Estás en la Tercera ventana");
            
            Scene scene= new Scene(terceraVentana);
            
            primaryStage.setScene(scene);
            Ventana3Controller ventanota= loader.getController();
            ventanota.setAdmin(this);

  
          
                        
           
        
            
        }catch(IOException ex)
        {
            ex.printStackTrace();
        }
    }
   public void cargarCuartaVentana()
   
    {
        try
        {
            FXMLLoader loader= new FXMLLoader();
            loader.setLocation(Administrador.class.getResource("/taller2/ventana4.fxml"));
            BorderPane cuartaVentana= (BorderPane) loader.load();
            
            
            primaryStage.setTitle("");
            
            Scene scene= new Scene(cuartaVentana);
            
            primaryStage.setScene(scene);
            Ventana4Controller ventanitaoa= loader.getController();
            ventanitaoa.setAdmin(this);

              
            primaryStage.setOnCloseRequest(new EventHandler<WindowEvent>()
            {
                public void handle(WindowEvent event)
                {
                    System.exit(0);
                }                
            });
            
  
          
                        
           
        
            
        }catch(IOException ex)
        {
            ex.printStackTrace();
        }
    }
   
    
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // TODO code application logic here
        launch(args);
    }
    
}

