package jp.co.seattle.library.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

/**
 * 貸出サービス
 * 
 * rentalsテーブルに関する処理を実装する
 */
@Service
public class RentalsService {
	final static Logger logger = LoggerFactory.getLogger(RentalsService.class);
	@Autowired
	private JdbcTemplate jdbcTemplate;

	/**
	 * 書籍を借りて登録する
	 *
	 * @param bookId 書籍ID
	 */
	public void rentalBook(int bookId) {

		// JSPに渡すデータを設定する
		String sql = "INSERT INTO rentals (book_id) VALUES (" + bookId + ")";
		jdbcTemplate.update(sql);
	}

	/**
	 * 貸出書籍情報を取得する
	 *
	 * @param bookId 書籍ID
	 */
	public int selectRentalBook(int bookId) {

		// JSPに渡すデータを設定する
		String sql = "SELECT book_id FROM rentals where book_id =" + bookId;
		try {
			int selectRentalBook = jdbcTemplate.queryForObject(sql, int.class);
			return selectRentalBook;
		} catch (Exception e) {
			return 0;
		}
	}

}