import java.io.*;
import java.net.*;
import java.util.concurrent.TimeUnit;
import javax.swing.*;
import javax.swing.text.*;
import java.awt.event.*;
import java.awt.*;

public class fileio{
  public static DatagramSocket ds ;
  public static JFrame frame;
  public static Thread t;
  public static String serverName = "", word[];
  public static int port = 7978, ft = 0;
  public static FileReader file = null;
  public static String content = "";
  public static BufferedReader br;
  public static int delay =5;
  public static Object lock = new Object();


  public static void main(String [] args){
    frame= new JFrame("Sender<Simulator>");
    String[] words = null;



    JTextField portF= new JTextField("7978");
    JTextField ipF= new JTextField("127.0.0.1");
    JTextField fileF= new JTextField("input.txt");
    JLabel l1= new JLabel("IP ADDRESS");
    JLabel l2= new JLabel("PORT");
    JLabel l3 = new JLabel("SELECT FILE");
    JLabel l4 = new JLabel("STATUS");
    JButton pauseB = new JButton("Pause/Resume");
    JButton sendB = new JButton("SEND");
    JButton stopB = new JButton("STOP");
    JButton fileB = new JButton("B FILE");
    JButton fileD = new JButton("D FILE");
    JTextArea stat = new JTextArea("Initializing...");
    DefaultCaret caret = (DefaultCaret)stat.getCaret();
    caret.setUpdatePolicy(DefaultCaret.ALWAYS_UPDATE);
    JScrollPane stati = new JScrollPane(stat,JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,
                    JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
    stat.setLineWrap(true);
    stat.setWrapStyleWord(true);

    portF.setBounds(130,35,80,25);
    ipF.setBounds(20,35,80,25);
    fileB.setBounds(20,95,100,20);
    fileD.setBounds(20,120,100,20);

    l1.setBounds(20,5,100,25);
    l2.setBounds(130,5,50,25);
    l3.setBounds(20,70,100,25);
    l4.setBounds(170,140,60,25);
    pauseB.setBounds(140,105,150,25);
    sendB.setBounds(240,35,140,25);
    stopB.setBounds(310,105,70,25);
    stati.setBounds(20,170,360,100);
    stat.setEditable(false);


    frame.setSize(400,330);
    frame.setVisible(true);
    frame.setLayout(null);
    Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
    frame.setLocation(dim.width/2-frame.getSize().width/2, dim.height/2-frame.getSize().height/2);
    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    frame.add(portF);frame.add(ipF);frame.add(l1);frame.add(l2);frame.add(sendB);frame.add(l4);
    frame.add(pauseB);frame.add(l3);frame.add(fileB);frame.add(stopB);frame.add(stati);frame.add(fileD);

    fileD.addActionListener(new ActionListener(){
      public void actionPerformed(ActionEvent push5){
        ft =1;
        fileB.doClick();

      }

    });
    fileB.addActionListener(new ActionListener(){
      public void actionPerformed(ActionEvent push4){
        JFileChooser fc=new JFileChooser();
        File workingDirectory = new File(System.getProperty("user.dir"));
        fc.setCurrentDirectory(workingDirectory);
        int j=fc.showOpenDialog(fileB);
        if(j==JFileChooser.APPROVE_OPTION){

        //  try{
          File f=fc.getSelectedFile();
          fileF.setText(f.getPath());
          System.out.println(f.getPath());
      //  }catch(Exception e){e.printStackTrace();}
      }
      }
    });

    pauseB.addActionListener(new ActionListener(){
      public void actionPerformed(ActionEvent push3){
        t= new Thread(new Runnable() {
         public void run() {
      //  while(true){

      try{
        synchronized(lock){
        if(pauseB.getText()=="PAUSE"){
          delay=1;
          System.out.println("PAUSED");
          pauseB.setText("RESUME");
          stat.append("PAUSED");
        }
        else if(pauseB.getText()=="RESUME"){
          delay=5;

          lock.notify();
          System.out.println("RESUME");
          stat.append("RESUME");
          pauseB.setText("PAUSE");}
        }
      }catch(Exception ex){ex.printStackTrace();}
    }});t.start();

      }

    });

    sendB.addActionListener(new ActionListener(){
      public void actionPerformed(ActionEvent push1){


        try{
          pauseB.doClick();
          //pauseB.doClick();
          ipF.setEditable(false);
          portF.setEditable(false);
          fileF.setEditable(false);
          pauseB.setText("PAUSE");
          serverName = ipF.getText();
          InetAddress ip = InetAddress.getByName(serverName);
          port= Integer.parseInt(portF.getText());
          String filename = fileF.getText();
          file = new FileReader(filename);
          br = new BufferedReader(file);
          stat.setText("Initializing...");
          System.out.println("Connecting to " + serverName + " on port " + port);
          stat.append("\nConnecting to " + serverName + " on port " + port);
          ds = new DatagramSocket();
          //client = new Socket(serverName, port);

      //    System.out.println("Just connected to " + client.getRemoteSocketAddress());
        //  stat.append("\nJust connected to " + client.getRemoteSocketAddress());
    //      OutputStream outToServer = client.getOutputStream();
      //    DataOutputStream out = new DataOutputStream(outToServer);

             new Thread(new Runnable() {
            public void run() {
              try{

          while((content = br.readLine())!=null)
          {
            if(ft==1){
            word = content.split(" ");
            FileReader conf = new FileReader(System.getProperty("user.dir")+"/config.txt");
            BufferedReader fr = new BufferedReader(conf);
            String pack = fr.readLine();
            String line = "";
            for(int i =0; i< pack.length();i++){
              if(pack.charAt(i)=='i'){

                word[i]="00000000000000000000000000000000"+Integer.toBinaryString(Integer.parseInt(word[i]));
                word[i]=word[i].substring(word[i].length()-32,word[i].length());
              }
              if(pack.charAt(i)=='s'){
                word[i]="00000000000000000000000000000000"+Integer.toBinaryString(Integer.parseInt(word[i]));
                word[i]=word[i].substring(word[i].length()-16,word[i].length());
              }
              if(pack.charAt(i)=='l'){
                word[i]="0000000000000000000000000000000000000000000000000000000000000000"+Long.toBinaryString(Long.parseLong(word[i]));
                word[i]=word[i].substring(word[i].length()-64,word[i].length());

              }
              if(pack.charAt(i)=='f'){
                word[i]="00000000000000000000000000000000"+Integer.toBinaryString(Float.floatToIntBits(Float.parseFloat(word[i])));
                word[i]=word[i].substring(word[i].length()-32,word[i].length());

              }
              if(pack.charAt(i)=='d'){
                word[i]="0000000000000000000000000000000000000000000000000000000000000000"+Long.toBinaryString(Double.doubleToLongBits(Double.parseDouble(word[i])));
                word[i]=word[i].substring(word[i].length()-64,word[i].length());

              }
              if(pack.charAt(i)=='b'){

              }
              if(pack.charAt(i)=='o'){
                word[i]="00000000"+Integer.toBinaryString(Integer.parseInt(word[i]));
                word[i]=word[i].substring(word[i].length()-8,word[i].length());

              }
              line+=word[i];

            }
            System.out.println(line+"-Binary Line");
            content = line;

            }
            System.out.println(content);
            stat.append("\n\n");

            for(int i=0;i<content.length()-8;i+=8){
              int ctt =i/8+1;
              String he = content.substring(i,i+8);
            int decimal = Integer.parseInt(he,2);
            String hexStr = Integer.toString(decimal,16);
            stat.append("("+ctt+")" +hexStr+" ");}
            DatagramPacket dp = new DatagramPacket(content.getBytes(), content.length(), ip, port);
            ds.send(dp);
      //    out.writeUTF(content);
          TimeUnit.SECONDS.sleep(5);
          synchronized(lock){
          //  System.out.println("Not fied");
          if(delay!=5)
          lock.wait();
        //  System.out.println("Notified");
          }

        }
        ds.close();
        ft=0;
        }catch(Exception ex){ex.printStackTrace();}
      }}).start();

        }catch (Exception e) {
             // if any error occurs
             e.printStackTrace();
          }
      }
    });


    stopB.addActionListener(new ActionListener(){
      public void actionPerformed(ActionEvent push2){
        try{
        br.close();
        stat.append("\n\nStopped Sending Data");
        ipF.setEditable(true);
        portF.setEditable(true);
        fileF.setEditable(true);
        pauseB.setText("Pause/Resume");
        ds.close();
      }catch(IOException io){io.printStackTrace();}

      }
    });




  }
}
