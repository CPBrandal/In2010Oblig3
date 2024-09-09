import java.util.ArrayList;

public class Film {
    public String filmId;
    String tittel;
    public float rating;
    int stemmer;
    public ArrayList<Skuespiller> listeSkuespillere = new ArrayList<>();

    public Film(String id, String navn, float r){
        filmId = id;
        tittel = navn;
        rating = r;
    }

    public void leggTilSkuespiller(Skuespiller s){
        listeSkuespillere.add(s);
    }

    public String toString(){
        return (tittel + " , " + rating);
    }
}
