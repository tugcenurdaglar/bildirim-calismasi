package com.tugcenurdaglar.bildirimcalisma;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {
    private Button buttonBildir;

    private NotificationCompat.Builder builder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        buttonBildir = findViewById(R.id.buttonBildir);

        buttonBildir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                durumaBagli();;

            }
        });
    }

    public void durumaBagli(){
        //öncelikle bildirim yöneticisinden bir tane nesne oluşturulur
        //manger tarzı şeyler systemservice inden alınır
        NotificationManager bildirimYoneticisi =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        //bildirim üzerine tıklanıldığında nereye gitmesini istersek:

        Intent intent = new Intent(MainActivity.this, KarsilamaEkraniActivity.class);

        PendingIntent gidilecekIntent = PendingIntent.getActivity(this,1,intent,PendingIntent.FLAG_UPDATE_CURRENT);


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){//OREO sürümü için bu kod çalışacak

            String kanalId = "kanalId";
            //amacı bildirim geldiği zaman, bildirimleri bir arada toplayabilmek için aynı kanalda olması isteniyor

            String kanalAd = "kanalAd";
            String kanalTanım = "kanalTanım";

            int kanalOnceligi = NotificationManager.IMPORTANCE_HIGH;//yüksek öncelikli olarak gösterilsin demek

            //Notification kanalını oluştur

            NotificationChannel kanal = bildirimYoneticisi.getNotificationChannel(kanalId);

            if (kanal == null){ //daha yeni oluşturulmuşsa

                kanal = new NotificationChannel(kanalId,kanalAd,kanalOnceligi);
                kanal.setDescription(kanalTanım);
                bildirimYoneticisi.createNotificationChannel(kanal);
            }

            builder = new NotificationCompat.Builder(this,kanalId);

            builder.setContentTitle("Başlık");
            builder.setContentText("İçerik");
            builder.setSmallIcon(R.drawable.resim);
            builder.setAutoCancel(true); //gelen bildirime tıklanldığında kendini otomatik olarak kapatıyor
            builder.setContentIntent(gidilecekIntent);




        }else {//OREO sürümü haricinde bu kod çalışacak

            builder = new NotificationCompat.Builder(this);

            builder.setContentTitle("Başlık");
            builder.setContentText("İçerik");
            builder.setSmallIcon(R.drawable.resim);
            builder.setAutoCancel(true); //gelen bildirime tıklanldığında kendini otomatik olarak kapatıyor
            builder.setContentIntent(gidilecekIntent);
            builder.setPriority(Notification.PRIORITY_HIGH);

        }

        bildirimYoneticisi.notify(1,builder.build());

    }
}