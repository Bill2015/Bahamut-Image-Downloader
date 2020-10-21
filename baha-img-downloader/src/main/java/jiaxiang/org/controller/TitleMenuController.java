package jiaxiang.org.controller;

import jiaxiang.org.model.DataModel;
import javafx.scene.control.MenuItem;
import javafx.stage.Stage;
import jiaxiang.org.view.TitleMenuView;
import jiaxiang.org.components.InfoStage;

public class TitleMenuController {
    /** 主程式資料模型 */
    final private DataModel dataModel;
    /** 下載介面 */
    final private TitleMenuView menuBarView;
    /** 主要的 Stage */
    final private Stage primaryState;
    /** 建構子 
     *  @param dataModel 主程式資料模型
     *  @param view 介面*/
    public TitleMenuController( DataModel dataModel, TitleMenuView view, Stage primaryState  ){
        this.dataModel      = dataModel;
        this.menuBarView    = view;
        this.primaryState   = primaryState;

        helpMenuButtonInitialize();
        aboutMenuButtonInitialize();
    }

    /** {@code 幫助} 按鈕初始化 */
    private void helpMenuButtonInitialize(){
        MenuItem menuItem[] = menuBarView.getHelpMenuItem();
        menuItem[0].setOnAction(  evt -> {
            new InfoStage("版本",       "Version:                   Beta v1.0\n" +
                                        "Release Date:         14/10/2020\n" +
                                        "Author:                    Bill2015", primaryState ).show();
        });
        menuItem[1].setOnAction(  evt -> {
            new InfoStage("軟體資訊",    "主要介面：JavaFx\n" +
                                        "網路爬蟲：Jsoup\n" +
                                        "編譯環境：Apache Maven\n" +
                                        "整合開發環境：Visual Studio", primaryState ).show();
        });
        menuItem[2].setOnAction(  evt -> {
            new InfoStage("更新紀錄", "無", primaryState ).show();
        });
    }

    /** {@code 幫助} 按鈕初始化 */
    private void aboutMenuButtonInitialize(){
        MenuItem menuItem[] = menuBarView.getAboutMenuItem();
        menuItem[0].setOnAction(  evt -> {
            new InfoStage("如何使用", "在搜尋欄裡，貼上巴哈的任何一個討論串，然後按下搜尋即可看到結果\n" +
                                        "Demo URL:\n" +
                                        "https://forum.gamer.com.tw/C.php?bsn=60076&snA=5962581&tnum=34&bPage=2", primaryState ).show();
        });
        menuItem[1].setOnAction(  evt -> {
            new InfoStage("快速操作", "快速存檔     快捷鍵（Ctrl + S）\n" +
                                        "快速篩選     快捷鍵（Ctrl + F）\n" +
                                        "快速重置     快捷鍵（Ctrl + R）", primaryState ).show();
        });
        menuItem[2].setOnAction(  evt -> {
            new InfoStage("注意事項", "1. 當大量抓取伺服器資料，會對巴哈伺服器造成負擔\n" +
                                        "2. 樓層數越少，爬蟲的時間會越快\n" +
                                        "3. 樓層數一開始就得先訂好，假如之後要更改會需要重新抓取資料\n" +
                                        "4. 假如一次載大量的圖串時，可能會造成記憶體不足\n" +
                                        "5. 目前只確定 png, jpg, gif 能正常存檔，其他種類的圖檔還尚未確定", primaryState ).show();
        });
    }
        
}
