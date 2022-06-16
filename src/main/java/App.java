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
    private JButton novoCalculo;

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

                    textField3.setText(text);
                    in.close();
                    bf.close();
                    pw.close();
                    socket.close();
                    textField1.setEnabled(false);
                    textField2.setEnabled(false);
                    calcularButton.setEnabled(false);

                }catch(Exception e){
                    JOptionPane.showMessageDialog(null,"O estagiário derrubou o servidor!!");
                }

            }
        });

        novoCalculo.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                textField1.setEnabled(true);
                textField2.setEnabled(true);
                calcularButton.setEnabled(true);
                textField1.setText("");
                textField2.setText("");
                textField3.setText("");
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

        String dia1 = "";
        String mes1 = "";
        String ano1 = "";
        String dia2 = "";
        String mes2 = "";
        String ano2 = "";

        for(int i =0; i<=data1.length()-1;i++){
            if (i<=1){
                dia1 = dia1 +  data1.charAt(i);
            }else if(i>1 && i<=3){
                mes1 = mes1 + data1.charAt(i);
            }else if(i>3 && i<=7){
                ano1 = ano1 + data1.charAt(i);
            }
        }

        for(int i =0; i<=data2.length()-1;i++){
            if (i<=1){
                dia2 = dia2 + data2.charAt(i);
            }else if(i<=3){
                mes2 = mes2 + data2.charAt(i);
            }else if(i<=7){
                ano2 = ano2 + data2.charAt(i);
            }
        }

        int resano =  Integer.parseInt(ano2) - Integer.parseInt(ano1) ;
        int resmes =  Integer.parseInt(mes2) - Integer.parseInt(mes1) ;
        int resdia =  Integer.parseInt(dia2) - Integer.parseInt(dia1) ;

        if(resdia<0){
            resdia = 30 + resdia;
        }
        if (resmes<0){
            resmes = 12 + resmes;
        }

        System.out.println("diferença de dias: "+resdia);
        System.out.println("diferença de mes: "+resmes);
        System.out.println("diferença de anos: "+resano);

        PrintWriter pr = new PrintWriter(socket.getOutputStream());
        pr.println(resano+" anos, "+ resmes+ " meses, "+ resdia+" dias");
        pr.flush();
    }
}


