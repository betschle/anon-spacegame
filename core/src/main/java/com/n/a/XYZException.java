package com.n.a;

import java.util.HashMap;
import java.util.Map;

/**
 * XYZ Exceptions with Error Codes.
 * Following ranges are defined:
 * <ul>
 * <li>E0000 - E0999 - General Errors</li>
 * <li>E1000 - E1999 - Mostly IO Operations/IO Exceptions</li>
 * <li>E2000 - E2999 - Persistence, Serialization errors</li>
 * <li>E3000 - E3999 - ID errors for assets or gamedata</li>
 * </ul>
 */
public class XYZException extends RuntimeException {

    public enum ErrorCode {

        // General Errors
        /** Error not specified */
        E0000("Error not specified"),
        /** Something was not initialized */
        E0001("Initialization Error"),
        /** Class Configuration missing. This happens when the program expected an annotation, but it was not found. */
        E0002("Class Configuration missing"),
        /** Validation error. Values are not consistent */
        E0003("Validation error"),
        /** Argument validation Error */
        E0004("Illegal Argument(s)"),

        // File errors 1000 - 1999. Mostly IO Operations/IO Exceptions
        /** File not found */
        E1000("File not found"),
        /** Could not open file */
        E1010("Could not open file"),
        /** Could not open file due to file already in use */
        E1011("File already in use"),
        /** Could not open file due to no read access*/
        E1012("No read-access to file"),

        // Serialization errors 2000 - 2999. Regards contents of files or data
        /** Could not read Object */
        E2000("Could not read Object"),
        /** Could not write Object */
        E2001("Could not write Object"),
        /** Version mismatch */
        E2002("Version mismatch"),
        /** Instantiation error */
        E2003("Instantiation error"),
        /** Error with datamodel in file */
        E2010("Could not parse contents. Error in data structure"),

        // ID errors RE assets or gamedata
        /** Could not find ID in a Repository or similar */
        E3000("Could not find ID"),
        /** Could not find a registered entity */
        E3001("Registered Entity not found"),
        /** Could not find Datapack */
        E3100("Datapack not found"),
        /** Could not find ID a texture*/
        E3101("Texture not found"),
        /** Could not find data model */
        E3102("Data not found"),
        ;

        private String message = "-";

        private ErrorCode(String message) {
            this.message = message;
        }
    }

    private ErrorCode errorCode = ErrorCode.E0000;

    public XYZException() {
    }

    /**
     *
     * @param errorcode
     */
    public XYZException(ErrorCode errorcode) {
        super(getMessage(errorcode, (String) null));
        this.errorCode = errorcode;
    }

    /**
     *
     * @param errorcode
     * @param comment
     */
    public XYZException(ErrorCode errorcode, String comment) {
        super(getMessage(errorcode, comment));
        this.errorCode = errorcode;
    }

    /**
     *
     * @param errorcode
     * @param comment
     * @param cause
     */
    public XYZException(ErrorCode errorcode, String comment, Throwable cause) {
        super(getMessage(errorcode, comment), cause);
        this.errorCode = errorcode;
    }

    /**
     *
     * @param cause
     */
    public XYZException(Throwable cause) {
        super(getMessage(ErrorCode.E0000, null), cause);
    }

    /**
     *
     * @param errorcode
     * @param cause
     */
    public XYZException(ErrorCode errorcode, Throwable cause) {
        super(getMessage(errorcode, null), cause);
    }

    private static String getMessage(ErrorCode errorcode, String comment) {
        return "[" + errorcode.name() + "] " + errorcode.message + ( comment != null ? ": " + comment : "" );
    }

    public ErrorCode getErrorCode() {
        return errorCode;
    }
}
