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
public class Player extends AbstractEntity implements Comparable{
    public Player(){
        this.isCharacter = true;
    }
    
    public Player(int in,int h,int ac,String nm,int dx){
        this.init = in;
        this.hp = h;
        this.armorClass = ac;
        this.name = nm;
        this.isCharacter = true;
        this.dex = dx;
    }


    @Override
    public void setInit() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void setHp() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void setArmorClass() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void setName() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
    @Override
    public int compareTo(Object o) {
        int compareInit=((Player)o).getInit();
        if((compareInit-(this.init))==0)
        {
            return ((((Player)o).getDex())-(this.dex));
        }
        return (compareInit-(this.init));
    }

    @Override
    public void setDex() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}
