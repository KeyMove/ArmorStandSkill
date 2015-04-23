/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.github.KeyMove;

import java.util.HashMap;
import java.util.Map;
import org.bukkit.Effect;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Animals;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Monster;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;
import org.bukkit.util.Vector;

/**
 *
 * @author Administrator
 */
public class G2 extends 盔甲动作{

    Entity 施法单位;
    ArmorStand e1;
    ArmorStand e2;
    ArmorStand e3;
    int deg=0;
    int 间隔;
    int 状态;
    int 恢复时间1;
    int 恢复时间2;
    int 恢复时间3;
    ItemStack 普通;
    ItemStack 失效;
    public G2(Plugin p,Entity e) {
        super(p);
        Location loc=e.getLocation();
        状态=0;
        施法单位=e;
        deg=350;
        e1=创建马甲(loc);
        e2=创建马甲(loc);
        e3=创建马甲(loc);
        普通=new ItemStack(Material.SKULL_ITEM);
        失效=new ItemStack(Material.SKULL_ITEM,1,(short)1);
        e1.setHelmet(普通);
        e2.setHelmet(普通);
        e3.setHelmet(普通);
    }

    //P ↑  Y ←
    Location 计算圆面向(Location 点,int 面向距离,int 偏移量,int 角度){
        double x,y;
        Location 目标点=点;
        目标点.setPitch(0);
        Vector v=目标点.getDirection();
        v.multiply(偏移量);
        v.setY(0);
        目标点.add(v);
        x=Math.sin(Math.PI/180*角度)*面向距离;
        y=Math.cos(Math.PI/180*角度)*面向距离;
        目标点.setPitch((float) y);
        目标点.setYaw((float) (目标点.getYaw()+x));
        v=目标点.getDirection();
        v.multiply(偏移量);
        目标点.add(v);
        
        return 目标点;
    }
    
    @Override
    public void 动作处理() {
        float yc=施法单位.getLocation().getYaw();
        Location loc=计算圆面向(施法单位.getLocation(),30,1,deg);
        loc.setYaw(yc);
        e1.teleport(loc);
        loc=计算圆面向(施法单位.getLocation(),30,1,(deg+120)%360);
        loc.setYaw(yc);
        e2.teleport(loc);
        loc=计算圆面向(施法单位.getLocation(),30,1,(deg+240)%360);
        loc.setYaw(yc);
        e3.teleport(loc);
        if(恢复时间1!=0)
            if(--恢复时间1==0)
                e1.setHelmet(普通);
        if(恢复时间2!=0)
            if(--恢复时间2==0)
                e2.setHelmet(普通);
        if(恢复时间3!=0)
            if(--恢复时间3==0)
                e3.setHelmet(普通);
        switch(状态){
            case 0:
                deg+=10;
                if(deg>=360)
                    deg-=360;
                for(Entity e:e1.getNearbyEntities(1, 1, 1)){
                    if((e instanceof Monster)||(e instanceof Animals)){
                        e.setVelocity(e1.getLocation().getDirection().multiply(2));
                    }
                }
                break;
            case 1:
                if(间隔!=0){
                    if(--间隔==0)
                        状态=0;
                    break;
                }
                e1.setHelmet(失效);
                e2.setHelmet(失效);
                e3.setHelmet(失效);
                float f=0.2F;
                Map<Entity,Integer> map=new HashMap<>();
                Vector v;
                for(int j=0;j<100;j++){
                    if(恢复时间1==0)
                    {
                        loc=e1.getEyeLocation();
                        v=loc.getDirection().multiply(f);
                        v.setY(0);
                        loc.add(v);
                        loc.getWorld().playEffect(loc, Effect.HAPPY_VILLAGER, 0);
                    }
                    if(恢复时间2==0)
                    {
                        loc=e2.getEyeLocation();
                        v=loc.getDirection().multiply(f);
                        v.setY(0);
                        loc.add(v);
                        loc.getWorld().playEffect(loc.add(loc.getDirection().multiply(f)), Effect.HAPPY_VILLAGER, 0);
                    }
                    if(恢复时间3==0)
                    {
                        loc=e3.getEyeLocation();
                        v=loc.getDirection().multiply(f);
                        v.setY(0);
                        loc.add(v);
                        loc.getWorld().playEffect(loc.add(loc.getDirection().multiply(f)), Effect.HAPPY_VILLAGER, 0);
                    }
                    f+=0.02;
                }
                if(恢复时间1==0)
                    恢复时间1=30;
                if(恢复时间2==0)
                    恢复时间2=50;
                if(恢复时间3==0)
                    恢复时间3=70;
                间隔=10;
                break;
        }
    }

    @Override
    public void 设置动作ID(int id) {
        状态=id;
    }
    
    
    
}
