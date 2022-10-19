package data;

import object.Concert;

import java.util.ArrayList;

public class Shared {

    private ArrayList<Concert> concerts = new ArrayList<>();

    public String getActorsAndCards(){
        String out = "";
        ArrayList<Concert> tmp = new ArrayList<>();
        for(Concert concert: concerts){
            if(concert.getCards() != 0){
                tmp.add(concert);
            }
        }
        for (Concert concert : tmp) {
            if(tmp.indexOf(concert) != tmp.size()-1){
                out = out + concert.getName()+"#"+ concert.getCards()+":";
            }
            if(tmp.indexOf(concert) == tmp.size()-1){
                out = out + concert.getName()+"#"+ concert.getCards();
            }
        }
        return out;
    }
    public int getCardCount(String actorname){
        for (Concert concert: concerts){
            if(concert.getName().equals(actorname)){
                return concert.getCards();
            }
        }
        return 0;
    }

    public String getCards(int count, String actor_name){
        for (Concert concert : concerts) {
            if(concert.getName().equals(actor_name)) {
                if (concert.getCards() >= count) {
                    String out = "";
                    for(int i = count; i > 0; i--) {
                        if(i-1 == 0){
                            out = out + concert.getName().substring(0, 5) + concert.getCards();
                            concert.setCards(concert.getCards() - 1);
                        }else{
                            out = out + concert.getName().substring(0, 5) + concert.getCards() + ":";
                            concert.setCards(concert.getCards() - 1);
                        }
                    }
                    return out;
                }
                return "SOLD OUT";
            }
        }
        return "WRONG ACTOR NAME";
    }
    public void add(String actor_name, int cards){
        boolean set = false;
        for (Concert concert:concerts) {
            if(concert.getName().equals(actor_name)){
                concert.setCards(concert.getCards()+cards);
                set = true;
                break;
            }
        }
        if(!set) {
            concerts.add(new Concert(actor_name,cards));
        }
    }
}
