/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.github.KeyMove;

import static java.lang.System.out;
import java.lang.reflect.Field;
import org.bukkit.Effect;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.craftbukkit.v1_8_R1.entity.CraftArmorStand;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.util.EulerAngle;
import org.bukkit.util.Vector;

/**
 *
 * @author Administrator
 */
public class ArmorStandSkill extends JavaPlugin implements Listener{

    @Override
    public void onEnable() {
        getServer().getPluginManager().registerEvents(this, this);
    }

    //P ↑  Y ←
    Location 计算圆面向(Location 点,int 面向距离,int 偏移量,int 角度){
        double x,y;
        Location 目标点=点;
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
    
    class tc implements Runnable{

        Location p;
        Player player;
        ArmorStand e1;
        ArmorStand e2;
        ArmorStand e3;
        int deg=0;
        public tc(Player p){
            this.player=p;
            this.p=this.player.getLocation();
            deg=350;
            e1=创建马甲(this.p);
            e2=创建马甲(this.p);
            e3=创建马甲(this.p);
            e1.setHelmet(new ItemStack(Material.SKULL_ITEM));
            e2.setHelmet(new ItemStack(Material.SKULL_ITEM));
            e3.setHelmet(new ItemStack(Material.SKULL_ITEM));
        }
        
        @Override
        public void run() {
            //p=player.getLocation();
            float yc=player.getLocation().getYaw();
            Location loc=计算圆面向(player.getLocation(),30,1,deg);
            loc.setYaw(yc);
            e1.teleport(loc);
            loc=计算圆面向(player.getLocation(),30,1,(deg+120)%360);
            loc.setYaw(yc);
            e2.teleport(loc);
            loc=计算圆面向(player.getLocation(),30,1,(deg+240)%360);
            loc.setYaw(yc);
            e3.teleport(loc);
            deg+=10;
            if(deg>=360)
                deg-=360;
        }
    }
    
    屠夫钩子 g=null;
    
    @EventHandler void Int(PlayerInteractEvent e){
        if(g!=null){
            g.设置动作ID(1);
        }
    }
    
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if(!(sender instanceof Player))return false;
        Player p=(Player)sender;
        if(g==null){
            g=new 屠夫钩子(this, p);
            g.执行动作();
        }
        
        //盔甲动作 d=new 盔甲动作(this);
        //d.执行动作();
        //getServer().getScheduler().scheduleSyncRepeatingTask(this, new tc(p), 1, 1);
        //getServer().broadcastMessage("P:"+p.getLocation().getPitch()+" Y:"+p.getLocation().getYaw());
        //Vector v=p.getLocation().getDirection();
       // getServer().broadcastMessage(v.toString());
       // p.getLocation().setPitch(90);
        //v=p.getLocation().getDirection();
       // getServer().broadcastMessage(v.toString());
       // v.multiply(5);
       // v.setY(0);
       // p.teleport(p.getLocation().add(v));
        return true;
    }

    
    
}
