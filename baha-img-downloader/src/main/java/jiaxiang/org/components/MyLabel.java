package jiaxiang.org.components;

import javafx.scene.control.Label;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;

/** <p>自訂義 {@link Label}</p> 
 *  字體預設 {@code 微軟正黑體}*/
final public class MyLabel extends Label {
    public final static String FONT_FAMLIY = "微軟正黑體";
    /** <p>自訂義 {@link Label} 建構子</p> 
     *  字體顏色預設 {@code #b8b8b8}
     *  @param text 顯示的文字 
     *  @param fontSize 文字大小 */
    public MyLabel( String text, int fontSize ){
        super(text);
        setFont( new Font( FONT_FAMLIY, fontSize) );
        setTextFill( Color.web("#b8b8b8") );
    }
    /** <p>自訂義 {@link Label} 建構子</p> 
     *  字體顏色預設 {@code 黑色}
     *  @param text 顯示的文字 
     *  @param fontSize 文字大小 
     *  @param style Label 的 Style */
    public MyLabel( String text, int fontSize, String style ){
        super(text);
        setFont( new Font( FONT_FAMLIY, fontSize) );
        setStyle( style );
    }
    /** <p>自訂義 {@link Label} 建構子</p> 
     *  字體顏色預設 {@code 黑色}
     *  @param text 顯示的文字 
     *  @param fontSize 文字大小  
     *  @param color 字體的顏色 */
    public MyLabel( String text, int fontSize, Color color ){
        this(text, fontSize);
        setTextFill( color );
    }
    /** <p>自訂義 {@link Label} 建構子</p> 
     *  字體顏色預設 {@code 黑色}
     *  @param text 顯示的文字 
     *  @param fontSize 文字大小 
     *  @param style Label 的 Style 
     *  @param color 字體的顏色 */
    public MyLabel( String text, int fontSize, String style, Color color ){
        this(text, fontSize, color);
        setStyle( style );
    }
}
