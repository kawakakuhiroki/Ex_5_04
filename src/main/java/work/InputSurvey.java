package work;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

/**----------------------------------------------------------------------*
 *■■■InputSurveyクラス■■■
 *概要：サーブレット
 *詳細：HTML文書（回答入力画面）を出力する。
 *----------------------------------------------------------------------**/
public class InputSurvey extends HttpServlet {
	private static final long serialVersionUID = 1L;

	public InputSurvey() {
		super();
	}

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		//SaveSurveyからforwardされた場合、エラーと入力値を取得
		@SuppressWarnings("unchecked")
		List<String> errors = (List<String>) request.getAttribute("errorMessages");
		String nameValue    = escape( valueOrEmpty(request.getAttribute("NAME")) );
		String ageValue     = escape( valueOrEmpty(request.getAttribute("AGE")) );
		String sexValue     = valueOrEmpty(request.getAttribute("SEX"));
		String levelValue   = valueOrEmpty(request.getAttribute("SATISFACTION_LEVEL"));
		String messageValue = escape( valueOrEmpty(request.getAttribute("MESSAGE")) );

		//レスポンス（出力データ）の文字コードを設定
		response.setContentType("text/html;charset=UTF-8");  //文字コードをUTF-8で設定

		//出力用のストリームの取得
		PrintWriter out = response.getWriter();

		//HTML文書（回答入力画面）の出力
		out.println("<html>                                                                                  ");
		out.println("<head>                                                                                  ");
		out.println("  <title>回答入力画面</title>                                                               ");
		out.println("</head>                                                                                 ");
		out.println("<body>                                                                                  ");
		out.println("  <h2>わんちゃん暮らし改善アンケートフォーム</h2>                                       ");
		if (errors != null && !errors.isEmpty()) {
			out.println("  <div style=\"color:red;\">");
			for (String error : errors) {
				out.println("    <p>" + escape(error) + "</p>");
			}
			out.println("  </div>");
		}
		out.println("  <form action=\"SaveSurvey\" method=\"post\">                                          ");
		out.println("    <p>名前：                                                                           ");
		out.println("      <input type=\"text\" name=\"NAME\" maxlength = \"20\" value=\"" + nameValue + "\">");
		out.println("    </p>                                                                                ");
		out.println("    <p>年齢：                                                                           ");
		out.println("      <input type=\"number\" name=\"AGE\" maxlength = \"3\" value=\"" + ageValue + "\"> ");
		out.println("    </p>                                                                                ");
		out.println("    <p>性別：                                                                           ");
		out.println("      <input type=\"radio\" name=\"SEX\" value=\"1\" " + ("1".equals(sexValue) ? "checked" : "") + ">オス");
		out.println("      <input type=\"radio\" name=\"SEX\" value=\"2\" " + ("2".equals(sexValue) ? "checked" : "") + ">メス");
		out.println("    </p>                                                                                ");
		out.println("    <p> 満足度：                                                                        ");
		out.println("      <select name=\"SATISFACTION_LEVEL\">                                              ");
		out.println("        <option value=\"5\" " + ("5".equals(levelValue) ? "selected" : "") + ">とても満足</option>");
		out.println("        <option value=\"4\" " + ("4".equals(levelValue) ? "selected" : "") + ">満足      </option>");
		out.println("        <option value=\"3\" " + ("3".equals(levelValue) ? "selected" : "") + ">普通      </option>");
		out.println("        <option value=\"2\" " + ("2".equals(levelValue) ? "selected" : "") + ">不満      </option>");
		out.println("        <option value=\"1\" " + ("1".equals(levelValue) ? "selected" : "") + ">とても不満</option>");
		out.println("      </select>                                                                         ");
		out.println("    </p>                                                                                ");
		out.println("    <p>飼い主へのご意見・ご感想をご記入ください：<br>                                   ");
		out.println("      <textarea name=\"MESSAGE\" rows=\"4\" cols=\"50\" maxlength = \"250\">" + messageValue + "</textarea> ");
		out.println("    </p>                                                                                ");
		out.println("    <input type=\"submit\" value=\"回答する(SaveSurveyを起動)\">                        ");
		out.println("    <br>                                                                                ");
		out.println("    <a href=\"ShowAllSurvey\">                                                          ");
		out.println("    回答一覧画面へ"                                                                      );
		out.println("    </a>                                                                                ");
		out.println("</body>                                                                                 ");
		out.println("</html>                                                                                 ");

	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		doGet(request, response);
	}

	private String valueOrEmpty(Object obj) {
	    // obj が null の場合は空文字を返す
	    if (obj == null) {
	        return "";
	    }
	    // obj が null でない場合は文字列に変換して返す
	    return obj.toString();
	}

	//エスケープして画面に戻す
	private String escape(String value) {
		return value
				.replace("&", "&amp;")
				.replace("<", "&lt;")
				.replace(">", "&gt;")
				.replace("\"", "&quot;");
	}

}
