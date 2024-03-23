package com.example.demo.ineedlist.service;

import java.time.DateTimeException;
import java.time.LocalDate;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;

import com.example.demo.ineedlist.common.Utils;
import com.example.demo.ineedlist.entity.Ineed;
import com.example.demo.ineedlist.form.IneedData;
import com.example.demo.ineedlist.form.IneedQuery;
import com.example.demo.ineedlist.repository.IneedRepository;
import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class IneedService {

	private final IneedRepository ineedRepository;

	public boolean isValid(IneedData ineedData, BindingResult result) {
		boolean ans = true;

		// 件名が全角スペースだけの場合、エラー
		String title = ineedData.getTitle();
		if (title != null && !title.equals("")) {
			boolean isAllDoubleSpace = true;
			for (int i = 0; i < title.length(); i++) {
				if (title.charAt(i) != '　') {
					isAllDoubleSpace = false;
					break;
				}
			}
			if (isAllDoubleSpace) {
				FieldError fieldError = new FieldError(result.getObjectName(), "title", "件名が全角スペースです");
				result.addError(fieldError);
				ans = false;
			}
		}

		// 期限が過去日付ならエラー
		String deadline = ineedData.getDeadline();
		if (!deadline.equals("")) {
			LocalDate tody = LocalDate.now();
			LocalDate deadlineDate = null;
			try {
				deadlineDate = LocalDate.parse(deadline);
				if (deadlineDate.isBefore(tody)) {
					FieldError fieldError = new FieldError(result.getObjectName(), "deadline", "期限を設定するときは今日以降にしてください");
					result.addError(fieldError);
					ans = false;
				}
			} catch (DateTimeException e) {
				FieldError fieldError = new FieldError(result.getObjectName(), "deadline",
						"期限を設定するときはyyyy-mm-dd形式で入力してください");
				result.addError(fieldError);
				ans = false;
			}
		}
		return ans;
	}

	public boolean isValid(IneedQuery ineedQuery, BindingResult result) {
		boolean ans = true;
		// 期限:開始の形式をチェック
		String date = ineedQuery.getDeadlineFrom();
		if (!date.equals("")) {
			try {
				LocalDate.parse(date);
			} catch (DateTimeException e) {
				// parseできない場合
				FieldError fieldError = new FieldError(result.getObjectName(), "deadlineFrom",
						"期限：開始を入力するときはyyyy-mm-dd形式で入力してください");
				result.addError(fieldError);
				ans = false;
			}
		}
		// 期限:終了の形式をチェック
		date = ineedQuery.getDeadlineTo();
		if (!date.equals("")) {
			try {
				LocalDate.parse(date);
			} catch (DateTimeException e) {
				// parseできない場合
				FieldError fieldError = new FieldError(result.getObjectName(), "deadlineTo",
						"期限：終了を入力するときはyyyy-mm-dd形式で入力してください");
				result.addError(fieldError);
				ans = false;
			}
		}
		return ans;
	}

	public List<Ineed> doQuery(IneedQuery ineedQuery) {
		List<Ineed> ineedList = null;
		if (ineedQuery.getTitle().length() > 0) {
			// タイトルで検索
			ineedList = ineedRepository.findByTitleLike("%" + ineedQuery.getTitle() + "%");
		} else if (ineedQuery.getImportance() != null && ineedQuery.getImportance() != -1) {
			// 重要度で検索
			ineedList = ineedRepository.findByImportance(ineedQuery.getImportance());
		} else if (ineedQuery.getUrgency() != null && ineedQuery.getUrgency() != -1) {
			// 緊急度で検索
			ineedList = ineedRepository.findByUrgency(ineedQuery.getUrgency());
		} else if (!ineedQuery.getDeadlineFrom().equals("") && ineedQuery.getDeadlineTo().equals("")) {
			// 期限 開始～
			ineedList = ineedRepository
					.findByDeadlineGreaterThanEqualOrderByDeadlineAsc(Utils.str2date(ineedQuery.getDeadlineFrom()));
		} else if (ineedQuery.getDeadlineFrom().equals("") && !ineedQuery.getDeadlineTo().equals("")) {
			// 期限 ～終了
			ineedList = ineedRepository
					.findByDeadlineLessThanEqualOrderByDeadlineAsc(Utils.str2date(ineedQuery.getDeadlineTo()));
		} else if (!ineedQuery.getDeadlineFrom().equals("") && !ineedQuery.getDeadlineTo().equals("")) {
			// 期限 開始～終了
			ineedList = ineedRepository.findByDeadlineBetweenOrderByDeadlineAsc(
					Utils.str2date(ineedQuery.getDeadlineFrom()), Utils.str2date(ineedQuery.getDeadlineTo()));
		} else if (ineedQuery.getDone() != null & ineedQuery.getDone().equals("Y")) {
			// 完了で検索
			ineedList = ineedRepository.findAll();
		}
		return ineedList;
	}
}
