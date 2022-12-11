package org.company;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;

/**
 * Класс, отвечающий за аккумуляцию игрового процесса.
 * Класс написан полностью в рамках освоения swing technology.
 */
public class Room {
    public Object lockObject = new Object();
    public boolean isRoomPaused = true;
    private JPanel[][] playGround;
    private GameFrameInterface instancePlay;
    private MainFrameInterface instanceMain;
    private int xPlayGroundMax, yPlayGroundMax;
    private Snake snake;
    private Mouse mouse;
    private int score;
    private JTextArea scoreArea;

    public int getPlayGroundWidth() {
        return xPlayGroundMax;
    }

    public int getPlayGroundHeight() {
        return yPlayGroundMax;
    }

    public Mouse getMouse() {
        return mouse;
    }

    public Room(JPanel[][] playGround, JTextArea scoreArea, GameFrameInterface instancePlay, MainFrameInterface instanceMain) {
        this.instancePlay = instancePlay;
        this.instanceMain = instanceMain;
        this.playGround = playGround; // ссылка на псевдографическую панель
        this.scoreArea = scoreArea;
        this.yPlayGroundMax = playGround.length;
        this.xPlayGroundMax = playGround[0].length;
        this.snake = new Snake(this.xPlayGroundMax / 2, this.yPlayGroundMax / 2, Room.this);
        this.snake.setDirection(SnakeDirection.DOWN);
        this.score = 1;
        this.scoreArea.setText(Integer.toString(this.score));
        this.createMouse();
        this.print();
        System.out.println("Room was created");
    } // end Room constructor

    /**
     * Метод run() активирует все основные события игры
     */
    public void run() {
        Room.this.isRoomPaused = false;
        System.out.println("void Room.run() started.");
        while (Room.this.snake.isAlive()) {
            //"наблюдатель" содержит события о нажатии клавиш?
            if (Room.this.instancePlay.hasKeyEvents()) {
                KeyEvent event = Room.this.instancePlay.getEventFromTop();
                //Если "стрелка влево" - сдвинуть фигурку влево
                if (event.getKeyCode() == KeyEvent.VK_LEFT)
                    Room.this.snake.setDirection(SnakeDirection.LEFT);
                    //Если "стрелка вправо" - сдвинуть фигурку вправо
                else if (event.getKeyCode() == KeyEvent.VK_RIGHT)
                    Room.this.snake.setDirection(SnakeDirection.RIGHT);
                    //Если "стрелка вверх" - сдвинуть фигурку вверх
                else if (event.getKeyCode() == KeyEvent.VK_UP)
                    Room.this.snake.setDirection(SnakeDirection.UP);
                    //Если "стрелка вниз" - сдвинуть фигурку вниз
                else if (event.getKeyCode() == KeyEvent.VK_DOWN)
                    Room.this.snake.setDirection(SnakeDirection.DOWN);
            }

            Room.this.snake.move();   // Moving the snake
            Room.this.print();        // Rendering the current state of the game
            try {
                sleep();    // Executing the pause between moves
            } catch (InterruptedException ex) {
                System.out.println("Room.sleep - InterruptedException");
            }
        }

        System.out.println("Game Over!");
        new GameOverForm(Room.this.instanceMain, Room.this.instancePlay);
        //new GameOverForm();
    } // end void run()

    /**
     * Метод sleep() управляет скоростью движения змеи и постановкой игры на паузу
     */
    public void sleep() throws InterruptedException { // This method is working with game speed
        if (Room.this.isRoomPaused) {
            synchronized (Room.this.lockObject) {
                Room.this.lockObject.wait(); // Executing the pause state of current game
            }
        }
        else {
            if (this.score < 6) Thread.sleep(500);
            else if (Room.this.score >=6 && Room.this.score < 11) Thread.sleep(400);
            else if (Room.this.score >=11 && Room.this.score < 16) Thread.sleep(300);
            else if (Room.this.score >=16 && Room.this.score < 21) Thread.sleep(200);
            else if (Room.this.score >=21 && Room.this.score < 26) Thread.sleep(150);
            else Thread.sleep(100);
        }
    } // end void sleep()

    /**
     * Метод print() отображает текущего состояния игры
     */
    public void print() { // This method is rendering the current state of the game
        // Поле
        for (int y = 0; y < Room.this.yPlayGroundMax; y++) {
            for (int x = 0; x < Room.this.xPlayGroundMax; x++) Room.this.playGround[y][x].setBackground(Color.ORANGE);
        }

        // Голова змеи
        this.playGround[Room.this.snake.getSections().get(0).getY()]
                [Room.this.snake.getSections().get(0).getX()]
                .setBackground(Room.this.snake.isAlive() ? Color.BLUE : Color.MAGENTA);

        // Тело змеи
        for (int i = 1; i < Room.this.snake.getSections().size(); i++) {
            Room.this.playGround[Room.this.snake.getSections().get(i).getY()]
                    [Room.this.snake.getSections().get(i).getX()]
                    .setBackground(Color.DARK_GRAY);
        }

        // Мышь
        Room.this.playGround[Room.this.mouse.getY()][Room.this.mouse.getX()].setBackground(Color.RED);
    } // end void print()

    public void createMouse() {
        int x = (int) (Math.random() * Room.this.xPlayGroundMax);
        int y = (int) (Math.random() * Room.this.yPlayGroundMax);
        Room.this.mouse = new Mouse(x, y);
    }

    public void eatMouse() {
        Room.this.score++;
        Room.this.scoreArea.setText(null); // clearing the previous value in the form
        Room.this.scoreArea.setText(Integer.toString(score)); // output a new value
        Room.this.createMouse();
    }

    public void pauseRoom() {
        if (!Room.this.isRoomPaused) {
            synchronized (Room.this.lockObject) {
                Room.this.isRoomPaused = true;
                System.out.println("Paused");
            }
        }
        else {
            Room.this.continueRoom();
        }
    }

    public void continueRoom() {
        if (Room.this.isRoomPaused) {
            synchronized (Room.this.lockObject) {
                Room.this.isRoomPaused = false;
                Room.this.lockObject.notify();
                System.out.println("Resumed");
            }
        }
    }
}