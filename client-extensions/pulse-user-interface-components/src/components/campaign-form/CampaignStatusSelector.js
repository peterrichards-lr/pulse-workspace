import ClayForm, {ClaySelect} from '@clayui/form'
import {buildGraphQlQuery, parseGraphQlQueryResponse} from "../../common/utility"
import {useEffect, useState} from "react"
import baseFetch from "../../common/services/liferay/api"
import ClaySelectController from "../react-hook-form/ClaySelectController"
import {useTranslation} from "react-i18next"

const GRAPHQL_PATH = '/o/graphql'

const CampaignStatusSelector = ({
                                    campaignStatusListTypeErc,
                                    setValue,
                                    control,
                                    defaultValue,
                                    errors,
                                    spriteMap
                                }) => {
    const {t} = useTranslation()
    const controlName = "campaignStatus"
    const [options, setOptions] = useState([])

    useEffect(() => {
        const campaignStatuses = buildGraphQlQuery(
            'listTypeDefinitionByExternalReferenceCode',
            'listTypeEntries { name, key }',
            {
                externalReferenceCode: `"${campaignStatusListTypeErc}"`
            }
        )

        baseFetch(GRAPHQL_PATH, {
            method: 'POST',
            body: campaignStatuses
        }).then(
            (campaignStatusesResponse) => {
                const {listTypeEntries} = parseGraphQlQueryResponse(
                    'listTypeDefinitionByExternalReferenceCode',
                    campaignStatusesResponse
                )
                if (listTypeEntries === undefined || !(listTypeEntries instanceof Array)) {
                    console.warn('listTypeEntries is not an array')
                    return
                }
                console.debug(`Found ${listTypeEntries.length} option(s)`)
                const filteredListTypeEntries = listTypeEntries.filter((status) => status.key !== 'complete' && status.key !== 'expired')
                setOptions(filteredListTypeEntries)
            }
        ).catch((reason) => console.error(reason))
    }, [campaignStatusListTypeErc]);

    useEffect(() => {
        if (options.length > 0) {
            if (defaultValue) {
                setValue(controlName, defaultValue)
                return
            }
            const value = options.at(0)?.key
            console.debug(`${t('campaignStatus')} default`, value)
            setValue(controlName, value)
        }
    }, [options, defaultValue, setValue])

    return (
        <ClayForm.Group className={`${errors[controlName] ? "has-error" : ""}`}>
            <ClaySelectController
                name={controlName}
                label={t('campaignStatus')}
                control={control}
                required={true}
            >
                {options.map(item => (
                    <ClaySelect.Option
                        key={item.key}
                        label={item.name}
                        value={item.key}
                    />
                ))}
            </ClaySelectController>
            {errors[controlName] && <ClayForm.FeedbackItem>
                <ClayForm.FeedbackIndicator
                    spritemap={spriteMap}
                    symbol="exclamation-full"
                />
                {`The ${controlName} is invalid.`}
            </ClayForm.FeedbackItem>}
        </ClayForm.Group>
    )
}

export default CampaignStatusSelector