package jiaxiang.org.controller;

import java.io.File;

import javafx.beans.property.SimpleListProperty;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCodeCombination;
import javafx.scene.input.KeyCombination.ModifierValue;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import jiaxiang.org.view.DownloadView;
import jiaxiang.org.components.ImageBox;
import jiaxiang.org.components.ProgressStage;
import jiaxiang.org.model.DataModel;
import jiaxiang.org.utils.ZipService;

public class DownloadController{
    /** 主程式資料模型 */
    final private DataModel dataModel;
    /** 下載介面 */
    final private DownloadView downloadView;
    /** 主要的 Stage */
    final private Stage primaryState;
    /** 預設檔案存檔路徑 */
    final private static String DEFAULT_PATH = System.getProperty("user.home") + "/Desktop"; 
    /** 建構子 
     *  @param dataModel 主程式資料模型
     *  @param view 介面*/
    public DownloadController( DataModel dataModel, DownloadView view, Stage primaryState ){
        this.dataModel      = dataModel;
        this.downloadView   = view;
        this.primaryState   = primaryState;

        downloadEventInitialize();
    }

    
    //============================================================================================
    //============================================================================================
    /** 搜尋功能初始化 
     *  @param primaryState 主要視窗(用來綁定快捷鍵)*/
    final public void downloadEventInitialize(){
        downloadView.setOnDownloadButtonAction( evt -> downloadOperation() );
        downloadView.setOnKeyPressed(evt -> {
            if (evt.getCode() == KeyCode.ENTER)
                downloadOperation();
        });
        KeyCodeCombination qucikSaveFile = new KeyCodeCombination(  KeyCode.S, 
                                                                    ModifierValue.UP, 
                                                                    ModifierValue.DOWN,
                                                                    ModifierValue.UP, 
                                                                    ModifierValue.UP, 
                                                                    ModifierValue.UP);
                    primaryState.getScene().getAccelerators().put(qucikSaveFile, () -> downloadOperation() );
    }
    /** 搜尋時的動作處理 
     *  @param primaryState 主要視窗(用來綁定快捷鍵) */
    final private void downloadOperation(){
        final SimpleListProperty<ImageBox> imageBoxList = dataModel.getImageList();

        // 假如圖片顯示區塊是空的就不要執行
        if ( imageBoxList.filtered( imageBox -> imageBox.isNeedSaved() ).size() <= 0 ) {
            downloadView.alert("空空的不能輸出檔案喔！");
            return;
        }
        try {
            // 檔案路徑選擇器
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("儲存圖檔 Zip");
            fileChooser.setInitialDirectory( new File( DEFAULT_PATH ) );
            fileChooser.setInitialFileName( "ImageZip" );
            fileChooser.getExtensionFilters().add( new FileChooser.ExtensionFilter("zip 壓縮檔", "*.zip") );
            File zipFile = fileChooser.showSaveDialog( primaryState );
            if (zipFile == null)
                return;

            // 壓縮服務
            ZipService zipService = new ZipService(zipFile, imageBoxList, downloadView.getFileName() );
            ProgressStage progressStage = new ProgressStage("檔案壓縮中...", "進度", null, zipService.getMaxZipCount() );
            progressStage.getProgressBar().progressProperty().bind( zipService.progressProperty() );
            progressStage.show();
            zipService.start();
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println("錯誤！");
        }
    }
}