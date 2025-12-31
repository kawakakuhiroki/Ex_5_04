package work;

import java.io.IOException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

/**----------------------------------------------------------------------*
 * ■■■SaveSurveyクラス■■■
 *
 * 【役割】
 *  ・アンケート入力画面から送信されたリクエストを受け取るサーブレット
 *  ・入力値を取得し、サーバーサイドでバリデーションチェックを行う
 *  ・正しい入力値を SurveyDto に格納する
 *  ・ビジネスロジック（SaveSurveyBL）を呼び出して DB 登録を実行する
 *  ・処理結果に応じて、完了画面 or エラー画面へ遷移させる
 *
 * 【ポイント】
 *  ・入力チェック失敗時は forward（入力値・エラーメッセージを保持）
 *  ・登録成功時は redirect（二重送信防止）
 *----------------------------------------------------------------------**/
public class SaveSurvey extends HttpServlet {
	private static final long serialVersionUID = 1L;

	public SaveSurvey() {
		super();
	}

	/**
	 * GETリクエストを受け取った場合も、POST処理に統一する
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		doPost(request, response);
	}

	/**
	 * アンケート送信時のメイン処理
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		//========================================
		// 文字コード設定
		//========================================
		// レスポンス（HTML出力）の文字コードをUTF-8に設定
		response.setContentType("text/html;charset=UTF-8");
		// リクエスト（フォーム入力値）の文字コードをUTF-8に設定
		request.setCharacterEncoding("UTF-8");

		//========================================
		// リクエストパラメータの取得
		//========================================
		// ※フォームから送信された値はすべて String 型で受け取る
		String nameParam              = request.getParameter("NAME");               
		String ageParam               = request.getParameter("AGE");                
		String sexParam               = request.getParameter("SEX");                
		String satisfactionLevelParam = request.getParameter("SATISFACTION_LEVEL"); 
		String messageParam           = request.getParameter("MESSAGE");            

		//========================================
		// 入力値を画面に戻すために request に保持
		//========================================
		// 入力エラー時に、ユーザーが再入力しやすいように
		request.setAttribute("NAME", nameParam);
		request.setAttribute("AGE", ageParam);
		request.setAttribute("SEX", sexParam);
		request.setAttribute("SATISFACTION_LEVEL", satisfactionLevelParam);
		request.setAttribute("MESSAGE", messageParam);

		//========================================
		// 入力値のバリデーションチェック
		//========================================
		// DTO（データをまとめて運ぶ箱）を生成
		SurveyDto dto = new SurveyDto();
		// エラーメッセージを複数保持するためのリスト
		List<String> errorMessages = new ArrayList<>();

		// 入力チェックを実行
		if (!isValidInput(
				nameParam,
				ageParam,
				sexParam,
				satisfactionLevelParam,
				messageParam,
				dto,
				errorMessages)) {

			// 入力エラーがある場合は、エラーメッセージを request にセット
			request.setAttribute("errorMessages", errorMessages);

			// 入力画面に forward（入力値・エラーメッセージを保持したまま戻す）
			// getRequestDispatcher().forward() は元のメソッドをに返す。InputSurveyのdoGetから来たのでdoGetに返す
			RequestDispatcher rd = request.getRequestDispatcher("InputSurvey");
			rd.forward(request, response);
			return; // 以降の処理（DB登録）は行わない
		}

		//========================================
		// DTO に登録日時を設定
		//========================================
		// DBに登録する際の「回答時間」として、現在時刻を設定
		dto.setTime(new Timestamp(System.currentTimeMillis()));

		//========================================
		// DB登録処理の実行
		//========================================
		SaveSurveyBL logic = new SaveSurveyBL();
		// true：登録成功 / false：登録失敗
		boolean succesInsert = logic.executeInsertSurvey(dto);

		//========================================
		// 処理結果に応じた画面遷移
		//========================================
		if (succesInsert) {
			// 登録成功 → 完了画面へ（リダイレクト）
			response.sendRedirect("htmls/finish.html");
		} else {
			// 登録失敗 → エラー画面へ
			response.sendRedirect("htmls/error.html");
		}
	}

	/**
	 * 入力値のサーバーサイドチェックを行うメソッド
	 *
	 * 【処理内容】
	 *  ・未入力チェック
	 *  ・数値変換チェック
	 *  ・値の範囲チェック
	 *  ・問題なければ DTO に正しい値を格納
	 *
	 * @return true：入力OK / false：入力NG
	 */
	private boolean isValidInput(
			String name,
			String ageStr,
			String sexStr,
			String levelStr,
			String message,
			SurveyDto dto,
			List<String> errors) {

		//========================================
		// 未入力チェック
		//========================================
		// null または 空白のみの入力はエラーとする
		if (name == null || name.trim().isEmpty()) {
			errors.add("名前を入力してください。");
		}
		if (ageStr == null || ageStr.trim().isEmpty()) {
			errors.add("年齢を入力してください。");
		}
		if (sexStr == null || sexStr.trim().isEmpty()) {
			errors.add("性別を選択してください。");
		}
		if (levelStr == null || levelStr.trim().isEmpty()) {
			errors.add("満足度を選択してください。");
		}
		if (message == null || message.trim().isEmpty()) {
			errors.add("ご意見・ご感想を入力してください。");
		}

		// 未入力エラーが1つでもあれば処理終了
		if (!errors.isEmpty()) {
			return false;
		}

		//========================================
		// 数値変換チェック
		//========================================
		int age;
		int sex;
		int level;
		try {
			// 文字列を数値に変換（変換できなければ例外発生）
			age   = Integer.parseInt(ageStr);
			sex   = Integer.parseInt(sexStr);
			level = Integer.parseInt(levelStr);
		} catch (NumberFormatException e) {
			errors.add("年齢・性別・満足度は数値で入力してください。");
			return false;
		}

		//========================================
		// 値の範囲チェック
		//========================================
		if (age <= 0) {
			errors.add("年齢は正の数で入力してください。");
		}
		if (sex != 1 && sex != 2) {
			errors.add("性別は1(オス)/2(メス)から選択してください。");
		}
		if (level < 1 || level > 5) {
			errors.add("満足度は1～5の範囲で選択してください。");
		}

		if (!errors.isEmpty()) {
			return false;
		}

		//========================================
		// 正しい入力値を DTO に格納
		//========================================
		// ※ここに来る時点で、すべての入力値は「安全」である
		dto.setName(name.trim());
		dto.setAge(age);
		dto.setSex(sex);
		dto.setSatisfactionLevel(level);
		dto.setMessage(message.trim());

		return true;
	}
}