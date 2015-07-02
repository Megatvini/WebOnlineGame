package Core.FileUploading;

import Core.Dao.AccountDao;
import Interfaces.iAccount;
import Interfaces.iProfile;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;



@WebServlet(urlPatterns = "/images/*")
public class Downloader extends HttpServlet {




    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        ServletContext cntx= getServletContext();
        FileManager fileManager = (FileManager) cntx.getAttribute(FileManager.class.getName());

        AccountDao accountDao = (AccountDao) cntx.getAttribute(AccountDao.class.getName());


        // Get the absolute path of the image
        String nickName  = request.getParameter("nickname");
        try {
            if (nickName == null)
                throw new RuntimeException("invalid path");

            iAccount account  = accountDao.getUser(nickName);

            String filename = account.getPicturePath() ;

            if(filename.equals("")){
                filename = returnDefaultPictureName(account);
            }

            // retrieve mimeType dynamically
            String mime = cntx.getMimeType(filename);

            response.setContentType(mime);

            File file = fileManager.createFile(fileManager.createFilePath(filename));

            response.setContentLength((int) file.length());

            OutputStream out = response.getOutputStream();

            FileInputStream in = new FileInputStream(file);

            // Copy the contents of the file to the output stream
            byte[] buf = new byte[1024];
            int count = 0;
            while ((count = in.read(buf)) >= 0) {
                out.write(buf, 0, count);
            }
            out.close();
            in.close();

        }catch (Exception e ){
            response.setContentType("text/plain");
            response.getWriter().println(e.getMessage());
        }

    }

    private String returnDefaultPictureName(iAccount account) {
        String filename;
        String sex  = "male" ;
        if (account.getGender().equals(iProfile.Gender.FEMALE)){
            sex = "female";
        }
        filename= defaultPicName(sex);
        return filename;
    }

    private String defaultPicName(String sex) {
        return "no_profile_"+sex+".jpg";
    }

}