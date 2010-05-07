/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.tetristowerwars.model;

import org.jbox2d.dynamics.Body;
import org.tetristowerwars.model.material.WoodMaterial;

/**
 *
 * @author Administrator
 */
public class TriggerBlock extends Block {

    private final TriggerListener triggerListener;
    private String text;
    private boolean visibility = false;

    public void setVisible(boolean visibility) {
        this.visibility = visibility;
    }

    public TriggerBlock(Body body, String text, TriggerListener triggerListener) {
        super(body, new WoodMaterial());
        this.text = text;
        this.triggerListener = triggerListener;
    }

    public TriggerListener getTriggerListener() {
        return triggerListener;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public boolean isVisible() {
        return visibility;
    }
}
