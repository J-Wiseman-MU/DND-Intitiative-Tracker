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
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Text;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author johnr
 */
public class LifeChangerFXMLController implements Initializable, PropertyChangeListener {

    /**
     * Initializes the controller class.
     */
    private LifeChangerFXMLModel model;
    private Alert error;
    private Stage stage;

    @FXML
    private Button enterButton;

    @FXML
    private Text promptText;

    @FXML
    private TextField lifeAmount;

    @FXML
    private Button toggleButton;

    void start(ArrayList<AbstractEntity> list, int index, Stage stg) {
            model.startModel(index, list);
        
        toggleButton.setText("-");
        if (stage == null) {
            stage = stg;
        }
        lifeAmount.clear();
        stage.setTitle("");
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        model = new LifeChangerFXMLModel();
        model.addPropertyChangeListener(this);
        error = new Alert(AlertType.ERROR);
    }

    @FXML
    private void handleToggleButtonOnMouseClicked(MouseEvent event) {
        model.toggleModel();
    }

    @FXML
    private void enterButtonOnClicked(MouseEvent event) {
        int amount = 0;

        try {
            if (lifeAmount.getText().equals("")) {
                Exception e = null;
                throw e;
            }
            amount = Integer.parseInt(lifeAmount.getText());
            System.out.println(amount);
            model.changeLife(amount);
        } catch (Exception e) {
            error.setAlertType(Alert.AlertType.ERROR);
            error.setHeaderText("Enter an Integer.");
            error.showAndWait();
            lifeAmount.clear();
        }
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        switch (evt.getPropertyName()) {
            case "start":
                promptText.setText("Change " + ((String)evt.getNewValue()) + "'s life by:");
                break;
            case "toggle":
                toggleButton.setText((String)evt.getNewValue());
                break;
            case "close":
                stage.close();
                break;
            case "error":
                error.setAlertType(Alert.AlertType.ERROR);
                error.setHeaderText((String) evt.getNewValue());
                error.showAndWait();
                break;
        }
    }

}
