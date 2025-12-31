package work;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

/**
 * ----------------------------------------------------------------------*
 * ■■■SurveyDaoクラス■■■
 * 概要：DAO（「survey」テーブル）
 * ----------------------------------------------------------------------
 **/
public class SurveyDao {
	// -------------------------------------------
	// データベースへの接続情報
	// -------------------------------------------

	// JDBCドライバの相対パス
	// ※バージョンによって変わる可能性があります（MySQL5系の場合は「com.mysql.jdbc.Driver」）
	String DRIVER_NAME = "com.mysql.cj.jdbc.Driver";

	// 接続先のデータベース
	// ※データベース名が「test_db」でない場合は該当の箇所を変更してください
	// String JDBC_URL =
	// "jdbc:mysql://localhost/test_db?characterEncoding=UTF-8&serverTimezone=JST&useSSL=false";
	String JDBC_URL = "jdbc:mysql://localhost:3306/test_db?serverTimezone=Asia/Tokyo&allowPublicKeyRetrieval=true";

	// 接続するユーザー名
	// ※ユーザー名が「test_user」でない場合は該当の箇所を変更してください
	String USER_ID = "kawakaku";

	// 接続するユーザーのパスワード
	// ※パスワードが「test_pass」でない場合は該当の箇所を変更してください
	String USER_PASS = "31644452r";

	// ----------------------------------------------------------------
	// メソッド
	// ----------------------------------------------------------------

	/**
	 * ----------------------------------------------------------------------*
	 * ■doSelectメソッド
	 * 概要 ：「survey」テーブルの全件を取得する
	 * 引数 ：なし
	 * 戻り値：取得結果のリスト（例外時はnull）
	 * ----------------------------------------------------------------------
	 **/
	public List<SurveyDto> doSelect() {

		try {
			Class.forName(DRIVER_NAME); // JDBCドライバをロード＆接続先として指定
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}

		List<SurveyDto> list = new ArrayList<>();

		String sql = "SELECT NAME, AGE, SEX, SATISFACTION_LEVEL, MESSAGE, TIME FROM SURVEY ORDER BY TIME DESC";

		try (Connection con = DriverManager.getConnection(JDBC_URL, USER_ID, USER_PASS);
				PreparedStatement ps = con.prepareStatement(sql);
				ResultSet rs = ps.executeQuery()) {

			while (rs.next()) {
				SurveyDto dto = new SurveyDto();
				dto.setName(rs.getString("NAME"));
				dto.setAge(rs.getInt("AGE"));
				dto.setSex(rs.getInt("SEX"));
				dto.setSatisfactionLevel(rs.getInt("SATISFACTION_LEVEL"));
				dto.setMessage(rs.getString("MESSAGE"));
				dto.setTime(rs.getTimestamp("TIME"));
				list.add(dto);
			}
		} catch (SQLException e) {
			e.printStackTrace();
			return null; // 取得失敗時は呼び出し側で判定できるようにnullを返す
		}

		return list;
	}

	/**
	 * ----------------------------------------------------------------------*
	 * ■doInsertメソッド
	 * 概要 ：「survey」テーブルに対象のアンケートデータを挿入する
	 * 引数 ：対象のアンケートデータ（SurveyDto型）
	 * 戻り値：実行結果（真：成功、偽：例外発生）
	 * ----------------------------------------------------------------------
	 **/
	public boolean doInsert(SurveyDto dto) {

		// -------------------------------------------
		// JDBCドライバのロード
		// -------------------------------------------
		try {
			Class.forName(DRIVER_NAME); // JDBCドライバをロード＆接続先として指定
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}

		// -------------------------------------------
		// SQL発行
		// -------------------------------------------

		// 実行結果（真：成功、偽：例外発生）格納用変数
		boolean isSuccess = true;

		// 発行するSQL文の生成（INSERT）
		StringBuffer buf = new StringBuffer();
		buf.append("INSERT INTO SURVEY (  ");
		buf.append("  NAME,               ");
		buf.append("  AGE,                ");
		buf.append("  SEX,                ");
		buf.append("  SATISFACTION_LEVEL, ");
		buf.append("  MESSAGE,            ");
		buf.append("  TIME                ");
		buf.append(") VALUES (            ");
		buf.append("  ?,                  ");
		buf.append("  ?,                  ");
		buf.append("  ?,                  ");
		buf.append("  ?,                  ");
		buf.append("  ?,                  ");
		buf.append("  ?                   ");
		buf.append(")                     ");
		String sql = buf.toString();

		try (Connection con = DriverManager.getConnection(JDBC_URL, USER_ID, USER_PASS)) {
			con.setAutoCommit(false);
			try (PreparedStatement ps = con.prepareStatement(sql)) {

				// パラメータをセット
				ps.setString(1, dto.getName()); // 第1パラメータ：更新データ（名前）
				ps.setInt(2, dto.getAge()); // 第2パラメータ：更新データ（年齢）
				ps.setInt(3, dto.getSex()); // 第3パラメータ：更新データ（性別）
				ps.setInt(4, dto.getSatisfactionLevel()); // 第4パラメータ：更新データ（満足度）
				ps.setString(5, dto.getMessage()); // 第5パラメータ：更新データ（メッセージ）
				ps.setTimestamp(6, dto.getTime()); // 第6パラメータ：更新データ（更新時刻）

				// SQL文の実行
				ps.executeUpdate();

				// 明示的にコミットを実施
				con.commit();
			} catch (SQLException e) {
				con.rollback(); // 例外時はこの段階でロールバック
				throw e;
			}
		} catch (SQLException e) {
			e.printStackTrace();
			isSuccess = false;
		}

		// 実行結果を返す
		return isSuccess;
	}
}
