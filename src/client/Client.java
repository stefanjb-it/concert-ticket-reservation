package client;

import object.Concert;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Arrays;

public class Client {

    private Socket socket;
    private int port;
    private String address;
    private BufferedReader in;
    private PrintWriter out;
    private ArrayList<Concert> myCards = new ArrayList<>();

    public Client(String host, int port){
        this.address = host;
        this.port = port;
    }

    private void connect(){
        try {
            socket = new Socket(address,port);
            this.in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            this.out = new PrintWriter(socket.getOutputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public ArrayList<Concert> getConcerts() throws IOException, InterruptedException {
        ArrayList <Concert> output = new ArrayList<>();
        if (socket == null){
            connect();
        }
        out.println("GET CONCERTS");
        out.flush();
        String input = in.readLine();
        String[] splitted_in = input.split(":");
        for (String s : splitted_in) {
            output.add(new Concert(s.split("#")[0], Integer.parseInt(s.split("#")[1])));
        }
        return output;
    }

    public ArrayList<String> getTickets (Concert concert, int count) throws IOException {
        ArrayList<String> output = new ArrayList<>();
        if (socket == null){
            connect();
        }else{
            out.println("GET TICKETS "+concert.getName()+" "+count);
            out.flush();
            String input = in.readLine();
            if(!input.equals("SOLD OUT") && !input.equals("WRONG ACTOR NAME")){
                String[] splitted = input.split(":");
                for(int i = 0;i < splitted.length;i++){
                    System.out.println(splitted[i]);
                }
                output.addAll(Arrays.asList(splitted));
                myCards.add(new Concert(concert.getName(),count));
                concert.setCards(count);
                return output;
            }else{
                output.add(input);
                return output;
            }
        }
        return null;
    }

    public String returnTickets(Concert concert, int count) throws IOException {
        if (socket == null){
            connect();
        }else{
            for(Concert c:myCards){
                if(c.getName().equals(concert.getName())){
                    if(count <= concert.getCards()){
                        out.println("RETURN TICKETS "+c.getName()+" "+count);
                        out.flush();
                        return in.readLine();
                    }
                }
            }
        }
        return null;
    }

    /*public static void main(String[] args) throws IOException, InterruptedException {
        Client cl = new Client("localhost",15500);
        cl.connect();
        cl.getConcerts();
        Concert c = new Concert("Logic",0);
        ArrayList <String> outh = cl.getTickets(c,1);
        for (String s:outh){
            System.out.println(s);
        }
        System.out.println(cl.returnTickets(c,2));
    }*/
}
