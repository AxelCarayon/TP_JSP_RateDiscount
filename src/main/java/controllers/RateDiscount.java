/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controllers;

import SQL.DiscountEntity;
import SQL.ExtendedDAO;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import simplejdbc.DAOException;
import simplejdbc.DataSourceFactory;

/**
 *
 * @author Axel
 */
@WebServlet(name = "RateDiscount", urlPatterns = {"/RateDiscount"})
public class RateDiscount extends HttpServlet {

    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException, SQLException {
        ExtendedDAO dao = new ExtendedDAO(DataSourceFactory.getDataSource());
        String code = request.getParameter("code");
        String action = "";
        String error = "";
        action += request.getParameter("action");
        
        try {
            try{
                if (action.equals("ADD")){
                    Double rate = Double.parseDouble(request.getParameter("rate"));
                    dao.insertDiscount(new DiscountEntity(code,rate));
                    System.out.println("Rabais ajouté");
                }
            }catch(Exception e){
                if (request.getParameter("rate").equals("") || code.equals("")){
                    error = "Le code ou le taux n'as pas été renseigné.";
                }
                else{
                    error = "Impossible d'ajouter la réduction, elle existe déjà.";
                }
            }
            try{
            if (action.equals("DELETE")){
                dao.deleteDiscount(code);
                System.out.println("Rabais supprimé");
            }
            }catch(Exception e){
                error = "Impossible de supprimer la réduction, elle est utilisée ailleurs.";
            }
            List<DiscountEntity> discounts = dao.existingDiscountCode();
            request.setAttribute("discounts",discounts);
            request.setAttribute("error",error);
        } catch (DAOException ex) {
            Logger.getLogger(RateDiscount.class.getName()).log(Level.SEVERE, null, ex);
        }
        String jspView = "RateDiscount.jsp"; 
        
        response.setContentType("text/html;charset=UTF-8");
        request.getRequestDispatcher(jspView).forward(request, response);
    }

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /**
     * Handles the HTTP <code>GET</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            processRequest(request, response);
        } catch (SQLException ex) {
            Logger.getLogger(RateDiscount.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * Handles the HTTP <code>POST</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            processRequest(request, response);
        } catch (SQLException ex) {
            Logger.getLogger(RateDiscount.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

}
