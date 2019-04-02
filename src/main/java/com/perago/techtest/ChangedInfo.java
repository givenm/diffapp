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
public class ChangedInfo {

    private Class<?> dataTypeClass;
    private String from;
    private String to;

    public ChangedInfo(String from, String to) {
        this.from = from;
        this.to = to;
    }

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public String getTo() {
        return to;
    }

    public void setTo(String to) {
        this.to = to;
    }

    public Class<?> getDataTypeClass() {
        return dataTypeClass;
    }

    public void setDataTypeClass(Class<?> dataTypeClass) {
        this.dataTypeClass = dataTypeClass;
    }

}
