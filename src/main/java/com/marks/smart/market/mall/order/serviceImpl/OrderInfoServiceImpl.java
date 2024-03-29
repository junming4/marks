package com.marks.smart.market.mall.order.serviceImpl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.github.miemiedev.mybatis.paginator.domain.PageBounds;
import com.github.miemiedev.mybatis.paginator.domain.PageList;
import com.marks.common.domain.PojoDomain;
import com.marks.common.enums.AcctEnums;
import com.marks.common.enums.FeeEnums;
import com.marks.common.enums.OrderEnums;
import com.marks.common.enums.OrderEnums.CashType;
import com.marks.common.enums.StockEnums;
import com.marks.common.util.IDUtil;
import com.marks.common.util.date.DateUtil;
import com.marks.common.util.number.MoneyUtil;
import com.marks.smart.count.acct.base.service.UserExtService;
import com.marks.smart.count.acct.ext.pojo.PointLog;
import com.marks.smart.count.acct.ext.pojo.TranLog;
import com.marks.smart.count.acct.ext.service.AcctService;
import com.marks.smart.market.fee.log.pojo.FeeLog;
import com.marks.smart.market.fee.log.service.FeeLogService;
import com.marks.smart.market.mall.order.dao.OrderGoodDao;
import com.marks.smart.market.mall.order.dao.OrderInfoDao;
import com.marks.smart.market.mall.order.pojo.OrderGood;
import com.marks.smart.market.mall.order.pojo.OrderInfo;
import com.marks.smart.market.mall.order.service.OrderInfoService;
import com.marks.smart.market.mall.order.thread.LessVipPointThread;
import com.marks.smart.market.mall.order.thread.SalesThread;
import com.marks.smart.market.mall.stock.pojo.BarCode;
import com.marks.smart.market.mall.stock.pojo.StockBatch;
import com.marks.smart.market.mall.stock.pojo.StockBatchForm;
import com.marks.smart.market.mall.stock.pojo.StockInfo;
import com.marks.smart.market.mall.stock.service.BarCodeService;
import com.marks.smart.market.mall.stock.service.StockBatchService;
import com.marks.smart.market.mall.stock.service.StockInfoService;
import com.marks.smart.system.user.sysuser.pojo.SysUser;
import com.marks.smart.system.user.sysuser.service.SysUserService;

@Service
@Transactional
public class OrderInfoServiceImpl implements OrderInfoService {

	@Autowired
	private OrderInfoDao orderInfoDao;

	@Autowired
	private OrderGoodDao orderGoodDao;

	@Autowired
	private SysUserService sysUserService;

	@Autowired
	private StockBatchService stockBatchService;
	@Autowired
	private BarCodeService barCodeService;

	@Autowired
	private AcctService acctService;

	@Autowired
	private UserExtService userExtService;

	@Autowired
	private FeeLogService feeLogService;

	@Autowired
	private StockInfoService stockInfoService;
	private String pointType = "2";// 1:按商品积分 2:按订单金额积分 1元 1积分

	/**
	 * private OrderInfoDao orderInfoDao;
	 * 
	 * public OrderInfoDao getOrderInfoDao(){ return orderInfoDao; } public void
	 * setOrderInfoDao(OrderInfoDao orderInfoDao){ this.orderInfoDao =orderInfoDao;
	 * }
	 * 
	 */
	/**
	 * 根据ID查找订单管理
	 */
	@Override
	public OrderInfo findById(String id) {
		return orderInfoDao.findById(id);
	}

	/**
	 * 删除订单管理
	 */
	@Override
	public void delete(String id) {
		orderInfoDao.delete(id);
	}

	/**
	 * 查找所有订单管理
	 */
	@Override
	public List<OrderInfo> findAll() {
		return orderInfoDao.findAll();
	}

	/**
	 * 删除多个订单管理
	 */
	@Override
	public void deleteBatch(List<String> ids) {
		orderInfoDao.deleteBatch(ids);
	}

	public PojoDomain<OrderInfo> list(int page_number, int page_size, Map<String, Object> param) {
		PojoDomain<OrderInfo> pojoDomain = new PojoDomain<OrderInfo>();
		PageBounds pageBounds = new PageBounds(page_number, page_size);
		List<OrderInfo> list = orderInfoDao.list(pageBounds, param);
		PageList<OrderInfo> pageList = (PageList<OrderInfo>) list;
		pojoDomain.setPojolist(list);
		pojoDomain.setPage_number(page_number);
		pojoDomain.setPage_size(page_size);
		pojoDomain.setTotal_count(pageList.getPaginator().getTotalCount());
		return pojoDomain;
	}

	@Override
	public String getOrderId() {
		return IDUtil.getNumID();
	}

	@Override
	public void saveRecharge(OrderInfo info) {
		dealOrder(info);
		// 保存订单
		orderInfoDao.save(info);
		// 充值帐户
		saveBalAmt(info);
		// 记录费用
		saveFeeLog(info);
	}

	private void saveBalAmt(OrderInfo info) {
		TranLog log = new TranLog();
		log.setCashAmt(info.getPayAmt());
		log.setRemarks("会员充值");
		log.setSendAmt(MoneyUtil.subtract(info.getPayableAmt(), info.getPayAmt()));
		log.setTranAmt(info.getPayableAmt());
		log.setTranCode(AcctEnums.TranCode.add.getValue());
		log.setTranDesc("会员充值");
		log.setTranTime(info.getCommitTime());
		log.setUserid(info.getVipId());
		log.setChannelId(info.getChannelId());
		log.setOrgId(info.getOrgId());
		log.setOrgName(info.getOrgName());
		log.setOperatorCode(info.getCashManCode());
		log.setOperatorName(info.getCashMan());
		log.setOrderId(info.getOrderId());
		acctService.saveAmt(log);

	}

	private void dealOrder(OrderInfo info) {
		// 会员信息
		if (info.getVipId() != null && info.getVipId().length() > 4) {
			SysUser vip = sysUserService.findById(info.getCompanyId(), info.getVipId());
			if (vip != null) {
				info.setVipMobile(vip.getBind_mobile());
				info.setVipName(vip.getUsername());
				info.setVipCode(vip.getUserCode());
				info.setVipId(vip.getUserid());
			} else {
				info.setVipId(null);
			}
		}
		info.setI_year(Integer.parseInt(info.getCashDate().substring(0, 4)));
		info.setI_month(Integer.parseInt(info.getCashDate().substring(5, 7)));
		info.setI_season(DateUtil.getSeason(info.getI_month()));
		info.setRecevieAmt(MoneyUtil.add(info.getCashAmt(), info.getWxAmt()));
		info.setRecevieAmt(MoneyUtil.add(info.getRecevieAmt(), info.getAlipayAmt()));
		info.setRecevieAmt(MoneyUtil.add(info.getRecevieAmt(), info.getOtherAmt()));
	}

	@Override
	public void saveOrder(OrderInfo info, List<OrderGood> goodList, List<String> barCodeList) {
		dealOrder(info);
		// 计算订单商品
		List<StockBatch> stockList = new ArrayList<StockBatch>();

		List<BarCode> barList = null;
		if (barCodeList != null && barCodeList.size() > 0) {
			barList = barCodeService.getBarCodeListByBarCodes(barCodeList);
			if (null != barList && barList.size() > 0) {
				for (OrderGood good : goodList) {
					for (BarCode bar : barList) {
						if (good.getGoodId().equals(bar.getGoodId())) {
							good.addBarCode(bar.getBarcode());
							good.addBarList(bar);
						}

					}
				}
			}
		}
		// 给会员积分
		if (null != info.getVipId() && info.getVipId().length() > 4) {
			saveVipPoint(info, goodList);
		}
		if (info.getUsePoint() > 0) {
			saveVipLessPoint(info);
		}
		// 储值卡
		if (MoneyUtil.compare(info.getStoredAmt(), "0.01")) {
			saveVipLessBalAmt(info);
		}
		dealGood(info, goodList, stockList);
		// 保存订单
		orderInfoDao.save(info);
		// 保存订单商品
		orderGoodDao.saveBatch(goodList);
		// 处理会员等级等信息
		if (null != info.getVipId() && info.getVipId().length() > 4) {
			userExtService.updateVipInfoByOrder(info.getVipId(), info.getPoint(), info.getPayAmt());
		}
		if (MoneyUtil.compare(info.getRecevieAmt(), "0.01")) {
			// 记录费用
			saveFeeLog(info);
		}
		// 更新售出数量
		updateSaleNumsForStock(info, goodList);

		// 减库存
		if (stockList.size() > 0) {
			stockBatchService.updateSaleOut(stockList, barList);
		}
		// 处理订单赠送优惠
		if (null != info.getVipId() && info.getVipId().length() > 4) {
			new Thread(new SalesThread(info)).start();
		}
	}

	private void updateSaleNumsForStock(OrderInfo info, List<OrderGood> goodList) {
		for (OrderGood good : goodList) {
			// 更新售出数量
			StockInfo stock = new StockInfo();
			stock.setCompanyId(info.getCompanyId());
			stock.setGoodId(good.getGoodId());
			stock.setOrgId(info.getOrgId());
			stock.setOrgName(info.getOrgName());
			stock.setBarNo(good.getBarNo());
			stock.setGoodNo(good.getGoodNo());
			stock.setGoodName(good.getGoodName());
			stock.setSaleNums(good.getNums());
			stock.setSaleAmt(good.getPayAmt());
			stockInfoService.updateSaleOut(stock);
		}
	}

	@Override
	public void saveRefund(OrderInfo info, List<OrderGood> goodList, List<String> barCodeList) {
		// 处理订单
		dealOrder(info);
		// 计算订单商品
		List<StockBatch> stockList = new ArrayList<StockBatch>();

		List<BarCode> barList = null;
		if (barCodeList != null && barCodeList.size() > 0) {
			barList = barCodeService.getBarCodeListByBarCodes(barCodeList);
			if (null != barList && barList.size() > 0) {
				for (OrderGood good : goodList) {
					for (BarCode bar : barList) {
						if (good.getGoodId().equals(bar.getGoodId())) {
							good.addBarCode(bar.getBarcode());
							good.addBarList(bar);
						}

					}
				}
			}
		}
		// 处理原订单
		if (null != info.getOldOrderId() && info.getOldOrderId().length() > 4) {

			List<OrderGood> oldGoodList = orderGoodDao.findByOrderId(info.getOldOrderId());
			int usePoint = 0;
			String balAmt = "0";
			String payableAmt = "0";
			String pointAmt = "0";
			int refundNums = 0;
			if (null != oldGoodList && oldGoodList.size() > 0) {
				for (OrderGood oldGood : oldGoodList) {
					for (OrderGood good : goodList) {
						if (oldGood.getOrderGoodId().equals(good.getOldOrderGoodId())) {

							good.setPayPrice(oldGood.getPayPrice());
							good.setNowPrice(oldGood.getNowPrice());
							good.setPoint(oldGood.getPoint() / oldGood.getNums());
							good.setPayableAmt(MoneyUtil.multiply(good.getPayPrice(), good.getNums() + ""));
							refundNums += good.getNums();
							usePoint += oldGood.getUsePoint() * good.getNums() / oldGood.getNums();
							balAmt = MoneyUtil.add(balAmt,
									MoneyUtil.divide(MoneyUtil.multiply(oldGood.getStoredAmt(), good.getNums() + ""),
											oldGood.getNums() + ""));
							payableAmt = MoneyUtil.add(payableAmt,
									MoneyUtil.multiply(oldGood.getPayPrice(), good.getNums() + ""));
							pointAmt = MoneyUtil.add(pointAmt,
									MoneyUtil.divide(MoneyUtil.multiply(oldGood.getPointAmt(), good.getNums() + ""),
											oldGood.getNums() + ""));
							oldGood.setHadRefundAmt(MoneyUtil.add(oldGood.getHadRefundAmt(), good.getPayableAmt()));
							oldGood.setHadRefundNums(oldGood.getHadRefundNums() + good.getNums());
						}
					}
					orderGoodDao.update(oldGood);
				}
				OrderInfo old = orderInfoDao.findById(info.getOldOrderId());
				if (null != old) {
					old.setHadRefundAmt(MoneyUtil.add(old.getHadRefundAmt(), payableAmt));
					old.setHadRefundNums(old.getHadRefundNums() + refundNums);
					orderInfoDao.update(old);
				}
			}
			info.setPayAmt(payableAmt);
			info.setPayableAmt(payableAmt);
			info.setPayingAmt(payableAmt);
			info.setUsePoint(usePoint);
			info.setStoredAmt(balAmt);
			info.setPointAmt(pointAmt);
			// 给会员积分
			if (null != info.getVipId() && info.getVipId().length() > 4) {
				saveVipPoint(info, goodList);
			}
			if (info.getUsePoint() != 0) {
				saveVipLessPoint(info);
			}
			// 储值卡
			if (MoneyUtil.compare(info.getStoredAmt(), "0.01") || MoneyUtil.compare("-0.01", info.getStoredAmt())) {
				saveVipLessBalAmt(info);
			}
		}
		dealGood(info, goodList, stockList);
		// 保存订单
		orderInfoDao.save(info);
		// 保存订单商品
		orderGoodDao.saveBatch(goodList);
		// 减库存
		// 处理会员等级等信息
		if (!MoneyUtil.compare(info.getRecevieAmt(), "0.00")) {
			// 记录费用
			saveFeeLog(info);
		}
		// 退积分
		// 退储值卡金额

		// 更新售出数量
		updateSaleNumsForStock(info, goodList);
		// 退货处理
		for (OrderGood good : goodList) {
			StockBatchForm batch = new StockBatchForm();
			batch.setCompanyId(info.getCompanyId());
			batch.setCostPrice(good.getCostPrice());
			batch.setGoodId(good.getGoodId());
			batch.setNums(-good.getNums());
			batch.setOperator(info.getCashMan());
			batch.setOrderId(info.getOrderId());
			batch.setOrgid(info.getOrgId());
			batch.setOrgname(info.getOrgName());
			batch.setProductDate(DateUtil.getCurrDateStr().substring(0, 10));
			batch.setStockManageType(StockEnums.StockManageType.nums.getValue());
			batch.setSupplier2("");
			batch.setSupplierId2("");
			batch.setYwCode(StockEnums.YwCode.refund_stockIn.getValue());
			batch.setStockStatus(StockEnums.StockStatus.stockIn.getValue());
			batch.setRemarks("退货入库");
			batch.setTranSaleAmt(good.getPayAmt());
			batch.setTranSaleNums(good.getNums());
			batch.setStockType(StockEnums.StockType.good.getValue());
			stockBatchService.saveFirstStockIn(batch);
		}

	}

	private void saveFeeLog(OrderInfo info) {
		if (MoneyUtil.compare("0.01", info.getRecevieAmt()) && MoneyUtil.compare(info.getRecevieAmt(), "-0.01")) {
			return;
		}
		FeeLog log = new FeeLog();
		log.setCompanyId(info.getCompanyId());
		log.setTranAmt(info.getRecevieAmt());
		if (OrderEnums.CashType.recharge.getValue().equals(info.getCashType())) {
			log.setFeeCode(FeeEnums.FeeCode.recharge.getValue());
			log.setItemCode(FeeEnums.ItemCode.recharge.getValue());
			log.setInOrOut(FeeEnums.InOrOut.in.getValue());
			log.setRemarks("会员充值");
		} else if (OrderEnums.CashType.refund.getValue().equals(info.getCashType())) {
			log.setFeeCode(FeeEnums.FeeCode.consume.getValue());
			log.setItemCode(FeeEnums.ItemCode.refund.getValue());
			log.setInOrOut(FeeEnums.InOrOut.out.getValue());
			log.setRemarks("退货" + info.getRemarks() + "商品");
			log.setTranAmt(MoneyUtil.negate(info.getRecevieAmt()));
		} else {
			log.setFeeCode(FeeEnums.FeeCode.consume.getValue());
			log.setItemCode(FeeEnums.ItemCode.consume.getValue());
			log.setInOrOut(FeeEnums.InOrOut.in.getValue());
			log.setRemarks("购买" + info.getRemarks() + "商品");
		}
		log.setIdNo(info.getOrderId());
		log.setItemName(FeeEnums.ItemCode.getByKey(log.getItemCode()));
		log.setTranTime(info.getCommitTime());
		log.setPayeeCode(info.getCashManCode());
		log.setPayeeName(info.getCashMan());
		log.setPayeeOrgId(info.getOrgId());
		log.setPayeeOrgName(info.getOrgName());
		log.setSalesMan(info.getGuiderName());
		log.setSalesManCode(info.getGuiderCode());
		feeLogService.save(log);
	}

	private void saveVipLessBalAmt(OrderInfo info) {
		TranLog log = new TranLog();
		log.setSendAmt(info.getStoredAmt());
		log.setRemarks("会员购买消费");
		log.setTranAmt(info.getStoredAmt());
		log.setTranCode(AcctEnums.TranCode.less.getValue());
		log.setTranDesc("购买消费");
		if (OrderEnums.CashType.refund.getValue().equals(info.getCashType())) {
			log.setTranDesc("退货退款");
			log.setRemarks("会员退货退款");
			log.setTranCode(AcctEnums.TranCode.add.getValue());
			log.setTranAmt(String.valueOf(-Double.parseDouble(info.getStoredAmt())));
		}
		log.setTranTime(info.getCommitTime());
		log.setUserid(info.getVipId());
		log.setChannelId(info.getChannelId());
		log.setOrgId(info.getOrgId());
		log.setOrgName(info.getOrgName());
		log.setOperatorCode(info.getCashManCode());
		log.setOperatorName(info.getCashMan());
		log.setOrderId(info.getOrderId());
		acctService.saveAmt(log);
	}

	private void saveVipLessPoint(OrderInfo info) {
		PointLog log = new PointLog();
		log.setChannelId(info.getChannelId());
		log.setOperatorCode(info.getCashManCode());
		log.setOperatorName(info.getCashMan());
		log.setOrgId(info.getOrgId());
		log.setOrgName(info.getOrgName());
		log.setTranCode(AcctEnums.TranCode.less.getValue());
		log.setRemarks("购买扣减积分");
		log.setTranDesc("积分支付扣减");
		log.setTranPoint(info.getUsePoint());
		if (OrderEnums.CashType.refund.getValue().equals(info.getCashType())) {
			log.setRemarks("退货新增积分");
			log.setTranDesc("积分支付退货");
			log.setTranCode(AcctEnums.TranCode.add.getValue());
			log.setTranPoint(-info.getUsePoint());
		}
		log.setTranTime(info.getCommitTime());
		log.setUserid(info.getVipId());
		log.setOrderId(info.getOrderId());
		new Thread(new LessVipPointThread(log)).start();
//		acctService.savePoint(log);
	}

	private void saveVipPoint(OrderInfo info, List<OrderGood> goodList) {
		int goodPoint = 0;
		for (OrderGood good : goodList) {
			good.setPoint(good.getPoint() * good.getNums());
			goodPoint += good.getPoint();
			if (!"1".equals(pointType)) {
				good.setPoint(0);
			}
			good.setUsePoint(0);
		}

		PointLog log = new PointLog();
		log.setOrderId(info.getOrderId());
		if ("1".equals(pointType)) {
			log.setTranPoint(goodPoint);
		} else {
			double point = Double.parseDouble(info.getPayAmt());
			log.setTranPoint((int) point);
		}
		if (OrderEnums.CashType.refund.getValue().equals(info.getCashType())) {
			log.setTranPoint(-log.getTranPoint());
			log.setTranCode(AcctEnums.TranCode.less.getValue());
			log.setTranDesc("退货扣减");
			log.setRemarks("退货扣减积分");
		} else {
			log.setTranCode(AcctEnums.TranCode.add.getValue());
			log.setTranDesc("购买积分");
			log.setRemarks("购买商品积分");
		}
		log.setTranTime(info.getCommitTime());
		log.setUserid(info.getVipId());
		log.setChannelId(info.getChannelId());
		log.setOrgId(info.getOrgId());
		log.setOrgName(info.getOrgName());
		log.setOperatorCode(info.getCashManCode());
		log.setOperatorName(info.getCashMan());
		acctService.savePoint(log);
		info.setPoint(log.getTranPoint());
	}

	private void dealGood(OrderInfo info, List<OrderGood> goodList, List<StockBatch> stockList) {
		String payableAmt = "";// 应付金额
		int nums = 0;// 商品数量
		int idx = 1;
		for (OrderGood good : goodList) {
			info.setRemarks(good.getGoodName() + "等");
			good.setCompanyId(info.getCompanyId());
			good.setCompanyName(info.getCompanyName());
			good.setOrderGoodId(info.getOrderId() + idx);
			good.setOrderId(info.getOrderId());
			payableAmt = MoneyUtil.add(payableAmt, good.getPayableAmt());
			nums = nums + good.getNums();
			idx++;
		}
		info.setSimpleDiscountAmt(MoneyUtil.subtract(info.getPayableAmt(), info.getPayAmt()));
		info.setNums(nums);
		String costAmt = "";
		String salePriceAmt = "";
		String mandiscountAmt = "";
		String nowPriceAmt = "";// 现价金额
		String countGoodPayableAmt = "";
		for (OrderGood good : goodList) {
			String rate = "0.000000";
			double totalPayableAmt = Double.parseDouble(payableAmt);
			if (totalPayableAmt != 0) {
				rate = String.valueOf(Double.parseDouble(good.getPayableAmt()) / totalPayableAmt);
			}
			countGoodPayableAmt = MoneyUtil.multiply(info.getPayableAmt(), rate);
			// 实付金额
			good.setPayAmt(MoneyUtil.multiply(info.getPayAmt(), rate));
			good.setPayPrice(MoneyUtil.divide(good.getPayAmt(), String.valueOf(good.getNums())));
			// 收款金额
			good.setRecevieAmt(MoneyUtil.multiply(info.getRecevieAmt(), rate));
			// 售价金额
			good.setSalePriceAmt(MoneyUtil.multiply(good.getSalePrice(), String.valueOf(good.getNums())));
			// 现价金额
			good.setNowPriceAmt(MoneyUtil.multiply(good.getNowPrice(), String.valueOf(good.getNums())));
			// 支付金额
			good.setCashAmt(MoneyUtil.multiply(info.getCashAmt(), rate));
			good.setAlipayAmt(MoneyUtil.multiply(info.getAlipayAmt(), rate));
			good.setWxAmt(MoneyUtil.multiply(info.getWxAmt(), rate));
			good.setOtherAmt(MoneyUtil.multiply(info.getOtherAmt(), rate));
			good.setStoredAmt(MoneyUtil.multiply(info.getStoredAmt(), rate));

			// 积分抵扣金额
			good.setPointAmt(MoneyUtil.multiply(info.getPointAmt(), rate));
			if ("2".equals(pointType)) {
				good.setPoint((int) Double.parseDouble(MoneyUtil.multiply(info.getPoint() + "", rate)));
			}
			good.setUsePoint((int) Double.parseDouble(MoneyUtil.multiply(info.getUsePoint() + "", rate)));

			String paySalePriceAmt = MoneyUtil.multiply(String.valueOf(good.getPayNums()), good.getSalePrice());
			String payNowPriceAmt = MoneyUtil.multiply(String.valueOf(good.getPayNums()), good.getNowPrice());
			// 促销总额=满减促销+单品促销+单品折扣+赠送金额
			good.setSalesAmt(MoneyUtil.subtract(good.getSalePriceAmt(), good.getPayAmt()));
			// 单品促销
			good.setDiscountAmt(MoneyUtil.subtract(good.getSalePriceAmt(), good.getNowPriceAmt()));
			// 单品折扣
			good.setGoodManDiscountAmt(MoneyUtil.subtract(payNowPriceAmt, good.getPayableAmt()));
			// 满减促销
			good.setFullCutAmt(MoneyUtil.subtract(good.getPayableAmt(), countGoodPayableAmt));
			// 整单折扣
			good.setSimpleDiscountAmt(MoneyUtil.multiply(info.getSimpleDiscountAmt(), rate));
			// 手工总折扣
			good.setSimpleDiscountAmt(MoneyUtil.add(good.getSimpleDiscountAmt(), good.getGoodManDiscountAmt()));
			// 赠送金额
			good.setSendAmt(MoneyUtil.multiply(good.getSendNums() + "",good.getNowPrice()));
			BigDecimal payRate = new BigDecimal(rate);
			payRate = payRate.setScale(18, BigDecimal.ROUND_HALF_UP);
			good.setPayRate(payRate.toString());
			stock(info, good, stockList);
			costAmt = MoneyUtil.add(costAmt, good.getCostAmt());
			salePriceAmt = MoneyUtil.add(salePriceAmt, good.getSalePriceAmt());
			mandiscountAmt = MoneyUtil.add(mandiscountAmt, good.getGoodManDiscountAmt());
			nowPriceAmt = MoneyUtil.add(nowPriceAmt, good.getNowPriceAmt());
		}
		info.setNowPriceAmt(nowPriceAmt);
		info.setCostAmt(costAmt);
		info.setSalePriceAmt(salePriceAmt);
		;
		// 促销总额=满减促销+单品促销+单品折扣
		info.setSalesAmt(MoneyUtil.subtract(info.getSalePriceAmt(), info.getPayAmt()));
		// 满减金额
		info.setFullCutAmt(MoneyUtil.subtract(payableAmt, info.getPayableAmt()));
		// 单品促销总折扣
		info.setDiscountAmt(MoneyUtil.subtract(info.getSalePriceAmt(), info.getNowPriceAmt()));
		// 人工总折扣
		info.setSimpleDiscountAmt(MoneyUtil.add(info.getSimpleDiscountAmt(), mandiscountAmt));

	}

	private void stock(OrderInfo info, OrderGood good, List<StockBatch> stockList) {
		// 退货
		if (CashType.refund.getValue().equals(info.getCashType())) {
			good.setCostPrice(good.getCostPrice());
			good.setCostAmt(MoneyUtil.multiply(good.getCostPrice(), String.valueOf(good.getNums())));
		} else {// 购买
			List<StockBatch> stock = stockBatchService.getStockBatchByGood(info, good);
			if (null != stock && stock.size() > 0) {
				good.setCostPrice(stock.get(0).getCostPrice());
				good.setCostAmt(MoneyUtil.multiply(good.getCostPrice(), String.valueOf(good.getNums())));
				for (StockBatch b : stock) {
					b.setOperator(info.getCashMan());
				}
				stockList.addAll(stock);
			} else {
				good.setCostPrice(good.getCostPrice());
				good.setCostAmt(MoneyUtil.multiply(good.getCostPrice(), String.valueOf(good.getNums())));
			}
			if (null != good.getBarList() && good.getBarList().size() > 0) {
				for (BarCode b : good.getBarList()) {
					b.setMobile(info.getVipMobile());
					b.setOrderGoodId(good.getOrderGoodId());
					b.setOrderId(good.getOrderId());
					b.setOrgid(info.getOrgId());
					b.setOrgname(info.getOrgName());
					b.setPrice(good.getSalePrice());
					b.setSalePrice(MoneyUtil.divide(good.getPayAmt(), String.valueOf(good.getNums())));
					b.setUserid(info.getVipId());
					b.setUsername(info.getVipName());
					b.setOperator(info.getCashMan());
				}
			}
		}

		// 获取剩余库存
		StockInfo stockInfo = stockInfoService.findByOrgIdAndGoodNo(info.getCompanyId(), info.getOrgId(),
				good.getGoodNo());
		if (null != stockInfo) {
			int balStockNums = stockInfo.getStockNums() - good.getNums();
			if (balStockNums < 0) {
				balStockNums = 0;
			}
			good.setBalStockNums(balStockNums);
			good.setBalStockAmt(MoneyUtil.subtract(stockInfo.getStockAmount(), good.getCostAmt()));
			if (!MoneyUtil.compare(good.getBalStockAmt(), "0")) {
				good.setBalStockAmt("0.00");
			}
		}
		if (good.getBalStockNums() == 0) {
			good.setBalStockAmt("0.00");
		}

	}

}