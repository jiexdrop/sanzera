package com.jiexdrop.sanzera.control;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.jiexdrop.sanzera.entity.DynamicEntity;

/**
 * Created by jiexdrop on 04/01/18.
 */

public class PlayerController {
    private TextButton left, right, jump;
    private boolean debug = true;

    public PlayerController(TextButton left, TextButton right, TextButton jump) {
        this.left = left;
        this.right = right;
        this.jump = jump;
    }

    public boolean jumping(){
        return jump.isPressed() || Gdx.input.isKeyPressed(Input.Keys.Z);
    }

    public boolean lefting(){
        return left.isPressed() || Gdx.input.isKeyPressed(Input.Keys.Q);
    }

    public boolean goingRight(){
        return right.isPressed() || Gdx.input.isKeyPressed(Input.Keys.D);
    }


    public boolean placeBlock(){
        return Gdx.input.isKeyJustPressed(Input.Keys.SPACE);
    }

    public boolean placeFlag(){
        return Gdx.input.isKeyJustPressed(Input.Keys.F);
    }

    public boolean removeBlock(){
        return Gdx.input.isKeyJustPressed(Input.Keys.X);
    }

    public boolean newLevel(){
        return Gdx.input.isKeyJustPressed(Input.Keys.N);
    }

    public boolean isDebug(){
        return debug;
    }

    public boolean saveLevel() {
        return Gdx.input.isKeyJustPressed(Input.Keys.L);
    }
}
