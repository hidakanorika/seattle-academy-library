package jp.co.seattle.library.controller;

import java.util.Locale;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import jp.co.seattle.library.service.BooksService;

/**
 * 検索コントローラー
 */
@Controller // APIの入り口
public class SearchController {
	final static Logger logger = LoggerFactory.getLogger(SearchController.class);

	@Autowired
	private BooksService booksService;

	/**
	 * 対象書籍を検索する
	 *
	 * @param locale ロケール情報
	 * @param bookId 書籍ID
	 * @param model  モデル情報
	 * @return 遷移先画面名
	 */

	@Transactional
	@RequestMapping(value = "/searchBook", method = RequestMethod.POST)
	public String searchBook(Locale locale, @RequestParam("keyword") String keyword, Model model) {
		logger.info("Welcome delete! The client locale is {}.", locale);

		if (booksService.searchBook(keyword).isEmpty()) {
			model.addAttribute("searchMessage", "検索結果がありません。");
		} else {
		model.addAttribute("bookList", booksService.searchBook(keyword));
		}
		return "home";
	}
}
