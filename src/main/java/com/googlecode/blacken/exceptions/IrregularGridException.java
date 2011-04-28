package com.googlecode.blacken.exceptions;

/**
 * An operation was performed that is disallowed due to the regularity of 
 * the grid.
 * 
 * <p>This can either be an operation on an irregular grid which is disallowed
 * because it is an irregular grid, or an operation on a regular grid which is
 * only allowed on irregular grids.</p>
 * 
 * @author Steven Black
 *
 */
public class IrregularGridException extends IllegalArgumentException {

    /**
     * serialization UID
     */
    private static final long serialVersionUID = 2473129069282045073L;

    public IrregularGridException() {
    }

    public IrregularGridException(String arg0) {
        super(arg0);
    }

    public IrregularGridException(Throwable arg0) {
        super(arg0);
    }

    public IrregularGridException(String arg0, Throwable arg1) {
        super(arg0, arg1);
    }

}
