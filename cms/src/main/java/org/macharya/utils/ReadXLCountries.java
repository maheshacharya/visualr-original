package org.macharya.utils;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.io.FileInputStream;
import java.util.Iterator;

public class ReadXLCountries {
    public static void main(String[] args) {
        try {
            FileInputStream file = new FileInputStream(new File("/Users/maheshacharya/downloads/ISOCountryCodes081507.xls.xlsx"));

            //Create Workbook instance holding reference to .xlsx file
            XSSFWorkbook workbook = new XSSFWorkbook(file);

            //Get first/desired sheet from the workbook
            XSSFSheet sheet = workbook.getSheetAt(0);

            //Iterate through each rows one by one
            Iterator<Row> rowIterator = sheet.iterator();
            StringBuilder builder = new StringBuilder();

            while (rowIterator.hasNext()) {
                builder.append("<sv:node sv:name=\"selection:listitem\">\n");
                builder.append("<sv:property sv:name=\"jcr:primaryType\" sv:type=\"Name\">\n");
                builder.append(" <sv:value>selection:listitem</sv:value>");
                builder.append("</sv:property>\n");

                Row row = rowIterator.next();
                //For each row, iterate through all the columns
                Iterator<Cell> cellIterator = row.cellIterator();
                int i = 0;
                while (cellIterator.hasNext()) {
                    Cell cell = cellIterator.next();
                    //Check the cell type and format accordingly

                    switch (cell.getCellType()) {
                        case Cell.CELL_TYPE_NUMERIC:
                            // builder.append("..numeric........"+cell.getCellType());
                            // System.out.print(cell.getNumericCellValue() + "\t");
                            builder.append("<sv:value>" + cell.getNumericCellValue() + "</sv:value>\n");
                            builder.append("</sv:property>\n");
                            break;
                        case Cell.CELL_TYPE_STRING:
                            if (cell.getStringCellValue().length() < 3) {
                                builder.append("<sv:property sv:name=\"selection:key\" sv:type=\"String\">\n");
                                builder.append("<sv:value>" + cell.getStringCellValue().toUpperCase() + "</sv:value>\n");
                                builder.append("</sv:property>\n");
                            } else {
                                //  builder.append("...string......."+cell.getCellType());
                                builder.append(" <sv:property sv:name=\"selection:label\" sv:type=\"String\">\n");
                                builder.append("<sv:value>" + cell.getStringCellValue().replaceAll("&", "and") + "</sv:value>\n");
                                builder.append(" </sv:property>\n");
                            }
                            break;
                    }
                }

                builder.append("</sv:node>\n");
                builder.append("\n");
            }
            file.close();
            System.out.println(builder.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}