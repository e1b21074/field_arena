package oit.is.rumba.field_arena.model;

public class CardInfo {

    public String Attribute(int id) {
        String name = "";

        switch(id){
            case 1:
                name = "武器";
                break;
            case 2:
                name = "防具";
                break;
            case 3:
                name = "回復";
                break;
        }
        return name;
    }
}
