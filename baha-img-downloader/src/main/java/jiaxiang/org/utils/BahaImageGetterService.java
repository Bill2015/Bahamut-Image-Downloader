package jiaxiang.org.utils;


import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.HashMap;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import javafx.beans.property.SimpleListProperty;
import javafx.concurrent.Service;
import javafx.concurrent.Task;
import jiaxiang.org.components.ImageBox;
import jiaxiang.org.components.ImageBox.ImageBoxBuilder;

//c-post__header__title 

public final class BahaImageGetterService extends Service<Void>{
    /** 此串開始的樓層 */
    private int floorStart = 1;
    /** 此串結束的樓層 */
    private int floorEnd = 99999;
    private final String inUrl;
    /** 爬蟲完的圖片串列存檔 */
    private final SimpleListProperty<ImageBox> resultList;
    /** 建構子，執行  
     *  @param inUrl 要剖析的串
     *  @param floorStart 開始的樓層
     *  @param floorEnd 結束的樓層 */
    public BahaImageGetterService( String inUrl, int floorStart, int floorEnd, SimpleListProperty<ImageBox> resultList ){
        this.floorStart = floorStart;
        this.floorEnd   = floorEnd;
        this.inUrl      = inUrl;
        this.resultList = resultList;
    }
    @Override protected Task<Void> createTask() {
        BahaImageGetterTask task = null;
        try{
            task = new BahaImageGetterTask(inUrl, floorStart, floorEnd, resultList);
        }catch(Exception e){}
        return task;
    }

    /** <p>抓取巴哈圖片的類別</p>
     *  <p>繼承了 {@link Task} 已讓 {@link Service } 使用*/
    private static class BahaImageGetterTask extends Task<Void>{
        
        /** 網址後方的參數(Get) */
        private String postVal;
        /** 將 HTML 檔寫成檔案，以方便查看(目前程式不會用到) */
        private HtmlFileWriter htmlFileWriter;
        /** 此串的最大頁數 */
        private int maxPage = -1;
        /** 此串開始的樓層 */
        private int floorStart = 1;
        /** 此串結束的樓層 */
        private int floorEnd = 99999;
        /** 爬蟲完的圖片串列存檔 */
        final private SimpleListProperty<ImageBox> resultList;
        /** 主要網址標頭 */
        final static private String HEADER = "https://forum.gamer.com.tw/C.php";
        /** 每一頁的樓層數(根據網頁版的巴哈，每一頁都有20樓) */
        final static private int MAX_PAGE_COUNT = 20;
        /** 餅乾 */
        final static private HashMap<String, String> COOKIES = new HashMap<>();
        /** 輸入的網址 */
        private String inUrl;

        //靜態變數初始化
        {
            COOKIES.put("_ga", "GA1.4.1059733215.1591118241");      
            COOKIES.put("_gid", "GA1.4.1850309596.1593739183");      
            COOKIES.put("ckBahaGif", "e1fad4bcddf343af8933981b53170c63");     
        }
        
        /** 建構子，執行  
         *  @param inUrl 要剖析的串
         *  @param floorStart 開始的樓層
         *  @param floorEnd 結束的樓層 */
        public BahaImageGetterTask( String inUrl, int floorStart, int floorEnd, SimpleListProperty<ImageBox> resultList ) throws Exception{
            this.floorStart = floorStart;
            this.floorEnd   = floorEnd;
            this.inUrl      = inUrl;
            this.resultList = resultList;

             
        }
        @Override protected Void call() throws Exception{
            
            //用來將 Document 寫出，以方便剖析
            //htmlFileWriter = new HtmlFileWriter();

            Document document = Jsoup.connect( inUrl ).cookies( COOKIES ).get();

            //取得此串的最大頁數
            maxPage = getMaxPage( document.getElementsByClass("BH-pagebtnA").first() );
            maxPage = Math.min( (floorEnd / MAX_PAGE_COUNT) + 1, maxPage);

            // 設定文章標題為此行程的訊息
            updateTitle( getArticleTitle( document ) );

            //利用樓層計算開始的頁數
            final int startPage = floorStart / MAX_PAGE_COUNT;

            //只會發生在第一頁
            postVal = inUrl.split("bsn=")[1];
            postVal = "bsn=" + postVal;
            updateProgress(0, maxPage - startPage);
        
            for( int i = startPage + 1 ; i <= maxPage ; i++ ){
                progress( String.join("", HEADER, "?page=", Integer.toString(i), "&", postVal)  );
                updateProgress(i - startPage, maxPage - startPage);

                System.out.println( "頁數：" + (i - startPage) + " ,最大頁數：" +  (maxPage - startPage) );
            }

            //htmlFileWriter.close();

            return null;
        }

        /** 抓取網站 Html Document 
         *  @param url 網址 */
        private void progress( String url ) throws IOException{
            //取得網址
            Document doc = Jsoup.connect( url ).cookies( COOKIES ).get();

            //取得作者元素
            Elements authors = getAuthorsElements( doc );
            for (Element author : authors) {

                if( !author.getElementsByClass("c-disable").isEmpty() )continue;

                //假如樓層範圍超了，就跳出
                int nowFloor = Integer.parseInt( author.getElementsByClass("floor").attr("data-floor") );
                if( nowFloor < floorStart || nowFloor > floorEnd )break;

                //建立 ImageBox Builder
                ImageBoxBuilder imageBoxBuilder = ImageBox.getBuilder()
                                                            .setAuthorId(   author.getElementsByClass("userid").text() )          
                                                            .setAuthorName( author.getElementsByClass("username").text() )
                                                            .setScoreGP(    getScore( author.getElementsByClass("postgp") ) )
                                                            .setScoreBP(    getScore( author.getElementsByClass("postbp") ) )
                                                            .setFloor(      nowFloor );
                
                //取得文章裡的圖片，留言並沒有包含                                            
                Elements imageElement = author.getElementsByClass("FM-P2")
                                                    .first().getElementsByClass("photoswipe-image");
                //將此樓的文章圖片全部取出
                for( Element img : imageElement ){
                    //取得圖片網址
                    String imgUrl = img.attr("href");
                    //將 ImageBoxBuilder 轉成 ImageBox
                    resultList.add( imageBoxBuilder.setImageUrl( imgUrl ).build() );
                    //等待，以防止太頻繁與伺服器請求
                    try { Thread.sleep( 100 );} catch (Exception e) {}
                }
            }
        }

        /** 取得這個頁面的所有發布者文章 
         *  @return 發文者節點s {@code [Elements]}*/
        private Elements getAuthorsElements( Document doc ) throws IOException{
            Elements authors = doc.body().getElementsByTag("section");
            authors.removeIf( elem -> !elem.hasAttr("id") );
            return authors;
        }

        /** 取得這個頁面的標題
         *  @return 文章標題 {@code [String]}*/
        private String getArticleTitle( Document doc ){
            return doc.body().getElementsByClass("title").first().text();
        }

        /** 取得此元素的推數與噓數 
         *  @param element 推與噓的 HTML 元素
         *  @return 推數與噓數 {@code [int]}*/
        private int getScore( Elements elements ){
            //取得 BP or GB 數字元素
            String strnum = elements.first().child(0).text();
            //字數轉換
            try{ 
                switch (strnum) {
                    case "爆":  return  9999;
                    case "X":   return -9999;
                    case "-":   return  0;
                    default:    return Integer.parseInt( strnum ); 
                } 
            }
            catch( NumberFormatException e ){ 
                return 0; 
            }
        }

        /** 取得最大頁數 
         *  @param element 頁數的 HTML 元素
         *  @return 最大頁數 {@code [int]}*/
        private int getMaxPage( Element element ){
            return Integer.parseInt( element.children().last().text() ); 
        }

        /** <p>利用此類別可以將接收回來的 HTML Document 寫成檔案，</p> 
         *  <p>以方便自己做剖析，獲取需要的元素，簡單來說，就只是做</p>
         *  <p>測試的功能。</p>
         *  <p>繼承了 {@link OutputStreamWriter}</p>*/  
        private final class HtmlFileWriter extends OutputStreamWriter {
            /** 建構子 */
            public HtmlFileWriter() throws IOException{
                super( new FileOutputStream( new File("htmlDoc.html") ), "UTF-8" );
            }
            /** 要寫入檔案的寫入元素集 
             *  @param elements 欲寫入的元素集*/
            public void write( Elements elements ) throws IOException{
                for (Element elem : elements) 
                    super.write( elem.outerHtml() + "\n\n\n" );
                flush();
            }
            /** 要寫入檔案的元素
             *  @param element 欲寫入的元素*/
            public void write( Element element ) throws IOException{
                super.write( element.outerHtml() + "\n\n\n" );
            }
            /** 要寫入檔案 HTML Document
             *  @param doc 欲 HTML Document*/
            public void write( Document doc ) throws IOException{
                super.write( doc.body().outerHtml() + "\n\n" );
                flush();
            }
        }

    }



}