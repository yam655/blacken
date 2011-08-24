package com.googlecode.blacken.exceptions;

/**
 * An operation was performed that is disallowed due to the regularity of 
 * the grid.
 * 
 * <p>This can either be an operation on an irregular grid which is disallowed
 * because it is an irregular grid, or an operation on a regular grid which is
 * only allowed on irregular grids.</p>
 * 
 * @author yam655
 */
public class IrregularGridException extends IllegalArgumentException {
    private static final long serialVersionUID = 2473129069282045073L;

    /**
     * Grid regularity exception.
     */
    public IrregularGridException() {
        // do nothing
    }

    /**
     * Grid regularity exception.
     * @param arg0 descriptive message
     */
    public IrregularGridException(String arg0) {
        super(arg0);
    }

    /**
     * Grid regularity exception.
     * @param arg0 cause
     */
    public IrregularGridException(Throwable arg0) {
        super(arg0);
    }

    /**
     * Grid regularity exception.
     * @param arg0 descriptive message
     * @param arg1 cause
     */
    public IrregularGridException(String arg0, Throwable arg1) {
        super(arg0, arg1);
    }

}
