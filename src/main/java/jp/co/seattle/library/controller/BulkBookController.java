package jp.co.seattle.library.controller;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import jp.co.seattle.library.dto.BookDetailsInfo;
import jp.co.seattle.library.service.BooksService;

/**
 * 一括登録コントローラー
 */
@Controller // APIの入り口
public class BulkBookController {
	final static Logger logger = LoggerFactory.getLogger(BulkBookController.class);

	@Autowired
	private BooksService booksService;

	@RequestMapping(value = "/bulkBook", method = RequestMethod.GET) // value＝actionで指定したパラメータ
	// RequestParamでname属性を取得
	public String login(Model model) {
		return "bulkBook";
	}

	@RequestMapping(value = "/bulkRegist", method = RequestMethod.POST)
	public String bulkBook(Locale locale, @RequestParam("upload_file") MultipartFile uploadFile, Model model) {

		try (BufferedReader br = new BufferedReader(
				new InputStreamReader(uploadFile.getInputStream(), StandardCharsets.UTF_8))) {

			String line = null;
			List<BookDetailsInfo> bookList = new ArrayList<BookDetailsInfo>();
			List<String> errorLine = new ArrayList<String>();
			int count = 0;

			while ((line = br.readLine()) != null) {
				final String data[] = line.split(",", -1);
				count++;

				BookDetailsInfo bookInfo = new BookDetailsInfo();
				bookInfo.setTitle(data[0]);
				bookInfo.setAuthor(data[1]);
				bookInfo.setPublisher(data[2]);
				bookInfo.setPublishDate(data[3]);
				bookInfo.setIsbn(data[4]);
				bookInfo.setExplanatoryText(data[5]);

				boolean checkRequired = data[0].isEmpty() || data[1].isEmpty() || data[2].isEmpty()
						|| data[3].isEmpty();
				boolean checkDate = !data[3].matches("^[0-9]{8}+$");
				boolean checkIsbn = !data[4].isEmpty() && !data[4].matches("^[0-9]{10}|[0-9]{13}$");

				if (checkRequired || checkDate || checkIsbn) {
					errorLine.add(count + "行目の書籍登録でエラーが起きました。");
				}
				bookList.add(bookInfo);
			}

			if (bookList.size() == 0) {
				model.addAttribute("errorBulk", "csvに書籍情報がありません。");
				return "bulkBook";
			}

			if (errorLine.size() > 0) {
				model.addAttribute("errorBulk", errorLine);
				return "bulkBook";
			}

			for (int i = 0; i < bookList.size(); i++) {
				BookDetailsInfo bookInfo = bookList.get(i);

				booksService.registBook(bookInfo);
			}

			model.addAttribute("bookList", booksService.getBookList());
			return "home";

		} catch (IOException e) {
			throw new RuntimeException("ファイルが読み込めません", e);
		}
	}
}