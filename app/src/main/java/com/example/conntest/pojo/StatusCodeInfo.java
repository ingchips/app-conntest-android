package com.example.conntest.pojo;

public class StatusCodeInfo {
    private int statusCode;
    private int count;

    public int getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(int statusCode) {
        this.statusCode = statusCode;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    @Override
    public String toString() {
        return "{statusCode:" + statusCode + ", count;" + count + "}";
    }
}
