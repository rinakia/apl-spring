package com.example.demo.ineedlist.controller;

import java.util.Locale;

import org.springframework.context.MessageSource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.demo.ineedlist.common.OpMsg;
import com.example.demo.ineedlist.entity.Account;
import com.example.demo.ineedlist.form.LoginData;
import com.example.demo.ineedlist.repository.AccountRepository;
import com.example.demo.ineedlist.service.LoginService;

import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class LoginController {
	private final AccountRepository accountRepository;
	private final LoginService loginService;
	private final MessageSource messageSource;
	private final HttpSession session;

	// ログイン画面表示
	@GetMapping("/")
	public ModelAndView showLogin(ModelAndView mv) {
		mv.setViewName("loginForm");
		mv.addObject("loginData", new LoginData());
		return mv;
	}

	// ログイン画面表示
	@GetMapping("/login")
	public ModelAndView login(ModelAndView mv) {
		mv.setViewName("loginForm");
		mv.addObject("loginData", new LoginData());
		return mv;
	}

	// ログイン実行結果確認
	@PostMapping("/login/do")
	public String login(@ModelAttribute @Validated LoginData loginData, BindingResult result, Model model,
			RedirectAttributes redirectAttributes, Locale locale) {
		if (result.hasErrors()) {
			String msg = messageSource.getMessage("msg.e.input_something_wrong", null, locale);
			model.addAttribute("msg", new OpMsg("E", msg));
			return "loginForm";
		}
		// サービスでチェック
		if (!loginService.isValid(loginData, result, locale)) {
			// エラーあり -> エラーメッセージをセット
			String msg = messageSource.getMessage("msg.e.input_something_wrong", null, locale);
			model.addAttribute("msg", new OpMsg("E", msg));
			return "loginForm";
		}

		// セッション情報クリア
		session.invalidate();
		// LoginしたユーザーのaccountIdをセッションへ格納する
		Account account = accountRepository.findByLoginId(loginData.getLoginId()).get();
		session.setAttribute("accountId", account.getId());
		// Login成功メッセージをセットしてリダイレクト
		String msg = messageSource.getMessage("msg.i.login_successful",
				new Object[] { account.getLoginId(), account.getName() }, locale);
		redirectAttributes.addFlashAttribute("msg", new OpMsg("I", msg));
		return "redirect:/ineed";
	}

	// ログアウト
	@GetMapping("/logout")
	public String logout(RedirectAttributes redirectAttributes, Locale locale) {
		// セッション情報クリア
		session.invalidate();
		// ログアウト完了メッセージをセットしてリダイレクト
		String msg = messageSource.getMessage("msg.i.logout_successful", null, locale);
		redirectAttributes.addFlashAttribute("msg", new OpMsg("I", msg));
		return "redirect:/";
	}

}
