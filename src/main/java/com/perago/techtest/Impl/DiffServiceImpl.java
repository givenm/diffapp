/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.perago.techtest.Impl;

import com.perago.techtest.Diff;
import com.perago.techtest.DiffEngine;
import com.perago.techtest.DiffException;
import java.io.Serializable;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Given Nyauyanga
 */
public class DiffServiceImpl implements DiffEngine {

    @Override
    public <T extends Serializable> T apply(T original, Diff<?> diff) throws DiffException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public <T extends Serializable> Diff<T> calculate(T original, T modified) throws DiffException {

        Diff<T> diff = new Diff<>();

        //newly created object
        if (original == null && modified != null) {
            Map<String, Object> objectData = getObjectData(modified, diff);
            diff.setCreatedInformation(objectData);
        }

        //deleted object
        if (original != null && modified == null) {
            diff.setDeletedInformation(Boolean.TRUE);
        }

        //Updated object
        if (original != null && modified != null && !original.equals(modified)) {

        }

        return diff;
    }

    private <T extends Serializable> Map<String, Object> getObjectData(Object object, Diff<T> diff) throws DiffException {
        Map<String, Object> objectInfoMap = new LinkedHashMap<>();
        for (Field field : object.getClass().getDeclaredFields()) {
            try {

                if (!"serialVersionUID".equalsIgnoreCase(field.getName())) {
                    field.setAccessible(true);

                    Object value = field.get(object);
                    //check the declaring class type of var and if it's a class declared in our package then it will need to use it's own Diff
                    if (field.getType().getTypeName().contains("com.perago") && value != null) {
                        Map<String, Object> objectData = getObjectData(field.get(object), diff);
                        Diff<T> subDiff = new Diff<>();
                        subDiff.setCreatedInformation(objectData);
                        objectInfoMap.put(field.getName(), subDiff);
                    } else {                        
                        objectInfoMap.put(field.getName(), value == null ? null : String.valueOf(value));
                    }
                }
            } catch (IllegalArgumentException | IllegalAccessException ex) {
                Logger.getLogger(DiffServiceImpl.class.getName()).log(Level.SEVERE, null, ex);
                throw new DiffException(ex.getLocalizedMessage());
            }
        }
        return objectInfoMap;
    }

}
