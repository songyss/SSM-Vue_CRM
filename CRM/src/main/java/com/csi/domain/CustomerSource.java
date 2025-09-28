package com.csi.domain;

// CustomerSource.java
public class CustomerSource {
    private String name;
    private int value;
    
    // 构造函数
    public CustomerSource() {}
    
    public CustomerSource(String name, int value) {
        this.name = name;
        this.value = value;
    }
    
    // Getter和Setter方法
    public String getName() {
        return name;
    }
    
    public void setName(String name) {
        this.name = name;
    }
    
    public int getValue() {
        return value;
    }
    
    public void setValue(int value) {
        this.value = value;
    }
}
