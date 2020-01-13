package com.example.lab3;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Future;
import io.vertx.core.eventbus.MessageConsumer;
import io.vertx.core.json.JsonObject;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class ConsumerVerticle extends AbstractVerticle {

    private String name = null;
    private EntityManagerFactory emf =
            Persistence.createEntityManagerFactory("test");

    public ConsumerVerticle(String name) {
        this.name = name;
    }

    public void start(Future<Void> startFuture) {

        MessageConsumer<JsonObject> messageConsumer = vertx.eventBus().consumer("hej");
        messageConsumer.handler(message -> {
            JsonObject jsonMessage = message.body();
            System.out.println("CONSUMERVERTICLE: userID: " + jsonMessage.getValue("userId"));
            String data = getLoginHistoryToPlot(String.valueOf(jsonMessage.getValue("userId")));
            JsonObject jsonReply = new JsonObject().put("data", data);
            message.reply(jsonReply);
        });

    }

    public String getLoginHistoryToPlot(String userId) {
        System.out.println("USERID: " + userId);
        List<TLoginEntity> result = getAllLoginHistoryByUser(Integer.parseInt(userId));
        Integer[] intArr = loginListToWeekArray(result);

        return convertToNumbersList(intArr);
    }

    public List<TLoginEntity> getAllLoginHistoryByUser(int userId){
        EntityManager em = emf.createEntityManager();
        em.getTransaction().begin();
        return em.createNamedQuery("TLoginEntity.findAllByUser", TLoginEntity.class).setParameter("userId", userId).getResultList();
    }

    private static Integer [] loginListToWeekArray(List<TLoginEntity> list){
        Calendar cal1 = Calendar.getInstance();
        Calendar cal2 = Calendar.getInstance();
        Date today = new Date();
        Integer [] result = {
                0, 0, 0, 0, 0, 0, 0
        };

        Date [] dates = new Date[7];

        dates[0] = today;
        dates[1] = new Date(today.getTime() - (86400000 * 1));
        dates[2] = new Date(today.getTime() - (86400000 * 2));
        dates[3] = new Date(today.getTime() - (86400000 * 3));
        dates[4] = new Date(today.getTime() - (86400000 * 4));
        dates[5] = new Date(today.getTime() - (86400000 * 5));
        dates[6] = new Date(today.getTime() - (86400000 * 6));

        for(TLoginEntity t: list){
            cal1.setTime(t.gettDate());
            for(int i = 0; i<dates.length; i++){
                cal2.setTime(dates[i]);
                if(cal1.get(Calendar.DAY_OF_YEAR) == cal2.get(Calendar.DAY_OF_YEAR) &&
                        cal1.get(Calendar.YEAR) == cal2.get(Calendar.YEAR)){
                    result[i]++;
                }
            }
        }

        return result;

    }

    private static String convertToNumbersList(Integer[] target) {
        String result = "";
        for(int i = 0; i < target.length - 1; i++){
            result += target[i] + ",";
        }
        result += target[target.length-1];
        System.out.println("Result: " + result);
        return result;
    }

}