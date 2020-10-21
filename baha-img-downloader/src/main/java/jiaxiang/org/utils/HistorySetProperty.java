package jiaxiang.org.utils;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import javafx.beans.property.SimpleSetProperty;
import javafx.collections.FXCollections;
import jiaxiang.org.components.HistoryBox;


public class HistorySetProperty extends SimpleSetProperty<HistoryBox> {

    final private String SOURSE_FILE_PATH = "workspace/setting-1.json";
    private File file;
    /** 建構子 */
    public HistorySetProperty(){
        super( FXCollections.observableSet( new HashSet<HistoryBox>() ) );
    }
    /** 讀取記錄檔 */
    final public void load()throws IOException, ParseException, NumberFormatException, java.text.ParseException{
        file = new File( SOURSE_FILE_PATH );
        JSONObject settingJson = (JSONObject)new JSONParser().parse( new FileReader( file.getAbsolutePath(), Charset.forName("UTF-8") ) );
    
        JSONArray historyArray = (JSONArray)settingJson.get("History");

        for( Object obj : historyArray ){
            JSONObject jObj = (JSONObject)obj;

            String bsn = jObj.get("BSN").toString(), sna = jObj.get("SNA").toString();

            add( 
                new HistoryBox( jObj.get("ArticleTitle").toString(), 
                                    bsn, 
                                    sna,
                                    Integer.parseInt( jObj.get("StartFloor").toString() ), 
                                    Integer.parseInt( jObj.get("EndFloor").toString() ),
                                    jObj.get("DownloadDate").toString()
                )   
            );
        }
    }

    final public void saved() throws IOException{
        FileWriter fileWriter = new FileWriter( file, Charset.forName("UTF-8") );

        HashMap<String, JSONArray> mainMap = new HashMap<>();

    
        JSONArray hisJsonArray = new JSONArray();
        forEach( ( historyBox ) -> hisJsonArray.add( historyBox.getInfoMap() ) );

        mainMap.put( "History" , (JSONArray)hisJsonArray );
        JSONObject mainJsonObj = new JSONObject( mainMap );

        fileWriter.write( mainJsonObj.toJSONString() );
        fileWriter.flush();
        fileWriter.close();
    }

}
