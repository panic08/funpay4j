package ru.funpay4j.client;

import ru.funpay4j.core.commands.offer.GetOffer;
import ru.funpay4j.core.commands.game.GetPromoGames;
import ru.funpay4j.core.commands.lot.GetLot;
import ru.funpay4j.core.commands.user.GetUser;
import ru.funpay4j.core.objects.game.PromoGame;
import ru.funpay4j.core.objects.lot.Lot;
import ru.funpay4j.core.objects.offer.Offer;
import ru.funpay4j.core.objects.user.User;

import java.util.List;

public interface FunPayParser {

    //Specific parsing requests
    Lot parse(GetLot command);

    List<PromoGame> parse(GetPromoGames command);

    Offer parse(GetOffer command);

    User parse(GetUser command);
}
