package test;

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
@WebServlet(urlPatterns = "/upload")
public class UploadServlet extends HttpServlet {

    public static final String NO_PROFILE_PIC_NAME = "no_profile" ;

    public static final int maxSize = 100000*4;
    public static final int MAX_NUM_PICS = 4000;


    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {

            FileManager fileManager = (FileManager) request.getServletContext().getAttribute(FileManager.class.getName());


            DiskFileItemFactory factory = new DiskFileItemFactory();

            factory.setSizeThreshold(maxSize * MAX_NUM_PICS);

            ServletFileUpload fileUpload = new ServletFileUpload(factory);


            fileUpload.setSizeMax(maxSize);

            List<FileItem> items = fileUpload.parseRequest(request);

            UUID  idOne = UUID.randomUUID() ;


            String picFileName = (idOne + "");


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

                    File targetFile = fileManager.createFile(fileManager.createFilePath(picFileName));


                    OutputStream outStream = new FileOutputStream(targetFile);
                    outStream.write(buffer);


                    if (fileManager.validatePic(picFileName)) {
                        response.setContentType("text/plain");
                        response.setCharacterEncoding("UTF-8");
                        response.getOutputStream().println("load successful");

                       // setPicPath(request, picFileName);
                    } else {
                        handlePicUploadError(request, response);
                    }



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

    public void setPicPath(HttpServletRequest request, String filepath) {
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





}