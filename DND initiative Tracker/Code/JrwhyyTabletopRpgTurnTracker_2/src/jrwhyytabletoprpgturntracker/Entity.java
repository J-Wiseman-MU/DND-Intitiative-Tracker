/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jrwhyytabletoprpgturntracker;

/**
 *
 * @author johnr
 */
public interface Entity {
    public void setInit();
    public int getInit();
    public void setDex();
    public int getDex();
    public void setHp();
    public int getHp();
    public void setArmorClass();
    public int getArmorClass();
    public void setName();
    public String getName();
    public Boolean getIsCharacter();
}
