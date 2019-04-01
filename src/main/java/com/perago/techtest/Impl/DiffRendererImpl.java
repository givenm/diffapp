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

    public String render(Diff<?> diff) throws DiffException {
        if (diff == null) {
            throw new DiffException("Diff must must not be null");
        }
        Map<String, String> createdInformation = diff.getCreatedInformation();
        Boolean deletedInformation = diff.getDeletedInformation();
        Map<String, LinkedList> updatedInformation = diff.getUpdatedInformation();
        StringBuilder stringBuilder = new StringBuilder();
        if (createdInformation != null && !createdInformation.isEmpty()) {
            String prefix = "Create: ";
            int major = 1;
            int minor = 0;
            stringBuilder.append(major).append(".").append(" ").append(prefix).append("Class Name").append("\n");
            for (Map.Entry<String, String> entry : createdInformation.entrySet()) {
                String value = entry.getValue();
                if (value != null) {
                    stringBuilder.append(major).append(".").append(++minor).append(" ").append(prefix).append(entry.getKey()).append(" as ").append("\"").append(entry.getValue()).append("\"").append("\n");
                } else {
                    stringBuilder.append(major).append(".").append(++minor).append(" ").append(prefix).append(entry.getKey()).append(" as ").append(entry.getValue()).append("\n");
                }
            }

            return stringBuilder.toString();
        }

        if (deletedInformation != null && deletedInformation) {
            String prefix = "Delete: ";
            int major = 1;
            stringBuilder.append(major).append(".").append(" ").append(prefix).append("Class Name").append("\n");
            return stringBuilder.toString();
        }

        if (updatedInformation != null && !updatedInformation.isEmpty()) {

        }

        return stringBuilder.toString();
    }

}
