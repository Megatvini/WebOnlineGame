package test;

import Core.Dao.AccountDao;
import Interfaces.iAccount;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.commons.io.FilenameUtils;

import javax.imageio.ImageIO;
import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.awt.*;
import java.io.*;
import java.util.List;
import java.util.UUID;

@MultipartConfig
@WebServlet(urlPatterns = "/upload")
public class UploadServlet extends HttpServlet {


    public String directoryPathToPics = "pics/";
    public static final String NO_PROFILE_PIC_NAME = "no_profile" ;

    public String testPick = directoryPathToPics+NO_PROFILE_PIC_NAME;

    public static final int maxSize = 40000*4;
    public static final int MAX_NUM_PICS = 4000;

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {

            DiskFileItemFactory factory = new DiskFileItemFactory();


            factory.setSizeThreshold(maxSize * MAX_NUM_PICS);

            ServletFileUpload fileUpload = new ServletFileUpload(factory);


            fileUpload.setSizeMax(maxSize);

            List<FileItem> items = fileUpload.parseRequest(request);

            UUID idOne = UUID.randomUUID();


            String picFileName = (idOne + "");
            String filepath = directoryPathToPics + (picFileName);

            for (FileItem item : items) {
                if (item.isFormField()) {
                    // Process regular form field (input type="text|radio|checkbox|etc", select, etc).
                    String fieldName = item.getFieldName();
                    String fieldValue = item.getString();
                    // ... (do your job here)
                } else {
                    // Process form file field (input type="file").
                    String fieldName = item.getFieldName();
                    String fileName = FilenameUtils.getName(item.getName());
                    InputStream fileContent = item.getInputStream();

                    byte[] buffer = new byte[fileContent.available()];
                    fileContent.read(buffer);

                    File targetFile = new File(filepath);
                    OutputStream outStream = new FileOutputStream(targetFile);
                    outStream.write(buffer);


                    if (validatePic(filepath)) {

                        setPicPath(request, filepath);
                    } else {
                        handlePicUploadError(request, response);
                    }

                    response.setContentType("text/plain");
                    response.setCharacterEncoding("UTF-8");

                    // ... (do your job here)
                }
            }
        } catch (FileUploadException e) {

            throw new ServletException("Cannot parse multipart request.", e);

        }
    }

    private void handlePicUploadError(HttpServletRequest request, HttpServletResponse response) {

        try {
            response.sendRedirect("/index.jsp?error=1");
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private void setPicPath(HttpServletRequest request, String filepath) {
        String nickname = (String) request.getSession().getAttribute("nickname");
        AccountDao userControl = (AccountDao) getServletContext().getAttribute(AccountDao.class.getName());
        iAccount profile;
        try {
            profile = userControl.getUser(nickname);
            profile.setPicturePath(filepath);

        } catch (Exception e) {
            e.printStackTrace();
        }


    }

    private boolean validatePic( String filepath) {
        File imageFile = new File(filepath);

        Image image= null;
        try {
            image = ImageIO.read(imageFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (image == null) {
            deletePicFIle(imageFile.getName());
            return false;

        }
        return true;
    }

    private void deletePicFIle(String imageFilename) {
        String filepath = directoryPathToPics+imageFilename;
        File imageFile = new File(filepath);
       boolean deleted =  imageFile.delete();


    }


}