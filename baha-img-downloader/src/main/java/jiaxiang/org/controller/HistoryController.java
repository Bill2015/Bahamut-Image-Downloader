package jiaxiang.org.controller;

import org.json.simple.parser.ParseException;

import java.io.IOException;

import javafx.application.Platform;
import javafx.collections.SetChangeListener;
import jiaxiang.org.view.HistoryView;
import jiaxiang.org.components.HistoryBox;
import jiaxiang.org.model.DataModel;
import jiaxiang.org.utils.HistorySetProperty;

public class HistoryController {
    /** 主程式資料模型 */
    final private DataModel dataModel;
    /** 下載介面 */
    final private HistoryView historyView;
    /** 建構子 
     *  @param dataModel 主程式資料模型
     *  @param view 介面*/
    public HistoryController( DataModel dataModel, HistoryView view ){
        this.dataModel      = dataModel;
        this.historyView   = view;

        ChangeEventInintialize();
    }

    private void ChangeEventInintialize(){
        HistorySetProperty historySet = dataModel.getHistorySet();
        
        try {
            historySet.addListener( ( SetChangeListener.Change<? extends HistoryBox> object ) -> {
                if( object.wasAdded() ){
                    Platform.runLater( () -> historyView.getFlowPane().getChildren().add( object.getElementAdded() ) );
                }
                else if( object.wasRemoved() ){
                    Platform.runLater(  () -> historyView.getFlowPane().getChildren().remove( object.getElementRemoved() )  );
                }
            } );
            historySet.load();
        } catch (ParseException | java.text.ParseException | IOException | NumberFormatException e ) {
            e.printStackTrace();
        }
    }
}
