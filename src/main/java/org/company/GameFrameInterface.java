package org.company;

import java.awt.event.KeyEvent;

public interface GameFrameInterface {
    public void stopFrame();

    public boolean hasKeyEvents();

    public KeyEvent getEventFromTop();
}