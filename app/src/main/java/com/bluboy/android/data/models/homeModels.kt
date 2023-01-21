package com.bluboy.android.data.models

import com.bluboy.android.presentation.utility.AppConstant
import com.google.gson.annotations.SerializedName


data class LogoutPRQ(
    @SerializedName(AppConstant.authKey) val authKey: String
)

data class CMSPagePRQ(
    @SerializedName("page_code")
    var page_code: String? = ""
)

data class CMSPageRS(
    @SerializedName("data")
    var cmsData: CMSData? = null
) : BaseResponse()

data class CMSData(
    @SerializedName("page_title")
    var pageTitle: String? = "",
    @SerializedName("page_content")
    var pageContent: String? = "",
    @SerializedName("page_description")
    var pageDescription: String? = ""
)

data class FAQDataListRS(
    @SerializedName("data")
    var faqData: FaqData? = null
) : BaseResponse()

data class FaqData(
    @SerializedName("faqs")
    var faqs: List<FAQDetail>? = listOf()
)

data class FAQDetail(
    @SerializedName("faq_answer")
    var faqAnswer: String = "",
    @SerializedName("faq_id")
    var faqId: String = "",
    @SerializedName("faq_question")
    var faqQuestion: String = "",
    var isExpanded: Boolean = false
)

data class ContactUsPRQ(
    @SerializedName(AppConstant.authKey) val authKey: String? = "",
    @SerializedName(AppConstant.email) var contact_email: String? = "",
    @SerializedName(AppConstant.feedback) var feedback: String? = "",
    @SerializedName(AppConstant.app_version) var vAppVersion: String? = "",
    @SerializedName(AppConstant.device_type) var eDeviceType: String? = "",
    @SerializedName(AppConstant.device_name) var vDeviceName: String? = ""
)
