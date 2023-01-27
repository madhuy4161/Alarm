import javazoom.jl.player.*;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class Remainder implements Runnable{
    private JPanel Remainder;
    private JButton ringtoneButton;
    private JButton setButton;
    private JButton listenButton;
    private JComboBox comboBox1;
    private JComboBox comboBox2;
    private JLabel Time_display;
    private JLabel alarm_music;
    private JComboBox comboBox3;
    String sound,title,timeMeridian;
    FileInputStream fileInputStream;
    BufferedInputStream bufferedInputStream;
    Player player;
    long all;
    String hours,hh,mm,tm;
    String hoursAlarm,minutesAlarm,tmAlarm;


    public void run(){
        while (true){
            Calendar c=Calendar.getInstance();
            SimpleDateFormat simpleDateFormat=new SimpleDateFormat("hh:mm:ss a");
            Date date=c.getTime();
            hours=simpleDateFormat.format(date);
            Time_display.setText(hours);
        }
    }

    public void chooseSong()
    {
        JFileChooser jfc=new JFileChooser();
        int sound_int=jfc.showOpenDialog(null);
        if(sound_int == JFileChooser.APPROVE_OPTION){
            File alarm_Music=jfc.getSelectedFile();
            sound= alarm_Music.getAbsolutePath();
            title= jfc.getSelectedFile().getName();
            System.out.println(title);
        }
        else if (sound_int==JFileChooser.CANCEL_OPTION)
        {
            JOptionPane.showMessageDialog(null,"you dont choose alarm miusic");
        }
    }
    public void startAlarm()
    {
        try{
            fileInputStream=new FileInputStream(sound);
            bufferedInputStream=new BufferedInputStream(fileInputStream);
            player=new Player(bufferedInputStream);
            all=fileInputStream.available();
            new Thread()
            {
                public void run(){
                    try{
                       player.play();
                    }
                    catch (Exception e)
                    {

                    }
                }
            }.start();
        }
        catch(Exception e){

    }
    }

    public Remainder() {
        Thread t=new Thread(this);
        t.start();
        Calendar c=Calendar.getInstance();
        SimpleDateFormat simpleDateFormat1=new SimpleDateFormat("hh");
        SimpleDateFormat simpleDateFormat2=new SimpleDateFormat("mm");
        SimpleDateFormat simpleDateFormat3=new SimpleDateFormat("a");
        Date date=c.getTime();
        hh=simpleDateFormat1.format(date);
        mm=simpleDateFormat2.format(date);
        tm=simpleDateFormat3.format(date);
        comboBox1.setSelectedItem(hh);
        comboBox2.setSelectedItem(mm);
        comboBox3.setSelectedItem(tm);
        listenButton.setEnabled(false);
        ringtoneButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                chooseSong();
                if(!sound.equals(null))
                {
                    alarm_music.setText("Alarm Music : "+title);
                }
                ringtoneButton.setEnabled(true);
                listenButton.setEnabled(true);
            }
        });
        listenButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(listenButton.getText().equals("Listen"))
                {
                    startAlarm();
                    listenButton.setText("Stop Sound");
                    setButton.setEnabled(false);
                } else if (listenButton.getText().equals("Stop Sound")) {
                   stopAlarm();
                   listenButton.setText("Listen");
                   setButton.setEnabled(true);
                }

            }
        });
        setButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                setButton.setEnabled(false);
                hoursAlarm=comboBox1.getSelectedItem().toString();
                minutesAlarm=comboBox2.getSelectedItem().toString();
                tmAlarm=comboBox3.getSelectedItem().toString();
                if( alarm_music.getText() != ""){
                  alarm_Time(Integer.valueOf(hoursAlarm),Integer.valueOf(minutesAlarm),tmAlarm);
                }
                else{
                    JOptionPane.showMessageDialog(null,"You dont choose alrm music","warning",JOptionPane.INFORMATION_MESSAGE);

                }

            }
        });
    }

    public void stopAlarm(){
        if(player != null)
        {
            player.close();
        }
    }
    public void alarm_Time(final int hour,final int minute,final String timemeridian){
        Thread t=new Thread(){
            public void run()
            {
                int time=0;
                while (time==0)
                {
                    Calendar c=Calendar.getInstance();
                    int h=c.get(Calendar.HOUR);
                    int m=c.get(Calendar.MINUTE);
                    int t=c.get(Calendar.AM_PM);
                    if(t==1)
                    {
                        timeMeridian="PM";
                    }
                    else {
                        timeMeridian="AM";
                    }
                    if(hour ==h && minute ==m && timemeridian.equals(timeMeridian))
                    {
                        startAlarm();
                        JOptionPane.showConfirmDialog(null,"STOP ALARM","",JOptionPane.CLOSED_OPTION);
                        stopAlarm();
                        setButton.setEnabled(true);
                        break;
                    }
                }
            }
        };t.start();
    }


    public static void main(String[] args) {
        JFrame frame = new JFrame("Remainder");
        frame.setContentPane(new Remainder().Remainder);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);


    }
}



