package jiaxiang.org;


import javafx.animation.RotateTransition;
import javafx.beans.property.SimpleListProperty;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ContentDisplay;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.Tooltip;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.util.Duration;
import jiaxiang.org.components.AlertInfo;
import jiaxiang.org.components.ImageBox;
import jiaxiang.org.components.LabeledProgressBar;
import jiaxiang.org.components.MyLabel;
import jiaxiang.org.components.NumberTextField;
import jiaxiang.org.utils.BahaImageGetterService;


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
    final private NumberTextField floorTextFieldStart;
    /** 結束樓層 的 {@link NumberTextField} */
    final private NumberTextField floorTextFieldEnd;
    /** 進度條 */
    final private LabeledProgressBar searchProgressBar;

    private final CheckBox clearCheck;
    public SearchPane(){
        //標題列
        Label title = new Label( "請輸入要下載的圖串：" );
        title.setFont( new Font("微軟正黑體", 18) );
        title.setStyle("-fx-text-fill: #b8b8b8;");
        setTop( title );

        //搜尋按鈕
        final ImageView img = new ImageView("/textures/search-icon.png");
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
        
        //是否要清空前次搜尋結果的 CheckBox
        final MyLabel checkLabel = new MyLabel( "是否要清除前次結果：", 14 );
        checkLabel.setGraphic( (clearCheck = new CheckBox() ) );
        checkLabel.setContentDisplay( ContentDisplay.RIGHT );
        clearCheck.setFont( new Font("微軟正黑體", 12 ) );
        clearCheck.setTextFill( Color.web("#b8b8b8") );
        clearCheck.setSelected( true );

        //樓層的初始化
        final MyLabel floorLabel = new MyLabel( "樓層：", 18 );
        floorTextFieldStart = new NumberTextField(1, 99999, 1, 100);
        floorTextFieldStart.setFont( new Font(MyLabel.FONT_FAMLIY, 12 ) );
        floorTextFieldStart.setPadding( new Insets(3) );
        floorTextFieldEnd = new NumberTextField(0, 99999, 99999, 100);
        floorTextFieldEnd.setMaxDefaultText( "最後一樓" );
        floorTextFieldEnd.setFont( new Font(MyLabel.FONT_FAMLIY, 12 ) );
        floorTextFieldEnd.setPadding( new Insets(3) );
        HBox floorHBox = new HBox( checkLabel, floorLabel, floorTextFieldStart, new MyLabel("至", 14), floorTextFieldEnd, searchProgressBar);
        floorHBox.setAlignment( Pos.BOTTOM_LEFT );
        floorHBox.setSpacing( 15 );
        setBottom( floorHBox );

        //錯誤訊息提示文字初始化
        alertInfo = new AlertInfo();
    }   

    /** 取得是否要清除前次搜尋結果 
     *  @return {@code true = 要清除} | {@code false = 不清除}*/
    final public boolean isClearPervious(){ return clearCheck.isSelected(); }
    /** 提示警告訊息
     *  @param text 要提示的文字 */
    final public void alert( String text ){
        alertInfo.alert( urlTextField, text, 110, 20);
    }
    /** 取得搜尋的址網址
     *  @return 搜尋網址 {@code [String]}*/
    public String getInputURL(){ return urlTextField.getText(); }
    /** 取得目前 樓層 的下限值 
     *  @return 樓層下限值 {@code [int]} */
    final public int getFloorStartValue(){  return floorTextFieldStart.getCurrentValue(); }
    /** 取得目前 樓層 的上限值 
     *  @return 樓層上限值 {@code [int]} */
    final public int getFloorEndValue(){    return floorTextFieldEnd.getCurrentValue(); }

    //============================================================================================
    //============================================================================================
    /** 搜尋功能初始化 
     *  @param imageBoxList 圖片串列*/
    final public void searchEventInitialize( SimpleListProperty<ImageBox> imageBoxList ){
        searchBtn.setOnAction( evt -> searchOperation( imageBoxList ) );
        setOnKeyPressed(evt -> {
            if (evt.getCode() == KeyCode.ENTER)
                searchOperation( imageBoxList );
        });
    }
    /** 搜尋時的動作處理 
     *  @param imageBoxList 圖串*/
    final private void searchOperation( SimpleListProperty<ImageBox> imageBoxList ){
        // 監聽當搜尋鍵按下時
        try {
            int fStart  = floorTextFieldStart.getCurrentValue();
            int fEnd    = floorTextFieldEnd.getCurrentValue();

            //假如勾著就代表要清除前次搜尋
            if( clearCheck.isSelected() ){
                imageBoxList.clear();
                System.out.println("List Cleared");
            }

            System.out.println( "Task Started！" );
            BahaImageGetterService bahaGetterService = new BahaImageGetterService( getInputURL(), fStart, fEnd, imageBoxList );
            searchProgressBar.getProgressProperty().bind( bahaGetterService.progressProperty() );
            searchProgressBar.setVisible( true );
            searchBtn.setDisable( true );     //先關閉搜尋按鈕一陣子
            bahaGetterService.start();

            //當完成後
            bahaGetterService.setOnSucceeded(evt -> {
                final RotateTransition delay = new RotateTransition();
                delay.setDelay( Duration.seconds(2000) );
                delay.setOnFinished(delayEvt -> searchProgressBar.setVisible( false ));
                delay.play();
                searchBtn.setDisable( false );     //開啟搜尋按鈕
                System.out.println( "Task End！" );
            });

        } catch (ArrayIndexOutOfBoundsException e) {
            alert("輸入的網址不符合！");
            searchProgressBar.setVisible( false );
        } catch (Exception e) {
            alert("神奇的錯誤！");
            searchProgressBar.setVisible( false );
        }
    }
}
