/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jrwhyytabletoprpgturntracker;

import java.util.ArrayList;

/**
 *
 * @author johnr
 */
public class EntityCreatorModel extends AbstractModel{
    private int entities;
    private int currentEntity;
    private Monster monster;
    private Player player;
    private boolean declaringPlayers;
    private Integer health;
    private Integer armor;
    private Integer dexterity;
    private String name;
    private boolean flag;
    private ArrayList<Monster> monsterList;
    private ArrayList<Player> playerList;
    
    void startModel(int entities, boolean players, ArrayList<Player> playerList, ArrayList<Monster> monsterList)
    {
        this.entities = entities;
        this.declaringPlayers = players;
        this.playerList = playerList;
        this.monsterList = monsterList;
        if (declaringPlayers == true) {
            firePropertyChange("playTitle",null,null);
        } else {
            firePropertyChange("monTitle",null,null);
        }
        currentEntity = 1;
    }
    
    void submitModel(String hp,String ac,String nm,String dx){
        health = 0;
        armor = 0;
        dexterity = 0;
        if (declaringPlayers == true) {

            flag = true;
            try {
                Exception e = null;
                if (hp == "" || ac == "" || nm == "" || dx == "") {
                    throw e;
                }
                this.health = Integer.parseInt(hp);
                this.armor = Integer.parseInt(ac);
                this.dexterity = Integer.parseInt(dx);
                this.name = nm;
                if((this.health) < 1 || (this.armor) < 1 || (this.dexterity) < 1)
                {
                    throw e;
                } 
            } catch (Exception e) {
                flag = false;
                firePropertyChange("error",null,"Enter a positive Integer for armor, health, and dexterity.");
            }
            if (flag == true) {
                playerList.add(new Player(0, this.health, this.armor, this.name,this.dexterity));

                currentEntity++;
                if (currentEntity > entities) {
                    firePropertyChange("close",null,null);
                } else {
                    firePropertyChange("playerTitle",currentEntity,null);
                }
            }else
            {
                firePropertyChange("clear",null,null);
            }
        } else {
            flag = true;
            try {
                Exception e = null;
                if (hp == "" || ac == "" || nm == "" || dx == "") {
                    throw e;
                }
                this.health = Integer.parseInt(hp);
                this.armor = Integer.parseInt(ac);
                this.dexterity = Integer.parseInt(dx);
                this.name = nm;
                if((this.health) < 1 || (this.armor) < 1 || (this.dexterity) < 1)
                {
                    throw e;
                } 
            } catch (Exception e) {
                flag = false;
                firePropertyChange("error",null,"Enter a positive Integer for armor, health, and dexterity.");
            }
            if (flag == true) {
                monsterList.add(new Monster(0, this.health, this.armor, this.name, this.dexterity));

                currentEntity++;
                if (currentEntity > entities) {
                    firePropertyChange("close",null,null);
                } else {
                    firePropertyChange("monsterTitle",currentEntity,null);
                }
            }else
            {
                firePropertyChange("clear",null,null);
            }
        }
    }
            
}
