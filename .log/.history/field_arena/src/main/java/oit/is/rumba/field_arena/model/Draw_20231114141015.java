package oit.is.rumba.field_arena.model;

import oit.is.rumba.field_arena.model.*;
import java.util.ArrayList;
import java.util.Random;

public class Draw {
    
    private ArrayList<Card> handList = new ArrayList<Card>();
    private Card hand = null;

    public Card getHand(ArrayList<Card> cards) {
        CardDraw(cards);
        return hand;
    }

    public void CardDraw(ArrayList<Card> cards) {
        Random rand = new Random();
        int index = rand.nextInt(cards.size());
        Card card = cards.get(index);
        drawCard(card);
    }

    private void drawCard(Card card) {
        this.hand = card;
    }

    public ArrayList<Card> getHandList() {
        return handList;
    }

    public void setHandList(ArrayList<Card> handList) {
        this.handList = handList;
    }
           
}
