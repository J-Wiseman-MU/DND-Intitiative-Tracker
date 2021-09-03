/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jrwhyytabletoprpgturntracker;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.text.Text;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author johnr
 */
public class EntityCreatorController implements Initializable , PropertyChangeListener{

    /**
     * Initializes the controller class.
     */
    private Stage stage;
    private FXMLDocumentController mainController;
    private EntityCreatorModel model;
    private Alert error;
    
    @FXML
    private TextField dexField;
    
    @FXML
    private Text dexText;
    
    @FXML
    private Button submit;

    @FXML
    private TextField armorClassEntry;

    @FXML
    private TextField hpEntry;

    @FXML
    private TextField nameEntry;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        model = new EntityCreatorModel();
        model.addPropertyChangeListener(this);

    }

    public void start(Stage stg, int entities, boolean players, ArrayList<Player> playerList, ArrayList<Monster> monsterList) {
        if (this.stage == null) {
            this.stage = stg;
        }
        this.stage = stg;
        error = new Alert(AlertType.ERROR);
        armorClassEntry.clear();
        hpEntry.clear();
        nameEntry.clear();
        model.startModel(entities, players, playerList, monsterList);
    }


    @FXML
    private void submitOnMouseCicked(MouseEvent event) {
        model.submitModel(hpEntry.getText(), armorClassEntry.getText(), nameEntry.getText(), dexField.getText());
    }
    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        switch(evt.getPropertyName())
        {
            case "playTitle":
                stage.setTitle("Declare Stats for player 1");
                break;
            case "monTitle":
                stage.setTitle("Declare Stats for monster 1");
                break;
            case "close":
                stage.close();
                break;
            case "clear":
                armorClassEntry.clear();
                hpEntry.clear();
                nameEntry.clear();
                dexField.clear();
                break;
            case "playerTitle":
                armorClassEntry.clear();
                hpEntry.clear();
                nameEntry.clear();
                dexField.clear();
                stage.setTitle("Declare Stats for player " + ((int)evt.getOldValue()));
                break;
            case "monsterTitle":
                armorClassEntry.clear();
                hpEntry.clear();
                nameEntry.clear();
                dexField.clear();
                stage.setTitle("Declare Stats for monster " + ((int)evt.getOldValue()));
                break;
            case "error":
                error.setAlertType(Alert.AlertType.ERROR);
                error.setHeaderText((String) evt.getNewValue());
                error.showAndWait();
                break;
        }
    }

}
