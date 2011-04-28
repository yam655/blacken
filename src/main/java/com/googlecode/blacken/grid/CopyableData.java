package com.googlecode.blacken.grid;

/**
 * This is a Copyable-wrapper around <i>simple</i> data items.
 * 
 * <p>The target for the items wrapped are Java simple types. These are
 * explicitly copy-as-value types and not copy-as-reference types. (This
 * is because Integer and friends don't support {@link #clone()}.)</p>
 * 
 * @author Steven Black
 * @param <T> type of data
 */
public class CopyableData <T> extends Copyable {
    @SuppressWarnings("unchecked")
    public static <U extends CopyableData<?>> U copy(U source) {
        return (U) source.clone();
    }
    public T data;
    
    public CopyableData(T data) {
        this.data = data;
    }
    public CopyableData(CopyableData<T> other) {
        this.data = other.data;
    }
    
    @SuppressWarnings("unchecked")
    @Override
    public CopyableData<T> clone() {
        return (CopyableData<T>)super.clone();
    }

    public CopyableData<T> copy() {
        return (CopyableData<T>) clone();
    }

    public boolean equals(CopyableData<T> other) {
        if (data == other.data) {
            return true;
        }
        return data.equals(other.data);
    }
    
    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }
    
    @Override
    public String toString() {
        StringBuffer buf = new StringBuffer();
        buf.append(CopyableData.class.getName());
        buf.append(":");
        if (data == null) {
            buf.append("null");
        } else {
            buf.append(data.toString());
        }
        return buf.toString();
    }
}
