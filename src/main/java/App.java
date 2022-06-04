import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

public class App extends JFrame {
    private JTextField textField1;
    private JTextField textField2;
    private JTextField textField3;
    private JButton calcularButton;
    private JPanel mainPainel;

    public App(){
        setContentPane(mainPainel);
        setTitle("Calcular período");
        setSize(450,300);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setVisible(true);

        calcularButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent IOActionEvent) {

                String primeiradata = textField1.getText().trim();
                String segundadata = textField2.getText().trim();

                try {
                    Socket socket = new Socket("localhost",4999);

                    PrintWriter pw = new PrintWriter(socket.getOutputStream());
                    pw.println(primeiradata+segundadata);
                    pw.flush();

                    InputStreamReader in = new InputStreamReader(socket.getInputStream());
                    BufferedReader bf = new BufferedReader(in);

                    String text = bf.readLine();
                    System.out.println("server :"+text);

                }catch(Exception e){
                    JOptionPane.showMessageDialog(null,"O estagiário derrubou o servidor!!");
                }

            }
        });
    }

    public static void main(String[] args) throws IOException {
        App minhaCalculadora = new App();

        ServerSocket serverSocket = new ServerSocket(4999);
        Socket socket = serverSocket.accept();
        System.out.println("cliente connected");

        InputStreamReader in = new InputStreamReader(socket.getInputStream());
        BufferedReader bf = new BufferedReader(in);

        String text = bf.readLine();
        System.out.println("client : "+text);

        String data1 = "";
        String data2 = "";

        for (int i = 0;i<=text.length();i++){
            if (i <= 7){
                data1 = data1 + text.charAt(i);
            }else if(i >7 && i<=15){
                data2 = data2 + text.charAt(i);
            }
        }

        System.out.println(data1);
        System.out.println(data2);

        int dia1 = 0;
        int mes1 = 0;
        int ano1 = 0;
        int dia2 = 0;
        int mes2 = 0;
        int ano2 = 0;

        for(int i =0; i<=data1.length();i++){
            if (i<=1){
                dia1 = dia1 + data1.charAt(i);
            }else if(i>1 && i<=3){
                mes1 = mes1 + data1.charAt(i);
            }else if(i>3 && i<=7){
                ano1 = ano1 + data1.charAt(i);
            }
        }

        for(int i =0; i<=data2.length();i++){
            if (i<=1){
                dia2 = dia2 + data2.charAt(i);
            }else if(i<=3){
                mes2 = mes2 + data2.charAt(i);
            }else if(i<=7){
                ano2 = ano2 + data2.charAt(i);
            }
        }

        System.out.println("ano1 "+ano1);
        System.out.println("ano2 "+ano2);

        int resano = ano2-ano1;
        int resmes = mes2-mes1;
        int resdia = dia2-dia1;

        System.out.println("diferença de dias: "+resdia);
        System.out.println("diferença de mes: "+resmes);
        System.out.println("diferença de anos: "+resano);
//
//        String teste1 = String.valueOf(resdia);
//        String teste2 = String.valueOf(resmes);
//        String teste3 = String.valueOf(resano);
//
//        String resultado = teste1+teste2+teste3;

        PrintWriter pr = new PrintWriter(socket.getOutputStream());
        pr.println("teste");
        pr.flush();
    }
}


