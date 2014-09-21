/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package unibaveopencodeviewer.servlet;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author Stéfani
 */
public class ViewerServlet extends HttpServlet{
    
    private static final String repositorioLivro = "resources/livros/";
    private static final String repositorioHtml = "resources/html/";

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String numTombo = req.getParameter("bookid");
        if(numTombo != null){
            File livro = new File(repositorioLivro, numTombo + ".pdf");
            if(livro.isFile() && livro.canRead()){
                resp.setContentType("application/pdf");
                resp.setStatus(HttpServletResponse.SC_OK);
                resp.setContentLength((int) livro.length());
                Files.copy(livro.toPath(), resp.getOutputStream());
                return;
            }
            resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
            resp.setContentType("text/html");
            File notFound = new File(repositorioHtml, "notFound.html");
            BufferedReader br = new BufferedReader(new FileReader(notFound));
            String linha = null;
            
            while((linha = br.readLine()) != null){
                resp.getWriter().println(linha.replace("%bookID%", numTombo));
            }
            br.close();
            return;
        }
        resp.setContentType("text/html");
        resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
        File notFound = new File(repositorioHtml, "notFoundParam.html");
        Files.copy(notFound.toPath(), resp.getOutputStream());
    }
}
