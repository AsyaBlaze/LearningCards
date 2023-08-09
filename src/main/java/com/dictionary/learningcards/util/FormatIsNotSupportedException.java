package com.dictionary.learningcards.util;

public class FormatIsNotSupportedException extends RuntimeException {
    public FormatIsNotSupportedException(String file) {
        super(file + " does not have a supported format. Try to load file .xls or .xlsx");
    }
}
