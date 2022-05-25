package jp.co.seattle.library.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import jp.co.seattle.library.service.BooksService;
import jp.co.seattle.library.service.RentalsService;

/**
 * 貸出履歴コントローラー
 */
@Controller
public class HistoryController {
	final static Logger logger = LoggerFactory.getLogger(BooksService.class);
	
	@Autowired
	private RentalsService rentalsService;

	@RequestMapping(value = "/historyBook", method = RequestMethod.POST)
	public String historyBook(Model model) {
		model.addAttribute("rentalBookList",rentalsService.rentalBookList());
		return "history";
	}
}