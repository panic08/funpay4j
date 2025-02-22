/*
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package ru.funpay4j.core;

import lombok.NonNull;
import okhttp3.OkHttpClient;
import ru.funpay4j.client.FunPayClient;
import ru.funpay4j.client.FunPayParser;
import ru.funpay4j.client.jsoup.JsoupFunPayParser;
import ru.funpay4j.client.objects.game.ParsedPromoGame;
import ru.funpay4j.client.objects.lot.ParsedLot;
import ru.funpay4j.client.objects.offer.ParsedOffer;
import ru.funpay4j.client.objects.user.*;
import ru.funpay4j.client.okhttp.OkHttpFunPayClient;
import ru.funpay4j.core.commands.offer.GetOffer;
import ru.funpay4j.core.commands.game.GetPromoGames;
import ru.funpay4j.core.commands.lot.GetLot;
import ru.funpay4j.core.commands.user.GetSellerReviews;
import ru.funpay4j.client.exceptions.FunPayApiException;
import ru.funpay4j.core.commands.user.GetUser;
import ru.funpay4j.client.exceptions.lot.LotNotFoundException;
import ru.funpay4j.client.exceptions.offer.OfferNotFoundException;
import ru.funpay4j.client.exceptions.user.UserNotFoundException;
import ru.funpay4j.core.objects.game.PromoGame;
import ru.funpay4j.core.objects.game.PromoGameCounter;
import ru.funpay4j.core.objects.lot.Lot;
import ru.funpay4j.core.objects.lot.LotCounter;
import ru.funpay4j.core.objects.offer.Offer;
import ru.funpay4j.core.objects.offer.PreviewOffer;
import ru.funpay4j.core.objects.user.*;

import java.net.Proxy;
import java.util.List;
import java.util.stream.Collectors;

/**
 * This FunPay executor is used to execute commands
 *
 * @author panic08
 * @since 1.0.0
 */
public class FunPayExecutor {
    @NonNull
    protected final FunPayParser funPayParser;

    @NonNull
    protected final FunPayClient funPayClient;

    /**
     * Creates a new FunPayExecutor instance
     */
    public FunPayExecutor() {
        OkHttpClient httpClient = new OkHttpClient();

        this.funPayParser = new JsoupFunPayParser(httpClient, FunPayURL.BASE_URL);
        this.funPayClient = new OkHttpFunPayClient(httpClient, FunPayURL.BASE_URL);
    }

    /**
     * Creates a new FunPayExecutor instance
     *
     * @param baseURL base URL of the primary server
     * @param proxy proxy for forwarding requests
     */
    public FunPayExecutor(@NonNull String baseURL, @NonNull Proxy proxy) {
        OkHttpClient httpClient = new OkHttpClient.Builder()
                .proxy(proxy)
                .build();

        this.funPayParser = new JsoupFunPayParser(httpClient, baseURL);
        this.funPayClient = new OkHttpFunPayClient(httpClient, baseURL);
    }

    /**
     * Creates a new FunPayExecutor instance
     *
     * @param baseURL base URL of the primary server
     */
    public FunPayExecutor(@NonNull String baseURL) {
        OkHttpClient httpClient = new OkHttpClient();

        this.funPayParser = new JsoupFunPayParser(httpClient, baseURL);
        this.funPayClient = new OkHttpFunPayClient(httpClient, baseURL);
    }

    /**
     * Creates a new FunPayExecutor instance
     *
     * @param proxy proxy for forwarding requests
     */
    public FunPayExecutor(@NonNull Proxy proxy) {
        OkHttpClient httpClient = new OkHttpClient.Builder()
                .proxy(proxy)
                .build();

        this.funPayParser = new JsoupFunPayParser(httpClient, FunPayURL.BASE_URL);
        this.funPayClient = new OkHttpFunPayClient(httpClient, FunPayURL.BASE_URL);
    }

    /**
     * Execute to get lot
     *
     * @param command command that will be executed
     * @return lot
     * @throws FunPayApiException if the other api-related exception
     * @throws LotNotFoundException if the lot with id does not found
     */
    public Lot execute(GetLot command) throws FunPayApiException, LotNotFoundException {
        ParsedLot parsedLot = funPayParser.parseLot(command.getLotId());
        return Lot.builder()
                .id(parsedLot.getId())
                .gameId(parsedLot.getGameId())
                .title(parsedLot.getTitle())
                .description(parsedLot.getDescription())
                .lotCounters(parsedLot.getLotCounters().stream()
                        .map(parsedLotCounter -> {
                            return LotCounter.builder()
                                    .lotId(parsedLotCounter.getLotId())
                                    .param(parsedLotCounter.getParam())
                                    .counter(parsedLotCounter.getCounter())
                                    .build();
                        })
                        .collect(Collectors.toList()))
                .previewOffers(parsedLot.getPreviewOffers().stream()
                        .map(parsedPreviewOffer -> {
                            ParsedPreviewSeller previewSeller = parsedPreviewOffer.getSeller();
                            return PreviewOffer.builder()
                                    .offerId(parsedPreviewOffer.getOfferId())
                                    .shortDescription(parsedPreviewOffer.getShortDescription())
                                    .price(parsedPreviewOffer.getPrice())
                                    .isAutoDelivery(parsedPreviewOffer.isAutoDelivery())
                                    .isPromo(parsedPreviewOffer.isPromo())
                                    .seller(PreviewSeller.builder()
                                            .userId(previewSeller.getUserId())
                                            .username(previewSeller.getUsername())
                                            .avatarPhotoLink(previewSeller.getAvatarPhotoLink())
                                            .isOnline(previewSeller.isOnline())
                                            .reviewCount(previewSeller.getReviewCount())
                                            .build())
                                    .build();
                        })
                        .collect(Collectors.toList()))
                .build();
    }

    /**
     * Execute to get promo games
     *
     * @param command command that will be executed
     * @return promo games
     * @throws FunPayApiException if the other api-related exception
     */
    public List<PromoGame> execute(GetPromoGames command) throws FunPayApiException {
        List<ParsedPromoGame> promoGame = funPayParser.parsePromoGames(command.getQuery());
        return promoGame.stream().map(parsedPromoGame -> {
            return PromoGame.builder()
                    .lotId(parsedPromoGame.getLotId())
                    .title(parsedPromoGame.getTitle())
                    .promoGameCounters(parsedPromoGame.getPromoGameCounters().stream()
                            .map(parsedPromoGameCounter -> {
                                return PromoGameCounter.builder()
                                        .lotId(parsedPromoGameCounter.getLotId())
                                        .title(parsedPromoGameCounter.getTitle())
                                        .build();
                            })
                            .collect(Collectors.toList()))
                    .build();
        }).collect(Collectors.toList());
    }

    /**
     * Execute to get offer
     *
     * @param command command that will be executed
     * @return offer
     * @throws FunPayApiException if the other api-related exception
     * @throws OfferNotFoundException if the offer with id does not found
     */
    public Offer execute(GetOffer command) throws FunPayApiException, OfferNotFoundException {
        ParsedOffer offer = funPayParser.parseOffer(command.getOfferId());
        return Offer.builder()
                .id(offer.getId())
                .shortDescription(offer.getShortDescription())
                .detailedDescription(offer.getDetailedDescription())
                .isAutoDelivery(offer.isAutoDelivery())
                .price(offer.getPrice())
                .attachmentLinks(offer.getAttachmentLinks())
                .parameters(offer.getParameters())
                .seller(PreviewSeller.builder()
                        .userId(offer.getSeller().getUserId())
                        .username(offer.getSeller().getUsername())
                        .avatarPhotoLink(offer.getSeller().getAvatarPhotoLink())
                        .isOnline(offer.getSeller().isOnline())
                        .reviewCount(offer.getSeller().getReviewCount())
                        .build())
                .build();

    }

    /**
     * Execute to get user
     *
     * @param command command that will be executed
     * @return user
     * @throws FunPayApiException if the other api-related exception
     * @throws UserNotFoundException if the user with id does not found
     */
    public User execute(GetUser command) throws FunPayApiException, UserNotFoundException {
        ParsedUser user = funPayParser.parseUser(command.getUserId());
        if (user instanceof ParsedSeller) {
            return Seller.builder()
                    .id(user.getId())
                    .username(user.getUsername())
                    .avatarPhotoLink(user.getAvatarPhotoLink())
                    .isOnline(user.isOnline())
                    .badges(user.getBadges())
                    .lastSeenAt(user.getLastSeenAt())
                    .registeredAt(user.getRegisteredAt())
                    .rating(((ParsedSeller) user).getRating())
                    .reviewCount(((ParsedSeller) user).getReviewCount())
                    .previewOffers(((ParsedSeller) user).getPreviewOffers().stream().map(parsedPreviewOffer -> {
                        ParsedPreviewSeller previewSeller = parsedPreviewOffer.getSeller();
                        return PreviewOffer.builder()
                                .offerId(parsedPreviewOffer.getOfferId())
                                .shortDescription(parsedPreviewOffer.getShortDescription())
                                .price(parsedPreviewOffer.getPrice())
                                .isAutoDelivery(parsedPreviewOffer.isAutoDelivery())
                                .isPromo(parsedPreviewOffer.isPromo())
                                .seller(PreviewSeller.builder()
                                        .userId(previewSeller.getUserId())
                                        .username(previewSeller.getUsername())
                                        .avatarPhotoLink(previewSeller.getAvatarPhotoLink())
                                        .isOnline(previewSeller.isOnline())
                                        .reviewCount(previewSeller.getReviewCount())
                                        .build())
                                .build();
                    }).collect(Collectors.toList()))
                    .lastReviews(((ParsedSeller) user).getLastReviews().stream().map(parsedSellerReview -> {
                        return SellerReview.builder()
                                .gameTitle(parsedSellerReview.getGameTitle())
                                .price(parsedSellerReview.getPrice())
                                .text(parsedSellerReview.getText())
                                .stars(parsedSellerReview.getStars())
                                .sellerReplyText(parsedSellerReview.getSellerReplyText())
                                .build();
                    }).collect(Collectors.toList()))
                    .build();
        } else {
            return User.builder()
                    .id(user.getId())
                    .username(user.getUsername())
                    .avatarPhotoLink(user.getAvatarPhotoLink())
                    .isOnline(user.isOnline())
                    .badges(user.getBadges())
                    .lastSeenAt(user.getLastSeenAt())
                    .registeredAt(user.getRegisteredAt())
                    .build();
        }
    }

    /**
     * Execute to get seller reviews
     *
     * @param command command that will be executed
     * @return seller reviews
     * @throws FunPayApiException if the other api-related exception
     * @throws UserNotFoundException if the user with id does not found/seller
     */
    public List<SellerReview> execute(GetSellerReviews command) throws FunPayApiException, UserNotFoundException {
        List<ParsedSellerReview> sellerReviews;
        if (command.getStarsFilter() != null) {
            sellerReviews = funPayParser.parseSellerReviews(command.getUserId(), command.getPages(), command.getStarsFilter());
        } else {
            sellerReviews = funPayParser.parseSellerReviews(command.getUserId(), command.getPages());
        }
        return sellerReviews.stream().map(parsedSellerReview -> {
            if (parsedSellerReview instanceof ParsedAdvancedSellerReview) {
                return AdvancedSellerReview.builder()
                        .senderUserId(((ParsedAdvancedSellerReview) parsedSellerReview).getSenderUserId())
                        .senderUsername(((ParsedAdvancedSellerReview) parsedSellerReview).getSenderUsername())
                        .senderAvatarLink(((ParsedAdvancedSellerReview) parsedSellerReview).getSenderAvatarLink())
                        .orderId(((ParsedAdvancedSellerReview) parsedSellerReview).getOrderId())
                        .createdAt(((ParsedAdvancedSellerReview) parsedSellerReview).getCreatedAt())
                        .gameTitle(parsedSellerReview.getGameTitle())
                        .price(parsedSellerReview.getPrice())
                        .text(parsedSellerReview.getText())
                        .stars(parsedSellerReview.getStars())
                        .sellerReplyText(parsedSellerReview.getSellerReplyText())
                        .build();
            } else {
                return SellerReview.builder()
                        .gameTitle(parsedSellerReview.getGameTitle())
                        .price(parsedSellerReview.getPrice())
                        .text(parsedSellerReview.getText())
                        .stars(parsedSellerReview.getStars())
                        .sellerReplyText(parsedSellerReview.getSellerReplyText())
                        .build();
            }
        }).collect(Collectors.toList());
    }
}
