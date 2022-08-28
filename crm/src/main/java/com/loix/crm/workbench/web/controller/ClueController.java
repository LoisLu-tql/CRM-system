package com.loix.crm.workbench.web.controller;

import com.loix.crm.commons.constant.Constants;
import com.loix.crm.commons.domain.ReturnObject;
import com.loix.crm.commons.utils.DateUtils;
import com.loix.crm.commons.utils.UUIDUtils;
import com.loix.crm.settings.domain.DicValue;
import com.loix.crm.settings.domain.User;
import com.loix.crm.settings.service.DicValueService;
import com.loix.crm.settings.service.UserService;
import com.loix.crm.workbench.domain.Activity;
import com.loix.crm.workbench.domain.Clue;
import com.loix.crm.workbench.domain.ClueActivityRelation;
import com.loix.crm.workbench.domain.ClueRemark;
import com.loix.crm.workbench.service.ActivityService;
import com.loix.crm.workbench.service.ClueActivityRelationService;
import com.loix.crm.workbench.service.ClueRemarkService;
import com.loix.crm.workbench.service.ClueService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.*;

@Controller
public class ClueController {

    @Autowired
    private UserService userService;

    @Autowired
    private DicValueService dicValueService;

    @Autowired
    private ClueService clueService;

    @Autowired
    private ClueRemarkService clueRemarkService;

    @Autowired
    private ActivityService activityService;
    
    @Autowired
    private ClueActivityRelationService clueActivityRelationService;

    @RequestMapping("/workbench/clue/index.do")
    public String index(HttpServletRequest request) {
        List<User> userList = userService.queryAllUsers();
        List<DicValue> appellationList = dicValueService.queryDicValueByTypeCode("appellation");
        List<DicValue> clueStateList = dicValueService.queryDicValueByTypeCode("clueState");
        List<DicValue> sourceList = dicValueService.queryDicValueByTypeCode("source");

        request.setAttribute("userList", userList);
        request.setAttribute("appellationList", appellationList);
        request.setAttribute("clueStateList", clueStateList);
        request.setAttribute("sourceList", sourceList);

        return "workbench/clue/index";
    }

    @RequestMapping("/workbench/clue/saveCreateClue.do")
    public @ResponseBody Object saveCreateClue(Clue clue, HttpSession session) {
        User user = (User) session.getAttribute(Constants.SESSION_USER);

        clue.setId(UUIDUtils.getUUID());
        clue.setCreateTime(DateUtils.formatDateTime(new Date()));
        clue.setCreateBy(user.getId());

        ReturnObject returnObject = new ReturnObject();
        try {
            int ret = clueService.saveCreateClue(clue);
            if(ret > 0) {
                returnObject.setCode(Constants.RETURN_OBJECT_CODE_SUCCESS);
            } else {
                returnObject.setCode(Constants.RETURN_OBJECT_CODE_FAIL);
                returnObject.setMessage("Fail..");
            }
        } catch (Exception e) {
            e.printStackTrace();
            returnObject.setCode(Constants.RETURN_OBJECT_CODE_FAIL);
            returnObject.setMessage("Fail..");
        }

        return returnObject;
    }

    @RequestMapping("/workbench/clue/detailClue.do")
    public String detailClue(String id, HttpServletRequest request) {
        Clue clue = clueService.queryClueForDetailById(id);
        List<ClueRemark> remarkList = clueRemarkService.queryClueRemarkForDetailByClueId(id);
        List<Activity> activityList = activityService.queryActivityForDetailByClueId(id);

        request.setAttribute("clue", clue);
        request.setAttribute("remarkList", remarkList);
        request.setAttribute("activityList", activityList);

        return "workbench/clue/detail";
    }

    @RequestMapping("/workbench/clue/queryActivityForDetailByNameClueId.do")
    public @ResponseBody Object queryActivityForDetailByNameClueId(String activityName, String clueId) {
        Map<String, Object> map = new HashMap<>();
        map.put("activityName", activityName);
        map.put("clueId", clueId);

        List<Activity> activityList = activityService.queryActivityForDetailByNameClueId(map);
        return activityList;
    }
    
    @RequestMapping("/workbench/clue/saveBind.do")
    public @ResponseBody Object saveBind(String[] activityId, String clueId) {
        ClueActivityRelation car = null;
        List<ClueActivityRelation> relationList = new ArrayList<>();
        for(String ai : activityId) {
            car = new ClueActivityRelation();
            car.setActivityId(ai);
            car.setClueId(clueId);
            car.setId(UUIDUtils.getUUID());
            relationList.add(car);
        }

        ReturnObject returnObject = new ReturnObject();
        try {
            int ret = clueActivityRelationService.saveCreateClueActivityRelationByList(relationList);
            if(ret > 0) {
                returnObject.setCode(Constants.RETURN_OBJECT_CODE_SUCCESS);
                List<Activity> activityList = activityService.queryActivityForDetailByIds(activityId);
                returnObject.setRetData(activityList);
            } else {
                returnObject.setCode(Constants.RETURN_OBJECT_CODE_FAIL);
                returnObject.setMessage("Fail..");
            }
        } catch (Exception e) {
            e.printStackTrace();
            returnObject.setCode(Constants.RETURN_OBJECT_CODE_FAIL);
            returnObject.setMessage("Fail..");
        }

        return returnObject;
    }
    
    @RequestMapping("/workbench/clue/saveUnbind.do")
    public @ResponseBody Object saveUnbind(ClueActivityRelation relation) {
        ReturnObject returnObject = new ReturnObject();
        try {
            int ret = clueActivityRelationService.deleteClueActivityRelationByClueIdActivityId(relation);
            if(ret > 0) {
                returnObject.setCode(Constants.RETURN_OBJECT_CODE_SUCCESS);
            } else {
                returnObject.setCode(Constants.RETURN_OBJECT_CODE_FAIL);
                returnObject.setMessage("Fail..");
            }
        } catch (Exception e) {
            e.printStackTrace();
            returnObject.setCode(Constants.RETURN_OBJECT_CODE_FAIL);
            returnObject.setMessage("Fail..");
        }

        return returnObject;
    }

    @RequestMapping("/workbench/clue/selectAllClues.do")
    public @ResponseBody Object selectAllClues() {
        List<Clue> clueList = clueService.queryAllClues();
        ReturnObject returnObject = new ReturnObject();

        try {
            returnObject.setCode(Constants.RETURN_OBJECT_CODE_SUCCESS);
            returnObject.setRetData(clueList);
        } catch (Exception e) {
            e.printStackTrace();
            returnObject.setCode(Constants.RETURN_OBJECT_CODE_FAIL);
            returnObject.setMessage("Fail..");
        }
        return returnObject;
    }

    @RequestMapping("/workbench/clue/toConvert.do")
    public String toConvert(String id, HttpServletRequest request) {
        Clue clue = clueService.queryClueForDetailById(id);
        List<DicValue> stageList = dicValueService.queryDicValueByTypeCode("stage");

        request.setAttribute("clue", clue);
        request.setAttribute("stageList", stageList);

        return "workbench/clue/convert";
    }

    @RequestMapping("/workbench/clue/queryActivityForConvertByNameClueId.do")
    public @ResponseBody Object queryActivityForConvertByNameClueId(String activityName, String clueId) {
        Map<String, Object> map = new HashMap<>();
        map.put("activityName", activityName);
        map.put("clueId", clueId);

        List<Activity> activityList = activityService.queryActivityForConvertByNameClueId(map);
        return activityList;
    }

    @RequestMapping("/workbench/clue/convertClue.do")
    public @ResponseBody Object convertClue(String clueId, String money, String name, String expectedDate, String stage,
                                            String activityId, String isCreateTran, HttpSession session) {
        Map<String, Object> map = new HashMap<>();
        map.put("clueId", clueId);
        map.put("money", money);
        map.put("name", name);
        map.put("expectedDate", expectedDate);
        map.put("stage", stage);
        map.put("activityId", activityId);
        map.put("isCreateTran", isCreateTran);
        map.put(Constants.SESSION_USER, session.getAttribute(Constants.SESSION_USER));

        ReturnObject returnObject = new ReturnObject();
        try {
            clueService.saveConvert(map);
            returnObject.setCode(Constants.RETURN_OBJECT_CODE_SUCCESS);
        } catch (Exception e) {
            e.printStackTrace();
            returnObject.setCode(Constants.RETURN_OBJECT_CODE_FAIL);
            returnObject.setMessage("Fail..");
        }

        return returnObject;
    }
}
