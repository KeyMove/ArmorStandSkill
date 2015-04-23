/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.github.KeyMove;

import org.bukkit.Effect;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Animals;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Damageable;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Monster;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;
import org.bukkit.util.Vector;

/**
 *
 * @author Administrator
 */
public class G4 extends 盔甲动作{

    int 状态;
    int 间隔;
    int 角度;
    int 振荡角度;
    Entity 施法单位;
    ArmorStand[] 马甲;
    ArmorStand 驻留马甲;
    ItemStack 普通;
    boolean 驻留;
    public G4(Plugin p,Entity e) {
        super(p);
        驻留=false;
        施法单位=e;
        状态=0;
        间隔=0;
        角度=0;
        振荡角度=0;
        驻留马甲=创建马甲(e.getLocation());
        马甲=new ArmorStand[2];
        普通=new ItemStack(Material.SKULL_ITEM);
        for(int i=0;i<2;i++)
        {
            马甲[i]=创建马甲(e.getLocation());
            马甲[i].setHelmet(普通);
        }
    }

    @Override
    public void 动作处理() {
        Location loc;
        Entity 目标单位;
        if(驻留)
            目标单位=驻留马甲;
        else
            目标单位=施法单位;
        switch(状态){
            case 0:
                break;
            case 1:
                驻留=!驻留;
                状态=0;
                break;
        }
        角度+=25;
        if(角度>=360){
            角度=0;
        }
        振荡角度+=50;
        if(振荡角度>=360){
            振荡角度=0;
        }
        int 偏移=0;
        for(int i=0;i<2;i++){
            if(!驻留)
                驻留马甲.teleport(施法单位);
            loc=极坐标位移点(目标单位.getLocation(),角度+偏移,2);
            loc=振荡波形(loc, 振荡角度+偏移, 0.5F);
            马甲[i].teleport(loc);
            loc.getWorld().playEffect(马甲[i].getLocation(), Effect.HAPPY_VILLAGER, 0);
            偏移+=180;
        }
        for(int i=0;i<2;i++){
            for(Entity e:马甲[i].getNearbyEntities(0.5, 1, 0.5)){
                if((e instanceof Monster)||(e instanceof Animals)){
                        if(e instanceof Damageable){
                            ((Damageable)e).damage(1, 施法单位);
                        }
                        Vector v=e.getVelocity();
                        v.multiply(0.5);
                        v.setY(v.getY()+0.1);
                        e.setVelocity(v);
                    }
            }
        }
    }

    @Override
    public void 设置动作ID(int id) {
        状态=id;
    }
    
    
}
