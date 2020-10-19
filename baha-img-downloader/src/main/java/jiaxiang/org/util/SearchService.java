package jiaxiang.org.util;

import javafx.concurrent.Service;
import javafx.concurrent.Task;

public class SearchService extends Service<BahaHmtlGetter>{
    /** 此串開始的樓層 */
    private int floorStart = 1;
    /** 此串結束的樓層 */
    private int floorEnd = 99999;
    private String inUrl;
    /** 建構子，執行  
     *  @param inUrl 要剖析的串
     *  @param floorStart 開始的樓層
     *  @param floorEnd 結束的樓層 */
    public SearchService( String inUrl, int floorStart, int floorEnd ){
        this.floorStart = floorStart;
        this.floorEnd   = floorEnd;
        this.inUrl      = inUrl;
    }
    @Override protected Task<BahaHmtlGetter> createTask(){
        BahaHmtlGetter task = null;
        try{
            task = new BahaHmtlGetter(inUrl, floorStart, floorEnd);
        }catch(Exception e){}
        return task;
    }
}