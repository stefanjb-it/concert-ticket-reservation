package server;

import data.Shared;
import object.Concert;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;

public class ServerThread extends Thread {

    private final Socket socket;
    private final Shared shared;
    private final boolean active = true;
    private final ArrayList<Concert> concerts = new ArrayList<>();

    public ServerThread(Socket socket, Shared shared) {
        this.socket = socket;
        this.shared = shared;
    }

    public void run() {
        try {
            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            PrintWriter out = new PrintWriter(socket.getOutputStream());

            work(in, out);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void work(BufferedReader in, PrintWriter out) throws IOException {
        String line;
        String name;
        String out_s;
        String cmd = "";
        while ((line = in.readLine()) != null || active) {
            for (int i = 0; i < concerts.size();i++) {
                if(concerts.get(i).getCards() == 0){
                    concerts.remove(i);
                }
            }
            try {
                cmd = line.split(" ")[0] + " " + line.split(" ")[1];
            } catch (ArrayIndexOutOfBoundsException ignored) {

            }
            String[] all = line.split(" ");
            switch (cmd) {
                case "GET CONCERTS":
                    out.println(shared.getActorsAndCards());
                    out.flush();
                    break;
                case "GET TICKETS":
                    name = line.substring(cmd.length() + 1, line.length() - 2);
                    out_s = shared.getCards(Integer.parseInt(all[all.length - 1]), name);
                    if (!out_s.equals("SOLD OUT") && !out_s.equals("WRONG ACTOR NAME")) {
                        concerts.add(new Concert(name, Integer.parseInt(all[all.length - 1])));
                    }
                    out.println(out_s);
                    out.flush();
                    break;
                case "RETURN TICKETS":
                    out_s = "";
                    name = line.substring(cmd.length() + 1, line.length() - 2);
                    for (Concert concert : concerts) {
                        if (concert.getName().equals(name)) {
                            out_s = name.substring(0, 5) + shared.getCardCount(name);
                            shared.add(name, Integer.parseInt(all[all.length - 1]));
                            concert.setCards(concert.getCards() - Integer.parseInt(all[all.length - 1]));
                            out_s = out_s + ":" + name + shared.getCardCount(name);
                            out.println(out_s);
                            out.flush();
                            break;
                        }
                    }
                    break;
            }
        }
    }
}
