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
public enum Operations {

    CREATE("Create: "), UPDATE("Update: "), DELETE("Delete: ");

    private final String operationname;

    private Operations(String operationname) {
        this.operationname = operationname;
    }

    public String getOperationname() {
        return operationname;
    }

}
