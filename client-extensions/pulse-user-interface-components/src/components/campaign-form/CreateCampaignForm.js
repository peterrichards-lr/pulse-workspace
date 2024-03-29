import ClayForm from '@clayui/form'
import ClayButton from '@clayui/button'
import ClayAlert from '@clayui/alert';
import Sheet from "../common/Sheet"
import {Liferay} from "../../common/services/liferay/liferay"
import UtmDetails from "./UtmDetails"
import BasicDetails from "./BasicDetails"
import Lifecycle from "./Lifecycle"
import {useForm} from "react-hook-form"
import {postFetch} from "../../common/services/pulse/api"
import {useMemo, useState} from "react"
import {useTranslation} from "react-i18next"

const PULSE_REDIRECT_URL_PREFIX = process.env.REACT_APP_PULSE_REDIRECT_URL_PREFIX
const PULSE_CAMPAIGN_API_PATH = '/api/campaigns'
const spriteMap = Liferay.Icons.spritemap

const CreateCampaignForm = ({
                                campaignStatusListTypeErc,
                                defaultCampaignStatus
                            }) => {
    const {t} = useTranslation()
    campaignStatusListTypeErc = campaignStatusListTypeErc ? campaignStatusListTypeErc : '89201798-3fc2-6d45-d924-316395794c25'
    defaultCampaignStatus = defaultCampaignStatus ? defaultCampaignStatus : 'draft'
    console.debug('campaignStatusListTypeErc', campaignStatusListTypeErc)
    console.debug('defaultCampaignStatus', defaultCampaignStatus)

    const [pulseUrl, setPulseUrl] = useState();
    const {register, handleSubmit, control, setValue, reset, formState: {errors}} = useForm()
    const resetState = useMemo(() => ({
        name: null,
        description: null,
        campaignStatus: defaultCampaignStatus,
        startDate: null,
        endDate: null,
        utmContent: null,
        utmMedium: null,
        utmSource: null,
        utmTerm: null,
    }), [defaultCampaignStatus])

    const onSubmit = (data) => {
        console.debug('data', data)
        postFetch(PULSE_CAMPAIGN_API_PATH, data)
            .then((response) => {
                let toastConfig = {
                    message: t('createCampaignSuccessMessage'),
                    title:  t('createCampaignSuccessTitle'),
                    type: 'success',
                    toastProps: {
                        autoClose: 1000,
                        className: 'alert-success',
                        style: {top: 0}
                    }
                }
                console.debug(response)
                setPulseUrl(PULSE_REDIRECT_URL_PREFIX + response.token);
                Liferay.Util.openToast(toastConfig)
            }).catch((response) => {
            console.debug(response)
            let toastConfig
            if (response.status === 400) {
                response.json().then((reason => {
                    console.debug(reason)
                    const toastConfig = {
                        message: reason.message,
                        title: t('createCampaignBadRequestTitle'),
                        type: 'warning',
                        toastProps: {
                            autoClose: 1000,
                            className: 'alert-success',
                            style: {top: 0}
                        }
                    }
                    reset(resetState)
                    Liferay.Util.openToast(toastConfig)
                }))
                return
            } else if (response.status === 401) {
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
                    message: t('createCampaignErrorMessage'),
                    title: t('createCampaignErrorTitle'),
                    type: 'danger',
                    toastProps: {
                        autoClose: 1000,
                        className: 'alert-danger',
                        style: {top: 0}
                    }
                }
            }
            reset(resetState)
            Liferay.Util.openToast(toastConfig)
        })
    }

    return (
        <div className="campaign-information">
            <ClayForm onSubmit={handleSubmit(onSubmit)}>
                <Sheet title={t('createCampaignHeading')} footer={
                    <ClayForm.Group className="btn-group-item">
                        <ClayButton displayType="primary" type="submit">
                            {t('createCampaignSubmit')}
                        </ClayButton>
                        <ClayButton displayType="secondary" type="button" onClick={() => reset(resetState)}>
                            {t('createCampaignReset')}
                        </ClayButton>
                    </ClayForm.Group>
                }>
                    <BasicDetails
                        register={register}
                        control={control}
                        setValue={setValue}
                        defaultCampaignStatus={defaultCampaignStatus}
                        campaignStatusListTypeErc={campaignStatusListTypeErc}
                        spriteMap={spriteMap}
                        errors={errors}
                    />
                    <Lifecycle
                        control={control}
                        spriteMap={spriteMap}
                        errors={errors}
                    />
                    <UtmDetails
                        register={register}
                        spriteMap={spriteMap}
                        errors={errors}
                    />
                    {pulseUrl &&
                        <ClayAlert displayType="success" spritemap={spriteMap}
                                   title={t('pulseUrl')}>
                            <a href={pulseUrl} target="_blank">{pulseUrl}</a>
                        </ClayAlert>
                    }
                </Sheet>
            </ClayForm>
        </div>
    )
}

export default CreateCampaignForm