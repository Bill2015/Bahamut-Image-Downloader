package jiaxiang.org.controller;

import javafx.animation.RotateTransition;
import javafx.beans.property.SimpleListProperty;
import javafx.scene.input.KeyCode;
import javafx.util.Duration;
import jiaxiang.org.view.SearchView;
import jiaxiang.org.components.ImageBox;
import jiaxiang.org.model.DataModel;
import jiaxiang.org.utils.BahaImageGetterService;

public class SearchController {
    /** 主程式資料模型 */
    final private DataModel dataModel;
    /** 下載介面 */
    final private SearchView searchView;
    /** 建構子 
     *  @param dataModel 主程式資料模型
     *  @param view 介面*/
    public SearchController( DataModel dataModel, SearchView view ){
        this.dataModel      = dataModel;
        this.searchView     = view;
        
        searchEventInitialize();
    }

    //============================================================================================
    //============================================================================================
    /** 搜尋功能初始化 
     *  @param imageBoxList 圖片串列*/
    final private void searchEventInitialize(){
        searchView.setOnSearchButtonAction( evt -> searchOperation() );
        searchView.setOnKeyPressed(evt -> {
            if (evt.getCode() == KeyCode.ENTER)
                searchOperation();
        });
    }
    /** 搜尋時的動作處理 
     *  @param imageBoxList 圖串*/
    final private void searchOperation(){
        SimpleListProperty<ImageBox> imageBoxList = dataModel.getImageList();
        // 監聽當搜尋鍵按下時
        try {
            int fStart  = searchView.getFloorStartValue();
            int fEnd    = searchView.getFloorEndValue();

            //假如勾著就代表要清除前次搜尋
            if( searchView.isClearPervious() ){
                imageBoxList.clear();
                System.out.println("List Cleared");
            }

            //網路搜尋服務開始
            System.out.println( "Task Started！" );
            BahaImageGetterService bahaGetterService = new BahaImageGetterService( searchView.getInputURL(), fStart, fEnd, imageBoxList );
            searchView.getSearchProgressBar().progressProperty().bind( bahaGetterService.progressProperty() );
            searchView.setProgressBarVisiable( true );
            searchView.getSearchButton().setDisable( true );     //先關閉搜尋按鈕一陣子
            bahaGetterService.start();

            //當完成後
            bahaGetterService.setOnSucceeded(evt -> {
                final RotateTransition delay = new RotateTransition();
                delay.setDelay( Duration.seconds( 2000 ) );
                delay.setOnFinished( delayEvt -> searchView.setProgressBarVisiable( false ) );
                delay.play();
                searchView.getSearchButton().setDisable( false );     //開啟搜尋按鈕
                System.out.println( "Task End！" );

                //設定目前紀錄
                dataModel.setNowHistory( bahaGetterService.getValue() );
                //取得文章標題
                System.out.println( "文章標號 UID：" + dataModel.getNowArticleTitle() );
                
            });

        } catch (ArrayIndexOutOfBoundsException e) {
            searchView.alert("輸入的網址不符合！");
            searchView.setProgressBarVisiable( false );
        } catch (Exception e) {
            searchView.alert("神奇的錯誤！");
            searchView.setProgressBarVisiable( false );
        }
    }

}
