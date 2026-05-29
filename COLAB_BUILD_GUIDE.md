# Google Colab'ta Gallery2 Derlemesi ve İmzalanması

## Hızlı Başlangıç

### Adım 1: Projeyi Klonla
```bash
cd /content
git clone https://github.com/DolbyLaboratories/mirror-aosp-platform-packages-apps-Gallery2.git
cd mirror-aosp-platform-packages-apps-Gallery2
```

### Adım 2: Java ve Build Tools Kur
```bash
# Java 17 kur (Colab'ta genellikle Java 11 var)
apt-get update && apt-get install -y openjdk-17-jdk

# Android SDK Command Line Tools kur (minimal)
mkdir -p /content/android-sdk
cd /content/android-sdk
wget https://dl.google.com/android/repository/commandlinetools-linux-9477386_latest.zip
unzip -q commandlinetools-linux-*.zip
rm *.zip

# SDK Manager ile platformları kur
export JAVA_HOME=/usr/lib/jvm/java-17-openjdk-amd64
export ANDROID_SDK_ROOT=/content/android-sdk
export PATH=$ANDROID_SDK_ROOT/cmdline-tools/latest/bin:$PATH

# Android API 31 ve platformu kur
yes | sdkmanager --sdk_root=$ANDROID_SDK_ROOT "platforms;android-31" "build-tools;31.0.0" "tools"
```

### Adım 3: Gradlew İzini Ayarla
```bash
cd /content/mirror-aosp-platform-packages-apps-Gallery2
chmod +x ./gradlew
```

### Adım 4: Derleme Yap
```bash
# Ortam değişkenlerini ayarla
export JAVA_HOME=/usr/lib/jvm/java-17-openjdk-amd64
export ANDROID_SDK_ROOT=/content/android-sdk

# Gradle ile derleme (Debug)
./gradlew assembleDebug --stacktrace

# Sonuç:
# build/outputs/apk/debug/Gallery2-debug.apk
```

### Adım 5: APK'yi İndir
```bash
# Colab interface'ten indir:
from google.colab import files
files.download('build/outputs/apk/debug/Gallery2-debug.apk')
```

### Adım 6: Cihazda Kurulum (Sizin Makinede)
```bash
# İmza anahtarı oluştur (varsa geçin)
keytool -genkeypair -v -keystore ~/.android/nefiora-release.jks \
  -alias nefiora_key -keyalg RSA -keysize 2048 -validity 9125

# Zipalign
zipalign -v -p 4 Gallery2-debug.apk gallery2_aligned.apk

# İmzala
apksigner sign --ks ~/.android/nefiora-release.jks \
  --out gallery2-signed.apk gallery2_aligned.apk

# Cihaza yükle (USB debugging açık, bağlı)
adb install -r gallery2-signed.apk
```

---

## Tüm Komutlar (Kopyala-Yapıştır)

### Colab Hücresine Yapıştır:
```python
# Gerekli paketi kur ve klonla
!cd /content && git clone https://github.com/DolbyLaboratories/mirror-aosp-platform-packages-apps-Gallery2.git || true
!apt-get update -qq && apt-get install -y openjdk-17-jdk > /dev/null 2>&1

# Android SDK Kurulumu
!mkdir -p /content/android-sdk && cd /content/android-sdk && \
  wget -q https://dl.google.com/android/repository/commandlinetools-linux-9477386_latest.zip && \
  unzip -q commandlinetools-linux-*.zip && \
  rm *.zip

# SDK Manager ile platformları kur
import os
os.environ['JAVA_HOME'] = '/usr/lib/jvm/java-17-openjdk-amd64'
os.environ['ANDROID_SDK_ROOT'] = '/content/android-sdk'
os.environ['PATH'] = '/content/android-sdk/cmdline-tools/latest/bin:' + os.environ.get('PATH', '')

!echo "y" | /content/android-sdk/cmdline-tools/latest/bin/sdkmanager --sdk_root=/content/android-sdk "platforms;android-31" "build-tools;31.0.0" "tools" 2>&1 | grep -v "Warning"

# Derleme
os.chdir('/content/mirror-aosp-platform-packages-apps-Gallery2')
!chmod +x gradlew
!./gradlew assembleDebug --stacktrace 2>&1 | tail -50

print("✅ Derleme bitti! APK: build/outputs/apk/debug/Gallery2-debug.apk")

# İndir
from google.colab import files
files.download('build/outputs/apk/debug/Gallery2-debug.apk')
```

---

## Sonra Cihazda (Sizin Makinede - Terminal)

```bash
# İndirilen APK'yi makinenize kopyalayın
cd ~/Downloads  # veya APK'nin olduğu klasör

# Şifre ile keystore oluştur:
keytool -genkeypair -v -keystore ~/.android/nefiora-release.jks \
  -alias nefiora_key -keyalg RSA -keysize 2048 -validity 9125

# Zipalign
zipalign -v -p 4 Gallery2-debug.apk gallery2_aligned.apk

# İmzala
apksigner sign --ks ~/.android/nefiora-release.jks \
  --out gallery2-signed.apk gallery2_aligned.apk

# Doğrula
apksigner verify --print-certs gallery2-signed.apk

# Cihaza kur (USB debugging açık, bağlı)
adb install -r gallery2-signed.apk
```

---

**Avantajları:**
✅ 12GB+ RAM (RAM sorunu yok)  
✅ Hızlı derleme (~5-10 dakika)  
✅ İnternet bağlantısı stabil  
✅ Kalıcı depolama yok (ama indir var)

**Yapmanız gerekenler:**
1. Google Colab'a (colab.research.google.com) gidin
2. Yeni notebook oluşturun
3. Yukarıdaki Python kodunu hücreye yapıştırın
4. Çalıştırın (`Shift+Enter`)
5. APK indir
6. Sizin makinede imzala + cihaza yükle

Deneyin, başarılı olursa harika — sorun olursa hata mesajını söyleyin!

