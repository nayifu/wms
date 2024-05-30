package com.zbkj.service.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.RandomUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.zbkj.common.constants.Constants;
import com.zbkj.common.constants.DateConstants;
import com.zbkj.common.constants.MerchantConstants;
import com.zbkj.common.constants.SystemRoleConstants;
import com.zbkj.common.exception.CrmebException;
import com.zbkj.common.model.bill.MerchantDailyStatement;
import com.zbkj.common.model.bill.MerchantMonthStatement;
import com.zbkj.common.model.merchant.*;
import com.zbkj.common.model.system.SystemAdmin;
import com.zbkj.common.page.CommonPage;
import com.zbkj.common.request.*;
import com.zbkj.common.response.*;
import com.zbkj.common.utils.CrmebUtil;
import com.zbkj.common.utils.SecurityUtil;
import com.zbkj.common.vo.LoginUserVo;
import com.zbkj.common.vo.MerchantConfigInfoVo;
import com.zbkj.common.vo.MerchantTransferInfoVo;
import com.zbkj.common.vo.dateLimitUtilVo;
import com.zbkj.service.dao.MerchantDao;
import com.zbkj.service.service.*;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.support.TransactionTemplate;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * StoreServiceImpl 接口实现
 * +----------------------------------------------------------------------
 * | CRMEB [ CRMEB赋能开发者，助力企业发展 ]
 * +----------------------------------------------------------------------
 * | Copyright (c) 2016~2023 https://www.crmeb.com All rights reserved.
 * +----------------------------------------------------------------------
 * | Licensed CRMEB并不是自由软件，未经许可不能去掉CRMEB相关版权
 * +----------------------------------------------------------------------
 * | Author: CRMEB Team <admin@crmeb.com>
 * +----------------------------------------------------------------------
 */
@Service
public class MerchantServiceImpl extends ServiceImpl<MerchantDao, Merchant> implements MerchantService {

    @Resource
    private MerchantDao dao;

    @Autowired
    private TransactionTemplate transactionTemplate;
    @Autowired
    private SystemAttachmentService systemAttachmentService;
    @Autowired
    private SystemAdminService adminService;
    @Autowired
    private StoreProductService storeProductService;
    @Autowired
    private MerchantInfoService merchantInfoService;
    @Autowired
    private MerchantCategoryService merchantCategoryService;
    @Autowired
    private MerchantTypeService merchantTypeService;
    @Autowired
    private MerchantDailyStatementService merchantDailyStatementService;
    @Autowired
    private MerchantMonthStatementService merchantMonthStatementService;
    @Autowired
    private MerchantApplyService merchantApplyService;
    @Autowired
    private UserService userService;
    @Autowired
    private UserMerchantCollectService userMerchantCollectService;
    @Autowired
    private EmailService emailService;
    @Autowired
    private SystemConfigService systemConfigService;

    /**
     * 商户分页列表
     *
     * @param searchRequest    搜索参数
     * @param pageParamRequest 分页参数
     * @return PageInfo
     */
    @Override
    public PageInfo<MerchantPageResponse> getAdminPage(MerchantSearchRequest searchRequest, PageParamRequest pageParamRequest) {
        Page<Merchant> page = PageHelper.startPage(pageParamRequest.getPage(), pageParamRequest.getLimit());
        LambdaQueryWrapper<Merchant> lqw = Wrappers.lambdaQuery();
        if (ObjectUtil.isNotNull(searchRequest.getCategoryId())) {
            lqw.eq(Merchant::getCategoryId, searchRequest.getCategoryId());
        }
        if (ObjectUtil.isNotNull(searchRequest.getTypeId())) {
            lqw.eq(Merchant::getTypeId, searchRequest.getTypeId());
        }
        if (ObjectUtil.isNotNull(searchRequest.getIsSelf())) {
            lqw.eq(Merchant::getIsSelf, searchRequest.getIsSelf());
        }
        if (ObjectUtil.isNotNull(searchRequest.getIsSwitch())) {
            lqw.eq(Merchant::getIsSwitch, searchRequest.getIsSwitch());
        }
        if (StrUtil.isNotEmpty(searchRequest.getKeywords())) {
            lqw.and(i -> i.like(Merchant::getName, searchRequest.getKeywords())
                    .or().apply(StrUtil.format(" find_in_set('{}', keywords)", searchRequest.getKeywords())));
        }
        if (StrUtil.isNotBlank(searchRequest.getDateLimit())) {
            dateLimitUtilVo dateLimitUtilVo = com.zbkj.common.utils.DateUtil.getDateLimit(searchRequest.getDateLimit());
            lqw.between(Merchant::getCreateTime, dateLimitUtilVo.getStartTime(), dateLimitUtilVo.getEndTime());
        }
        lqw.eq(Merchant::getIsDel, false);
        lqw.orderByDesc(Merchant::getSort);
        List<Merchant> merchantList = dao.selectList(lqw);
        if (CollUtil.isEmpty(merchantList)) {
            return CommonPage.copyPageInfo(page, CollUtil.newArrayList());
        }
        List<MerchantPageResponse> responseList = merchantList.stream().map(e -> {
            MerchantPageResponse response = new MerchantPageResponse();
            BeanUtils.copyProperties(e, response);
            SystemAdmin systemAdmin = adminService.getById(e.getCreateId());
            response.setCreateName(systemAdmin.getRealName());
            return response;
        }).collect(Collectors.toList());
        return CommonPage.copyPageInfo(page, responseList);
    }

    /**
     * 商户分页列表表头数量
     *
     * @return MerchantHeaderNumResponse
     */
    @Override
    public MerchantHeaderNumResponse getListHeaderNum() {
        LambdaQueryWrapper<Merchant> lqw = Wrappers.lambdaQuery();
        // 开启的商户数
        lqw.eq(Merchant::getIsDel, false);
        lqw.eq(Merchant::getIsSwitch, 1);
        Integer openNum = dao.selectCount(lqw);
        // 关闭的商户数
        lqw.clear();
        lqw.eq(Merchant::getIsDel, false);
        lqw.eq(Merchant::getIsSwitch, 0);
        Integer closeNum = dao.selectCount(lqw);
        return new MerchantHeaderNumResponse(openNum, closeNum);
    }

    /**
     * 获取所有商户数量
     *
     * @return Integer
     */
    @Override
    public Integer getAllCount() {
        LambdaQueryWrapper<Merchant> lqw = Wrappers.lambdaQuery();
        lqw.eq(Merchant::getIsDel, false);
        return dao.selectCount(lqw);
    }

    /**
     * 获取入驻协议
     *
     * @return MerchantAgreementResponse
     */
    @Override
    public MerchantAgreementResponse getSettledAgreement() {
        return new MerchantAgreementResponse(systemConfigService.getMerchantSettlementAgreement());
    }

    /**
     * 获取商户客服信息
     */
    @Override
    public MerchantServiceInfoResponse getCustomerServiceInfo(Integer id) {
        getByIdException(id);
        MerchantInfo merchantInfo = merchantInfoService.getByMerId(id);
        MerchantServiceInfoResponse response = new MerchantServiceInfoResponse();
        BeanUtils.copyProperties(merchantInfo, response);
        return response;
    }

    /**
     * 添加商户
     *
     * @param request 请求参数
     * @return Boolean
     */
    @Override
    public Boolean add(MerchantAddRequest request) {
        // 检查商户名or商户账号or商户邮箱是否重复
        if (checkMerchantName(request.getName())) {
            throw new CrmebException("商户名已存在");
        }
        if (checkMerchantEmail(request.getEmail())) {
            throw new CrmebException("商户邮箱已存在");
        }
        if (adminService.checkAccount(request.getEmail())) {
            throw new CrmebException("商户账号已存在");
        }

        Merchant merchant = new Merchant();
        BeanUtils.copyProperties(request, merchant);
        request.setPassword(RandomUtil.randomString(8));
        if (StrUtil.isNotEmpty(merchant.getQualificationPicture())) {
            merchant.setQualificationPicture(systemAttachmentService.clearPrefix(merchant.getQualificationPicture()));
        }
        merchant.setCreateType(MerchantConstants.CREATE_TYPE_ADMIN);
        LoginUserVo loginUserVo = SecurityUtil.getLoginUserVo();
        merchant.setCreateId(loginUserVo.getUser().getId());

        // 初始化商户信息表
        MerchantInfo merchantInfo = new MerchantInfo();
        // 初始化日/月帐单
        MerchantDailyStatement dailyStatement = new MerchantDailyStatement();
        MerchantMonthStatement monthStatement = new MerchantMonthStatement();
        Boolean execute = transactionTemplate.execute(e -> {
            // 初始化管理账号
            dao.insert(merchant);
            SystemAdmin systemAdmin = initSystemAdmin(request.getEmail(), request.getPassword(), request.getName());
            systemAdmin.setMerId(merchant.getId());
            adminService.save(systemAdmin);
            merchant.setAdminId(systemAdmin.getId());
            dao.updateById(merchant);

            merchantInfo.setMerId(merchant.getId());
            merchantInfoService.save(merchantInfo);

            dailyStatement.setMerId(merchant.getId()).setDataDate(DateUtil.date().toDateStr());
            monthStatement.setMerId(merchant.getId()).setDataDate(DateUtil.date().toString(DateConstants.DATE_FORMAT_MONTH));
            merchantDailyStatementService.save(dailyStatement);
            merchantMonthStatementService.save(monthStatement);
            return Boolean.TRUE;
        });
        if (execute) {
            // 发送商户审核成功通知
            emailService.sendMerchantAuditSuccess(merchant.getEmail(), request.getPassword());
        }
        return execute;
    }

    /**
     * 初始化管理员账号
     *
     * @param account  账号
     * @param password 密码
     * @param name     名称
     * @return SystemAdmin
     */
    private SystemAdmin initSystemAdmin(String account, String password, String name) {
        SystemAdmin systemAdmin = new SystemAdmin();
        systemAdmin.setAccount(account);
        String pwd = CrmebUtil.encryptPassword(password, account);
        systemAdmin.setPwd(pwd);
        systemAdmin.setRealName(name);
        systemAdmin.setStatus(true);
        systemAdmin.setRoles("2");
        systemAdmin.setType(SystemRoleConstants.SYSTEM_ROLE_TYPE_MERCHANT_SUPER);
        return systemAdmin;
    }

    /**
     * 编辑商户
     *
     * @param request 请求参数
     * @return Boolean
     */
    @Override
    public Boolean edit(MerchantUpdateRequest request) {
        Merchant merchant = getByIdException(request.getId());
        if (!request.getName().equals(merchant.getName())) {
            if (checkMerchantName(request.getEmail(), request.getId())) {
                throw new CrmebException("商户名称已存在");
            }
        }
        if (!request.getEmail().equals(merchant.getEmail())) {
            if (checkMerchantEmail(request.getEmail(), request.getId())) {
                throw new CrmebException("商户邮箱已存在");
            }
        }
        Merchant tempMerchant = new Merchant();
        BeanUtils.copyProperties(request, tempMerchant);
        if (StrUtil.isNotEmpty(tempMerchant.getQualificationPicture())) {
            tempMerchant.setQualificationPicture(systemAttachmentService.clearPrefix(tempMerchant.getQualificationPicture()));
        }
        return dao.updateById(tempMerchant) > 0;
    }

    /**
     * 修改商户密码
     *
     * @param request 请求对象
     */
    @Override
    public Boolean updatePassword(MerchantUpdatePasswordRequest request) {
        if (!request.getNewPassword().trim().equals(request.getPasswordAgain().trim())) {
            throw new CrmebException("密码不一致，请重新输入");
        }
        Merchant merchant = getByIdException(request.getId());
        SystemAdmin systemAdmin = adminService.getDetail(merchant.getAdminId());
        String pwd = CrmebUtil.encryptPassword(request.getNewPassword().trim(), systemAdmin.getAccount());
        systemAdmin.setPwd(pwd);
        return adminService.updateById(systemAdmin);
    }

    /**
     * 修改复制商品数量
     *
     * @param request 请求对象
     * @return Boolean
     */
    @Override
    public Boolean updateCopyProductNum(MerchantUpdateProductNumRequest request) {
        Merchant merchant = getByIdException(request.getId());
        if (request.getType().equals(Constants.OPERATION_TYPE_SUBTRACT) && merchant.getCopyProductNum() - request.getNum() < 0) {
            throw new CrmebException("扣减后的数量不能小于0");
        }
        UpdateWrapper<Merchant> updateWrapper = new UpdateWrapper<>();
        if (request.getType().equals(Constants.OPERATION_TYPE_ADD)) {
            updateWrapper.setSql(StrUtil.format("copy_product_num = copy_product_num + {}", request.getNum()));
        }
        if (request.getType().equals(Constants.OPERATION_TYPE_SUBTRACT)) {
            updateWrapper.setSql(StrUtil.format("copy_product_num = copy_product_num - {}", request.getNum()));
            updateWrapper.last(StrUtil.format("and (copy_product_num - {} >= 0)", request.getNum()));
        }
        updateWrapper.eq("id", request.getId());
        return update(updateWrapper);
    }

    /**
     * 平台端商户详情
     *
     * @param id 商户ID
     * @return MerchantPlatformDetailResponse
     */
    @Override
    public MerchantPlatformDetailResponse getPlatformDetail(Integer id) {
        Merchant merchant = getByIdException(id);
        MerchantPlatformDetailResponse response = new MerchantPlatformDetailResponse();
        BeanUtils.copyProperties(merchant, response);
        SystemAdmin systemAdmin = adminService.getDetail(merchant.getAdminId());
        response.setAccount(systemAdmin.getAccount());
        return response;
    }

    /**
     * 商户推荐开关
     *
     * @param id 商户ID
     * @return Boolean
     */
    @Override
    public Boolean recommendSwitch(Integer id) {
        Merchant merchant = getByIdException(id);
        merchant.setIsRecommend(!merchant.getIsRecommend());
        return dao.updateById(merchant) > 0;
    }

    /**
     * 关闭商户
     *
     * @param id 商户ID
     * @return Boolean
     */
    @Override
    public Boolean close(Integer id) {
        Merchant merchant = getByIdException(id);
        if (!merchant.getIsSwitch()) {
            throw new CrmebException("商户已是关闭状态");
        }
        // 1.修改商户状态，2.强制下架商户所有商品,3.手动关闭的商户，商品审核状态自动切换为需要审核状态
        merchant.setIsSwitch(false);
        merchant.setProductSwitch(true);
        return transactionTemplate.execute(e -> {
            dao.updateById(merchant);
            storeProductService.forcedRemovalAll(merchant.getId());
            return Boolean.TRUE;
        });
    }

    /**
     * 开启商户
     *
     * @param id 商户ID
     * @return Boolean
     */
    @Override
    public Boolean open(Integer id) {
        Merchant merchant = getByIdException(id);
        if (merchant.getIsSwitch()) {
            throw new CrmebException("商户已经是开启状态");
        }
        if (StrUtil.isEmpty(merchant.getAvatar()) || StrUtil.isEmpty(merchant.getBackImage()) || StrUtil.isEmpty(merchant.getStreetBackImage())) {
            throw new CrmebException("请让商户先进行商户头像、背景图配置");
        }
        MerchantInfo merchantInfo = merchantInfoService.getByMerId(merchant.getId());
        if (StrUtil.isEmpty(merchantInfo.getServiceLink()) && StrUtil.isEmpty(merchantInfo.getServicePhone())
                && StrUtil.isEmpty(merchantInfo.getServiceMessage()) && StrUtil.isEmpty(merchantInfo.getServiceEmail())) {
            throw new CrmebException("请让商户先进行客服配置");
        }
        merchant.setIsSwitch(true);
        return dao.updateById(merchant) > 0;
    }

    /**
     * 入驻审核成功，初始化商户
     *
     * @param request   商户添加参数
     * @param auditorId 审核员id
     */
    @Override
    public Boolean auditSuccess(MerchantAddRequest request, Integer auditorId) {
        // 检查商户名or商户账号or商户邮箱是否重复
        if (checkMerchantName(request.getName())) {
            throw new CrmebException("商户名已存在");
        }
        if (checkMerchantEmail(request.getEmail())) {
            throw new CrmebException("商户邮箱已存在");
        }
        if (adminService.checkAccount(request.getEmail())) {
            throw new CrmebException("商户账号已存在");
        }
        Merchant merchant = new Merchant();
        BeanUtils.copyProperties(request, merchant);
        if (StrUtil.isNotEmpty(merchant.getQualificationPicture())) {
            merchant.setQualificationPicture(systemAttachmentService.clearPrefix(merchant.getQualificationPicture()));
        }
        merchant.setCreateType(MerchantConstants.CREATE_TYPE_APPLY);
        merchant.setCreateId(auditorId);
        // 初始化日/月帐单信息
        MerchantDailyStatement dailyStatement = new MerchantDailyStatement();
        MerchantMonthStatement monthStatement = new MerchantMonthStatement();

        // 初始化管理账号
        int insert = dao.insert(merchant);
        if (insert <= 0) return false;
        SystemAdmin systemAdmin = initSystemAdmin(request.getEmail(), request.getPassword(), request.getName());
        systemAdmin.setMerId(merchant.getId());
        boolean save = adminService.save(systemAdmin);
        if (!save) return false;
        merchant.setAdminId(systemAdmin.getId());
        dao.updateById(merchant);
        MerchantInfo merchantInfo = new MerchantInfo();
        merchantInfo.setMerId(merchant.getId());

        dailyStatement.setMerId(merchant.getId()).setDataDate(DateUtil.date().toDateStr());
        monthStatement.setMerId(merchant.getId()).setDataDate(DateUtil.date().toString(DateConstants.DATE_FORMAT_MONTH));
        merchantDailyStatementService.save(dailyStatement);
        merchantMonthStatementService.save(monthStatement);
        return merchantInfoService.save(merchantInfo);
    }

    /**
     * 获取商户详情
     *
     * @param id 商户ID
     * @return Merchant
     */
    @Override
    public Merchant getByIdException(Integer id) {
        Merchant merchant = getById(id);
        if (ObjectUtil.isNull(merchant) || merchant.getIsDel()) {
            throw new CrmebException("Merchant does not exist");
        }
        return merchant;
    }

    /**
     * 扣减商户复制商品数量
     *
     * @param id 商户id
     * @return Boolean
     */
    @Override
    public Boolean subCopyProductNum(Integer id) {
        UpdateWrapper<Merchant> wrapper = Wrappers.update();
        wrapper.setSql(" copy_product_num = copy_product_num -1 ");
        wrapper.eq("id", id);
        wrapper.ge("copy_product_num", 1);
        return update(wrapper);
    }

    /**
     * 操作商户余额
     *
     * @param merId 商户id
     * @param price 金额
     * @param type  操作类型
     * @return Boolean
     */
    @Override
    public Boolean operationBalance(Integer merId, BigDecimal price, String type) {
        UpdateWrapper<Merchant> wrapper = Wrappers.update();
        if (type.equals(Constants.OPERATION_TYPE_ADD)) {
            wrapper.setSql(" balance = balance + " + price);
        } else {
            wrapper.setSql(" balance = balance - " + price);
//            wrapper.last(StrUtil.format("balance - {} >= 0", price));
        }
        wrapper.eq("id", merId);
        return update(wrapper);
    }

    /**
     * 获取商户列表（商户id）
     *
     * @param merIdList 商户ID列表
     */
    @Override
    public List<Merchant> getListByIdList(List<Integer> merIdList) {
        LambdaQueryWrapper<Merchant> lqw = Wrappers.lambdaQuery();
        lqw.in(Merchant::getId, merIdList);
        return dao.selectList(lqw);
    }

    /**
     * 获取商户Id组成的Map
     *
     * @param merIdList 商户ID列表
     * @return Map
     */
    @Override
    public Map<Integer, Merchant> getMerIdMapByIdList(List<Integer> merIdList) {
        List<Merchant> merchantList = getListByIdList(merIdList);
        Map<Integer, Merchant> merchantMap = new HashMap<>();
        merchantList.forEach(merchant -> {
            merchantMap.put(merchant.getId(), merchant);
        });
        return merchantMap;
    }

    /**
     * 商户端商户基础信息
     *
     * @return MerchantBaseInfoResponse
     */
    @Override
    public MerchantBaseInfoResponse getBaseInfo() {
        SystemAdmin systemAdmin = SecurityUtil.getLoginUserVo().getUser();
        Merchant merchant = getByIdException(systemAdmin.getMerId());
        MerchantBaseInfoResponse baseInfo = new MerchantBaseInfoResponse();
        MerchantCategory merchantCategory = merchantCategoryService.getById(merchant.getCategoryId());
        MerchantType merchantType = merchantTypeService.getById(merchant.getTypeId());
        BeanUtils.copyProperties(merchant, baseInfo);
        baseInfo.setMerCategory(merchantCategory.getName());
        baseInfo.setMerType(merchantType.getName());
        return baseInfo;
    }

    /**
     * 商户端商户配置信息
     *
     * @return MerchantConfigInfoResponse
     */
    @Override
    public MerchantConfigInfoVo getConfigInfo() {
        SystemAdmin systemAdmin = SecurityUtil.getLoginUserVo().getUser();
        Merchant merchant = getByIdException(systemAdmin.getMerId());
        MerchantInfo merchantInfo = merchantInfoService.getByMerId(merchant.getId());
        MerchantConfigInfoVo infoResponse = new MerchantConfigInfoVo();
        BeanUtils.copyProperties(merchant, infoResponse);
        BeanUtils.copyProperties(merchantInfo, infoResponse);
        return infoResponse;
    }

    /**
     * 商户端商户转账信息
     *
     * @return MerchantTransferInfoResponse
     */
    @Override
    public MerchantTransferInfoVo getTransferInfo() {
        SystemAdmin systemAdmin = SecurityUtil.getLoginUserVo().getUser();
        MerchantInfo merchantInfo = merchantInfoService.getByMerId(systemAdmin.getMerId());
        if (ObjectUtil.isNull(merchantInfo)) {
            throw new CrmebException("商户信息不存在");
        }
        MerchantTransferInfoVo response = new MerchantTransferInfoVo();
        BeanUtils.copyProperties(merchantInfo, response);
        return response;
    }

    /**
     * 商户端商户配置信息编辑
     *
     * @param request 编辑参数
     * @return Boolean
     */
    @Override
    public Boolean configInfoEdit(MerchantConfigInfoVo request) {
        serviceTypeCheck(request);
        SystemAdmin systemAdmin = SecurityUtil.getLoginUserVo().getUser();
        Merchant tempMerchant = getByIdException(systemAdmin.getMerId());
        MerchantInfo tempMerchantInfo = merchantInfoService.getByMerId(tempMerchant.getId());
        Merchant merchant = new Merchant();
        MerchantInfo merchantInfo = new MerchantInfo();
        BeanUtils.copyProperties(request, merchant);
        BeanUtils.copyProperties(request, merchantInfo);
        String cdnUrl = systemAttachmentService.getCdnUrl();
        merchant.setBackImage(systemAttachmentService.clearPrefix(request.getBackImage(), cdnUrl));
        merchant.setAvatar(systemAttachmentService.clearPrefix(request.getAvatar(), cdnUrl));
        merchant.setStreetBackImage(systemAttachmentService.clearPrefix(request.getStreetBackImage(), cdnUrl));
        merchant.setId(tempMerchant.getId());
        if (StrUtil.isNotBlank(merchant.getPcBanner())) {
            merchant.setPcBanner(systemAttachmentService.clearPrefix(merchant.getPcBanner(), cdnUrl));
        }
        if (StrUtil.isNotBlank(merchant.getPcBackImage())) {
            merchant.setPcBackImage(systemAttachmentService.clearPrefix(merchant.getPcBackImage(), cdnUrl));
        }
        merchantInfo.setId(tempMerchantInfo.getId());
        return transactionTemplate.execute(e -> {
            updateById(merchant);
            merchantInfoService.updateById(merchantInfo);
            return Boolean.TRUE;
        });
    }

    /**
     * 商户端商户转账信息编辑
     *
     * @param request 编辑参数
     * @return Boolean
     */
    @Override
    public Boolean transferInfoEdit(MerchantTransferInfoVo request) {
        SystemAdmin systemAdmin = SecurityUtil.getLoginUserVo().getUser();
        MerchantInfo tempMerchantInfo = merchantInfoService.getByMerId(systemAdmin.getMerId());
        if (ObjectUtil.isNull(tempMerchantInfo)) {
            throw new CrmebException("商户信息不存在");
        }
        MerchantInfo merchantInfo = new MerchantInfo();
        BeanUtils.copyProperties(request, merchantInfo);
        merchantInfo.setId(tempMerchantInfo.getId());
        return merchantInfoService.updateById(merchantInfo);
    }

    /**
     * 商户端商户开关
     *
     * @return Boolean
     */
    @Override
    public Boolean updateSwitch() {
        SystemAdmin systemAdmin = SecurityUtil.getLoginUserVo().getUser();
        Merchant merchant = getByIdException(systemAdmin.getMerId());
        if (merchant.getIsSwitch()) {
            merchant.setIsSwitch(false);
            return updateById(merchant);
        }
        if (StrUtil.isEmpty(merchant.getAvatar()) || StrUtil.isEmpty(merchant.getBackImage()) || StrUtil.isEmpty(merchant.getStreetBackImage())) {
            throw new CrmebException("请先进行商户头像、背景图配置");
        }
        MerchantInfo merchantInfo = merchantInfoService.getByMerId(merchant.getId());
        if (StrUtil.isEmpty(merchantInfo.getServiceLink()) && StrUtil.isEmpty(merchantInfo.getServicePhone())
                && StrUtil.isEmpty(merchantInfo.getServiceMessage()) && StrUtil.isEmpty(merchantInfo.getServiceEmail())) {
            throw new CrmebException("请先进行客服配置");
        }
        merchant.setIsSwitch(true);
        return updateById(merchant);
    }

    /**
     * 获取所有的商户id
     *
     * @return List
     */
    @Override
    public List<Integer> getAllId() {
        LambdaQueryWrapper<Merchant> lqw = Wrappers.lambdaQuery();
        lqw.select(Merchant::getId);
        lqw.eq(Merchant::getIsDel, false);
        List<Merchant> merchantList = dao.selectList(lqw);
        return merchantList.stream().map(Merchant::getId).collect(Collectors.toList());
    }

    /**
     * 商户入驻申请
     *
     * @param request 申请参数
     * @return Boolean
     */
    @Override
    public Boolean settledApply(MerchantSettledApplyRequest request) {
        Integer uid = userService.getUserIdException();
        emailService.checkEmailValidateCode(request.getEmail(), request.getCaptcha());
        // 检查商户名or商户账号or商户邮箱是否重复
        if (checkMerchantName(request.getName())) {
            throw new CrmebException("Merchant name already exists");
        }
        if (checkMerchantEmail(request.getEmail())) {
            throw new CrmebException("Merchant mailbox already exists");
        }
        SystemAdmin admin = adminService.getByAccountAndType(request.getEmail(), SystemRoleConstants.SYSTEM_ROLE_TYPE_MERCHANT_SUPER);
        if (ObjectUtil.isNotNull(admin)) {
            throw new CrmebException("Merchant mailbox already exists");
        }
        if (adminService.checkAccount(request.getEmail())) {
            throw new CrmebException("Merchant mailbox already exists");
        }
        MerchantApply merchantApply = new MerchantApply();
        BeanUtils.copyProperties(request, merchantApply);
        merchantApply.setQualificationPicture(systemAttachmentService.clearPrefix(request.getQualificationPicture()));
        merchantApply.setUid(uid);
        return merchantApplyService.save(merchantApply);
    }

    /**
     * 商户入驻申请记录
     *
     * @param pageParamRequest 分页参数
     * @return List
     */
    @Override
    public PageInfo<MerchantSettledResponse> findSettledRecord(PageParamRequest pageParamRequest) {
        Integer uid = userService.getUserIdException();
        PageInfo<MerchantApply> pageInfo = merchantApplyService.findSettledRecord(uid, pageParamRequest);
        List<MerchantApply> merchantApplyList = pageInfo.getList();
        if (CollUtil.isEmpty(merchantApplyList)) {
            return CommonPage.copyPageInfo(pageInfo, CollUtil.newArrayList());
        }
        List<MerchantSettledResponse> responseList = merchantApplyList.stream().map(apply -> {
            MerchantSettledResponse response = new MerchantSettledResponse();
            BeanUtils.copyProperties(apply, response);
            return response;
        }).collect(Collectors.toList());
        return CommonPage.copyPageInfo(pageInfo, responseList);
    }

    /**
     * 商户搜索列表
     *
     * @param request          搜索参数
     * @param pageParamRequest 分页参数
     * @return List
     */
    @Override
    public PageInfo<MerchantSearchResponse> findSearchList(MerchantMoveSearchRequest request, PageParamRequest pageParamRequest) {
        Page<Merchant> page = PageHelper.startPage(pageParamRequest.getPage(), pageParamRequest.getLimit());
        LambdaQueryWrapper<Merchant> lqw = Wrappers.lambdaQuery();
        if (ObjectUtil.isNotNull(request.getCategoryId())) {
            lqw.eq(Merchant::getCategoryId, request.getCategoryId());
        }
        if (ObjectUtil.isNotNull(request.getTypeId())) {
            lqw.eq(Merchant::getTypeId, request.getTypeId());
        }
        if (ObjectUtil.isNotNull(request.getIsSelf())) {
            lqw.eq(Merchant::getIsSelf, request.getIsSelf());
        }
        if (StrUtil.isNotEmpty(request.getKeywords())) {
            lqw.and(i -> i.like(Merchant::getName, request.getKeywords())
                    .or().apply(StrUtil.format(" find_in_set('{}', keywords)", request.getKeywords())));
        }
        lqw.eq(Merchant::getIsSwitch, true);
        lqw.eq(Merchant::getIsDel, false);
        lqw.orderByDesc(Merchant::getStarLevel, Merchant::getSort, Merchant::getId);
        List<Merchant> merchantList = dao.selectList(lqw);
        if (CollUtil.isEmpty(merchantList)) {
            return CommonPage.copyPageInfo(page, CollUtil.newArrayList());
        }
        List<MerchantSearchResponse> responseList = merchantList.stream().map(merchant -> {
            MerchantSearchResponse response = new MerchantSearchResponse();
            BeanUtils.copyProperties(merchant, response);
            // 获取商户推荐商品
            List<ProMerchantProductResponse> merchantProductResponseList = storeProductService.getRecommendedProductsByMerId(merchant.getId(), 3);
            response.setProList(merchantProductResponseList);
            return response;
        }).collect(Collectors.toList());
        return CommonPage.copyPageInfo(page, responseList);
    }

    /**
     * 店铺街
     *
     * @param pageParamRequest 分页参数
     * @return List
     */
    @Override
    public PageInfo<MerchantSearchResponse> getStreet(PageParamRequest pageParamRequest) {
        Page<Merchant> page = PageHelper.startPage(pageParamRequest.getPage(), pageParamRequest.getLimit());
        LambdaQueryWrapper<Merchant> lqw = Wrappers.lambdaQuery();
        lqw.eq(Merchant::getIsSwitch, true);
        lqw.eq(Merchant::getIsDel, false);
        lqw.orderByDesc(Merchant::getIsRecommend, Merchant::getSort, Merchant::getId);
        List<Merchant> merchantList = dao.selectList(lqw);
        if (CollUtil.isEmpty(merchantList)) {
            return CommonPage.copyPageInfo(page, CollUtil.newArrayList());
        }
        List<MerchantSearchResponse> responseList = merchantList.stream().map(merchant -> {
            MerchantSearchResponse response = new MerchantSearchResponse();
            BeanUtils.copyProperties(merchant, response);
            // 获取商户推荐商品
            List<ProMerchantProductResponse> merchantProductResponseList = storeProductService.getRecommendedProductsByMerId(merchant.getId(), 3);
            response.setProList(merchantProductResponseList);
            return response;
        }).collect(Collectors.toList());
        return CommonPage.copyPageInfo(page, responseList);
    }

    /**
     * 店铺首页信息
     *
     * @param id 商户id
     * @return MerchantIndexInfoResponse
     */
    @Override
    public MerchantIndexInfoResponse getIndexInfo(Integer id) {
        Integer userId = userService.getUserId();
        Merchant merchant = getByIdException(id);
        if (!merchant.getIsSwitch()) {
            throw new CrmebException("The store is not open");
        }
        MerchantIndexInfoResponse response = new MerchantIndexInfoResponse();
        BeanUtils.copyProperties(merchant, response);
        MerchantInfo merchantInfo = merchantInfoService.getByMerId(merchant.getId());
        BeanUtils.copyProperties(merchantInfo, response);
        if (userId > 0) {
            response.setIsCollect(userMerchantCollectService.isCollect(userId, merchant.getId()));
        } else {
            response.setIsCollect(false);
        }
        return response;
    }

    /**
     * 店铺详细信息
     *
     * @param id 商户id
     * @return MerchantDetailResponse
     */
    @Override
    public MerchantDetailResponse getDetail(Integer id) {
        Merchant merchant = getByIdException(id);
        if (!merchant.getIsSwitch()) {
            throw new CrmebException("The store is not open");
        }
        MerchantInfo merchantInfo = merchantInfoService.getByMerId(merchant.getId());
        MerchantDetailResponse response = new MerchantDetailResponse();
        BeanUtils.copyProperties(merchant, response);
        BeanUtils.copyProperties(merchantInfo, response);
        response.setCollectNum(userMerchantCollectService.getCountByMerId(merchant.getId()));
        return response;
    }

    /**
     * 商户是否使用商户分类
     *
     * @param cid 分类id
     * @return Boolean
     */
    @Override
    public Boolean isExistCategory(Integer cid) {
        LambdaQueryWrapper<Merchant> lqw = Wrappers.lambdaQuery();
        lqw.select(Merchant::getId);
        lqw.eq(Merchant::getIsDel, false);
        lqw.eq(Merchant::getCategoryId, cid);
        lqw.last(" limit 1");
        Merchant merchant = dao.selectOne(lqw);
        return ObjectUtil.isNotNull(merchant);
    }

    /**
     * 商户是否使用商户类型
     *
     * @param tid 类型id
     * @return Boolean
     */
    @Override
    public Boolean isExistType(Integer tid) {
        LambdaQueryWrapper<Merchant> lqw = Wrappers.lambdaQuery();
        lqw.select(Merchant::getId);
        lqw.eq(Merchant::getIsDel, false);
        lqw.eq(Merchant::getTypeId, tid);
        lqw.last(" limit 1");
        Merchant merchant = dao.selectOne(lqw);
        return ObjectUtil.isNotNull(merchant);
    }

    /**
     * 客服类型校验
     */
    private void serviceTypeCheck(MerchantConfigInfoVo request) {
        if (request.getServiceType().equals(MerchantConstants.MERCHANT_SERVICE_TYPE_H5)) {
            if (StrUtil.isEmpty(request.getServiceLink())) {
                throw new CrmebException("客服H5链接不能为空");
            }
        }
        if (request.getServiceType().equals(MerchantConstants.MERCHANT_SERVICE_TYPE_PHONE)) {
            if (StrUtil.isEmpty(request.getServicePhone())) {
                throw new CrmebException("客服电话不能为空");
            }
        }
        if (request.getServiceType().equals(MerchantConstants.MERCHANT_SERVICE_TYPE_MESSAGE)) {
            if (StrUtil.isEmpty(request.getServiceMessage())) {
                throw new CrmebException("客服Message不能为空");
            }
        }
        if (request.getServiceType().equals(MerchantConstants.MERCHANT_SERVICE_TYPE_EMAIL)) {
            if (StrUtil.isEmpty(request.getServiceEmail())) {
                throw new CrmebException("客服邮箱不能为空");
            }
        }
    }

    /**
     * 检查商户名是否重复
     *
     * @param name 商户名称
     */
    private Boolean checkMerchantName(String name) {
        LambdaQueryWrapper<Merchant> lqw = Wrappers.lambdaQuery();
        lqw.select(Merchant::getId);
        lqw.eq(Merchant::getName, name);
        lqw.eq(Merchant::getIsDel, false);
        lqw.last(" limit 1");
        Merchant merchant = dao.selectOne(lqw);
        return ObjectUtil.isNotNull(merchant);
    }

    /**
     * 检查商户名是否重复
     *
     * @param name 商户名称
     * @param id   商户ID
     */
    private Boolean checkMerchantName(String name, Integer id) {
        LambdaQueryWrapper<Merchant> lqw = Wrappers.lambdaQuery();
        lqw.select(Merchant::getId);
        lqw.eq(Merchant::getName, name);
        lqw.ne(Merchant::getId, id);
        lqw.eq(Merchant::getIsDel, false);
        lqw.last(" limit 1");
        Merchant merchant = dao.selectOne(lqw);
        return ObjectUtil.isNotNull(merchant);
    }

    /**
     * 检查商户邮箱是否重复
     *
     * @param email 邮箱
     */
    private Boolean checkMerchantEmail(String email) {
        LambdaQueryWrapper<Merchant> lqw = Wrappers.lambdaQuery();
        lqw.select(Merchant::getId);
        lqw.eq(Merchant::getEmail, email);
        lqw.eq(Merchant::getIsDel, false);
        lqw.last(" limit 1");
        Merchant merchant = dao.selectOne(lqw);
        return ObjectUtil.isNotNull(merchant);
    }

    /**
     * 检查商户邮箱是否重复
     *
     * @param email 邮箱
     * @param id    商户ID
     */
    private Boolean checkMerchantEmail(String email, Integer id) {
        LambdaQueryWrapper<Merchant> lqw = Wrappers.lambdaQuery();
        lqw.select(Merchant::getId);
        lqw.eq(Merchant::getEmail, email);
        lqw.eq(Merchant::getIsDel, false);
        lqw.ne(Merchant::getId, id);
        lqw.last(" limit 1");
        Merchant merchant = dao.selectOne(lqw);
        return ObjectUtil.isNotNull(merchant);
    }
}

