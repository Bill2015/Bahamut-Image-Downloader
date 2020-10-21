package jiaxiang.org.view;


import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.FlowPane;


public class ImagePaneView extends ScrollPane {
    /** 中心圖片展示區塊 */
    final private FlowPane flowPane;
    /** 卷軸的移動速度 */
    final private int SRCOLL_SPEED = 2;

    public ImagePaneView() {
        flowPane = new FlowPane();
        flowPane.setPadding( new Insets(5) );
        flowPane.setVgap( 5 );
        flowPane.setHgap( 5 );

        // 垂直 Scroll Bar 監聽
        setVbarPolicy( ScrollPane.ScrollBarPolicy.ALWAYS ); // Vertical scroll bar
        setHbarPolicy( ScrollPane.ScrollBarPolicy.NEVER );
        setContent( flowPane );
        viewportBoundsProperty().addListener( (obser, oldBounds, newBounds) -> {
            flowPane.setPrefWidth( newBounds.getWidth() );
            flowPane.setPrefHeight( newBounds.getHeight() );
        });
        // 增加卷軸的移動速度
        flowPane.setOnScroll( (event) -> {
            double deltaY   = event.getDeltaY() * SRCOLL_SPEED; // *2 讓卷軸能快一些
            double width    = getContent().getBoundsInLocal().getWidth();
            double vvalue   = getVvalue();
            setVvalue(vvalue + -deltaY / width);
        });

        flowPane.setStyle("-fx-background-color: rgb(70, 70, 70)");
        setStyle("-fx-background-color: rgb(70, 70, 70); -fx-border: none;");

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
