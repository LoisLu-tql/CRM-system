package com.loix.crm.workbench.web.controller;

import com.loix.crm.commons.constant.Constants;
import com.loix.crm.commons.domain.ReturnObject;
import com.loix.crm.commons.utils.DateUtils;
import com.loix.crm.commons.utils.HSSFUtils;
import com.loix.crm.commons.utils.UUIDUtils;
import com.loix.crm.settings.domain.User;
import com.loix.crm.settings.service.UserService;
import com.loix.crm.workbench.domain.Activity;
import com.loix.crm.workbench.domain.ActivityRemark;
import com.loix.crm.workbench.service.ActivityRemarkService;
import com.loix.crm.workbench.service.ActivityService;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.*;
import java.util.*;

@Controller
public class ActivityController {

    @Autowired
    private UserService userService;

    @Autowired
    private ActivityService activityService;

    @Autowired
    private ActivityRemarkService activityRemarkService;

    @RequestMapping("/workbench/activity/index.do")
    public String index(HttpServletRequest request) {
        List<User> userList = userService.queryAllUsers();
        request.setAttribute("userList", userList);
        return "workbench/activity/index";
    }

    @RequestMapping("/workbench/activity/saveCreateActivity.do")
    public @ResponseBody Object saveCreateActivity(Activity activity, HttpSession session) {

        User user = (User) session.getAttribute(Constants.SESSION_USER);
        activity.setId(UUIDUtils.getUUID());
        activity.setCreateTime(DateUtils.formatDateTime(new Date()));
        activity.setCreateBy(user.getId());

        ReturnObject returnObject = new ReturnObject();
        try {
            // invoke service
            int ret = activityService.saveCreateActivity(activity);
//            System.out.println(ret);
            if(ret > 0) {
                returnObject.setCode(Constants.RETURN_OBJECT_CODE_SUCCESS);
            } else {
                returnObject.setCode(Constants.RETURN_OBJECT_CODE_FAIL);
                returnObject.setMessage("Your input has problem.");
            }
        } catch (Exception e) {
            e.printStackTrace();
            returnObject.setCode(Constants.RETURN_OBJECT_CODE_FAIL);
            returnObject.setMessage("Your input has problem.");
        }


        return returnObject;
    }

    @RequestMapping("/workbench/activity/queryActivityByConditionForPage.do")
    public @ResponseBody Object queryActivityByConditionForPage(String name, String owner, String startDate,
                                                                String endDate, int pageNo, int pageSize) {
        Map<String, Object> map = new HashMap<>();
        map.put("name", name);
        map.put("owner", owner);
        map.put("startDate", startDate);
        map.put("endDate", endDate);
        map.put("beginNo", (pageNo-1)*pageSize);
        map.put("pageSize", pageSize);

        List<Activity> activityList = activityService.queryActivityByConditionForPage(map);
        int totalRows = activityService.queryCountOfActivityByCondition(map);

        Map<String, Object> retMap = new HashMap<>();
        retMap.put("activityList", activityList);
        retMap.put("totalRows", totalRows);
        return retMap;
    }

    @RequestMapping("/workbench/activity/deleteActivityIds.do")
    public @ResponseBody Object deleteActivityIds(String[] id) {
        ReturnObject returnObject = new ReturnObject();
        try {
            int ret = activityService.deleteActivityByIds(id);
            if(ret > 0) {
                returnObject.setCode(Constants.RETURN_OBJECT_CODE_SUCCESS);
            } else {
                returnObject.setCode(Constants.RETURN_OBJECT_CODE_FAIL);
                returnObject.setMessage("Fail..Please try again..");
            }
        } catch (Exception e) {
            e.printStackTrace();
            returnObject.setCode(Constants.RETURN_OBJECT_CODE_FAIL);
            returnObject.setMessage("Fail..Please try again..");
        }
        return returnObject;
    }

    @RequestMapping("/workbench/activity/queryActivityById.do")
    public @ResponseBody Object queryActivityById(String id) {
        Activity activity = activityService.queryActivityById(id);
        return activity;
    }

    @RequestMapping("/workbench/activity/saveEditActivity.do")
    public @ResponseBody Object saveEditActivity(Activity activity, HttpSession session) {
        User user = (User) session.getAttribute(Constants.SESSION_USER);
        activity.setEditTime(DateUtils.formatDateTime(new Date()));
        activity.setEditBy(user.getId());

        ReturnObject returnObject = new ReturnObject();
        try {
            int res = activityService.saveEditActivity(activity);
            if(res > 0) returnObject.setCode(Constants.RETURN_OBJECT_CODE_SUCCESS);
            else {
                returnObject.setCode(Constants.RETURN_OBJECT_CODE_FAIL);
                returnObject.setMessage("Fail, try again..");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return returnObject;
    }

    @RequestMapping("/workbench/activity/fileDownload.do")
    public void fileDownload(HttpServletResponse response) throws Exception {
        response.setContentType("application/octet-stream;charset=UTF-8");
        // get output stream
        OutputStream out = response.getOutputStream();

        response.addHeader("Content-Disposition", "attachment;filename=mystudentList.xls");

        // read excel file    input stream -> output stream
        InputStream is = new FileInputStream("D:\\try-poi\\studentList.xls");
        byte[] buff = new byte[256];
        int len;
        while((len=is.read(buff)) != -1) {
            out.write(buff, 0, len);
        }

        is.close();
        out.flush(); // belongs to tomcat, do not need to be closed by us
    }

    @RequestMapping("/workbench/activity/exportAllActivities.do")
    public void exportAllActivities(HttpServletResponse response) throws IOException {
        List<Activity> activityList = activityService.queryAllActivities();
        // write to excel
        HSSFWorkbook wb = new HSSFWorkbook();
        HSSFSheet sheet = wb.createSheet("ActivityList");
        HSSFRow row = sheet.createRow(0);
        HSSFCell cell = row.createCell(0);
        cell.setCellValue("ID");
        cell = row.createCell(1);
        cell.setCellValue("Owner");
        cell = row.createCell(2);
        cell.setCellValue("Name");
        cell = row.createCell(3);
        cell.setCellValue("StartDate");
        cell = row.createCell(4);
        cell.setCellValue("EndDate");
        cell = row.createCell(5);
        cell.setCellValue("Cost");
        cell = row.createCell(6);
        cell.setCellValue("Description");
        cell = row.createCell(7);
        cell.setCellValue("CreateTime");
        cell = row.createCell(8);
        cell.setCellValue("CreateBy");
        cell = row.createCell(9);
        cell.setCellValue("EditTime");
        cell = row.createCell(10);
        cell.setCellValue("EditBy");

        Activity activity = null;
//        System.out.println(activityList.size());
        for(int i=0; i<activityList.size(); i++) {
            activity = activityList.get(i);
            row = sheet.createRow(i+1);
            cell = row.createCell(0);
            cell.setCellValue(activity.getId());
            cell = row.createCell(1);
            cell.setCellValue(activity.getOwner());
            cell = row.createCell(2);
            cell.setCellValue(activity.getName());
            cell = row.createCell(3);
            cell.setCellValue(activity.getStartDate());
            cell = row.createCell(4);
            cell.setCellValue(activity.getEndDate());
            cell = row.createCell(5);
            cell.setCellValue(activity.getCost());
            cell = row.createCell(6);
            cell.setCellValue(activity.getDescription());
            cell = row.createCell(7);
            cell.setCellValue(activity.getCreateTime());
            cell = row.createCell(8);
            cell.setCellValue(activity.getCreateBy());
            cell = row.createCell(9);
            cell.setCellValue(activity.getEditTime());
            cell = row.createCell(10);
            cell.setCellValue(activity.getEditBy());
        }

        response.setContentType("application/octet-stream;charset=UTF-8");
        response.addHeader("Content-Disposition", "attachment;filename=ActivityList.xls");
        // get output stream
        OutputStream out = response.getOutputStream();
        wb.write(out);

        wb.close();
        out.flush();
    }

    @RequestMapping("/workbench/activity/importActivity.do")
    public @ResponseBody Object importActivity(MultipartFile activityFile, HttpSession session) {
        User user = (User)session.getAttribute(Constants.SESSION_USER);
        ReturnObject returnObject = new ReturnObject();
        try {
            InputStream is = activityFile.getInputStream();
            // parse excel file
            HSSFWorkbook wb = new HSSFWorkbook(is);
            HSSFSheet sheet = wb.getSheetAt(0);

            HSSFRow row = null;
            HSSFCell cell = null;
            Activity activity = null;
            List<Activity> activityList = new ArrayList<>();
            for(int i=1; i<=sheet.getLastRowNum(); i++) {
                row = sheet.getRow(i);
                activity = new Activity();
                activity.setId(UUIDUtils.getUUID());
                activity.setOwner(user.getId());
                activity.setCreateTime(DateUtils.formatDateTime(new Date()));
                activity.setCreateBy(user.getId());
                for(int j=0; j<row.getLastCellNum(); j++) {
                    cell = row.getCell(j);
                    String cellValue = HSSFUtils.getCellValueForStr(cell);
                    if(j == 0) activity.setName(cellValue);
                    else if(j == 1) activity.setStartDate(cellValue);
                    else if(j == 2) activity.setEndDate(cellValue);
                    else if(j == 3) activity.setCost(cellValue);
                    else if(j == 4) activity.setDescription(cellValue);
                }
                activityList.add(activity);

            }

            int res = activityService.saveCreateActivityByList(activityList);

            returnObject.setCode(Constants.RETURN_OBJECT_CODE_SUCCESS);
            returnObject.setRetData(res);

        } catch (Exception e) {
            e.printStackTrace();
            returnObject.setCode(Constants.RETURN_OBJECT_CODE_FAIL);
            returnObject.setMessage("Fail! Try again..");
        }
        return returnObject;
    }

    @RequestMapping("/workbench/activity/detailActivity.do")
    public String detailActivity(String id, HttpServletRequest request) {
        Activity activity = activityService.queryActivityForDetailById(id);
        List<ActivityRemark> remarkList = activityRemarkService.queryActivityRemarkForDetailByActivityId(id);
        request.setAttribute("activity", activity);
        request.setAttribute("remarkList", remarkList);
        return "/workbench/activity/detail";
    }

}
