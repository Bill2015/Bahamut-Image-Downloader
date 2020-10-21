package jiaxiang.org;

import java.io.IOException;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import jiaxiang.org.controller.DownloadController;
import jiaxiang.org.controller.FilterController;
import jiaxiang.org.controller.ImageViewController;
import jiaxiang.org.controller.TitleMenuController;
import jiaxiang.org.controller.SearchController;
import jiaxiang.org.model.DataModel;
import jiaxiang.org.view.DownloadView;
import jiaxiang.org.view.FilterView;
import jiaxiang.org.view.HistoryView;
import jiaxiang.org.view.ImagePaneView;
import jiaxiang.org.view.TitleMenuView;
import jiaxiang.org.view.SearchView;
import lk.vivoxalabs.customstage.CustomStage;
import lk.vivoxalabs.customstage.CustomStageBuilder;
import lk.vivoxalabs.customstage.tools.HorizontalPos;

/***********************************************************************
 *  Baha Image Downloader 
 *
 *  Author: Bill2015 
 * 
 *  Date:   14/10/2020
 * 
 *  Descirption:
 *     用此程式可以方便且快速的下載巴哈的圖串，
 *     並且可以自訂義樓層數與 推(GP)與噓(BP)數
 *     但要是注意，這些可能都會造成巴哈伺服器負
 *     擔，所以小心使用，不要一次載大量的圖片應
 *     該是沒什麼問題，
 ***********************************************************************/

/**
 * 主要的程式類別
 * <p>
 * 繼承了 {@link Application}
 */
public class App extends Application {

    /**主要的 Stage<p>
     * 通常只用在開啟第二個視窗時需要鎖住</p>
     */
    public CustomStage PRIMARY_STAGE;
    /** 主程式資料 */
    private DataModel dataModel;
    public static void main(String[] args) {
        try {
            launch(args);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void start(Stage primaryStage) throws IOException {

        // Model 初始化
        dataModel       = new DataModel();  //主程式資料

        //View 初始化
        final TitleMenuView titleMenuView   = new TitleMenuView();    // 上方工具列
        final SearchView    searchView      = new SearchView();     // 網址輸入列
        final ImagePaneView imagePaneView   = new ImagePaneView();  // 主要圖片預覽列
        final DownloadView  downloadView    = new DownloadView();   // 下載面板
        final FilterView    filterView      = new FilterView();     // 篩選面板
        final HistoryView   historyView     = new HistoryView();    // 歷史紀錄面板


        // 主要介面布局
        Insets insets = new Insets(5);
        BorderPane borderPane = new BorderPane(imagePaneView, searchView, null, downloadView, null);
        borderPane.setPadding(new Insets(10));
        BorderPane.setMargin(imagePaneView, insets);
        BorderPane.setMargin(searchView, insets);
        BorderPane.setMargin(downloadView, insets);
        borderPane.setPadding(new Insets(0, 30, 0, 30));

        StackPane stackPane = new StackPane(new BorderPane(borderPane, titleMenuView, historyView, null, filterView));
        stackPane.getStylesheets().addAll( 
            "/styles/borderTitle.css",
            "/styles/tabPane.css",
            "/styles/alertMenu.css",
           "/styles/button.css",
            "/styles/nodeBox.css",
            "/styles/progressBar.css"
        );


        // 使用一個名為 Custom Stage 的函式庫 Web： https://github.com/Oshan96/CustomStage/wiki
        PRIMARY_STAGE = new CustomStageBuilder().setTitleColor("#FFFFFF") // 設定標顏色
                .setWindowColor("rgba(66,66,66,0.4)") // Color of the window (used alpha range to make transparent)
                .setIcon("/textures/logo.jpg").setWindowTitle("巴哈圖串下載器", HorizontalPos.RIGHT, HorizontalPos.CENTER)
                .build();

        stackPane.setStyle("-fx-background-color: #3a3a3a");

        PRIMARY_STAGE.changeScene( stackPane );
        PRIMARY_STAGE.show();

        //Controller 初始化
        final ImageViewController   imageViewController = new ImageViewController( dataModel, imagePaneView );
        final SearchController      searchController    = new SearchController( dataModel, searchView );
        final FilterController      filterController    = new FilterController( dataModel, filterView, PRIMARY_STAGE );
        final DownloadController    downloadController  = new DownloadController( dataModel, downloadView, PRIMARY_STAGE );
        final TitleMenuController   titleMenuController = new TitleMenuController( dataModel, titleMenuView, PRIMARY_STAGE );


    }




}

