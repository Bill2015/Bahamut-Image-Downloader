package jiaxiang.org;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.TextField;
import javafx.scene.control.Tooltip;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.text.Font;
import jiaxiang.org.components.AlertInfo;
import jiaxiang.org.components.LabeledProgressBar;
import jiaxiang.org.components.MyLabel;
import jiaxiang.org.components.NumberTextField;


/** 搜尋區塊的面板 <p>
 *  繼承了 {@link BorderPane} */
public class SearchPane extends BorderPane {
    /** 網址輸入區 */
    final private TextField urlTextField;
    /** 搜尋按鈕 */
    final private Button searchBtn;
    /** 錯誤與警告提示欄 */
    final private AlertInfo alertInfo;
    /** 開始樓層 的 {@link NumberTextField} */
    private final NumberTextField floorTextFieldStart;
    /** 結束樓層 的 {@link NumberTextField} */
    private final NumberTextField floorTextFieldEnd;
    /** 進度條 */
    private final LabeledProgressBar searchProgressBar;
    public SearchPane(){
        //標題列
        Label title = new Label( "請輸入要下載的圖串：" );
        title.setFont( new Font("微軟正黑體", 18) );
        title.setStyle("-fx-text-fill: #b8b8b8;");
        setTop( title );

        //搜尋按鈕
        ImageView img = new ImageView("/textures/search-icon.png");
        img.setFitWidth( 28 );
        img.setFitHeight( 28 );
        searchBtn = new Button("搜尋", img );
        searchBtn.getStyleClass().add( "searchBtn" );
        searchBtn.setFont( new Font("微軟正黑體", 14) );
        searchBtn.setTooltip( new Tooltip("在做搜尋時，樓層越多也需要越久的時間，並且可能會造成巴哈伺服器負擔") );
        setRight( searchBtn );
        
        //網址輸入列
        urlTextField = new TextField("https://example.com");
        urlTextField.setFont( new Font("微軟正黑體", 18) );
        urlTextField.setTooltip( new Tooltip("在做搜尋時，樓層越多也需要越久的時間，並且可能會造成巴哈伺服器負擔") );
        setCenter( urlTextField );

        setPadding( new Insets(10) );

        //進度條
        searchProgressBar = new LabeledProgressBar("圖片抓取中......", "圖片抓取完畢！", 360);
        searchProgressBar.setVisible( false );
        

        //樓層的初始化
        MyLabel floorLabel = new MyLabel( "樓層：", 18 );
        floorTextFieldStart = new NumberTextField(1, 99999, 1, 100);
        floorTextFieldStart.setFont( new Font(MyLabel.FONT_FAMLIY, 12 ) );
        floorTextFieldStart.setPadding( new Insets(3) );
        floorTextFieldEnd = new NumberTextField(0, 99999, 99999, 100);
        floorTextFieldEnd.setMaxDefaultText( "最後一樓" );
        floorTextFieldEnd.setFont( new Font(MyLabel.FONT_FAMLIY, 12 ) );
        floorTextFieldEnd.setPadding( new Insets(3) );
        HBox floorHBox = new HBox(floorLabel, floorTextFieldStart, new MyLabel("至", 14), floorTextFieldEnd, searchProgressBar);
        floorHBox.setAlignment( Pos.BOTTOM_LEFT );
        floorHBox.setSpacing( 15 );
        setBottom( floorHBox );

        //錯誤訊息提示文字初始化
        alertInfo = new AlertInfo();
    }   

    /** 提示警告訊息
     *  @param text 要提示的文字 */
    final public void alert( String text ){
        alertInfo.alert( urlTextField, text, 110, 20);
    }
    /** 取得搜尋按鈕
     *  @return 搜尋按鈕 {@code [Button]}*/
    public Button getSearchButton(){ return searchBtn; };
     /** 取得搜尋進度條
     *  @return 進度條 {@code [ProgressBar]}*/
    public ProgressBar getProgressBar(){ return searchProgressBar.getProgressBar(); };
    /** 取得搜尋的址網址
     *  @return 搜尋網址 {@code [String]}*/
    public String getInputURL(){ return urlTextField.getText(); }
    /** 取得目前 樓層 的下限值 
     *  @return 樓層下限值 {@code [int]} */
    final public int getFloorStartValue(){  return floorTextFieldStart.getCurrentValue(); }
    /** 取得目前 樓層 的上限值 
     *  @return 樓層上限值 {@code [int]} */
    final public int getFloorEndValue(){    return floorTextFieldEnd.getCurrentValue(); }
     /** 設定進度條顯示或隱藏 
     *  @param flag {@code true = 顯示} | {@code false = 隱藏}  */
    final public void setProgressBarVisiable( boolean flag ){  searchProgressBar.setVisible( flag ); }

    
}
