/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jrwhyytabletoprpgturntracker;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

/**
 *
 * @author johnr
 */
public class FXMLDocumentModel extends AbstractModel {

    private Integer turn;
    private Integer totalTurns;
    private Integer players;
    private Integer monsters;
    private Integer tempPlayers;
    private Integer tempMonsters;
    private Integer partiesPresent;
    private ArrayList<Player> playerList;
    private ArrayList<Monster> monsterList;
    private ArrayList<Player> tempPlayerList;
    private ArrayList<Monster> tempMonsterList;
    private ArrayList<AbstractEntity> fightList;
    private Integer aliveMonsters;
    private BufferedReader buffRead;
    private Boolean combatStarted;
    private boolean about;

    public void startModel() {
        this.players = 0;
        this.monsters = 0;
        this.partiesPresent = 0;
        this.totalTurns = 0;
        this.combatStarted = false;
        about = false;
    }

    public int getPartiesPresent() {
        return this.partiesPresent;
    }

    public int getMonsters() {
        return monsters;
    }

    public int getPlayers() {
        return players;
    }

    public void setPlayerInitiative(int i, int p) {
        playerList.get(i).init = p;
    }

    public String getPlayerName(int index) {
        return playerList.get(index).name;
    }

    public void initializeLists() {
        if (playerList == null) {
            playerList = new ArrayList<>();
        }
        if (monsterList == null) {
            monsterList = new ArrayList<>();
        }
        if (fightList == null) {
            fightList = new ArrayList<>();
        }
        if (tempPlayerList == null) {
            tempPlayerList = new ArrayList<>();
        }
        if (tempMonsterList == null) {
            tempMonsterList = new ArrayList<>();
        }
    }

    public int numOfEntitiesAdded(String s, boolean isChar) {
        if (isChar == true) {
            try {
                tempPlayers = Integer.parseInt(s);
                players += tempPlayers;
                return tempPlayers;
            } catch (Exception e) {
                firePropertyChange("error", null, "Enter an integer greater than zero.");
                return -1;
            }
        } else {
            try {
                tempMonsters = Integer.parseInt(s);
                monsters += tempMonsters;
                return tempMonsters;
            } catch (Exception e) {
                firePropertyChange("error", null, "Enter an integer greater than zero.");
                return -1;
            }
        }
    }

    public void addPlay() {
        if (totalTurns == 0) {
            int players = this.players;
            playerList.add(new Player());
            firePropertyChange("addPlayClick", playerList, monsterList);
            if (players == 0) {
                partiesPresent++;
            }
            //entityAdder(false);
            //addToView();
        }
    }

    public void addMon() {
        if (totalTurns == 0) {
            int monsters = this.monsters;
            playerList.add(new Player());
            firePropertyChange("addMonClick", playerList, monsterList);
            if (monsters == 0) {
                partiesPresent++;
            }
            //entityAdder(false);
            //addToView();
        }
    }

    public void lifeChangerModel(int index) {
        if (partiesPresent == 2 && totalTurns != 0 && about == false){
            firePropertyChange("nextTurn", null, turn);
            firePropertyChange("lifeChange", index, fightList);

            int i = 0;

            if (fightList.get(index).isCharacter == true && fightList.get(index).hp < 0) {
                if(turn==(totalTurns-1))
                {
                    turn--;
                }
                players--;
                fightList.remove(fightList.get(index));
                totalTurns--;
            } else if (fightList.get(index).isCharacter == false && fightList.get(index).hp < 1) {
                if(turn==(totalTurns-1))
                {
                    turn--;
                }
                monsters--;
                fightList.remove(fightList.get(index));
                totalTurns--;
            }
            if (monsters == 0) {
                partiesPresent--;
                totalTurns = 0;
                firePropertyChange("setStart", 1, null);
                monsterList.clear();
                fightList.clear();
                firePropertyChange("nonCombatRefresh", playerList, monsterList);
                this.combatStarted = false;
            } else if (players == 0) {
                partiesPresent--;
                totalTurns = 0;
                firePropertyChange("setStart", 1, null);
                playerList.clear();
                fightList.clear();
                firePropertyChange("nonCombatRefresh", playerList, monsterList);
                this.combatStarted = false;
            } else {
                firePropertyChange("refreshView", totalTurns, fightList);
            }
            firePropertyChange("nextTurn", null, turn);
        }
    }

    //compareator/comparable:https://beginnersbook.com/2013/12/java-arraylist-of-object-sort-example-comparable-and-comparator/
    public void startCombatModel() {
        this.initializeLists();
        if (this.combatStarted == false) {
            int i;
            for (i = 0; i < monsters; i++) {
                monsterList.get(i).init = (((int) Math.floor(Math.random() * (20) + 1))+(monsterList.get(i).dex));
            }
            Collections.sort(playerList);
            Collections.sort(monsterList);
            totalTurns = monsters + players;
            firePropertyChange("clearView", totalTurns, null);
            int x = 0;
            int y = 0;
            for (i = 0; i < totalTurns; i++) {
                if ((playerList.get(x).init) >= (monsterList.get(y).init)) {
                    fightList.add(playerList.get(x));
                    if (x < (players - 1)) {
                        x++;
                    } else {
                        playerList.get(x).init = 0;
                    }
                } else {
                    fightList.add(monsterList.get(y));
                    if (y < (monsters - 1)) {
                        y++;
                    } else {
                        monsterList.get(y).init = 0;
                    }
                }
            }
            firePropertyChange("addFight", totalTurns, fightList);
            aliveMonsters = monsters;
            turn = 0;
            this.combatStarted = true;
            firePropertyChange("nextTurn", null, turn);
        } else {
            if (turn == (totalTurns - 1)) {
                turn = 0;
            } else {
                turn++;
            }

            firePropertyChange("nextTurn", null, turn);
        }
    }

    //PrintWriter:https://www.w3schools.com/java/java_files_create.asp
    public void saveCurrentPlayermodel(File pFile) {
        if (pFile != null) {
            PrintWriter writer;
            try {
                writer = new PrintWriter(pFile);
                writer.println("P");
                writer.println(players);
                int i;
                for (i = 0; i < players; i++) {
                    writer.println(playerList.get(i).name);
                    writer.println(playerList.get(i).hp);
                    writer.println(playerList.get(i).armorClass);
                    writer.println(playerList.get(i).dex);
                    writer.println(playerList.get(i).init);
                }
                writer.close();
            } catch (IOException ex) {
                System.out.println(ex.getMessage());
            }
        }
    }

    public void saveCurrentMonsterModel(File mFile) {
        if (mFile != null) {
            PrintWriter writer;
            try {
                writer = new PrintWriter(mFile);
                writer.println("M");
                writer.println(monsters);
                int i;
                for (i = 0; i < monsters; i++) {
                    writer.println(monsterList.get(i).name);
                    writer.println(monsterList.get(i).hp);
                    writer.println(monsterList.get(i).armorClass);
                    writer.println(monsterList.get(i).dex);
                    writer.println(monsterList.get(i).init);
                }
                writer.close();
            } catch (IOException ex) {
                System.out.println(ex.getMessage());
            }
        }
    }

    //FileReader:https://docs.oracle.com/javase/8/docs/api/java/io/FileReader.html
    //BufferedReader:https://www.javatpoint.com/how-to-read-file-line-by-line-in-java#:~:text=Scanner%20class-,Using%20BufferedReader%20Class,a%20file%20line%20by%20line.
    //https://docs.oracle.com/javase/8/docs/api/java/io/BufferedReader.html#:~:text=Class%20BufferedReader&text=Reads%20text%20from%20a%20character,large%20enough%20for%20most%20purposes.
    public void loadPlayerModel(File pFile) {
        tempPlayers = 0;
        this.clearTempLists();
        if (pFile != null) {
            try {
                buffRead = new BufferedReader(new FileReader(pFile));
                if (buffRead.readLine().equals("P")) {
                    tempPlayers = Integer.parseInt(buffRead.readLine());
                    players += tempPlayers;
                    int i, ac, hp, init,dex;
                    String nm;
                    for (i = 0; i < tempPlayers; i++) {
                        nm = buffRead.readLine();
                        hp = Integer.parseInt(buffRead.readLine());
                        ac = Integer.parseInt(buffRead.readLine());
                        dex = Integer.parseInt(buffRead.readLine());
                        init = Integer.parseInt(buffRead.readLine());
                        playerList.add(new Player(init, hp, ac, nm,dex));
                        tempPlayerList.add(playerList.get(i + (players - tempPlayers)));
                    }
                    if (players - tempPlayers == 0) {
                        partiesPresent++;
                    }
                    firePropertyChange("addPlay", tempPlayers, tempPlayerList);
                } else {
                    firePropertyChange("error", null, "Choose a valid player file.");
                }
            } catch (Exception ex) {
                System.out.println(ex.getMessage());
            }
        }
    }

    public void loadMonsterModel(File mFile) {
        tempMonsters = 0;
        this.clearTempLists();
        if (mFile != null) {
            try {
                buffRead = new BufferedReader(new FileReader(mFile));
                if (buffRead.readLine().equals("M")) {
                    tempMonsters = Integer.parseInt(buffRead.readLine());
                    monsters += tempMonsters;
                    int i, ac, hp, init,dex;
                    String nm;
                    for (i = 0; i < tempMonsters; i++) {
                        nm = buffRead.readLine();
                        hp = Integer.parseInt(buffRead.readLine());
                        ac = Integer.parseInt(buffRead.readLine());
                        dex = Integer.parseInt(buffRead.readLine());
                        init = Integer.parseInt(buffRead.readLine());
                        monsterList.add(new Monster(init, hp, ac, nm, dex));
                        tempMonsterList.add(monsterList.get(i + (monsters - tempMonsters)));
                    }
                    if (monsters - tempMonsters == 0) {
                        partiesPresent++;
                    }
                    firePropertyChange("addMon", tempMonsters, tempMonsterList);
                } else {
                    firePropertyChange("error", null, "Choose a valid monster file.");
                }
            } catch (Exception ex) {
                System.out.println(ex.getMessage());
            }
        }
    }

    public void clearTempLists() {
        if (tempPlayerList != null) {
            tempPlayerList.clear();
        }
        if (tempMonsterList != null) {
            tempMonsterList.clear();
        }
    }

    public void easyStartModel() {
        this.clearTempLists();
        partiesPresent = 2;
        players += 2;
        monsters += 3;
        int i;
        for (i = 0; i < 2; i++) {
            playerList.add(new Player(0, 12, 13, "player " + (i + 1),i));
            tempPlayerList.add(playerList.get(i + (players - 2)));
        }
        for (i = 0; i < 3; i++) {
            monsterList.add(new Monster(0, 6, 9, "Kobold " + (i + 1),i));
            tempMonsterList.add(monsterList.get(i + (monsters - 3)));
        }
        firePropertyChange("addMon", 3, tempMonsterList);
        firePropertyChange("addPlay", 2, tempPlayerList);
    }

    public void resetModel() {
        int old = partiesPresent;
        players = 0;
        monsters = 0;
        partiesPresent = 0;
        totalTurns = 0;
        turn = 0;
        this.combatStarted = false;
        if (fightList != null) {
            fightList.clear();
        }
        if (playerList != null) {
            playerList.clear();
        }
        if (monsterList != null) {
            monsterList.clear();
        }
        firePropertyChange("reset", old, partiesPresent);
    }
    public void about()
    {
        firePropertyChange("about",null,"This is the about page, click reset to leave.");
        firePropertyChange("about",null,"Purpose:");
        firePropertyChange("about",null,"In many tabletop rpg games like dnd, combat is based on turns.");
        firePropertyChange("about",null,"Turn order is based on one dice rolled for each monster and player.");
        firePropertyChange("about",null,"Once all of the players have rolled this dice, they add a value to calculate their initiative.");
        firePropertyChange("about",null,"A person acting as the monsters rolls initiative for each monster.");
        firePropertyChange("about",null,"Then, the person acting as the monsters(commonly known as the dm) gathers each initiative, player");
        firePropertyChange("about",null,"and monster, orders those values from high to low, and establishes turn order based on that order.");
        firePropertyChange("about",null,"Additionally, the dm keeps track of monster health and dead monsters.");
        firePropertyChange("about",null,"This program is designed to do the following:");
        firePropertyChange("about",null,"1.Roll monster initiative automatically");
        firePropertyChange("about",null,"2.Track the health of each monster and player");
        firePropertyChange("about",null,"3.Remove monsters from the fight when their heath drops to zero or below");
        firePropertyChange("about",null,"4.Remove players from the fight when their health drops below zero");
        firePropertyChange("about",null,"5.Sort monsters and players into initiative order from high to low.");
        firePropertyChange("about",null,"6.Highlight the entity whose turn it is");
        firePropertyChange("about",null,"7.Display monster and player name and armor class");
        firePropertyChange("about",null,"");
        firePropertyChange("about",null,"Functions");
        firePropertyChange("about",null,"");
        firePropertyChange("about",null,"Add Players:");
        firePropertyChange("about",null,"First prompts the user for the amount of players they would like to add.");
        firePropertyChange("about",null,"Then the function prompts the user for the health,armor class, and name of the player.");
        firePropertyChange("about",null,"After these varibles have been specified, a player is constructed out of them ");
        firePropertyChange("about",null,"and added to a list of players and the view.");
        firePropertyChange("about",null,"");
        firePropertyChange("about",null,"Add Monsters:");
        firePropertyChange("about",null,"Functions identically to Add Players, except a monster is constructed and added to a different list.");
        firePropertyChange("about",null,"");
        firePropertyChange("about",null,"Start:");
        firePropertyChange("about",null,"The start button prompts the user for the intiatives of the players, and then rolls for the monsters.");
        firePropertyChange("about",null,"Both are then ordered and displayed on the view list. The Start button's text is changed to next.");
        firePropertyChange("about",null,"The first entry in the viewlist is now highlighted. Next can be used to cycle the highlight.");
        firePropertyChange("about",null,"If all monsters or players are killed, combat ceacses, and the buttons text is changed back.");
        firePropertyChange("about",null,"");
        firePropertyChange("about",null,"Changing Hp:");
        firePropertyChange("about",null,"If you wish to remove Hp from an entity , simply click it during combat.");
        firePropertyChange("about",null,"This will pull up a dialog asking how much you wish to remove.");
        firePropertyChange("about",null,"Simply enter an integer value and click submit, conversely, if you wish to add Hp,");
        firePropertyChange("about",null," simply click on the - and it will change to a + for Hp adding.");
        firePropertyChange("about",null,"");
        firePropertyChange("about",null,"Save Current Monsters/Save Current Players:");
        firePropertyChange("about",null,"Saves the Monsters or Players to a file specified by the user.");
        firePropertyChange("about",null,"");
        firePropertyChange("about",null,"Load Monsters/Load Players:");
        firePropertyChange("about",null,"Prompts the user to select a file to open and extract Monsters/Players from.");
        firePropertyChange("about",null,"Function produces an error if the file is not a valid Monster/Player file.");
        firePropertyChange("about",null,"");
        firePropertyChange("about",null,"Easy Start For TA:");
        firePropertyChange("about",null,"Places three kobolds and two players on the list as if they were added and readies the program for combat.");
        firePropertyChange("about",null,"");
        firePropertyChange("about",null,"Reset:");
        firePropertyChange("about",null,"Clears the UI elements and all data in the program.");
    }
}
