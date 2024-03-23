package com.example.demo.ineedlist.form;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class LoginData {
	
	@NotBlank
	private String loginId;
	
	@NotBlank
	private String password;
	

}
