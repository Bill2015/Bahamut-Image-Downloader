package jiaxiang.org.components;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.stage.Window;

public class InfoStage extends Stage {
    /**
     * 資訊欄
     * @param title 標題
     * @param text 內文
     * @param parentStage 父 {@link Stage}，當這個視窗還沒關掉時會鎖住父視窗
     */
    public InfoStage(String title,String text, Window parentStage){


        //永遠在最上層
        setAlwaysOnTop(true);
        initModality(Modality.WINDOW_MODAL);

        
        

        //建立底層的 Stack Pane
        BorderPane borderPane = new BorderPane();
        borderPane.setStyle("-fx-background-color: rgba(100, 100, 100, 0.75); -fx-background-radius: 20px");
        borderPane.setPadding( new Insets(10) );
        borderPane.getStylesheets().add(getClass().getResource("/styles/button.css").toExternalForm());
        borderPane.getStylesheets().add(getClass().getResource("/styles/menuBar.css").toExternalForm());

        

        //設定標題的文字
        MyLabel titleLabel = new MyLabel( title, 20 );
        titleLabel.setTextFill( Color.WHITE );
        titleLabel.setStyle("-fx-background-color: transparent;");
        borderPane.setTop( titleLabel );
        BorderPane.setMargin( titleLabel, new Insets(10) );

        //中間顯示文字
        MyLabel textLabel = new MyLabel( text, 16 );
        textLabel.setStyle( "-fx-background-color: transparent;" );
        textLabel.setWrapText(true);
        borderPane.setCenter( textLabel );
        BorderPane.setMargin( textLabel, new Insets(10) );
        BorderPane.setAlignment( textLabel, Pos.TOP_LEFT );
        

        //設定確認按鈕
        Button button= new Button("關閉");
        button.setFont( new Font( "微軟正黑體", 20) );
        button.getStyleClass().add("closeBtn");
        button.setTextFill( Color.WHITE );
        borderPane.setBottom( button );
        BorderPane.setMargin( button, new Insets(10) );



        button.addEventHandler(MouseEvent.MOUSE_CLICKED, e->{
            close();
        });
        borderPane.setPadding( new Insets(20));


        setWidth(  480 );
        setHeight(  360 );
        initStyle( StageStyle.TRANSPARENT );
        setScene( new Scene( borderPane ){ { setFill( Color.rgb( 100, 100, 100, 0.01) ); }} );
        
        //鎖住父視窗
        initOwner(parentStage);
    }
}