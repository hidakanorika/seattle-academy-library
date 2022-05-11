package jp.co.seattle.library.controller;

import java.util.Locale;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import jp.co.seattle.library.service.BooksService;
import jp.co.seattle.library.service.RentalsService;

/**
 * 貸出コントローラー
 */
@Controller
public class RentalBookController {
	final static Logger logger = LoggerFactory.getLogger(BooksService.class);

	@Autowired
	private BooksService booksService;

	@Autowired
	private RentalsService rentalsService;

	@RequestMapping(value = "/rentBook", method = RequestMethod.POST)
	public String rentalsBook(Locale locale, @RequestParam("bookId") int bookId, Model model) {

		if (rentalsService.selectRentalBook(bookId) == 0) {
			rentalsService.rentalBook(bookId);

		} else {
			model.addAttribute("error", "貸出し済みです。");
		}
		model.addAttribute("bookDetailsInfo", booksService.getBookInfo(bookId));
		return "details";
	}
}