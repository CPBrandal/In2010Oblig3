public class test {
    public static void main(String[] args) {
        String s = "nm0000313	Jeff Bridges	tt0371746";
        String[] sL = s.split("\t");
        for(String str : sL){
            System.out.println(str);
        }
    }
}
