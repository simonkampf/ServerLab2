package com.example.lab3;


import io.vertx.core.AbstractVerticle;
import io.vertx.core.http.HttpServer;
import io.vertx.core.http.HttpServerResponse;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.handler.StaticHandler;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

public class HttpVerticle extends AbstractVerticle {

    String plot = null;

    @Override
    public void start() throws Exception {

        String temp = new File("").getAbsolutePath();
        String path = temp + "/src/main/java/com/mycompany/";
        HttpServer httpServer = vertx.createHttpServer();

        Router router = Router.router(vertx);
        router.route().handler(StaticHandler.create("canvasjs-2"));
        router.route("/showData");

        httpServer.requestHandler(request -> {

            String userId = request.getParam("userId");
            String plotType = request.getParam("plotType");
            HttpServerResponse response = request.response();
            response.putHeader("content-type", "text/html");


            JsonObject jsonMessage = new JsonObject().put("userId", userId);

            vertx.eventBus().send("hej", jsonMessage, messageAsyncResult -> {
                if(messageAsyncResult.succeeded()) {
                    JsonObject jsonReply = (JsonObject) messageAsyncResult.result().body();
                    System.out.println("HTTPVERTICLE: received reply: " + jsonReply.getValue("data"));
                    float[] data = stringToFloatArray(String.valueOf(jsonReply.getValue("data")));
                    response.end(plotBuilder(data, plotType));
                }
            });

        });

        httpServer.listen(8091);
    }

    private float[] stringToFloatArray(String valueString){
        if(valueString.equals("")){
            float[] values = {0.0f,0.0f,0.0f,0.0f,0.0f,0.0f,0.0f};
            return values;
        }
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


    private String plotBuilder(float[] yData, String plotType){ // ta inData och Diagramtyp

        if(plotType == null){
            return plot;
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

        //System.out.println(html);

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