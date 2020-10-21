package jiaxiang.org.components;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;

import javafx.scene.control.OverrunStyle;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;

public class HistoryBox extends BorderPane {
    final private String URL;
    final private String BSN;
    final private String SNA;
    private String title;
    private int startFloor;
    private int endFloor;
    private Date date;

    /** 建構子 
     *  @param title 文章標題
     *  @param BSN 看板編號
     *  @param SNA 文章編號
     *  @param startFloor 開始下載的樓層
     *  @param endFloor 結束下載的樓層
     *  @param date 下載日期 */
    public HistoryBox(String title, String BSN, String SNA, int startFloor, int endFloor, String date) throws ParseException{
        this.URL        = String.join("bsn=", BSN, "&snA=", SNA);
        this.BSN        = BSN;
        this.SNA        = SNA;
        this.title      = title;
        this.startFloor = startFloor;
        this.endFloor   = endFloor;
        this.date       = new SimpleDateFormat("yyyy-mm-dd").parse( date );

        componentInitialize();
    }

    /** 初始化可視物件 */
    private void componentInitialize(){
        MyLabel titleLabel = new MyLabel( title, 12 );
        titleLabel.setTextFill( Color.WHITESMOKE );
        titleLabel.setTextOverrun( OverrunStyle.ELLIPSIS );

        VBox vBox = new VBox( 
            titleLabel,
            new MyLabel( String.format( "下載樓層: %d-%d", startFloor, endFloor ), 10 ),
            new MyLabel( new SimpleDateFormat("下載日期: yyyy/mm/dd").format( date ) , 10, Color.AQUA )
        );
        vBox.setSpacing( 3 );
        setCenter( vBox );

        getStyleClass().add( "HistoryBox-body" );
    }

    /** 回傳出資訊 */
    @Override public String toString(){
        return  ( "Title :" + title +
                    ", URL : " + URL + 
                    ", Floor : " + startFloor + " - " + endFloor + 
                    ", Date : " + new SimpleDateFormat("yyyy/mm/dd").format(date) );
    }

    /** 取得資訊雜湊圖 用來給 Json 存檔
     *  @return 資訊 {@code [HashMap<String, Object>]}*/
    public HashMap<String, Object> getInfoMap(){
        HashMap<String, Object> map = new HashMap<>();
        map.put("ArticleTitle"  , title );
        map.put("BSN"           , BSN );
        map.put("SNA"           , SNA );
        map.put("StartFloor"    , startFloor );
        map.put("EndFloor"      , endFloor );
        map.put("DownloadDate"  , new SimpleDateFormat("yyyy-mm-dd").format(date) );
        return map;
    }
}
