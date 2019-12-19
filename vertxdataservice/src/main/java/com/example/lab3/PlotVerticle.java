package com.example.lab3;


import io.vertx.core.AbstractVerticle;
import io.vertx.core.http.HttpServer;
import io.vertx.core.http.HttpServerResponse;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.handler.StaticHandler;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

public class PlotVerticle extends AbstractVerticle {

    String currentPage = null;

    @Override
    public void start() throws Exception {

        String temp = new File("").getAbsolutePath();
        String path = temp + "/src/main/java/com/mycompany/";
        HttpServer httpServer = vertx.createHttpServer();

        Router router = Router.router(vertx);
        router.route().handler(StaticHandler.create("canvasjs-2"));
        router.route("/showData");



        httpServer.requestHandler(request -> {
            final float[] yData = stringToFloatArray(request.getParam("values"));
            String plotType = request.getParam("plotType");
            HttpServerResponse response = request.response();
            response.putHeader("content-type", "text/html");
            currentPage = pageBuilder(yData, plotType);
            response.end(currentPage); // PLOTTYTPES: column, spline, pie
        });

        httpServer.listen(8091);
    }

    private float[] stringToFloatArray(String valueString){
        String[] strArr = {"0","0","0","0","0","0","0"};
        float[] values = new float[7];
        if(valueString != null){
            strArr = valueString.split(",");
        }
        for(int i = 0; i<values.length;i++){
            values[i] = Float.valueOf(strArr[i]);
        }

        return values;
    }


    private String pageBuilder(float[] yData, String plotType){ // ta inData och Diagramtyp

        if(plotType == null){
            return currentPage;
        }

        String[] xData = generateDateArray();

        if(plotType.equals("pie")){
            int sum = 0;
            for(int i = 0; i<yData.length; i++){
                sum = sum + (int) yData[i];
            }
            for(int i = 0; i<yData.length; i++){
                System.out.println(yData[i]);
                yData[i] = (yData[i] / sum);
            }
            System.out.println(sum);
        }

        StringBuilder sb = new StringBuilder();
        sb.append("[");
        for(int i =0; i<xData.length; i++){
            if(i == xData.length-1){
                sb.append("{ label: " +"\""+ xData[i] +"\""+ ", y: " + yData[i] + "}");
            }else{
                sb.append("{ label: " +"\""+ xData[i] +"\""+ ", y: " + yData[i] + "},");
            }
        }
        sb.append("]");


        // PLOTTYTPES: column, spline, pie



        String html = "<html>\n" +
                "<head>\n" +
                "    <script src=\"https://canvasjs.com/assets/script/canvasjs.min.js\"></script>\n" +
                "    <script type=\"text/javascript\">\n" +

                "        window.onload = function () {\n" +
                "\n" +
                "            var chart = new CanvasJS.Chart(\"chartContainer\", {\n" +
                "title: { text: \"Login:s last week\" },"+
                "                data: [\n" +
                "                    {\n" +
                "                        type: \""+plotType+"\",\n" +
                "                        dataPoints:  "  +sb.toString() +
                "                        " +
                "                    }\n" +
                "                ]\n" +
                "            });\n" +
                "\n" +
                "            chart.render();\n" +
                "        }\n" +
                "\n" +
                "    </script>\n" +
                "</head>\n" +
                "\n" +
                "<body>\n" +
                "<div id=\"chartContainer\" style=\"height: 500px; width: 50%;\"></div>\n" +
                "\n" +
                "</body>\n" +
                "</html>";

        return html;
    }

    private String[] generateDateArray(){
        String[] xData = new String[7];

        long DAY_IN_MS = 1000 * 60 * 60 * 24;
        Date date = new Date();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM");

        for(int i = 0; i<xData.length; i++){
            xData[i] = simpleDateFormat.format(date);
            date = new Date(date.getTime() - DAY_IN_MS);
        }


        return xData;
    }
}