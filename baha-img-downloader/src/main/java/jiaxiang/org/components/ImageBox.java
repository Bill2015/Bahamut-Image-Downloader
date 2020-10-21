package jiaxiang.org.components;

import java.net.MalformedURLException;
import java.net.URL;

import javafx.animation.ScaleTransition;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.Tooltip;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.util.Duration;
import jiaxiang.org.view.ImagePaneView;

public class ImageBox extends BorderPane{
    /** 發文者 {@code ID} */
    final private String authorId;
    /** 發文者 {@code 暱稱} */
    final private String authorName;
    /** 此樓的 {@code 推} 數 */
    final private int gpCount;
    /** 此樓的 {@code 噓} 數 */
    final private int c8763Count;
    /** 此樓的 {@code 樓層} */
    final private int floor;
    /** 此圖片的 {@code 網址} */
    final private String url;
    /** 顯示圖片的 {@code ImageView} */
    private ImageView imageView;
    /** 設定是否要儲存該圖片 */
    private boolean needSaved = true;
    /** 是否錯誤(通常是圖片讀取時發生錯誤) */
    private boolean isFailed = false;
    protected ImageBox( String authorId, String authorName, int gbCount, int c8763Count, int floor, String url ){
        this.authorId   = authorId;
        this.authorName = authorName;
        this.gpCount    = gbCount;
        this.c8763Count = c8763Count;
        this.floor      = floor;
        this.url        = url;
        componentInitialize();
    }

    /** ImageBox 所有的元件初始化 */
    final private void componentInitialize(){

        //設定圖片顯示
        Image img = new Image( url, true );
        //假如圖片 Loading 失敗就隱藏
        img.errorProperty().addListener( (obser, oldVal, newVal) -> {
            this.isFailed = true; 
            setNeedSaved( false );
        }  );
        imageView = new ImageView( img );

        //依照比例縮小，因為是 Lazy Loading 所以要監聽完成時候
        img.progressProperty().addListener((obser, oldVal, newVal) -> {
            if( newVal.doubleValue() >= 1.0 ){
                double iw = img.getWidth(), ih = img.getHeight();
                if( iw >= 256 || ih >= 256 ){
                    double maxRate = 256.0 / Math.max( iw , ih );
                    imageView.setFitWidth( iw * maxRate );
                    imageView.setFitHeight( ih * maxRate );
                }
                else{
                    imageView.setFitWidth( iw );
                    imageView.setFitHeight( ih );
                }
            }
        });
       
       
        setCenter( imageView );

        //設定貼文者
        BorderPane topBoderPane = new BorderPane();
        VBox authorVBox = new VBox( 
            new MyLabel( authorName , 16), 
            new MyLabel( authorId, 12, Color.AQUA ) 
        );
        authorVBox.setSpacing( 0 );
        authorVBox.setPadding( new Insets(10, 0, 10, 0) );
        authorVBox.setAlignment( Pos.BASELINE_LEFT );
        topBoderPane.setLeft( authorVBox );
        
        //設定樓層元件
        VBox floorVBox = new VBox( 
            createDeleteIcon(),
            new MyLabel( String.format("%4d 樓", floor )  , 16)
        );
        floorVBox.setSpacing( 0 );
        floorVBox.setPadding( new Insets(10, 0, 10, 0) );
        floorVBox.setAlignment( Pos.BASELINE_RIGHT );
        topBoderPane.setRight( floorVBox );

        //上方元件
        setTop( topBoderPane );

        //----------------------------------------------------------------------------------
        //設定 推 與 噓
        BorderPane bottomBoderPane = new BorderPane();
        HBox scoreHBox = new HBox( 
            new MyLabel( "推: " + (gpCount == 9999 ? "爆" : gpCount) , 14, Color.GOLD ), 
            new MyLabel( "噓: " + (c8763Count == 0 ? "-" : (c8763Count == -9999) ? "X" : c8763Count), 14, Color.LIGHTGREY ) 
        );
        scoreHBox.setSpacing( 10 );
        scoreHBox.setAlignment( Pos.BASELINE_LEFT );
        bottomBoderPane.setLeft( scoreHBox );


        setMaxWidth( 280 );
        setMinWidth( 280 );
        setPrefWidth( 280 );
        setBottom( bottomBoderPane );
        setPadding( new Insets(5, 10, 5, 10) );
        getStyleClass().add( "imageBox-body" );
        managedProperty().bind( visibleProperty() );
    }

    //==================================================================================
    /** 新增刪除按鈕 
     *  @return 回傳刪除按鈕 {@code [Label]}*/
    final private Label createDeleteIcon(){
        Label deleteIcon = new Label("", new ImageView("/textures/delete.png") );
        deleteIcon.addEventHandler( MouseEvent.MOUSE_RELEASED , evt -> {
            if( evt.getButton() == MouseButton.PRIMARY ){
                ScaleTransition scaleTransition = new ScaleTransition( Duration.seconds( 0.4 ), this );
                scaleTransition.setToX( 0.0 );scaleTransition.setToY( 0.0 );
                scaleTransition.playFromStart();

                scaleTransition.setOnFinished( aniEvt -> {
                    ((ImagePaneView) this.getParent().getParent().getParent().getParent()).getFlowPane().getChildren().remove( this );
                    this.setScaleX( 1.0 );this.setScaleY( 1.0 );
                } );
                
                needSaved = false;
            }
        } );

        //放大與縮小效果
        ScaleTransition scaleIn = new ScaleTransition( Duration.seconds(0.1), deleteIcon );
        scaleIn.setToX( 1.6 );scaleIn.setToY( 1.6 );
        ScaleTransition scaleOut = new ScaleTransition( Duration.seconds(0.1), deleteIcon );
        scaleOut.setToX( 1 );scaleOut.setToY( 1 );

        //刪除 Icon 的 Hover 效果
        deleteIcon.setOnMouseEntered( evt -> scaleIn.play() );
        deleteIcon.setOnMouseExited( evt -> scaleOut.play() );
        deleteIcon.setTooltip( new Tooltip("刪除此圖片，儲存時將不會存到") );

        return deleteIcon;
    }

    /** 設定是否需要儲存圖片 
     *  @param flag 設定是否需要儲存圖片 {@code true = 要} | {@code false = 不要}*/
    final public void setNeedSaved( boolean flag ){ 
        //需要儲存就把它加入至父節點
        if( flag ){
            if( isFailed == false ){
                setVisible( true );
                setPrefWidth( 280 );    //邊長復原
            }
        }
        else {
            setVisible( false );
            setPrefWidth( 0 );      //邊長設為 0 已讓其他物件上推 
        }
        this.needSaved = flag; 
    }
    
    //==================================================================================
    /** 是否需要儲存圖片 
     *  @return 是否需要儲存圖片檔案*/
    final public boolean isNeedSaved(){ return needSaved; }

    /** 取得目前 GP 的下限值 
     *  @return GP值 {@code [int]} */
    final public int getGPValue(){          return gpCount; }
    /** 取得目前 BP 的上限值 
     *  @return BP值 {@code [int]} */
    final public int getBPValue(){          return c8763Count; }
    
    /** 回傳 URL 網址
     *  @return 網址 {@code [URL]}
     *  @exception MalformedURLException 格式不符合 */
    final public URL getURL() throws MalformedURLException{ 
        return new URL(url); 
    }

     /** 回傳 圖片 副檔名
     *  @return 副檔名 {@code [String]} */
    final public String getExtension(){
        return url.substring( url.lastIndexOf(".") + 1, url.length()  );
    }

    /** 建立 {@link ImageBox} 的 Builder
     *  @return 回傳 {@code [ImageBoxBuilder]}*/
    public static final ImageBoxBuilder getBuilder(){ return new ImageBoxBuilder(); }
    
    /** {@link ImageBox} 建構者 */
    public static final class ImageBoxBuilder{
        /** 發文者 {@code ID} */
        private String authorId;
        /** 發文者 {@code 暱稱} */
        private String authorName;
        /** 此樓的 {@code 推} 數 */
        private int gpCount;
        /** 此樓的 {@code 噓} 數 */
        private int bpCount;
        /** 此樓的 {@code 樓層} */
        private int floor;
        /** 此圖片的 {@code 網址} */
        private String url;
        public ImageBoxBuilder(){}
        /** 設定貼文者ID 
         * @param id 貼文者ID */
        final public ImageBoxBuilder setAuthorId(String id){ this.authorId = id; return this; }
        /** 設定貼文者暱稱 
         * @param name 貼文者暱稱 */
        final public ImageBoxBuilder setAuthorName(String name){  this.authorName = name; return this; }
        /** 設定此文章的推(GP)數 
         * @param gpCount 推(GP)數 */
        final public ImageBoxBuilder setScoreGP( int gpCount ){ this.gpCount = gpCount;  return this; }
        /** 設定此文章的噓(BP)數 
         * @param bpCount 噓(BP)數 */
        final public ImageBoxBuilder setScoreBP( int bpCount ){ this.bpCount = bpCount;  return this; }
        /** 設定此文章所在的樓層
         * @param floor 樓層 */
        final public ImageBoxBuilder setFloor( int floor ){ this.floor = floor; return this; }
        /** 設定此圖片的網址
         * @param url 網址 */
        final public ImageBoxBuilder setImageUrl( String url ){ this.url = url; return this; }
        /** 建構 {@link ImageBox} 物件
         * @return 傳出已建構完的ImageBox {@code [ImageBox]} */
        final public ImageBox build(){
            return new ImageBox(authorId, authorName, gpCount, bpCount, floor, url);
        }
    }

    
}
