<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="C" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ page import="com.pang.myPractice.global.Content" %>
<%--
  Created by IntelliJ IDEA.
  User: Administrator
  Date: 2019/6/23
  Time: 9:52
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<jsp:include page="top.jsp"/>
<section id="content" class="table-layout animated fadeIn">
    <div class="tray tray-center">
        <div class="content-header">
            <h2> 处理报销单 </h2>
            <p class="lead"></p>
        </div>
        <div class="admin-form theme-primary mw1000 center-block" style="padding-bottom: 175px;">
            <div class="panel heading-border">
                <div class="panel-body bg-light">
                    <div class="section-divider mt20 mb40">
                        <span> 基本信息 </span>
                    </div>
                    <div class="section row">
                        <div class="col-md-2">事由</div>
                        <div class="col-md-6">${claimVoucher.cause}</div>
                    </div>
                    <div class="section row">
                        <div class="col-md-2">创建人</div>
                        <div class="col-md-4">${claimVoucher.creater.name}</div>
                        <div class="col-md-2">创建时间</div>
                        <div class="col-md-4"><spring:eval expression="claimVoucher.createTime"/></div>
                    </div>
                    <div class="section row">
                        <div class="col-md-2">待处理人</div>
                        <div class="col-md-4">${claimVoucher.dealer.name}</div>
                        <div class="col-md-2">状态</div>
                        <div class="col-md-4">${claimVoucher.status}</div>
                    </div>
                    <div class="section-divider mt20 mb40">
                        <span> 费用明细 </span>
                    </div>
                    <div class="section row">
                        <C:forEach items="${items}" var="item">
                            <div class="col-md-3">${item.item}</div>
                            <div class="col-md-3">${item.amount}</div>
                            <div class="col-md-5">${item.comment}</div>
                        </C:forEach>
                    </div>
                    <div class="section row">
                        <div class="col-md-2">总金额</div>
                        <div class="col-md-6">${claimVoucher.totalAmount}</div>
                    </div>
                    <div class="section-divider mt20 mb40">
                        <span> 处理流程 </span>
                    </div>
                    <div class="section row">
                        <c:forEach items="${records}" var="record">
                            <div class="col-md-1">${record.dealer.name}</div>
                            <div class="col-md-3"><spring:eval expression="record.dealTime"/></div>
                            <div class="col-md-1">${record.dealWay}</div>
                            <div class="col-md-2">${record.dealResult}</div>
                            <div class="col-md-5">备注：${record.comment}</div>
                        </c:forEach>
                    </div>
                    <form:form id="admin-form" name="addForm" action="/claim_voucher/check" modelAttribute="record">
                        <form:hidden path="claimVoucherId"/>
                        <div class="panel-body bg-light">
                            <div class="section-divider mt20 mb40">
                                <span> 基本信息 </span>
                            </div>
                            <div class="section">
                                <label for="comment" class="field prepend-icon">
                                    <form:input path="comment" cssClass="gui-input" placeholder="备注..."/>
                                    <label for="comment" class="field-icon">
                                        <i class="fa fa-lock"></i>
                                    </label>
                                </label>
                            </div>
                            <div class="panel-footer text-right">
                                <C:if test="${sessionScope.employee.post == Content.POST_FM || sessionScope.employee.post == Content.POST_GM}">
                                    <button type="submit" class="button" name="dealWay"
                                            value="${Content.DEAL_PASS}">${Content.DEAL_PASS}</button>
                                    <button type="submit" class="button" name="dealWay"
                                            value="${Content.DEAL_BACK}">${Content.DEAL_BACK}</button>
                                    <button type="submit" class="button" name="dealWay"
                                            value="${Content.DEAL_REJECT}">${Content.DEAL_REJECT}</button>
                                </C:if>
                                <C:if test="${sessionScope.employee.post == Content.POST_CASHIER}">
                                    <button type="submit" class="button" name="dealWay"
                                            value="${Content.DEAL_PAID}">${Content.DEAL_PAID}</button>
                                </C:if>

                                <button type="button" class="button" onclick="javascript:window.history.go(-1);"> 返回
                                </button>
                            </div>
                        </div>
                    </form:form>
                </div>
            </div>
        </div>
    </div>
</section>
<jsp:include page="bottom.jsp"/>
