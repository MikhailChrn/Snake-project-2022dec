package org.company;

import java.util.ArrayList;
import java.util.List;

/**
 * Класс змея.
 * Класс заимствован из JavaRush
 * Класс доработан косметически.
 */
public class Snake {
    // Ссылка на вызывающее окно
    private Room room;
    //Направление движения змеи
    private SnakeDirection direction;
    //Состояние - жива змея или нет.
    private boolean isAlive;
    //Список кусочков змеи.
    private List<SnakeSection> sections;

    public Snake(int x, int y, Room room) {
        Snake.this.room = room;
        Snake.this.sections = new ArrayList<>();
        Snake.this.sections.add(new SnakeSection(x, y));
        Snake.this.isAlive = true;
    }

    public boolean isAlive() {
        return Snake.this.isAlive;
    }

    public SnakeDirection getDirection() {
        return Snake.this.direction;
    }

    public void setDirection(SnakeDirection direction) {
        Snake.this.direction = direction;
    }

    public List<SnakeSection> getSections() {
        return Snake.this.sections;
    }

    /**
     * Метод перемещает змею на один ход.
     * Направление перемещения задано переменной direction.
     */
    public void move() {
        if (!Snake.this.isAlive) return;

        if (Snake.this.direction == SnakeDirection.UP)
            move(0, -1);
        else if (Snake.this.direction == SnakeDirection.RIGHT)
            move(1, 0);
        else if (Snake.this.direction == SnakeDirection.DOWN)
            move(0, 1);
        else if (Snake.this.direction == SnakeDirection.LEFT)
            move(-1, 0);
    }

    /**
     * Метод перемещает змею в соседнюю клетку.
     * Координаты клетки заданы относительно текущей головы с помощью переменных (dx, dy).
     */
    void move(int dx, int dy) {
        //Создаем новую голову - новый "кусочек змеи".
        SnakeSection head = sections.get(0);
        head = new SnakeSection(head.getX() + dx, head.getY() + dy);

        //Проверяем - не вылезла ли голова за границу комнаты
        checkBorders(head);
        if (!Snake.this.isAlive) return;

        //Проверяем - не пересекает ли змея саму себя
        checkBody(head);
        if (!Snake.this.isAlive) return;

        //Проверяем - не съела ли змея мышь.
        Mouse mouse = this.room.getMouse();
        if (head.getX() == mouse.getX() && head.getY() == mouse.getY()) //съела
        {
            Snake.this.sections.add(0, head);                  //Добавили новую голову
            Snake.this.room.eatMouse();                   //Хвост не удаляем, но создаем новую мышь.
        } else //просто движется
        {
            Snake.this.sections.add(0, head);                  //добавили новую голову
            Snake.this.sections.remove(sections.size() - 1);   //удалили последний элемент с хвоста
        }
    }

    /**
     * Метод проверяет - находится ли новая голова в пределах комнаты
     */
    private void checkBorders(SnakeSection head) {
        if ((head.getX() < 0 ||
                head.getX() >= Snake.this.room.getPlayGroundWidth()) ||
                head.getY() < 0 ||
                head.getY() >= Snake.this.room.getPlayGroundHeight())
        {
            Snake.this.isAlive = false;
        }
    }

    /**
     * Метод проверяет - не совпадает ли голова с каким-нибудь участком тела змеи.
     */
    private void checkBody(SnakeSection head) {
        if (Snake.this.sections.contains(head)) {
            Snake.this.isAlive = false;
        }
    }
}