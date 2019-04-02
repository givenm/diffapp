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
import com.perago.techtest.Operations;
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
        populateBuilder(0, 0, stringBuilder, diff, null);
        return stringBuilder.toString();
    }

    private void populateBuilder(int level, int operationPosition, StringBuilder stringBuilder, Diff<?> diff, Operations lastOperation) {

        Map<String, Object> createdInformation = diff.getCreatedInformation();
        Boolean deletedInformation = diff.getDeletedInformation();
        Map<String, Object> updatedInformation = diff.getUpdatedInformation();

        if (createdInformation != null && !createdInformation.isEmpty()) {
            String prefix = Operations.CREATE.getOperationname();
            String numberItem;
            if (lastOperation != Operations.CREATE || lastOperation == null) {
                stringBuilder.append(++level).append(".").append(" ").append(prefix).append(diff.getClassName()).append("\n");
            }
            for (Map.Entry<String, Object> entry : createdInformation.entrySet()) {
                Object value = entry.getValue();
                numberItem = level + "." + ++operationPosition;
                //recurse down the children
                if (value instanceof Diff) {
                    //we will retain position of rendering the diff
                    populateBuilder(++level, ++operationPosition, stringBuilder, (Diff<?>) value, Operations.CREATE);
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
            String prefix = Operations.DELETE.getOperationname();
            int major = 1;
            if (lastOperation != Operations.DELETE || lastOperation == null) {
                stringBuilder.append(major).append(".").append(" ").append(prefix).append(diff.getClassName()).append("\n");
            }
        }

        if (updatedInformation != null && !updatedInformation.isEmpty()) {
            String prefix = Operations.UPDATE.getOperationname();
            String numberItem;
            if (lastOperation != Operations.UPDATE || lastOperation == null) {
                stringBuilder.append(++level).append(".").append(" ").append(prefix).append(diff.getClassName()).append("\n");
            }
            for (Map.Entry<String, Object> entry : updatedInformation.entrySet()) {
                Object value = entry.getValue();
                numberItem = level + "." + ++operationPosition;
                //recurse down the children
                if (value instanceof Diff) {
                    stringBuilder
                            .append(numberItem).append(" ").append(prefix).append(entry.getKey())
                            .append("\n");
                    populateBuilder(++level, ++operationPosition, stringBuilder, (Diff<?>) value, Operations.UPDATE);
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
