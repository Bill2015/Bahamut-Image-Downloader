package jiaxiang.org;

import java.io.IOException;
import java.util.ArrayList;


import javafx.application.Application;
import javafx.application.Platform;
import javafx.beans.property.SimpleListProperty;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.geometry.Insets;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import jiaxiang.org.components.ImageBox;
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
public class App extends Application{

    /** 主要的 Stage<p>
     * 通常只用在開啟第二個視窗時需要鎖住 </p>
     */
    public CustomStage PRIMARY_STAGE;

    /** 圖片串列 */
    private SimpleListProperty<ImageBox> imageBoxList;

    /** 上方工具列 */
    private TitleMenuBar menuBar;

    /** 網址輸入列 */
    private SearchPane searchPane;

    /** 主要圖片預覽列 */
    private ImagePane imagePane;

    /** 過濾器頁面 */
    private FilterPane filterPane;

    /** 下載的面板 */
    private DownloadPane downloadPane;

    public static void main(String[] args) {
        try {
            launch(args);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void start(Stage primaryStage) throws IOException {

        menuBar         = new TitleMenuBar(PRIMARY_STAGE); // 上方工具列
        searchPane      = new SearchPane(); // 網址輸入列
        imagePane       = new ImagePane(); // 主要圖片預覽列
        downloadPane    = new DownloadPane(); // 下載面板
        filterPane      = new FilterPane(); // 篩選面板

        // 主要介面布局
        Insets insets = new Insets(5);
        BorderPane borderPane = new BorderPane(imagePane, searchPane, null, downloadPane, null);
        borderPane.setPadding(new Insets(10));
        BorderPane.setMargin(imagePane, insets);
        BorderPane.setMargin(searchPane, insets);
        BorderPane.setMargin(downloadPane, insets);
        borderPane.setPadding(new Insets(0, 30, 0, 30));

        StackPane stackPane = new StackPane(new BorderPane(borderPane, menuBar, null, null, filterPane));
        stackPane.getStylesheets().add(getClass().getResource("/styles/borderTitle.css").toExternalForm());
        stackPane.getStylesheets().add(getClass().getResource("/styles/tabPane.css").toExternalForm());
        stackPane.getStylesheets().add(getClass().getResource("/styles/alertMenu.css").toExternalForm());
        stackPane.getStylesheets().add(getClass().getResource("/styles/button.css").toExternalForm());
        stackPane.getStylesheets().add(getClass().getResource("/styles/imageBox.css").toExternalForm());
        stackPane.getStylesheets().add(getClass().getResource("/styles/progressBar.css").toExternalForm());

        stackPane.setStyle("-fx-background-color: #3a3a3a");

        // 使用一個名為 Custom Stage 的函式庫 Web： https://github.com/Oshan96/CustomStage/wiki
        PRIMARY_STAGE = new CustomStageBuilder().setTitleColor("#FFFFFF") // 設定標顏色
                .setWindowColor("rgba(66,66,66,0.4)") // Color of the window (used alpha range to make transparent)
                .setIcon("/textures/logo.jpg").setWindowTitle("巴哈圖串下載器", HorizontalPos.RIGHT, HorizontalPos.CENTER)
                .build();

        PRIMARY_STAGE.changeScene(stackPane);
        PRIMARY_STAGE.show();

        //註冊 ImageBox 改變事件
        addImageBoxListListner();

        // 初始化搜尋按鈕與 Enter 鍵
        searchPane.searchEventInitialize( imageBoxList );

        // 初始化搜尋按鈕與 Enter 鍵
        filterPane.filterEventInitialize( imageBoxList, PRIMARY_STAGE );

        // 初始化圖片下載按鈕與 Enter 鍵
        downloadPane.downloadEventInitialize( imageBoxList, PRIMARY_STAGE );
    }

    private void addImageBoxListListner(){
        imageBoxList = new SimpleListProperty<ImageBox>( FXCollections.observableArrayList(new ArrayList<ImageBox>()) );
        imageBoxList.addListener((ListChangeListener.Change<? extends ImageBox> object) -> {
            while( object.next() ){
                if( object.wasAdded() ){
                    Platform.runLater( () -> imagePane.getFlowPane().getChildren().addAll( object.getAddedSubList() ) );
                }
                else if( object.wasRemoved() ){
                    Platform.runLater(  () -> imagePane.getFlowPane().getChildren().removeAll( object.getRemoved() )  );
                }
            }
        } );
    }

}

