package org.company;

public interface MainFrameInterface {
    public void resetGameFrameLink();
    public void stopFrame();
    /**
     * Вот это мне архитектурно не нравится - передача экземпляра Main.
     * Ты хочешь чтобы дернули метод на instanceMain.
     * Тогда сделай интерфейс, с методом onGameFinished(), который реализует класс Main и в его реализации делай что нужно делать по окончании игры.
     * Отдавай Main по этому интерфейсу. Тогда твой Main и PlayGroundForm становятся независимыми.
     */
}