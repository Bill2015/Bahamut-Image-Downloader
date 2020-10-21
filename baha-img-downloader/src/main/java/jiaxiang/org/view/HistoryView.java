package jiaxiang.org.view;

import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.FlowPane;
import jiaxiang.org.components.MyLabel;

public class HistoryView extends BorderPane{
    /** 中心歷史紀錄展示區塊 */
    final private FlowPane flowPane;
    /** 卷軸的移動速度 */
    final private int SRCOLL_SPEED = 2;
    public HistoryView(){

        MyLabel title = new MyLabel("下載紀錄", 24 );
        BorderPane.setAlignment( title , Pos.CENTER );
        BorderPane.setMargin( title , new Insets( 20, 0, 20, 0 ) );
        setTop( title );

        flowPane = new FlowPane();
        flowPane.setPadding( new Insets(5) );
        flowPane.setVgap( 5 );
        flowPane.setHgap( 5 );

        final ScrollPane scrollPane = new ScrollPane( flowPane );
        // 垂直 Scroll Bar 監聽
        scrollPane.setVbarPolicy( ScrollPane.ScrollBarPolicy.AS_NEEDED ); // Vertical scroll bar
        scrollPane.setHbarPolicy( ScrollPane.ScrollBarPolicy.NEVER );
        scrollPane.viewportBoundsProperty().addListener( (obser, oldBounds, newBounds) -> {
            flowPane.setPrefWidth( newBounds.getWidth() );
            flowPane.setPrefHeight( newBounds.getHeight() - 1 );
        });
        // 增加卷軸的移動速度
        flowPane.setOnScroll( (event) -> {
            double deltaY   = event.getDeltaY() * SRCOLL_SPEED; // *2 讓卷軸能快一些
            double width    = scrollPane.getContent().getBoundsInLocal().getWidth();
            double vvalue   = scrollPane.getVvalue();
            scrollPane.setVvalue(vvalue + -deltaY / width);
        });

        flowPane.setStyle("-fx-background-color: rgb(70, 70, 70)");
        scrollPane.setStyle("-fx-background-color: rgb(70, 70, 70); -fx-border: none;");

        setPrefWidth( 160 );
        setMinWidth( 160 );
        setCenter( scrollPane );
        getStyleClass().add("history-Pane");
        setPadding( new Insets(0, 20, 20, 20) );

    }
    /**取得中間顯示的圖片父節點
     *  @return 圖片展示父節點 {@code [FlowPane]}*/
    public FlowPane getFlowPane() {
        return flowPane;
    }

    /**取得中間顯示的所有圖片節點
     * @return 所有顯示的圖片節點串列 {@code [ObservableList<Node>]} */
    public ObservableList<Node> getImageBoxNodes() {
        return flowPane.getChildren();
    }

}
