package org.company;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

import static javax.swing.WindowConstants.EXIT_ON_CLOSE;

/**
 * Класс являющийся точкой входа в программу.
 * Класс написан в рамках освоения swing technology.
 */
public class Main implements MainFrameInterface {
    private JFrame mainFrame;
    private JPanel centerPanel;
    private JPanel bottomPanel;
    private JLabel startLabel;
    private JComboBox comboBox;
    private String[] items = {
            "15x15",
            "20x20",
            "25x25"
    };
    private JButton buttonStart;
    private JButton buttonExit;
    private GameFrameInterface instancePlay; // ссылка на playGroundForm

    int xFramePixelSize = 300;
    int yFramePixelSize = 100;

    public Main() {
        // center panel
        this.mainFrame = new JFrame("SnakeProject2022dec");
        this.centerPanel = new JPanel();
        this.mainFrame.add(centerPanel, BorderLayout.CENTER);
        this.startLabel = new JLabel("Размер игрового поля :");
        this.centerPanel.add(startLabel);
        this.comboBox = new JComboBox<>(items);
        this.centerPanel.add(comboBox);

        // bottom panel
        this.bottomPanel = new JPanel();
        this.mainFrame.add(bottomPanel, BorderLayout.PAGE_END);
        this.buttonStart = new JButton("Старт");
        this.buttonStart.addActionListener(new StartListener());
        this.bottomPanel.add(buttonStart);
        this.buttonExit = new JButton("Выход");
        this.buttonExit.addActionListener(new ExitListener());
        this.bottomPanel.add(buttonExit);

        Dimension screen = Toolkit.getDefaultToolkit().getScreenSize();

        this.mainFrame.setDefaultCloseOperation(EXIT_ON_CLOSE);
        this.mainFrame.setVisible(true);
        this.mainFrame.setSize(this.xFramePixelSize, this.yFramePixelSize);
        this.mainFrame.setLocation(
                (screen.width - this.xFramePixelSize) / 2 ,
                (screen.height - this.yFramePixelSize) / 4);
        this.mainFrame.setResizable(false);
    } // end Main constructor

    /**
     * Класс StartListener стартует PlayGroundForm в новом потоке.
     */
    private class StartListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            String string = (String) Main.this.comboBox.getSelectedItem();
            int groundSize = Integer.valueOf(string.substring(0,2));

            if (Main.this.instancePlay == null) {
                new Thread(() -> Main.this.startGame(groundSize)).start();
            } else return;
        }
    } // end startListener

    private void startGame (int groundSize) {
        Main.this.instancePlay = new PlayGroundForm(groundSize, Main.this);
    }

    private class ExitListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            Main.this.stopFrame();
        }
    } // end startListener

    @Override
    public void resetGameFrameLink() {
        Main.this.instancePlay = null;
    }

    @Override
    public void stopFrame() {
        Main.this.resetGameFrameLink();
        System.exit(0);
    }

    public static void main(String[] args) {
        new Main();
    } // end void main
}