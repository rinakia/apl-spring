package com.example.demo.ineedlist.common;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class OpMsg {
	private String msgType;
		// "I":info "W":warn "E":error
	private String msgText;
}
