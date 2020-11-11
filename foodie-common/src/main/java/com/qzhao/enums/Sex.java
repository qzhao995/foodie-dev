package com.qzhao.enums;

public enum Sex {
    woman(0,""),
    man(1,""),
    secret(2,"");


    public Integer type;
    public String value;

    Sex(Integer type,String value){
        this.type=type;
        this.value=value;
    }



}
