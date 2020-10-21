package jiaxiang.org.components;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.ProgressBar;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.stage.Window;
import javafx.util.Duration;

public class ProgressStage extends Stage {
    /** 文字顯示區塊 */
    final private MyLabel label;
    /** 進度條 */
    final private ProgressBar progressBar;
    /** 載入中的圖片 */
    final private String LOADING_IMAGE = "/textures/loading.gif";
    /** 載入完成後的圖片 */
    final private String COMPLETE_IMAGE = "/textures/download-complete.png";
    /** 資訊欄
     * @param title 標題
     * @param text 內文
     * @param parentStage 父 {@link Stage}，當這個視窗還沒關掉時會鎖住父視窗*/
    public ProgressStage( String title, String text, Window parentStage, int maxCount ){
        //建立底層的 Stack Pane
        BorderPane borderPane = new BorderPane();
        borderPane.setStyle("-fx-background-color: rgba(100, 100, 100, 0.75); -fx-background-radius: 20px");
        borderPane.setPadding( new Insets(10) );
        borderPane.getStylesheets().add(getClass().getResource("/styles/button.css").toExternalForm());
        borderPane.getStylesheets().add(getClass().getResource("/styles/menuBar.css").toExternalForm());
        borderPane.getStylesheets().add(getClass().getResource("/styles/progressBar.css").toExternalForm());

        //Loading Gif
        ImageView loadingView = new ImageView( LOADING_IMAGE );
        loadingView.setFitHeight( 196 );
        loadingView.setFitWidth( 256 );
        borderPane.setCenter( loadingView );

        //進度條與敘述
        label = new MyLabel( "進度：", 18 );
        progressBar = new ProgressBar();
        progressBar.prefWidthProperty().bind( borderPane.widthProperty().subtract( 20 ) );
        VBox vBox = new VBox( label, progressBar );
        vBox.setAlignment( Pos.CENTER );
        borderPane.setBottom( vBox );

        //更新文字資訊
        progressBar.progressProperty().addListener( (obser, oldVal, newVal) -> {
            final int val = (int)Math.ceil( newVal.doubleValue() * maxCount ); 
            if( val == 0 )
                label.setText( "初始化......" );
            else if( newVal.doubleValue() >= 1.0 ){
                label.setText( "成功儲存！" );
                loadingView.setImage( new Image( COMPLETE_IMAGE ) );
                KeyFrame keyFrame = new KeyFrame( Duration.seconds( 2 ), onFinishEvt -> {
                    this.close();
                } );
                new Timeline( keyFrame ).play();
            }
            else
                label.setText( String.format("進度： %2d 張圖片 / 共 %2d 張圖片", val, maxCount) );
        } );


       
        
        setWidth( 360 );
        setHeight( 280 );
        initStyle( StageStyle.TRANSPARENT );
        setScene( new Scene( borderPane ){ { setFill( Color.rgb( 100, 100, 100, 0.01) ); }} );
        
        //鎖住父視窗
        setTitle( title );
        initOwner( parentStage );
        initModality( Modality.WINDOW_MODAL );
    }
    /** 取得進度條 
     *  @return 進度條 {@code [ProgressBar]} */
    public ProgressBar getProgressBar(){ return progressBar; }
}
