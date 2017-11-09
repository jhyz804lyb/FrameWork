package com.jh.utils;

import org.apache.poi.hssf.usermodel.*;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.beans.*;
import java.io.*;
import java.lang.reflect.Method;
import java.net.URLEncoder;
import java.text.*;
import java.util.*;

/**
 * 类名称：ExcelUtil <br>
 * 类描述：导出EXCEL工具类 <br>
 * 创建人：李亚彬 <br>
 * 创建时间：2016年1月5日 下午1:43:18 <br>
 * 修改人： <br>
 * 修改时间： <br>
 * 修改备注： <br>
 * * @version V1.0
 */
public class ExcelUtil
{
    public static final int NUMBER_TYPE = 1;//输字类型
    public static final int STRING_TYPE = 0;//字符串类型

    /**
     * 判断字符是否可以装换成数字<br>
     * <br>
     * 创建人： 李亚彬 <br>
     * 创建时间： 2016年1月4日 上午11:01:36
     * Copyright (c)2016 <br>
     * * @param str
     * * @return
     */
    private static boolean isPreaseNumber(String str)
    {
        if (null == str || "".equals(str))
        {
            return false;
        }
        int len = str.length();
        if (str.indexOf(".") == 0)
            return false;
        if (str.lastIndexOf(".") != str.indexOf("."))
            return false;
        for (int i = len; --i >= 0; )
        {
            int chr = str.charAt(i);
            if ((chr < 48 && chr != 46) || chr > 57)
                return false;
        }
        return true;
    }

    /**
     * 导出数据<br>
     * <br>
     * 创建人： 李亚彬 <br>
     * 创建时间： 2016年1月5日 下午2:54:57
     * Copyright (c)2016 <br>
     * * @param localHSSFWorkbook 工作簿
     * * @param paramList 表头
     * * @param paramList1 数据源
     * * @param paramHttpServletResponse 响应对象
     * * @param paramString 文件名
     */
    public static void exportBigDataExcell(SXSSFWorkbook localHSSFWorkbook, List<String> paramList,
                                           List<Object[]> paramList1, HttpServletResponse paramHttpServletResponse,
                                           String paramString)
    {
        ServletOutputStream localServletOutputStream = null;
        try
        {
            // SXSSFWorkbook localHSSFWorkbook = new SXSSFWorkbook(1000);
            localServletOutputStream = paramHttpServletResponse.getOutputStream();
            Sheet localHSSFSheet = localHSSFWorkbook.createSheet();
            localHSSFWorkbook.setSheetName(0, paramString);
            Row localHSSFRow1 = localHSSFSheet.createRow(0);
            Row localHSSFRow2 = null;
            localHSSFSheet.createFreezePane(0, 1);
            CellStyle titlestyle = getTitleStyle(localHSSFWorkbook);
            CellStyle bodystyle = getbodyStyle(localHSSFWorkbook);
            int i = 0;
            int j = paramList.size();
            for (int index = 0; index < j; index++)
            {
                localHSSFSheet.autoSizeColumn(index, true);
                localHSSFSheet.setColumnWidth(index, 4000);
            }//把列宽个设置为自动
            localHSSFRow1.setHeight((short) 400);
            while (i < j)
            {
                createCell(titlestyle, localHSSFRow1, i, (String) paramList.get(i), false);
                i++;
            }
            i = 1;
            j = paramList1.size();
            while (i <= j)
            {
                localHSSFRow2 = localHSSFSheet.createRow(i);
                localHSSFRow2.setHeight((short) 400);
                Object[] arrayOfObject = (Object[]) paramList1.get(i - 1);
                for (int k = 0; k < arrayOfObject.length; k++)
                    createCell(bodystyle, localHSSFRow2, k, arrayOfObject[k] == null ? "" : arrayOfObject[k].toString(),
                            false);
                i++;
            }
            localServletOutputStream = paramHttpServletResponse.getOutputStream();
            paramHttpServletResponse.reset();
            paramHttpServletResponse.addHeader("Content-Disposition",
                    String.format("attachment;filename*=utf-8'zh_cn'%s.xls",
                            new Object[]{URLEncoder.encode(paramString, "utf-8")}));
            paramHttpServletResponse.setCharacterEncoding("UTF-8");
            paramHttpServletResponse.setContentType("application/x-msdownload");
            paramHttpServletResponse.flushBuffer();
            localHSSFWorkbook.write(localServletOutputStream);
            localServletOutputStream.flush();
            localServletOutputStream.close();

            return;
        }
        catch (IOException localIOException1)
        {

        }
        finally
        {
            try
            {
                localServletOutputStream.close();
            }
            catch (IOException localIOException4)
            {

            }
        }
    }

    /**
     * 重载导出,控制列的数据是字符串还是数字<br>
     * <br>
     * 创建人： 李亚彬 <br>
     * 创建时间： 2016年1月5日 下午3:45:32
     * Copyright (c)2016 <br>
     * * @param localHSSFWorkbook 工作簿
     * * @param paramList 表头数据
     * * @param paramList1 数据源
     * * @param paramHttpServletResponse 响应对象
     * * @param paramString 文件名
     * * @param dataType 类型标识数组，制定列是数字还是字符。 数组下标表示列，值决定是否数字(1:数字;0:字符)
     */
    public static void exportBigDataExcell(SXSSFWorkbook Workbook, List<String> titleList, List<Object[]> dataList,
                                           HttpServletResponse response, String fileName, int[] dataType)
    {
        ServletOutputStream localServletOutputStream = null;
        try
        {
            // SXSSFWorkbook localHSSFWorkbook = new SXSSFWorkbook(1000);
            localServletOutputStream = response.getOutputStream();
            Sheet localHSSFSheet = Workbook.createSheet();
            Workbook.setSheetName(0, fileName);
            Row localHSSFRow1 = localHSSFSheet.createRow(0);
            Row localHSSFRow2 = null;
            localHSSFSheet.createFreezePane(0, 1);
            int i = 0;
            int j = titleList.size();
            localHSSFRow1.setHeight((short) 400);
            CellStyle titlestyle = getTitleStyle(Workbook);
            CellStyle bodystyle = getbodyStyle(Workbook);
            for (int index = 0; index < j; index++)
            {
                localHSSFSheet.autoSizeColumn(index, true);
                localHSSFSheet.setColumnWidth(index, 4000);
            }//把列宽个设置为自动
            while (i < j)
            {
                createCell(titlestyle, localHSSFRow1, i, (String) titleList.get(i), false);
                i++;
            }
            i = 1;
            j = dataList.size();
            while (i <= j)
            {
                localHSSFRow2 = localHSSFSheet.createRow(i);
                localHSSFRow2.setHeight((short) 400);
                Object[] arrayOfObject = (Object[]) dataList.get(i - 1);
                for (int k = 0; k < arrayOfObject.length; k++)
                {
                    if (dataType != null)
                    {
                        if (k < dataType.length && dataType[k] == NUMBER_TYPE)
                        {
                            createCell(bodystyle, localHSSFRow2, k,
                                    arrayOfObject[k] == null ? "" : arrayOfObject[k].toString(),
                                    isPreaseNumber(arrayOfObject[k] == null ? "" : arrayOfObject[k].toString()) ? true :
                                            false);
                        }
                        else if (k < dataType.length && dataType[k] == STRING_TYPE)
                        {
                            createCell(bodystyle, localHSSFRow2, k,
                                    arrayOfObject[k] == null ? "" : arrayOfObject[k].toString(), false);
                        }
                        else
                        {
                            createCell(bodystyle, localHSSFRow2, k,
                                    arrayOfObject[k] == null ? "" : arrayOfObject[k].toString(), false);
                        }
                    }
                    else
                    {
                        createCell(bodystyle, localHSSFRow2, k,
                                arrayOfObject[k] == null ? "" : arrayOfObject[k].toString(), false);
                    }
                }

                i++;
            }
            localServletOutputStream = response.getOutputStream();
            response.reset();
            response.addHeader("Content-Disposition", String.format("attachment;filename*=utf-8'zh_cn'%s.xls",
                    new Object[]{URLEncoder.encode(fileName, "utf-8")}));
            response.setCharacterEncoding("UTF-8");
            response.setContentType("application/x-msdownload");
            response.flushBuffer();
            Workbook.write(localServletOutputStream);
            localServletOutputStream.flush();
            localServletOutputStream.close();
            return;
        }
        catch (IOException localIOException1)
        {

        }
        finally
        {
            try
            {
                localServletOutputStream.close();
            }
            catch (IOException localIOException4)
            {

            }
        }
    }

    /**
     * 导出制定样式的数据<br>
     * <br>
     * 创建人： 李亚彬 <br>
     * 创建时间： 2016年1月5日 下午3:27:06
     * Copyright (c)2016 <br>
     * * @param localHSSFWorkbook 工作簿
     * * @param paramList 表头
     * * @param paramList1 数据源
     * * @param paramHttpServletResponse 响应对象
     * * @param paramString 文件名
     * * @param dataType 类型标识数组，制定列是数字还是字符。 数组下标表示列，值决定是否数字(1:数字;0:字符)
     * * @param localHSSFCellStyle 单元格的样式
     * * @param localHSSFSheet steel
     */
    public static void exportBigDataExcell(SXSSFWorkbook workbook, List<String> titleList, List<Object[]> dataList,
                                           HttpServletResponse response, String fileName, int[] dataType,
                                           CellStyle cellstyle, Sheet sheet)
    {
        ServletOutputStream localServletOutputStream = null;
        try
        {
            // SXSSFWorkbook localHSSFWorkbook = new SXSSFWorkbook(1000);
            localServletOutputStream = response.getOutputStream();
            if (sheet == null)
                sheet = workbook.createSheet();
            if (null == cellstyle)
            {
                cellstyle = workbook.createCellStyle();
            }
            workbook.setSheetName(0, fileName);
            Row localHSSFRow1 = sheet.createRow(0);
            Row localHSSFRow2 = null;
            sheet.createFreezePane(0, 1);
            cellstyle.setDataFormat(HSSFDataFormat.getBuiltinFormat("#.###"));
            int i = 0;
            int j = titleList.size();
            while (i < j)
            {
                createCell(cellstyle, localHSSFRow1, i, (String) titleList.get(i), false);
                i++;
            }
            i = 1;
            j = dataList.size();
            while (i <= j)
            {
                localHSSFRow2 = sheet.createRow(i);
                Object[] arrayOfObject = (Object[]) dataList.get(i - 1);
                for (int k = 0; k < arrayOfObject.length; k++)
                {
                    if (dataType != null)
                    {
                        if (k < dataType.length && dataType[k] == NUMBER_TYPE)
                        {
                            createCell(cellstyle, localHSSFRow2, k,
                                    arrayOfObject[k] == null ? "" : arrayOfObject[k].toString(),
                                    isPreaseNumber(arrayOfObject[k] == null ? "" : arrayOfObject[k].toString()) ? true :
                                            false);
                        }
                        else if (k < dataType.length && dataType[k] == STRING_TYPE)
                        {
                            createCell(cellstyle, localHSSFRow2, k,
                                    arrayOfObject[k] == null ? "" : arrayOfObject[k].toString(), false);
                        }
                        else
                        {
                            createCell(cellstyle, localHSSFRow2, k,
                                    arrayOfObject[k] == null ? "" : arrayOfObject[k].toString(), false);
                        }
                    }
                    else
                    {
                        createCell(cellstyle, localHSSFRow2, k,
                                arrayOfObject[k] == null ? "" : arrayOfObject[k].toString(),
                                isPreaseNumber(arrayOfObject[k] == null ? "" : arrayOfObject[k].toString()) ? true :
                                        false);
                    }
                }

                i++;
            }
            localServletOutputStream = response.getOutputStream();
            response.reset();
            response.addHeader("Content-Disposition", String.format("attachment;filename*=utf-8'zh_cn'%s.xls",
                    new Object[]{URLEncoder.encode(fileName, "utf-8")}));
            response.setCharacterEncoding("UTF-8");
            response.setContentType("application/x-msdownload");
            response.flushBuffer();
            workbook.write(localServletOutputStream);
            localServletOutputStream.flush();
            localServletOutputStream.close();
            return;
        }
        catch (IOException localIOException1)
        {

        }
        finally
        {
            try
            {
                localServletOutputStream.close();
            }
            catch (IOException localIOException4)
            {

            }
        }
    }

    //创建单元格样式
    private static void createCell(CellStyle localHSSFCellStyle, Row paramHSSFRow, int paramShort, String paramString,
                                   boolean num)
    {
        Cell localHSSFCell = paramHSSFRow.createCell(paramShort);
        localHSSFCellStyle.setAlignment((short) 6);
        localHSSFCell.setCellStyle(localHSSFCellStyle);
        if (paramString == null)
            localHSSFCell.setCellValue("");
        else
        {
            if (num && !"".equals(paramString))
            {
                localHSSFCell.setCellType(HSSFCell.CELL_TYPE_NUMERIC);
                localHSSFCell.setCellValue(Double.valueOf(paramString));
            }
            else
            {
                localHSSFCell.setCellValue(paramString);
                localHSSFCell.setCellType(1);
            }
        }
    }

    /**
     * TODO(请输入方法的描述)<br>
     * <br>
     * 创建人： 李亚彬 <br>
     * 创建时间： 2016年1月11日 下午1:58:35
     * Copyright (c)2016 <br>
     * * @param localHSSFCellStyle
     * * @param paramHSSFRow
     * * @param paramShort
     * * @param paramString
     * * @param num
     */
    private static void createCellItem(CellStyle localHSSFCellStyle, Row paramHSSFRow, int paramShort,
                                       String paramString, boolean num)
    {
        Cell localHSSFCell = paramHSSFRow.createCell(paramShort);
        localHSSFCellStyle.setAlignment((short) 6);
        //localHSSFCellStyle.setFillForegroundColor(CellStyle.BORDER_HAIR);
        localHSSFCell.setCellStyle(localHSSFCellStyle);
        if (paramString == null)
            localHSSFCell.setCellValue("");
        else
        {
            if (num && !"".equals(paramString))
            {
                localHSSFCell.setCellType(HSSFCell.CELL_TYPE_NUMERIC);
                localHSSFCell.setCellValue(Double.valueOf(paramString));
            }
            else
            {
                localHSSFCell.setCellValue(paramString);
                localHSSFCell.setCellType(1);
            }
        }
    }

    /**
     * 导出优化<br>
     * <br>
     * 创建人： 李亚彬 <br>
     * 创建时间： 2016年1月11日 下午1:32:26
     * Copyright (c)2016 <br>
     * * @param Workbook
     * * @param titleList
     * * @param dataList
     * * @param response
     * * @param fileName 文件名
     * * @param dataType 类型标识数组，制定列是数字还是字符。 数组下标表示列，值决定是否数字(1:数字;0:字符)
     */
    public static void ExportFile(SXSSFWorkbook Workbook, List<String> titleList,
                                  List<Object[]> dataList, HttpServletResponse response,
                                  String fileName, int[] dataType, int[] cellWidth, int cell_Height)
    {
        ServletOutputStream localServletOutputStream = null;
        try
        {
            localServletOutputStream = response.getOutputStream();
            Sheet localHSSFSheet = Workbook.createSheet();
            Workbook.setSheetName(0, fileName);
            Row localHSSFRow1 = localHSSFSheet.createRow(0);
            Row localHSSFRow2 = null;
            localHSSFSheet.createFreezePane(0, 1);
            localHSSFRow1.setHeight((short) (100 * cell_Height));
            CellStyle titlestyle = getTitleStyle(Workbook);
            CellStyle bodystyle = getbodyStyle(Workbook);
            int i = 0;
            int j = titleList.size();
            for (int index = 0; index < j; index++)
            {
                //localHSSFSheet.autoSizeColumn(index, true);
                if (cellWidth != null && cellWidth.length == j)
                    localHSSFSheet.setColumnWidth(index, 1000 * cellWidth[index]);
                else
                    localHSSFSheet.autoSizeColumn(index, true);

            }//把列宽个设置为自动


            for (String title : titleList)
            {
                createCell(titlestyle, localHSSFRow1, i, title, false);
                i++;
            }
            i = 1;
            j = dataList.size();
            for (Object[] objs : dataList)
            {
                localHSSFRow2 = localHSSFSheet.createRow(i);
                localHSSFRow2.setHeight((short) 400);
                int count = 0;
                for (Object obj : objs)
                {
                    if (dataType != null)
                    {
                        if (count < dataType.length && dataType[count] == NUMBER_TYPE)
                        {
                            createCell(bodystyle, localHSSFRow2, count, obj == null ? "" : obj.toString(),
                                    isPreaseNumber(obj == null ? "" : obj.toString()) ? true : false);
                        }
                        else if (count < dataType.length && dataType[count] == STRING_TYPE)
                        {
                            createCell(bodystyle, localHSSFRow2, count, obj == null ? "" : obj.toString(), false);
                        }
                        else
                        {
                            createCell(bodystyle, localHSSFRow2, count, obj == null ? "" : obj.toString(), false);
                        }
                    }
                    else
                    {
                        createCell(bodystyle, localHSSFRow2, count, obj == null ? "" : obj.toString(), false);
                    }
                    count++;
                }
                i++;
            }
            localServletOutputStream = response.getOutputStream();
            response.reset();
            response.addHeader("Content-Disposition", String.format("attachment;filename*=utf-8'zh_cn'%s.xls",
                    new Object[]{URLEncoder.encode(fileName, "utf-8")}));
            response.setCharacterEncoding("UTF-8");
            response.setContentType("application/x-msdownload");
            response.flushBuffer();
            Workbook.write(localServletOutputStream);
            localServletOutputStream.flush();
            localServletOutputStream.close();
            return;
        }
        catch (IOException localIOException1)
        {

        }
        finally
        {
            try
            {
                localServletOutputStream.close();
            }
            catch (IOException localIOException4)
            {

            }
        }


    }

    /**
     * 导出优化<br>
     * <br>
     * 创建人： 李亚彬 <br>
     * 创建时间： 2016年1月11日 下午1:32:26
     * Copyright (c)2016 <br>
     * * @param Workbook
     * * @param titleList
     * * @param dataList
     * * @param response
     * * @param fileName 文件名
     * * @param dataType 类型标识数组，制定列是数字还是字符。 数组下标表示列，值决定是否数字(1:数字;0:字符)
     */
    public static void ExportFile(SXSSFWorkbook Workbook, List<String> titleList,
                                  List<Object[]> dataList, HttpServletResponse response,
                                  String fileName, int[] dataType)
    {
        ServletOutputStream localServletOutputStream = null;
        try
        {
            localServletOutputStream = response.getOutputStream();
            Sheet localHSSFSheet = Workbook.createSheet();
            Workbook.setSheetName(0, fileName);
            Row localHSSFRow1 = localHSSFSheet.createRow(0);
            Row localHSSFRow2 = null;
            localHSSFSheet.createFreezePane(0, 1);
            localHSSFRow1.setHeight((short) 400);
            CellStyle titlestyle = getTitleStyle(Workbook);
            CellStyle bodystyle = getbodyStyle(Workbook);
            int i = 0;
            int j = titleList.size();
            for (int index = 0; index < j; index++)//把列宽个设置为自动
                //localHSSFSheet.autoSizeColumn(index, true);
                localHSSFSheet.setColumnWidth(index, 1000 * 10);

            for (String title : titleList)
            {
                createCell(titlestyle, localHSSFRow1, i, title, false);
                i++;
            }
            i = 1;
            j = dataList.size();
            for (Object[] objs : dataList)
            {
                localHSSFRow2 = localHSSFSheet.createRow(i);
                localHSSFRow2.setHeight((short) 400);
                int count = 0;
                for (Object obj : objs)
                {
                    if (dataType != null)
                    {
                        if (count < dataType.length && dataType[count] == NUMBER_TYPE)
                        {
                            createCell(bodystyle, localHSSFRow2, count, obj == null ? "" : obj.toString(),
                                    isPreaseNumber(obj == null ? "" : obj.toString()) ? true : false);
                        }
                        else if (count < dataType.length && dataType[count] == STRING_TYPE)
                        {
                            createCell(bodystyle, localHSSFRow2, count, obj == null ? "" : obj.toString(), false);
                        }
                        else
                        {
                            createCell(bodystyle, localHSSFRow2, count, obj == null ? "" : obj.toString(), false);
                        }
                    }
                    else
                    {
                        createCell(bodystyle, localHSSFRow2, count, obj == null ? "" : obj.toString(), false);
                    }
                    count++;
                }
                i++;
            }
            localServletOutputStream = response.getOutputStream();
            response.reset();
            response.addHeader("Content-Disposition", String.format("attachment;filename*=utf-8'zh_cn'%s.xls",
                    new Object[]{URLEncoder.encode(fileName, "utf-8")}));
            response.setCharacterEncoding("UTF-8");
            response.setContentType("application/x-msdownload");
            response.flushBuffer();
            Workbook.write(localServletOutputStream);
            localServletOutputStream.flush();
            localServletOutputStream.close();
            return;
        }
        catch (IOException localIOException1)
        {

        }
        finally
        {
            try
            {
                localServletOutputStream.close();
            }
            catch (IOException localIOException4)
            {

            }
        }


    }

    /**
     * 导出含有子项的excul<br>
     * <br>
     * 创建人： 李亚彬 <br>
     * 创建时间： 2016年1月11日 下午3:06:59
     * Copyright (c)2016 <br>
     * * @param Workbook  工作簿对象
     * * @param bodytitleList 主项表头
     * * @param bodydataList  主项信息
     * * @param itemtitleList 子项表头
     * * @param itemdataMap   子项数据 :key：主项某一列
     * * @param response      响应对象
     * * @param fileName      文件名
     * * @param dataType	  主项是否为数字标志，可以为空
     * * @param itemdataType 子项是否为数字标志，可以为空
     * * @param keyWord      关联列的序号 不可为空
     * * @param isShowKey    是否显示关联列 不为空
     */
    public static void ExportHasItemFile(SXSSFWorkbook Workbook, List<String> bodytitleList,
                                         List<Object[]> bodydataList, List<String> itemtitleList,
                                         Map<Object, List<Object[]>> itemdataMap, HttpServletResponse response,
                                         String fileName, int[] dataType, int[] itemdataType, int keyWord,
                                         boolean isShowKey)
    {
        ServletOutputStream localServletOutputStream = null;
        try
        {
            localServletOutputStream = response.getOutputStream();
            Sheet localHSSFSheet = Workbook.createSheet();
            Workbook.setSheetName(0, fileName);
            Row localHSSFRow1 = null;
            Row localHSSFRow2 = null;
            Row localHSSFRow3 = null;
            Row localHSSFRow4 = null;

            localHSSFSheet.createFreezePane(0, 1);
            CellStyle titlestyle = getTitleStyle(Workbook);
            CellStyle bodystyle = getbodyStyle(Workbook);
            CellStyle itemtitlestyle = getItemTitleStyle(Workbook);
            int j = bodytitleList.size();
            for (int index = 0; index < j; index++)
            {//把列宽个设置为自动
                localHSSFSheet.autoSizeColumn(index, true);
                localHSSFSheet.setColumnWidth(index, 4000);
            }
            int i = 0;
            j = bodydataList.size();
            for (Object[] objs : bodydataList)
            {//主表表头构建
                localHSSFRow1 = localHSSFSheet.createRow(i);
                localHSSFRow1.setHeight((short) 400);
                i++;
                int c = 0;
                for (String title : bodytitleList)
                {
                    createCell(titlestyle, localHSSFRow1, c, title, false);
                    c++;
                }
                localHSSFRow2 = localHSSFSheet.createRow(i);
                localHSSFRow2.setHeight((short) 400);
                int count = 0;
                for (Object obj : objs)
                {//主表信息构建
                    if (obj != null && !isShowKey)
                    {//如果不显示关连列
                        if (objs != null && obj.equals(objs[keyWord]))
                            continue;
                    }
                    if (dataType != null)
                    {
                        if (count < dataType.length && dataType[count] == NUMBER_TYPE)
                        {
                            createCell(bodystyle, localHSSFRow2, count, obj == null ? "" : obj.toString(),
                                    isPreaseNumber(obj == null ? "" : obj.toString()) ? true : false);
                        }
                        else if (count < dataType.length && dataType[count] == STRING_TYPE)
                        {
                            createCell(bodystyle, localHSSFRow2, count, obj == null ? "" : obj.toString(), false);
                        }
                        else
                        {
                            createCell(bodystyle, localHSSFRow2, count, obj == null ? "" : obj.toString(), false);
                        }
                    }
                    else
                    {
                        createCell(bodystyle, localHSSFRow2, count, obj == null ? "" : obj.toString(), false);
                    }
                    count++;
                }
                //开始加载子项表头
                i++;
                localHSSFRow4 = localHSSFSheet.createRow(i);
                localHSSFRow4.setHeight((short) 400);
                i++;
                c = 0;
                for (String title : itemtitleList)
                {
                    createCellItem(itemtitlestyle, localHSSFRow4, c, title, false);
                    c++;
                }
                if (keyWord < 0 || keyWord >= objs.length)
                    break;
                for (Object[] itemobjs : itemdataMap.get(objs[keyWord]))
                {//加载子项
                    count = 0;
                    localHSSFRow3 = localHSSFSheet.createRow(i);
                    localHSSFRow3.setHeight((short) 400);
                    i++;
                    for (Object obj : itemobjs)
                    {//主表信息构建
                        if (itemdataType != null)
                        {
                            if (count < itemdataType.length && itemdataType[count] == NUMBER_TYPE)
                            {
                                createCell(bodystyle, localHSSFRow3, count, obj == null ? "" : obj.toString(),
                                        isPreaseNumber(obj == null ? "" : obj.toString()) ? true : false);
                            }
                            else if (count < itemdataType.length && itemdataType[count] == STRING_TYPE)
                            {
                                createCell(bodystyle, localHSSFRow3, count, obj == null ? "" : obj.toString(), false);
                            }
                            else
                            {
                                createCell(bodystyle, localHSSFRow3, count, obj == null ? "" : obj.toString(), false);
                            }
                        }
                        else
                        {
                            createCell(bodystyle, localHSSFRow3, count, obj == null ? "" : obj.toString(), false);
                        }
                        count++;
                    }
                }

            }
            localServletOutputStream = response.getOutputStream();
            response.reset();
            response.addHeader("Content-Disposition", String.format("attachment;filename*=utf-8'zh_cn'%s.xls",
                    new Object[]{URLEncoder.encode(fileName, "utf-8")}));
            response.setCharacterEncoding("UTF-8");
            response.setContentType("application/x-msdownload");
            response.flushBuffer();
            Workbook.write(localServletOutputStream);
            localServletOutputStream.flush();
            localServletOutputStream.close();
            return;
        }
        catch (IOException localIOException1)
        {

        }
        finally
        {
            try
            {
                localServletOutputStream.close();
            }
            catch (IOException localIOException4)
            {

            }
        }


    }

    /**
     * h<br>
     * <br> 获取工作簿对象
     * 创建人： 李亚彬 <br>
     * 创建时间： 2016年1月6日 上午10:06:38
     * Copyght (c)2016 <br>
     */
    public static SXSSFWorkbook getWorkBook()
    {
        return new SXSSFWorkbook();
    }

    /**
     * h<br>
     * <br> 获取工作簿对象
     * 创建人： 李亚彬 <br>
     * 创建时间： 2016年1月6日 上午10:06:38
     * Copyght (c)2016 <br>
     */
    public static SXSSFWorkbook getWorkBook(int a)
    {
        return new SXSSFWorkbook(a);
    }

    /**
     * 获取表头样式，主要设置字体背景色(#CCC)<br>
     * <br>
     * 创建人： 李亚彬 <br>
     * 创建时间： 2016年2月22日 下午1:27:02
     * Copyright (c)2016 <br>
     * * @param Workbook
     * * @return
     */
    private static CellStyle getTitleStyle(SXSSFWorkbook Workbook)
    {
        CellStyle localHSSFCellStyle = Workbook.createCellStyle();
        localHSSFCellStyle.setVerticalAlignment(CellStyle.VERTICAL_CENTER);
        localHSSFCellStyle.setBorderBottom(CellStyle.BORDER_THIN);
        localHSSFCellStyle.setBorderLeft(CellStyle.BORDER_THIN);
        localHSSFCellStyle.setBorderRight(CellStyle.BORDER_THIN);
        localHSSFCellStyle.setBorderTop(CellStyle.BORDER_THIN);
        localHSSFCellStyle.setDataFormat(HSSFDataFormat.getBuiltinFormat("#.###"));

        //------------------字体设置
        Font headerFont = Workbook.createFont();//创建字体样式
        headerFont.setBoldweight(Font.BOLDWEIGHT_BOLD);
        headerFont.setFontName("Times New Roman");  //设置字体类型
        headerFont.setFontHeightInPoints((short) 12);    //设置字体大小
        localHSSFCellStyle.setFont(headerFont); //设置字体样式
        localHSSFCellStyle.setFillForegroundColor(IndexedColors.GREY_25_PERCENT.getIndex());
        localHSSFCellStyle.setFillPattern(CellStyle.SOLID_FOREGROUND);
        return localHSSFCellStyle;

    }

    /**
     * 子项表头样式(颜色较浅)<br>
     * <br>
     * 创建人： 李亚彬 <br>
     * 创建时间： 2016年2月23日 上午8:33:18
     * Copyright (c)2016 <br>
     * * @param Workbook
     * * @return
     */
    private static CellStyle getItemTitleStyle(SXSSFWorkbook Workbook)
    {
        CellStyle localHSSFCellStyle = Workbook.createCellStyle();
        localHSSFCellStyle.setVerticalAlignment(CellStyle.VERTICAL_CENTER);
        localHSSFCellStyle.setBorderBottom(CellStyle.BORDER_THIN);
        localHSSFCellStyle.setBorderLeft(CellStyle.BORDER_THIN);
        localHSSFCellStyle.setBorderRight(CellStyle.BORDER_THIN);
        localHSSFCellStyle.setBorderTop(CellStyle.BORDER_THIN);
        localHSSFCellStyle.setDataFormat(HSSFDataFormat.getBuiltinFormat("#.###"));

        //------------------字体设置
        Font headerFont = Workbook.createFont();//创建字体样式
        headerFont.setBoldweight(Font.BOLDWEIGHT_BOLD);
        headerFont.setFontName("Times New Roman");  //设置字体类型
        headerFont.setFontHeightInPoints((short) 11);    //设置字体大小
        localHSSFCellStyle.setFont(headerFont); //设置字体样式
        localHSSFCellStyle.setFillForegroundColor(IndexedColors.LIGHT_CORNFLOWER_BLUE.getIndex());
        localHSSFCellStyle.setFillPattern(CellStyle.SOLID_FOREGROUND);
        return localHSSFCellStyle;

    }

    /**
     * <br>
     * <br>
     * 创建人： 李亚彬 <br>
     * 创建时间： 2016年2月24日 上午11:04:14
     * Copyright (c)2016 <br>
     * * @param filepath  文件路径
     * * @param startRow  具体从哪一行开始读取Excel。
     * * @param title  要转换的对象对应的属性字段。
     * * @param paramClass 对应的对象的Class
     * * @return
     * * @throws Exception
     */
    public static <T> List<T> ExcelToBaen(String filepath, int startRow, List<String> title, Class<?> paramClass,
                                          FileInputStream f) throws Exception
    {
        List<Object[]> datalist = readExcel(filepath, startRow, f);
        if (null == title)
            throw new Exception("对应字段模板不能为空！");
        return GetListByConverBean(datalist, title, paramClass);

    }

    /**
     * <br>
     * <br>
     * 创建人： 李亚彬 <br>
     * 创建时间： 2016年2月24日 上午11:04:14
     * Copyright (c)2016 <br>
     * * @param filepath  文件路径
     * * @param startRow  具体从哪一行开始读取Excel。
     * * @param title  要转换的对象对应的属性字段。
     * * @param paramClass 对应的对象的Class
     * * @return
     * * @throws Exception
     */
    public static <T> List<T> ExcelToBaen(String filepath, int startRow, List<String> title, Class<?> paramClass)
            throws Exception
    {
        List<Object[]> datalist = readExcel(filepath, startRow, null);
        if (null == title)
            throw new Exception("对应字段模板不能为空！");
        return GetListByConverBean(datalist, title, paramClass);

    }

    /**
     * 读取Excel文件<br>
     * <br>
     * 创建人： 李亚彬 <br>
     * 创建时间： 2016年1月6日 下午4:00:45
     * Copyright (c)2016 <br>
     * * @param filepath
     * * @return
     * * @throws Exception
     */
    public static List<Object[]> readExcel(String filepath, int startRow, InputStream stream) throws Exception
    {
        List<Object[]> list = new ArrayList<Object[]>();
        SimpleDateFormat sdf = null;
        try
        {

            POIFSFileSystem fs = null;
            if (stream == null)
                fs = new POIFSFileSystem(new FileInputStream(filepath));
            else
                fs = new POIFSFileSystem(stream);
            // 创建工作簿
            HSSFWorkbook workBook = new HSSFWorkbook(fs);
            // 创建工作表
            HSSFSheet sheet = workBook.getSheetAt(0);
            int rows = sheet.getPhysicalNumberOfRows(); // 获得行数
            if (rows > 0)
            {
                sheet.getMargin(HSSFSheet.TopMargin);
                for (int j = startRow; j < rows; j++)
                { // 行循环
                    HSSFRow row = sheet.getRow(j);
                    HSSFRow row1 = sheet.getRow(0);
                    if (row != null)
                    {
                        int cells = row1.getPhysicalNumberOfCells();// 获得列数
                        Object[] obj = new Object[cells];
                        for (short k = 0; k < cells; k++)
                        {
                            HSSFCell cell = row.getCell(k);// 列循环
                            String value = "";
                            if (cell != null)
                            {
                                switch (cell.getCellType())
                                {
                                    case HSSFCell.CELL_TYPE_NUMERIC: // 数值型
                                        if (HSSFDateUtil.isCellDateFormatted(cell))
                                        {// 处理日期格式、时间格式
                                            SimpleDateFormat sdfa = null;
                                            if (cell.getCellStyle().getDataFormat() == HSSFDataFormat
                                                    .getBuiltinFormat("h:mm"))
                                            {
                                                sdfa = new SimpleDateFormat("HH:mm");
                                            }
                                            else
                                            {// 日期
                                                sdf = new SimpleDateFormat("yyyy-MM-dd");
                                            }
                                            Date date = cell.getDateCellValue();
                                            value = sdf.format(date);
                                        }
                                        else if (cell.getCellStyle().getDataFormat() == 176)
                                        {
                                            // 处理自定义日期格式：m月d日(通过判断单元格的格式id解决，id的值是58)
                                            SimpleDateFormat sdfb = new SimpleDateFormat("yyyy-MM-dd");
                                            double value1 = cell.getNumericCellValue();
                                            Date date = DateUtil
                                                    .getJavaDate(value1);
                                            value = sdfb.format(date);
                                        }
                                        else
                                        {
                                            double value1 = cell.getNumericCellValue();
                                            CellStyle style = cell.getCellStyle();
                                            DecimalFormat format = new DecimalFormat();
                                            String temp = style.getDataFormatString();
                                            // 单元格设置成常规
                                            if (temp.equals("General"))
                                            {
                                                format.applyPattern("#");
                                            }
                                            value = format.format(value1);
                                        }
                                        break;
                                       /*if (HSSFDateUtil.isCellDateFormatted(cell)) {
                                        // 如果是date类型则 ，获取该cell的date值
									   Date date = HSSFDateUtil.getJavaDate(cell.getNumericCellValue());
									   sdf=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
									   value= sdf.format(date);
									} else {// 纯数字
										cell.setCellType(HSSFCell.CELL_TYPE_STRING);
										value=cell.getStringCellValue();
										if(value.endsWith(".0")){
											value=value.substring(0,value.length()-2);
										}
									}
									break;*/
                                /* 此行表示单元格的内容为string类型 */
                                    case HSSFCell.CELL_TYPE_STRING: // 字符串型
                                        value = cell.getRichStringCellValue().toString();
                                        break;
                                    case HSSFCell.CELL_TYPE_FORMULA:// 公式型
                                        try
                                        {
                                            value = cell.getRichStringCellValue().toString();
                                        }
                                        catch (Exception e)
                                        {
                                            value = String.valueOf(cell.getNumericCellValue());
                                        }
                                        break;
                                    case HSSFCell.CELL_TYPE_BOOLEAN:// 布尔
                                        value = " " + cell.getBooleanCellValue();
                                        break;
                                /* 此行表示该单元格值为空 */
                                    case HSSFCell.CELL_TYPE_BLANK: // 空值
                                        value = " ";
                                        break;
                                    case HSSFCell.CELL_TYPE_ERROR: // 故障
                                        value = " ";
                                        break;
                                    default:
                                        value = cell.getRichStringCellValue().toString();
                                }
                            }
                            obj[k] = value;
                        }
                        list.add(obj);
                    }

                }

            }

        }
        catch (Exception ex)
        {
            //log.error("解析Excel文件异常=====>" + ex.getMessage());
        }
        return list;
    }

    private static <T> List<T> GetListByConverBean(List<Object[]> paramList, List<String> paramList1,
                                                   Class<?> paramClass) throws Exception
    {
        ArrayList localArrayList = new ArrayList();
        HashMap localHashMap = ConverBean(paramClass);
        DecimalFormat localDecimalFormat = new DecimalFormat("0");
        //localDecimalFormat.setRoundingMode(RoundingMode.HALF_UP);

        Iterator localIterator = paramList.iterator();
        while (localIterator.hasNext())
        {
            Object[] arrayOfObject = (Object[]) localIterator.next();
            Object localObject1 = paramClass.newInstance();
            Method localMethod = null;
            int i = 0;
            int j = paramList1.size();
            while (i < j)
            {
                localMethod = (Method) localHashMap.get(((String) paramList1.get(i)).toLowerCase());
                if (localMethod == null)
                {
                    i++;
                    continue;
                }
                Class[] arrayOfClass = localMethod.getParameterTypes();
                if ((arrayOfClass[0] == Integer.TYPE) || (arrayOfClass[0] == Integer.class))
                {
                    String result = String.valueOf(arrayOfObject[i]);
                    if (StringUtils.isEmpty(result))
                    {
                        result = "0";
                    }
                    localMethod.invoke(localObject1, new Object[]{Integer.valueOf(Integer.parseInt(result))});
                }
                else if ((arrayOfClass[0] == Float.TYPE) || (arrayOfClass[0] == Float.class))
                {
                    String result = String.valueOf(arrayOfObject[i]);
                    if (StringUtils.isEmpty(result))
                    {
                        result = "0";
                    }
                    localMethod.invoke(localObject1, new Object[]{Float.valueOf(Float.parseFloat(result))});
                }
                else if ((arrayOfClass[0] == Double.TYPE) || (arrayOfClass[0] == Double.class))
                {
                    String result = String.valueOf(arrayOfObject[i]);
                    if (StringUtils.isEmpty(result))
                    {
                        result = "0";
                    }
                    localMethod.invoke(localObject1, new Object[]{Double.valueOf(Double.parseDouble(result))});
                }
                else if ((arrayOfClass[0] == Byte.TYPE) || (arrayOfClass[0] == Byte.class))
                {
                    String result = String.valueOf(arrayOfObject[i]);
                    if (StringUtils.isEmpty(result))
                    {
                        result = "0";
                    }
                    localMethod.invoke(localObject1, new Object[]{Byte.valueOf(Byte.parseByte(result))});
                }
                else if ((arrayOfClass[0] == Character.TYPE) || (arrayOfClass[0] == Character.class))
                {
                    localMethod.invoke(localObject1,
                            new Object[]{Character.valueOf(((Character) arrayOfObject[i]).charValue())});
                }
                else if ((arrayOfClass[0] == Boolean.TYPE) || (arrayOfClass[0] == Character.class))
                {
                    String result = String.valueOf(arrayOfObject[i]);
                    if (StringUtils.isEmpty(result))
                    {
                        result = "0";
                    }
                    localMethod.invoke(localObject1, new Object[]{Boolean.valueOf(Boolean.parseBoolean(result))});
                }
                else if ((arrayOfClass[0] == Long.TYPE) || (arrayOfClass[0] == Long.class))
                {
                    String result = String.valueOf(arrayOfObject[i]);
                    if (StringUtils.isEmpty(result))
                    {
                        result = "0";
                    }
                    localMethod.invoke(localObject1, new Object[]{Long.valueOf(Long.parseLong(result))});
                }
                else
                    localMethod.invoke(localObject1, new Object[]{arrayOfClass[0].cast(arrayOfObject[i])});
                i++;
            }
            Object localObject2 = localObject1;
            localArrayList.add(localObject2);
        }


        return localArrayList;
    }

    private static HashMap<String, Method> ConverBean(Class<?> paramClass) throws IntrospectionException
    {
        Class localClass = null;
        BeanInfo localBeanInfo = null;
        HashMap localHashMap = new HashMap();

        localBeanInfo = Introspector.getBeanInfo(paramClass, localClass);
        PropertyDescriptor[] arrayOfPropertyDescriptor1 = localBeanInfo.getPropertyDescriptors();
        for (PropertyDescriptor localPropertyDescriptor : arrayOfPropertyDescriptor1)
        {
            Method localMethod = localPropertyDescriptor.getWriteMethod();
            if (localMethod != null)
            {
                String str = localPropertyDescriptor.getName().toLowerCase();
                localHashMap.put(str, localMethod);
            }
        }


        return localHashMap;
    }

    private List<Object> getTitleList(String excelFileName)
    {
        List<Object> titleList = new ArrayList<Object>();
        try
        {
            HSSFWorkbook wb = new HSSFWorkbook(new FileInputStream(excelFileName));
            HSSFSheet sheet = wb.getSheetAt(0);
            int totalRows = sheet.getLastRowNum();
            if (totalRows == 0)
            {
                return null;
            }
            HSSFRow row = sheet.getRow(1);
            int totalCol = row.getPhysicalNumberOfCells();
            for (short i = 0; i < totalCol; i++)
            {
                if (i == 0)
                {//忽略序号列
                    //continue;
                }
                HSSFCell cell = row.getCell(i);
                String value = cell.getStringCellValue();
                if (!StringUtils.isEmpty(value))
                {
                    titleList.add(value.replaceAll("\n", "").replaceAll("（必填）", ""));
                }
            }
        }
        catch (FileNotFoundException e)
        {
            e.printStackTrace();
        }
        catch (IOException e)
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        return titleList;
    }

    /**
     * 主体样式内容<br>
     * <br>
     * 创建人： 李亚彬 <br>
     * 创建时间： 2016年2月22日 下午1:27:36
     * Copyright (c)2016 <br>
     * * @param Workbook
     * * @return
     */
    private static CellStyle getbodyStyle(SXSSFWorkbook Workbook)
    {
        CellStyle localHSSFCellStyle = Workbook.createCellStyle();
        localHSSFCellStyle.setVerticalAlignment(CellStyle.VERTICAL_CENTER);//垂直对齐
        localHSSFCellStyle.setBorderBottom(CellStyle.BORDER_THIN);//设置边框
        localHSSFCellStyle.setBorderLeft(CellStyle.BORDER_THIN);
        localHSSFCellStyle.setBorderRight(CellStyle.BORDER_THIN);
        localHSSFCellStyle.setBorderTop(CellStyle.BORDER_THIN);
        localHSSFCellStyle.setDataFormat(HSSFDataFormat.getBuiltinFormat("#.###"));

        //------------------字体设置
        Font headerFont = Workbook.createFont();//创建字体样式
        headerFont.setBoldweight(Font.BOLDWEIGHT_BOLD);
        headerFont.setFontName("宋体");  //设置字体类型
        headerFont.setFontHeightInPoints((short) 10);    //设置字体大小
        localHSSFCellStyle.setFont(headerFont);

        return localHSSFCellStyle;

    }
}
