package org.company;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

import static javax.swing.WindowConstants.EXIT_ON_CLOSE;

public class GameOverForm implements GameFrameInterface {
    private JFrame gameOverFrame;
    private JPanel centerPanel;
    private JPanel bottomPanel;
    private JLabel gameOverLabel;
    private JButton buttonExit;
    private GameFrameInterface instancePlay; // ссылка на playGroundForm
    private MainFrameInterface instanceMain; // ссылка на mainForm
    private int xFramePixelSize = 300;
    private int yFramePixelSize = 100;

    public GameOverForm (MainFrameInterface instanceMain, GameFrameInterface instancePlay) {
        this.instanceMain = instanceMain;
        this.instancePlay = instancePlay;

        this.gameOverFrame = new JFrame("SnakeProject2022dec");

        // center panel
        this.centerPanel = new JPanel();
        this.gameOverFrame.add(centerPanel, BorderLayout.CENTER);
        this.gameOverLabel = new JLabel("Игра окончена !");
        this.centerPanel.add(gameOverLabel);

        // bottom panel
        this.bottomPanel = new JPanel();
        this.gameOverFrame.add(bottomPanel, BorderLayout.PAGE_END);
        this.buttonExit = new JButton("Хорошо");
        this.buttonExit.addActionListener(new GameOverListener());
        this.bottomPanel.add(buttonExit);

        Dimension screen = Toolkit.getDefaultToolkit().getScreenSize();

        this.gameOverFrame.setDefaultCloseOperation(EXIT_ON_CLOSE);
        this.gameOverFrame.addWindowListener(new GameOverWindowListener());

        this.gameOverFrame.setVisible(true);
        this.gameOverFrame.setSize(xFramePixelSize, yFramePixelSize);
        this.gameOverFrame.setLocation(
                (screen.width - GameOverForm.this.xFramePixelSize) / 2 ,
                (screen.height - GameOverForm.this.yFramePixelSize) / 2
        );
        this.gameOverFrame.setResizable(false);
    } // end GameOverForm constructor

    private class GameOverWindowListener extends WindowAdapter {
        @Override
        public void windowClosing(WindowEvent e) {
            GameOverForm.this.stopFrame();
        }
    } // class PlayGroundWindowListener

    private class GameOverListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            GameOverForm.this.stopFrame();
        }
    } // end ExitListener

    @Override
    public void stopFrame() {
        GameOverForm.this.instanceMain.resetGameFrameLink(); // выключение PlayGroundForm
        GameOverForm.this.instancePlay.stopFrame();
        GameOverForm.this.gameOverFrame.dispose();
    }

    @Override
    public boolean hasKeyEvents() {
        // EMPTY
        return false;
    }

    @Override
    public KeyEvent getEventFromTop() {
        // EMPTY
        return null;
    }
}