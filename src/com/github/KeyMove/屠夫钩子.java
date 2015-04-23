/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.github.KeyMove;

import java.util.List;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Animals;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Damageable;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Monster;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;
import org.bukkit.util.EulerAngle;
import org.bukkit.util.Vector;

/**
 *
 * @author Administrator
 */
public class 屠夫钩子 extends 盔甲动作{

    int 间隔;
    int 状态;
    int 分段位置;
    int 距离;
    boolean 装备;
    boolean 方向;
    Entity 施法单位;
    ArmorStand 钩子马甲;
    ArmorStand[] 分段马甲;
    ItemStack 钩子;
    ItemStack 分段;
    EulerAngle 旋转角度;
    Location 触发点;
    Entity 钩中单位;
    public 屠夫钩子(Plugin p,Entity e) {
        super(p);
        分段位置=0;
        状态=0;
        间隔=0;
        距离=10;
        施法单位=e;
        钩中单位=null;
        钩子=new ItemStack(Material.DIAMOND_HOE);
        分段=new ItemStack(Material.STICK);
        Location loc=e.getLocation();
        loc.setY(0);
        旋转角度=new EulerAngle(-10*Math.PI/180, 0 , -90*Math.PI/180);
        钩子马甲=创建马甲(loc);
        钩子马甲.setSmall(false);
        //钩子马甲.setItemInHand(钩子);
        钩子马甲.setRightArmPose(EulerAngle.ZERO);
        double v=-10*Math.PI/180;
        钩子马甲.setRightArmPose(钩子马甲.getRightArmPose().setX(v));
        v=-90*Math.PI/180;
        钩子马甲.setRightArmPose(钩子马甲.getRightArmPose().setZ(v));
        分段马甲=new ArmorStand[20];
        for(int i=0;i<20;i++){
            分段马甲[i]=创建马甲(loc);
            分段马甲[i].setSmall(false);
            //分段马甲[i].setItemInHand(分段);
            分段马甲[i].setRightArmPose(钩子马甲.getRightArmPose());
        }
    }

    @Override
    public void 动作处理() {
        if(状态!=1)return;
        if(触发点==null)
            触发点=施法单位.getLocation();
        Location loc=触发点;
        //loc.setY(loc.getY()+1);
        if(!装备){
            钩子马甲.teleport(极坐标位移点(loc.clone(), loc.getYaw(), 1));
            钩子马甲.setItemInHand(钩子);
            for(int i=0;i<20;i++){
                分段马甲[i].teleport(施法单位);
                分段马甲[i].setItemInHand(分段);
            }
            装备=true;
        }
        钩子马甲.teleport(极坐标位移点(loc.clone(), loc.getYaw(), (float)(距离/14)));
        if(!方向)
        {
            int pos=距离/14;
            if(pos!=0)pos--;
            分段马甲[pos].teleport(极坐标位移点(loc.clone(), loc.getYaw(), (float)(距离/14)));
        }
        else
        {
            int pos=距离/14;
            if(pos>=20)pos--;
            分段马甲[pos].teleport(loc);
        }
        
        if(钩中单位!=null){
            钩中单位.teleport(极坐标位移点(loc.clone(), loc.getYaw(), (float)(距离/14)));
            if(钩中单位 instanceof Damageable){
                ((Damageable)钩中单位).damage(1, 施法单位);
            }
        }
        
        for(Entity e:钩子马甲.getNearbyEntities(0.5, 1, 0.5)){
            if((e instanceof Monster)||(e instanceof Animals)){
                钩中单位=e;
                方向=true;
            }
        }
        
        if(方向)
            距离-=8;
        else
            距离+=8;
        if(方向){
        if(距离<=10){
            方向=false;
            距离=10;
            状态=0;
            钩子马甲.setItemInHand(null);
            loc.setY(0);
            钩子马甲.teleport(loc);
            for(int i=0;i<20;i++){
                分段马甲[i].setItemInHand(null);
                分段马甲[i].teleport(loc);
            }
            装备=false;
            触发点=null;
            钩中单位=null;
        }
        }
        else{
            if(距离>=200)
                方向=true;
        }
    }

    @Override
    public void 设置动作ID(int id) {
        状态=id;
    }
    
    
    
}
