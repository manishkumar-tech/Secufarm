package com.weather.risk.mfi.myfarminfo.retofitservices;


import com.google.gson.JsonArray;
import com.weather.risk.mfi.myfarminfo.bean.IrrrigationMngtResponse;
import com.weather.risk.mfi.myfarminfo.bean.NutritionMngtResponse;
import com.weather.risk.mfi.myfarminfo.bean.PostHarvMngtResponse;
import com.weather.risk.mfi.myfarminfo.bean.WeedMngtRequest;
import com.weather.risk.mfi.myfarminfo.bean.WeedMngtResponse;
import com.weather.risk.mfi.myfarminfo.bean.YeildImprovementResponse;
import com.weather.risk.mfi.myfarminfo.groundwater.GroundWaterBeans;
import com.weather.risk.mfi.myfarminfo.groundwater.GroundWaterResponse;
import com.weather.risk.mfi.myfarminfo.marketplace.CollectAmountRequest;
import com.weather.risk.mfi.myfarminfo.marketplace.ServiceResponseModel;
import com.weather.risk.mfi.myfarminfo.payment.model.AmountListResponse;
import com.weather.risk.mfi.myfarminfo.payment.model.OTPResponse;
import com.weather.risk.mfi.myfarminfo.payment.model.OrderHistoryBean;
import com.weather.risk.mfi.myfarminfo.payment.model.OrderHistoryRequest;
import com.weather.risk.mfi.myfarminfo.payment.model.PaySurveyorAmountRequest;
import com.weather.risk.mfi.myfarminfo.payment.model.PaymentListRequest;
import com.weather.risk.mfi.myfarminfo.payment.model.SurveyorResponseBean;
import com.weather.risk.mfi.myfarminfo.policyregistration.ServiceDetailsResponse;
import com.weather.risk.mfi.myfarminfo.policyregistration.ServiceHistoryResponse;
import com.weather.risk.mfi.myfarminfo.policyregistration.UserFarmResponse;
import com.weather.risk.mfi.myfarminfo.retrofitmodelnew.BankNameResponse;
import com.weather.risk.mfi.myfarminfo.retrofitmodelnew.CategoryDetailResponse;
import com.weather.risk.mfi.myfarminfo.retrofitmodelnew.CategoryModel;
import com.weather.risk.mfi.myfarminfo.retrofitmodelnew.CattleDashboardOwnerListResponse;
import com.weather.risk.mfi.myfarminfo.retrofitmodelnew.CheckoutRequest;
import com.weather.risk.mfi.myfarminfo.retrofitmodelnew.CheckoutResponse;
import com.weather.risk.mfi.myfarminfo.retrofitmodelnew.Dashboard_FarmerResponse;
import com.weather.risk.mfi.myfarminfo.retrofitmodelnew.FarmerDetailsResponse;
import com.weather.risk.mfi.myfarminfo.retrofitmodelnew.FarmerListRequest;
import com.weather.risk.mfi.myfarminfo.retrofitmodelnew.FarmerResponse;
import com.weather.risk.mfi.myfarminfo.retrofitmodelnew.GetOTPRequest;
import com.weather.risk.mfi.myfarminfo.retrofitmodelnew.ImageDetectionRequest;
import com.weather.risk.mfi.myfarminfo.retrofitmodelnew.ImageDetectionResponse;
import com.weather.risk.mfi.myfarminfo.retrofitmodelnew.LoginCheckCattleDashboardResponse;
import com.weather.risk.mfi.myfarminfo.retrofitmodelnew.OrderDetailRequest;
import com.weather.risk.mfi.myfarminfo.retrofitmodelnew.OrderDetailResponse;
import com.weather.risk.mfi.myfarminfo.retrofitmodelnew.OtpVerifyRequest;
import com.weather.risk.mfi.myfarminfo.retrofitmodelnew.RequestCategoryBean;

import java.util.List;

import io.reactivex.Observable;
import retrofit2.Response;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;


public interface ApiService {


    //  Crop Scheduler Activity List
    @GET("GetCropAdvisory/{farmerID}/{Language}/na/0/{date}")
    Observable<Response<Dashboard_FarmerResponse>> getDashbaordfarmerDetail(@Path("farmerID") String FarmerId, @Path("Language") String Language,@Path("date") String date);
//    Call<JsonObject> getDashbaordfarmerDetail(@Path("farmerID") String FarmerId,@Path("Language") String Language);

    @POST("FarmerPersonel/GetProjectCategory")
    Observable<Response<List<CategoryModel>>> getCategory(@Body RequestCategoryBean body);


    @POST("FarmerPersonel/GetProjectServices")
    Observable<Response<List<CategoryDetailResponse>>> getCategoryDetails(@Body RequestCategoryBean body);

    @POST("FarmerOrder/AllFarmerWithBalanceAmount")
    Observable<Response<List<AmountListResponse>>> pendingAmountListByUser(@Body PaymentListRequest request);

    @POST("FarmerOrder/GetFarmerWiseOrderDetails")
    Observable<Response<List<List<OrderHistoryBean>>>> orderHistory(@Body OrderHistoryRequest request);


    @POST("FarmerOrder/OrderDetails")
    Observable<Response<OrderDetailResponse>> orderDetailMethod(@Body OrderDetailRequest request);

    @POST("AddServices")
    Observable<Response<CheckoutResponse>> checkoutMethod(@Body CheckoutRequest request);

//    @POST("FarmerOrder/SentOTP")
//    Observable<Response<Integer>> getOTPMethod(@Body GetOTPRequest request);

    @POST("FarmerOrder/SentOTP")
    Observable<Response<OTPResponse>> getOTPMethod(@Body GetOTPRequest request);

    @POST("FarmerOrder/VerifyOTP")
    Observable<Response<String>> verifyOTPMethod(@Body OtpVerifyRequest request);

    @POST("FarmerOrder/TotalCollection")
    Observable<Response<JsonArray>> collectAmountMethod(@Body CollectAmountRequest request);

    @POST("FarmerOrder/GetSurveyerCollectedAmount")
    Observable<Response<List<SurveyorResponseBean>>> getSurveyorCollectedAmount(@Body PaymentListRequest request);

    @POST("FarmerOrder/InsertSurveyerCollectedAmount")
    Observable<Response<String>> paySurveyorAmount(@Body PaySurveyorAmountRequest request);

    @POST("FarmerPersonel/GetFarmerDetails")
    Observable<Response<FarmerDetailsResponse>> getFarmerDetails(@Body FarmerListRequest body);


    @POST("FarmerPersonel/GetFarmerPersonelInfo")
    Observable<Response<List<FarmerResponse>>> getFarmerList(@Body FarmerListRequest body);

    //  Crop Scheduler Activity List
    @GET("AndroidAPI/getCattleOwnerID/{strToken}/{strPhoneno}")
    Observable<Response<List<LoginCheckCattleDashboardResponse>>> getCattleOwnerID(@Path("strToken") String strTokens, @Path("strPhoneno") String strPhonenos);

    //  Crop Scheduler Activity List
    @GET("AndroidAPI/getCattleID/{strToken}/{OwnerID}")
    Observable<Response<List<CattleDashboardOwnerListResponse>>> getCattleID(@Path("strToken") String strTokens, @Path("OwnerID") String OwnerIDs);


    //Image Detecting
    @POST("AndroidAIapi/API/AndroidMlDetection")
    Observable<Response<List<ImageDetectionResponse>>> getImageDetection(@Body ImageDetectionRequest body);


    @POST("FarmerOrder/GetCategoryTypeMaster")
    Observable<Response<List<ServiceResponseModel>>> getServiceMethod(@Body RequestCategoryBean body);

    //    Weed Management https://secu.farm/
    @POST("admin/getweeddata")
    Observable<Response<List<WeedMngtResponse>>> getWeedManagement(@Body WeedMngtRequest body);

    @POST("admin/getharvestingdata")
    Observable<Response<List<PostHarvMngtResponse>>> getPostHarvManagement(@Body WeedMngtRequest body);

    @POST("admin/getnutritiondata")
    Observable<Response<List<NutritionMngtResponse>>> getNutritionManagement(@Body WeedMngtRequest body);

    @POST("admin/getirrigationdata")
    Observable<Response<List<IrrrigationMngtResponse>>> getNutritionIrrigationManagement(@Body WeedMngtRequest body);

    @POST("admin/getweeddata")
    Observable<Response<List<YeildImprovementResponse>>> getYeildImprovement(@Body WeedMngtRequest body);

    //Policy Rgsitration

    @GET("policy/Banks")
    Observable<Response<List<BankNameResponse>>> getBankName();

    @POST("PolicyOrder/SentOTP")
    Observable<Response<OTPResponse>> getOTPMethodPolcy(@Body GetOTPRequest request);

    @POST("PolicyOrder/VerifyOTP")
    Observable<Response<String>> verifyOTPMethodPolicy(@Body OtpVerifyRequest request);

    @POST("PolicyOrder/GetFarmerWiseOrderDetails")
    Observable<Response<List<List<ServiceHistoryResponse>>>> ServiceHistory(@Body OrderHistoryRequest request);

    @POST("PolicyOrder/detail")
    Observable<Response<ServiceDetailsResponse>> SerivceDetailMethod(@Body OrderDetailRequest request);

    @POST("PolicyOrder/GetProjectServices")
    Observable<Response<List<CategoryDetailResponse>>> getPolicyCategoryDetails(@Body RequestCategoryBean body);

    @GET("Farm/Detailed/{UserID}")
    Observable<Response<List<UserFarmResponse>>> getUserFarmList(@Path("UserID") String UserID);

    /*Ground Water*/
    @POST("GroundWater")
    Observable<Response<List<GroundWaterResponse>>> getGroundWater(@Body GroundWaterBeans body);



}
