package com.mine.growthstory.selectBg.bean

import androidx.annotation.Keep


/**
 * @author: qch
 * Date: 2022/07/05 13:58
 */
@Keep
class Material<T> {

    var id:Int = 0 //素材ID

    var title = "" //素材title

    var materialType:Int = 0 //素材类型

    var price:Int = 0 //素材价格

    var description :String? = null //素材描述

    var thumbnailUrl :String? = null //缩略图

    var usingLimit:Int = 0 //使用限制

    var content = ""  //素材内容 json串 ，不同类型会有不同结果根据 type 区分

    var version:String?=null //素材版本

    var productCode: String?=null //素材编码，单独购买时使用

    var status = 0 //素材发布状态 @link PublishStatus

    var purchaseType = 0  //

    var serviceLevel = 0  //

    var materialSource = 0 //

    var assetFormat:String?=null //字段[第三方素材库：格式]

    var userLimitMap:Map<Int,Int>? = null

    var materialCode = ""

    var publishId = 0

    var extra:String? = null //扩展字段，目前暂时不用

    var hasPurchase = false //是否已购买

    var entity:T ? = null  //根据不同type 反序列化content内容生成不类型 素材内容

    var downloadProgress = 0.0f //本地字段，下载时使用

    var isDownloading = false//本地字段

}