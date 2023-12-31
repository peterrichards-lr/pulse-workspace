import ClayForm from '@clayui/form'
import ClayButton from "@clayui/button"
import ClayAlert from '@clayui/alert'
import {Liferay} from "../../common/services/liferay/liferay"
import {useState} from "react"
import {useForm} from "react-hook-form"
import {postFetch} from "../../common/services/pulse/api"
import {useTranslation} from "react-i18next"

const PULSE_REFRESH_CACHE_API_PATH = '/api/refresh-cache'
const spriteMap = Liferay.Icons.spritemap

const RefreshCache = () => {
    const {t} = useTranslation()
    const {handleSubmit} = useForm()
    const [jobId, setJobId] = useState()

    const onSubmit = () => {
        postFetch(PULSE_REFRESH_CACHE_API_PATH)
            .then((response) => {
                console.log('response', response)
                let toastConfig = {
                    message: t('refreshCacheSuccessMessage'),
                    title: t('success'),
                    type: 'success',
                    toastProps: {
                        autoClose: 1000,
                        className: 'alert-success',
                        style: {top: 0}
                    }
                }
                const jobId = response?.jobId
                setJobId(jobId);
                Liferay.Util.openToast(toastConfig)
            }).catch((response) => {
            console.debug(response)
            let toastConfig
            if (response.status === 401) {
                toastConfig = {
                    message: t('unauthorizedMessage'),
                    title: t('unauthorizedTitle'),
                    type: 'danger',
                    toastProps: {
                        autoClose: 1000,
                        className: 'alert-danger',
                        style: {top: 0}
                    }
                }
            } else {
                toastConfig = {
                    message: t('refreshCacheErrorMessage'),
                    title: t('danger'),
                    type: 'danger',
                    toastProps: {
                        autoClose: 1000,
                        className: 'alert-danger',
                        style: {top: 0}
                    }
                }
            }
            Liferay.Util.openToast(toastConfig)
        })
    }

    return (
        <>
            <ClayForm onSubmit={handleSubmit(onSubmit)}>
                <ClayForm.Group>
                    <ClayButton displayType="primary" type="submit">
                        {t('refreshCacheSubmit')}
                    </ClayButton>
                </ClayForm.Group>
                {jobId &&
                    <ClayAlert displayType="success" spritemap={spriteMap}
                               title={t('refreshCacheRequestSubmitted')}>
                        {jobId}
                    </ClayAlert>
                }
            </ClayForm>
        </>
    )
}

export default RefreshCache