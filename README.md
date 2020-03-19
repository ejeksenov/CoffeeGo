# CoffeeGo

CoffeeGo is used to pay by scanning QR Code and get for this cashback.

## Introduction

  This project was written by solid principles of Clean Architecture and divided into three layers:
  
  1. Data layer contains Firebase Auth(authentification by email), Cloud Firestore(for reading and writing data) and Firebase Storage(for uploading images) implementation.
  
  2. Domain layer contains UseCases, Domain Objects/Models (Kotlin Data Classes), and Repository Interfaces.
  
  3. Presentation layer contains UI, View Objects, Android components, etc. This layer implemented MVVM and used Koin for dependency injection.

## Libraries used

* [Navigation Component](https://developer.android.com/guide/navigation/navigation-getting-started) -  A component that helps to navigate between fragments and activities 
* [ZXing](https://github.com/dm77/barcodescanner) - A barcode and qrcode scanner library
* [Glide](https://github.com/bumptech/glide) - An image loading and caching library for Android
* [Kotlin Coroutines](https://developer.android.com/kotlin/coroutines) - A concurrency design pattern 
* [Koin](https://github.com/InsertKoinIO/koin) - a pragmatic lightweight dependency injection framework for Kotlin

## Screenshots
![](https://raw.github.com/ejeksenov/CoffeeGo/master/coffego_screenshots.png)

## License

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.
