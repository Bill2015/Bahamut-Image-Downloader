package jiaxiang.org;

import java.io.File;

import javafx.beans.property.SimpleListProperty;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCodeCombination;
import javafx.scene.input.KeyCombination.ModifierValue;
import javafx.scene.layout.HBox;
import javafx.scene.text.Font;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import jiaxiang.org.components.AlertInfo;
import jiaxiang.org.components.ImageBox;
import jiaxiang.org.components.MyLabel;
import jiaxiang.org.components.ProgressStage;
import jiaxiang.org.utils.ZipService;


public class DownloadPane extends HBox{
    /** 檔案下載按鈕 */
    final private Button downloadBtn;
    /** 要下載的圖片集名稱 */
    final private TextField fileNamField;
    /** 錯誤與警告提示欄 */
    final private AlertInfo alertInfo;

    public DownloadPane(){
        downloadBtn = new Button( "下載成 .Zip 檔" );
        downloadBtn.setFont( new Font("微軟正黑體", 14) );
        downloadBtn.getStyleClass().add("downloadBtn");

        fileNamField = new TextField( "圖片" );
        fileNamField.setFont( new Font("微軟正黑體", 14) );

        MyLabel title = new MyLabel("圖片檔名： ", 18);

        setAlignment( Pos.BASELINE_LEFT );

        setSpacing( 15 );
        setPadding( new Insets(0, 0, 20, 0) );
        getChildren().addAll(  downloadBtn, title, fileNamField );
        //錯誤訊息提示文字初始化
        alertInfo = new AlertInfo();
    }
    /** 提示警告訊息
     *  @param text 要提示的文字 */
    final public void alert( String text ){
        alertInfo.alert( downloadBtn, text);
    }
    /** 取得圖片集檔案名稱
     *  @return 檔案名稱 {@code [String]} */
    public String getFileName(){ return fileNamField.getText(); };

    //============================================================================================
    //============================================================================================
    /** 搜尋功能初始化 
     *  @param imageBoxList 圖片串列*/
    final public void downloadEventInitialize( SimpleListProperty<ImageBox> imageBoxList, Stage primaryState ){
        downloadBtn.setOnAction( evt -> downloadOperation( imageBoxList, primaryState ) );
        setOnKeyPressed(evt -> {
            if (evt.getCode() == KeyCode.ENTER)
                downloadOperation( imageBoxList, primaryState );
        });
        KeyCodeCombination qucikSaveFile = new KeyCodeCombination(  KeyCode.S, 
                                                                    ModifierValue.UP, 
                                                                    ModifierValue.DOWN,
                                                                    ModifierValue.UP, 
                                                                    ModifierValue.UP, 
                                                                    ModifierValue.UP);
                    primaryState.getScene().getAccelerators().put(qucikSaveFile, () -> downloadOperation( imageBoxList, primaryState ) );
    }
    /** 搜尋時的動作處理 
     *  @param imageBoxList 圖串*/
    final private void downloadOperation( SimpleListProperty<ImageBox> imageBoxList, Stage primaryState ){
         // 假如圖片顯示區塊是空的就不要執行
         if ( imageBoxList.filtered( imageBox -> imageBox.isNeedSaved() ).size() <= 0 ) {
            alert("空空的不能輸出檔案喔！");
            return;
        }
        try {
            // 檔案路徑選擇器
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("儲存圖檔 Zip");
            fileChooser.setInitialDirectory( new File( "A", System.getProperty("user.home") + "/Desktop") );
            fileChooser.getExtensionFilters().add( new FileChooser.ExtensionFilter("zip 壓縮檔", "*.zip") );
            File zipFile = fileChooser.showSaveDialog( primaryState );
            if (zipFile == null)
                return;

            // 壓縮服務
            ZipService zipService = new ZipService(zipFile, imageBoxList, fileNamField.getText() );
            ProgressStage progressStage = new ProgressStage("檔案壓縮中...", "進度", primaryState, zipService.getMaxZipCount() );
            progressStage.getProgressBar().progressProperty().bind( zipService.progressProperty() );
            progressStage.show();
            zipService.start();

          
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println("錯誤！");
        }
    }
}
