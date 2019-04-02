/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.perago.techtest.Impl;

import com.perago.techtest.ChangedInfo;
import com.perago.techtest.Counter;
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
        populateBuilder(new Counter(1, 0), stringBuilder, diff, null);
        return stringBuilder.toString();
    }

    private void populateBuilder(Counter counter, StringBuilder stringBuilder, Diff<?> diff, Operations lastOperation) {

        Map<String, Object> createdInformation = diff.getCreatedInformation();
        String deletedInformation = diff.getDeletedInformation();
        Map<String, Object> updatedInformation = diff.getUpdatedInformation();

        if (createdInformation != null && !createdInformation.isEmpty()) {
            String prefix = Operations.CREATE.getOperationname();
            counter.setCurrentOperation(Operations.CREATE);
            if (lastOperation != null && lastOperation != Operations.CREATE) {
                Counter newSubCounter = new Counter(counter.getRoot(), 0);
                counter.setSubCounter(newSubCounter);
                stringBuilder
                        .append(getTrackedNumber(counter, true)).append(" ").append(prefix).append(diff.getClassName()).append("\n");
            } else {
                stringBuilder.append(getTrackedNumber(counter, true)).append(" ").append(prefix).append(diff.getClassName()).append("\n");
            }

            createdInformation.entrySet().forEach((entry) -> {
                Object value = entry.getValue();
                //recurse down the children
                if (value instanceof Diff) {
                    //we will retain position of rendering the diff
                    populateBuilder(counter, stringBuilder, (Diff<?>) value, Operations.CREATE);
                } else {
                    if (value != null) {
                        stringBuilder
                                .append(getTrackedNumber(counter, false)).append(" ").append(prefix).append(entry.getKey())
                                .append(" as ").append("\"").append(entry.getValue()).append("\"")
                                .append("\n");
                    } else {
                        stringBuilder
                                .append(getTrackedNumber(counter, false)).append(" ").append(prefix).append(entry.getKey())
                                .append(" as ").append(entry.getValue()).append("\n");
                    }
                }
            });

        }

        if (deletedInformation != null && !deletedInformation.isEmpty()) {
            String prefix = Operations.DELETE.getOperationname();
            counter.setCurrentOperation(Operations.DELETE);
            if (lastOperation != null && lastOperation != Operations.DELETE) {
                Counter newSubCounter = new Counter(counter.getRoot(), 0);
                counter.setSubCounter(newSubCounter);
                stringBuilder
                        .append(getTrackedNumber(counter, true)).append(" ").append(prefix).append(diff.getClassName()).append("\n");
            } else {
                stringBuilder
                        .append(getTrackedNumber(counter, true)).append(" ").append(prefix).append(diff.getClassName()).append("\n");
            }
        }

        if (updatedInformation != null && !updatedInformation.isEmpty()) {
            String prefix = Operations.UPDATE.getOperationname();
            counter.setCurrentOperation(Operations.UPDATE);
            if (lastOperation != null && lastOperation != Operations.UPDATE) {
                Counter newSubCounter = new Counter(counter.getRoot(), 0);
                counter.setSubCounter(newSubCounter);
                stringBuilder
                        .append(getTrackedNumber(counter, true)).append(" ").append(prefix).append(diff.getClassName()).append("\n");
            } else {
                stringBuilder
                        .append(getTrackedNumber(counter, true)).append(" ").append(prefix).append(diff.getClassName()).append("\n");
            }
            updatedInformation.entrySet().forEach((entry) -> {
                Object value = entry.getValue();
                //recurse down the children
                if (value instanceof Diff) {
                    stringBuilder
                            .append(getTrackedNumber(counter, false)).append(" ").append(prefix).append(entry.getKey())
                            .append("\n");
                    populateBuilder(counter, stringBuilder, (Diff<?>) value, Operations.UPDATE);
                } else if (value instanceof ChangedInfo) {
                    ChangedInfo changedInfo = (ChangedInfo) value;
                    if (changedInfo.getTo() != null) {
                        stringBuilder
                                .append(getTrackedNumber(counter, false)).append(" ").append(prefix).append(entry.getKey())
                                .append(" from ").append("\"").append(changedInfo.getFrom()).append("\"")
                                .append(" to ").append("\"").append(changedInfo.getTo()).append("\"")
                                .append("\n");
                    } else {
                        stringBuilder
                                .append(getTrackedNumber(counter, false)).append(" ").append(prefix).append(entry.getKey())
                                .append(" from ").append("\"").append(changedInfo.getFrom()).append("\"")
                                .append(" to ").append(changedInfo.getTo())
                                .append("\n");
                    }
                }
            });
        }

    }

    private String getTrackedNumber(Counter counter, boolean isClassDisplay) {
        StringBuilder numberBuilder = new StringBuilder();

        if (!isClassDisplay) {

            if (counter.getSubCounter() == null) {
                counter.setPosition(counter.getPosition() + 1);
                numberBuilder
                        .append(counter.getRoot())
                        .append(".")
                        .append(counter.getPosition());
            } else {

                counter.getSubCounter().setPosition(counter.getSubCounter().getPosition() + 1);
                numberBuilder
                        .append(counter.getSubCounter().getRoot())
                        .append(".")
                        .append(getTrackedNumber(counter.getSubCounter(), false))
                        .append(".")
                        .append(counter.getSubCounter().getPosition());
            }

        } else {
            if (counter.getSubCounter() != null && counter.getCurrentOperation() != counter.getSubCounter().getCurrentOperation()) {
                numberBuilder
                        .append(counter.getRoot())
                        .append(".")
                        .append(getTrackedNumber(counter.getSubCounter(), false));
            } else {
                numberBuilder
                        .append(counter.getRoot());
            }

        }

        return numberBuilder.toString();
    }

}
