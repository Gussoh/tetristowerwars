/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.tetristowerwars.model;

import org.jbox2d.dynamics.Body;

/**
 *
 * @author Administrator
 */
public class TriggerBlock extends Block {

    private final Runnable runnable;
    private final String text;

    public TriggerBlock(Body body, String text, Runnable runnable) {
        super(body);
        this.text = text;
        this.runnable = runnable;
    }

    public Runnable getRunnable() {
        return runnable;
    }

    public String getText() {
        return text;
    }
}
