import java.util.List;

public interface ConvertisseurMouvements {
    List<Mouvement> convertir(String ngramme, DispositionClavier disposition);
}