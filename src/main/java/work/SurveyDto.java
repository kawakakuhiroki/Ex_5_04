package work;

import java.sql.Timestamp;

/**----------------------------------------------------------------------*
 *■■■SurveyDtoクラス■■■
 *概要：DTO（「survey」テーブル）
 *----------------------------------------------------------------------**/
public class SurveyDto {

	//----------------------------------------------------------------
	//フィールド
	//----------------------------------------------------------------
	private String    name ;                //名前
	private int       age ;                 //年齢
	private int       sex ;                 //性別
	private int       satisfactionLevel ;   //満足度
	private String    message ;             //メッセージ
	private Timestamp time ;                //更新時刻

	//----------------------------------------------------------------
	//getter/setter
	//----------------------------------------------------------------

	//getter/setter（対象フィールド：name）
	public String getName() { return name; }
	public void setName(String name) { this.name = name; }

	//getter/setter（対象フィールド：age）
	public int getAge() { return age; }
	public void setAge(int age) { this.age = age; }

	//getter/setter（対象フィールド：sex）
	public int getSex() { return sex; }
	public void setSex(int sex) { this.sex = sex; }

	//getter/setter（対象フィールド：satisfactionLevel）
	public int getSatisfactionLevel() { return satisfactionLevel; }
	public void setSatisfactionLevel(int satisfactionLevel) { this.satisfactionLevel = satisfactionLevel; }

	//getter/setter（対象フィールド：message）
	public String getMessage() { return message; }
	public void setMessage(String message) { this.message = message; }

	//getter/setter（対象フィールド：time）
	public Timestamp getTime() { return time; }
	public void setTime(Timestamp time) { this.time = time; }

}
