package jiaxiang.org.view;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import jiaxiang.org.components.MyLabel;
import jiaxiang.org.components.NumberTextField;

public class FilterView extends BorderPane {

    /** GP 的 {@link NumberTextField} */
    final private NumberTextField gpTextField;
    /** BP 的 {@link NumberTextField} */
    final private NumberTextField bpTextField;
    /** 篩選鈕按鍵 */
    final private Button filterBtn;
    /** 回復鈕按鍵 */
    final private Button resetBtn;

    public FilterView(){
        
        MyLabel titleLabel = new MyLabel("篩選列表", 30);
        titleLabel.setPadding( new Insets(30, 0, 30, 0) );
        setTop( titleLabel );

        //推數的元件初始化
        MyLabel gpLabel = new MyLabel( "推數（GP）：", 18 );
        gpTextField = new NumberTextField(0, 1000, 0, 70);
        gpTextField.setMaxDefaultText( "爆" );
        gpTextField.setFont( new Font(MyLabel.FONT_FAMLIY, 12 ) );
        gpTextField.setPadding( new Insets(3) );
        gpTextField.setTooltip( new Tooltip("範圍為 0 ~ 1000+") );
        HBox gpHBox = new HBox(gpTextField, new MyLabel("以上顯示", 14));
        gpHBox.setAlignment( Pos.BOTTOM_LEFT );
        gpHBox.setSpacing( 5 );


        //噓數的元件初始化
        MyLabel bpLabel = new MyLabel( "噓數（BP）：", 18 );
        bpTextField = new NumberTextField(0, 1000, 1000, 70);
        bpTextField.setFont( new Font(MyLabel.FONT_FAMLIY, 12 ) );
        bpTextField.setPadding( new Insets(3) );
        bpTextField.setMinDefaultText( "-" );
        bpTextField.setMaxDefaultText( "X" );
        bpTextField.setTooltip( new Tooltip("範圍為 0 ~ 1000+") );
        HBox bpHBox = new HBox(bpTextField, new MyLabel("以上不顯示", 14));
        bpHBox.setAlignment( Pos.BOTTOM_LEFT );
        bpHBox.setSpacing( 5 );


        //篩選按鈕初始化
        filterBtn = new Button( "篩選" );
        filterBtn.setFont( new Font(MyLabel.FONT_FAMLIY, 16 ) );
        filterBtn.getStyleClass().add( "filterBtn" );
        filterBtn.setTooltip( new Tooltip("在做篩選時，不會造曾巴哈站內的伺服器負擔，請安心使用") );

        resetBtn = new Button( "重置" );
        resetBtn.setFont( new Font(MyLabel.FONT_FAMLIY, 16 ) );
        resetBtn.getStyleClass().add( "resetBtn" );
        resetBtn.setTooltip( new Tooltip("將移除的圖片全部重新復原") );

        
        HBox btnHBox = new HBox( filterBtn, resetBtn );
        btnHBox.setSpacing( 20 );
        AnchorPane anchorPane = new AnchorPane( btnHBox );
        AnchorPane.setBottomAnchor(btnHBox, 20.0);
        setBottom( anchorPane );
        


        setPadding( new Insets(0, 20, 0, 20) );
        setCenter( new VBox( titleLabel, 
                                gpLabel, 
                                gpHBox, 
                                new MyLabel(" ", 18),
                                bpLabel, 
                                bpHBox, 
                                new MyLabel(" ", 18)
        ) );

        getStyleClass().add("filter-Pane");
    }
    /** 取得目前 GP 的下限值 
     *  @return GP值 {@code [int]} */
    final public int getGPValue(){          return gpTextField.getCurrentValue(); }
    /** 取得目前 BP 的上限值 
     *  @return BP值 {@code [int]} */
    final public int getBPValue(){          return bpTextField.getCurrentValue(); }
    /** 取得篩選鈕
     *  @return 篩選鈕 {@code [Button]} */
    final public Button getFilterButton(){  return filterBtn; }
    /** 取得重置鈕
     *  @return 重置鈕 {@code [Button]} */
    final public Button getResetButton(){  return resetBtn; }

    /** 設定按下 {@code Filter Button} 的事件
     *  @param eventHandler 欲直行的動作*/
    final public void setOnFilterButtonAction( EventHandler<ActionEvent> eventHandler ){ 
        filterBtn.setOnAction( eventHandler );
    };
    /** 設定按下 {@code Reset Button} 的事件
     *  @param eventHandler 欲直行的動作*/
    final public void setOnResetButtonAction( EventHandler<ActionEvent> eventHandler ){ 
        resetBtn.setOnAction( eventHandler );
    };
}
