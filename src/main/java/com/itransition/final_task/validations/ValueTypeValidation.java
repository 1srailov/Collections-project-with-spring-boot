package com.itransition.final_task.validations;

import java.util.Optional;

public class ValueTypeValidation {

    public static Integer StringToInteger(String value) {
        try {
            return Integer.parseInt(value);
        } catch (NumberFormatException e) {
            return null;
        }
    }

    public static Double StringToDouble(String value){
        try {
            return Double.parseDouble(value);
        } catch (NumberFormatException e) {
            return null;
        }
    }

    public static Boolean StringToBoolean(String value) {
        try {
            return Boolean.parseBoolean(value);
        } catch (NumberFormatException e) {
            return null;
        }
    }
}
