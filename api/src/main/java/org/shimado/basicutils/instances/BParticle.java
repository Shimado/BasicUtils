package org.shimado.basicutils.instances;

import org.bukkit.World;

import javax.annotation.Nonnull;

public class BParticle {

    private World world;
    private double x;
    private double y;
    private double z;
    private double angle;
    private double pos;
    private double radius;

    public BParticle(@Nonnull World world, double x, double y, double z, double angle, double pos, double radius) {
        this.world = world;
        this.x = x;
        this.y = y;
        this.z = z;
        this.angle = angle;
        this.pos = pos;
        this.radius = radius;
    }

    @Nonnull
    public World getWorld() {
        return world;
    }

    public void setWorld(@Nonnull World world) {
        this.world = world;
    }


    public double getX() {
        return x;
    }

    public void setX(double x) {
        this.x = x;
    }

    public void addX(double x){
        this.x += x;
    }


    public double getY() {
        return y;
    }

    public void setY(double y) {
        this.y = y;
    }

    public void addY(double y){
        this.y += y;
    }


    public double getZ() {
        return z;
    }

    public void setZ(double z) {
        this.z = z;
    }

    public void addZ(double z){
        this.z += z;
    }


    public double getAngle() {
        return angle;
    }

    public void setAngle(double angle) {
        this.angle = angle;
    }

    public void addAngle(double angle){
        this.angle += angle;
    }


    public double getPos() {
        return pos;
    }

    public void setPos(double pos) {
        this.pos = pos;
    }

    public void addPos(double pos){
        this.pos += pos;
    }


    public double getRadius() {
        return radius;
    }

    public void setRadius(double radius) {
        this.radius = radius;
    }

    public void addRadius(double radius){
        this.radius = radius;
    }

}
