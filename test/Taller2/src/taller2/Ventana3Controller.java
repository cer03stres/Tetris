/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package taller2;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.MenuBar;
import javafx.scene.image.ImageView;
import javax.swing.JOptionPane;

/**
 * FXML Controller class
 *
 * @author Cer03stres
 */
public class Ventana3Controller implements Initializable {

    @FXML
    private MenuBar Menu;
    @FXML
    private Button Boton1;
    @FXML
    private Button Boton2;
    @FXML
    private Button Boton3;
    @FXML
    private Button Boton4;
    @FXML
    private Button Boton5;
    @FXML
    private ImageView Imag;
    @FXML
    private Label Texto;

    /**
     * Initializes the controller class.
     */
 private Administrador admin;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }    
    
    public void setAdmin(Administrador admin)            
    {
        this.admin= admin;
    }
    
    public Administrador getAdmin()            
    {
        return admin;
    }
    @FXML
    public void abrirPrimeraVentana()
    {
        admin.initRootLayout();
    }
    @FXML
     
    public void abrirSegundaVentana()
    {
        admin.cargarNuevaVentana();
    }
    
    @FXML
    public void abrirCuartaVentana()
    {
        admin.cargarCuartaVentana();
    }
    @FXML
    public void Cerrar(ActionEvent event)
    {
        Platform.exit();
                System.exit(0);
    }
      @FXML
   public void Menu(ActionEvent event)
   {
       JOptionPane.showMessageDialog(null, "Presentado por: Julian David Angulo y Pablo Emilio Carmona");
   }
}