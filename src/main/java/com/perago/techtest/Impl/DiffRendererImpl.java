/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.perago.techtest.Impl;

import com.perago.techtest.ChangedInfo;
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

        Map<String, Object> createdInformation = diff.getCreatedInformation();
        Boolean deletedInformation = diff.getDeletedInformation();
        Map<String, Object> updatedInformation = diff.getUpdatedInformation();

        if (createdInformation != null && !createdInformation.isEmpty()) {
            String prefix = "Create: ";
            String numberItem;

            stringBuilder.append(++level).append(".").append(" ").append(prefix).append("Class Name").append("\n");
            for (Map.Entry<String, Object> entry : createdInformation.entrySet()) {
                Object value = entry.getValue();
                numberItem = level + "." + ++operationPosition;
                //recurse down the children
                if (value instanceof Diff) {
                    //we will retain position of rendering the diff
                    populateBuilder(++level, ++operationPosition, stringBuilder, (Diff<?>) value);
                } else {
                    if (value != null) {
                        stringBuilder
                                .append(numberItem).append(" ").append(prefix).append(entry.getKey())
                                .append(" as ").append("\"").append(entry.getValue()).append("\"")
                                .append("\n");
                    } else {
                        stringBuilder
                                .append(numberItem).append(" ").append(prefix).append(entry.getKey())
                                .append(" as ").append(entry.getValue()).append("\n");
                    }
                }

            }

        }

        if (deletedInformation != null && deletedInformation) {
            String prefix = "Delete: ";
            int major = 1;
            stringBuilder.append(major).append(".").append(" ").append(prefix).append("Class Name").append("\n");
        }

        if (updatedInformation != null && !updatedInformation.isEmpty()) {
            String prefix = "Updated: ";
            String numberItem;

            stringBuilder.append(++level).append(".").append(" ").append(prefix).append("Class Name").append("\n");
            for (Map.Entry<String, Object> entry : updatedInformation.entrySet()) {
                Object value = entry.getValue();
                numberItem = level + "." + ++operationPosition;
                //recurse down the children
                if (value instanceof Diff) {
                    populateBuilder(++level, ++operationPosition, stringBuilder, (Diff<?>) value);
                } else if (value instanceof ChangedInfo) {
                    ChangedInfo changedInfo = (ChangedInfo) value;
                    if (changedInfo.getTo() != null) {
                        stringBuilder
                                .append(numberItem).append(" ").append(prefix).append(entry.getKey())
                                .append(" from ").append("\"").append(changedInfo.getFrom()).append("\"")
                                .append(" to ").append("\"").append(changedInfo.getTo()).append("\"")
                                .append("\n");
                    } else {
                        stringBuilder
                                .append(numberItem).append(" ").append(prefix).append(entry.getKey())
                                .append(" from ").append("\"").append(changedInfo.getFrom()).append("\"")
                                .append(" to ").append(changedInfo.getTo())
                                .append("\n");
                    }
                }

            }
        }

    }

}
