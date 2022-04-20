package ma.cdgk.integration.camel.mapping;

import ma.cdgk.domain.events.bancaire.AmortissementCreditEvent;
import ma.cdgk.event.repo.producers.amortissement.Amortissement;
import org.springframework.stereotype.Service;

@Service
public class MapAmortissementCreditEventToXmlAmortissement
        implements EventMapping<
                AmortissementCreditEvent, Amortissement> {

    @Override
    public Amortissement map(AmortissementCreditEvent amortissementCreditEvent) {
        Amortissement amortissement = new Amortissement();
        amortissement.setCodeAgence(amortissement.getCodeAgence());
        amortissement.setComptePcec(amortissementCreditEvent.getComptePcec());
        amortissement.setDateArrete(amortissementCreditEvent.getDateArrete());
        amortissement.setEventType(amortissementCreditEvent.getEventType());
        amortissement.setDateCalcul(amortissementCreditEvent.getDateCalcul());
        amortissement.setDevise(amortissementCreditEvent.getDevise());
        amortissement.setIdentifiantClientEspece(
                amortissementCreditEvent.getIdentifiantClientEspece());
        amortissement.setNomAgence(amortissementCreditEvent.getNomAgence());
        return amortissement;
    }
}
