# OHLI iOS 27 Home — Android Launcher

Proyek launcher Android native yang terinspirasi dari susunan Home Screen iOS 27 pada gambar referensi. Dibuat dengan Java dan komponen Android bawaan agar tidak membutuhkan library UI besar.

## Target perangkat

- Minimum Android 8.0 (API 26)
- Compile SDK 35
- Java 17
- Gradle 8.7 + Android Gradle Plugin 8.6.1
- Tidak membutuhkan server, akun, API key, atau layanan berbayar

## Fitur yang tersedia

- Terdaftar sebagai aplikasi Home/Launcher Android.
- Layar Home dengan wallpaper gradasi biru–toska dan kartu transparan bergaya Liquid Glass.
- Kartu cuaca (shortcut ke pengaturan lokasi; bukan prakiraan cuaca online).
- Kartu kalender dengan tanggal perangkat dan shortcut ke aplikasi kalender.
- Grid aplikasi 4 kolom. Ikon diambil dari aplikasi yang terpasang dan diberi tampilan tile membulat; beberapa aplikasi umum memakai glyph bergaya iOS.
- Dock bawah dengan aplikasi Telepon, Browser, Pesan, dan Kamera yang ditemukan di perangkat.
- App drawer dengan pencarian dan shortcut untuk membuka aplikasi asli.
- Panel pengaturan launcher, shortcut ke pengaturan Android, dan tombol menerapkan wallpaper.
- Pratinjau layar kunci bergaya iPhone (hanya pratinjau di dalam aplikasi, bukan pengganti lock screen sistem).
- Swipe ke atas dari area bawah layar atau ketuk kolom pencarian untuk membuka App Drawer.

## Cara membuat APK lewat GitHub Actions

1. Buat repository GitHub baru.
2. Ekstrak ZIP proyek ini, lalu unggah **isi folder proyek** ke root repository. Pastikan folder `.github/workflows` ikut terunggah.
3. Buka tab **Actions** di repository. Workflow **Build OHLI iOS 27 Launcher APK** akan berjalan setelah push. Untuk menjalankan manual, pilih workflow itu lalu tekan **Run workflow**.
4. Setelah status workflow hijau, buka hasil run dan unduh artifact **OHLI-iOS27-Launcher-APKs**.
5. Di dalam artifact ada satu APK release yang ditandatangani dengan debug keystore agar mudah diuji langsung di HP.
6. Pasang APK di perangkat Android 8 atau lebih baru. Bila diminta, izinkan instalasi dari sumber yang digunakan.
7. Tekan tombol Home, lalu pilih **OHLI iOS 27 Home** sebagai launcher default. Jika belum muncul, buka aplikasi → Pengaturan Home → Jadikan launcher utama.

## Catatan penting tentang batas Android

Launcher biasa tidak mempunyai izin untuk mengganti lock screen asli, panel notifikasi/Quick Settings sistem, ikon di dalam aplikasi lain, font seluruh sistem, atau antarmuka aplikasi Pengaturan Vivo. Untuk itu dibutuhkan dukungan sistem, ROM khusus, atau akses root. Aplikasi ini tidak meminta akses root dan tidak menonaktifkan sistem keamanan Android.

Widget cuaca hanya menyediakan shortcut pengaturan lokasi, belum mengambil data cuaca real-time. Font memakai sans-serif bawaan Android; font San Francisco milik Apple tidak disertakan. Ikon dan desain dibuat orisinal dengan arah visual serupa, bukan aset Apple resmi.

## Build lokal

Buka proyek ini menggunakan Android Studio yang mempunyai JDK 17 dan Android SDK Platform 35. Proyek menggunakan Gradle 8.7. Jika Android Studio meminta versi Gradle, pilih 8.7. Build task: `assembleRelease`.

## Signing

APK release dalam workflow saat ini menggunakan debug keystore agar dapat dipasang untuk pengujian. Jangan gunakan signing ini untuk publikasi resmi. Sebelum distribusi publik, buat keystore pribadi dan simpan password/key alias di GitHub Secrets, lalu ganti konfigurasi signing.
