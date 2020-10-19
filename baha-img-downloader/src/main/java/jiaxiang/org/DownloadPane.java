package jiaxiang.org;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.text.Font;
import jiaxiang.org.components.AlertInfo;
import jiaxiang.org.components.MyLabel;


public class DownloadPane extends HBox{
    /** 檔案下載按鈕 */
    private Button downloadBtn;
    /** 要下載的圖片集名稱 */
    private TextField fileNamField;
    /** 錯誤與警告提示欄 */
    final private AlertInfo alertInfo;

    public DownloadPane(){
        downloadBtn = new Button( "下載成 .Zip 檔" );
        downloadBtn.setFont( new Font("微軟正黑體", 14) );
        downloadBtn.getStyleClass().add("downloadBtn");

        fileNamField = new TextField( "圖片" );
        fileNamField.setFont( new Font("微軟正黑體", 14) );

        MyLabel title = new MyLabel("圖片檔名： ", 18);

        setAlignment( Pos.BASELINE_LEFT );

        setSpacing( 15 );
        setPadding( new Insets(0, 0, 20, 0) );
        getChildren().addAll(  downloadBtn, title, fileNamField );
        //錯誤訊息提示文字初始化
        alertInfo = new AlertInfo();
    }
    /** 提示警告訊息
     *  @param text 要提示的文字 */
    final public void alert( String text ){
        alertInfo.alert( downloadBtn, text);
    }
    /** 取得下載按鈕 
     *  @return 下載按鈕 {@code [Button]} */
    public Button getDownloadButton(){ return downloadBtn; }
    /** 取得圖片集檔案名稱
     *  @return 檔案名稱 {@code [String]} */
    public String getFileName(){ return fileNamField.getText(); };
}
