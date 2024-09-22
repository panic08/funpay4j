package ru.funpay4j.client;

import ru.funpay4j.core.commands.lot.GetLot;
import ru.funpay4j.core.objects.lot.Lot;

public interface FunPayParser {

    //Specific parsing requests
    Lot parse(GetLot command);
}
