package jiaxiang.org.components;

import javafx.application.Platform;
import javafx.geometry.Side;
import javafx.scene.Node;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;

/** 警告提示欄 */
public class AlertInfo extends ContextMenu {
    /** 顯示錯誤訊息用的 */
    final private MenuItem errorMsg;
    
    /** 建構子 */
    public AlertInfo(){
        errorMsg = new MenuItem("只能輸入數字！");
        errorMsg.getStyleClass().add("alertMenuItem");
             
        getStyleClass().add("alertMenu");
        getItems().add( errorMsg );
    }

    /** 提示警告訊息
     *  @param node 要顯示的節點
     *  @param text 要提示的文字 */
    final public void alert( Node node, String text ){
        errorMsg.setText( text );
        new Thread(() -> {
            Platform.runLater( () -> { if( !isShowing() ) show( node, Side.TOP, 0, 65); } );
            try{Thread.sleep(1000);}catch(Exception e){}
            Platform.runLater( () -> hide());
        }).start();
    }

    /** 提示警告訊息
     *  @param node 要顯示的節點
     *  @param text 要提示的文字 
     *  @param dy Y 偏移量 */
    final public void alert( Node node, String text, double dy ){
        errorMsg.setText( text );
        new Thread(() -> {
            Platform.runLater( () -> { if( !isShowing() ) show( node, Side.TOP, 0, dy); } );
            try{Thread.sleep(1000);}catch(Exception e){}
            Platform.runLater( () -> hide());
        }).start();
    }

    /** 提示警告訊息
     *  @param node 要顯示的節點
     *  @param text 要提示的文字 
     *  @param dx X 偏移量 
     *  @param dy Y 偏移量*/
    final public void alert( Node node, String text, double dx, double dy ){
        errorMsg.setText( text );
        new Thread(() -> {
            Platform.runLater( () -> { if( !isShowing() ) show( node, Side.TOP, dx, dy); } );
            try{Thread.sleep(1000);}catch(Exception e){}
            Platform.runLater( () -> hide());
        }).start();
    }
}
