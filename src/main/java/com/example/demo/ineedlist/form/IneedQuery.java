package com.example.demo.ineedlist.form;

import lombok.Data;

@Data
public class IneedQuery {
	private String title;
	private Integer importance;
	private Integer urgency;
	private String deadlineFrom;
	private String deadlineTo;
	private String done;
	
	public IneedQuery() { 
	title = "";
	importance = -1;
	urgency = -1;
	deadlineFrom = "";
	deadlineTo = "";
	done = "";
	} 

}
