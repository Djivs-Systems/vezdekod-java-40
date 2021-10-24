package main.java;

import lib.Window;
import lib.render.Texture;
import org.lwjgl.glfw.GLFW;
import org.lwjgl.opengl.GL11;

import java.awt.*;
import java.util.concurrent.ThreadLocalRandom;

import java.awt.geom.Point2D;
import java.util.ArrayList;

public class Game extends Window {
    private double thickness = 3;
    private int circle_increase_step = 10;

    private final Texture backgroundTexture = Texture.load("back.png");

    Circle mainCircle, cursorCircle;

    public Game() {
        super(800, 600, "Vezdekod :3", true, "Arial", 30);
        mainCircle = new Circle(new Point2D.Double(400, 300), 200);
        cursorCircle = new Circle(new Point2D.Double(400, 300), 50);
    }

    class Circle {
        private int radius;
        private int color;
        private Point2D.Double center;
        private ArrayList<Point2D.Double> pointsArray;
        private final int angles_amount = 100;
        private final int screenHeight = 600;

        public Circle(Point2D.Double circle_center, int radius) {
            setColor(0);
            pointsArray = new ArrayList<Point2D.Double>();

            this.radius = radius;
            this.center = circle_center;
            fillPointsArray();
        }

        public void setColor(int color) {
            this.color = color;
        }
        public void setRadius(int radius) {
            if (radius <= 0 || radius * 2 > screenHeight)
                return;
            this.radius = radius;
            fillPointsArray();
        }

        public void setCenter(Point2D.Double circle_center) {
            this.center = circle_center;
            fillPointsArray();
        }
        public int getRadius() {
            return radius;
        }
        public Point2D.Double getCenter() {
            return center;
        }
        public int getColor() {
            return color;
        }

        public void drawOnCanvas(lib.render.Canvas canvas, double thickness) {
            for (int i = 1; i < angles_amount; ++i) {
                //canvas.drawTriangle(center.x, center.y, pointsArray.get(i).x, pointsArray.get(i).y,pointsArray.get(i-1).x, pointsArray.get(i-1).y, color);
                canvas.drawLine(color, pointsArray.get(i).x, pointsArray.get(i).y,pointsArray.get(i-1).x, pointsArray.get(i-1).y, thickness);
            }
            //canvas.drawTriangle(center.x, center.y, pointsArray.get(0).x, pointsArray.get(0).y,pointsArray.get(angles_amount-1).x, pointsArray.get(angles_amount-1).y, color);
            canvas.drawLine(color, pointsArray.get(angles_amount-1).x, pointsArray.get(angles_amount-1).y,pointsArray.get(0).x, pointsArray.get(0).y, thickness);
        }

        private void fillPointsArray() {
            if (pointsArray.size() > 0)
                pointsArray.clear();
            double rotating_angle = 360.0/angles_amount;
            double passed_angle = 0;
            double x, y;
            pointsArray.add(new Point2D.Double(center.x - radius, center.y));
            for (int i = 0; i < angles_amount - 1; ++i) {
                passed_angle += rotating_angle;
                x = center.x - Math.cos(Math.toRadians(passed_angle)) * radius;
                y = center.y - Math.sin(Math.toRadians(passed_angle)) * radius;
                pointsArray.add(new Point2D.Double(x, y));
            }
        }

    }

    private void processCirclesCrossing() {
        double dist = Math.sqrt(Math.pow(mainCircle.getCenter().x - cursorCircle.getCenter().x, 2) +  Math.pow(mainCircle.getCenter().y - cursorCircle.getCenter().y, 2));
        if (dist <= mainCircle.getRadius() + cursorCircle.getRadius() && dist > mainCircle.getRadius() - cursorCircle.getRadius())
            cursorCircle.setColor(-100);
        else if (cursorCircle.getColor() == -100)
            cursorCircle.setColor(0);
    }
    @Override
    protected void onFrame(double elapsed) {
        canvas.drawTexture(backgroundTexture, 0, 0, width, height, width, height);
        mainCircle.drawOnCanvas(canvas, thickness);
        if (cursorOver)
            cursorCircle.drawOnCanvas(canvas, thickness);
    }

    @Override
    protected void onKeyButton(int key, int scancode, int action, int mods) {
        if (key == GLFW.GLFW_KEY_UP && action == GLFW.GLFW_PRESS) {
            mainCircle.setRadius(mainCircle.getRadius() + circle_increase_step);
            processCirclesCrossing();
        } else if (key == GLFW.GLFW_KEY_DOWN && action == GLFW.GLFW_PRESS) {
            mainCircle.setRadius(mainCircle.getRadius() - circle_increase_step);
            processCirclesCrossing();
        }
    }

    @Override
    protected void onScroll(double dx, double dy) {
        switch ((int) dy) {
            case -1:
                mainCircle.setRadius(mainCircle.getRadius() - circle_increase_step);
                break;
            case 1:
                mainCircle.setRadius(mainCircle.getRadius() + circle_increase_step);
                break;
            default:
                return;
        }
        processCirclesCrossing();
    }

    @Override
    protected void onCursorMoved(double x, double y) {
        cursorX = x;
        cursorY = y;
        cursorCircle.setCenter(new Point2D.Double(cursorX, cursorY));
        processCirclesCrossing();
    }

    public static void main(String[] args) {
        new Game().show();
    }
};