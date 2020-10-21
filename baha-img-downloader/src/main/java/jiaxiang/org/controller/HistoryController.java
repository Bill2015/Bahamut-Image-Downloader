package jiaxiang.org.controller;

import org.json.simple.parser.ParseException;

import java.io.IOException;
import javafx.animation.ScaleTransition;
import javafx.collections.ListChangeListener;
import javafx.event.ActionEvent;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.util.Duration;
import jiaxiang.org.bin.History;
import jiaxiang.org.bin.History.HistoryNode;
import jiaxiang.org.components.HoverIcon;
import jiaxiang.org.view.DownloadView;
import jiaxiang.org.view.HistoryView;
import jiaxiang.org.view.SearchView;
import jiaxiang.org.model.DataModel;
import jiaxiang.org.utils.HistoryListProperty;

public class HistoryController {
    /** 主程式資料模型 */
    final private DataModel dataModel;
    /** 歷史紀錄介面 */
    final private HistoryView historyView;
    /** 下載介面 */
    final private DownloadView downloadView;
    /** 搜尋介面 */
    final private SearchView searchView;
    /** 建構子 
     *  @param dataModel 主程式資料模型
     *  @param view 介面*/
    public HistoryController( DataModel dataModel, HistoryView view, DownloadView downloadView, SearchView searchView ){
        this.dataModel      = dataModel;
        this.historyView    = view;
        this.downloadView   = downloadView;
        this.searchView     = searchView;

        ChangeEventInintialize();
        addNewHistoryInintialize();

        //先將一開始的 HistoryNode 給註冊事件
        for ( History history : dataModel.getHistoryList()) {
            addHistoryListner( history );
        }
    }

    /** 按下下載後，新增 {@link History} 物件 */
    private void addNewHistoryInintialize(){
        downloadView.getDownloadButton().addEventFilter( ActionEvent.ACTION , evt -> {
            //加入目前的紀錄至 Map 裡
            
            try{
                HistoryListProperty historyList = dataModel.getHistoryList();
                History nowHistory = dataModel.getNowHistory(), temp;

                //假如沒有包含這個文章的紀錄
                if( (temp = historyList.getHistoryByUID( nowHistory.getUID() ) ) == null ){
                    historyList.add( nowHistory );
                    //註冊按下時的事件
                    addHistoryListner( nowHistory );
                }
                //更新已存在的資訊
                else{
                    temp.update( nowHistory );
                }

                historyList.saved();
            }catch( IOException e ){
                System.out.println( "歷史紀錄儲存錯誤！" );
            }
        });
    }

    /** 初始化 {@code HistorySet} 更動事件 */
    private void ChangeEventInintialize(){
        HistoryListProperty historyList = dataModel.getHistoryList();

        try {
            historyList.addListener( ( ListChangeListener.Change<? extends History> object ) -> {
                while( object.next() ){
                    //先清空，在重新加入
                    historyView.getFlowPaneChildren().clear();
                    for (History history : historyList ) {
                        historyView.getFlowPaneChildren().add( history.getHistoryNode() );
                    }
                }
            } );
            historyList.load();

        } catch (ParseException | java.text.ParseException | IOException | NumberFormatException e ) {
            e.printStackTrace();
        }
    }

    /** 註冊按下區塊時，的動作 (以現有的做初始化) */
    private void addHistoryListner( History history ){
        HistoryNode historyNode = history.getHistoryNode();

        //註冊按下這個區塊時，會替換搜尋文字
        historyNode.addEventFilter( MouseEvent.MOUSE_CLICKED, mouseEvt -> {
            if( !(mouseEvt.getTarget() instanceof HoverIcon)  ){
                searchView.setInputURL( history.getURL() );
                searchView.setFloorRange( history.getFloorStart() ,history.getFloorEnd() );
            }
           
        } );
        //註冊按下刪除時，會刪掉字起
        historyNode.setDeleteEvent( mouseEvent -> {
            mouseEvent.consume();
            //滑鼠左鍵
            if( mouseEvent.getButton() == MouseButton.PRIMARY ){
                //框框縮小
                ScaleTransition scaleTransition = new ScaleTransition( Duration.seconds( 0.4 ), historyNode );
                scaleTransition.setToX( 0.0 );scaleTransition.setToY( 0.0 );
                scaleTransition.playFromStart();

                //移除自己 ( 物件 與 串列 都移除) 
                scaleTransition.setOnFinished( aniEvent -> {
                    historyView.getFlowPaneChildren().remove( historyNode );
                    dataModel.getHistoryList().remove( history );
                } );
            }
        } );
    }
    
}
