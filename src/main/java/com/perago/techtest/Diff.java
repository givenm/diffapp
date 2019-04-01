package com.perago.techtest;

import java.io.Serializable;
import java.util.LinkedList;
import java.util.Map;

/**
 * The object representing a diff. Diffs must reflect all information that was
 * created/updated/deleted. Information that was not changed must not be
 * reflected in a Diff.
 *
 * @param <T>
 */
public class Diff<T extends Serializable> {

    private Map<String, String> createdInformation;
    private Map<String, LinkedList> updatedInformation;
    private Boolean deletedInformation;

    public Diff() {
    }

    public Map<String, String> getCreatedInformation() {
        return createdInformation;
    }

    public void setCreatedInformation(Map<String, String> createdInformation) {
        this.createdInformation = createdInformation;
    }

    public Map<String, LinkedList> getUpdatedInformation() {
        return updatedInformation;
    }

    public void setUpdatedInformation(Map<String, LinkedList> updatedInformation) {
        this.updatedInformation = updatedInformation;
    }

    public Boolean getDeletedInformation() {
        return deletedInformation;
    }

    public void setDeletedInformation(Boolean deletedInformation) {
        this.deletedInformation = deletedInformation;
    }

}
