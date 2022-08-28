package com.loix.crm.workbench.web.controller;

import com.loix.crm.commons.constant.Constants;
import com.loix.crm.commons.domain.ReturnObject;
import com.loix.crm.settings.domain.DicValue;
import com.loix.crm.settings.domain.User;
import com.loix.crm.settings.service.DicValueService;
import com.loix.crm.settings.service.UserService;
import com.loix.crm.workbench.domain.Tran;
import com.loix.crm.workbench.domain.TranHistory;
import com.loix.crm.workbench.domain.TranRemark;
import com.loix.crm.workbench.service.CustomerService;
import com.loix.crm.workbench.service.TranHistoryService;
import com.loix.crm.workbench.service.TranRemarkService;
import com.loix.crm.workbench.service.TranService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;

@Controller
public class TranController {

    @Autowired
    private DicValueService dicValueService;

    @Autowired
    private UserService userService;

    @Autowired
    private CustomerService customerService;

    @Autowired
    private TranService tranService;

    @Autowired
    private TranRemarkService tranRemarkService;

    @Autowired
    private TranHistoryService tranHistoryService;

    @RequestMapping("/workbench/transaction/index.do")
    public String index(HttpServletRequest request) {

        List<DicValue> stageList = dicValueService.queryDicValueByTypeCode("stage");
        List<DicValue> transactionTypeList = dicValueService.queryDicValueByTypeCode("transactionType");
        List<DicValue> sourceList = dicValueService.queryDicValueByTypeCode("source");
        List<Tran> tranList = tranService.queryAllTran();

        request.setAttribute("stageList", stageList);
        request.setAttribute("transactionTypeList", transactionTypeList);
        request.setAttribute("sourceList", sourceList);
        request.setAttribute("tranList", tranList);

        return "workbench/transaction/index";
    }

    @RequestMapping("/workbench/transaction/toSave.do")
    public String toSave(HttpServletRequest request) {
        List<User> userList = userService.queryAllUsers();
        List<DicValue> stageList = dicValueService.queryDicValueByTypeCode("stage");
        List<DicValue> transactionTypeList = dicValueService.queryDicValueByTypeCode("transactionType");
        List<DicValue> sourceList = dicValueService.queryDicValueByTypeCode("source");

        request.setAttribute("userList", userList);
        request.setAttribute("stageList", stageList);
        request.setAttribute("transactionTypeList", transactionTypeList);
        request.setAttribute("sourceList", sourceList);

        return "workbench/transaction/save";
    }

    @RequestMapping("/workbench/transaction/getPossibilityByStage.do")
    public @ResponseBody Object getPossibilityByStage(String stageValue) {
        ResourceBundle bundle = ResourceBundle.getBundle("possibility");// get the .properties file under resource dir
        return bundle.getString(stageValue);
    }

    @RequestMapping("/workbench/transaction/queryCustomerNameByName.do")
    public @ResponseBody Object queryCustomerNameByName(String customerName) {
        List<String> customerNameList = customerService.queryCustomerNameByName(customerName);
        return customerNameList;
    }

    @RequestMapping("/workbench/transaction/saveCreateTran.do")
    public @ResponseBody Object saveCreateTran(@RequestParam Map<String, Object> map, HttpSession session) {
        map.put(Constants.SESSION_USER, session.getAttribute(Constants.SESSION_USER));

        ReturnObject returnObject = new ReturnObject();

        try {
            tranService.saveCreateTran(map);
            returnObject.setCode(Constants.RETURN_OBJECT_CODE_SUCCESS);
        } catch (Exception e) {
            e.printStackTrace();
            returnObject.setCode(Constants.RETURN_OBJECT_CODE_FAIL);
            returnObject.setMessage("Fail..");
        }

        return returnObject;
    }

    @RequestMapping("/workbench/transaction/detailTran.do")
    public String detailTran(String id, HttpServletRequest request) {
        Tran tran = tranService.queryTranForDetailById(id);
        List<TranRemark> remarkList = tranRemarkService.queryTranRemarkForDetailByTranId(id);
        List<TranHistory> historyList = tranHistoryService.queryTranHistoryForDetailByTranId(id);

        ResourceBundle bundle = ResourceBundle.getBundle("possibility");
        String possibility = bundle.getString(tran.getStage());
        tran.setPossibility(possibility);

        List<DicValue> stageList = dicValueService.queryDicValueByTypeCode("stage");

        request.setAttribute("tran", tran);
        request.setAttribute("remarkList", remarkList);
        request.setAttribute("historyList", historyList);
        request.setAttribute("stageList", stageList);

        return "workbench/transaction/detail";
    }


}
