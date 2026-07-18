# 🏃 FitTrack Pro

![Android](https://img.shields.io/badge/Platform-Android-green?style=for-the-badge&logo=android)
![Kotlin](https://img.shields.io/badge/Language-Kotlin-purple?style=for-the-badge&logo=kotlin)
![Jetpack Compose](https://img.shields.io/badge/UI-Jetpack%20Compose-blue?style=for-the-badge)
![Firebase](https://img.shields.io/badge/Backend-Firebase-orange?style=for-the-badge&logo=firebase)
![Material3](https://img.shields.io/badge/Design-Material%203-lightblue?style=for-the-badge)
![Status](https://img.shields.io/badge/Status-Completed-brightgreen?style=for-the-badge)

> **A professional production-quality Android Fitness Tracker App**
> Built with Jetpack Compose, Material Design 3, Firebase & Room Database

---

## 📱 App Preview

| Splash Screen | Dashboard | Activity Log | Progress |
|:---:|:---:|:---:|:---:|
| *(Add Screenshot)* | *(Add Screenshot)* | *(Add Screenshot)* | *(Add Screenshot)* |

| Login | Register | BMI Calculator | Profile |
|:---:|:---:|:---:|:---:|
| *(Add Screenshot)* | *(Add Screenshot)* | *(Add Screenshot)* | *(Add Screenshot)* |

> 📸 To add screenshots: Run the app on emulator → Press `Ctrl+S` in Android Studio → Upload images to `/screenshots` folder in your repo and replace the placeholders above.

---

## ✨ Features

### 🔐 Authentication
- Email & Password Login
- New User Registration with name, email, password
- Forgot Password via email reset link
- Form validation with error messages
- Beautiful gradient login & register screens

### 🏠 Dashboard (Home)
- Personalized greeting — Good Morning / Afternoon / Evening
- Displays logged-in user's real name
- Large animated circular step progress ring
- 4 gradient metric cards — Steps, Calories, Workout Time, Water
- Recent activities list for today
- Daily motivational quote (random)
- FAB button to quickly log a new activity

### 🏋️ Activity Logging
- 8 Exercise types — Walking, Running, Cycling, Gym, Yoga, Swimming, HIIT, Strength Training
- Log: workout name, type, duration, calories burned, distance, notes, date, time
- Swipe left on any workout to delete it
- Search bar to filter all workouts instantly

### 📊 Progress Analytics
- All-time stats — Total Workouts, Total Calories, Longest Session, Average Time
- Weekly steps bar chart (last 7 days)
- Weekly calorie progress with goal indicator
- Achievements section shortcut

### 🏅 Achievements
- 8 Badges to unlock:
    - 💪 First Workout
    - 🔥 7 Day Streak
    - 🏅 10 Workouts
    - 💯 100 Workouts
    - 🚶 50,000 Steps
    - 🎯 Goal Achiever
    - 💧 Hydration Hero
    - ⚡ HIIT Master
- Locked badges shown grayed out
- Unlocked badges shown with green highlight

### 💧 Water Tracker
- Track glasses of water throughout the day
- Animated circular progress ring
- Add glass / Remove glass buttons
- Goal completion celebration message
- Visual glass-by-glass indicator

### ⚖️ BMI Calculator
- Enter height (cm) and weight (kg)
- Instant BMI result with one decimal
- Color-coded category card:
    - 🔵 Underweight — BMI < 18.5
    - 🟢 Normal — BMI 18.5 to 24.9
    - 🟠 Overweight — BMI 25.0 to 29.9
    - 🔴 Obese — BMI > 30.0
- Full BMI scale reference table

### 👤 Profile
- Displays user avatar, name, email
- Total workouts and calories stats
- Quick links: BMI Calculator, Settings, Help, Privacy, About
- One-tap Logout

### ✏️ Edit Profile
- Edit full name, height, weight, age, gender
- Select fitness goal from list
- Save changes button

### ⚙️ Settings
- Dark Mode toggle (saves preference)
- Workout Reminders toggle
- Water Reminder toggle
- Metric / Imperial units toggle
- Logout option

---

## 🎨 Design Highlights

| Element | Value |
|---------|-------|
| **Primary Color** | Deep Blue `#2563EB` |
| **Secondary Color** | Emerald Green `#22C55E` |
| **Accent Color** | Orange `#F97316` |
| **Background (Light)** | `#F8FAFC` |
| **Background (Dark)** | `#0F172A` |
| **Card Style** | Rounded corners 20–24dp, soft elevation |
| **Typography** | Poppins (Regular, Medium, SemiBold, Bold) |
| **Animations** | Circular ring animation, fade + slide navigation, scale effects |
| **Dark Mode** | Full dark mode support |

---

## 🛠️ Tech Stack

| Technology | Purpose |
|------------|---------|
| **Kotlin** | Primary programming language |
| **Jetpack Compose** | Modern declarative UI framework |
| **Material Design 3** | UI components, theming, color system |
| **MVVM Architecture** | Clean separation of UI, logic, and data |
| **Room Database** | Local offline data storage |
| **Firebase Authentication** | User login, register, password reset |
| **Firebase Firestore** | Cloud sync for workouts and stats |
| **Navigation Compose** | Screen navigation with animated transitions |
| **ViewModel + StateFlow** | Reactive state management |
| **Kotlin Coroutines** | Async background operations |
| **Coil** | Profile image loading |
| **Lottie** | Animation support (placeholder ready) |
| **DataStore** | Save dark mode and user preferences |

---

## 📂 Project Structure

```
FitTrackPro/
├── build.gradle.kts                        ← Root Gradle config
├── settings.gradle.kts                     ← Repositories + module setup
├── gradle.properties                       ← Build performance settings
├── README.md                               ← This file
└── app/
    ├── build.gradle.kts                    ← All dependencies
    ├── google-services.json                ← Firebase config (replace with yours)
    ├── proguard-rules.pro                  ← Release build rules
    └── src/main/
        ├── AndroidManifest.xml             ← App permissions + activity
        ├── res/
        │   ├── values/
        │   │   ├── strings.xml             ← App string resources
        │   │   └── themes.xml              ← Base XML theme for Compose
        │   └── font/
        │       ├── poppins_regular.ttf     ← Download from Google Fonts
        │       ├── poppins_medium.ttf
        │       ├── poppins_semibold.ttf
        │       └── poppins_bold.ttf
        └── java/com/fittrack/pro/
            ├── MainActivity.kt             ← App entry + bottom navigation
            ├── data/
            │   ├── database/
            │   │   ├── entity/
            │   │   │   └── Entities.kt     ← Room tables (Workout, Stats, Water, Achievement, Profile)
            │   │   ├── dao/
            │   │   │   └── Daos.kt         ← Database query functions
            │   │   └── FitTrackDatabase.kt ← Room database singleton
            │   └── repository/
            │       └── Repositories.kt     ← Business logic + Firebase sync
            ├── viewmodel/
            │   └── ViewModels.kt           ← FitTrackViewModel + AuthViewModel
            ├── ui/
            │   ├── theme/
            │   │   └── Theme.kt            ← Colors, typography, Material3 theme
            │   ├── navigation/
            │   │   └── Navigation.kt       ← NavHost + all route definitions
            │   ├── components/
            │   │   └── Components.kt       ← Reusable: cards, rings, buttons, headers
            │   └── screens/
            │       ├── splash/
            │       │   └── SplashScreen.kt
            │       ├── auth/
            │       │   └── AuthScreens.kt  ← Login, Register, ForgotPassword
            │       ├── dashboard/
            │       │   └── DashboardScreen.kt
            │       ├── activity/
            │       │   └── ActivityScreens.kt ← List, Log, Search, Water Tracker
            │       ├── progress/
            │       │   └── ProgressScreens.kt ← Analytics, Achievements
            │       └── profile/
            │           └── ProfileScreens.kt  ← Profile, Edit, BMI, Settings
            └── utils/
                └── Utils.kt                ← DateUtils, CalorieEstimator, Constants
```

---

## 🚀 How to Run

### Prerequisites
- Android Studio **Hedgehog 2023.1.1** or newer
- Kotlin **2.0.0**
- Minimum Android **API 26** (Android 8.0)
- A Firebase account (free)

---

### Step 1 — Clone the Repository
```bash
git clone https://github.com/YOUR_USERNAME/FitTrackPro.git
cd FitTrackPro
```

---

### Step 2 — Firebase Setup (Required)

1. Go to **https://console.firebase.google.com**
2. Click **"Create a project"** → Name it `FitTrack Pro` → Click **Continue**
3. Click the **Android icon** to add an Android app
4. Enter package name: `com.fittrack.pro` → Click **Register App**
5. Download **`google-services.json`**
6. Paste it into the `app/` folder — replacing the placeholder file
7. In Firebase Console → **Build → Authentication → Get Started**
    - Click **Email/Password** → Enable → Save ✅
8. In Firebase Console → **Build → Firestore Database → Create Database**
    - Select **Start in test mode** → Choose your region → Enable ✅

---

### Step 3 — Add Poppins Font (Required)

1. Go to **https://fonts.google.com/specimen/Poppins**
2. Click **"Download family"** → Unzip the downloaded file
3. In Android Studio → right-click `res/` → **New → Android Resource Directory** → type `font` → OK
4. Copy these 4 files from the unzipped folder into `res/font/`:

| Original File Name | Rename To |
|---|---|
| `Poppins-Regular.ttf` | `poppins_regular.ttf` |
| `Poppins-Medium.ttf` | `poppins_medium.ttf` |
| `Poppins-SemiBold.ttf` | `poppins_semibold.ttf` |
| `Poppins-Bold.ttf` | `poppins_bold.ttf` |

> ⚠️ File names must be **all lowercase with underscores** — no hyphens or capitals

---

### Step 4 — Generate App Icons (Required)

1. In Android Studio, right-click `res/` folder
2. Click **New → Image Asset**
3. Leave all settings default → Click **Next → Finish**

---

### Step 5 — Sync and Run

```
File → Sync Project with Gradle Files
Click ▶ Run  (or press Shift + F10)
```

---

## 📋 All Requirements — Completed ✅

| Requirement | Status |
|-------------|--------|
| Splash screen with animated logo and slogan | ✅ |
| Firebase Email/Password Authentication | ✅ |
| User Registration with name and email | ✅ |
| Login with validation and error messages | ✅ |
| Forgot Password via email | ✅ |
| Bottom Navigation Bar (4 tabs) | ✅ |
| Dashboard with greeting and user name | ✅ |
| Circular step progress ring | ✅ |
| Gradient metric cards (Steps, Calories, Workout, Water) | ✅ |
| Recent activities on dashboard | ✅ |
| Motivational quote | ✅ |
| FAB to log activity quickly | ✅ |
| Activity logging with 8 exercise types | ✅ |
| Swipe to delete workouts | ✅ |
| Search workouts instantly | ✅ |
| Progress analytics screen | ✅ |
| Weekly steps bar chart | ✅ |
| Weekly calorie progress bar | ✅ |
| Achievements with 8 badges | ✅ |
| Water Tracker with animated ring | ✅ |
| BMI Calculator with category colors | ✅ |
| Profile screen with stats | ✅ |
| Edit Profile (name, height, weight, age, gender, goal) | ✅ |
| Settings (dark mode, notifications, units) | ✅ |
| Room Database for offline storage | ✅ |
| Firebase Firestore cloud sync | ✅ |
| MVVM Architecture | ✅ |
| ViewModel + StateFlow | ✅ |
| Repository Pattern | ✅ |
| Kotlin Coroutines | ✅ |
| Material Design 3 | ✅ |
| Poppins Typography | ✅ |
| Dark Mode support | ✅ |
| Smooth navigation animations | ✅ |

---

## 🎓 Internship Details

| Detail | Info |
|--------|------|
| **Organization** | CodeAlpha |
| **Internship Domain** | Android App Development |
| **Task** | Build a Professional Fitness Tracker App |
| **Duration** | July 2025 |

---

## 👨‍💻 Developer

** Awais Abbas **
- 🌐 GitHub:  https://github.com/malikawaisabbas7
  
  
- 💼 LinkedIn:  linkedin.com/in/awaisabbas7
---

## 📄 License

This project is built for educational and portfolio purposes.

---

<div align="center">
  <strong>⭐ If you found this project helpful, please give it a star!</strong>
  <br><br>
  Built with ❤️ using Jetpack Compose + Firebase
</div>