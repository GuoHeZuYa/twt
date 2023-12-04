package cn.twt.open.utils;

import cn.afterturn.easypoi.excel.ExcelExportUtil;
import cn.afterturn.easypoi.excel.entity.ExportParams;
import cn.afterturn.easypoi.excel.entity.enmus.ExcelType;
import org.apache.poi.ss.usermodel.Workbook;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Objects;

/**
 * @author Lino
 */
public class ExcelUtils {
    public static void exportExcel(List<?> data, Class<?> dataType, String filename, HttpServletResponse response) {
        ExportParams exportParams = new ExportParams();
        defaultExport(data, dataType, filename, response, exportParams);
    }

    private static void defaultExport(List<?> data, Class<?> dataType, String filename, HttpServletResponse response, ExportParams exportParams) {
        Workbook workbook = ExcelExportUtil.exportExcel(exportParams, dataType, data);
        if (Objects.nonNull(workbook)) {
            downloadFile(filename, response, workbook);
        }
    }

    private static void downloadFile(String filename, HttpServletResponse response, Workbook workbook) {
        OutputStream outputStream=null;
        try {
            response.setCharacterEncoding("UTF-8");
            response.setHeader("Content-Type", "application/vnd.ms-excel");
            response.setHeader("Content-Disposition",
                    "attachment;filename=" + new String(filename.getBytes(), StandardCharsets.ISO_8859_1) + ".xlsx");
            outputStream = response.getOutputStream();

            workbook.write(outputStream);
        } catch (IOException e) {
            throw new RuntimeException(e.getMessage());
        }finally {
            try {
                if (Objects.nonNull(outputStream)) {
                    outputStream.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
