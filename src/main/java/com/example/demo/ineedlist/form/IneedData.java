package com.example.demo.ineedlist.form;

import java.sql.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import com.example.demo.ineedlist.entity.Ineed;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class IneedData {
	private Integer id;

	@NotBlank(message = "件名を入力してください")
	private String title;

	@NotNull(message = "重要度を選択してください")
	private Integer importance;

	@Min(value = 0, message = "緊急度を選択してください")
	private Integer urgency;

	private String deadline;
	private String done;

	public Ineed toEntity() {
		Ineed ineed = new Ineed();
		ineed.setId(id);
		ineed.setTitle(title);
		ineed.setImportance(importance);
		ineed.setUrgency(urgency);
		ineed.setDone(done);
		SimpleDateFormat sdFormat = new SimpleDateFormat("yyyy-MM-dd");
		long ms;
		try {
			ms = sdFormat.parse(deadline).getTime();
			ineed.setDeadline(new Date(ms));
		} catch (ParseException e) {
			ineed.setDeadline(null);
		}
		return ineed;
	}
}
