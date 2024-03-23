package com.example.demo.ineedlist.service;

import java.util.Locale;
import java.util.Optional;

import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;

import com.example.demo.ineedlist.entity.Account;
import com.example.demo.ineedlist.form.LoginData;
import com.example.demo.ineedlist.repository.AccountRepository;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class LoginService {
	private final MessageSource messageSource;
	private final AccountRepository accountRepository;

	// Loginチェック
	public boolean isValid(LoginData loginData, BindingResult result, Locale locale) {
		Optional<Account> account = accountRepository.findByLoginId(loginData.getLoginId());
		if (account.isEmpty()) {
			// LoginIDがなし
			FieldError fieldError = new FieldError(result.getObjectName(), "password",
					messageSource.getMessage("NotFound.loginData.loginId", null, locale));
			result.addError(fieldError);
			return false;
		}

		// password check
		if (!account.get().getPassword().equals(loginData.getPassword())) {
			// PWが不一致
			FieldError fieldError = new FieldError(result.getObjectName(), "password",
					messageSource.getMessage("NotFound.loginData.password", null, locale));
			result.addError(fieldError);
			return false;
		}
		
		return true;
	}
}
