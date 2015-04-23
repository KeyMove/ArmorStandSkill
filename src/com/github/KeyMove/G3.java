/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.github.KeyMove;

import static java.lang.System.out;
import org.bukkit.Bukkit;
import org.bukkit.Effect;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Animals;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Monster;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;
import org.bukkit.util.Vector;

/**
 *
 * @author Administrator
 */
public class G3 extends 盔甲动作{

    Entity 施法单位;
    ArmorStand[] 飞行器;
    Entity[] 目标;
    int 角度;
    int 飞离;
    int 状态;
    int 间隔;
    ItemStack 普通;
    public G3(Plugin p,Entity e) {
        super(p);
        飞离=0;
        状态=0;
        间隔=0;
        施法单位=e;
        飞行器=new ArmorStand[6];
        目标=new Entity[3];
        角度=0;
        普通=new ItemStack(Material.SKULL_ITEM);
        for(int i=0;i<6;i++)
        {
            飞行器[i]=创建马甲(e.getLocation());
            飞行器[i].setHelmet(普通);
        }
    }

    //P ↑  Y ←
    Location 计算到目标点(Location 点,int 角度,int 高度){
        Location loc=点;        
        点.setPitch(0);
        loc.setYaw((loc.getYaw()+角度)%360);
        Vector v=loc.getDirection();
        v.setY(0);
        v.multiply(2);
        loc.add(v);
        loc.setY(loc.getY()+高度);
        return loc;
    }
    
    @Override
    public void 动作处理() {
        Location loc=施法单位.getLocation();
        角度+=10;
        if(角度>=360){
            角度-=360;
        }
        int 偏移量=0;
        int 偏移=0;
        if(飞离!=3)
            偏移量=360/(2*(3-飞离));
        Vector v;
        for(int i=0;i<飞离;i++){
            Location l=计算到目标点(目标[i].getLocation(),角度,3);
            l.setPitch(45);
            v=l.getDirection();
            //l.setYaw((float) 飞行器[i*2].getLocation().distance(loc));
            飞行器[i*2].teleport(l);
            l.getWorld().spigot().playEffect(l, Effect.HAPPY_VILLAGER, 0, 0, (float) v.getX(),(float) v.getY(),(float) v.getZ(), 1, 0, 16);
            l=计算到目标点(目标[i].getLocation(),角度+180,3);
            l.setPitch(45);
            v=l.getDirection();
            //l.setYaw((float) 飞行器[i*2+1].getLocation().distance(loc));
            飞行器[i*2+1].teleport(l);
            l.getWorld().spigot().playEffect(l, Effect.HAPPY_VILLAGER, 0, 0, (float) v.getX(),(float) v.getY(),(float) v.getZ(), 1, 0, 16);
        }
        for(int i=飞离;i<3;i++){
            Location l=计算到目标点(施法单位.getLocation(),角度+偏移,3);
            //l.setPitch(45);
            偏移+=偏移量;
            l.setPitch((float) 飞行器[i*2].getLocation().distance(loc)+180);
            飞行器[i*2].teleport(l);
            l=计算到目标点(施法单位.getLocation(),角度+偏移,3);
            //l.setPitch(45);
            偏移+=偏移量;
            l.setPitch((float) 飞行器[i*2+1].getLocation().distance(loc)+180);
            飞行器[i*2+1].teleport(l);
        }
        switch(状态){
            case 0:
                
                break;
            case 1:
                if(飞离!=0){
                    飞离=0;
                }
                else{
                    for(Entity e:施法单位.getNearbyEntities(8, 8, 8)){
                        if((e instanceof Monster)||(e instanceof Animals)){
                            目标[飞离]=e;
                            if(++飞离==3)
                                break;
                            out.print(e);
                        }
                        
                    }
                }
                状态=0;
                break;
        }
    }

    @Override
    public void 设置动作ID(int id) {
        状态=id;
    }
    
    
    
}
