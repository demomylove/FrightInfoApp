# Flight Info Android App

A real-time flight information query application built with Android MVVM architecture, providing users with up-to-date flight status, schedules, and details.

## 🚀 Features

- **Real-time Flight Tracking**: Get live updates on flight status, delays, and gate changes
- **Flight Search**: Search flights by flight number, route, or airline
- **Modern UI**: Clean, intuitive Material Design interface
- **Offline Support**: Mock data fallback when API is unavailable
- **MVVM Architecture**: Clean, scalable, and maintainable code structure
- **Pull-to-Refresh**: Easy data refresh functionality

## 📱 App Structure

### Main Components
- Search flights by flight number (e.g., CA1234, MU5678)
- View real-time flight updates with automatic refresh
- Detailed flight information including gates, terminals, and aircraft type
- Status indicators with color-coded labels (On Time, Delayed, Cancelled, Boarding, etc.)
- Swipe-to-refresh functionality for latest data

## 🏗️ Architecture

This app follows the **MVVM (Model-View-ViewModel)** architecture pattern:

### Data Layer
- [`FlightInfo`](app/src/main/java/com/flightinfo/app/data/model/FlightInfo.kt): Flight data models
- [`FlightApiService`](app/src/main/java/com/flightinfo/app/data/api/FlightApiService.kt): Retrofit API interface
- [`FlightRepository`](app/src/main/java/com/flightinfo/app/data/repository/FlightRepository.kt): Single source of truth for data

### Presentation Layer
- [`FlightSearchViewModel`](app/src/main/java/com/flightinfo/app/ui/viewmodel/FlightSearchViewModel.kt): Business logic and state management
- [`MainActivity`](app/src/main/java/com/flightinfo/app/ui/MainActivity.kt): Main activity with search and tabs
- [`FlightListFragment`](app/src/main/java/com/flightinfo/app/ui/fragment/FlightListFragment.kt): Flight list display
- [`FlightAdapter`](app/src/main/java/com/flightinfo/app/ui/adapter/FlightAdapter.kt): RecyclerView adapter

### Dependency Injection
- [`NetworkModule`](app/src/main/java/com/flightinfo/app/di/NetworkModule.kt): Hilt DI setup for networking

## 🛠️ Technology Stack

- **Language**: Kotlin
- **Architecture**: MVVM + Repository Pattern
- **UI**: Material Design Components, ViewBinding
- **Networking**: Retrofit + OkHttp + Gson
- **Async**: Coroutines + Flow
- **DI**: Hilt (Dagger)
- **UI Navigation**: ViewPager2 + TabLayout

## 📦 Key Files

```
FlightInfoApp/
├── app/
│   ├── build.gradle                 # App dependencies and configuration
│   └── src/main/
│       ├── AndroidManifest.xml      # App permissions and components
│       ├── java/com/flightinfo/app/
│       │   ├── FlightInfoApplication.kt
│       │   ├── data/
│       │   │   ├── api/FlightApiService.kt
│       │   │   ├── model/FlightInfo.kt
│       │   │   └── repository/FlightRepository.kt
│       │   ├── di/NetworkModule.kt
│       │   ├── ui/
│       │   │   ├── MainActivity.kt
│       │   │   ├── adapter/
│       │   │   ├── fragment/
│       │   │   └── viewmodel/
│       │   └── utils/Resource.kt
│       └── res/
│           ├── drawable/           # Icons and backgrounds
│           ├── layout/            # XML layouts
│           └── values/           # Colors, strings, themes
├── build.gradle                  # Project configuration
├── gradle.properties           # Gradle settings
└── settings.gradle             # Module settings
```

## ⚙️ Setup and Installation

### Prerequisites
- Android Studio Hedgehog (2023.1.1) or later
- Android SDK 21+ (Android 5.0+)
- Kotlin 1.9.0+
- Java 8+

### Installation Steps

1. **Import Project**
   ```bash
   # Open Android Studio
   # File → Open → Select FlightInfoApp folder
   ```

2. **Build Project**
   ```bash
   # In Android Studio terminal:
   ./gradlew build
   ```

3. **Run Application**
   - Connect Android device or start emulator
   - Click Run button (Shift+F10) or:
   ```bash
   ./gradlew installDebug
   ```

## 🔧 Configuration

### Mock Data
The app currently uses mock flight data for demonstration. Key mock flights include:

- **CA1234**: Air China (PEK → SHA) - On Time
- **MU5678**: China Eastern (SHA → SZX) - Delayed 30 min
- **CZ9012**: China Southern (CAN → PEK) - Boarding
- **CA1001**: Air China (PEK → LAX) - In Flight
- **UA857**: United Airlines (SFO → NRT) - On Time

### API Integration
To integrate with real flight APIs:

1. Update [`FlightApiService`](app/src/main/java/com/flightinfo/app/data/api/FlightApiService.kt) endpoints
2. Add API keys in [`NetworkModule`](app/src/main/java/com/flightinfo/app/di/NetworkModule.kt)
3. Replace mock data in [`FlightRepository`](app/src/main/java/com/flightinfo/app/data/repository/FlightRepository.kt)

## 📋 Usage

### Search Flights
1. Open the app
2. Enter flight number (e.g., "CA1234")
3. Tap "Search Flights"
4. View results in "Search Results" tab

### Real-time Flights
1. Switch to "Real-time" tab
2. View live flight updates
3. Pull down to refresh data

### Flight Details
Each flight card shows:
- Flight number and airline
- Departure/arrival times and airports
- Current status (color-coded)
- Gate and terminal information
- Aircraft type

## 🎨 UI Components

### Status Colors
- 🟢 **On Time**: Green (`#4CAF50`)
- 🟡 **Delayed**: Orange (`#FF9800`)
- 🔴 **Cancelled**: Red (`#F44336`)
- 🔵 **Boarding**: Blue (`#2196F3`)
- 🟣 **Departed/In Flight**: Purple (`#9C27B0`)

### Layout Features
- Material Design 3 components
- Responsive RecyclerView with cards
- SwipeRefreshLayout for pull-to-refresh
- TabLayout with ViewPager2
- Loading states and error handling

## 🧪 Testing

The app includes comprehensive error handling:
- Network connectivity issues
- Empty search results
- API timeout scenarios
- Invalid flight numbers

## 📱 Device Compatibility

- **Minimum SDK**: API 21 (Android 5.0)
- **Target SDK**: API 34 (Android 14)
- **Screen Sizes**: Phones and tablets
- **Orientation**: Portrait and landscape

## 🚀 Future Enhancements

- Flight booking integration
- Push notifications for flight updates
- Favorite flights list
- Offline caching with Room database
- Flight route maps
- Price tracking
- Multiple language support

## 🤝 Contributing

1. Fork the repository
2. Create feature branch (`git checkout -b feature/amazing-feature`)
3. Commit changes (`git commit -m 'Add amazing feature'`)
4. Push to branch (`git push origin feature/amazing-feature`)
5. Open Pull Request

## 📄 License

This project is licensed under the MIT License - see the LICENSE file for details.

## 🙏 Acknowledgments

- Material Design Components
- Aviation Stack API (for future integration)
- Open source Android community

---

**Built with ❤️ using Android MVVM Architecture**