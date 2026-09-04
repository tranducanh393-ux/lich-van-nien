# 🗓️ Lịch Vạn Niên (Vietnamese Lunar Calendar) - Android Jetpack Compose Material 3

Ứng dụng **Lịch Vạn Niên** thuần Việt hiện đại, được xây dựng hoàn toàn bằng công nghệ mới nhất của Google: **Kotlin, Jetpack Compose, Material 3, Navigation3**.

Ứng dụng kết hợp hài hòa giữa nét đẹp văn hóa cổ truyền dân tộc Việt Nam với ngôn ngữ thiết kế **Google Material 3** thanh lịch, mượt mà và trực quan.

---

## ✨ Tính Năng Nổi Bật

### 📅 1. Tra Cứu Lịch Ngày (Bloc Lịch Chuẩn Dân Gian)
- **Tờ lịch Bloc phong cách M3**: Hiển thị rõ ràng ngày Dương lịch, ngày Âm lịch, Thứ trong tuần.
- **Thông tin Can Chi chuẩn xác**: Can chi của Ngày, Tháng, Năm; Tiết khí thiên văn học.
- **Phong thủy & Giờ Hoàng Đạo**:
  - Đánh giá ngày Hoàng Đạo / Hắc Đạo (kèm các sao tốt, trực, sao xấu).
  - Tra cứu 12 khung giờ trong ngày: 6 giờ Hoàng Đạo (Đại Cát) & 6 giờ Hắc Đạo.
  - Hướng xuất hành: Hỷ Thần, Tài Thần, Hạc Thần.
- **Tuần Trăng & Linh Vật Ngày**: Hình ảnh trực quan pha mặt trăng (Trăng non, Trăng rằm, Trăng khuyết) và con giáp hộ mệnh của ngày.

### 📆 2. Lịch Tháng & Lịch Năm Toàn Cảnh
- Xem lưới lịch tháng trực quan, đánh dấu ngày Mùng 1 và ngày Rằm (Sóc & Vọng).
- Phân biệt ngày cuối tuần và các ngày lễ quan trọng.
- Cho phép chuyển nhanh đến bất kỳ ngày nào trong quá khứ hoặc tương lai.

### 🎆 3. Lễ Tết & Sự Kiện Cổ Truyền
- Đếm ngược đến **Tết Nguyên Đán Giáp Thìn / Ất Tỵ**.
- Danh mục chi tiết các ngày lễ truyền thống Việt Nam: Tết Nguyên Tiêu, Giỗ Tổ Hùng Vương, Tết Đoan Ngọ, Vu Lan Báo Hiếu, Trung Thu, Tết Ông Công Ông Táo...
- Các ngày lễ kỷ niệm quốc gia và ngày lễ quốc tế.
- Hướng dẫn chi tiết phong tục tập quán và nét đẹp văn hóa của từng ngày lễ.

### 📜 4. Kho Tàng Văn Khấn Cổ Truyền
- Hơn 100+ bài văn khấn chuẩn nghi lễ dân gian Việt Nam.
- Phân loại rõ ràng:
  - Văn khấn Lễ Tết (Tất niên, Giao thừa, Mùng 1 Tết, Hóa vàng...)
  - Văn khấn Rằm & Mùng Một gia tiên, thần linh
  - Văn khấn Gia đình & Đời sống (Động thổ, Nhập trạch, Khai trương, Mừng thọ...)
  - Văn khấn Đình, Đền, Miếu, Phủ, Chùa chiền.
- Hướng dẫn chuẩn bị mâm lễ chu tất, đúng phong tục.
- Bộ lọc tìm kiếm nhanh theo từ khóa.

### 🔄 5. Công Cụ Đổi Ngày Âm ↔ Dương Chuẩn Xác
- Tích hợp thuật toán chuyển đổi âm dương thiên văn học chính xác cho múi giờ Việt Nam (UTC+7).
- Hỗ trợ đầy đủ các năm nhuận âm lịch và tháng nhuận.

### 🎨 6. Thiết Kế Chuẩn Google Material 3
- **Chủ đề màu sắc đa dạng**:
  - 🔵 **Xanh Hiện Đại (M3 Ocean / Indigo Blue)** - Mặc định thanh lịch.
  - 🟢 **Xanh Ngọc Bích (Jade Green)** - Tươi mát, bình an.
  - 🟡 **Vàng Hổ Phách (Warm Amber)** - Ấm cúng, may mắn.
  - 🟣 **Tối Trầm Hiện Đại (Dark Sleek)** - Tối ưu cho ban đêm và màn hình OLED.
- **Material You Dynamic Color**: Tự động đồng bộ màu sắc giao diện theo hình nền điện thoại trên Android 12+.
- **Chế độ chữ lớn**: Tối ưu khả năng tiếp cận, giúp người cao tuổi dễ dàng đọc lịch và văn khấn.

### 🔐 7. Hệ Thống Tài Khoản & Xác Thực Thật Sự (Real Authentication)
- Đăng ký và Đăng nhập tài khoản bằng Email & Mật khẩu thực tế.
- Bảo mật: Băm mật khẩu với thuật toán SHA-256 và chuỗi muối (Salt) riêng biệt cho từng tài khoản.
- Duy trì phiên đăng nhập bền vững (Persistent Session) khi tắt/mở ứng dụng.
- Tích hợp cơ chế chọn tài khoản Google (Google Sign-In) trên thiết bị.

---

## 🛠️ Công Nghệ Sử Dụng (Tech Stack)

| Thành Phần | Công Nghệ |
|---|---|
| **Ngôn ngữ** | Kotlin 2.3+ |
| **Giao diện (UI)** | Jetpack Compose (Compose BOM 2026.03.01) |
| **Design System** | Material 3 1.3+ & Material Icons Extended |
| **Điều hướng** | Androidx Navigation3 (`androidx.navigation3:1.0.1`) |
| **Kiến trúc** | MVVM / Clean Architecture, StateFlow, Coroutines |
| **Build Tool** | Gradle 9.0+ & Android Gradle Plugin 9.0.1 |
| **Tối thiểu** | Android 7.0 (API level 24)+, Biên dịch trên API 36 |

---

## 🚀 Hướng Dẫn Cài Đặt & Chạy Ứng Dụng

### Yêu Cầu Môi Trường
- **JDK**: Java Development Kit 17 trở lên.
- **Android Studio**: Ladybug / Meerkat hoặc mới hơn (khuyên dùng).
- **Android SDK**: Compile SDK 36, Min SDK 24.

### Các Bước Thực Hiện
1. **Clone repository về máy:**
   ```bash
   git clone https://github.com/your-username/lich-van-nien.git
   cd lich-van-nien
   ```

2. **Mở dự án:**
   - Mở Android Studio -> Chọn **Open** -> Trỏ đến thư mục vừa clone.
   - Chờ Gradle đồng bộ (Sync Project with Gradle Files).

3. **Biên dịch qua dòng lệnh (Command Line):**
   ```bash
   # Trên Windows PowerShell
   ./gradlew assembleDebug

   # Trên macOS / Linux
   ./gradlew assembleDebug
   ```

4. **Cài đặt file APK vào máy/giả lập:**
   - File APK đầu ra được tạo tại:
     `app/build/outputs/apk/debug/app-debug.apk`
   - Cài đặt bằng ADB:
     ```bash
     adb install app/build/outputs/apk/debug/app-debug.apk
     ```

---

## 📂 Cấu Trúc Mã Nguồn (Project Structure)

```
app/src/main/java/com/example/lichamviet/
├── data/
│   ├── auth/                    # Quản lý xác thực & phiên người dùng (AuthManager)
│   ├── model/                   # Data classes (SolarDate, LunarDate, Holiday, VanKhan, etc.)
│   └── repository/              # Kho dữ liệu (VietCalendarEngine, HolidayRepository, VanKhanRepository, UserPreferencesRepository)
├── theme/                       # Hệ thống Theme Material 3 (Color, Theme, Type)
├── ui/
│   ├── components/              # Các thành phần giao diện dùng chung (AppBottomBar, etc.)
│   └── screens/
│       ├── day/                 # Màn hình Lịch Ngày
│       ├── month/               # Màn hình Lịch Tháng
│       ├── year/                # Màn hình Lịch Năm
│       ├── holidays/            # Màn hình Lễ Tết & Sự kiện
│       ├── utilities/           # Màn hình Tiện ích (Đổi ngày, Văn khấn)
│       ├── settings/            # Màn hình Cài đặt & Hộp thoại Xác thực (AuthDialog)
│       └── premium/             # Màn hình Gói Lộc Phát VIP
├── MainActivity.kt              # Entry point Activity
└── Navigation.kt                # Khung điều hướng chính Navigation3
```

---

## 💖 Ủng Hộ Phát Triển (Donate / Sponsor)

Nếu bạn yêu thích ứng dụng **Lịch Vạn Niên (Lịch Âm Việt)** và muốn tiếp thêm động lực cho tác giả duy trì máy chủ và hoàn thiện ứng dụng ngày một tốt hơn, bạn có thể ủng hộ tác giả qua:

| 🏦 Ngân Hàng Techcombank (VietQR) | 📱 Ví Điện Tử MoMo (VietQR) |
| :---: | :---: |
| **Techcombank (Kỹ Thương Việt Nam)** | **Ví MoMo** |
| Số tài khoản: **`9203 0920 03`** | Số điện thoại: **`0345413260`** |
| Chủ TK: **TRAN DUC ANH** | Chủ ví: **TRẦN ĐỨC ANH** |
| Cú pháp: `Ung ho Lich Am Viet` | Lời nhắn: `Ung ho Lich Am Viet` |

*Trân trọng cảm ơn mọi sự đồng hành và đóng góp quý báu của bạn!* ❤️

---

## 📄 Bản Quyền & Giấy Phép (License)
Dự án được phát hành dưới giấy phép [MIT License](LICENSE). Mọi đóng góp (Pull Request, Issue) đều được chào đón nồng nhiệt!
