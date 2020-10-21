package jiaxiang.org.controller;

import jiaxiang.org.model.DataModel;
import javafx.application.Platform;
import javafx.collections.ListChangeListener;
import jiaxiang.org.view.ImagePaneView;
import jiaxiang.org.components.ImageBox;

public class ImageViewController {
    /** 主程式資料模型 */
    final private DataModel dataModel;
    /** 下載介面 */
    final private ImagePaneView imageView;
    /** 建構子 
     *  @param dataModel 主程式資料模型
     *  @param view 介面*/
    public ImageViewController( DataModel dataModel, ImagePaneView view ){
        this.dataModel      = dataModel;
        this.imageView      = view;

        ChangeEventInintialize();
    }

    /** 初始化串列改變事件 {@code ImageBoxList }*/
    private void ChangeEventInintialize(){
        dataModel.getImageList().addListener( (ListChangeListener.Change<? extends ImageBox> object) -> {
            while( object.next() ){
                if( object.wasAdded() ){
                    Platform.runLater( () -> imageView.getFlowPane().getChildren().addAll( object.getAddedSubList() ) );
                }
                else if( object.wasRemoved() ){
                    Platform.runLater(  () -> imageView.getFlowPane().getChildren().removeAll( object.getRemoved() )  );
                }
            }
        } );
    }
        
}
