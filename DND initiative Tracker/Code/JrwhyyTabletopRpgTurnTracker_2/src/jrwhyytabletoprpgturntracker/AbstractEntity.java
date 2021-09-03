/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jrwhyytabletoprpgturntracker;

import java.util.Comparator;

/**
 *
 * @author johnr
 */
public abstract class AbstractEntity implements Entity{
    int hp;
    int armorClass;
    int init;
    int dex;
    String name;
    Boolean isCharacter;


    final public void setInit(int x) {
        this.init = x;      
    }

    @Override
    public int getInit() {
        return init;
    }
    
    public void setDex(int x){
        this.dex = x;
    }
    
    @Override
    public int getDex(){
        return dex;
    }

    public void setHp(int x) {
        this.hp = x;
    }

    @Override
    public int getHp() {
        return hp;
    }

    public void setArmorClass(int x) {
        this.armorClass = x;
    }

    @Override
    public int getArmorClass() {
        return armorClass;
    }
    
    public void setName(String x){
        this.name = x;
    }
    
    @Override
    public String getName(){
        return name;
    }
    
    @Override
    public Boolean getIsCharacter(){
        return isCharacter;
    }
}
