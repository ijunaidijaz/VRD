package com.vrd.gsaf.api.responses.appliedJobs;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class AppliedJob {

    @SerializedName("job_applied_id")
    @Expose
    private Integer jobAppliedId;
    @SerializedName("fair_id")
    @Expose
    private Integer fairId;
    @SerializedName("company_id")
    @Expose
    private Integer companyId;
    @SerializedName("job_id")
    @Expose
    private Integer jobId;
    @SerializedName("recruiter_id")
    @Expose
    private Integer recruiterId;
    @SerializedName("candidate_id")
    @Expose
    private Integer candidateId;
    @SerializedName("import_apply_id")
    @Expose
    private String importApplyId;
    @SerializedName("created_at")
    @Expose
    private String createdAt;
    @SerializedName("updated_at")
    @Expose
    private String updatedAt;

    public Integer getJobAppliedId() {
        return jobAppliedId;
    }

    public void setJobAppliedId(Integer jobAppliedId) {
        this.jobAppliedId = jobAppliedId;
    }

    public Integer getFairId() {
        return fairId;
    }

    public void setFairId(Integer fairId) {
        this.fairId = fairId;
    }

    public Integer getCompanyId() {
        return companyId;
    }

    public void setCompanyId(Integer companyId) {
        this.companyId = companyId;
    }

    public Integer getJobId() {
        return jobId;
    }

    public void setJobId(Integer jobId) {
        this.jobId = jobId;
    }

    public Integer getRecruiterId() {
        return recruiterId;
    }

    public void setRecruiterId(Integer recruiterId) {
        this.recruiterId = recruiterId;
    }

    public Integer getCandidateId() {
        return candidateId;
    }

    public void setCandidateId(Integer candidateId) {
        this.candidateId = candidateId;
    }

    public String getImportApplyId() {
        return importApplyId;
    }

    public void setImportApplyId(String importApplyId) {
        this.importApplyId = importApplyId;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public String getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(String updatedAt) {
        this.updatedAt = updatedAt;
    }

}
