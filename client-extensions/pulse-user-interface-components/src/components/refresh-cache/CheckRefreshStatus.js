import ClayForm, {ClayInput} from '@clayui/form'
import ClayButton from "@clayui/button";
import ClayAlert from '@clayui/alert';
import {Liferay} from "../../common/services/liferay/liferay";
import {useMemo, useState} from "react";
import {useForm} from "react-hook-form";
import RequiredMask from "../common/RequiredMark";
import Sheet from "../common/Sheet";
import {getFetch} from "../../common/services/pulse/api";

const PULSE_REFRESH_CACHE_API_PATH = '/api/refresh-cache/{job-id}'
const spriteMap = Liferay.Icons.spritemap

const CheckRefreshStatus = () => {
    const {register, handleSubmit, reset, formState: {errors}} = useForm()
    const [jobStatus, setJobStatus] = useState("")
    const [alertType, setAlertType] = useState("")
    const resetState = useMemo(() => ({
        jobId: null
    }), [])

    const onSubmit = (data) => {
        const path = `${PULSE_REFRESH_CACHE_API_PATH.replace("{job-id}", data.jobId)}`
        getFetch(path)
            .then((response) => {
                setJobStatus(response.status)
                if (response.status === 'COMPLETE') {
                    setAlertType("success")
                } else if (response.status === 'SUBMITTED') {
                    setAlertType("info")
                } else if (response.status === 'EXCEPTION') {
                    setAlertType("danger")
                }
            })
            .catch((response) => {
                console.debug(response)
                let toastConfig
                if (response.status === 401) {
                    toastConfig = {
                        message: 'The bearer token has expired',
                        title: 'Error',
                        type: 'danger',
                        toastProps: {
                            autoClose: 1000,
                            className: 'alert-danger',
                            style: {top: 0}
                        }
                    }
                } else if (response.status === 404) {
                    setJobStatus(`${data.jobId} was not found`)
                    setAlertType("warning")
                    return;
                } else {
                    toastConfig = {
                        message: 'An error occurred when checking the job status',
                        title: 'Error',
                        type: 'danger',
                        toastProps: {
                            autoClose: 1000,
                            className: 'alert-danger',
                            style: {top: 0}
                        }
                    }
                }
                if (toastConfig)
                    Liferay.Util.openToast(toastConfig)
            })
    }

    const conditionalAlert = (jobStatus &&
        <ClayAlert displayType={alertType} spritemap={spriteMap} title={Liferay.Language.get(alertType)}>
            {jobStatus}
        </ClayAlert>
    )

    return (
        <ClayForm onSubmit={handleSubmit(onSubmit)}>
            <Sheet title={Liferay.Language.get('refresh-job-status')} footer={
                <ClayForm.Group className="btn-group-item">
                    <ClayButton displayType="primary" type="submit">
                        {Liferay.Language.get('check-status')}
                    </ClayButton>
                    <ClayButton displayType="secondary" type="button" onClick={() => reset(resetState)}>
                        {Liferay.Language.get('reset')}
                    </ClayButton>
                </ClayForm.Group>
            }>
                <ClayForm.Group>
                    <label htmlFor="jobId">{Liferay.Language.get('job-id')}
                        <RequiredMask/>
                    </label>
                    <ClayInput
                        type="text"
                        {...register("jobId", {required: true})}
                    />
                    {errors.name && <ClayForm.FeedbackItem>
                        <ClayForm.FeedbackIndicator
                            spritemap={spriteMap}
                            symbol="exclamation-full"
                        />
                        {"The job id is required."}
                    </ClayForm.FeedbackItem>}
                </ClayForm.Group>
                {conditionalAlert}
            </Sheet>
        </ClayForm>
    )
}

export default CheckRefreshStatus