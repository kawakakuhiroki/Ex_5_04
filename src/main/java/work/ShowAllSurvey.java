package work;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class ShowAllSurvey extends HttpServlet {
	private static final long serialVersionUID = 1L;

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		doPost(request, response);
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		// レスポンス（出力データ）の文字コードを設定
		response.setContentType("text/html;charset=UTF-8");
		// リクエスト（受信データ）の文字コードを設定
		request.setCharacterEncoding("UTF-8");

		ShowAllSurveyBL logic = new ShowAllSurveyBL();

		List<SurveyDto> select = logic.executeSelectSurvey();

		if (select != null && !select.isEmpty()) {

			// レスポンス（出力データ）の文字コードを設定
			response.setContentType("text/html;charset=UTF-8"); // 文字コードをUTF-8で設定

			// 出力用のストリームの取得
			PrintWriter out = response.getWriter();

			// HTML文書（回答入力画面）の出力
			out.println("<html>                                                                                  ");
			out.println("<head>                                                                                  ");
			out.println("  <title>回答出力</title>                                                               ");
			out.println("</head>                                                                                 ");
			out.println("<body>                                                                                  ");
			out.println("  <h2>アンケート回答一覧</h2>                                                           ");
			out.println("     <table class=\"list\" border=\"1\" id=\"TABLE\">                                   ");
			out.println("       <tr bgcolor=\"#c0c0c0\">                                                         ");
			out.println("         <th>名前</th>                                                                  ");
			out.println("         <th>年齢</th>                                                                  ");
			out.println("         <th>性別</th>                                                                  ");
			out.println("         <th>満足度</th>                                                                ");
			out.println("         <th>ご意見・ご感想</th>                                                        ");
			out.println("         <th>回答時間</th>                                                              ");
			out.println("       </tr>                                                                            ");
			for (int i = 0; i < select.size(); i++) {
				SurveyDto dto = select.get(i);
				out.println("   <tr>                                                                             ");
				out.println("     <td>" + dto.getName() + "</td>                                                 ");
				out.println("     <td>" + dto.getAge() + "</td>                                                  ");
				out.println("     <td>" + dto.getSex() + "</td>                                                  ");
				out.println("     <td>" + dto.getSatisfactionLevel() + "</td>                                    ");
				out.println("     <td>" + dto.getMessage() + "</td>                                              ");
				out.println("     <td>" + dto.getTime() + "</td>                                                 ");
				out.println("   </tr>                                                                            ");
			}
			out.println("     </table>                                                                           ");
			out.println("    <br>                                                                                ");
			out.println("    <a href=\"InputSurvey\">                                                            ");
			out.println("    回答入力画面へ");
			out.println("    </a>                                                                                ");
			out.println("</body>                                                                                 ");
			out.println("</html>                                                                                 ");
		} else {
			// DB登録に失敗した場合、エラー画面（error.html）を表示する
			response.sendRedirect("htmls/error.html");

		}
	}
}