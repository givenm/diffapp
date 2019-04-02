/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.perago.techtest.Impl;

import com.perago.techtest.ChangedInfo;
import com.perago.techtest.Constants;
import com.perago.techtest.Diff;
import com.perago.techtest.DiffEngine;
import com.perago.techtest.DiffException;
import java.io.Serializable;
import java.lang.reflect.Field;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.commons.lang3.SerializationUtils;
import org.apache.commons.lang3.StringUtils;

/**
 *
 * @author Given Nyauyanga
 */
public class DiffServiceImpl implements DiffEngine {

    @Override
    public <T extends Serializable> T apply(T original, Diff<?> diff) throws DiffException {

        if (diff == null || (diff.getDeletedInformation() == null && diff.getCreatedInformation() == null && diff.getUpdatedInformation() == null)) {
            return null;
        }

        return recreateModifiedObject(original, diff);
    }

    private <T extends Object & Serializable> T recreateModifiedObject(T original, Diff<?> diff) throws DiffException {
        T originalClone = null;
        //reverse engineer fron delete Map
        if (diff.getDeletedInformation() != null) {
            //probably unnessesary check
            if (diff.getDeletedInformation().endsWith(Constants.DELETED_ROOT)) {
                //this means the modifiction had deleted the root object
                originalClone = null;
            }
        }

        if (diff.getUpdatedInformation() != null) {
            originalClone = SerializationUtils.clone(original);
            for (Map.Entry<String, Object> entry : diff.getUpdatedInformation().entrySet()) {
                try {
                    String fieldName = entry.getKey();
                    Field field = originalClone.getClass().getDeclaredField(fieldName);
                    field.setAccessible(true);
                    Object value = entry.getValue();
                    if (value != null && value instanceof Diff) {
                        Diff<?> subDiff = (Diff<?>) value;
                        //we are drilling down to the child object and originalclone becomes child object in recursion
                        T childObject = recreateModifiedObject((T) field.get(originalClone), subDiff);
                        field.set(originalClone, childObject);
                    } else if (value != null && value instanceof ChangedInfo) {
                        ChangedInfo changedInfo = (ChangedInfo) value;
                        field.set(originalClone, changedInfo.getTo());
                    }
                } catch (NoSuchFieldException | SecurityException | IllegalArgumentException | IllegalAccessException ex) {
                    Logger.getLogger(DiffServiceImpl.class.getName()).log(Level.SEVERE, null, ex);
                    throw new DiffException(ex.getLocalizedMessage());
                }
            }
        }

        if (diff.getCreatedInformation() != null) {
            Object newCreatedObject;
            try {
                newCreatedObject = diff.getClassInContext().newInstance();
                for (Map.Entry<String, Object> entry : diff.getCreatedInformation().entrySet()) {

                    String fieldName = entry.getKey();
                    Field field = newCreatedObject.getClass().getDeclaredField(fieldName);
                    field.setAccessible(true);
                    field.set(newCreatedObject, entry.getValue());
                }
                originalClone = (T) newCreatedObject;
            } catch (NoSuchFieldException | SecurityException | IllegalArgumentException | IllegalAccessException | InstantiationException ex) {
                Logger.getLogger(DiffServiceImpl.class.getName()).log(Level.SEVERE, null, ex);
                throw new DiffException(ex.getLocalizedMessage());
            }
        }

        return originalClone;

    }

    @Override
    public <T extends Serializable> Diff<T> calculate(T original, T modified) throws DiffException {

        Diff<T> diff = new Diff<>();

        //newly created object
        if (original == null && modified != null) {
            Map<String, Object> objectData = getObjectData(modified, diff);
            diff.setCreatedInformation(objectData);
            diff.setClassName(modified.getClass().getSimpleName());
        }

        //deleted object
        if (original != null && modified == null) {
            diff.setDeletedInformation(Constants.DELETED_ROOT);
            diff.setClassName(original.getClass().getSimpleName());
        }

        //Updated object
        if (original != null && modified != null && !original.equals(modified)) {
            Map<String, Object> updatedInformation = trackUpdatedInfomation(original, modified, diff);
            diff.setUpdatedInformation(updatedInformation);
            diff.setClassName(original.getClass().getSimpleName());
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
                        Diff<T> subDiff = populateCreatedInformationDiff(value, diff);
                        objectInfoMap.put(field.getName(), subDiff);
                        subDiff.setClassName(object.getClass().getSimpleName());
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

    private <T extends Object & Serializable> Map<String, Object> trackUpdatedInfomation(Object original, Object modified, Diff<T> diff) throws DiffException {
        Map<String, Object> updatedInformationMap = new LinkedHashMap<>();
        for (Field field : original.getClass().getDeclaredFields()) {
            try {

                if (!"serialVersionUID".equalsIgnoreCase(field.getName())) {
                    field.setAccessible(true);

                    Object originalValue = field.get(original);
                    Object modifiedValue = field.get(modified);
                    //We only care about modified values
                    if (originalValue != null && !originalValue.equals(modifiedValue)) {

                    } else if (originalValue == null && modifiedValue != null) {
                        //There is a newly reated 

                    }
                    //check the declaring class type of var and if it's a class declared in our
                    //package then it will need to use it's own Diff plus both objects must not be equal
                    if (field.getType().getTypeName().contains("com.perago") && originalValue != null && modifiedValue != null) {
                        Map<String, Object> trackedChangesMap = trackUpdatedInfomation(originalValue, modifiedValue, diff);
                        Diff<T> subDiff = new Diff<>();
                        subDiff.setUpdatedInformation(trackedChangesMap);
                        updatedInformationMap.put(field.getName(), subDiff);
                        subDiff.setClassName(originalValue.getClass().getSimpleName());
                    } else if (field.getType().getTypeName().contains("com.perago") && originalValue == null && modifiedValue != null) {
                        Diff<T> subDiff = populateCreatedInformationDiff(modifiedValue, diff);
                        updatedInformationMap.put(field.getName(), subDiff);
                    } else if (field.getType().getTypeName().contains("com.perago") && originalValue != null && modifiedValue == null) {
                        Diff<T> subDiff = new Diff<>();
                        subDiff.setDeletedInformation(field.getName());
                        updatedInformationMap.put(field.getName(), subDiff);
                        subDiff.setClassName(originalValue.getClass().getSimpleName());
                    } else if (!StringUtils.equals(String.valueOf(originalValue), String.valueOf(modifiedValue))) {
                        ChangedInfo changedInfo = new ChangedInfo(originalValue == null ? null : String.valueOf(originalValue), modifiedValue == null ? null : String.valueOf(modifiedValue));
                        changedInfo.setDataTypeClass(originalValue.getClass());
                        updatedInformationMap.put(field.getName(), changedInfo);
                        
                    }

                }
            } catch (IllegalArgumentException | IllegalAccessException ex) {
                Logger.getLogger(DiffServiceImpl.class.getName()).log(Level.SEVERE, null, ex);
                throw new DiffException(ex.getLocalizedMessage());
            }
        }
        return updatedInformationMap;
    }

    private <T extends Object & Serializable> Diff<T> populateCreatedInformationDiff(Object modifiedValue, Diff<T> diff) throws DiffException {
        //we have a newly created object here
        Map<String, Object> objectData = getObjectData(modifiedValue, diff);
        Diff<T> subDiff = new Diff<>();
        subDiff.setCreatedInformation(objectData);
        subDiff.setClassName(modifiedValue.getClass().getSimpleName());
        subDiff.setClassInContext(modifiedValue.getClass());
        return subDiff;
    }

}
