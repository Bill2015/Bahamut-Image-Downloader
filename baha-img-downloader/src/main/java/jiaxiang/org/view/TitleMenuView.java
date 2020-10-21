package jiaxiang.org.view;

import javafx.animation.SequentialTransition;
import javafx.animation.TranslateTransition;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.util.Duration;
import jiaxiang.org.components.MyLabel;



public class TitleMenuView extends HBox{
    /** 關於的 {@link MenuItem} 
     *  <ul>
     *      <li>{@code [0]} - 版本 (Version)</li>
     *      <li>{@code [1]} - 軟體資訊 (Software Info)</li>
     *      <li>{@code [2]} - 更新資訊 (Update Log)</li>
     * </ul>*/
    final private MenuItem[] aboutMenuItem; 

    /** 幫助以及使用的 {@link MenuItem} 
     *  <ul>
     *      <li>{@code [0]} - 如何使用 (How to use)</li>
     *      <li>{@code [1]} - 快速操作 (Keyboard shortcut)</li>
     *      <li>{@code [2]} - 注意事項 (Notice)</li>
     * </ul>*/
     final private MenuItem[] helpMenuItem; 

    public TitleMenuView(){
        //Css Style
        getStylesheets().add(getClass().getResource("/styles/menuBar.css").toExternalForm());

        //初始化關於按鈕
        aboutMenuItem = new MenuItem[]{
            new MenuItem( "版本" ),
            new MenuItem( "軟體資訊" ),
            new MenuItem( "更新紀錄" )
        };

        //初始化幫助按鈕
        helpMenuItem = new MenuItem[]{
            new MenuItem( "如何使用" ),
            new MenuItem( "快速操作" ),
            new MenuItem( "注意事項" )
        };

        //程式 Icon
        MenuBar menu = new MenuBar();
        menu.getMenus().add( createIconMenu()  );

        //視窗
        menu.getMenus().add( new Menu( "視窗" )  );

        //幫助
        menu.getMenus().add( new Menu("幫助"){{getItems().addAll( helpMenuItem );}}  );

        //關於
        menu.getMenus().add( new Menu("關於"){{getItems().addAll( aboutMenuItem );}}  );

        getChildren().addAll( menu );
        setHgrow( menu, Priority.ALWAYS );
      
    }

    /** 取得 {@code 幫助} 的 {@link MenuItem} 
     *  @return 幫助選單按鈕   {@code [MenuItem[]]}*/
    public MenuItem[] getHelpMenuItem(){ return helpMenuItem; }
    
    /** 取得 {@code 關於} 的 {@link MenuItem} 
     *  @return 選單選單按鈕   {@code [MenuItem[]]}*/
    public MenuItem[] getAboutMenuItem(){ return aboutMenuItem; }
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

