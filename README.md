# 📚 Quản Lý Mượn – Trả Sách Thư Viện Mini

![Quản Lý Mượn Trả Sách Thư Viện Mini](https://i.pinimg.com/736x/cb/64/7e/cb647e178bbccfb7498002aa2d070f18.jpg)

Trong các thư viện nhỏ như thư viện lớp học, câu lạc bộ, hoặc nhóm tự học, việc quản lý mượn – trả sách thường gặp khó khăn do quy trình thủ công, dễ sai sót và khó theo dõi.  

Phần mềm **“Quản Lý Mượn – Trả Sách Thư Viện Mini”** được xây dựng để khắc phục những hạn chế này, mang đến **giao diện thân thiện**, **chức năng đầy đủ**, và phù hợp với các thư viện quy mô nhỏ.  

Hệ thống giúp **tự động hóa** quy trình quản lý sách, độc giả, lịch sử mượn – trả, xử lý vi phạm (trễ hạn, mất sách), và cung cấp **báo cáo thống kê chi tiết**. Nhờ đó, việc vận hành thư viện trở nên **khoa học**, **nhanh chóng**, và **hiệu quả**.

---

## 🌟 Tổng quan hệ thống

Phần mềm là một giải pháp **hiện đại, chính xác, tiện lợi** hỗ trợ:  
- **Thủ thư**: Quản lý sách, độc giả, lịch sử mượn – trả, vi phạm, và xuất báo cáo thống kê.  
- **Độc giả**: Tra cứu sách, xem lịch sử mượn, nhận thông báo nhắc nhở, và theo dõi phiếu phạt (nếu có).  

Hệ thống giúp **giảm thiểu sai sót**, **tăng trải nghiệm người dùng**, và đảm bảo **bảo mật** thông qua phân quyền rõ ràng giữa thủ thư và độc giả.

---

## 🎯 Mục tiêu hệ thống

- **Quản lý người dùng**: Hỗ trợ đăng nhập, đăng xuất, đổi mật khẩu, cập nhật thông tin cá nhân an toàn.  
- **Quản lý sách**: Lưu trữ thông tin sách, trạng thái (có sẵn, đang mượn, hư hỏng, mất), phân loại theo thể loại.  
- **Quản lý mượn – trả**: Ghi nhận lịch sử mượn – trả theo thời gian thực, cảnh báo sách trễ hạn.  
- **Xử lý vi phạm**: Quản lý trường hợp trả trễ, hư hỏng, mất sách, tự động tính phí phạt.  
- **Thống kê & báo cáo**: Xuất báo cáo số lượt mượn, vi phạm, doanh thu từ phạt dưới dạng PDF.  
- **Tra cứu tiện lợi**: Tìm kiếm sách theo tên, mã, thể loại; xem lịch sử mượn và phí phạt.  
- **Phân quyền**: Đảm bảo bảo mật, giới hạn quyền truy cập theo vai trò (thủ thư/độc giả).

---

## ⚙️ Chức năng chính

### 🔐 Vai trò: Thủ thư  
- **Đăng nhập/Đăng xuất**  
- **Quản lý độc giả**:  
  - Thêm, sửa, xóa độc giả.  
  - Xem danh sách độc giả.  
- **Quản lý sách**:  
  - Thêm, sửa, xóa sách.  
  - Phân loại sách theo thể loại.  
- **Quản lý mượn – trả**:  
  - Ghi nhận mượn/trả sách.  
  - Kiểm tra trạng thái trả sách (đúng hạn/trễ hạn).  
- **Quản lý vi phạm**:  
  - Ghi nhận vi phạm: trả trễ, hư hỏng, mất sách.  
  - Tính và lưu thông tin tiền phạt.  
- **Thống kê & báo cáo**:  
  - Thống kê lượt mượn – trả theo tháng/năm.  
  - Thống kê vi phạm và doanh thu từ phạt.  
  - Xuất báo cáo PDF.  

### 🙋‍♂️ Vai trò: Độc giả  
- **Đăng nhập/Đăng xuất**  
- **Quản lý thông tin cá nhân**: Cập nhật thông tin, đổi mật khẩu.  
- **Tra cứu sách**: Tìm kiếm theo tên, mã, thể loại, hoặc trạng thái.  
- **Xem lịch sử mượn**:  
  - Danh sách sách đã mượn.  
  - Ngày mượn, hạn trả, trạng thái trả.  
- **Nhận thông báo**: Nhắc nhở sách sắp đến hạn hoặc trễ hạn.  
- **Xem phiếu phạt**: Xem lý do, số tiền phạt, trạng thái thanh toán.

---

## 🛠️ Công nghệ sử dụng

### 📐 Mô hình kiến trúc  
- **Mô hình**: MVC (Model - View - Controller)

### 💻 Ngôn ngữ lập trình & Công cụ phát triển  
| Thành phần                  | Công nghệ / Thư viện       |  
|----------------------------|---------------------------|  
| Ngôn ngữ chính             | Java                     |  
| IDE                        | VS Code, Eclipse         |  
| Hệ thống quản lý mã nguồn  | GitHub                   |  
| Quản lý thư viện & build   | Maven                    |  

### 🖥️ Giao diện người dùng (User Interface)  
| Thành phần                  | Công nghệ / Thư viện       |  
|----------------------------|---------------------------|  
| Giao diện ứng dụng         | Java Swing               |  
| Thiết kế mockup            | Figma                    |  
| Kiểm tra đầu vào           | Regex (Biểu thức chính quy) |  

### 🗄️ Xử lý dữ liệu & Bảo mật  
| Thành phần                  | Công nghệ / Thư viện       |  
|----------------------------|---------------------------|  
| Cơ sở dữ liệu              | MySQL                    |  
| Kết nối CSDL               | JDBC                     |  
| Mã hóa mật khẩu            | SHA-1                    |  

### 📊 Thống kê & Báo cáo  
| Thành phần                  | Công nghệ / Thư viện       |  
|----------------------------|---------------------------|  
| Vẽ biểu đồ                 | JFreeChart               |  
| Xuất báo cáo PDF           | Apache PDFBox            |  

---

## 🔗 Tài liệu tham khảo  
- **Link Figma**: [Thiết kế giao diện](https://www.figma.com/your-figma-link-here)  
- **Link Video tham khảo**: [Video demo hệ thống](https://www.youtube.com/your-video-link-here) 

---

## 💡 Lợi ích của hệ thống  
- **Tiết kiệm thời gian**: Tự động hóa quy trình quản lý, giảm công việc thủ công.  
- **Tăng độ chính xác**: Hạn chế sai sót trong theo dõi mượn – trả và tính toán phí phạt.  
- **Nâng cao trải nghiệm**: Giao diện thân thiện, dễ sử dụng cho cả thủ thư và độc giả.  
- **Bảo mật cao**: Phân quyền rõ ràng, mã hóa mật khẩu đảm bảo an toàn thông tin.

---

*Phần mềm được phát triển bởi nhóm sinh viên với mục tiêu mang lại giải pháp quản lý thư viện mini hiệu quả và hiện đại. Mọi góp ý xin vui lòng liên hệ qua Email: phinew331@gmail.com!*
