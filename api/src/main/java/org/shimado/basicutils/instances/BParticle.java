package org.shimado.basicutils.instances;

import org.bukkit.World;

public class BParticle {

    private World world;
    private double x;
    private double y;
    private double z;
    private double angle;
    private double pos;
    private double radius;

    public BParticle(World world, double x, double y, double z, double angle, double pos, double radius) {
        this.world = world;
        this.x = x;
        this.y = y;
        this.z = z;
        this.angle = angle;
        this.pos = pos;
        this.radius = radius;
    }

    public World getWorld() {
        return this.world;
    }

    public void setWorld(World world) {
        this.world = world;
    }

    public double getX() {
        return this.x;
    }

    public void setX(double x) {
        this.x = x;
    }

    public void addX(double x){
        this.x += x;
    }

    public double getY() {
        return this.y;
    }

    public void setY(double y) {
        this.y = y;
    }

    public void addY(double y){
        this.y += y;
    }

    public double getZ() {
        return this.z;
    }

    public void setZ(double z) {
        this.z = z;
    }

    public void addZ(double z){
        this.z += z;
    }

    public double getAngle() {
        return this.angle;
    }

    public void setAngle(double angle) {
        this.angle = angle;
    }

    public void addAngle(double angle){
        this.angle += angle;
    }

    public double getPos() {
        return this.pos;
    }

    public void setPos(double pos) {
        this.pos = pos;
    }

    public void addPos(double pos){
        this.pos += pos;
    }

    public double getRadius() {
        return this.radius;
    }

    public void setRadius(double radius) {
        this.radius = radius;
    }

    public void addRadius(double radius){
        this.radius = radius;
    }

}
