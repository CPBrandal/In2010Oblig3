import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.PriorityQueue;

class Graf{
    HashSet<Skuespiller> skueSpillerMengde = new HashSet<Skuespiller>();
    ArrayList<Kant> kantMengde = new ArrayList<Kant>();
    ArrayList<Film> filmListe = new ArrayList<>();
    HashMap <String, Film> hash1 = new HashMap<String, Film>();
    HashMap <String, ArrayList<Skuespiller>> hash2 = new HashMap<String, ArrayList<Skuespiller>>();
    HashMap<Skuespiller, ArrayList<Skuespiller>> naboListe = new HashMap<>();
    HashMap<String, Skuespiller> skuespillere = new HashMap<String, Skuespiller>();
    int antNoder = skueSpillerMengde.size();

    class Kant implements Comparable<Kant>{
        Skuespiller kilde;
        Skuespiller slutt;
        Film film;
        public float tyngde;

        Kant(Skuespiller k, Skuespiller s, Film f){
            kilde = k;
            slutt = s;
            film = f;
            tyngde = f.rating;
        }

        public String toString(){
            return kilde + " ====> " + slutt + " " + film;
        }
        
        @Override public int compareTo(Kant k){ // for å printe ut kantene alfabetisk
            return (this.kilde.navn).compareTo(k.kilde.navn);
        }
    }

    void lesFraFilFilmer(String filnavn) throws FileNotFoundException, IOException {
        BufferedReader leser = new BufferedReader(new FileReader(filnavn + ".tsv"));
        String les;
        while((les = leser.readLine()) != null){
            String[] leseliste = les.split("\t");
            Film f = new Film(leseliste[0], leseliste[1], Float.parseFloat(leseliste[2]));
            filmListe.add(f);
            hash1.put(f.filmId, f);
            hash2.put(f.filmId, new ArrayList<>());
        }
        leser.close();
    }

    void lesFraFilSkuespiller(String filnavn) throws FileNotFoundException, IOException {
        BufferedReader leser = new BufferedReader(new FileReader(filnavn + ".tsv"));
        String les;
        while((les = leser.readLine()) != null){
            String[] leseliste = les.split("\t");
            Skuespiller sk = new Skuespiller(leseliste[0], leseliste[1]);
            for(int x = 2; x < leseliste.length; x++){
                sk.leggTilFilm(leseliste[x]);
                if(hash2.containsKey(leseliste[x])) {
                    hash2.get(leseliste[x]).add(sk);
                }
            }
            skueSpillerMengde.add(sk);
            skuespillere.put(sk.nmId, sk);
        }
        leser.close();
    } 

    public void fikseKanter(){
        for (Film f : filmListe) {
            List<Skuespiller> sk1 = hash2.get(f.filmId);

            for (int x = 0; x < sk1.size() - 1; x++) {
                for (int y = x + 1; y < sk1.size(); y++) {
                    kantMengde.add(new Kant(sk1.get(x), sk1.get(y), f));
                    sk1.get(x).leggTilNabo(sk1.get(y));
                    sk1.get(y).leggTilNabo(sk1.get(x));
                }
            }
        }
    }

    public void BFSFull(){
        HashSet<Skuespiller> besøkt = new HashSet<Skuespiller>();
        for(Skuespiller s: skueSpillerMengde){
            if(!besøkt.contains(s)){
                BFSBesøk(s, besøkt, null);
            }
        }
    }

    public void BFSBesøk(Skuespiller s, HashSet<Skuespiller> besøkt, Skuespiller s2){
        besøkt.add(s);
        LinkedList <Skuespiller> koe = new LinkedList<Skuespiller>();
        koe.add(s);
        while (koe.size() != 0){
            Skuespiller u = koe.pop();
            for(Skuespiller sk : u.naboMengde){
                if(!besøkt.contains(sk)){
                    besøkt.add(sk);
                    koe.add(sk);
                }
            }
        }
    }

    public Skuespiller BFSSøk(Skuespiller s, HashSet<Skuespiller> besøkt, Skuespiller s2){
        besøkt.add(s);
        LinkedList <Skuespiller> koe = new LinkedList<Skuespiller>();
        koe.add(s);
        while (koe.size() != 0){
            Skuespiller u = koe.pop();
            for(Skuespiller sk : u.naboMengde){
                if(!besøkt.contains(sk)){
                    sk.forelder = u;
                    besøkt.add(sk);
                    koe.add(sk);
                }
                if(sk == s2){
                    return sk;
                }
            }
        }
        return null;
    }

    public void finnKortesteVei(String s1, String s2){
        HashSet<Skuespiller> set = new HashSet<Skuespiller>();
        Skuespiller besoekt = BFSSøk(skuespillere.get(s1), set, skuespillere.get(s2));
        if(besoekt == null){
            System.out.println("Fant ikke link mellom skuespillere");
            return;
        }
        ArrayList<Skuespiller> l = new ArrayList<>();
        Skuespiller s = besoekt;
        boolean bl = true;
        while(bl){
            l.add(0, s);
            s = s.forelder;
            if(s.forelder == null){
                l.add(0, s);
                bl = false;
            }
        }
        this.printUtKant(l);
    }

    public void printUtKant(ArrayList<Skuespiller> sL){
        System.out.println(sL.get(0));
        for(int x = 0; x < sL.size()-1; x++){
            for(Kant k : kantMengde){
                if(k.kilde.equals(sL.get(x)) && k.slutt.equals(sL.get(x+1)) || k.kilde.equals(sL.get(x+1)) && k.slutt.equals(sL.get(x))){
                    System.out.println("===>" + "[" + k.film + "]" + "===>");
                    break;
                }
            }
            System.out.println(sL.get(x+1));
        }
    }

    public void dijkstra(Graf g, Skuespiller s){
        PriorityQueue<Skuespiller> pQ = new PriorityQueue<>();
        pQ.add(s);
        HashMap<Skuespiller, Float> dist = new HashMap<Skuespiller, Float>();
        for(Skuespiller sk1 : skueSpillerMengde){
            dist.put(sk1, 100f);
        }
        dist.put(s, 0f);
        while (pQ.size() != 0){
            Skuespiller u = pQ.poll();
            for(Skuespiller sk2 : u.naboMengde){
                Kant kant = new Kant(null, null, null);
                for(Kant k1 : kantMengde){
                    if(k1.kilde.equals(u) && k1.slutt.equals(sk2) || k1.kilde.equals(sk2) && k1.slutt.equals(u)){ 
                        kant = k1; // Kommer til å bli endret
                        break;
                    }
                }
                float c = dist.get(u) + kant.tyngde;
                if(c < dist.get(sk2)){
                    dist.put(sk2, c);
                    pQ.add(sk2);
                }
            }
        }
    }

    public static void main(String[] args) throws FileNotFoundException, IOException {
        Graf graf = new Graf();
        graf.lesFraFilFilmer("movies");
        graf.lesFraFilSkuespiller("actors");
        graf.fikseKanter();
        System.out.println(graf.skueSpillerMengde.size());
        System.out.println(graf.kantMengde.size());
        System.out.println(graf.filmListe.size());
        graf.finnKortesteVei("nm2255973","nm0000460");
        /* System.out.println(graf.skuespillere.get("nm0000460"));
        System.out.println(graf.skuespillere.get("nm2255973")); */
    }
}