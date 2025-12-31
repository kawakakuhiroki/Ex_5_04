package work;

import java.util.List;

/**----------------------------------------------------------------------*
 *■■■ShowAllSurveyBLクラス■■■
 *概要：ビジネスロジック（アンケートデータの表示）
 *----------------------------------------------------------------------**/

public class ShowAllSurveyBL {

	public List<SurveyDto> executeSelectSurvey() {
		
		
		//-------------------------------------------
		//データベースへの接続を実施
		//-------------------------------------------

		//DAOクラスをインスタンス化＆全てのユーザーデータを登録するよう依頼
		SurveyDao dao = new SurveyDao();
		return dao.doSelect();
	}
}
