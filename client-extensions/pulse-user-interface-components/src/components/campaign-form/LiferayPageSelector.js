import ClayForm, {ClaySelect} from '@clayui/form'
import {buildGraphQlQuery, parseGraphQlQueryResponse} from "../../common/utility"
import {useEffect, useState} from "react"
import baseFetch from "../../common/services/liferay/api"
import {Liferay} from "../../common/services/liferay/liferay"
import ClaySelectController from "../react-hook-form/ClaySelectController"
import {useTranslation} from "react-i18next"

const GRAPHQL_PATH = '/o/graphql'

const LiferayPageSelector = ({
                                 setValue,
                                 control,
                                 defaultValue,
                                 errors,
                                 spriteMap
                             }) => {
    const {t} = useTranslation()
    const controlName = "targetUrl"
    const [options, setOptions] = useState([]);

    useEffect(() => {
        const liferayPages = buildGraphQlQuery(
            'sitePages',
            'items { title, friendlyUrlPath }',
            {
                siteKey: `"${Liferay.ThemeDisplay.getSiteGroupId()}"`
            }
        )

        baseFetch(GRAPHQL_PATH, {
            method: 'POST',
            body: liferayPages
        }).then(
            (liferayPagesResponse) => {
                const {items} = parseGraphQlQueryResponse(
                    'sitePages',
                    liferayPagesResponse
                );
                if (items === undefined || !(items instanceof Array)) {
                    console.warn(t('notAnArray', {object: "items"}));
                    return;
                }
                console.debug(t('foundNOptions', {n: items.length}));
                setOptions(items);
            }
        ).catch((reason) => console.error(reason))
    }, [])

    useEffect(() => {
        if (options.length > 0) {
            if (defaultValue) {
                setValue(controlName, defaultValue)
                return
            }
            const value = options.at(0)?.friendlyUrlPath
            console.debug(t('default', {object: value}))
            setValue(controlName, value)
        }
    }, [options, defaultValue, setValue])

    return (
        <ClayForm.Group className={`${errors[controlName] ? "has-error" : ""}`}>
            <ClaySelectController
                name={controlName}
                label={t('targetUrl')}
                control={control}
                required={true}
            >
                {options.map(item => (
                    <ClaySelect.Option
                        key={item.friendlyUrlPath}
                        label={t('targetUrlOptionLabel', {k: item.friendlyUrlPath, v: item.title})}
                        value={item.friendlyUrlPath}
                    />
                ))}
            </ClaySelectController>
            {errors[controlName] && <ClayForm.FeedbackItem>
                <ClayForm.FeedbackIndicator
                    spritemap={spriteMap}
                    symbol="exclamation-full"
                />
                {t('controlNameInvalid', {controlName: controlName})}
            </ClayForm.FeedbackItem>}
        </ClayForm.Group>
    )
}

export default LiferayPageSelector