package com.genelle.alexandre.bean;

import java.util.ArrayList;
import java.util.List;

public class SessionBean {
	
	public SessionBean() {
		list = new ArrayList<String>();
	}
	
	private String str;
	private List<String> list;
	
	public String getStr() {
		return str;
	}
	public void setStr(String str) {
		this.str = str;
	}
	public List<String> getList() {
		return list;
	}
	public void setList(List<String> list) {
		this.list = list;
	}

}
