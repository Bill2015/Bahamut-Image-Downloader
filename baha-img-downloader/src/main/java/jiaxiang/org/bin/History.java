package jiaxiang.org.bin;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;

import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.control.ContentDisplay;
import javafx.scene.control.Label;
import javafx.scene.control.OverrunStyle;
import javafx.scene.control.Tooltip;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.util.Duration;
import jiaxiang.org.components.HoverIcon;
import jiaxiang.org.components.MyLabel;
import jiaxiang.org.utils.BahaImageGetterService;

public class History {
    /** <p>取得文章的 {@code UID}</p> 
     *  <p>其意思就是 [Bsn]-[snA] = [UID]</p>*/
    final private String UID;
    /** 文章標題 */
    private String title;
    /** 開始下載的樓層 */
    private int startFloor;
    /** 結束下載的樓層 */
    private int endFloor;
    /** 下載日期 */
    private Date date;
    /** 顯示元件 */
    private HistoryNode historyNode;
    /** 建構子 
     *  @param title 文章標題
     *  @param BSN 看板編號
     *  @param SNA 文章編號
     *  @param startFloor 開始下載的樓層
     *  @param endFloor 結束下載的樓層 */
    private History(String UID, String title, int startFloor, int endFloor){
        this.UID            = UID;
        this.title          = title;
        this.startFloor     = startFloor;
        this.endFloor       = endFloor;
    }
    /** 建構子 
     *  @param title 文章標題
     *  @param BSN 看板編號
     *  @param SNA 文章編號
     *  @param startFloor 開始下載的樓層
     *  @param endFloor 結束下載的樓層
     *  @param date 下載日期 */
    public History(String UID, String title, int startFloor, int endFloor, String date) throws ParseException{
        this(UID, title, startFloor, endFloor);
        this.date           = new SimpleDateFormat("yyyy-MM-dd").parse( date );
        this.historyNode    = new HistoryNode( this );
    }

    /** 建構子 
     *  @param title 文章標題
     *  @param BSN 看板編號
     *  @param SNA 文章編號
     *  @param startFloor 開始下載的樓層
     *  @param endFloor 結束下載的樓層
     *  @param date 下載日期 */
    public History(String UID, String title, int startFloor, int endFloor, Date date) throws ParseException{
        this(UID, title, startFloor, endFloor);
        this.date           = date;
        this.historyNode    = new HistoryNode( this );
    }

    /** 更新 {@link History} 所有資訊
     *  @param in 欲更新的 {@link History}*/
    public void update( History in ){
        this.title          = in.title;
        this.startFloor     = in.startFloor;
        this.endFloor       = in.endFloor;
        this.date           = in.date;
        historyNode.update();
    }

    /** 回傳出資訊 */
    @Override public String toString(){
        return  ( "Title :" + title +
                    ", UID : " + UID + 
                    ", Floor : " + startFloor + " - " + endFloor + 
                    ", Date : " + new SimpleDateFormat("yyyy/MM/dd").format(date) );
    }

    /** 取得資訊雜湊圖 用來給 Json 存檔
     *  @return 資訊 {@code [HashMap<String, Object>]}*/
    final public HashMap<String, Object> getInfoMap(){
        HashMap<String, Object> map = new HashMap<>();
        map.put("ArticleTitle"  , title );
        map.put("UID"           , UID );
        map.put("StartFloor"    , startFloor );
        map.put("EndFloor"      , endFloor );
        map.put("DownloadDate"  , new SimpleDateFormat("yyyy-MM-dd").format(date) );
        return map;
    }

    /** 取得標題 
     *  @return 標題 {@code [String]}*/
    final public String getTitle(){ return title; }

    /** 取得開始樓層
     *  @return 開始樓層 {@code [int]]}*/
    final public int getFloorStart(){ return startFloor; }

    /** 取得結束樓層
     *  @return 結束樓層 {@code [int]}*/
    final public int getFloorEnd(){ return endFloor; }

    /** 取得下載日期
     *  @return 下載日期 {@code [Date]}*/
    final public Date getDate(){ return date; }

    /** 取得文章唯一編號
     *  @return 唯一編號 {@code [String]}*/
    final public String getUID(){ return UID; }

    /** 取得 {@link HistoryNode} 元件節點
     *  @return 元件節點 {@code [HistoryNode]}*/
    final public HistoryNode getHistoryNode(){ return historyNode; }

    /** 取得文章網址
     *  @return 網址 {@code [String]}*/
    final public String getURL(){
        String temp[] = UID.split("-");
        return String.join("",  BahaImageGetterService.HEADER,
                                "?bsn=", temp[0],
                                "&snA=", temp[1] );
    }

    /** <p>{@link History} 節點化</p> 
     *  <p>繼承了 {@code BorderPane}*/
    public class HistoryNode extends BorderPane {
        /** 歷史紀錄 {@link History } 是 {@code 純數值物件} */
        final private History history;
        /** 標題 {@link MyLabel } */
        final private MyLabel titleLabel;
        /** 樓層 {@link MyLabel } */
        final private MyLabel floorLabel;
        /** 日期 {@link MyLabel } */
        final private MyLabel dateLabel;
        /** 刪除按鈕 {@link MyLabel } */
        final private Label deleteIcon;
        /** 初始化可視物件 */
        public HistoryNode( History history ){
            this.history = history;
    


            //標題
            titleLabel = new MyLabel( title , 11, Color.WHITESMOKE );
            titleLabel.setWrapText( false );
            titleLabel.setTextOverrun( OverrunStyle.ELLIPSIS );
            titleLabel.setTooltip( new Tooltip( title ){
                { 
                    setShowDelay( Duration.seconds(0) ); 
                    setFont( new Font("微軟正黑體", 14) ); 
                }
            } );

            //樓層
            floorLabel = new MyLabel( String.format( "下載樓層:\n\t%d-%d", startFloor, endFloor ) , 12 );
            floorLabel.setTextOverrun( OverrunStyle.ELLIPSIS );

            //日期
            dateLabel = new MyLabel( new SimpleDateFormat("下載日期:\n\tyyyy/MM/dd").format( date ) , 12, Color.AQUA );
            dateLabel.setTextOverrun( OverrunStyle.ELLIPSIS );

            //刪除按鈕
            deleteIcon = HoverIcon.getBuilder()
                                    .setFileUrl( "/textures/delete.png" )
                                    .setHintText( "刪除此紀錄" )
                                    .setSize( 12, 12 )
                                    .build();

            VBox vBox = new VBox( new BorderPane(titleLabel, null, deleteIcon, null, null), floorLabel, dateLabel );
            vBox.setSpacing( 3 );
            setCenter( vBox );

            titleLabel.prefWidthProperty().bind( vBox.widthProperty().subtract( 5 ) );

            
            getStyleClass().add( "HistoryBox-body" );

            //等到父節點出現再綁定邊長
            parentProperty().addListener( (obser, oldVal, newVal) -> {
                if( newVal != null && oldVal == null ){
                    prefWidthProperty().bind( ((FlowPane)getParent()).widthProperty().subtract( 11 ) );
                }
            } );
        }

        /** 更新 HistoryNode 裡的物件 */
        private void update(){
            titleLabel.setText( title );
            titleLabel.setTooltip( new Tooltip( title ){ 
                {
                    setShowDelay( Duration.seconds(0) ); 
                    setFont( new Font("微軟正黑體", 14) ); 
                }
            } );
            floorLabel.setText( String.format( "下載樓層:\n\t%d-%d", startFloor, endFloor ) );
            dateLabel.setText( new SimpleDateFormat("下載日期:\n\tyyyy/MM/dd").format( date ) );
        }
    
        /** 取得 {@link History} 紀錄
         *  @return 唯一編號 {@code [History]}*/
        final public History getHistory(){ return history; }

        /** 設定按鍵刪除事件
         * @param clickEvent 刪除事件 */
        final public void setDeleteEvent( EventHandler<MouseEvent> clickEvent ){
            deleteIcon.setOnMouseClicked( clickEvent );
        }
    }
    
}
