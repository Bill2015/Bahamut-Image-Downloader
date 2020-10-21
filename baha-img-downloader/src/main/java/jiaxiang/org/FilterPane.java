package jiaxiang.org;

import javafx.beans.property.SimpleListProperty;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Tooltip;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCodeCombination;
import javafx.scene.input.KeyCombination.ModifierValue;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import jiaxiang.org.components.ImageBox;
import jiaxiang.org.components.MyLabel;
import jiaxiang.org.components.NumberTextField;

public class FilterPane extends BorderPane {

    /** GP 的 {@link NumberTextField} */
    final private NumberTextField gpTextField;
    /** BP 的 {@link NumberTextField} */
    final private NumberTextField bpTextField;

    /** 篩選鈕按鍵 */
    final private Button filterBtn;

    /** 回復鈕按鍵 */
    final private Button resetBtn;

    public FilterPane(){
        
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

    //============================================================================================
    //============================================================================================
    /** 篩選功能初始化 
     *  @param imageBoxsList 圖片串列
     *  @param primaryState 主要視窗(用來綁定快捷鍵) */
    final public void filterEventInitialize( SimpleListProperty<ImageBox> imageBoxsList, Stage primaryState  ){
        filterBtn.setOnAction( evt -> filterOperation( imageBoxsList ) );
        setOnKeyPressed(evt -> {
            if (evt.getCode() == KeyCode.ENTER)
                filterOperation( imageBoxsList );
        });
        // 篩選按鈕 快捷鍵(Ctrl + F)
        KeyCodeCombination qucikFilter = new KeyCodeCombination(KeyCode.F, 
                                                                ModifierValue.UP, 
                                                                ModifierValue.DOWN,
                                                                ModifierValue.UP, 
                                                                ModifierValue.UP, 
                                                                ModifierValue.UP);
                primaryState.getScene().getAccelerators().put(qucikFilter, () -> filterOperation( imageBoxsList ) );
    
    
        // 重置按鈕 快捷鍵(Ctrl + R)
        resetBtn.setOnAction(evt -> resetOperation( imageBoxsList ) );
        KeyCodeCombination qucikReset = new KeyCodeCombination( KeyCode.R, 
                                                                ModifierValue.UP, 
                                                                ModifierValue.DOWN,
                                                                ModifierValue.UP, 
                                                                ModifierValue.UP, 
                                                                ModifierValue.UP);
                primaryState.getScene().getAccelerators().put(qucikReset, () -> resetOperation( imageBoxsList ) );
    }
    /** 篩選功能
     *  @param imageBoxList 圖片串列 */
    final private void filterOperation( SimpleListProperty<ImageBox> imageBoxList ){
        int gpValue = gpTextField.getCurrentValue();
        int bpValue = bpTextField.getCurrentValue();

        // 判斷文章的 GP(推) 與 BP(噓) 數
        for ( ImageBox imageBox : imageBoxList ) {
            //顯示 or 隱藏
            imageBox.setNeedSaved( (imageBox.getGPValue() >= gpValue && imageBox.getBPValue() <= bpValue) ? true : false );
        }
    }

    /** 篩選功能復原
     *  @param imageBoxList 圖片串列 */
    private void resetOperation( SimpleListProperty<ImageBox> imageBoxList ) {
        // 空的就不要執行了
        if ( imageBoxList.isEmpty() )
            return;
        imageBoxList.forEach(imageBox -> imageBox.setNeedSaved( true ) );
    }
}
