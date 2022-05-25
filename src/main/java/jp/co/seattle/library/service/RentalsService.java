package jp.co.seattle.library.service;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import jp.co.seattle.library.dto.RentalBooksInfo;
import jp.co.seattle.library.rowMapper.RentalBooksInfoRowMapper;

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
		String sql = "INSERT INTO rentals (book_id, rental_date) VALUES (" + bookId + ", now())";
		jdbcTemplate.update(sql);
	}
	
	/**
	 * 書籍の返却日を登録する
	 *
	 * @param bookId 書籍ID
	 */
	public void returnBook(int bookId) {

		// JSPに渡すデータを設定する
		String sql = "INSERT INTO rentals (book_id, return_date) VALUES (" + bookId + ", now())";
		jdbcTemplate.update(sql);
	}
	
	/**
	 * 書籍の貸出日を更新する
	 *
	 * @param bookId 書籍ID
	 */
	public void updRentalBook(int bookId) {

		// JSPに渡すデータを設定する
		String sql = "UPDATE rentals SET rental_date = now(), return_date = null WHERE book_id = " + bookId;
		jdbcTemplate.update(sql);
	}
	
	/**
	 * 書籍の返却日を更新する
	 *
	 * @param bookId 書籍ID
	 */
	public void updReturnBook(int bookId) {

		// JSPに渡すデータを設定する
		String sql = "UPDATE rentals SET return_date = now(), rental_date = null WHERE book_id = " + bookId;
		jdbcTemplate.update(sql);
	}

	/**
	 * 貸出書籍情報を取得する（書籍ID）
	 *
	 * @param bookId 書籍ID
	 * @return 書籍情報
	 */
	public int selectRentalBook(int bookId) {

		// JSPに渡すデータを設定する
		String sql = "SELECT book_id FROM rentals WHERE book_id =" + bookId;
		try {
			int selectRentalBook = jdbcTemplate.queryForObject(sql, int.class);
			return selectRentalBook;
		} catch (Exception e) {
			return 0;
		}
	}
	
	/**
	 * 貸出書籍情報を取得する（貸出日）
	 *
	 * @param bookId 書籍ID
	 * @return 貸出情報
	 */
	public String selectRentalBookDate(int bookId) {

		// JSPに渡すデータを設定する
		String sql = "SELECT rental_date FROM rentals WHERE book_id =" + bookId;
		try {
			String selectRentalBookDate = jdbcTemplate.queryForObject(sql, String.class);
			return selectRentalBookDate;
		} catch (Exception e) {
			return null;
		}
	}
	
	/**
	 * 貸出書籍情報を取得する
	 *
	 * @param bookId
	 * @return 貸出書籍リスト
	 */
	public List<RentalBooksInfo> rentalBookList() {

		List<RentalBooksInfo> rentalBookList = jdbcTemplate.query(
				"SELECT book_id, title, rental_date, return_date FROM books RIGHT OUTER JOIN rentals on books.id = rentals.book_id",
				new RentalBooksInfoRowMapper());

		return rentalBookList;
	}
	
	/**
	 * 削除した書籍をrentalsテーブルからも削除
	 * 
	 * @param bookId 書籍ID
	 */
	public void deleteBook(int bookId) {
		String sql = "DELETE FROM rentals where book_id =" + bookId;
		jdbcTemplate.update(sql);
	}
}