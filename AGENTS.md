# Repository Guidelines

## プロジェクト構成とモジュール整理
- `src/main/java/work`: Javaサーブレットと業務ロジック（`InputSurvey`, `SaveSurvey`, `ShowAllSurvey`, `*BL`, `SurveyDao`, `SurveyDto`）。
- `src/main/webapp`: Webアセットと設定。
  - `WEB-INF/web.xml`: サーブレットのマッピング。
  - `WEB-INF/lib`: 依存JAR（例：MySQL Connector）。
  - `htmls`: エラー／完了画面の静的HTML。
- `build/classes`: 生成された`.class`（編集しない）。

## ビルド・テスト・開発コマンド
本リポジトリにはMaven/Gradleの設定がありません。IDEまたはサーブレットコンテナ（例：Tomcat）でビルド／実行してください。

手動コンパイル例（パスは環境に合わせて調整）：
```sh
javac -d build/classes -cp src/main/webapp/WEB-INF/lib/mysql-connector-j-9.3.0.jar src/main/java/work/*.java
```
デプロイ後、`/InputSurvey` へアクセスして開始します。

## コーディングスタイルと命名規則
- インデント：既存のJavaはタブ。統一してください。
- 命名：クラスは`UpperCamelCase`、メソッド／変数は`lowerCamelCase`。
- `web.xml`のマッピングとサーブレットクラス名は必ず一致させる。
- コメント：既存の日本語ブロックコメントの形式に合わせる。

## テストガイドライン
- 既存のテスト構成はありません。
- テストを追加する場合は `src/test/java` に配置し、`*Test` 命名（JUnit推奨）。併せてビルドツールの導入が必要です。

## コミット・PRガイドライン
- コミットは短く要点を示す（例：「add project files」「InputSurvey.java修正」）。日本語可。
- PRには変更内容の概要、関連Issue（あれば）、UI/HTMLの変更はスクリーンショットを添付。

## セキュリティと設定
- DB接続情報は `src/main/java/work/SurveyDao.java` にあります。実運用の資格情報はコミットしないでください。
- MySQLの`survey`テーブルは `NAME`, `AGE`, `SEX`, `SATISFACTION_LEVEL`, `MESSAGE`, `TIME` を持つ前提です。
