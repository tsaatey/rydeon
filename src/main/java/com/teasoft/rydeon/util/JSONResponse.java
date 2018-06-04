package com.teasoft.rydeon.util;


/**
 *
 * @author Maxxi
 */
public class JSONResponse {
    
    private boolean status;
    private long count;
    private Object result;
    private String message;
    
    public JSONResponse() {}
    
    public JSONResponse(boolean status, long count, Object result, String message) {
        this.status = status;
        this.count = count;
        this.result = result;
        this.message = message;
    }

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public long getCount() {
        return count;
    }

    public void setCount(long count) {
        this.count = count;
    }

    public Object getResult() {
        return result;
    }

    public void setResult(Object result) {
        this.result = result;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

}
