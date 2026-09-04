package com.example.lichamviet.data.repository

import com.example.lichamviet.data.model.Holiday
import com.example.lichamviet.data.model.HolidayType
import com.example.lichamviet.data.model.LunarDate
import com.example.lichamviet.data.model.SolarDate

object HolidayRepository {

    // Danh sách ngày lễ, tết Âm lịch truyền thống Việt Nam
    val LUNAR_HOLIDAYS = listOf(
        Holiday(
            id = "ong_tao",
            name = "Tiễn Táo Quân về trời (Ông Táo)",
            isLunar = true,
            day = 23,
            month = 12,
            type = HolidayType.LUNAR_TRADITIONAL,
            subtitle = "23 tháng Chạp",
            description = "Ngày các Táo chầu trời bẩm báo Ngọc Hoàng việc nhân gian, cầu một năm mới bình an ấm no.",
            customs = listOf("Phóng sinh cá chép đỏ", "Lau dọn bao sái ban thờ", "Chuẩn bị mâm cơm cúng tiễn ông Táo")
        ),
        Holiday(
            id = "tat_nien",
            name = "Tất Niên & Đêm Giao Thừa",
            isLunar = true,
            day = 30, // hoặc 29 nếu tháng thiếu
            month = 12,
            type = HolidayType.LUNAR_TRADITIONAL,
            subtitle = "30 tháng Chạp",
            description = "Thời khắc chuyển giao năm cũ sang năm mới, bữa cơm đoàn viên sum vầy của cả gia đình người Việt.",
            customs = listOf("Ăn bữa cơm Tất Niên đoàn tụ", "Cúng Giao thừa ngoài trời và trong nhà", "Xông đất đầu năm", "Hái lộc may mắn")
        ),
        Holiday(
            id = "tet_mung_1",
            name = "Mùng 1 Tết Nguyên Đán (Tết Cha)",
            isLunar = true,
            day = 1,
            month = 1,
            type = HolidayType.LUNAR_TRADITIONAL,
            isNationalDayOff = true,
            subtitle = "Mùng 1 tháng Giêng",
            description = "Khởi đầu năm mới đại cát, ngày người Việt dành trọn cho ông bà, cha mẹ và gia tiên nội tộc.",
            customs = listOf("Mặc áo mới chúc tết ông bà cha mẹ", "Mừng tuổi bao lì xì đỏ", "Đi lễ chùa cầu an đầu năm", "Kiêng quét nhà, kiêng đổ rác")
        ),
        Holiday(
            id = "tet_mung_2",
            name = "Mùng 2 Tết Nguyên Đán (Tết Mẹ)",
            isLunar = true,
            day = 2,
            month = 1,
            type = HolidayType.LUNAR_TRADITIONAL,
            isNationalDayOff = true,
            subtitle = "Mùng 2 tháng Giêng",
            description = "Ngày về thăm họ ngoại, chúc tết bên ngoại theo truyền thống 'Mùng một tết cha, mùng hai tết mẹ'.",
            customs = listOf("Con cháu về thăm quê ngoại", "Mừng tuổi người già, trẻ nhỏ bên ngoại", "Thắp hương gia tiên bên ngoại")
        ),
        Holiday(
            id = "tet_mung_3",
            name = "Mùng 3 Tết Nguyên Đán (Tết Thầy)",
            isLunar = true,
            day = 3,
            month = 1,
            type = HolidayType.LUNAR_TRADITIONAL,
            isNationalDayOff = true,
            subtitle = "Mùng 3 tháng Giêng",
            description = "Tôn sư trọng đạo, học trò đến chúc tết thầy cô giáo đã dạy dỗ mình nên người.",
            customs = listOf("Đến thăm và tri ân thầy cô giáo", "Gặp gỡ bạn bè đồng môn đầu xuân")
        ),
        Holiday(
            id = "tet_mung_4",
            name = "Mùng 4 Tết (Lễ Hóa Vàng)",
            isLunar = true,
            day = 4,
            month = 1,
            type = HolidayType.LUNAR_TRADITIONAL,
            isNationalDayOff = true,
            subtitle = "Mùng 4 tháng Giêng",
            description = "Lễ cúng tiễn đưa tổ tiên sau những ngày về ăn Tết sum vầy cùng con cháu.",
            customs = listOf("Cúng tạ hóa vàng", "Khai xuân, du xuân trẩy hội")
        ),
        Holiday(
            id = "tet_mung_5",
            name = "Mùng 5 Tết (Hội Gò Đống Đa)",
            isLunar = true,
            day = 5,
            month = 1,
            type = HolidayType.LUNAR_TRADITIONAL,
            isNationalDayOff = true,
            subtitle = "Mùng 5 tháng Giêng",
            description = "Kỷ niệm chiến thắng Ngọc Hồi - Đống Đa lẫy lừng của Hoàng đế Quang Trung đại phá quân Thanh.",
            customs = listOf("Dự lễ hội Gò Đống Đa", "Rước rồng lửa, múa lân sư rồng")
        ),
        Holiday(
            id = "khai_ha",
            name = "Lễ Khai Hạ (Hạ Cây Nêu)",
            isLunar = true,
            day = 7,
            month = 1,
            type = HolidayType.LUNAR_TRADITIONAL,
            subtitle = "Mùng 7 tháng Giêng",
            description = "Lễ hạ cây nêu kết thúc chuỗi ngày nghỉ Tết, mở đầu cho năm lao động sản xuất mới.",
            customs = listOf("Làm lễ hạ cây nêu", "Khai bút đầu xuân", "Mở cửa hàng buôn bán lấy may")
        ),
        Holiday(
            id = "via_than_tai",
            name = "Ngày Vía Thần Tài",
            isLunar = true,
            day = 10,
            month = 1,
            type = HolidayType.LUNAR_TRADITIONAL,
            subtitle = "Mùng 10 tháng Giêng",
            description = "Ngày cúng Thần Tài cầu buôn may bán đắt, tài lộc hanh thông cả năm.",
            customs = listOf("Mua vàng lấy vía may mắn", "Lau dọn bàn thờ Thần Tài - Thổ Địa", "Cúng cá lóc nướng hoặc heo quay")
        ),
        Holiday(
            id = "nguyen_tieu",
            name = "Tết Nguyên Tiêu (Rằm tháng Giêng)",
            isLunar = true,
            day = 15,
            month = 1,
            type = HolidayType.LUNAR_TRADITIONAL,
            subtitle = "Rằm tháng Giêng",
            description = "Đêm rằm đầu tiên của năm mới. Cổ nhân có câu: 'Cúng cả năm không bằng Rằm tháng Giêng'.",
            customs = listOf("Lễ chùa cầu quốc thái dân an", "Cúng rằm Thượng Nguyên", "Ăn chay thanh tịnh", "Ngắm trăng làm thơ")
        ),
        Holiday(
            id = "han_thuc",
            name = "Tết Hàn Thực",
            isLunar = true,
            day = 3,
            month = 3,
            type = HolidayType.LUNAR_TRADITIONAL,
            subtitle = "Mùng 3 tháng 3 Âm lịch",
            description = "Tết bánh trôi, bánh chay thuần khiết, hướng về cội nguồn tổ tiên.",
            customs = listOf("Làm bánh trôi nước ngọt thơm", "Làm bánh chay dâng cúng tổ tiên")
        ),
        Holiday(
            id = "gio_to",
            name = "Giỗ Tổ Hùng Vương",
            isLunar = true,
            day = 10,
            month = 3,
            type = HolidayType.LUNAR_TRADITIONAL,
            isNationalDayOff = true,
            subtitle = "Mùng 10 tháng 3 Âm lịch",
            description = "Quốc lễ thiêng liêng tưởng nhớ công ơn khai thiên lập địa của các Vua Hùng: 'Dù ai đi ngược về xuôi / Nhớ ngày Giỗ Tổ mùng mười tháng ba'.",
            customs = listOf("Dâng hương tại Đền Hùng (Phú Thọ)", "Lễ tưởng niệm trên khắp cả nước", "Hát xoan, rước kiệu truyền thống")
        ),
        Holiday(
            id = "phat_dan",
            name = "Đại Lễ Phật Đản (Vesak)",
            isLunar = true,
            day = 15,
            month = 4,
            type = HolidayType.LUNAR_BUDDHIST,
            subtitle = "Rằm tháng 4 Âm lịch",
            description = "Kỷ niệm ngày Đức Phật Thích Ca Mâu Ni đản sinh, ngày hội văn hóa tâm linh từ bi lớn nhất của Phật tử Việt Nam.",
            customs = listOf("Lễ Tắm Phật", "Ăn chay phóng sinh", "Rước đèn hoa đăng lung linh trên sông")
        ),
        Holiday(
            id = "doan_ngo",
            name = "Tết Đoan Ngọ (Giết Sâu Bọ)",
            isLunar = true,
            day = 5,
            month = 5,
            type = HolidayType.LUNAR_TRADITIONAL,
            subtitle = "Mùng 5 tháng 5 Âm lịch",
            description = "Tết giữa năm tống khứ tà khí sâu bọ hại mùa màng, trừ khử bệnh tật cho cơ thể.",
            customs = listOf("Ăn cơm rượu nếp lúc sáng sớm", "Ăn mận, vải, dưa hấu, hoa quả chua", "Nấu bánh gio (bánh tro) chấm mật mía")
        ),
        Holiday(
            id = "vu_lan",
            name = "Lễ Vu Lan & Rằm tháng Bảy",
            isLunar = true,
            day = 15,
            month = 7,
            type = HolidayType.LUNAR_BUDDHIST,
            subtitle = "Rằm tháng 7 Âm lịch",
            description = "Đại lễ Vu Lan báo hiếu công ơn sinh thành dưỡng dục của cha mẹ và Lễ Xá tội vong nhân mở lượng từ bi.",
            customs = listOf("Nghi thức bông hồng cài áo (đỏ/hồng/trắng)", "Cúng thí thực cô hồn xá tội vong nhân", "Ăn chay làm việc thiện nguyện tích phước")
        ),
        Holiday(
            id = "trung_thu",
            name = "Tết Trung Thu (Rằm Trung Thu)",
            isLunar = true,
            day = 15,
            month = 8,
            type = HolidayType.LUNAR_TRADITIONAL,
            subtitle = "Rằm tháng 8 Âm lịch",
            description = "Tết trông Trăng sum họp đoàn viên của người Việt và Tết thiếu nhi với chị Hằng, chú Cuội.",
            customs = listOf("Phá cỗ trông trăng", "Ăn bánh nướng, bánh dẻo", "Rước đèn ông sao, múa lân sư rồng rộn rã")
        ),
        Holiday(
            id = "trung_cuu",
            name = "Tết Trùng Cửu",
            isLunar = true,
            day = 9,
            month = 9,
            type = HolidayType.LUNAR_TRADITIONAL,
            subtitle = "Mùng 9 tháng 9 Âm lịch",
            description = "Tết hoa cúc, ngày cát lành của trời đất dành cho việc dưỡng sinh, thưởng ngoạn cảnh sắc non sông.",
            customs = listOf("Uống trà hoa cúc", "Leo núi ngắm cảnh", "Cầu chúc người cao tuổi trường thọ")
        ),
        Holiday(
            id = "trung_thap",
            name = "Tết Trùng Thập (Tết Thầy Thuốc)",
            isLunar = true,
            day = 10,
            month = 10,
            type = HolidayType.LUNAR_TRADITIONAL,
            subtitle = "Mùng 10 tháng 10 Âm lịch",
            description = "Tết của người làm nghề y thuốc nam, cũng là lễ cơm mới vụ lúa mùa của người nông dân.",
            customs = listOf("Cúng cơm mới mừng vụ mùa bội thu", "Làm bánh giầy bánh dừa", "Tri ân thầy thuốc lương y")
        ),
        Holiday(
            id = "ha_nguyen",
            name = "Tết Hạ Nguyên",
            isLunar = true,
            day = 15,
            month = 10,
            type = HolidayType.LUNAR_TRADITIONAL,
            subtitle = "Rằm tháng 10 Âm lịch",
            description = "Lễ mừng lúa mới, tạ ơn Tiên Nông và Gia tiên đã phù hộ cho một mùa màng tốt tươi ấm no.",
            customs = listOf("Nấu xôi gạo mới cúng tổ tiên", "Cúng thần Đất, thần Nông")
        )
    )

    // Danh sách ngày lễ Dương lịch tại Việt Nam
    val SOLAR_HOLIDAYS = listOf(
        Holiday(
            id = "tet_duong_lich",
            name = "Tết Dương Lịch (Năm Mới)",
            isLunar = false,
            day = 1,
            month = 1,
            type = HolidayType.SOLAR_NATIONAL,
            isNationalDayOff = true,
            subtitle = "1 tháng 1",
            description = "Ngày đầu tiên của năm Dương lịch, khởi đầu những ước vọng mới.",
            customs = listOf("Nghỉ ngơi cùng gia đình", "Xem pháo hoa chào năm mới")
        ),
        Holiday(
            id = "hssv",
            name = "Ngày Học sinh - Sinh viên Việt Nam",
            isLunar = false,
            day = 9,
            month = 1,
            type = HolidayType.SOLAR_COMMEMORATIVE,
            subtitle = "9 tháng 1",
            description = "Kỷ niệm ngày truyền thống học sinh, sinh viên noi gương anh hùng Trần Văn Ơn.",
            customs = listOf("Tuyên dương sinh viên tiêu biểu", "Các phong trào học tập sáng tạo")
        ),
        Holiday(
            id = "dang",
            name = "Ngày thành lập Đảng Cộng sản Việt Nam",
            isLunar = false,
            day = 3,
            month = 2,
            type = HolidayType.SOLAR_COMMEMORATIVE,
            subtitle = "3 tháng 2",
            description = "Kỷ niệm ngày 3/2/1930 thành lập Đảng Cộng sản Việt Nam tại Hương Cảng.",
            customs = listOf("Mít tinh kỷ niệm", "Sinh hoạt chính trị truyền thống")
        ),
        Holiday(
            id = "valentine",
            name = "Lễ Tình Nhân (Valentine)",
            isLunar = false,
            day = 14,
            month = 2,
            type = HolidayType.SOLAR_COMMEMORATIVE,
            subtitle = "14 tháng 2",
            description = "Ngày tôn vinh tình yêu đôi lứa ngọt ngào.",
            customs = listOf("Tặng hoa hồng và socola", "Gửi gắm lời yêu thương")
        ),
        Holiday(
            id = "thay_thuoc",
            name = "Ngày Thầy Thuốc Việt Nam",
            isLunar = false,
            day = 27,
            month = 2,
            type = HolidayType.SOLAR_COMMEMORATIVE,
            subtitle = "27 tháng 2",
            description = "Tôn vinh các y bác sĩ, chiến sĩ áo trắng tận tụy chăm sóc sức khỏe nhân dân theo lời dạy 'Lương y như từ mẫu'.",
            customs = listOf("Tri ân đội ngũ y bác sĩ", "Thăm hỏi cán bộ ngành y")
        ),
        Holiday(
            id = "quoc_te_phu_nu",
            name = "Ngày Quốc tế Phụ nữ",
            isLunar = false,
            day = 8,
            month = 3,
            type = HolidayType.SOLAR_COMMEMORATIVE,
            subtitle = "8 tháng 3",
            description = "Tôn vinh vẻ đẹp, sự hy sinh và đóng góp to lớn của phụ nữ cho gia đình và xã hội.",
            customs = listOf("Tặng hoa và quà chúc mừng bà, mẹ, vợ, chị em gái", "Các hoạt động giao lưu văn nghệ")
        ),
        Holiday(
            id = "hanh_phuc",
            name = "Ngày Quốc tế Hạnh phúc",
            isLunar = false,
            day = 20,
            month = 3,
            type = HolidayType.SOLAR_COMMEMORATIVE,
            subtitle = "20 tháng 3",
            description = "Ngày lan tỏa thông điệp yêu thương, sẻ chia và xây dựng cuộc sống an vui.",
            customs = listOf("Trao nụ cười và hành động tử tế", "Gắn kết các thành viên gia đình")
        ),
        Holiday(
            id = "doan_thanh_nien",
            name = "Ngày thành lập Đoàn TNCS Hồ Chí Minh",
            isLunar = false,
            day = 26,
            month = 3,
            type = HolidayType.SOLAR_COMMEMORATIVE,
            subtitle = "26 tháng 3",
            description = "Ngày truyền thống của thế hệ thanh niên xung kích, sáng tạo vì Tổ quốc.",
            customs = listOf("Hội trại truyền thống thanh niên", "Các hoạt động tình nguyện vì cộng đồng")
        ),
        Holiday(
            id = "thong_nhat",
            name = "Ngày Giải phóng Miền Nam, Thống nhất Đất nước",
            isLunar = false,
            day = 30,
            month = 4,
            type = HolidayType.SOLAR_NATIONAL,
            isNationalDayOff = true,
            subtitle = "30 tháng 4",
            description = "Ngày đại thắng mùa xuân 1975, giang sơn thu về một mối, non sông liền một dải.",
            customs = listOf("Treo cờ Tổ quốc rực rỡ", "Dâng hương tưởng niệm các anh hùng liệt sĩ", "Chương trình nghệ thuật chào mừng")
        ),
        Holiday(
            id = "quoc_te_lao_dong",
            name = "Ngày Quốc tế Lao động",
            isLunar = false,
            day = 1,
            month = 5,
            type = HolidayType.SOLAR_NATIONAL,
            isNationalDayOff = true,
            subtitle = "1 tháng 5",
            description = "Tôn vinh tinh thần lao động cần cù, sáng tạo của giai cấp công nhân và người lao động.",
            customs = listOf("Nghỉ ngơi tái tạo năng lượng", "Tuyên dương gương lao động xuất sắc")
        ),
        Holiday(
            id = "dien_bien_phu",
            name = "Chiến thắng Điện Biên Phủ",
            isLunar = false,
            day = 7,
            month = 5,
            type = HolidayType.SOLAR_COMMEMORATIVE,
            subtitle = "7 tháng 5",
            description = "Kỷ niệm chiến thắng lịch sử 7/5/1954 'Lừng lẫy năm châu, chấn động địa cầu'.",
            customs = listOf("Ôn lại trang sử vàng oanh liệt", "Tri ân cựu chiến binh Điện Biên")
        ),
        Holiday(
            id = "sinh_nhat_bac",
            name = "Ngày sinh Chủ tịch Hồ Chí Minh",
            isLunar = false,
            day = 19,
            month = 5,
            type = HolidayType.SOLAR_COMMEMORATIVE,
            subtitle = "19 tháng 5",
            description = "Kỷ niệm ngày sinh vị Cha già kính yêu của dân tộc, Danh nhân văn hóa thế giới (19/5/1890).",
            customs = listOf("Vào Lăng viếng Bác", "Học tập và làm theo tư tưởng đạo đức Hồ Chí Minh")
        ),
        Holiday(
            id = "thieu_nhi",
            name = "Ngày Quốc tế Thiếu nhi",
            isLunar = false,
            day = 1,
            month = 6,
            type = HolidayType.SOLAR_COMMEMORATIVE,
            subtitle = "1 tháng 6",
            description = "Ngày hội rộn rã dành cho búp măng non của đất nước.",
            customs = listOf("Tổ chức vui chơi cho trẻ em", "Tặng quà khích lệ học tập")
        ),
        Holiday(
            id = "bao_chi",
            name = "Ngày Báo chí Cách mạng Việt Nam",
            isLunar = false,
            day = 21,
            month = 6,
            type = HolidayType.SOLAR_COMMEMORATIVE,
            subtitle = "21 tháng 6",
            description = "Kỷ niệm ngày Bác Hồ sáng lập báo Thanh Niên (1925), tôn vinh những người cầm bút chân chính.",
            customs = listOf("Trao Giải Báo chí Quốc gia", "Tri ân phóng viên nhà báo")
        ),
        Holiday(
            id = "gia_dinh",
            name = "Ngày Gia đình Việt Nam",
            isLunar = false,
            day = 28,
            month = 6,
            type = HolidayType.SOLAR_COMMEMORATIVE,
            subtitle = "28 tháng 6",
            description = "Ngày tôn vinh những giá trị tốt đẹp của mái ấm gia đình Việt Nam.",
            customs = listOf("Sum họp quây quần bên mâm cơm ấm áp", "Tôn vinh gia đình văn hóa tiêu biểu")
        ),
        Holiday(
            id = "thuong_binh",
            name = "Ngày Thương binh Liệt sĩ",
            isLunar = false,
            day = 27,
            month = 7,
            type = HolidayType.SOLAR_COMMEMORATIVE,
            subtitle = "27 tháng 7",
            description = "Đạo lý 'Uống nước nhớ nguồn', đời đời ghi nhớ công ơn những người đã ngã xuống vì độc lập tự do.",
            customs = listOf("Thắp nến tri ân tại nghĩa trang liệt sĩ", "Thăm hỏi Mẹ Việt Nam Anh hùng và thương bệnh binh")
        ),
        Holiday(
            id = "cm_thang_tam",
            name = "Cách mạng Tháng Tám & Ngày Truyền thống CAND",
            isLunar = false,
            day = 19,
            month = 8,
            type = HolidayType.SOLAR_COMMEMORATIVE,
            subtitle = "19 tháng 8",
            description = "Kỷ niệm ngày tổng khởi nghĩa Cách mạng Tháng Tám 1945 và thành lập Công an Nhân dân.",
            customs = listOf("Mít tinh tuyên truyền lịch sử", "Biểu dương lực lượng bảo vệ an ninh trật tự")
        ),
        Holiday(
            id = "quoc_khanh",
            name = "Ngày Quốc khánh Việt Nam",
            isLunar = false,
            day = 2,
            month = 9,
            type = HolidayType.SOLAR_NATIONAL,
            isNationalDayOff = true,
            subtitle = "2 tháng 9",
            description = "Kỷ niệm ngày 2/9/1945 Chủ tịch Hồ Chí Minh đọc bản Tuyên ngôn Độc lập khai sinh nước VNDCCH.",
            customs = listOf("Treo cờ Tổ quốc", "Tham gia các sự kiện văn hóa nghệ thuật", "Xem diễu binh, pháo hoa mừng ngày hội non sông")
        ),
        Holiday(
            id = "giai_phong_thu_do",
            name = "Ngày Giải phóng Thủ đô",
            isLunar = false,
            day = 10,
            month = 10,
            type = HolidayType.SOLAR_COMMEMORATIVE,
            subtitle = "10 tháng 10",
            description = "Kỷ niệm đoàn quân chiến thắng tiến về tiếp quản Thủ đô Hà Nội ngày 10/10/1954.",
            customs = listOf("Trang hoàng rực rỡ phố phường Hà Nội", "Các chương trình âm nhạc về Hà Nội ngàn năm văn hiến")
        ),
        Holiday(
            id = "doanh_nhan",
            name = "Ngày Doanh nhân Việt Nam",
            isLunar = false,
            day = 13,
            month = 10,
            type = HolidayType.SOLAR_COMMEMORATIVE,
            subtitle = "13 tháng 10",
            description = "Tôn vinh đội ngũ doanh nhân dám nghĩ dám làm, làm giàu cho quê hương đất nước.",
            customs = listOf("Vinh danh doanh nghiệp doanh nhân xuất sắc", "Diễn đàn phát triển kinh tế")
        ),
        Holiday(
            id = "phu_nu_vn",
            name = "Ngày Phụ nữ Việt Nam",
            isLunar = false,
            day = 20,
            month = 10,
            type = HolidayType.SOLAR_COMMEMORATIVE,
            subtitle = "20 tháng 10",
            description = "Kỷ niệm thành lập Hội Phụ nữ Việt Nam (1930), tôn vinh người phụ nữ kiên cường, đảm đang, nhân hậu.",
            customs = listOf("Tặng hoa và thiệp mừng đến phái đẹp", "Tổ chức các buổi gặp mặt ấm cúng")
        ),
        Holiday(
            id = "nha_giao",
            name = "Ngày Nhà giáo Việt Nam",
            isLunar = false,
            day = 20,
            month = 11,
            type = HolidayType.SOLAR_COMMEMORATIVE,
            subtitle = "20 tháng 11",
            description = "Tôn sư trọng đạo, tri ân những người thầy người cô tận tụy chở những chuyến đò tri thức.",
            customs = listOf("Học trò mang hoa về thăm thầy cô giáo cũ", "Lễ mít tinh kỷ niệm và hội thao văn nghệ học đường")
        ),
        Holiday(
            id = "quan_doi",
            name = "Ngày thành lập QĐND Việt Nam",
            isLunar = false,
            day = 22,
            month = 12,
            type = HolidayType.SOLAR_COMMEMORATIVE,
            subtitle = "22 tháng 12",
            description = "Kỷ niệm ngày 22/12/1944 thành lập Đội VN Tuyên truyền Giải phóng quân và Ngày hội Quốc phòng toàn dân.",
            customs = listOf("Gặp mặt cựu chiến binh và bộ đội Cụ Hồ", "Giáo dục truyền thống yêu nước cho thế hệ trẻ")
        ),
        Holiday(
            id = "giang_sinh",
            name = "Lễ Giáng Sinh (Noel)",
            isLunar = false,
            day = 25,
            month = 12,
            type = HolidayType.SOLAR_COMMEMORATIVE,
            subtitle = "25 tháng 12",
            description = "Lễ hội mùa đông an lành, đón mừng Chúa Giáng sinh.",
            customs = listOf("Trang hoàng cây thông và đèn lấp lánh", "Đi lễ nhà thờ đêm Noel", "Tặng quà chúc bình an")
        )
    )

    /**
     * Tìm ngày lễ cho một ngày cụ thể (kết hợp cả Dương lịch và Âm lịch)
     */
    fun getHolidaysForDate(solarDate: SolarDate, lunarDate: LunarDate): List<Holiday> {
        val result = mutableListOf<Holiday>()

        // Kiểm tra lễ Dương
        SOLAR_HOLIDAYS.filter { it.day == solarDate.day && it.month == solarDate.month }.forEach {
            result.add(it)
        }

        // Kiểm tra lễ Âm
        LUNAR_HOLIDAYS.filter {
            it.day == lunarDate.day && it.month == lunarDate.month && !lunarDate.isLeapMonth
        }.forEach {
            result.add(it)
        }

        // Kiểm tra đặc biệt: Đêm 29 tháng Chạp nếu tháng thiếu không có ngày 30
        if (lunarDate.month == 12 && lunarDate.day == 29) {
            // Kiểm tra xem ngày mai có phải Mùng 1 Tết không
            val tomorrow = VietCalendarEngine.convertSolar2Lunar(solarDate.day + 1, solarDate.month, solarDate.year)
            if (tomorrow.month == 1 && tomorrow.day == 1) {
                result.add(
                    Holiday(
                        id = "tat_nien_29",
                        name = "Tất Niên & Đêm Giao Thừa (Tháng thiếu)",
                        isLunar = true,
                        day = 29,
                        month = 12,
                        type = HolidayType.LUNAR_TRADITIONAL,
                        subtitle = "29 tháng Chạp",
                        description = "Thời khắc chuyển giao năm cũ sang năm mới của tháng Chạp thiếu ngày 30.",
                        customs = listOf("Cúng Giao thừa đón năm mới", "Ăn bữa cơm Tất Niên sum họp")
                    )
                )
            }
        }

        // Ngày Sóc (Mùng Một) và Ngày Vọng (Ngày Rằm) hàng tháng
        if (lunarDate.day == 1 && result.none { it.id.startsWith("tet_") }) {
            result.add(
                Holiday(
                    id = "mung_mot_${lunarDate.month}",
                    name = "Mùng Một (Ngày Sóc)",
                    isLunar = true,
                    day = 1,
                    month = lunarDate.month,
                    type = HolidayType.LUNAR_TRADITIONAL,
                    subtitle = "Mùng 1 Âm lịch",
                    description = "Ngày đầu tháng âm lịch, thắp hương ban thờ cầu hanh thông may mắn cho cả tháng.",
                    customs = listOf("Thắp hương gia tiên và thần linh", "Ăn chay thanh tịnh", "Làm việc thiện lành")
                )
            )
        } else if (lunarDate.day == 15 && result.none { it.id in listOf("nguyen_tieu", "phat_dan", "vu_lan", "trung_thu", "ha_nguyen") }) {
            result.add(
                Holiday(
                    id = "ngay_ram_${lunarDate.month}",
                    name = "Ngày Rằm (Ngày Vọng)",
                    isLunar = true,
                    day = 15,
                    month = lunarDate.month,
                    type = HolidayType.LUNAR_TRADITIONAL,
                    subtitle = "Rằm 15 Âm lịch",
                    description = "Ngày mặt trăng tròn đầy nhất giữa tháng, ngày vọng linh thiêng hướng về tổ tiên.",
                    customs = listOf("Dâng hương hoa trà quả", "Ăn chay niệm Phật", "Cầu gia đạo bình an thuận hòa")
                )
            )
        }

        return result
    }

    /**
     * Tìm ngày Dương lịch của Mùng 1 Tết Nguyên Đán tiếp theo
     */
    fun getNextTetSolarDate(currentYear: Int, currentMonth: Int, currentDay: Int): SolarDate {
        // Thử tìm Tết năm hiện tại
        val tetThisYear = VietCalendarEngine.convertLunar2Solar(1, 1, currentYear, false)
        if (tetThisYear.year > currentYear ||
            (tetThisYear.year == currentYear && tetThisYear.month > currentMonth) ||
            (tetThisYear.year == currentYear && tetThisYear.month == currentMonth && tetThisYear.day >= currentDay)
        ) {
            return tetThisYear
        }
        // Nếu Tết năm nay đã qua, trả về Tết năm sau
        return VietCalendarEngine.convertLunar2Solar(1, 1, currentYear + 1, false)
    }
}
