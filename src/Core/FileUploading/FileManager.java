package Core.FileUploading;

import javax.imageio.ImageIO;
import java.awt.*;
import java.io.File;
import java.io.IOException;

/**
 * Created by rezo on 6/28/15.
 */
public  class FileManager {
    public static String directoryPathToPics =  "pics/";

    public FileManager(String fileRoot){
        directoryPathToPics = fileRoot ;

    }



    public  File createFile(String pathname) {
        return new File(pathname);
    }

    public  void deletePicFIle(String imageFilename) {

        File imageFile = createFile(createFilePath(imageFilename));
        boolean deleted =  imageFile.delete();


    }

    public  String createFilePath(String fileName){
        return directoryPathToPics+fileName;
    }

    public  boolean validatePic( String fileName) {
        File imageFile = createFile(createFilePath(fileName));

        Image image= null;
        try {
            image = ImageIO.read(imageFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (image == null) {
            deletePicFIle(createFilePath(fileName));
            return false;

        }
        return true;
    }

}
