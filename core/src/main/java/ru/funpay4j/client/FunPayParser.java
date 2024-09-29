package ru.funpay4j.client;

import ru.funpay4j.core.methods.offer.GetOffer;
import ru.funpay4j.core.methods.game.GetPromoGames;
import ru.funpay4j.core.methods.lot.GetLot;
import ru.funpay4j.core.objects.game.PromoGame;
import ru.funpay4j.core.objects.lot.Lot;
import ru.funpay4j.core.objects.offer.Offer;

import java.util.List;

public interface FunPayParser {

    //Specific parsing requests
    Lot parse(GetLot command);

    List<PromoGame> parse(GetPromoGames command);

    Offer parse(GetOffer command);
}
