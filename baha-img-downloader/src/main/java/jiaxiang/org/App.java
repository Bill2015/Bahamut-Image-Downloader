package jiaxiang.org;
 
import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import javax.imageio.ImageIO;

import javafx.animation.RotateTransition;
import javafx.application.Application;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.layout.StackPane;
import javafx.scene.Node;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCodeCombination;
import javafx.scene.input.KeyCombination.ModifierValue;
import javafx.scene.layout.BorderPane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.util.Duration;
import jiaxiang.org.components.ImageBox;
import jiaxiang.org.util.SearchService;
import lk.vivoxalabs.customstage.CustomStage;
import lk.vivoxalabs.customstage.CustomStageBuilder;
import lk.vivoxalabs.customstage.tools.HorizontalPos;


import java.awt.image.BufferedImage;

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

/** 主要的程式類別 <p>
 *  繼承了 {@link Application} */
public class App extends Application {
    
    /** 主要的 Stage
     *  <p>通常只用在開啟第二個視窗時需要鎖住</p>*/
    public CustomStage PRIMARY_STAGE;

    /** 圖片串列 */
    private ArrayList<ImageBox> imageBoxList;

    /**上方工具列 */
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
        try{
            launch(args);
        }catch(Exception e){ e.printStackTrace(); }
    }
    
    @Override
    public void start(Stage primaryStage) throws IOException {

        menuBar         = new TitleMenuBar( PRIMARY_STAGE );         //上方工具列
        searchPane      = new SearchPane();                         //網址輸入列
        imagePane       = new ImagePane();                          //主要圖片預覽列
        downloadPane    = new DownloadPane();                       //下載面板
        filterPane      = new FilterPane();                         //篩選面板


        //主要介面布局
        Insets insets = new Insets( 5 );
        BorderPane borderPane = new BorderPane( imagePane, searchPane, null, downloadPane, null  );
        borderPane.setPadding( new Insets( 10 ) );
        BorderPane.setMargin( imagePane , insets );
        BorderPane.setMargin( searchPane , insets );
        BorderPane.setMargin( downloadPane , insets );
        borderPane.setPadding( new Insets(0, 30, 0, 30) );


        StackPane stackPane = new StackPane( new BorderPane(borderPane, menuBar, null, null, filterPane ) );
        stackPane.getStylesheets().add(getClass().getResource("/styles/borderTitle.css").toExternalForm());
        stackPane.getStylesheets().add(getClass().getResource("/styles/tabPane.css").toExternalForm());
        stackPane.getStylesheets().add(getClass().getResource("/styles/alertMenu.css").toExternalForm());
        stackPane.getStylesheets().add(getClass().getResource("/styles/button.css").toExternalForm());
        stackPane.getStylesheets().add(getClass().getResource("/styles/imageBox.css").toExternalForm());
        stackPane.getStylesheets().add(getClass().getResource("/styles/progressBar.css").toExternalForm());

        stackPane.setStyle( "-fx-background-color: #3a3a3a" );
        
        //使用一個名為 Custom Stage 的函式庫 Web： https://github.com/Oshan96/CustomStage/wiki
        PRIMARY_STAGE = new CustomStageBuilder()
                            .setTitleColor("#FFFFFF")                   //設定標顏色
                            .setWindowColor("rgba(66,66,66,0.4)")              //Color of the window (used alpha range to make transparent)
                            .setIcon("/textures/logo.jpg")
                            .setWindowTitle("巴哈圖串下載器", HorizontalPos.RIGHT, HorizontalPos.CENTER)
                            .build();

        PRIMARY_STAGE.changeScene( stackPane );
        PRIMARY_STAGE.show();

        //搜尋按鈕與 Enter 鍵
        searchPane.getSearchButton()    .setOnAction( evt -> addSearchEventListner() );
        searchPane                      .setOnKeyPressed( evt -> { if( evt.getCode() == KeyCode.ENTER )addSearchEventListner(); } );
       
        //下載按鈕 快捷鍵(Ctrl + S)
        downloadPane.getDownloadButton().setOnAction( evt -> addDownloadEventListner( downloadPane.getFileName() ) );
        KeyCodeCombination qucikSaveFile = new KeyCodeCombination( KeyCode.S, ModifierValue.UP, ModifierValue.DOWN, ModifierValue.UP, ModifierValue.UP, ModifierValue.UP);
        PRIMARY_STAGE.getScene().getAccelerators().put( qucikSaveFile , () -> addDownloadEventListner( downloadPane.getFileName() ) );

        //篩選按鈕 快捷鍵(Ctrl + F)
        filterPane.getFilterButton()    .setOnAction( evt -> addFilterEventListner() );
        KeyCodeCombination qucikFilter = new KeyCodeCombination( KeyCode.F, ModifierValue.UP, ModifierValue.DOWN, ModifierValue.UP, ModifierValue.UP, ModifierValue.UP);
        PRIMARY_STAGE.getScene().getAccelerators().put( qucikFilter , () -> addFilterEventListner() );
        
        //重置按鈕 快捷鍵(Ctrl + R)
        filterPane.getResetButton()     .setOnAction( evt -> addResetEventListner() );
        KeyCodeCombination qucikReset = new KeyCodeCombination( KeyCode.R, ModifierValue.UP, ModifierValue.DOWN, ModifierValue.UP, ModifierValue.UP, ModifierValue.UP);
        PRIMARY_STAGE.getScene().getAccelerators().put( qucikReset , () -> addResetEventListner() );

    }

    /** 註冊按下搜尋鍵的事件監聽 */
    private void addSearchEventListner(){
        
        //監聽當搜尋鍵按下時
        try{
            int fStart  = searchPane.getFloorStartValue();
            int fEnd    = searchPane.getFloorEndValue();

            SearchService searchService = new SearchService(  searchPane.getInputURL(), fStart, fEnd );
            searchPane.getProgressBar().progressProperty().bind( searchService.progressProperty() );
            searchPane.setProgressBarVisiable( true );
            searchService.start();
            searchService.setOnSucceeded( evt -> {
                imageBoxList = searchService.getValue().getResult();
                imagePane.setImageContent( imageBoxList );

                final RotateTransition delay = new RotateTransition();
                delay.setDelay( Duration.seconds(2000) );
                delay.setOnFinished( delayEvt ->  searchPane.setProgressBarVisiable( false ) );
                delay.play();
            } );



        }
        catch( ArrayIndexOutOfBoundsException e ){ 
            searchPane.alert( "輸入的網址不符合！" );
            searchPane.setProgressBarVisiable( false );
        }
        catch( Exception e ){
            searchPane.alert( "神奇的錯誤！" );
            searchPane.setProgressBarVisiable( false );
        }
    }

    /** 註冊按下篩選鍵的事件監聽 */
    private void addFilterEventListner(){
        int gpValue = filterPane.getGPValue();
        int bpValue = filterPane.getBPValue();
        

        ObservableList<Node> contents = imagePane.getImageBoxNodes(); 
        //空的就不要執行了
        if( contents.isEmpty() )
            return;
        //判斷文章的 GP(推) 與 BP(噓) 數
        for( ImageBox imageBox : imageBoxList ){
            if( imageBox.getGPValue() < gpValue || imageBox.getBPValue() < bpValue ){
                imageBox.setNeedSaved( false );
                contents.remove( imageBox );
            }
        }
    }

    /** 註冊按下重置鍵的事件監聽 */
    private void addResetEventListner(){
        //空的就不要執行了
        if( imageBoxList.isEmpty() )
            return;
        //全部刪掉，重新加入
        imagePane.getImageBoxNodes().clear(); 
        imageBoxList.forEach( imageBox -> imageBox.setNeedSaved( true ) );
        imagePane.setImageContent( imageBoxList );
    }

    /** 註冊按下下載鈕事件監聽 */
    private void addDownloadEventListner( String fileName ){
        int count = 1;
        //假如圖片顯示區塊是空的就不要執行
        if( imagePane.getFlowPane().getChildren().isEmpty() ){
            downloadPane.alert( "空空的不能輸出檔案喔！" );
            return;
        }
        try {
            //檔案路徑選擇器
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("儲存圖檔 Zip");
            fileChooser.setInitialDirectory( new File( System.getProperty("user.home") + "/Desktop" ) );
            fileChooser.getExtensionFilters().add( new FileChooser.ExtensionFilter( "zip 壓縮檔", "*.zip") );
            File zipFile = fileChooser.showSaveDialog( PRIMARY_STAGE );
            if( zipFile == null )return;


            //檔案寫入緩衝串列
            BufferedOutputStream bos = new BufferedOutputStream( new FileOutputStream( zipFile ) );
        
            //壓縮檔案寫入串列
            ZipOutputStream out = new ZipOutputStream( bos );
            //寫入圖片
            for (ImageBox imageBox : imageBoxList ) {
                //假如不存檔就直接跳過
                if( imageBox.isNeedSaved() == false )continue;


                //取得圖片檔名
                String fileExt = imageBox.getExtension();
                //將每一張圖片放至 Zip 檔
                out.putNextEntry( new ZipEntry( String.join(".", String.format("%s(%d)", fileName, count++) , fileExt) ) );
                //一般圖片 .PNG .JPG
                if( !fileExt.equalsIgnoreCase("GIF")  ){
                    //將圖片轉成 BufferedImage
                    BufferedImage img = ImageIO.read( imageBox.getURL() );
                    //圖片寫入壓縮檔
                    ImageIO.write( img, fileExt, out );
                }
                //假如是 GIF 就另外處理
                else{
                    //以 URL 串流讀入，原因是轉成 Buffered Image GIF 圖就不會動了
                    InputStream is = imageBox.getURL().openStream();
                    ByteArrayOutputStream bao = new ByteArrayOutputStream();
                    //緩衝區大小
                    byte[] buff = new byte[1024];
                    //將 GIF 轉成 Byte[]
                    for( int bytesRead = 0;  (bytesRead = is.read(buff)) != -1 ;    bao.write(buff, 0, bytesRead) );
                    byte[] data = bao.toByteArray();
                    out.write( data );
                    is.close();bao.close();
                }
                //關閉這次的物件，以換下一個檔案
                out.closeEntry();
            }
            out.close();
        }
        catch( Exception e ){ e.printStackTrace(); System.err.println("錯誤！");  }
    }



}

