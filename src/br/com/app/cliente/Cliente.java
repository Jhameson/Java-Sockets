/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.app.cliente;

import br.com.app.bean.FileMessage;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.nio.channels.FileChannel;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;

/**
 *
 * @author jhame
 */
public class Cliente {
    private Socket socket;
    private ObjectOutputStream outputStream;

    public Cliente() throws IOException {
        this.socket = new Socket("localhost", 5555);
        this.outputStream = new ObjectOutputStream(socket.getOutputStream());
        
        new Thread(new ListenerSocket(socket)).start();
        
        menu();
    }
    private void menu() throws IOException{
        String nome = JOptionPane.showInputDialog("Digite seu nome: ");
        this.outputStream.writeObject(new FileMessage(nome));
        
        int option = 0;
        while(option !=-1){
            option = Integer.valueOf(JOptionPane.showInputDialog("\nOlá "+nome+"\nEscolha uma opção!\n\n1 - Sair\n2 - Enviar"));
            if(option == 2){
                send(nome);
            }else if(option == 1){
                System.exit(0);
            }
        }
    }

    private void send(String nome) throws IOException {
              
        JFileChooser fileChooser = new  JFileChooser();
        int opt = fileChooser.showOpenDialog(null);
        if(opt == JFileChooser.APPROVE_OPTION){
            File file = fileChooser.getSelectedFile();
            this.outputStream.writeObject(new FileMessage(nome, file));
        }
    }

    private static class ListenerSocket implements Runnable {

        private ObjectInputStream inputStream;
        
        public ListenerSocket(Socket socket) throws IOException {
            this.inputStream = new ObjectInputStream(socket.getInputStream());
        }
        
        public void  run(){
            FileMessage message = null;
           
                try {
                    while((message = (FileMessage) inputStream.readObject()) != null){
                        JOptionPane.showMessageDialog(null, "\nVocê recebeu um arquivo de "+message.getCliente()+""
                                + "\n\nO arquivo é "+message.getFile().getName());
                        
                       // imprime(message);
                        salvar(message);
                         int option = Integer.valueOf(JOptionPane.showInputDialog("\nEscolha uma opção!\n\n1 - Sair\n2 - Enviar"));
           
                    }
                } catch (IOException ex) {
                    Logger.getLogger(Cliente.class.getName()).log(Level.SEVERE, null, ex);
                } catch (ClassNotFoundException ex) {
                    Logger.getLogger(Cliente.class.getName()).log(Level.SEVERE, null, ex);
                }
            
        }

        private void imprime(FileMessage message) {
            try {
                FileReader fileReader = new FileReader(message.getFile());
                BufferedReader bufferedReader = new BufferedReader(fileReader);
                
                String linha ;
                String text = "";
                while((linha = bufferedReader.readLine()) != null){
                    text += linha +"\n";
                }
                JOptionPane.showMessageDialog(null, text);
            } catch (FileNotFoundException ex) {
                Logger.getLogger(Cliente.class.getName()).log(Level.SEVERE, null, ex);
            } catch (IOException ex) {
                Logger.getLogger(Cliente.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

        private void salvar(FileMessage message) {
            try {
                
                Thread.sleep(new Random().nextInt(1000));
                
                long time = System.currentTimeMillis();
                
                FileInputStream fileInputStream = new FileInputStream(message.getFile());
                FileOutputStream fileOutputStream = new FileOutputStream("C:\\redes\\"+time+"_"+message.getFile().getName());
                
                FileChannel fin = fileInputStream.getChannel();
                FileChannel fout = fileOutputStream.getChannel();
                
                long size = fin.size();
                
                fin.transferTo(0, size, fout);
                
            } catch (FileNotFoundException ex) {
                Logger.getLogger(Cliente.class.getName()).log(Level.SEVERE, null, ex);
            } catch (IOException ex) {
                Logger.getLogger(Cliente.class.getName()).log(Level.SEVERE, null, ex);
            } catch (InterruptedException ex) {
                Logger.getLogger(Cliente.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
    
    public static void main(String[] args) {
        try {
            new Cliente();
        } catch (IOException ex) {
            Logger.getLogger(Cliente.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
