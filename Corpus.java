import java.util.List;

public class Corpus {
    private List<String> textes;

    public Corpus(List<String> textes) {
        this.textes = textes;
    }

    public List<String> getTextes() {
        return textes;
    }
}