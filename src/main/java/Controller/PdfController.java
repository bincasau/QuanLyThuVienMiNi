package Controller;

import Model.ThongTinThongKe;
import DAO.ThongTinThongKeDao;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.font.PDType0Font;
import org.apache.pdfbox.pdmodel.graphics.image.PDImageXObject;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartUtils;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;

import javax.swing.*;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.List;

public class PdfController {

    public void exportToPDF(int year) {
        List<ThongTinThongKe> ds = ThongTinThongKeDao.getInstance().layDanhSachTheoNam(year);
        String outputDir = "D:/Thống kê";
        String dest = outputDir + "/statistics_report_" + year + ".pdf";

        // Ensure the output directory exists
        try {
            Files.createDirectories(Paths.get(outputDir));
        } catch (IOException ex) {
            JOptionPane.showMessageDialog(null, 
                "Không thể tạo thư mục " + outputDir + ": " + ex.getMessage(), 
                "Lỗi", JOptionPane.ERROR_MESSAGE);
            return;
        }

        try (PDDocument document = new PDDocument()) {
            PDPage page = new PDPage(PDRectangle.A4);
            document.addPage(page);

            PDType0Font font = PDType0Font.load(document, new File("font/font-times-new-roman.ttf"));
            PDPageContentStream content = new PDPageContentStream(document, page);

            float pageWidth = PDRectangle.A4.getWidth();
            float yStart = 780;
            float y = yStart;
            float leading = 15;

            // Tiêu đề báo cáo (căn giữa)
            content.beginText();

            // Tiêu đề "NHÀ SÁCH MINI"
            content.setFont(font, 16);
            String title = "NHÀ SÁCH MINI";
            float titleWidth = font.getStringWidth(title) / 1000 * 16;
            content.newLineAtOffset((pageWidth - titleWidth) / 2, y);
            content.showText(title);

            // Tiêu đề "THỐNG KÊ HOẠT ĐỘNG THƯ VIỆN NĂM"
            y -= leading;
            content.setFont(font, 14);
            String subtitle = "THỐNG KÊ HOẠT ĐỘNG THƯ VIỆN NĂM " + year;
            float subtitleWidth = font.getStringWidth(subtitle) / 1000 * 14;
            content.newLineAtOffset(-((pageWidth - titleWidth) / 2), -leading);
            content.newLineAtOffset((pageWidth - subtitleWidth) / 2, 0);
            content.showText(subtitle);

            // Ngày in
            y -= leading;
            content.setFont(font, 12);
            String dateText = "Ngày in: " + LocalDate.now();
            float dateWidth = font.getStringWidth(dateText) / 1000 * 12;
            content.newLineAtOffset(-((pageWidth - subtitleWidth) / 2), -leading);
            content.newLineAtOffset((pageWidth - dateWidth) / 2, 0);
            content.showText(dateText);

            content.endText();
            content.close();

            // Append nội dung vào trang đầu để thêm biểu đồ cột
            content = new PDPageContentStream(document, page, PDPageContentStream.AppendMode.APPEND, true);

            float margin = 50;

            // Biểu đồ 1: Biểu đồ cột cho số lượng
            DefaultCategoryDataset barDataset = new DefaultCategoryDataset();
            for (ThongTinThongKe thongKe : ds) {
                String month = "" + thongKe.getThang();
                barDataset.addValue(thongKe.getSoDocGiaMoi(), "Đăng ký mới", month);
                barDataset.addValue(thongKe.getSoLuongTraSachTre(), "Trả sách trễ", month);
                barDataset.addValue(thongKe.getSoLuongLamHuSach(), "Hư sách", month);
                barDataset.addValue(thongKe.getSoLuongMatSach(), "Mất sách", month);
            }

            JFreeChart barChart = ChartFactory.createBarChart(
                "Thống kê số lượng theo tháng - Năm " + year,
                "Tháng", "Số lượng",
                barDataset, PlotOrientation.VERTICAL, true, true, false
            );

            File barChartFile = File.createTempFile("bar_chart", ".png");
            ChartUtils.saveChartAsPNG(barChartFile, barChart, 500, 300);

            PDImageXObject barChartImage = PDImageXObject.createFromFile(barChartFile.getAbsolutePath(), document);
            content.drawImage(barChartImage, margin, yStart - 320, 500, 300);

            content.close();

            // Trang mới cho biểu đồ 2 (biểu đồ đường)
            page = new PDPage(PDRectangle.A4);
            document.addPage(page);
            content = new PDPageContentStream(document, page);

            XYSeries revenueSeries = new XYSeries("Doanh thu");
            for (ThongTinThongKe thongKe : ds) {
                revenueSeries.add(thongKe.getThang(), thongKe.getDoanhThu());
            }

            XYSeriesCollection lineDataset = new XYSeriesCollection();
            lineDataset.addSeries(revenueSeries);

            JFreeChart lineChart = ChartFactory.createXYLineChart(
                "Doanh thu theo tháng - Năm " + year,
                "Tháng", "Doanh thu (VND)",
                lineDataset, PlotOrientation.VERTICAL, true, true, false
            );

            File lineChartFile = File.createTempFile("line_chart", ".png");
            ChartUtils.saveChartAsPNG(lineChartFile, lineChart, 500, 300);

            PDImageXObject lineChartImage = PDImageXObject.createFromFile(lineChartFile.getAbsolutePath(), document);
            content.drawImage(lineChartImage, margin, yStart - 350, 500, 300);

            content.close();

            // Xoá file tạm
            barChartFile.delete();
            lineChartFile.delete();

            document.save(dest);
            JOptionPane.showMessageDialog(null, 
                "Tệp PDF đã được tạo tại: " + dest, 
                "Thành công", JOptionPane.INFORMATION_MESSAGE);

        } catch (IOException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(null, 
                "Lỗi khi xuất PDF: " + ex.getMessage(), 
                "Lỗi", JOptionPane.ERROR_MESSAGE);
        }
    }
}