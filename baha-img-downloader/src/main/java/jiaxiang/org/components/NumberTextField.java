package jiaxiang.org.components;


import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;

/** 只能輸入 數字 的 TextArea */
public final class NumberTextField extends TextField{
    /** 最大與最小值 */
    final private int MIN_VALUE, MAX_VALUE;
    /** 預設最小值顯示的字 */
    private String MIN_VALUE_DEFAULT_STRING;
    /** 預設最大值顯示的字 */
    private String MAX_VALUE_DEFAULT_STRING;
    /** 當前值 */
    private int curVal;
    final private AlertInfo alertInfo;
    /** 建構子
     *  @param minVal 最小值
     *  @param maxVal 最大值
     *  @param curVal 初始值 
     *  @param width 物件寬度 */
    public NumberTextField(int minVal, int maxVal, int curVal, int width){
        MIN_VALUE = minVal;
        MAX_VALUE = maxVal;
        MIN_VALUE_DEFAULT_STRING = Integer.toString( minVal ); 
        MAX_VALUE_DEFAULT_STRING = Integer.toString( maxVal ); 
        this.curVal = curVal;
        
        //提醒使用者輸入有誤
        alertInfo = new AlertInfo();

        //匿名函式宣告： 當焦點轉移時 檢查有無超過邊界
        focusedProperty().addListener( e -> {
            checkOutOfRange();
        } );

        //匿名函式宣告： 當按下 Enter 鍵時 檢查有無超過邊界
        addEventHandler(KeyEvent.KEY_RELEASED, e ->{
            if( e.getCode() == KeyCode.ENTER )
                checkOutOfRange();
        } );

        setPrefWidth( width );
        setMinWidth( width );
        setMaxWidth( width );

        //預設字串
        setText( Integer.toString( curVal ) );
    }


    @Override
    final public void replaceText(int start, int end, String text){
        if( !validate(text) ) return;
        super.replaceText(start, end, text);
    }

    /** 驗證是否輸入數字或數值過大 
     *  @return {@code true = 通過} | {@code false = 不通過}*/
    final private boolean validate(String text){
        if( !text.matches("[0-9]*")){
            alert( "只能輸入數字！" );return false;
        }
        if( getText().length() >= 6 && getSelectedText().isBlank() ){
            alert( "數值過大！" );return false;
        }
        return true;
    }
    /** 提示警告訊息 */
    final private void alert( String text ){
        alertInfo.alert( this, text, 40, 44 );
    }
    /** 檢查是否超出範圍 */
    final private void checkOutOfRange(){
        int value = curVal;

        //判斷預設字形是否有相同
        if( getText().equals( MIN_VALUE_DEFAULT_STRING ) )
            value = MIN_VALUE;
        else if(  getText().equals( MAX_VALUE_DEFAULT_STRING )  )
            value = MAX_VALUE;
        else 
            value = Integer.parseInt( getText() );

        if( value == MIN_VALUE || value == MAX_VALUE ) {
            setText( (value == MIN_VALUE) ? MIN_VALUE_DEFAULT_STRING : MAX_VALUE_DEFAULT_STRING );
            curVal = (value == MIN_VALUE) ? MIN_VALUE : MAX_VALUE;
            return;
        }
        else if( value < MIN_VALUE || value > MAX_VALUE ) {
            alert( "數值只能界在" + MIN_VALUE + " 到 " + MAX_VALUE + "之間！" );
            alertInfo.requestFocus();
            setText( (value < MIN_VALUE) ? MIN_VALUE_DEFAULT_STRING : MAX_VALUE_DEFAULT_STRING );
            curVal = (value < MIN_VALUE) ? MIN_VALUE : MAX_VALUE;
            return;
        }
        curVal = value;
    }
 
    /** 預設最小值顯示的字 */
    final public void setMinDefaultText(String text){
        MIN_VALUE_DEFAULT_STRING = text;
        checkOutOfRange();
    }
    /** 預設最大值顯示的字 */
    final public void setMaxDefaultText(String text){
        MAX_VALUE_DEFAULT_STRING = text;
        checkOutOfRange();
    }
    /** 設定目前數字
     *  @param val 數值*/
    final public void setCurrentValue( int val ){
        if( val <= MIN_VALUE || val >= MAX_VALUE ) {
            setText( (val <= MIN_VALUE) ? MIN_VALUE_DEFAULT_STRING : MAX_VALUE_DEFAULT_STRING );
            curVal = (val <= MIN_VALUE) ? MIN_VALUE : MAX_VALUE;
        }
        else{
            curVal = val;
            setText( Integer.toString( val ) );
        }
    }
    /** 取得當前數值 
     *  @return 數值 {@code [int]}*/
    final public int getCurrentValue(){ return curVal; }
}