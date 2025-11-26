package com.demo.home;

import lombok.*;


@Getter
public class User {
	
	private String name;
    private int age;
    
    public User() {
    	
    	this.name = "홍길동";
    	this.age = 30;
    }

//	public String getName() {
//		// TODO Auto-generated method stub
//		return name;
//	}

}
    