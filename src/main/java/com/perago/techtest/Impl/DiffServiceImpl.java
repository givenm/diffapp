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
import java.util.HashMap;
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
            Map<String, String> objectData = getObjectData(modified);
            diff.setCreatedInformation(objectData);
        }
        
        //deleted object
        if(original != null && modified == null){
            diff.setDeletedInformation(Boolean.TRUE);
        }
        
        return diff;
    }

    private <T extends Serializable> Map<String, String> getObjectData(T object) throws DiffException {
        Map<String, String> objectInfoMap = new HashMap<>();
        for (Field f : object.getClass().getDeclaredFields()) {
            try {

                if (!"serialVersionUID".equalsIgnoreCase(f.getName())) {
                    Object value = executGetter(f, object);
                    objectInfoMap.put(f.getName(), value == null ? null : String.valueOf(value));
                }
            } catch (IllegalArgumentException ex) {
                Logger.getLogger(DiffServiceImpl.class.getName()).log(Level.SEVERE, null, ex);
                throw new DiffException(ex.getLocalizedMessage());
            }
        }
        return objectInfoMap;
    }

    public <T extends Serializable> Object executGetter(Field field, T o) throws DiffException {
        // find the correct method
        for (Method method : o.getClass().getMethods()) {
            if ((method.getName().startsWith("get")) && (method.getName().length() == (field.getName().length() + 3))) {
                if (method.getName().toLowerCase().endsWith(field.getName().toLowerCase())) {
                    // method found, execut it
                    try {
                        return method.invoke(o);
                    } catch (IllegalAccessException | InvocationTargetException ex) {
                        throw new DiffException(ex.getLocalizedMessage());
                    }

                }
            }
        }
        return null;
    }

}
