package net.iquesoft.android.seedprojectchat.util;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;

import net.iquesoft.android.seedprojectchat.R;
import net.iquesoft.android.seedprojectchat.view.classes.activity.LoginActivity;

import java.util.HashMap;

public class PushNotificationUtil {

    private static final String TAG = PushNotificationUtil.class.getSimpleName();

    private static PushNotificationUtil instance;

    private static Context context;
    private NotificationManager manager; // Системная утилита, упарляющая уведомлениями
    private int lastId = 0; //постоянно увеличивающееся поле, уникальный номер каждого уведомления
    private HashMap<Integer, Notification> notifications; //массив ключ-значение на все отображаемые пользователю уведомления


    //приватный контструктор для Singleton
    private PushNotificationUtil(Context context){
        this.context = context;
        manager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        notifications = new HashMap<Integer, Notification>();
    }
    /**
     * Получение ссылки на синглтон
     */
    public static PushNotificationUtil getInstance(Context context){
        if(instance==null){
            instance = new PushNotificationUtil(context);
        } else{
            instance.context = context;
        }
        return instance;
    }

    public int createInfoNotification(String message){
        Intent notificationIntent = new Intent(context, LoginActivity.class); // по клику на уведомлении откроется HomeActivity
        Notification.Builder nb = new Notification.Builder(context)
//NotificationCompat.Builder nb = new NotificationBuilder(context) //для версии Android > 3.0
                .setSmallIcon(R.drawable.happy) //иконка уведомления
                .setAutoCancel(true) //уведомление закроется по клику на него
                .setTicker(message) //текст, который отобразится вверху статус-бара при создании уведомления
                .setContentText(message) // Основной текст уведомления
                .setContentIntent(PendingIntent.getActivity(context, 0, notificationIntent, PendingIntent.FLAG_CANCEL_CURRENT))
                .setWhen(System.currentTimeMillis()) //отображаемое время уведомления
                .setContentTitle("You have new message") //заголовок уведомления
                .setDefaults(Notification.DEFAULT_ALL); // звук, вибро и диодный индикатор выставляются по умолчанию

        Notification notification = nb.getNotification(); //генерируем уведомление
        manager.notify(lastId, notification); // отображаем его пользователю.
        notifications.put(lastId, notification); //теперь мы можем обращаться к нему по id
        return lastId++;
    }
}
