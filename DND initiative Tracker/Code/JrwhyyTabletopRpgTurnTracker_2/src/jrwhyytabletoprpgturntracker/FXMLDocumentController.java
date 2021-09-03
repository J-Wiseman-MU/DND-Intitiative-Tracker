/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jrwhyytabletoprpgturntracker;

import java.awt.event.MouseEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextInputDialog;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

/**
 *
 * @author johnr
 */
//useful source:https://stackoverflow.com/questions/1311912/how-do-i-autoindent-in-netbeans#:~:text=To%20format%20all%20the%20code,%2B%20Shift%20%2B%20left%20arrow%20key.
public class FXMLDocumentController implements Initializable, PropertyChangeListener {

    private Stage stage;
    private Stage entityStage;
    private Stage lifeStage;
    private Stage fileStage;
    private Scene mainPageScene;
    private Scene entityCreatorScene;
    private Scene lifeChangerScene;
    private TextInputDialog numOfEntityRetriever;
    private Alert error;
    private ActionEvent event;
    private FXMLDocumentModel model;
    private EntityCreatorController entityCreatorController;
    private LifeChangerFXMLController lifeChangerFXMLController;
    private FileChooser fileChooser;

    @FXML
    private MenuItem addPlayers;

    @FXML
    private MenuItem ez;

    @FXML
    private Button startBtn;

    @FXML
    private MenuItem loadMonsters;

    @FXML
    private MenuItem loadPlayers;

    @FXML
    private ListView<String> fightOrder;

    @FXML
    private MenuItem savePlayers;

    @FXML
    private MenuItem saveMonsters;

    @FXML
    private MenuItem addMonsters;
    @FXML
    private Button resetButton;
    @FXML
    private MenuItem about;

    @FXML
    void addPlayersOnAction(ActionEvent event) throws IOException {
        model.addPlay();
    }

    @FXML
    void addMonstersOnAction(ActionEvent event) {
        model.addMon();
    }

    void entityAdder(boolean isChar, ArrayList<Player> playerList, ArrayList<Monster> monsterList) {
        model.initializeLists();

        int entNum = -1;
        while (entNum < 1) {
            numOfEntityRetriever.getEditor().clear();
            numOfEntityRetriever.setTitle("Input a Number of Players");
            numOfEntityRetriever.showAndWait();
            try {
                entNum = model.numOfEntitiesAdded(numOfEntityRetriever.getEditor().getText(), isChar);
            } catch (Exception e) {
                entNum = -1;
            }
            if (entNum < 1) {
                error.setAlertType(Alert.AlertType.ERROR);
                error.setHeaderText("Enter an Integer greater than zero.");
                error.showAndWait();
            }
        }
        numOfEntityRetriever.getEditor().clear();
        if (entityStage == null) {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("EntityCreator.fxml"));
                Parent root = loader.load();
                entityCreatorController = loader.getController();
                entityCreatorScene = new Scene(root);
                entityStage = new Stage();

                entityCreatorController.start(entityStage, entNum, isChar, playerList, monsterList);

                entityStage.setScene(entityCreatorScene);
                entityStage.showAndWait();
            } catch (IOException ex) {
                Logger.getLogger(FXMLDocumentController.class.getName()).log(Level.SEVERE, null, ex);
            }
        } else {
            entityCreatorController.start(entityStage, entNum, isChar, playerList, monsterList);
            entityStage.showAndWait();
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        numOfEntityRetriever = new TextInputDialog();
        error = new Alert(AlertType.ERROR);
        model = new FXMLDocumentModel();
        model.addPropertyChangeListener(this);
    }

    public void start(Stage stage) {
        this.stage = stage;
        this.mainPageScene = stage.getScene();
        fileChooser = new FileChooser();
        fileStage = new Stage();
        model.startModel();
        model.initializeLists();
    }

    @FXML
    private void fightOrderOnMouseClicked(javafx.scene.input.MouseEvent event) {
        Integer index = fightOrder.getSelectionModel().getSelectedIndex();
        model.lifeChangerModel(index);
    }

    @FXML
    private void startBtnOnMouseClicked(javafx.scene.input.MouseEvent event) {
        if (model.getPartiesPresent() == 2) {
            if (startBtn.getText().equals("Start"))
            {
                startBtn.setText("Next");
                //fightList = new ArrayList();
                int i, init;
                for (i = 0; i < model.getPlayers(); i++) {
                    numOfEntityRetriever.getEditor().clear();
                    numOfEntityRetriever.setTitle("Input " + model.getPlayerName(i) + "'s initiative.");
                    numOfEntityRetriever.showAndWait();
                    try {
                        init = Integer.parseInt(numOfEntityRetriever.getEditor().getText());
                        if (init < 1) {
                            Exception e = null;
                            throw e;
                        }
                        model.setPlayerInitiative(i, init);
                    } catch (Exception e) {
                        error.setAlertType(Alert.AlertType.ERROR);
                        error.setHeaderText("Enter an integer greater than zero.");
                        error.showAndWait();
                        i--;
                    }
                }
                numOfEntityRetriever.getEditor().clear();
            }
            model.startCombatModel();
        }
    }
//filechooser:https://www.genuinecoder.com/save-files-javafx-filechooser/
    @FXML
    private void saveCurrentPlayersOnAction(ActionEvent event) {
        if (model.getPlayers() > 0) {
            fileChooser.setTitle("Save players at");
            fileChooser.setInitialDirectory(
                    new File(System.getProperty("user.home"))
            );
            fileChooser.setInitialFileName("players.txt");
            File pFile = fileChooser.showSaveDialog(fileStage);
            model.saveCurrentPlayermodel(pFile);
        }
    }

    @FXML
    private void saveCurrentMonstersOnAction(ActionEvent event) {
        if (model.getMonsters() > 0) {
            fileChooser.setTitle("Save monsters at");
            fileChooser.setInitialDirectory(
                    new File(System.getProperty("user.home"))
            );
            fileChooser.setInitialFileName("monsters.txt");
            File mFile = fileChooser.showSaveDialog(fileStage);
            model.saveCurrentMonsterModel(mFile);
        }
    }

    @FXML
    private void loadPlayersOnAction(ActionEvent event) {
        if (model.getPartiesPresent() < 2) {
            model.initializeLists();
            fileChooser.setTitle("Choose File to load Player from");
            fileChooser.setInitialDirectory(
                    new File(System.getProperty("user.home"))
            );
            File pFile = fileChooser.showOpenDialog(fileStage);
            model.loadPlayerModel(pFile);
        }
    }

    @FXML
    private void loadMonstersOnAction(ActionEvent event) {
        if (model.getPartiesPresent() < 2) {
            model.initializeLists();
            fileChooser.setTitle("Choose File to load Monsters from");
            fileChooser.setInitialDirectory(
                    new File(System.getProperty("user.home"))
            );
            File mFile = fileChooser.showOpenDialog(fileStage);
            model.loadMonsterModel(mFile);
        }
    }

    @FXML
    private void easyStartOnAction(ActionEvent event) {
        model.initializeLists();
        model.easyStartModel();
    }

    @FXML
    private void resetButtonOnMouseClicked(javafx.scene.input.MouseEvent event) {
        addPlayers.setDisable(false);
        addMonsters.setDisable(false);
        loadMonsters.setDisable(false);
        loadPlayers.setDisable(false);
        saveMonsters.setDisable(false);
        savePlayers.setDisable(false);
        startBtn.setDisable(false);
        model.resetModel();
        if ((fightOrder.getItems()) != null) {
            fightOrder.getItems().clear();
        }
        startBtn.setText("Start");
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        //switch syntax:https://www.w3schools.com/java/java_switch.asp
        int i;
        switch (evt.getPropertyName()) {
            case "reset":
                if ((fightOrder.getItems()) != null) {
                    fightOrder.getItems().clear();
                }
                startBtn.setText("Start");
                break;
            case "addMon":
                for (i = 0; i < ((int) evt.getOldValue()); i++) {
                    fightOrder.getItems().add(((ArrayList<Monster>) evt.getNewValue()).get(i).name + " Hp:" + ((ArrayList<Monster>) evt.getNewValue()).get(i).hp + " Armor Class:" + ((ArrayList<Monster>) evt.getNewValue()).get(i).armorClass);
                }
                break;
            case "addPlay":
                for (i = 0; i < ((int) evt.getOldValue()); i++) {
                    fightOrder.getItems().add(((ArrayList<Player>) evt.getNewValue()).get(i).name + " Hp:" + ((ArrayList<Player>) evt.getNewValue()).get(i).hp + " Armor Class:" + ((ArrayList<Player>) evt.getNewValue()).get(i).armorClass);
                }
                break;
            case "clearView":
                if (fightOrder.getItems() != null) {
                    fightOrder.getItems().clear();
                }
                break;
            case "addFight":
                for (i = 0; i < ((int) evt.getOldValue()); i++) {
                    fightOrder.getItems().add(((ArrayList<AbstractEntity>) evt.getNewValue()).get(i).name + " Hp:" + ((ArrayList<AbstractEntity>) evt.getNewValue()).get(i).hp + " Armor Class:" + ((ArrayList<AbstractEntity>) evt.getNewValue()).get(i).armorClass);
                }
                break;
            case "nextTurn":
                fightOrder.getSelectionModel().select((int) evt.getNewValue());
                break;
            case "lifeChange":
                if (lifeStage == null) {
                    try {
                        FXMLLoader loader = new FXMLLoader(getClass().getResource("LifeChangerFXML.fxml"));
                        Parent root = loader.load();
                        lifeChangerFXMLController = loader.getController();
                        lifeChangerScene = new Scene(root);
                        lifeStage = new Stage();

                        lifeStage.setScene(lifeChangerScene);
                    } catch (IOException ex) {
                        Logger.getLogger(FXMLDocumentController.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
                lifeChangerFXMLController.start((ArrayList<AbstractEntity>) evt.getNewValue(), (int) evt.getOldValue(), lifeStage);
                lifeStage.showAndWait();
                break;
            case "refreshView":
                if (fightOrder.getItems() != null) {
                    fightOrder.getItems().clear();
                }
                for (i = 0; i < ((int) evt.getOldValue()); i++) {
                    fightOrder.getItems().add(((ArrayList<AbstractEntity>) evt.getNewValue()).get(i).name + " Hp:" + ((ArrayList<AbstractEntity>) evt.getNewValue()).get(i).hp + " Armor Class:" + ((ArrayList<AbstractEntity>) evt.getNewValue()).get(i).armorClass);
                }
                break;
            case "setStart":
                startBtn.setText("Start");
                break;
            case "addPlayClick":
                ((ArrayList<Player>) evt.getOldValue()).remove((((ArrayList<Player>) evt.getOldValue()).size() - 1));
                entityAdder(true, ((ArrayList<Player>) evt.getOldValue()), ((ArrayList<Monster>) evt.getNewValue()));
                if (fightOrder.getItems() != null) {
                    fightOrder.getItems().clear();
                }
                for (i = 0; i < ((ArrayList<Player>) evt.getOldValue()).size(); i++) {
                    fightOrder.getItems().add(((ArrayList<Player>) evt.getOldValue()).get(i).name + " Hp:" + ((ArrayList<Player>) evt.getOldValue()).get(i).hp + " Armor Class:" + ((ArrayList<Player>) evt.getOldValue()).get(i).armorClass);
                }
                for (i = 0; i < ((ArrayList<Monster>) evt.getNewValue()).size(); i++) {
                    fightOrder.getItems().add(((ArrayList<Monster>) evt.getNewValue()).get(i).name + " Hp:" + ((ArrayList<Monster>) evt.getNewValue()).get(i).hp + " Armor Class:" + ((ArrayList<Monster>) evt.getNewValue()).get(i).armorClass);
                }
                break;

            case "addMonClick":
                ((ArrayList<Player>) evt.getOldValue()).remove((((ArrayList<Player>) evt.getOldValue()).size() - 1));
                entityAdder(false, ((ArrayList<Player>) evt.getOldValue()), ((ArrayList<Monster>) evt.getNewValue()));
                if (fightOrder.getItems() != null) {
                    fightOrder.getItems().clear();
                }
                for (i = 0; i < ((ArrayList<Player>) evt.getOldValue()).size(); i++) {
                    fightOrder.getItems().add(((ArrayList<Player>) evt.getOldValue()).get(i).name + " Hp:" + ((ArrayList<Player>) evt.getOldValue()).get(i).hp + " Armor Class:" + ((ArrayList<Player>) evt.getOldValue()).get(i).armorClass);
                }
                for (i = 0; i < ((ArrayList<Monster>) evt.getNewValue()).size(); i++) {
                    fightOrder.getItems().add(((ArrayList<Monster>) evt.getNewValue()).get(i).name + " Hp:" + ((ArrayList<Monster>) evt.getNewValue()).get(i).hp + " Armor Class:" + ((ArrayList<Monster>) evt.getNewValue()).get(i).armorClass);
                }
                break;
            case "nonCombatRefresh":
                if (fightOrder.getItems() != null) {
                    fightOrder.getItems().clear();
                }
                for (i = 0; i < ((ArrayList<Player>) evt.getOldValue()).size(); i++) {
                    fightOrder.getItems().add(((ArrayList<Player>) evt.getOldValue()).get(i).name + " Hp:" + ((ArrayList<Player>) evt.getOldValue()).get(i).hp + " Armor Class:" + ((ArrayList<Player>) evt.getOldValue()).get(i).armorClass);
                }
                for (i = 0; i < ((ArrayList<Monster>) evt.getNewValue()).size(); i++) {
                    fightOrder.getItems().add(((ArrayList<Monster>) evt.getNewValue()).get(i).name + " Hp:" + ((ArrayList<Monster>) evt.getNewValue()).get(i).hp + " Armor Class:" + ((ArrayList<Monster>) evt.getNewValue()).get(i).armorClass);
                }
                break;
            case "error":
                error.setAlertType(Alert.AlertType.ERROR);
                error.setHeaderText((String) evt.getNewValue());
                error.showAndWait();
                break;
            case "about":
                fightOrder.getItems().add((String)evt.getNewValue());
                break;

        }
    }

    @FXML
    private void aboutOnAction(ActionEvent event) {
        model.resetModel();
        if ((fightOrder.getItems()) != null) {
            fightOrder.getItems().clear();
        }
        startBtn.setText("Start");
        addPlayers.setDisable(true);
        addMonsters.setDisable(true);
        loadMonsters.setDisable(true);
        loadPlayers.setDisable(true);
        saveMonsters.setDisable(true);
        savePlayers.setDisable(true);
        startBtn.setDisable(true);
        
        model.about();
    }

}
