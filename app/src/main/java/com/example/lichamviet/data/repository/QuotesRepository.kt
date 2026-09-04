package com.example.lichamviet.data.repository

object QuotesRepository {

    private val QUOTES = listOf(
        "Bầu ơi thương lấy bí cùng, tuy rằng khác giống nhưng chung một giàn." to "Ca dao Việt Nam",
        "Công cha như núi Thái Sơn, nghĩa mẹ như nước trong nguồn chảy ra." to "Ca dao Việt Nam",
        "Ăn quả nhớ kẻ trồng cây, ăn khoai nhớ kẻ cho dây mà trồng." to "Tục ngữ Việt Nam",
        "Một cây làm chẳng nên non, ba cây chụm lại nên hòn núi cao." to "Ca dao Việt Nam",
        "Uống nước nhớ nguồn, làm con phải hiếu." to "Tục ngữ Việt Nam",
        "Nhiễu điều phủ lấy giá gương, người trong một nước phải thương nhau cùng." to "Ca dao Việt Nam",
        "Lời nói chẳng mất tiền mua, lựa lời mà nói cho vừa lòng nhau." to "Tục ngữ Việt Nam",
        "Thuận vợ thuận chồng, tát biển Đông cũng cạn." to "Tục ngữ Việt Nam",
        "Lá lành đùm lá rách, lá rách đùm lá nát." to "Tục ngữ Việt Nam",
        "Dù ai đi ngược về xuôi, nhớ ngày Giỗ Tổ mùng mười tháng ba." to "Ca dao Việt Nam",
        "Đói cho sạch, rách cho thơm." to "Tục ngữ Việt Nam",
        "Giấy rách phải giữ lấy lề." to "Tục ngữ Việt Nam",
        "Ở hiền gặp lành, gieo gió gặt bão." to "Tục ngữ Việt Nam",
        "Học thầy không tày học bạn." to "Tục ngữ Việt Nam",
        "Khéo ăn thì no, khéo co thì ấm." to "Tục ngữ Việt Nam",
        "Gần mực thì đen, gần đèn thì rạng." to "Tục ngữ Việt Nam",
        "Con có cha như nhà có nóc, con không cha như nòng nọc đứt đuôi." to "Ca dao Việt Nam",
        "Chim có tổ, người có tông, như cây có cội, như sông có nguồn." to "Ca dao Việt Nam",
        "Anh em như thể tay chân, rách lành đùm bọc dở hay đỡ đần." to "Ca dao Việt Nam",
        "Vạn sự khởi đầu nan, gian nan đừng có nản." to "Tục ngữ Việt Nam",
        "Có chí thì nên, có công mài sắt có ngày nên kim." to "Tục ngữ Việt Nam",
        "Đi một ngày đàng, học một sàng khôn." to "Tục ngữ Việt Nam",
        "Tấc đất tấc vàng, mỗi hạt gạo một hạt ngọc trời." to "Tục ngữ Việt Nam",
        "Trâu ơi ta bảo trâu này, trâu ra ngoài ruộng trâu cày với ta." to "Ca dao Việt Nam",
        "Muốn sang thì bắc cầu kiều, muốn con hay chữ thì yêu lấy thầy." to "Ca dao Việt Nam",
        "Ai ơi bưng bát cơm đầy, dẻo thơm một hạt đắng cay muôn phần." to "Ca dao Việt Nam",
        "Cười người chớ vội cười lâu, cười người hôm trước hôm sau người cười." to "Ca dao Việt Nam",
        "Thương người như thể thương thân." to "Tục ngữ Việt Nam",
        "Nhất tự vi sư, bán tự vi sư." to "Tục ngữ dân gian",
        "Tốt gỗ hơn tốt nước sơn, xấu người đẹp nết còn hơn đẹp người." to "Tục ngữ Việt Nam"
    )

    fun getQuoteForDay(dayOfYear: Int): Pair<String, String> {
        val index = (dayOfYear - 1).coerceAtLeast(0) % QUOTES.size
        return QUOTES[index]
    }
}
