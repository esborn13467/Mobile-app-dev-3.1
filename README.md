
# Mindnest - Mental Health Support App

![Mindnest App Screenshot](https://via.placeholder.com/300x600/7A86B6/FFFFFF?text=Mindnest+Screenshot) 
*(Replace with actual screenshots)*

## Table of Contents
- [Overview](#overview)
- [Key Features](#key-features)
- [Technical Specifications](#technical-specifications)
- [Installation](#installation)
- [Usage](#usage)
- [Development Status](#development-status)
- [Contributing](#contributing)


## Overview
Mindnest is a mobile application designed to provide immediate mental health support for university students. The app offers:
- **Crisis intervention** with one-tap emergency calling
- **On-demand coping tools** for anxiety/stress management
- **Personalized self-care** through customizable reminders

Built with modern Android development practices using Jetpack Compose.

## Key Features

### 🚨 Emergency Resources
- Direct access to national/local helplines
- Pre-loaded contacts for immediate support
- Quick-dial functionality

### 🌊 Distraction Tools
- Guided breathing exercises
- 5-4-3-2-1 grounding technique
- Placeholder modules for future activities

### 💆 Self-Care System
- Custom reminder scheduling
- Wellness notification system
- Personalizable encouragement messages

### 🔐 User Management
- Secure login/registration
- Guest mode access
- Personalized dashboard

## Technical Specifications

| Category          | Details                          |
|-------------------|----------------------------------|
| **Platform**      | Android (API 31+)                |
| **Architecture**  | MVVM (Planned)                   |
| **UI Framework**  | Jetpack Compose                  |
| **Dependencies**  | Kotlin Coroutines, Navigation Component |
| **Minimum SDK**   | Android 12 (API 31)              |

## Installation

### Prerequisites
- Android Studio Giraffe (2022.3.1) or later
- Android SDK 33+
- Kotlin 1.8.0+

### Setup
1. Clone the repository:
   ```bash
   git clone https://github.com/esborn13467/Mobile-app-dev-3.1
   ```
2. Open project in Android Studio
3. Build and run on emulator/device

## Usage

### For End Users
1. Launch the app
2. Login or continue as guest
3. Access features via bottom navigation:
   - Emergency contacts (immediate help)
   - Distraction tools (calming exercises)
   - Self-care reminders (wellness scheduling)

### For Developers
Key components:
- `MainActivity.kt`: Entry point
- `NavGraph.kt`: Navigation structure
- `composables/`: All UI components
- `models/`: Data structures

## Development Status

| Module               | Completion |
|----------------------|------------|
| Core Functionality   | 80%        |
| UI Implementation    | 90%        |
| Backend Integration  | 15%        |

**Current Version:** 0.9.0 (Beta)

## Contributing
We welcome contributions! Please follow these steps:
1. Fork the repository
2. Create a feature branch 
```
git checkout -b feature/your-feature
```
3. Commit changes 
```
git commit -m 'Add some feature'
```
4. Push to branch 
```
git push origin feature/your-feature
```
5. Open a Pull Request

### Recommended Enhancements:
1. Add actual screenshots in place of placeholder
2. Include a demo video link (once available)
3. Add "Testing" section with sample credentials
4. Include build status badges (CI/CD)
5. Add roadmap with upcoming features
