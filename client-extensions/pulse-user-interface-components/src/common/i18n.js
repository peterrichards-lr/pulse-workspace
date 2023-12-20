import i18next from "i18next"
import { initReactI18next } from "react-i18next"

const resources = {
    en: {
        translation: {
            app_name: "Pulse",
            name: "Name",
            description: "Description",
            campaignStatus: "Status",
            createCampaignSuccessMessage: "The campaign was created successfully",
            createCampaignSuccessTitle: "Success",
            createCampaignBadRequestTitle: "Invalid",
            unauthorizedMessage: "The bearer token has expired",
            unauthorizedTitle: "Error",
            createCampaignErrorMessage: "An error occurred when creating the campaign",
            createCampaignErrorTitle: "Error",
            createCampaignHeading: "Campaign Information",
            createCampaignSubmit: "Create",
            createCampaignReset: "Reset",
            startDate: "Begin",
            startDateInvalid: "The start date is invalid",
            endDate: "End",
            endDateInvalid: "The end date is invalid",
            notAnArray: "{{object}} is not an array",
            foundNOptions: "Found {{n}} option(s)",
            targetUrl: "Target Page",
            default: "{{object}} default",
            targetUrlOptionLabel: "{{v}} [{{k}}]",
            controlNameInvalid: "The {{controlName}} is invalid",
            utmDataPanelTitle: "Urchin Tracking Module (UTM) Data",
            utmContent: "Content",
            utmTerm: "Term",
            utmSource: "Source",
            utmMedium: "Medium",
            mandatory: "Required",
            xNotFound: "{{x}} was not found",
            info: "Info",
            warning: "Warning",
            danger: "Error",
            success: "Success",
            checkRefreshStatusErrorMessage: "An error occurred when checking the job status",
            checkRefreshStatusErrorTitle: "Error",
            checkRefreshStatusHeading: "Refresh Job Status",
            checkRefreshStatusSubmit: "Check Status",
            checkRefreshStatusReset: "Reset",
            checkRefreshStatusJobId: "Job Identifier",
            xIsRequired: "The {{x}} is required",
            refreshCacheSuccessMessage: "The cache was refreshed successfully",
            refreshCacheErrorMessage: "An error occurred when refreshing the cache",
            refreshCacheSubmit: "Refresh",
            refreshCacheRequestSubmitted: "Request submitted",
            basicDetailsPanelTitle: "Basic Details",
            lifecyclePanelTitle: "Lifecycle"
        },
    }
}
i18next
    .use(initReactI18next)
    .init({
        resources,
        lng: "en",
        interpolation: {
            escapeValue: false,
        },
    })
export default i18next;