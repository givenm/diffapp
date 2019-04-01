/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.perago.techtest.Impl;

import com.perago.techtest.Diff;
import com.perago.techtest.DiffException;
import com.perago.techtest.DiffRenderer;
import java.util.LinkedList;
import java.util.Map;

/**
 *
 * @author Given Nyauyanga
 */
public class DiffRendererImpl implements DiffRenderer {

    @Override
    public String render(Diff<?> diff) throws DiffException {
        if (diff == null) {
            throw new DiffException("Diff must must not be null");
        }
        StringBuilder stringBuilder = new StringBuilder();
        populateBuilder(0, 0, stringBuilder, diff);
        return stringBuilder.toString();
    }

    private void populateBuilder(int level, int operationPosition, StringBuilder stringBuilder, Diff<?> diff) {

        Map<String, String> createdInformation = diff.getCreatedInformation();
        Boolean deletedInformation = diff.getDeletedInformation();
        Map<String, LinkedList> updatedInformation = diff.getUpdatedInformation();

        if (createdInformation != null && !createdInformation.isEmpty()) {
            String prefix = "Create: ";
            String numberItem;

            stringBuilder.append(++level).append(".").append(" ").append(prefix).append("Class Name").append("\n");
            for (Map.Entry<String, String> entry : createdInformation.entrySet()) {
                Object value = entry.getValue();
                numberItem = level + "." + ++operationPosition;
                if (value != null) {
                    stringBuilder.append(numberItem).append(" ").append(prefix).append(entry.getKey()).append(" as ").append("\"").append(entry.getValue()).append("\"").append("\n");
                } else {
                    stringBuilder.append(numberItem).append(" ").append(prefix).append(entry.getKey()).append(" as ").append(entry.getValue()).append("\n");
                }

            }

        }

        if (deletedInformation != null && deletedInformation) {
            String prefix = "Delete: ";
            int major = 1;
            stringBuilder.append(major).append(".").append(" ").append(prefix).append("Class Name").append("\n");
        }

        if (updatedInformation != null && !updatedInformation.isEmpty()) {

        }

        //recurse down the children
        if (diff.getSubDiff() != null) {
            populateBuilder(++level, ++operationPosition, stringBuilder, diff.getSubDiff());
        }

    }

}
