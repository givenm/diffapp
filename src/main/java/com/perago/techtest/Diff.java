package com.perago.techtest;

import java.io.Serializable;
import java.util.Map;

/**
 * The object representing a diff. Diffs must reflect all information that was
 * created/updated/deleted. Information that was not changed must not be
 * reflected in a Diff.
 *
 * @param <T>
 */
public class Diff<T extends Serializable> {

    private Map<String, Object> createdInformation;
    private Map<String, Object> updatedInformation;
    private String deletedInformation;
    private String className;
    private Class<?> classInContext;

    public Diff() {
    }

    public Map<String, Object> getCreatedInformation() {
        return createdInformation;
    }

    public void setCreatedInformation(Map<String, Object> createdInformation) {
        this.createdInformation = createdInformation;
    }

    public Map<String, Object> getUpdatedInformation() {
        return updatedInformation;
    }

    public void setUpdatedInformation(Map<String, Object> updatedInformation) {
        this.updatedInformation = updatedInformation;
    }

    public String getDeletedInformation() {
        return deletedInformation;
    }

    public void setDeletedInformation(String deletedInformation) {
        this.deletedInformation = deletedInformation;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public Class<?> getClassInContext() {
        return classInContext;
    }

    public void setClassInContext(Class<?> classInContext) {
        this.classInContext = classInContext;
    }

}
