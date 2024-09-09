import java.util.ArrayList;
import java.util.HashSet;

public class Skuespiller {
    public String nmId;
    public String navn;
    public Skuespiller forelder = null;
    public ArrayList<String> liste = new ArrayList<>();
    public HashSet<Skuespiller> naboMengde = new HashSet<Skuespiller>();

    public Skuespiller(String id, String n){
        nmId = id;
        navn = n;
    }

    public void leggTilFilm(String s){
        liste.add(s);
    }

    public void leggTilNabo(Skuespiller sk){
        naboMengde.add(sk);
    }

    public String toString(){
        return navn;
    }
}
