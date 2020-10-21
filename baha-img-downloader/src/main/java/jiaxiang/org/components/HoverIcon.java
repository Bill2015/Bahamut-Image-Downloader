package jiaxiang.org.components;

import javafx.animation.ScaleTransition;
import javafx.event.EventHandler;
import javafx.scene.control.Label;
import javafx.scene.control.Tooltip;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.util.Duration;

public class HoverIcon extends Label{

    /**建構子
     * @param text 顯示的文字
     * @param fileUrl 圖片路徑
     * @param hintText  提示文字
     * @param clickEvent 按鍵事件 
     * @param width 寬 
     * @param height 高 */
    protected HoverIcon( String text, String fileUrl, String hintText , EventHandler<MouseEvent> clickEvent, double width, double height ){
        ImageView view = new ImageView( fileUrl );

        //設定高度
        if( width > 0.0 )
            view.setFitWidth( width );
        if( height > 0.0 )
            view.setFitHeight( height );

        setText( text );
        setGraphic( view );
        setOnMouseClicked( clickEvent );

        //放大與縮小效果
        ScaleTransition scaleIn = new ScaleTransition( Duration.seconds(0.1), this );
        scaleIn.setToX( 1.6 );scaleIn.setToY( 1.6 );
        ScaleTransition scaleOut = new ScaleTransition( Duration.seconds(0.1), this );
        scaleOut.setToX( 1 );scaleOut.setToY( 1 );

        //刪除 Icon 的 Hover 效果
        setOnMouseEntered( evt -> scaleIn.play() );
        setOnMouseExited( evt -> scaleOut.play() );
        setTooltip( new Tooltip( hintText ) );
    }

    /** 建立 {@link HoverIcon} 的 Builder
     *  @return 回傳 {@code [HoverIconBuilder]}*/
    final static public HoverIconBuilder getBuilder(){
        return new HoverIconBuilder();
    }

    /** {@link HoverIcon} 建構者 */
    final static public class HoverIconBuilder{
        /** 顯示的文字 */
        private String text;
        /** 圖片路徑*/
        private String fileUrl;
        /** 提示文字*/
        private String hintText;
        /** 按鍵事件 */
        private EventHandler<MouseEvent> clickEvent;
        /** 圖片寬 */
        private double width;
        /** 圖片高 */
        private double height;
        /** 建構子  */
        public HoverIconBuilder(){
            this.text = "";
            this.fileUrl = "";
            this.hintText = "";
            this.width = -1.0;
            this.height = -1.0;
        }
        /** 設定顯示的文字 
         * @param text 顯示的文字 */
        final public HoverIconBuilder setText(String text){ this.text = text; return this; }
        /** 設定圖片路徑
         * @param fileUrl 圖片路徑 */
        final public HoverIconBuilder setFileUrl(String fileUrl){  this.fileUrl = fileUrl; return this; }
        /** 設定提示文字
         * @param hintText 提示文字 */
        final public HoverIconBuilder setHintText( String hintText ){ this.hintText = hintText;  return this; }
        /** 設定按鍵事件
         * @param clickEvent 事件 */
        final public HoverIconBuilder setClickEvent( EventHandler<MouseEvent> clickEvent ){ this.clickEvent = clickEvent;  return this; }
        /** 設定此圖片的邊常
         * @param width 寬 
         * @param height 高 */
        final public HoverIconBuilder setSize( double width, double height ){ this.width = width; this.height = height; return this; }

        /** 建構 {@link HoverIcon} 物件
         * @return 傳出已建構完的HoverIcon {@code [HoverIcon]} */
        final public HoverIcon build(){
            return new HoverIcon(text, fileUrl, hintText, clickEvent, width, height);
        }
    }
        
}
