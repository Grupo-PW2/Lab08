package controller.users;

import controller.access.AccessControllerView;
import model.User;

import javax.servlet.RequestDispatcher;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

@SuppressWarnings("serial")
public class UsersControllerIndex extends HttpServlet {

    public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {

        try{

            //Se usa para revisar si hay una sesion activa
            HttpSession sesion= request.getSession();

            if (AccessControllerView.checkPermission(sesion.getAttribute("userID").toString(),request.getRequestURI())){

                //Intenta hallar una sesion activa
                User user = UsersControllerView.getUser(sesion.getAttribute("userID").toString());
                if (user == null) throw new NullPointerException("UsersControllerIndex: El usuario recibido es nulo.");

                request.setAttribute("User",user);
                request.setAttribute("UsersList",UsersControllerView.getAllUsers());
                request.setAttribute("serverResponse",sesion.getAttribute("serverResponse"));
                sesion.setAttribute("serverResponse","!");
                RequestDispatcher dispatcher = getServletContext().getRequestDispatcher("/WEB-INF/View/Users/index.jsp");
                dispatcher.forward(request,response);

            } else {
                request.getSession().setAttribute("serverResponse","{\"color\": \"red\",\"response\":\"You don\\'t have permission to access /users.\"}");
                response.sendRedirect("/");
            }

            //Si no la encuentra, redirige a la pagina inicial.
            //Si el usuario no tiene permiso tambien redirigie a la pagina inicial
        } catch (Exception e){
            e.printStackTrace();
            response.sendRedirect("/");
        }

    }

    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        doPost(request, response);
    }
}