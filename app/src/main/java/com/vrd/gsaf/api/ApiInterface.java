package com.vrd.gsaf.api;


import com.google.gson.JsonObject;
import com.vrd.gsaf.api.responses.BaseResponse;
import com.vrd.gsaf.api.responses.FairsHalls.FairHallsResponse;
import com.vrd.gsaf.api.responses.PrivacyAndConditionsReponse;
import com.vrd.gsaf.api.responses.SlotsResponse;
import com.vrd.gsaf.api.responses.aboutUs.AboutUs;
import com.vrd.gsaf.api.responses.appliedJobs.AppliedJobs;
import com.vrd.gsaf.api.responses.careerTest.CareerTest;
import com.vrd.gsaf.api.responses.companies.Companies;
import com.vrd.gsaf.api.responses.compnayDetail.CompanyDetails;
import com.vrd.gsaf.api.responses.dashboardRecruiters.MatchingRecruiters;
import com.vrd.gsaf.api.responses.events.Fairs;
import com.vrd.gsaf.api.responses.exitPoll.ExitPoll;
import com.vrd.gsaf.api.responses.fairDetail.FairDetail;
import com.vrd.gsaf.api.responses.fairExtraFields.FairExtraFieldsResponse;
import com.vrd.gsaf.api.responses.fairMedia.FairMedia;
import com.vrd.gsaf.api.responses.faq.Faqs;
import com.vrd.gsaf.api.responses.general.General;
import com.vrd.gsaf.api.responses.goodies.Goodies;
import com.vrd.gsaf.api.responses.halls.Halls;
import com.vrd.gsaf.api.responses.interviewDates.InterviewDates;
import com.vrd.gsaf.api.responses.interviewSlots.InterviewSlots;
import com.vrd.gsaf.api.responses.jobDetail.JobDetail;
import com.vrd.gsaf.api.responses.jobPercentage.JobsMatchPercentage;
import com.vrd.gsaf.api.responses.jobs.Jobs;
import com.vrd.gsaf.api.responses.login.Login;
import com.vrd.gsaf.api.responses.otp.Otp;
import com.vrd.gsaf.api.responses.receptionist.ReceptionistDetail;
import com.vrd.gsaf.api.responses.resetpassword.Resetpassword;
import com.vrd.gsaf.api.responses.schedules.Schedules;
import com.vrd.gsaf.api.responses.speakerDetail.SpeakerDetail;
import com.vrd.gsaf.api.responses.speakers.Speakers;
import com.vrd.gsaf.api.responses.standPoll.StandPoll;
import com.vrd.gsaf.api.responses.stands.StandsData;
import com.vrd.gsaf.api.responses.uploads.UploadedFileUrlResponse;
import com.vrd.gsaf.api.responses.webinarDetail.WebinarDetail;
import com.vrd.gsaf.api.responses.webinars.Webinars;
import com.vrd.gsaf.model.ContentModel;
import com.vrd.gsaf.model.GetTablesResponse;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Path;
import retrofit2.http.Url;

public interface ApiInterface {
//
//    @POST("fair/show-by-shortname")
//    Call<ShortName> showByShortName(@Body JsonObject body);
//
//    @Multipart
//    @POST("auth/candidates")
//    Call<Register> registerCandidate(@Part MultipartBody.Part file, @Part("json") RequestBody json);
//


    @POST("api/organizerfairs")
    Call<Fairs> getEvents(@Header("requested-lang") String language, @Header("Accept") String accept, @Header("app-id") String app_id, @Header("app-key") String app_key);


    @POST("api/auth/front/login")
    Call<Login> loginUser(@Header("requested-lang") String language, @Header("Accept") String accept, @Header("app-id") String app_id, @Header("app-key") String app_key, @Body JsonObject body);

    @POST("api/auth/front/mobile/sso/login")
    Call<Login> socialLogin(@Header("requested-lang") String language, @Header("Accept") String accept, @Header("app-id") String app_id, @Header("app-key") String app_key, @Body JsonObject body);

    @Multipart
    @POST("api/auth/candidates")
    Call<Login> registerUser(@Header("requested-lang") String language, @Header("Accept") String accept, @Header("app-id") String app_id, @Header("app-key") String app_key,
                             @Part MultipartBody.Part file, @Part("json") RequestBody json);


//    @Multipart
//    @POST("auth/candidates")
//    Call<Login> registerUser(@Header("app-id") String app_id, @Header("app-key") String app_key,
//                             @Part MultipartBody.Part file,
//                             @Part MultipartBody.Part data);

    @POST("api/auth/candidates")
    Call<Login> registerUser(@Header("requested-lang") String language, @Header("Accept") String accept, @Header("app-id") String app_id, @Header("app-key") String app_key, @Body JsonObject body);

    @POST("api/set/user/language")
    Call<BaseResponse> updateLanguage(@Header("Authorization") String token, @Header("requested-lang") String language, @Header("Accept") String accept, @Header("app-id") String app_id, @Header("app-key") String app_key, @Body JsonObject body);


    @POST("api/auth/fair/show-by-shortname")
    Call<FairDetail> getFairDetails(@Header("requested-lang") String language, @Header("Accept") String accept, @Header("app-id") String app_id, @Header("app-key") String app_key, @Body JsonObject body);

    @POST("api/auth/fair/fairextrafields")
    Call<FairExtraFieldsResponse> getFairExtraFields(@Header("requested-lang") String language, @Header("Accept") String accept, @Header("app-id") String app_id, @Header("app-key") String app_key, @Body JsonObject body);

    @POST("api/password/reset")
    Call<Resetpassword> resetPassword(@Header("requested-lang") String language, @Header("Accept") String accept, @Header("app-id") String app_id, @Header("app-key") String app_key, @Body JsonObject body);

    @GET
    Call<Login> verifyOtp(@Header("requested-lang") String language, @Header("Accept") String accept, @Header("app-id") String app_id, @Header("app-key") String app_key, @Url String url);

    @GET("api/fair/halls/list/{id}")
    Call<FairHallsResponse> getFairHalls(@Path("id") Integer id, @Header("Authorization") String token, @Header("requested-lang") String language, @Header("Accept") String accept, @Header("app-id") String app_id, @Header("app-key") String app_key);

    @GET
    Call<UploadedFileUrlResponse> getFileUrl(@Url String url, @Header("Authorization") String token, @Header("requested-lang") String language, @Header("Accept") String accept, @Header("app-id") String app_id, @Header("app-key") String app_key);

    @POST("api/password/create")
    Call<Otp> sendOTPToEmail(@Header("requested-lang") String language, @Header("Accept") String accept, @Header("app-id") String app_id, @Header("app-key") String app_key, @Body JsonObject body);

    @POST("api/auth/fair/speakers")
    Call<Speakers> getSpeakers(@Header("requested-lang") String language, @Header("Accept") String accept, @Header("app-id") String app_id, @Header("app-key") String app_key, @Body JsonObject body);

    @GET
    Call<AboutUs> getAboutUs(@Header("requested-lang") String language, @Header("Accept") String accept, @Header("app-id") String app_id, @Header("app-key") String app_key, @Url String url);

    @GET
    Call<Faqs> getFaq(@Header("requested-lang") String language, @Header("Accept") String accept, @Header("app-id") String app_id, @Header("app-key") String app_key, @Url String url);

    @POST("api/fair/career/test/show")
    Call<CareerTest> getCareerTest(@Header("requested-lang") String language, @Header("Accept") String accept, @Header("Authorization") String token, @Header("app-id") String app_id, @Header("app-key") String app_key, @Body JsonObject body);

    @POST("api/auth/get/fair/career/test")
    Call<CareerTest> getCareerTestWithOutAuth(@Header("requested-lang") String language, @Header("Accept") String accept, @Header("app-id") String app_id, @Header("app-key") String app_key, @Body JsonObject body);

    @POST("api/auth/fair/exhibitors")
    Call<Companies> getCompanies(@Header("requested-lang") String language, @Header("Accept") String accept, @Header("app-id") String app_id, @Header("app-key") String app_key, @Body JsonObject body);

    @POST("api/company/detail")
    Call<CompanyDetails> getCompanyDetail(@Header("requested-lang") String language, @Header("Accept") String accept, @Header("Authorization") String token, @Header("app-id") String app_id, @Header("app-key") String app_key, @Body JsonObject body);

    @POST("api/candidate/companies")
    Call<Companies> getMatchingCompanies(@Header("requested-lang") String language, @Header("Accept") String accept, @Header("Authorization") String token, @Header("app-id") String app_id, @Header("app-key") String app_key, @Body JsonObject body);

    @POST("api/auth/fair/webinars")
    Call<Webinars> getWebinars(@Header("requested-lang") String language, @Header("Accept") String accept, @Header("app-id") String app_id, @Header("app-key") String app_key, @Body JsonObject body);

    @POST("api/candidate/webinars")
    Call<Webinars> getMatchingWebinars(@Header("requested-lang") String language, @Header("Accept") String accept, @Header("Authorization") String token, @Header("app-id") String app_id, @Header("app-key") String app_key, @Body JsonObject body);

    @POST("api/auth/fair/jobs")
    Call<Jobs> getJobs(@Header("requested-lang") String language, @Header("Accept") String accept, @Header("app-id") String app_id, @Header("app-key") String app_key, @Body JsonObject body);

    @POST("api/candidate/jobs")
    Call<Jobs> getMatchingJobs(@Header("requested-lang") String language, @Header("Accept") String accept, @Header("Authorization") String token, @Header("app-id") String app_id, @Header("app-key") String app_key, @Body JsonObject body);

    @POST("api/candidate/company/jobs")
    Call<Jobs> getStandJobs(@Header("requested-lang") String language, @Header("Accept") String accept, @Header("Authorization") String token, @Header("app-id") String app_id, @Header("app-key") String app_key, @Body JsonObject body);

    @POST("api/save/candidate/career-test")
    Call<Login> submitCareerTest(@Header("requested-lang") String language, @Header("Accept") String accept, @Header("Authorization") String token, @Header("app-id") String app_id, @Header("app-key") String app_key, @Body JsonObject body);

    @POST("api/candidate/recruiters")
    Call<MatchingRecruiters> getMatchingRecruiters(@Header("requested-lang") String language, @Header("Accept") String accept, @Header("Authorization") String token, @Header("app-id") String app_id, @Header("app-key") String app_key, @Body JsonObject body);

    @POST("api/candidate/company/recruiters")
    Call<MatchingRecruiters> getStandRecruiters(@Header("requested-lang") String language, @Header("Accept") String accept, @Header("Authorization") String token, @Header("app-id") String app_id, @Header("app-key") String app_key, @Body JsonObject body);

    @POST("api/candidate/apply/job")
    Call<AppliedJobs> applyJob(@Header("requested-lang") String language, @Header("Accept") String accept, @Header("Authorization") String token, @Header("app-id") String app_id, @Header("app-key") String app_key, @Body JsonObject body);


    @POST("api/save/candidate/profile")
    Call<Login> updateProfile(@Header("requested-lang") String language, @Header("Accept") String accept, @Header("Authorization") String token, @Header("app-id") String app_id, @Header("app-key") String app_key, @Body JsonObject body);

    @POST("api/recruiter/available/slot/dates")
    Call<InterviewDates> interviewDates(@Header("requested-lang") String language, @Header("Accept") String accept, @Header("Authorization") String token, @Header("app-id") String app_id, @Header("app-key") String app_key, @Body JsonObject body);

    @POST("api/recruiter/available/slots")
    Call<InterviewSlots> interviewSlots(@Header("requested-lang") String language, @Header("Accept") String accept, @Header("Authorization") String token, @Header("app-id") String app_id, @Header("app-key") String app_key, @Body JsonObject body);

    @POST("api/get/recruiter/match/slots")
    Call<SlotsResponse> getSlots(@Header("requested-lang") String language, @Header("Accept") String accept, @Header("Authorization") String token, @Header("app-id") String app_id, @Header("app-key") String app_key, @Body JsonObject body);

    @POST("api/auth/networktables/list")
    Call<GetTablesResponse> getTables(@Header("requested-lang") String language, @Header("Accept") String accept, @Header("Authorization") String token, @Header("app-id") String app_id, @Header("app-key") String app_key, @Body JsonObject body);

    @POST("api/company/recruiters/scheduling/invite")
    Call<InterviewDates> bookInterview(@Header("requested-lang") String language, @Header("Accept") String accept, @Header("Authorization") String token, @Header("app-id") String app_id, @Header("app-key") String app_key, @Body JsonObject body);

    @POST("api/candidate/cancel/interview")
    Call<InterviewDates> cancelInterView(@Header("requested-lang") String language, @Header("Accept") String accept, @Header("Authorization") String token, @Header("app-id") String app_id, @Header("app-key") String app_key, @Body JsonObject body);

    @GET
    Call<PrivacyAndConditionsReponse> getPrivacyAndConditions(@Header("requested-lang") String language, @Header("Accept") String accept, @Header("app-id") String app_id, @Header("app-key") String app_key, @Url String url);

    @GET
    Call<SpeakerDetail> getSpeakerDetail(@Header("requested-lang") String language, @Header("Accept") String accept, @Header("app-id") String app_id, @Header("app-key") String app_key, @Url String url);

    @GET
    Call<ContentModel> getNeedHelp(@Header("requested-lang") String language, @Header("Accept") String accept, @Header("app-id") String app_id, @Header("app-key") String app_key, @Url String url);

    @POST("api/auth/fair/webinars/detail")
    Call<WebinarDetail> getWebinarDetail(@Header("requested-lang") String language, @Header("Accept") String accept, @Header("app-id") String app_id, @Header("app-key") String app_key, @Body RequestBody jsonObject);

    @GET
    Call<WebinarDetail> resendEmail(@Header("requested-lang") String language, @Header("Accept") String accept, @Header("Authorization") String token, @Header("app-id") String app_id, @Header("app-key") String app_key, @Url String url);

    @GET
    Call<StandsData> getStandsAgainstHall(@Header("requested-lang") String language, @Header("Accept") String accept, @Header("Authorization") String token, @Header("app-id") String app_id, @Header("app-key") String app_key, @Url String url);


    @GET
    Call<FairMedia> getFairMedia(@Header("requested-lang") String language, @Header("Accept") String accept, @Header("Authorization") String token, @Header("app-id") String app_id, @Header("app-key") String app_key, @Url String url);

    @GET
    Call<ReceptionistDetail> getReceptionistDetail(@Header("requested-lang") String language, @Header("Accept") String accept, @Header("Authorization") String token, @Header("app-id") String app_id, @Header("app-key") String app_key, @Url String url);


    @POST
    Call<CompanyDetails> getCompanyDetailWithNoAuth(@Header("requested-lang") String language, @Header("Accept") String accept, @Header("app-id") String app_id, @Header("app-key") String app_key, @Url String url, @Body JsonObject body);

    @GET
    Call<JobDetail> getJobDetailWithNoAuth(@Header("requested-lang") String language, @Header("Accept") String accept, @Header("app-id") String app_id, @Header("app-key") String app_key, @Url String url);

    @GET
    Call<Halls> getTotalHalls(@Header("requested-lang") String language, @Header("Accept") String accept, @Header("Authorization") String token, @Header("app-id") String app_id, @Header("app-key") String app_key, @Url String url);


    @GET
    Call<General> acceptInvitation(@Header("requested-lang") String language, @Header("Accept") String accept, @Header("Authorization") String token, @Header("app-id") String app_id, @Header("app-key") String app_key, @Url String url);


    @POST("api/company/recruiters/interview/invitations")
    Call<Schedules> getSchedules(@Header("requested-lang") String language, @Header("Accept") String accept, @Header("Authorization") String token, @Header("app-id") String app_id, @Header("app-key") String app_key, @Body JsonObject body);


    @POST("api/auth/company/poll")
    Call<StandPoll> getStandPolling(@Header("requested-lang") String language, @Header("Accept") String accept, @Header("Authorization") String token, @Header("app-id") String app_id, @Header("app-key") String app_key, @Body JsonObject body);


    @POST("api/auth/save/poll")
    Call<StandPoll> saveStandPolling(@Header("requested-lang") String language, @Header("Accept") String accept, @Header("Authorization") String token, @Header("app-id") String app_id, @Header("app-key") String app_key, @Body JsonObject body);


    @POST("api/auth/save/survey")
    Call<ExitPoll> saveExitPoll(@Header("requested-lang") String language, @Header("Accept") String accept, @Header("Authorization") String token, @Header("app-id") String app_id, @Header("app-key") String app_key, @Body JsonObject body);

    @POST("api/auth/fair/survey")
    Call<ExitPoll> getExitPolling(@Header("requested-lang") String language, @Header("Accept") String accept, @Header("Authorization") String token, @Header("app-id") String app_id, @Header("app-key") String app_key, @Body JsonObject body);

    @POST("api/auth/matching/detail")
    Call<JobsMatchPercentage> getJobPercentage(@Header("requested-lang") String language, @Header("Accept") String accept, @Header("Authorization") String token, @Header("app-id") String app_id, @Header("app-key") String app_key, @Body JsonObject body);

    @POST("api/goodies/list")
    Call<Goodies> getGoodieBags(@Header("requested-lang") String language, @Header("Accept") String accept, @Header("Authorization") String token, @Header("app-id") String app_id, @Header("app-key") String app_key, @Body JsonObject body);


    @POST("api/goodiebag/companylist")
    Call<Goodies> getGoodieBagList(@Header("requested-lang") String language, @Header("Accept") String accept, @Header("Authorization") String token, @Header("app-id") String app_id, @Header("app-key") String app_key, @Body JsonObject body);

    @POST("api/addgoodiecandidate")
    Call<Goodies> addGoodieBag(@Header("requested-lang") String language, @Header("Accept") String accept, @Header("Authorization") String token, @Header("app-id") String app_id, @Header("app-key") String app_key, @Body JsonObject body);


}
