package jiaxiang.org;

import java.util.ArrayList;

import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.FlowPane;
import jiaxiang.org.components.ImageBox;

public class ImagePane extends ScrollPane {
    /** 中心圖片展示區塊 */
    private final FlowPane flowPane;
    /** 卷軸的移動速度 */
    private final int SRCOLL_SPEED = 2;
    public ImagePane(){
        flowPane = new FlowPane();
        flowPane.setPadding( new Insets(5) );
        flowPane.setVgap(5);
        flowPane.setHgap(5);

        
        //垂直 Scroll Bar 監聽
        setVbarPolicy( ScrollPane.ScrollBarPolicy.ALWAYS);    // Vertical scroll bar
        setHbarPolicy( ScrollPane.ScrollBarPolicy.NEVER );
        setContent( flowPane );
        viewportBoundsProperty().addListener( (obser, oldBounds, newBounds) -> {
                flowPane.setPrefWidth( newBounds.getWidth() );
                flowPane.setPrefHeight( newBounds.getHeight() );
            }
        );
        //增加卷軸的移動速度
        flowPane.setOnScroll( (event) -> {
                double deltaY = event.getDeltaY() * SRCOLL_SPEED; // *6 to make the scrolling a bit faster
                double width = getContent().getBoundsInLocal().getWidth();
                double vvalue = getVvalue();
                setVvalue(vvalue + -deltaY/width); // deltaY/width to make the scrolling equally fast regardless of the actual width of the component
            }
      );

        flowPane.setStyle("-fx-background-color: rgb(70, 70, 70)");
        setStyle("-fx-background-color: rgb(70, 70, 70); -fx-border: none;");

    }

    /** 取得中間顯示的圖片父節點
     *  @return 圖片展示父節點 {@code [FlowPane]}*/
    public FlowPane getFlowPane(){
        return flowPane;
    }

    /** 取得中間顯示的所有圖片節點
     *  @return 所有顯示的圖片節點串列 {@code [ObservableList<Node>]}*/
    public ObservableList<Node> getImageBoxNodes(){
        return flowPane.getChildren();
    }

    /** 設定中間顯示的所有圖片節點
     *  @param imageBoxList 設定顯示的圖片節點串列*/
    public void setImageContent( ArrayList<ImageBox> imageBoxList ){
        flowPane.getChildren().clear();
        flowPane.getChildren().addAll( imageBoxList );
        flowPane.getChildren().removeIf( node -> !((ImageBox)node).isNeedSaved() );
    }
}
