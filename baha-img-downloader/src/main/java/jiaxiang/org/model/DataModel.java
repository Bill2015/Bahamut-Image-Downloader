package jiaxiang.org.model;

import java.util.ArrayList;

import javafx.beans.property.SimpleListProperty;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.SetChangeListener;
import jiaxiang.org.components.HistoryBox;
import jiaxiang.org.components.ImageBox;
import jiaxiang.org.utils.HistorySetProperty;

public class DataModel{
    /** 目前搜尋文章標題結果 */
    private String articleTitle = "無";
    /** 圖片串列 */
    private SimpleListProperty<ImageBox> imageBoxList;
    /** 歷史紀錄串列 */
    private HistorySetProperty historySet;

    public DataModel(){
        //圖片串列
        imageBoxList = new SimpleListProperty<ImageBox>( FXCollections.observableArrayList( new ArrayList<ImageBox>() ) );

        //下載歷史紀錄串列
        historySet = new HistorySetProperty();
    }

    /** 取得 {@code ImageBoxList} 圖片搜尋結果 
     *  @return 圖片串列 {@code [SimpleListProperty<ImageBox>]}*/
    public SimpleListProperty<ImageBox> getImageList(){
        return imageBoxList;
    }
    /** 取得 {@code HistorySet} 圖片搜尋結果 
     *  @return 下載歷史紀錄集合 {@code [HistorySetProperty]}*/
    public HistorySetProperty getHistorySet(){
        return historySet;
    }
    /** 取得 {@code 目前文章標題}  
     *  @return 文章標題 {@code [String]}*/
    public String getNowArticleTitle(){
        return articleTitle;
    }

    /** 新增 {@code ImageList} 改變時的事件 
     *  @param listener 事件監聽器*/
    public void addImageListChangeEvent( ListChangeListener<? super ImageBox> listener ){
        imageBoxList.addListener( listener );
    } 

    /** 新增 {@code HistorySet} 改變時的事件 
     *  @param listener 事件監聽器*/
    public void addHistorySetChangeEvent( SetChangeListener<? super HistoryBox> listener ){
        historySet.addListener( listener );
    } 
    /** 設定目前文章標題 
     *  @param articleTitle 愈設定的標題*/
    public void setNowArticleTitle( String articleTitle ){
        this.articleTitle   = articleTitle;
    }
}