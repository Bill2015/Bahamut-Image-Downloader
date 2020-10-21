package jiaxiang.org.utils;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.HashMap;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import jiaxiang.org.bin.History;
import javafx.beans.property.SimpleListProperty;
import javafx.collections.FXCollections;


public class HistoryListProperty extends SimpleListProperty<History> {

    final private String SOURSE_FILE_PATH = "workspace/setting-1.json";
    /** 檔案位置 */
    private File file;
    /** 建構子 */
    public HistoryListProperty(){
        super( FXCollections.observableArrayList( new ArrayList<History>() ) );
    }
    /** 讀取記錄檔 */
    final public void load()throws IOException, ParseException, NumberFormatException, java.text.ParseException{
        file = new File( SOURSE_FILE_PATH );
        JSONObject settingJson = (JSONObject)new JSONParser().parse( new FileReader( file.getAbsolutePath(), Charset.forName("UTF-8") ) );
    
        JSONArray historyArray = (JSONArray)settingJson.get("History");

        for( Object obj : historyArray ){
            JSONObject jObj = (JSONObject)obj;

            String uid = jObj.get("UID").toString();

            if( contains( uid ) == false ){
                add( 
                    new History( uid,                                                   //文章編號
                                jObj.get("ArticleTitle").toString(),                        //文章標題
                                Integer.parseInt( jObj.get("StartFloor").toString() ),      //開始樓層
                                Integer.parseInt( jObj.get("EndFloor").toString() ),        //結束樓層
                                jObj.get("DownloadDate").toString()                         //下載日期
                    )   
                );
            }

        }
    }

    /** 保存記錄檔 */
    final public void saved() throws IOException{
        FileWriter fileWriter = new FileWriter( file, Charset.forName("UTF-8") );

        HashMap<String, JSONArray> mainMap = new HashMap<>();

    
        JSONArray hisJsonArray = new JSONArray();
        forEach( ( historyNode ) -> hisJsonArray.add( historyNode.getInfoMap() ) );

        mainMap.put( "History" , (JSONArray)hisJsonArray );
        JSONObject mainJsonObj = new JSONObject( mainMap );

        fileWriter.write( mainJsonObj.toJSONString() );
        fileWriter.flush();
        fileWriter.close();

    }

    /** 利用 {@code UID} 判斷是否有在此陣列 
     *  @param uid 欲判斷的 UID
     *  @return 是否有包含 {@code true = 有} | {@code false = 沒有}*/
    final public boolean contains( String uid ){
        for( History hobj : get() ){
            if( hobj.getUID().equals( uid ) )return true;
        }
        return false;
    }

    /** {@inheritDoc} */
    @Override final public boolean contains( Object object ){
        if( object instanceof History ){
            for( History hobj : get() ){
                if( ((History)object).getUID().equals( hobj.getUID() ) )return true;
            }
            return false;
        }
        else return false;
    }

    /** 利用 UID 尋找物件 {@link History} 
     *  @param UID 編號
     *  @return 沒有找到就回傳 {@code null} */
    final public History getHistoryByUID( String UID ){
        for( History hobj : get() ){
            if( hobj.getUID().equals( UID ) )return hobj;
        }
        return null;
    }
}
