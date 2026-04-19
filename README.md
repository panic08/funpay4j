![FunPay4j Logo](./funpay4j.jpg)

[![DeepWiki](https://img.shields.io/badge/DeepWiki-therepanic%2Ffunpay4j-blue.svg?logo=data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAACwAAAAyCAYAAAAnWDnqAAAAAXNSR0IArs4c6QAAA05JREFUaEPtmUtyEzEQhtWTQyQLHNak2AB7ZnyXZMEjXMGeK/AIi+QuHrMnbChYY7MIh8g01fJoopFb0uhhEqqcbWTp06/uv1saEDv4O3n3dV60RfP947Mm9/SQc0ICFQgzfc4CYZoTPAswgSJCCUJUnAAoRHOAUOcATwbmVLWdGoH//PB8mnKqScAhsD0kYP3j/Yt5LPQe2KvcXmGvRHcDnpxfL2zOYJ1mFwrryWTz0advv1Ut4CJgf5uhDuDj5eUcAUoahrdY/56ebRWeraTjMt/00Sh3UDtjgHtQNHwcRGOC98BJEAEymycmYcWwOprTgcB6VZ5JK5TAJ+fXGLBm3FDAmn6oPPjR4rKCAoJCal2eAiQp2x0vxTPB3ALO2CRkwmDy5WohzBDwSEFKRwPbknEggCPB/imwrycgxX2NzoMCHhPkDwqYMr9tRcP5qNrMZHkVnOjRMWwLCcr8ohBVb1OMjxLwGCvjTikrsBOiA6fNyCrm8V1rP93iVPpwaE+gO0SsWmPiXB+jikdf6SizrT5qKasx5j8ABbHpFTx+vFXp9EnYQmLx02h1QTTrl6eDqxLnGjporxl3NL3agEvXdT0WmEost648sQOYAeJS9Q7bfUVoMGnjo4AZdUMQku50McDcMWcBPvr0SzbTAFDfvJqwLzgxwATnCgnp4wDl6Aa+Ax283gghmj+vj7feE2KBBRMW3FzOpLOADl0Isb5587h/U4gGvkt5v60Z1VLG8BhYjbzRwyQZemwAd6cCR5/XFWLYZRIMpX39AR0tjaGGiGzLVyhse5C9RKC6ai42ppWPKiBagOvaYk8lO7DajerabOZP46Lby5wKjw1HCRx7p9sVMOWGzb/vA1hwiWc6jm3MvQDTogQkiqIhJV0nBQBTU+3okKCFDy9WwferkHjtxib7t3xIUQtHxnIwtx4mpg26/HfwVNVDb4oI9RHmx5WGelRVlrtiw43zboCLaxv46AZeB3IlTkwouebTr1y2NjSpHz68WNFjHvupy3q8TFn3Hos2IAk4Ju5dCo8B3wP7VPr/FGaKiG+T+v+TQqIrOqMTL1VdWV1DdmcbO8KXBz6esmYWYKPwDL5b5FA1a0hwapHiom0r/cKaoqr+27/XcrS5UwSMbQAAAABJRU5ErkJggg==)](https://deepwiki.com/therepanic/funpay4j)
[![CI](https://github.com/therepanic/funpay4j/actions/workflows/build.yml/badge.svg)](https://github.com/therepanic/funpay4j/actions/workflows/build.yml)
[![GitHub release](https://img.shields.io/github/v/release/therepanic/funpay4j)](https://github.com/therepanic/funpay4j/releases)
[![Maven Central](https://img.shields.io/maven-central/v/io.github.therepanic.funpay4j/core)](https://central.sonatype.com/artifact/io.github.therepanic.funpay4j/core)
[![Apache License](http://img.shields.io/badge/license-ASL-blue.svg)](https://github.com/therepanic/funpay4j/blob/master/COPYING)

---

The funpay4j library is a Java wrapper for FunPay private api
## Table of contents
- [Install](#install)
    - [Requirements](#requirements)
- [Overview](#overview)
- [Usage](#usage)
    - [Terms and Conditions](#terms-and-conditions)
    - [Quick Usage](#quick-usage)
        - [Create FunPayExecutor](#create-funpayexecutor)
        - [Get seller](#get-seller)
        - [Create AuthorizedFunPayExecutor](#create-authorizedfunpayexecutor)
        - [Raise all offers](#raise-all-offers)
- [Contributing](#contributing)

# Install

#### Example for gradle:
```java
dependencies {
    implementation 'io.github.therepanic.funpay4j:core:1.0.7'
}
```

#### Example for maven:
```xml
<dependencies>
    <dependency>
        <groupId>io.github.therepanic.funpay4j</groupId>
        <artifactId>core</artifactId>
        <version>1.0.7</version>
    </dependency>
</dependencies>
```

> [!NOTE]
> If you want the latest snapshot version, use [JitPack](https://jitpack.io/#therepanic/funpay4j)

## Requirements
This project depends on
- Java 8+
- Jsoup
- OkHttpClient
- Gson
- ProjectLombok

# Overview
This Java library allows you to interact with the FunPay. Most of the functionality that is present on the site is here

# Usage
## Terms and Conditions
This library is for personal use and for educational purposes due to the fact that there is no official FunPay api at this time

- Please, do not use this library to spam (botting, spam messaging, etc...)
- Use reasonable (human) delay in between sending requests

Contributors are not responsible for usage and maintainability. Due to the nature of this project, some features of the library are not guaranteed as they make change and break in the future. This library is licensed under ASL

## Quick Usage

### Create FunPayExecutor
Basic creation of FunPayExecutor
#### *Example:*
```java
FunPayExecutor funPayExecutor = new FunPayExecutor();
```

### Get seller
Basic seller's receipt
#### *Example:*
```java
FunPayExecutor funPayExecutor = ...

User user = null;

try {
    user = funPayExecutor.execute(GetUser.builder().userId(1).build());
} catch (FunPayApiException e) {
    throw new RuntimeException(e);
}

Seller seller = null;

if (user instanceof Seller) {
    seller = (Seller) user;
}
```

### Create AuthorizedFunPayExecutor
Basic creation of AuthorizedFunPayExecutor
#### *Example:*
```java
AuthorizedFunPayExecutor funPayExecutor = new AuthorizedFunPayExecutor("test-golden-key");
```

### Raise all offers
Basic raising of all offers
#### *Example:*
```java
AuthorizedFunPayExecutor funPayExecutor = ...

try {
    funPayExecutor.execute(RaiseAllOffers.builder().gameId(41).lotId(211).build());
} catch (FunPayApiException e) {
    throw new RuntimeException(e);
} catch (OfferAlreadyRaisedException e) {
    System.out.println("The offer has already been raised!!!");
}
```

# Contributing
If you have an addition you would like to make, please do not hesitate to make a pull request!
