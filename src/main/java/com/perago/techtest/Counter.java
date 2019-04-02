/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.perago.techtest;

/**
 *
 * @author Given Nyauyanga
 */
public class Counter {

    private int root;
    private int position;
    private Operations currentOperation;
    private Counter subCounter;

    public Counter(int root, int position) {
        this.root = root;
        this.position = position;
    }

    public int getRoot() {
        return root;
    }

    public void setRoot(int root) {
        this.root = root;
    }

    public int getPosition() {
        return position;
    }

    public Operations getCurrentOperation() {
        return currentOperation;
    }

    public void setCurrentOperation(Operations currentOperation) {
        this.currentOperation = currentOperation;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public Counter getSubCounter() {
        return subCounter;
    }

    public void setSubCounter(Counter subCounter) {
        this.subCounter = subCounter;
    }

}
