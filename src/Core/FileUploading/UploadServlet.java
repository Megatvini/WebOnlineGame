package Core.FileUploading;

import Core.Dao.AccountDao;
import Interfaces.iAccount;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.commons.io.FilenameUtils;


import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.util.List;
import java.util.UUID;

@MultipartConfig
@WebServlet(urlPatterns = "/uploadPic")
public class UploadServlet extends HttpServlet {

    public static final int maxSize = 100000*4;
    public static final int MAX_NUM_PICS = 4000;

    FileManager fileManager  = null;
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        gandleUpload(request, response);

    }

    private void gandleUpload(HttpServletRequest request, HttpServletResponse response) throws IOException {

        String picFileName = "" ;
        try {

            fileManager = (FileManager) request.getServletContext().getAttribute(FileManager.class.getName());


            ServletFileUpload fileUpload = setUpUploadUtilities();

            List<FileItem> items = fileUpload.parseRequest(request);

            UUID idOne = UUID.randomUUID() ;



            picFileName = (idOne + "");


            for (FileItem item : items) {

                if (item.isFormField()) {

                    String fieldName = item.getFieldName();
                    String fieldValue = item.getString();
                    //TODO handle them

                } else {
                    // Process form file field (input type="file").
                    InputStream fileContent = item.getInputStream();

                    picFileName+= "."+FilenameUtils.getExtension(item.getName());

                    byte[] buffer = new byte[fileContent.available()];
                    fileContent.read(buffer);

                    File targetFile = fileManager.createFile(fileManager.createFilePath(picFileName));


                    OutputStream outStream = new FileOutputStream(targetFile);
                    outStream.write(buffer);


                    if (fileManager.validatePic(picFileName)) {
                        sendSuccess(request, response, picFileName);
                    } else {
                        handlePicUploadError(request, response);
                    }


                }
            }
        } catch (FileUploadException e) {
            if (!picFileName.equals(""))
                fileManager.deletePicFIle(picFileName);
            response.getOutputStream().println("Large file");


        }
    }

    private ServletFileUpload setUpUploadUtilities() {
        DiskFileItemFactory factory = new DiskFileItemFactory();

        factory.setSizeThreshold(maxSize * MAX_NUM_PICS);

        ServletFileUpload fileUpload = new ServletFileUpload(factory);


        fileUpload.setSizeMax(maxSize);
        return fileUpload;
    }

    private void sendSuccess(HttpServletRequest request, HttpServletResponse response, String picFileName) throws IOException {
        response.setContentType("text/plain");
        response.setCharacterEncoding("UTF-8");
        response.getOutputStream().println("load successful");

        setPicPath(request, picFileName);
    }


    private void handlePicUploadError(HttpServletRequest request, HttpServletResponse response) throws IOException {

        response.getOutputStream().println("Invalid image format");

    }

    public void setPicPath(HttpServletRequest request, String filepath) {
        String nickname = (String) request.getSession().getAttribute("nickname");

        AccountDao userControl = (AccountDao) getServletContext().getAttribute(AccountDao.class.getName());
        iAccount profile;
        try {
            profile = userControl.getUser(nickname);
            if(!profile.getPicturePath().equals("")){
                fileManager.deletePicFIle(profile.getPicturePath());
            }
            profile.setPicturePath(filepath);
            userControl.changeUser(profile);
        } catch (Exception e) {
            e.printStackTrace();
        }


    }





}