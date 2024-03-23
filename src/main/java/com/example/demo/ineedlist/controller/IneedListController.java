package com.example.demo.ineedlist.controller;

import java.util.List;

import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.ModelAndView;
import com.example.demo.ineedlist.entity.Ineed;
import com.example.demo.ineedlist.form.IneedData;
import com.example.demo.ineedlist.form.IneedQuery;
import com.example.demo.ineedlist.repository.IneedRepository;
import com.example.demo.ineedlist.service.IneedService;

import jakarta.servlet.http.HttpSession;
import lombok.AllArgsConstructor;

@Controller
@AllArgsConstructor
public class IneedListController {
	private final HttpSession session;
	private final IneedRepository ineedRepository;
	private final IneedService ineedService;

	// 一覧表示
	@GetMapping("/ineed")
	public ModelAndView showIneedList(ModelAndView mv) {
		mv.setViewName("ineedList");
		List<Ineed> ineedList = ineedRepository.findAll(Sort.by(Sort.Direction.ASC, "id"));
		mv.addObject("ineedList", ineedList);
		mv.addObject("ineedQuery", new IneedQuery());
		return mv;
	}

	// 項目追加
	@GetMapping("/ineed/create")
	public ModelAndView createIneed(ModelAndView mv) {
		mv.setViewName("ineedForm");
		mv.addObject("ineedData", new IneedData());
		session.setAttribute("mode", "create");
		return mv;
	}

	// 項目チェック
	@PostMapping("/ineed/create")
	public String createIneed(@ModelAttribute @Validated IneedData ineedData, BindingResult result, Model model) {
		boolean isValid = ineedService.isValid(ineedData, result);
		if (!result.hasErrors() && isValid) {
			// errorなし
			Ineed ineed = ineedData.toEntity();
			ineedRepository.saveAndFlush(ineed);
			return "redirect:/ineed";
		} else {
			// errorあり
			// mv.setViewName("ineedForm");
			// mv.addObject("ineedData", ineedData);
			return "todoForm";
		}
	}

	// 項目編集
	@GetMapping("/ineed/{id}")
	public ModelAndView ineedById(@PathVariable(name = "id") int id, ModelAndView mv) {
		mv.setViewName("ineedForm");
		Ineed ineed = ineedRepository.findById(id).get();
		mv.addObject("ineedData", ineed);
		session.setAttribute("mode", "update");
		return mv;
	}

	// 項目編集チェック
	@PostMapping("/ineed/update")
	public String updateIneed(@ModelAttribute @Validated IneedData ineedData, BindingResult result, Model model) {
		// エラーチェック
		boolean isValid = ineedService.isValid(ineedData, result);
		if (!result.hasErrors() && isValid) {
			// エラーなし
			Ineed ineed = ineedData.toEntity();
			ineedRepository.saveAndFlush(ineed);
			return "redirect:/ineed";
		} else {
			// エラーあり
			model.addAttribute("ineedData", ineedData);
			return "ineedForm";
		}
	}

	// 削除処理
	@PostMapping("/ineed/delete")
	public String deleteIneed(@ModelAttribute IneedData ineedData) {
		ineedRepository.deleteById(ineedData.getId());
		return "redirect:/ineed";
	}

	// クエリ処理
	@PostMapping("/ineed/query")
	public ModelAndView queryIneed(@ModelAttribute IneedQuery ineedQuery, BindingResult result, ModelAndView mv) {
		mv.setViewName("ineedList");
		List<Ineed> ineedList = null;
		if (ineedService.isValid(ineedQuery, result)) {
			// エラーが無ければ検索
			ineedList = ineedService.doQuery(ineedQuery);
		}
		// mv.addObject("ineedQuery", ineedQuery);
		mv.addObject("ineedList", ineedList);
		return mv;
	}

	// キャンセル処理
	@PostMapping("/ineed/cancel")
	public String cancel() {
		return "redirect:/ineed";
	}

}
