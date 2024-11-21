/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package view;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;

/**
 * FXML Controller class
 *
 * @author Andhi
 */
public class registerUserController implements Initializable {

    @FXML
    private TextField username_register;
    @FXML
    private TextField address_register;
    @FXML
    private TextField email_register;
    @FXML
    private TextField nomorKontak_register;
    @FXML
    private TextField password_register;
    @FXML
    private Button buttonUser_register;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }    
    
}
