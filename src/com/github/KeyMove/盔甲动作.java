/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.github.KeyMove;

import static java.lang.System.out;
import java.lang.reflect.Field;
import java.util.Map;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_8_R1.entity.CraftArmorStand;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.EntityType;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

/**
 *
 * @author Administrator
 */
public class 盔甲动作 {
    Plugin 主插件;
    int ID;
    public 盔甲动作(Plugin p){
        this.主插件=p;
    }
    
    public void 停止动作(){
        Bukkit.getServer().getScheduler().cancelTask(ID);
    }
    
    public boolean 反射法修改私有成员变量(Object 类,String 名称,Object 值){
        try {
            Field f=类.getClass().getDeclaredField(名称);
            f.setAccessible(true);
            f.set(类, 值);
            return true;
        } catch (NoSuchFieldException | SecurityException | IllegalArgumentException | IllegalAccessException ex) {
            out.print(ex);
        }
        return false;
    }
    
    ArmorStand 创建马甲(Location 点){
        ArmorStand 盔甲架=(ArmorStand) 点.getWorld().spawnEntity(点, EntityType.ARMOR_STAND);
        盔甲架.setBasePlate(false);
        盔甲架.setGravity(false);
        盔甲架.setVisible(false);
        盔甲架.setSmall(true);
        反射法修改私有成员变量(((CraftArmorStand)盔甲架).getHandle(),"bg",31);//修改DisabledSlots值
        return 盔甲架;
    }
    //P ↑  Y ←
    Location 极坐标位移点(Location 从点开始,float 角度为,float 距离){
        Location 目标点=从点开始;
        目标点.setPitch(0);
        目标点.setYaw((角度为)%360);
        return 目标点.add(目标点.getDirection().multiply(距离));
    }
    
    Location 振荡波形(Location 从点开始,int 角度,float 幅度){
        Location 目标点=从点开始;
        目标点.setY(目标点.getY()+(Math.cos(Math.PI/180*角度)*幅度));
        return 目标点;
    }
    
    public void 执行动作(){
        ID=Bukkit.getServer().getScheduler().scheduleSyncRepeatingTask(主插件,new BukkitRunnable() {
            @Override
            public void run() {
                动作处理();
            }
        } , 1, 1);
    }
    
    public void 动作处理(){
        
    }
    
    public void 设置动作ID(int id){
        
    }
    
}
