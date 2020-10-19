package jiaxiang.org;

import javafx.animation.SequentialTransition;
import javafx.animation.TranslateTransition;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.stage.Stage;
import javafx.util.Duration;
import jiaxiang.org.components.InfoStage;
import jiaxiang.org.components.MyLabel;



public class TitleMenuBar extends HBox{

    final private Stage stage;
    
    public TitleMenuBar( Stage stage ){
        this.stage = stage;

        getStylesheets().add(getClass().getResource("/styles/menuBar.css").toExternalForm());

        //程式 Icon
        MenuBar menu = new MenuBar();
        menu.getMenus().add( createIconMenu()  );

        //視窗
        menu.getMenus().add( new Menu( "視窗" )  );

        //幫助
        menu.getMenus().add( new Menu("幫助"){{getItems().addAll( helpMenuItemInitialize() );}}  );

        //關於
        menu.getMenus().add( new Menu("關於"){{getItems().addAll(aboutMenuItemInitialize());}}  );

        getChildren().addAll( menu );
        setHgrow( menu, Priority.ALWAYS );
      
    }
    //==========================================================
    /** 建立 關於(About) 的 MenuItem */
    private MenuItem[] aboutMenuItemInitialize(){
        MenuItem aboutMenu1 = new MenuItem( "版本" );
        aboutMenu1.setOnAction(  evt -> {
            new InfoStage("版本",       "Version:                   Beta v1.0\n" +
                                        "Release Date:         14/10/2020\n" +
                                        "Author:                    Bill2015", stage ).show();
        });
        MenuItem aboutMenu2 = new MenuItem( "軟體資訊" );
        aboutMenu2.setOnAction(  evt -> {
            new InfoStage("軟體資訊",    "主要介面：JavaFx\n" +
                                        "網路爬蟲：Jsoup\n" +
                                        "編譯環境：Apache Maven\n" +
                                        "整合開發環境：Visual Studio", stage ).show();
        });
        MenuItem aboutMenu3 = new MenuItem( "更新紀錄" );
        aboutMenu3.setOnAction(  evt -> {
            new InfoStage("更新紀錄", "無", stage ).show();
        });


        return new MenuItem[]{ aboutMenu1, aboutMenu2, aboutMenu3 };
    }
    //==========================================================
    /** 建立 幫助(Help) 的 MenuItem */
    private MenuItem[] helpMenuItemInitialize(){
        MenuItem helpMenu1 = new MenuItem( "如何使用" );
        helpMenu1.setOnAction(  evt -> {
            new InfoStage("如何使用", "在搜尋欄裡，貼上巴哈的任何一個討論串，然後按下搜尋即可看到結果\n" +
                                        "Demo URL:\n" +
                                        "https://forum.gamer.com.tw/C.php?bsn=60076&snA=5962581&tnum=34&bPage=2", stage ).show();
        });
        MenuItem helpMenu2 = new MenuItem( "快速操作" );
        helpMenu2.setOnAction(  evt -> {
            new InfoStage("快速操作", "快速存檔     快捷鍵（Ctrl + S）\n" +
                                        "快速篩選     快捷鍵（Ctrl + F）\n" +
                                        "快速重置     快捷鍵（Ctrl + R）", stage ).show();
        });
        MenuItem helpMenu3 = new MenuItem( "注意事項" );
        helpMenu3.setOnAction(  evt -> {
            new InfoStage("注意事項", "1. 當大量抓取伺服器資料，會對巴哈伺服器造成負擔\n" +
                                        "2. 樓層數越少，爬蟲的時間會越快\n" +
                                        "3. 樓層數一開始就得先訂好，假如之後要更改會需要重新抓取資料\n" +
                                        "4. 假如一次載大量的圖串時，可能會造成記憶體不足\n" +
                                        "5. 目前只確定 png, jpg, gif 能正常存檔，其他種類的圖檔還尚未確定", stage ).show();
        });

        return new MenuItem[]{ helpMenu1, helpMenu2, helpMenu3 };
    }
    //==========================================================
    /** 建立 Icon Menu */
    private Menu createIconMenu(){
        Menu iconMenu = new Menu( "" );
        ImageView view = new ImageView( "/textures/logo.jpg" );
        view.setSmooth(true);
        view.setFitHeight( 24 );
        view.setFitWidth( 24 );
        iconMenu.setDisable( false );
        iconMenu.setGraphic( view );

        for(MenuItem item : new MenuItem[20] ){
            item = new MenuItem( "", new MyLabel("A", 16) );
            iconMenu.getItems().add( item );
        }

        //動畫，沒意義的吧
        SequentialTransition sequentialTransition = new SequentialTransition();
        iconMenu.setOnShowing( evt -> {
            for (MenuItem item : iconMenu.getItems()) {
                TranslateTransition transition1 = new TranslateTransition( Duration.millis( 100 ), item.getGraphic() );
                transition1.setFromX( -100 );transition1.setToX( 0 );
                sequentialTransition.getChildren().add( transition1 );
            }
            for (MenuItem item : iconMenu.getItems()) {
                TranslateTransition transition1 = new TranslateTransition( Duration.millis( 100 ), item.getGraphic() );
                transition1.setFromX( 0 );transition1.setToX( -100 );
                sequentialTransition.getChildren().add( transition1 );
            }
            sequentialTransition.setCycleCount( 10 );
            sequentialTransition.play();
        } );
        iconMenu.setOnHiding( evt -> {
            sequentialTransition.stop();
        } );

        return iconMenu;
    }
}

