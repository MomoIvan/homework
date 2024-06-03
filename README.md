## Order List

不會主動取得訂單資料，需使用者按下搜尋後才會抓取

* 預設每次顯示 50 筆資料
* 不帶入搜尋條件會返回所有數據

## Upload CSV

將檔案上傳並更新資料庫，會將之前的資料覆蓋

因爲有說要顯示上傳的檔案資料，所以有加入 List 在檔案上傳完成的時候顯示

* 預設每次顯示 50 筆資料

## etc..

Upload CSV 與 Order List 顯示列表的程式碼類似，若有機會可以研究將重複的程式碼整合成元件的方式

嘗試撰寫 API 測試，使用 Junit 遇到 Spring MVC Mockito Test returning 406 及使用 Intellij: Go to autowired bean definition 的問題

有花半天時間嘗試排除但無法正常修復，所以就沒有補上測試
