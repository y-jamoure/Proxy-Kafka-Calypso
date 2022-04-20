package ma.cdgk.integration.normalizer;

import ma.cdgk.domain.events.bancaire.AmortissementCreditEvent;
import ma.cdgk.integration.model.Event;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

@Service
public class AmortissementCreditNormalizer
        implements EventNormalizer<Event, AmortissementCreditEvent> {

    @Override
    public AmortissementCreditEvent normalize(Event event) {
        AmortissementCreditEvent amortissementCredit = new AmortissementCreditEvent();
        amortissementCredit.setEventType("amortissement");
        amortissementCredit.setDateArrete(LocalDate.now().toString());
        amortissementCredit.setVersion("1");
        amortissementCredit.setDateCalcul(LocalDate.now().toString());
        amortissementCredit.setDevise("MAD");
        amortissementCredit.setComptePcec("621654");
        amortissementCredit.setIdentifiantClientEspece("54165");
        amortissementCredit.setNumeroCompteEspece("32132132");
        amortissementCredit.setMontantEcheance("123.212");
        amortissementCredit.setMontantInteret("12.0");
        amortissementCredit.setDateFinTombee(LocalDateTime.now().plusDays(5).toString());
        amortissementCredit.setCodeAgence("001");
        amortissementCredit.setNomAgence(event.getEventType());
        amortissementCredit.setReferenceCredit("98794");
        amortissementCredit.setIdentifiantTechnique(UUID.randomUUID().toString());
        return amortissementCredit;
    }
}
