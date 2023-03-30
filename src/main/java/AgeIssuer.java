import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet(
        name = "Age Issuer",
        urlPatterns = {"/age-issuer"}
)
public class AgeIssuer extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
//        ServletOutputStream out = resp.getOutputStream();
//        out.write("hello heroku".getBytes());
//        out.flush();
//        out.close();
        response.setContentType("text/html");

        PrintWriter out = response.getWriter();
        out.println("<html><head>");
        out.println("<meta charset=\"UTF-8\">\n" +
                "    <meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\">\n" +
                "    <meta http-equiv=\"X-UA-Compatible\" content=\"ie=edge\">\n" +
                "    <title>Age Issuer</title>\n" +
                "    <link rel=\"stylesheet\"\n" +
                "          href=\"https://cdnjs.cloudflare.com/ajax/libs/twitter-bootstrap/3.3.7/css/bootstrap.min.css\" />\n" +
                "\n" +
                "    <style>\n" +
                "        .qr-code {\n" +
                "            max-width: 200px;\n" +
                "            margin: 10px;\n" +
                "        }\n" +
                "\n" +
                "        body {\n" +
                "            background-color: #555;\n" +
                "            justify-content: center;\n" +
                "            font-family: 'VT323', monospace;\n" +
                "            font-size: 1.5rem;\n" +
                "            color: black;\n" +
                "            margin: 20px;\n" +
                "            text-align: center;\n" +
                "        }\n" +
                "\n" +
                "        h1 {\n" +
                "            font-weight: normal;\n" +
                "            font-size: 40px;\n" +
                "            font-weight: normal;\n" +
                "            text-transform: uppercase;\n" +
                "            text-align: center;\n" +
                "        }\n" +
                "\n" +
                "        h3 {\n" +
                "            text-align: center;\n" +
                "            color: #46a294;\n" +
                "            font-size: 20px;\n" +
                "            width: 100%;\n" +
                "            margin: 15px 15px;\n" +
                "            position: relative;\n" +
                "            font-weight: 400;\n" +
                "        }\n" +
                "\n" +
                "        h2 {\n" +
                "            text-align: center;\n" +
                "            color: #46a294;\n" +
                "            font-size: 28px;\n" +
                "            width: 100%;\n" +
                "            margin: 10px 10px;\n" +
                "            position: relative;\n" +
                "            line-height: 58px;\n" +
                "            font-weight: 400;\n" +
                "        }\n" +
                "\n" +
                "        h2:before {\n" +
                "            content: \" \";\n" +
                "            position: absolute;\n" +
                "            left: 50%;\n" +
                "            bottom: 0;\n" +
                "            width: 100px;\n" +
                "            height: 2px;\n" +
                "            font-weight: bold;\n" +
                "            background-color: #2079df;\n" +
                "            margin-left: -50px;\n" +
                "        }\n" +
                "\n" +
                "        button.center-block {\n" +
                "            margin-top: 20px;\n" +
                "            display: block;\n" +
                "            width: 60%;\n" +
                "            line-height: 2em;\n" +
                "            background: rgba(114, 212, 202, 1);\n" +
                "            border-radius: 5px;\n" +
                "            border: 0;\n" +
                "            border-top: 1px solid #B2ECE6;\n" +
                "            box-shadow: 0 0 0 1px #46A294, 0 2px 2px #808389;\n" +
                "            color: #FFFFFF;\n" +
                "            font-size: 1.5em;\n" +
                "            text-align: center;\n" +
                "            text-shadow: 0 1px 2px #21756A;\n" +
                "        }\n" +
                "\n" +
                "        button.center-block:hover {\n" +
                "            background: linear-gradient(to bottom, rgba(107, 198, 186, 1) 0%, rgba(57, 175, 154, 1) 100%);\n" +
                "        }\n" +
                "\n" +
                "        button.center-block:active {\n" +
                "            box-shadow: inset 0 0 5px #000;\n" +
                "            background: linear-gradient(to bottom, rgba(57, 175, 154, 1) 0%, rgba(107, 198, 186, 1) 100%);\n" +
                "        }\n" +
                "\n" +
                "        button.center-block:focus {\n" +
                "            outline: none;\n" +
                "            border-color: green;\n" +
                "        }\n" +
                "    </style>");
        out.println("</head><body>");
        out.println("<br><h1> Age Issuer </h1> <br>");
        out.println("<h2> Scan and get your verifiable credential </h2>");
        out.println("<div class=\"container-fluid\">\n" +
                "    <div class=\"text-center\">\n" +
                "        <img src=\"https://chart.googleapis.com/chart?cht=qr&chl=192.168.1.2:7777&chs=160x160&chld=L|0\"\n" +
                "             class=\"qr-code img-thumbnail img-responsive\" />\n" +
                "    </div>\n" +
                "\n" +
                "</div>");
        out.println("</body></html>");
        out.flush();
        out.close();
    }

}
