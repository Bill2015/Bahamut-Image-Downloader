package jiaxiang.org.utils;

import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import java.awt.image.BufferedImage;

import javax.imageio.ImageIO;

import javafx.beans.property.SimpleListProperty;
import javafx.collections.transformation.FilteredList;
import javafx.concurrent.Service;
import javafx.concurrent.Task;
import jiaxiang.org.components.ImageBox;


final public class ZipService extends Service<Void> {
    /** 欲存檔的圖片串列 */
    final private FilteredList<ImageBox> imageBoxList;
    /** 存檔用的壓縮檔 */
    final private File zipFile;
    /** 每張圖片的名稱 */
    final private String imageName;
    /** 建構子 
     *  @param zipFile 存檔用的壓縮檔
     *  @param imageBoxList 欲存檔的圖片串列
     *  @param imageName 每張圖片的名稱*/
    public ZipService( File zipFile, SimpleListProperty<ImageBox> imageBoxList, String imageName ){
        this.zipFile        = zipFile;
        this.imageName      = imageName;
        this.imageBoxList   = imageBoxList.filtered( obj -> obj.isNeedSaved() );
    }
    @Override
    protected Task<Void> createTask() {
        ZipUtiltyTask zipTask = new ZipUtiltyTask();
        return zipTask;
    }
    /** 取得所需壓縮圖檔數量 
     *  @return 數量 {@code [int]}*/
    public int getMaxZipCount(){ return imageBoxList.size(); }
    /** <p>圖片壓縮的功能類別</p> 
     *  <p>繼承了 {@link Task} 已讓 {@code Service} 使用*/
    private class ZipUtiltyTask extends Task<Void>{

        public ZipUtiltyTask(){}
        protected Void call() throws FileNotFoundException, IOException, InterruptedException{
            int count = 1;

            // 更新進度
            updateProgress( 1, imageBoxList.size() );
            // 檔案寫入緩衝串列
            BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(zipFile));

            // 壓縮檔案寫入串列
            ZipOutputStream out = new ZipOutputStream(bos);
            // 寫入圖片
            for (ImageBox imageBox : imageBoxList) {

                // 取得圖片檔名
                String fileExt = imageBox.getExtension();
                // 將每一張圖片放至 Zip 檔
                out.putNextEntry( new ZipEntry( String.join(".", String.format("%s-%d", imageName, count++) , fileExt)));
                // 一般圖片 .PNG .JPG
                if (!fileExt.equalsIgnoreCase("GIF")) {
                    // 將圖片轉成 BufferedImage
                    BufferedImage img = ImageIO.read(imageBox.getURL());
                    // 圖片寫入壓縮檔
                    ImageIO.write(img, fileExt, out);
                }
                // 假如是 GIF 就另外處理
                else {
                    // 以 URL 串流讀入，原因是轉成 Buffered Image GIF 圖就不會動了
                    InputStream is = imageBox.getURL().openStream();
                    ByteArrayOutputStream bao = new ByteArrayOutputStream();
                    // 緩衝區大小
                    byte[] buff = new byte[1024];
                    // 將 GIF 轉成 Byte[]
                    for (int bytesRead = 0; (bytesRead = is.read(buff)) != -1; bao.write(buff, 0, bytesRead));
                    byte[] data = bao.toByteArray();
                    out.write(data);
                    is.close();
                    bao.close();
                }
                // 關閉這次的物件，以換下一個檔案
                out.closeEntry();
                // 更新進度
                updateProgress( count, imageBoxList.size() );
            }
            out.close(); 
            return null;
        }
    }
}
