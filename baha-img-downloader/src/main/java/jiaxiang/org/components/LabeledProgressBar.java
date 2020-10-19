package jiaxiang.org.components;

import javafx.geometry.Pos;
import javafx.scene.control.ProgressBar;
import javafx.scene.layout.HBox;

public final class LabeledProgressBar extends HBox{
    /** 進度條 */
    private final ProgressBar progressBar;
    /** 顯示文字 */
    private final MyLabel label;
    /** 開始文字 */
    private final String startText;
    /** 結束文字 */
    private final String endText;
    /** <p>建構子</p> 
     *  字體顏色預設 {@code #b8b8b8}
     *  @param startText 開始時的文字 
     *  @param endText 結束時的文字 
     *  @param length 進度條長度 */
    public LabeledProgressBar(String startText, String endText, double length){
        this.startText  = startText;
        this.endText    = endText;
        progressBar = new ProgressBar();
        progressBar.setProgress( 0.0 );
        progressBar.setPrefWidth( length );
        label = new MyLabel(startText, 16);
        label.setAlignment( Pos.BOTTOM_LEFT );


        progressBar.progressProperty().addListener( (obser, oldVal, newVal) -> {
            final int val = (int)Math.round(newVal.doubleValue() * 100); 
            if( val == 0 )
                label.setText( this.startText );
            else if( val >= 100 )
                label.setText( this.endText );
            else
                label.setText( String.format(" %2d%% / 100%%", val) );
        } );
        setVisible( false );
        setSpacing( 20 );
        setAlignment( Pos.BOTTOM_LEFT );
        getChildren().addAll( label, progressBar );
    }
    /** 取得搜尋進度條
    *  @return 進度條 {@code [ProgressBar]}*/
    public ProgressBar getProgressBar(){ return progressBar; }
    /** 取得文字
    *  @return 文字區塊 {@code [MyLabel]}*/
    public MyLabel  getLabel(){ return label; }
}