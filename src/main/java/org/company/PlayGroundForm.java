package org.company;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.Queue;
import java.util.concurrent.ArrayBlockingQueue;

import static javax.swing.WindowConstants.DISPOSE_ON_CLOSE;

/**
 * Форма, активируемая поле выбора размера поля.
 * В данной форме проистекает игровой процесс.
 * Написанный полностью класс в рамках освоения swing technology.
 */
public class PlayGroundForm implements GameFrameInterface {
    private JFrame playGroundFrame;
    private JPanel bottomPanel;
    private JPanel topPanel;
    public JPanel[][] gamePanel; // ссылка на панель псевдографики
    public JTextArea scoreField; // ссылка на поле с кол-ом очков
    private JLabel scoreLabel;
    private JButton buttonStart;
    private JButton buttonPause;
    private JButton buttonExit;
    private Thread currentGame = null; // ссылка на поток объекта
    private Room room = null; // ссылка на объект Room
    private Queue<KeyEvent> keyEvents = new ArrayBlockingQueue<KeyEvent>(100);
    private MainFrameInterface instanceMain;
    private int xFramePixelSize, yFramePixelSize;


    public PlayGroundForm(int groundSize, MainFrameInterface instanceMain) {
        this.instanceMain = instanceMain; // ссылка на стартовое меню
        this.playGroundFrame = new JFrame("SnakeProject2022oct");

        // top panel
        this.scoreLabel = new JLabel("Текущий счёт: ");
        this.scoreField = new JTextArea();
        this.scoreField.setColumns(3);
        this.scoreField.setRows(1);

        this.topPanel = new JPanel();
        this.playGroundFrame.add(this.topPanel, BorderLayout.PAGE_START);
        this.topPanel.add(this.scoreLabel, BorderLayout.PAGE_START);
        this.topPanel.add(this.scoreField, BorderLayout.PAGE_START);

        // center panel
        this.playGroundFrame.add(PlayGroundForm.this.createGamePanel(groundSize), BorderLayout.CENTER);

        // bottom panel
        this.bottomPanel = new JPanel();
        this.playGroundFrame.add(this.bottomPanel, BorderLayout.PAGE_END);
        this.buttonStart = new JButton("Старт");
        this.buttonStart.addActionListener(new StartListener());
        this.bottomPanel.add(this.buttonStart);
        this.buttonPause = new JButton("Пауза/Продолжить");
        this.buttonPause.addActionListener(new PauseListener());
        this.bottomPanel.add(this.buttonPause);
        this.buttonExit = new JButton("Выход");
        this.buttonExit.addActionListener(new ExitListener());
        this.bottomPanel.add(this.buttonExit);

        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        this.xFramePixelSize = groundSize * 20;
        this.yFramePixelSize = groundSize * 20 + 80;

        this.playGroundFrame.setTitle("SnakeProject2022dec");
        this.playGroundFrame.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        this.playGroundFrame.addWindowListener(new PlayGroundWindowListener());

        this.playGroundFrame.setVisible(true);
        this.playGroundFrame.setSize(this.xFramePixelSize, this.yFramePixelSize);
        this.playGroundFrame.setLocation(
                (screenSize.width - this.xFramePixelSize) / 2 ,
                5 * (screenSize.height - this.yFramePixelSize) / 8
        );
        this.playGroundFrame.setResizable(false);

        this.playGroundFrame.addKeyListener(new KeyboardListener());
        this.playGroundFrame.requestFocus();

        //ВЫЗОВ С 4мя АРГУМЕНТАМИ !!!
        this.room = new Room(this.gamePanel, this.scoreField,PlayGroundForm.this, this.instanceMain); // создание объекта Room
    } // end PlayGroundForm constructor

    /**
     * Метод createGamePanel генерирует псевдографическую панель.
     */
    private JPanel createGamePanel(int groundSize) {
        //
        JPanel panel = new JPanel(new GridLayout(groundSize, groundSize));
        PlayGroundForm.this.gamePanel = new JPanel[groundSize][groundSize];
        for (int yGamePanel = 0; yGamePanel < groundSize; yGamePanel++) {
            for (int xGamePanel = 0; xGamePanel < groundSize; xGamePanel++) {
                JPanel pane = new JPanel();
                pane.setBackground(Color.ORANGE);
                panel.add(pane);
                PlayGroundForm.this.gamePanel[yGamePanel][xGamePanel] = pane; // формирование панель псевдографики
            }
        }
        return panel;
    } // end void createGamePanel

    private class PlayGroundWindowListener extends WindowAdapter {
        @Override
        public void windowClosing(WindowEvent e) {
            PlayGroundForm.this.instanceMain.resetGameFrameLink(); // выключение ссылки на PlayGroundForm
        }
    } // class PlayGroundWindowListener

    /**
     * Класс StartListener стартует игру в новом потоке.
     */
    private class StartListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            if (PlayGroundForm.this.currentGame == null) {
                PlayGroundForm.this.currentGame = new Thread(new Runnable() {
                    @Override
                    public void run() {
                        // в новом потоке запускаю управляющий LightShow
                        PlayGroundForm.this.room.run();
                    }
                });

                PlayGroundForm.this.currentGame.start();
                PlayGroundForm.this.playGroundFrame.requestFocus();
            }
        }
    } // end StartListener

    private class PauseListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            PlayGroundForm.this.room.pauseRoom();
            PlayGroundForm.this.playGroundFrame.requestFocus();
        }
    }

    private class ExitListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            PlayGroundForm.this.closePlayGroundForm();
        }
    } // end ExitListener

    private class KeyboardListener extends KeyAdapter {
        public void keyPressed(KeyEvent e) {
            PlayGroundForm.this.keyEvents.add(e);
        }
    } // end KeyboardListener

    public void closePlayGroundForm() {
        PlayGroundForm.this.currentGame = null;
        PlayGroundForm.this.instanceMain.resetGameFrameLink();
        PlayGroundForm.this.playGroundFrame.dispose();
    }

    @Override
    public boolean hasKeyEvents() {
        return !PlayGroundForm.this.keyEvents.isEmpty();
    }

    @Override
    public KeyEvent getEventFromTop() {
        return PlayGroundForm.this.keyEvents.poll();
    }

    @Override
    public void stopFrame() {
        PlayGroundForm.this.closePlayGroundForm();
    }
}