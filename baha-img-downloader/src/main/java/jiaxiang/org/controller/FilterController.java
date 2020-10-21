package jiaxiang.org.controller;

import javafx.beans.property.SimpleListProperty;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCodeCombination;
import javafx.scene.input.KeyCombination.ModifierValue;
import javafx.stage.Stage;
import jiaxiang.org.view.FilterView;
import jiaxiang.org.components.ImageBox;
import jiaxiang.org.model.DataModel;

public class FilterController {
    /** 主程式資料模型 */
    final private DataModel dataModel;
    /** 下載介面 */
    final private FilterView filterView;
    /** 主要的 Stage */
    final private Stage primaryState;
    /** 建構子 
     *  @param dataModel 主程式資料模型
     *  @param view 介面*/
    public FilterController( DataModel dataModel, FilterView view, Stage primaryState ){
        this.dataModel      = dataModel;
        this.filterView     = view;
        this.primaryState   = primaryState;

        filterEventInitialize();
    }
    

    //============================================================================================
    //============================================================================================
    /** 篩選功能初始化 
     *  @param primaryState 主要視窗(用來綁定快捷鍵) */
    final private void filterEventInitialize(){
        filterView.setOnFilterButtonAction( evt -> filterOperation() );
        filterView.setOnKeyPressed(evt -> {
            if (evt.getCode() == KeyCode.ENTER)
                filterOperation();
        });
        // 篩選按鈕 快捷鍵(Ctrl + F)
        KeyCodeCombination qucikFilter = new KeyCodeCombination(KeyCode.F, 
                                                                ModifierValue.UP, 
                                                                ModifierValue.DOWN,
                                                                ModifierValue.UP, 
                                                                ModifierValue.UP, 
                                                                ModifierValue.UP);
                primaryState.getScene().getAccelerators().put(qucikFilter, () -> filterOperation() );
    
    
        // 重置按鈕 快捷鍵(Ctrl + R)
        filterView.setOnResetButtonAction(evt -> resetOperation() );
        KeyCodeCombination qucikReset = new KeyCodeCombination( KeyCode.R, 
                                                                ModifierValue.UP, 
                                                                ModifierValue.DOWN,
                                                                ModifierValue.UP, 
                                                                ModifierValue.UP, 
                                                                ModifierValue.UP);
                primaryState.getScene().getAccelerators().put(qucikReset, () -> resetOperation() );
    }
    /** 篩選功能*/
    final private void filterOperation(){
        final SimpleListProperty<ImageBox> imageBoxList = dataModel.getImageList();

        //取得 GP 與 BP
        int gpValue = filterView.getGPValue();
        int bpValue = filterView.getBPValue();

        // 判斷文章的 GP(推) 與 BP(噓) 數
        for ( ImageBox imageBox : imageBoxList ) {
            //顯示 or 隱藏
            imageBox.setNeedSaved( (imageBox.getGPValue() >= gpValue && imageBox.getBPValue() <= bpValue) ? true : false );
        }
    }

    /** 篩選功能復原
     *  @param imageBoxList 圖片串列 */
    private void resetOperation() {
        final SimpleListProperty<ImageBox> imageBoxList = dataModel.getImageList();
        // 空的就不要執行了
        if ( imageBoxList.isEmpty() )
            return;
        imageBoxList.forEach(imageBox -> imageBox.setNeedSaved( true ) );
    }
}
