/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.github.KeyMove;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;
import org.bukkit.util.Vector;

/**
 *
 * @author Administrator
 */
public class G1 extends 盔甲动作{

    Entity 施法单位;
    ArmorStand 马甲;
    int 攻击间隔;
    public G1(Plugin p,Entity e) {
        super(p);
        施法单位=e;
        马甲=创建马甲(e.getLocation());
        马甲.setSmall(false);
        马甲.setHelmet(new ItemStack(Material.SKULL_ITEM));
        攻击间隔=0;
    }

    @Override
    public void 动作处理() {
        Location 点=施法单位.getLocation();
        float yc=点.getYaw();
        点.setYaw((点.getYaw()+115)%360);
        点.setPitch(0);
        Vector v=点.getDirection();
        v.setY(0);
        v.multiply(1);
        点.add(v);
        点.setYaw(yc);
        点.setY(点.getY()-1);
        马甲.teleport(点);
        if(攻击间隔==0){
            点.setY(点.getY()+1.5);
            v=点.getDirection();
            v.multiply(1.1);
            点.add(v);
            Entity e=点.getWorld().spawnEntity(点, EntityType.SNOWBALL);
            v.multiply(5);
            e.setVelocity(v);
        }
        攻击间隔++;
        攻击间隔%=20;
    }
    
    
    
}
