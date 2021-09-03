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
public class LifeChangerFXMLModel extends AbstractModel {

    private boolean negative;
    private ArrayList<AbstractEntity> fightList;
    private int index;

    public void startModel(int index, ArrayList<AbstractEntity> fl) {
        negative = true;
        this.index = index;
        this.fightList = fl;
        firePropertyChange("start", null, fightList.get(index).name);
    }

    public void toggleModel() {
        if (negative == true) {
            negative = false;
            firePropertyChange("toggle", null, "+");
        } else {
            negative = true;
            firePropertyChange("toggle", null, "-");
        }
    }

    public void changeLife(int amount) {
        if (negative == true) {
            fightList.get(index).hp -= amount;
        } else {
            fightList.get(index).hp += amount;
        }
        firePropertyChange("close",null,index);
    }
}
