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
public class merchPageUserController implements Initializable {

    @FXML
    private TextField jumlahSaldoUser_merch;
    @FXML
    private TextField hargaPupuKdg_fill;
    @FXML
    private Button pupukKdg_buy;
    @FXML
    private TextField hargaPupukUrea_fill;
    @FXML
    private Button pupukUrea_buy;
    @FXML
    private TextField hargaPupukKps_fill;
    @FXML
    private Button pupukKps_buy;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }    
    
}
