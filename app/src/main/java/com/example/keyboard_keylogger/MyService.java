package com.example.keyboard_keylogger;

import android.content.Intent;
import android.inputmethodservice.InputMethodService;
import android.inputmethodservice.Keyboard;
import android.inputmethodservice.KeyboardView;
import android.os.IBinder;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.InputConnection;

public class MyService extends InputMethodService implements KeyboardView.OnKeyboardActionListener{

    private KeyboardView kv;
    private Keyboard keyboard;

    private boolean isCaps = false;


    @Override
    public View onCreateInputView(){
        kv = (KeyboardView)getLayoutInflater().inflate(R.layout.keyboard,null);
        keyboard = new Keyboard(this,R.xml.qwerty);
        kv.setKeyboard(keyboard);
        kv.setOnKeyboardActionListener(this);
        return kv;
    }

    @Override
    public void onPress(int i) {

    }

    @Override
    public void onRelease(int i) {

    }

    @Override
    public void onKey(int i, int[] ints) {
        InputConnection ic = getCurrentInputConnection();
        //playClick(i);
        System.out.println(i);
        switch (i) {
            case Keyboard.KEYCODE_DELETE:
                ic.deleteSurroundingText(1,0);break;
            case Keyboard.KEYCODE_SHIFT:
                isCaps =!isCaps;
                keyboard.setShifted(isCaps);
                kv.invalidateAllKeys();
                break;
            case Keyboard.KEYCODE_DONE:
                ic.sendKeyEvent(new KeyEvent(KeyEvent.ACTION_DOWN,KeyEvent.KEYCODE_ENTER));break;
            default:
                char code = (char)i;
                if (Character.isLetter(code) && isCaps) code = Character.toUpperCase(code);
                ic.commitText(String.valueOf(code),1);

        }
    }

    @Override
    public void onText(CharSequence charSequence) {

    }

    @Override
    public void swipeLeft() {

    }

    @Override
    public void swipeRight() {

    }

    @Override
    public void swipeDown() {

    }

    @Override
    public void swipeUp() {

    }
}