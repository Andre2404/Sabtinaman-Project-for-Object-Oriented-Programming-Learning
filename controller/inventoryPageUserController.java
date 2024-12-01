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
public class inventoryPageUserController implements Initializable {

    @FXML
    private TextField returnDateCangkul_fill;
    @FXML
    private TextField cangkulQty_fill;
    @FXML
    private Button cangkul_complaint;
    @FXML
    private TextField returnDateTraktor_fill;
    @FXML
    private TextField TraktorQty_fill;
    @FXML
    private Button traktor_complaint;
    @FXML
    private TextField returnDateSekop_fill;
    @FXML
    private TextField sekopQty_fill;
    @FXML
    private Button sekop_complaint;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }    
    
}
