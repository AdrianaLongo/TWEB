import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import dao.Corso;
import dao.DAO;
import dao.Docente;

import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.lang.reflect.Type;
import java.sql.SQLException;
import java.util.ArrayList;

@WebServlet(name = "PopolaDocenteServlet", urlPatterns = "/PopolaDocenteServlet")
public class PopolaDocenteServlet extends HttpServlet {
    DAO dao = null;
    ArrayList<Docente> docenti;
    Gson gson = new Gson();
    String Json;

    public void init(ServletConfig conf) throws ServletException {

        super.init(conf);
        ServletContext ctx = conf.getServletContext(); //prendo il context per accedere a web.xml
        String url = ctx.getInitParameter("DB-Url"); //indirizzo DB nel web.xml
        String user = ctx.getInitParameter("user");
        String pwd = ctx.getInitParameter("password");
        dao = new DAO(url, user, pwd); //creo un nuovo oggetto DAO, vedere costruttore in DAO
    }


    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("application/json, charset=UTF-8");
        //RequestDispatcher reqDisp = request.getRequestDispatcher("Logout.html");
        PrintWriter out = response.getWriter();
        try {
            docenti = dao.mostraDocenti();
            System.out.print("Tutor recuperati");
            Type type = new TypeToken<ArrayList<Docente>>() {}.getType();
            String jsonDocenti = gson.toJson(docenti, type); //e se io voglio passare più dati Json sulla stessa pagina ?
            out.print(jsonDocenti);
            System.out.println(jsonDocenti);
            out.close();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            Useful error = new Useful("Tutor not retrieved", -1);
            String Json = gson.toJson(error);
            out.println(Json);//mando un json al fronto di mancata operazione
            out.flush();
        }

    }
}
