package com.ruoyi.web.controller.business;


import com.ruoyi.common.annotation.Anonymous;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.common.core.service.SmsService;
import com.ruoyi.business.domain.Appointment;
import com.ruoyi.business.domain.Statement;
import com.ruoyi.system.domain.vo.ReservationVo;
import com.ruoyi.business.query.AppointmentQuery;
import com.ruoyi.business.service.IAppointmentService;
import com.ruoyi.system.util.PhoneUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

/**
 * 岗位控制器
 * 养修信息预约
 */
@RestController
@RequestMapping("business/appointment")
public class AppointmentController extends BaseController {

    @Autowired
    private IAppointmentService appointmentService;
    @Autowired
    private SmsService locationSmsService;

    /**
     * 列表
     * @param qo
     * @return
     */
    @GetMapping()
    @PreAuthorize("@ss.hasPermi('business:appointment:list')")
    public TableDataInfo list(AppointmentQuery qo) {
        startPage();
        return getDataTable(appointmentService.query(qo));
    }

    /**
     * 新增
     * @param appointment
     * @return
     */
    @PostMapping()
    @PreAuthorize("@ss.hasPermi('business:appointment:add')")
    public AjaxResult addSave(@RequestBody Appointment appointment) {
        appointmentService.save(appointment);
        return AjaxResult.success();
    }


    /**
     * 编辑
     * @param appointment
     * @return
     */
    @PutMapping()
    @PreAuthorize("@ss.hasPermi('business:appointment:edit')")
    public AjaxResult edit(@RequestBody Appointment appointment) {
        appointmentService.update(appointment);
        return AjaxResult.success();
    }

    /**
     * 根据id查询信息
     *
     * @param id
     * @return
     */
    @GetMapping("/{id}")
    @PreAuthorize("@ss.hasPermi('business:appointment:query')")
    public AjaxResult get(@PathVariable Long id) {
        Appointment appointment = appointmentService.get(id);
        return AjaxResult.success(appointment);
    }


    /**
     * 删除
     * @param ids
     * @return
     */
    @DeleteMapping("/{ids}")
    @PreAuthorize("@ss.hasPermi('business:appointment:remove')")
    public AjaxResult remove(@PathVariable String ids) {
        appointmentService.deleteBatch(ids);
        return AjaxResult.success();
    }

    /**
     * 确认到店
     * @param id
     * @return
     */
    @PutMapping("/arrival/{id}")
    @PreAuthorize("@ss.hasPermi('business:appointment:arrival')")
    public AjaxResult arrival(@PathVariable Long id) {
        appointmentService.arrival(id);
        return AjaxResult.success();
    }

    /**
     * 取消预约
     * @param id
     * @return
     */
    @RequestMapping("/cancel/{id}")
    @PreAuthorize("@ss.hasPermi('business:appointment:cancel')")
    public AjaxResult cancel(@PathVariable Long id) {
        appointmentService.cancel(id);
        return AjaxResult.success();
    }

    /**
     * 生成结算单
     * @param id
     * @return
     */
    @PreAuthorize("@ss.hasPermi('business:appointment:generateStatement')")
    @PutMapping("/statement/{id}")
    public AjaxResult generateStatement(@PathVariable Long id) {
        Statement statement = appointmentService.generateStatement(id);
        return AjaxResult.success(statement.getId());
    }

    /**
     * 客户填写信息进行预约
     * @param reservationVo
     * @return
     */
    @PostMapping("/reservation")
    @Anonymous
    public AjaxResult reservation(@RequestBody ReservationVo reservationVo) {
        //验证手机号是否正确
        boolean mobileNumber = PhoneUtil.isMobileNumber(reservationVo.getCustomerPhone());
        if (!mobileNumber) {
            return AjaxResult.error("不是合法的手机号");
        }
        //验证手机的验证码是否正确
        boolean verify = locationSmsService.checkCaptcha(reservationVo.getCustomerPhone(), "appointment", reservationVo.getCode());
        if (!verify) {
            return AjaxResult.error("验证码错误");
        }
        //执行新增预约单
        int reservation = appointmentService.reservation(reservationVo);
        if (reservation > 0) {
            return AjaxResult.success("预约成功");
        } else {
            return AjaxResult.error("预约失败");
        }
    }

    /**
     * 发送短信验证码
     * @param phone
     * @return
     */
    @PostMapping("/smsCode")
    @Anonymous
    public AjaxResult smsCode(String phone) {
        //验证手机号是否合法
        boolean mobileNumber = PhoneUtil.isMobileNumber(phone);
        if (!mobileNumber) {
            return AjaxResult.error("手机号不合法");
        }
        locationSmsService.sendCaptcha(phone, "appointment", 120);
        return AjaxResult.success();
    }

}
