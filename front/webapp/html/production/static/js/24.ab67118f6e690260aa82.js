webpackJsonp([24],{"+Squ":function(e,t){},e0CP:function(e,t,s){"use strict";Object.defineProperty(t,"__esModule",{value:!0});var o=s("pFYg"),i=s.n(o),r=s("Dd8w"),n=s.n(r),a=s("fxnj"),d=s.n(a),c=s("taJy"),l=s("62KO"),h=s("4FCr"),u=s("NYxO"),f=(n()({},Object(u.c)(["getorderTel"]),{fpData:function(){var e={};return this.getorderTel.orderAttrList.forEach(function(t){e=n()({},e,t)}),e}}),c.a,l.a,h.a,n()({},Object(u.b)(["myOrderDetail","cancelOrder","orderCancelReason","deleteOrder","confirmReceipt"]),{ontouchstart:function(e){this.sy=e.changedTouches[0].pageY},ontouchend:function(e){this.ey=e.changedTouches[0].pageY,this.ey-this.sy>50&&this.reFresh()},reFresh:function(){this.isFrsh||(this.isFrsh=!0,this.showLoading(),this.myOrderDetail({this:this,method:this.myOrderDetail,data:{orderId:this.$route.query.id},frsh:!0}))},gogod:function(e){d.a.miniProgram.navigateTo({url:"/pages/goodDetail/index?id="+e})},shoumy:function(e){this.$router.push("/refund?csi="+e+"&id="+this.$route.query.id)},lijifk:function(e){d.a.miniProgram.navigateTo({url:"/pages/payGd/index?id="+e.orderId+"&s="+e.saleOrderTypeId+"&m="+e.orderSubTotal})},copy:function(){this.myOrderDetail({this:this,method:this.myOrderDetail,data:{orderId:this.$route.query.id}})},sureRe:function(e){this.showLoading(),this.confirmReceipt({data:{orderId:e},this:this,method:this.confirmReceipt})},goCe:function(){var e={orderId:this.$route.query.id,operateReason:this.slected[0]};console.log(this.slected),this.cancelOrder({data:e,this:this}),this.show1=!1},cancelOrd:function(){this.orderCancelReason({this:this}),this.show1=!0},setRe:function(e){e=e.map(function(e){return{name:e.description,value:e.description}}),this.menus1=[e]},hxcz:function(){this.showLoading(),this.myOrderDetail({this:this,method:this.myOrderDetail,data:{orderId:this.$route.query.id},fg:1})},onConfirm:function(){this.deleteOrder({data:{orderId:this.$route.query.id},this:this,method:this.deleteOrder})},delOd:function(){var e=this;this.$vux.confirm.show({content:"确认删除订单",onCancel:function(){},onConfirm:function(){e.onConfirm()}})},goBack:function(){this.$router.replace("/myOrder")},timeh:function(e){var t=this;this.spTime=parseInt(e.diffTime)||0,console.log(this.spTime),console.log(i()(this.spTime));var s=void 0,o=void 0,r=void 0,n=void 0;window.nore=setInterval(function(){s=parseInt(t.spTime)||0,console.log(s),s<=0?t.myOrderDetail({this:t,data:{orderId:t.$route.query.id}}):(n=s%60,s=parseInt(s/60),r=s%60,s=parseInt(s/60),o=s%24,t.spatime=o+"时"+r+"分"+n+"秒",t.spTime=parseInt(t.spTime)-1,console.log(t.spTime))},1e3)},choseWhy:function(e){console.log(e)}}),{data:function(){return{isFrsh:!1,ey:0,sy:0,spTime:0,nore:"",show1:!1,menus1:[[]],slected:[""],spatime:""}},computed:n()({},Object(u.c)(["getorderTel"]),{fpData:function(){var e={};return this.getorderTel.orderAttrList.forEach(function(t){e=n()({},e,t)}),e}}),created:function(){window.nore=null,this.$route.query.token&&localStorage.setItem("token",decodeURIComponent(this.$route.query.token)),this.$route.query.refreshToken&&localStorage.setItem("refreshToken",decodeURIComponent(this.$route.query.refreshToken)),this.showLoading(),this.myOrderDetail({this:this,method:this.myOrderDetail,data:{orderId:this.$route.query.id}})},components:{Picker:c.a,Confirm:l.a,LoadMore:h.a},methods:n()({},Object(u.b)(["myOrderDetail","cancelOrder","orderCancelReason","deleteOrder","confirmReceipt"]),{ontouchstart:function(e){this.sy=e.changedTouches[0].pageY},ontouchend:function(e){this.ey=e.changedTouches[0].pageY,this.ey-this.sy>50&&this.reFresh()},reFresh:function(){this.isFrsh||(this.isFrsh=!0,this.showLoading(),this.myOrderDetail({this:this,method:this.myOrderDetail,data:{orderId:this.$route.query.id},frsh:!0}))},gogod:function(e){d.a.miniProgram.navigateTo({url:"/pages/goodDetail/index?id="+e})},shoumy:function(e){this.$router.push("/refund?csi="+e+"&id="+this.$route.query.id)},lijifk:function(e){d.a.miniProgram.navigateTo({url:"/pages/payGd/index?id="+e.orderId+"&s="+e.saleOrderTypeId+"&m="+e.orderSubTotal})},copy:function(){this.myOrderDetail({this:this,method:this.myOrderDetail,data:{orderId:this.$route.query.id}})},sureRe:function(e){this.showLoading(),this.confirmReceipt({data:{orderId:e},this:this,method:this.confirmReceipt})},goCe:function(){var e={orderId:this.$route.query.id,operateReason:this.slected[0]};console.log(this.slected),this.cancelOrder({data:e,this:this}),this.show1=!1},cancelOrd:function(){this.orderCancelReason({this:this}),this.show1=!0},setRe:function(e){e=e.map(function(e){return{name:e.description,value:e.description}}),this.menus1=[e]},hxcz:function(){this.showLoading(),this.myOrderDetail({this:this,method:this.myOrderDetail,data:{orderId:this.$route.query.id},fg:1})},onConfirm:function(){this.deleteOrder({data:{orderId:this.$route.query.id},this:this,method:this.deleteOrder})},delOd:function(){var e=this;this.$vux.confirm.show({content:"确认删除订单",onCancel:function(){},onConfirm:function(){e.onConfirm()}})},goBack:function(){this.$router.replace("/myOrder")},timeh:function(e){var t=this;this.spTime=parseInt(e.diffTime)||0,console.log(this.spTime),console.log(i()(this.spTime));var s=void 0,o=void 0,r=void 0,n=void 0;window.nore=setInterval(function(){s=parseInt(t.spTime)||0,console.log(s),s<=0?t.myOrderDetail({this:t,data:{orderId:t.$route.query.id}}):(n=s%60,s=parseInt(s/60),r=s%60,s=parseInt(s/60),o=s%24,t.spatime=o+"时"+r+"分"+n+"秒",t.spTime=parseInt(t.spTime)-1,console.log(t.spTime))},1e3)},choseWhy:function(e){console.log(e)}}),beforeDestroy:function(){clearInterval(window.nore)},mounted:function(){document.title="订单详情"}}),m={render:function(){var e=this,t=e.$createElement,s=e._self._c||t;return s("div",{staticClass:"orderDetail",on:{touchend:function(t){t.stopPropagation(),e.ontouchend(t)},touchstart:function(t){t.stopPropagation(),e.ontouchstart(t)}}},[s("div",{staticClass:"oDt flex flexcentera money bf"},[s("div",{staticClass:"flex flexcenter"},["待付款"==e.getorderTel.orderStatus?[s("i",{staticClass:"iconfont"},[e._v("")]),e._v(" "),s("span",[e._v("等待付款")]),e._v(" "),s("span",{staticStyle:{"margin-left":"0.53rem"}},[e._v("剩余时间："+e._s(e.spatime))])]:e._e(),e._v(" "),"待收货"==e.getorderTel.orderStatus?[s("i",{staticClass:"iconfont"},[e._v("")]),e._v(" "),"Y"==e.getorderTel.isReallyWaitShip?s("span",[e._v("等待发货")]):s("span",[e._v("等待收货")])]:e._e(),e._v(" "),"交易成功"==e.getorderTel.orderStatus?[s("i",{staticClass:"iconfont"},[e._v("")]),e._v(" "),s("span",[e._v("交易完成")])]:e._e(),e._v(" "),"交易关闭"==e.getorderTel.orderStatus?[s("i",{staticClass:"iconfont"},[e._v("")]),e._v(" "),s("span",[e._v("交易关闭")])]:e._e()],2)]),e._v(" "),s("load-more",{directives:[{name:"show",rawName:"v-show",value:e.isFrsh,expression:"isFrsh"}],staticStyle:{margin:"0 auto","margin-bottom":"0.1rem"},attrs:{tip:"正在刷新","show-loading":!0}}),e._v(" "),e._l(e.getorderTel.postalAddressList,function(t){return e.getorderTel.postalAddressList&&e.getorderTel.postalAddressList.length?s("ul",{key:t.mobilePhone,staticClass:"adress_sj bf"},[s("li",{staticClass:"bold"},[s("span",[e._v("收货人:"+e._s(t.attnName))]),s("span",[e._v(e._s(t.mobilePhone))])]),e._v(" "),s("li",{staticClass:"re"},[s("i",{staticClass:"iconfont"},[e._v("")]),e._v(e._s((t.provinceName||"")+(t.cityName||"")+(t.countyName||"")+(t.address1||"")))])]):e._e()}),e._v(" "),e.getorderTel.products.length?s("dl",{staticClass:"goods_de"},[s("dt",{staticClass:"bf"},[e._v("商品信息")]),e._v(" "),e._l(e.getorderTel.products,function(t){return s("dd",{key:t.productId,staticClass:"flex",on:{click:function(s){s.stopPropagation(),e.gogod(t.productId)}}},[s("div",{staticClass:"img_kk"},[s("img",{staticClass:"goods_img",attrs:{src:t.mediumImageUrl,alt:""}})]),e._v(" "),s("div",{staticClass:"flex flexcolumn flexaround re",staticStyle:{flex:"1"}},[s("ul",[s("li",{staticClass:"con_de lineclamp"},[e._v(e._s(t.productName))]),e._v(" "),s("li",{staticClass:"con_fj"},[e._v("数量："+e._s(parseInt(t.quantity)))])]),e._v(" "),0==t.price||"Y"==t.isPromoGift?s("div",{staticClass:"fdad"},[e._v("赠品")]):s("p",{staticClass:"money bold"},[s("i",[e._v("￥")]),e._v(e._s(t.price))]),e._v(" "),"交易成功"==e.getorderTel.orderStatus||"待收货"==e.getorderTel.orderStatus?s("button",{directives:[{name:"show",rawName:"v-show",value:"N"==t.isPromoGift&&!t.returnId,expression:"list.isPromoGift=='N'&&!list.returnId"}],staticClass:"zdy_btn sqsh",on:{click:function(s){s.stopPropagation(),e.shoumy(t.productId)}}},[e._v("申请售后")]):e._e()])])})],2):e._e(),e._v(" "),e.getorderTel.orderId?s("dl",{staticClass:"goods_de goods_de_1"},[s("dt",{staticClass:"bf"},[e._v("订单信息")]),e._v(" "),s("dd",{staticClass:"bf"},[s("p",[e._v("订单编号："+e._s(e.getorderTel.orderId))]),e._v(" "),e.getorderTel.createdStamp?s("p",[e._v("下单时间："+e._s(e.getorderTel.createdStamp.indexOf(".")>-1?e.getorderTel.createdStamp.slice(0,e.getorderTel.createdStamp.indexOf(".")):e.getorderTel.createdStamp))]):e._e(),e._v(" "),s("p",[e._v("支付方式："+e._s(e.getorderTel.paymentStyle))]),e._v(" "),s("p",[e._v("快递方式："+e._s(e.getorderTel.expressWay))])]),e._v(" "),s("dd",{staticClass:"bf"},["Y"==e.fpData.needInvoice?s("div",[s("p",[e._v("发票抬头："+e._s(e.fpData.invoiceTitle))]),e._v(" "),s("p",[e._v("发票类型：普通发票"+e._s("Person"==e.fpData.invoiceType?"个人":"单位"))]),e._v(" "),s("p",[e._v("发票内容："+e._s(e.fpData.invoiceContent))])]):e._e()]),e._v(" "),s("dd",{staticClass:"bf"},[s("div",{staticClass:"flex flexjustify flexcenter"},[s("span",[e._v("商品金额（"+e._s(e.getorderTel.num)+"件）")]),e._v(" "),s("span",{staticClass:"money"},[e._v("￥"+e._s(e.getorderTel.orderGrandTotal))])]),e._v(" "),e.getorderTel.orderHeaderAdjustments?s("div",{staticClass:"flex flexjustify flexcenter"},[s("span",[e._v("优惠")]),e._v(" "),s("span",{staticClass:"money"},[e._v("-￥"+e._s(e.getorderTel.orderHeaderAdjustments))])]):e._e(),e._v(" "),s("div",{staticClass:"flex flexjustify flexcenter"},[s("span",[e._v("运费")]),e._v(" "),s("span",{staticClass:"money"},[e._v("￥"+e._s(e.getorderTel.orderShippingTotal))])])]),e._v(" "),s("dd",{staticClass:"jw bold bf",staticStyle:{"margin-top":"0"}},[e._v("\n  共"+e._s(e.getorderTel.num)+"个商品，需付款："),s("span",{staticClass:"money"},[e._v(e._s(e.getorderTel.orderSubTotal))])])]):e._e(),e._v(" "),s("div",{staticClass:"flex flexjustify bf",staticStyle:{"margin-top":"0.1rem"}},[s("div",{}),e._v(" "),s("div",{staticClass:"button_xq flex"},["待付款"==e.getorderTel.orderStatus?[s("button",{staticClass:"zdy_btn",on:{click:function(t){t.stopPropagation(),e.cancelOrd(t)}}},[e._v("取消订单")]),s("button",{staticClass:"zdy_btn",on:{click:function(t){t.stopPropagation(),e.lijifk(e.getorderTel)}}},[e._v("立即付款")])]:e._e(),e._v(" "),"交易成功"==e.getorderTel.orderStatus?[s("button",{staticClass:"zdy_btn",on:{click:e.delOd}},[e._v("删除订单")])]:e._e(),e._v(" "),"交易关闭"==e.getorderTel.orderStatus?[s("button",{staticClass:"zdy_btn",on:{click:function(t){t.stopPropagation(),e.delOd(t)}}},[e._v("删除订单")])]:e._e(),e._v(" "),"待收货"==e.getorderTel.orderStatus&&"Y"!=e.getorderTel.isReallyWaitShip?[s("button",{staticClass:"zdy_btn"},[e._v("查看物流")]),s("button",{staticClass:"zdy_btn",on:{click:function(t){t.stopPropagation(),e.sureRe(e.getorderTel.orderId)}}},[e._v("确认收货")])]:e._e()],2)]),e._v(" "),e.show1?[s("div",{staticStyle:{position:"fixed",bottom:"0",left:"0",right:"0",top:"0","background-color":"rgba(0,0,0,0.3)"}}),e._v(" "),s("div",{staticClass:"myPk"},[s("div",{staticClass:"flex flexcenter",on:{click:function(t){t.stopPropagation(),e.goCe(t)}}},[e._v("完成")]),e._v(" "),s("picker",{staticClass:"bf",attrs:{data:e.menus1},on:{"on-change":e.choseWhy},model:{value:e.slected,callback:function(t){e.slected=t},expression:"slected"}})],1)]:e._e(),e._v(" "),s("confirm",{on:{"on-confirm":e.onConfirm}})],2)},staticRenderFns:[]};var p=s("VU/8")(f,m,!1,function(e){s("+Squ")},"data-v-69d33267",null);t.default=p.exports}});
//# sourceMappingURL=24.ab67118f6e690260aa82.js.map