![FunPay4j Logo](./funpay4j.jpg)

[![Apache License](http://img.shields.io/badge/license-ASL-blue.svg)](https://github.com/panic08/funpay4j/blob/master/COPYING)
[![PRs Welcome](https://img.shields.io/badge/PRs-welcome-brightgreen.svg)](https://legacy.reactjs.org/docs/how-to-contribute.html#your-first-pull-request)

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
- [Contributing](#contributing)

# Install

#### Example for gradle:
```java
repositories {
    maven {
        url 'https://jitpack.io'
    }
}

dependencies {
    implementation 'com.github.panic08:funpay4j:1.0.0'
}
```

#### Example for maven:
```xml
<repositories>
    <repository>
        <id>jitpack</id>
        <url>https://jitpack.io</url>
    </repository>
</repositories>

<dependencies>
<dependency>
    <groupId>com.github.panic08</groupId>
    <artifactId>funpay4j</artifactId>
    <version>1.0.1</version>
</dependency>
</dependencies>
```

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

User user = funPayExecutor.execute(GetUser.builder().userId(1).build());

Seller seller = null;
        
if (user instanceof Seller) {
    seller = (Seller) user;
}
```

# Contributing
If you have an addition you would like to make, please do not hesitate to make a pull request!